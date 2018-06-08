package stock.market

class Company {
    String companyName
    String ticker

    static hasMany = [userStocks:UserStock]

    static constraints = {
    }

    static mapping = {
        id generator: 'assigned', name: 'ticker'
    }
}
