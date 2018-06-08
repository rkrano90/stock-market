package stock.market

import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestBuilder

@Transactional
class IEXTradingAPIService {

    String baseUrl = "https://api.iextrading.com/1.0"

    public def getTickers() {
        def resp = new RestBuilder().get("${baseUrl}/ref-data/symbols"){}
        return resp
    }

    public def getStockInfo(String ticker) {
        def resp = new RestBuilder().get("${baseUrl}/stock/${ticker}/quote"){}
        return resp
    }

    public def getMultipleStockQuotes(String tickers){
        def resp = new RestBuilder().get("${baseUrl}/stock/market/quote?symbols=${tickers}"){}
        return resp
    }
}
