package stock.market

class UserStock implements Serializable{
    long userId
    String ticker
    long amount = 0
    double averagePrice
    double realizedGainOrLoss = 0


    static hasMany = [stockTransactions:StockTransaction]
    static hasOne = [company:Company]

    static constraints = {

    }

    static mapping = {

    }
}
