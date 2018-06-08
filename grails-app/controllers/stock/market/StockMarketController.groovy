package stock.market

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import stock.market.StockTransaction.TransactionType

import groovy.json.JsonSlurper

import java.text.DecimalFormat

class StockMarketController {

    SpringSecurityService springSecurityService
    def iexService = new IEXTradingAPIService()
    def slurper = new JsonSlurper()

    def index() {
        def user = springSecurityService.currentUser
        def companyList = Company.findAll("from Company order by ticker")
        def userStocks = UserStock.findAllByUserId(user.id, [sort:'amount', order:'desc'])
        def tickerMap
        if(userStocks) {
            tickerMap = getTickerMap(userStocks)
        }

        [user:user, companyList:companyList, tickerMap:tickerMap, userStocks:userStocks]
    }

    def getStockInfo = {
        def stockInfoResp = iexService.getStockInfo(params.ticker)
        HashMap<String,String> stockInfoMap = slurper.parseText((stockInfoResp.json).toString())

        render(template:"stockInfo", model:[stockInfo:stockInfoMap])
    }

    def addFunds = {
        def userId = new Long(params.userId)
        def amount = new Double(params.amount)
        def user = User.findById(userId)
        user.balance += amount
        user.save(flush:true, failOnError:true)
        render(template:"userBalance", model:[balance:user.balance])
    }

    def withdrawFunds = {
        def userId = new Long(params.userId)
        def amount = new Double(params.amount)
        def user = User.findById(userId)
        user.balance -= amount
        user.save(flush:true, failOnError:true)
        render(template:"userBalance", model:[balance:user.balance])
    }

    def buyStock = {
        def userId = new Long(params.userId)
        def amount = new Long(params.amount)
        def user = User.findById(userId)
        def stockInfoResp = iexService.getStockInfo(params.ticker)
        HashMap<String,String> stockInfoMap = slurper.parseText((stockInfoResp.json).toString())

        def price = new Double(stockInfoMap.get('latestPrice'))
        def totalValue = (Double)amount*price
        DecimalFormat dec = new DecimalFormat("\$#,###.00");
        if((Double)amount*price > user.balance){
            Map responseData = [error: "Not enough funds to buy ${amount} ${params.ticker} at ${dec.format(price)} (${dec.format(totalValue)})"]
            render(responseData as JSON)
            return
        }

        user.balance -= (Double)price*amount
        user.save(flush:true, failOnError:true)

        def userStock = UserStock.findOrCreateByUserIdAndTicker(userId, params.ticker)
        if(userStock.amount == 0){
            userStock.userId = userId
            userStock.amount = amount
            userStock.ticker = params.ticker
            userStock.averagePrice = price
            def company = Company.findByTicker(params.ticker)
            userStock.company = company
        }
        else{
            userStock.averagePrice = (Double)(userStock.amount*userStock.averagePrice + amount*price) / (Double)(userStock.amount + amount)
            userStock.amount += amount
        }
        userStock.save(flush: true, failOnError:true)

        StockTransaction trans = new StockTransaction()
        trans.userStockId = userStock.id
        trans.transactionType = TransactionType.BUY
        trans.price = price
        trans.amount = amount
        trans.transactionDate = new Date()
        trans.save(flush:true, failOnError:true)

        def allStocks = UserStock.findAllByUserId(userId, [sort:'amount', order:'desc'])
        def tickerMap = getTickerMap(allStocks)

        String htmlContent = g.render([template:'userStock', model:[userStocks:allStocks, tickerMap:tickerMap, user:user]])
        Map responseData = [userStockTemplate: htmlContent, balance: dec.format(user.balance)]

        render(responseData as JSON)
    }

    def sellStock = {
        def userId = new Long(params.userId)
        def amount = new Long(params.amount)
        def user = User.findById(userId)
        def stockInfoResp = iexService.getStockInfo(params.ticker)
        HashMap<String,String> stockInfoMap = slurper.parseText((stockInfoResp.json).toString())

        def userStock = UserStock.findByUserIdAndTicker(userId, params.ticker)

        def price = new Double(stockInfoMap.get('latestPrice'))
        if(amount > userStock.amount){
            Map responseData = [error: "Not enough shares to sell ${amount} ${params.ticker}"]
            render(responseData as JSON)
            return
        }

        def totalValue = (Double)price*amount
        def profitOrLoss = totalValue - (Double)(amount * userStock.averagePrice)

        userStock.realizedGainOrLoss += profitOrLoss
        userStock.amount -= amount
        userStock.save(flush:true, failOnError:true)

        user.balance += totalValue
        user.save(flush:true, failOnError:true)

        StockTransaction trans = new StockTransaction()
        trans.userStockId = userStock.id
        trans.transactionType = TransactionType.SELL
        trans.price = price
        trans.amount = amount
        trans.transactionDate = new Date()
        trans.profitOrLoss = profitOrLoss
        trans.save(flush:true, failOnError:true)

        def allStocks = UserStock.findAllByUserId(userId, [sort:'amount', order:'desc'])
        String htmlContent = ""
        if(allStocks) {
            def tickerMap = getTickerMap(allStocks)
            htmlContent = g.render([template:'userStock', model:[userStocks:allStocks, tickerMap:tickerMap, user:user]])
        }

        DecimalFormat dec = new DecimalFormat("\$#,###.00");
        Map responseData = [userStockTemplate: htmlContent, balance: dec.format(user.balance)]

        render(responseData as JSON)
    }

    def getTickerMap(def allStocks){
        StringBuilder sb = new StringBuilder()
        for(UserStock stock : allStocks){
            sb.append(stock.ticker)
            sb.append(',')
        }
        String allTickers = sb.substring(0,sb.length()-1)

        def multipleStockQuoteResp = iexService.getMultipleStockQuotes(allTickers)
        if(allStocks.size() == 1){
            HashMap<String, String> innerMap = slurper.parseText((multipleStockQuoteResp.json).toString())
            HashMap<String, HashMap<String,String>> outerMap = new HashMap<String, HashMap<String,String>>()
            outerMap.put(allStocks.get(0).ticker, innerMap)
            return outerMap
        }
        ArrayList<HashMap<String,String>> tickerList = slurper.parseText((multipleStockQuoteResp.json).toString())
        HashMap<String, HashMap<String,String>> tickerMap = new HashMap<String, HashMap<String, String>>()
        for(int i = 0; i<tickerList.size(); i++){
            tickerMap.put(tickerList[i].get('symbol'), tickerList[i])
        }

        return tickerMap
    }

    def scheduledRefresh = {
        Long userId = new Long(params.userId)
        def user = User.findById(userId)
        def userStocks = UserStock.findAllByUserId(user.id, [sort:'amount', order:'desc'])
        def htmlContent
        if(userStocks) {
            def tickerMap = getTickerMap(userStocks)
            htmlContent = g.render([template:'userStock', model:[userStocks:userStocks, tickerMap:tickerMap, user:user]])
        }

        DecimalFormat dec = new DecimalFormat("\$#,###.00");
        Map responseData = [userStockTemplate: htmlContent, balance: dec.format(user.balance)]

        render(responseData as JSON)
    }
}
