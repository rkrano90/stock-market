package stock.market

class StockTransaction implements Serializable{
    enum TransactionType{
        BUY, SELL
    }

    long userStockId
    Date transactionDate
    double price
    long amount
    double profitOrLoss
    TransactionType transactionType

    static constraints = {
        profitOrLoss nullable: true
    }

    static mapping = {
    }
}
