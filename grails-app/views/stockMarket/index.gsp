<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:javascript src="stockMarket.js"/>
    <title>Stock Market</title>
</head>
<body>
<input id="userId" type="hidden" value="${user.id}"/>
<g:form controller="logoff">
    <div id="headerDiv"><h6 style="display:inline">Welcome ${user.username}</h6>
        <div style="float:right"><input id="logout" type="submit" class="btn btn-danger btn-xs" value="Logout"></div>
    </div>
</g:form><br/>

<!-- Select Stock and Stock Info -->
<div id="stockDiv" style="text-align:center">
    <div>
        <select id="companyList" class="select2" onChange="javascript:getStockInfo()">
            <option></option>
            <g:each in="${companyList}" var="company">
                <option value="${company.ticker}">${company.ticker} - ${company.companyName}</option>
            </g:each>
        </select>
    </div><br/>
    <div id="stockInfoDiv"></div>
</div>

<!-- User Balance and Stocks -->
<div id="balanceDiv">
    <table class="table custTable custTable2">
        <thead>
            <th style="font-weight:bold">Balance</th>
            <th id="userBalance"><g:formatNumber number="${user.balance}" type="currency" currencyCode="USD" /></th>
            <th><a href="#addFundsModal" data-toggle="modal"><input type="button" class="btn btn-info btn-xs" value="Add Funds"></a></th>
            <th><a href="#withdrawFundsModal" data-toggle="modal"><input type="button" class="btn btn-danger btn-xs" value="Withdraw Funds"></a></th>
        </thead>
    </table>
</div>
<div id="userStockDiv" style="text-align:center">
    <g:if test="${!userStocks.isEmpty()}">
        <g:render template='userStock' model="[userStocks:userStocks, tickerMap:tickerMap, user:user]"/>
    </g:if>
</div>

<!-- Add Funds Modal -->
<div id="addFundsModal" class="modal fade">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Add Funds</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                    <h5>Enter Amount of Funds to Add:</h5>
                    <div class="form-group">
                        <input id="amountFundsAdd" class="form-control" required="true"><br>
                        <div style="text-align:center">
                            <input id="addFunds" onClick="javascript:addFunds()"  type="button" class="btn btn-primary btn-lg" value="Add Funds">
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Funds Modal -->
<div id="withdrawFundsModal" class="modal fade">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Withdraw Funds</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <h5>Enter Amount of Funds to Withdraw:</h5>
                <div class="form-group">
                    <input id="amountFundsWithdraw" class="form-control" required="true"><br>
                    <div style="text-align:center">
                        <input id="withdrawFunds" onClick="javascript:withdrawFunds()" type="button" class="btn btn-primary btn-lg" value="Withdraw Funds">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Buy Stock Modal -->
<div id="buyStockModal" class="modal fade">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header">
                <h4 id="buyStockTitle" class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <h5 id="buyStockHeader"></h5>
                <div class="form-group">
                    <input id="buyStockAmount" class="form-control" required="true"><br>
                    <div style="text-align:center">
                        <input id="buyStockModalBtn" onClick="javascript:buyStock()" type="button" class="btn btn-primary btn-lg" value="Buy Stock">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Sell Stock Modal -->
<div id="sellStockModal" class="modal fade">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header">
                <h4 id="sellStockTitle" class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <h5 id="sellStockHeader"></h5>
                <div class="form-group">
                    <input id="sellStockAmount" class="form-control" required="true"><br>
                    <div style="text-align:center">
                        <input id="sellStockModalBtn" type="button" class="btn btn-primary btn-lg" value="Sell Stock">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>