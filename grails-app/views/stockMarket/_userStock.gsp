<br/>
<div id="tableWrapper" class="innerDiv">
<table id="userStockTable" class="table custTable">
    <thead>
        <th>Ticker</th>
        <th>Share #</th>
        <th>Value (Profit/Loss)</th>
        <th>Sell</th>
    </thead>
    <tbody>
        <g:set var="totalValue" value="${user.balance}" />
        <g:each in="${userStocks}" var="stock">
            <g:set var="stockValue" value="${stock.amount * tickerMap.get(stock.ticker).get('latestPrice')}"/>
            <g:set var="totalValue" value="${totalValue += stockValue}" />
            <g:set var="currentProfit" value="${(stock.amount * tickerMap.get(stock.ticker).get('latestPrice')) - (stock.amount * stock.averagePrice)}"/>
            <tr>
                <td class="customTD">${stock.ticker}</td>
                <td class="customTD">${stock.amount}</td>
                <td class="customTD"><g:formatNumber number="${stockValue}" type="currency" currencyCode="USD" />
                    <g:if test="${stock.realizedGainOrLoss + currentProfit > 0}">
                        (<span style="color:green"><i class="fa fa-arrow-up"></i> <g:formatNumber number="${stock.realizedGainOrLoss + currentProfit}" type="currency" currencyCode="USD" /></span>)
                    </g:if>
                    <g:elseif test="${stock.realizedGainOrLoss + currentProfit < 0}">
                        (<span style="color:red"><i class="fa fa-arrow-down"></i> <g:formatNumber number="${stock.realizedGainOrLoss + currentProfit}" format="\$#,###.00" /></span>)
                    </g:elseif>
                    <g:else>
                        (<g:formatNumber number="${stock.realizedGainOrLoss + currentProfit}" type="currency" currencyCode="USD" />)
                    </g:else>
                </td>
                <td>
                    <input id="sellStockBtn" onClick="javascript:getSellStockModal('${stock.ticker}')" type="button" class="btn btn-danger btn-xs" value="Sell ${stock.ticker}"><br/>
                    <span style="font-weight:bold">Latest Stock Price:</span><br/>
                    <g:formatNumber number="${tickerMap.get(stock.ticker).get('latestPrice')}" type="currency" currencyCode="USD" /> updated at
                    <g:formatDate date="${new Date(tickerMap.get(stock.ticker).get('latestUpdate'))}" format="dd-MM-yyyy HH:mm:ss z" />
                </td>
            </tr>
        </g:each>
    </tbody>
</table>
</div>

<div class="innerDiv">
<table id="portfolioValueTable" class="table custTable">
    <thead>
        <th style="font-weight:bold">Portfolio Value:</th>
        <th id="totalValueCell"><g:formatNumber number="${totalValue}" type="currency" currencyCode="USD" /></th>
    </thead>
</table>
</div>
