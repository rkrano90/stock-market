package stock.market

import groovy.sql.Sql
import groovy.json.JsonSlurper

class BootStrap {

    def dataSource
    def init = { servletContext ->
        Sql sql = new Sql(dataSource)
        sql.execute("create sequence if not exists USER_ID_SEQ start with 1000 increment by 1")
        sql.execute("create sequence if not exists USER_STOCK_SEQ start with 1000 increment by 1")
        sql.execute("create sequence if not exists STOCK_TRANSACTION_SEQ start with 1000 increment by 1")

        /*
         * Calls API to get tickers at boot up since API call takes too long.
         */
        def slurper = new JsonSlurper()
        IEXTradingAPIService iexService = new IEXTradingAPIService()
        def tickerResp = iexService.getTickers()
        LinkedList<HashMap<String,String>> tickerList = slurper.parseText((tickerResp.json).toString())
        for(def ticker : tickerList){
            def company = new Company()
            company.companyName = ticker.get('name')
            company.ticker = ticker.get('symbol')
            if(company.ticker && company.companyName) {
                company.save(flush: false)
            }
        }
    }
    def destroy = {
    }
}
