<g:set var="lastPrice" value="${stockInfo.get('latestPrice')}"/>
<g:set var="openPrice" value="${stockInfo.get('open')}"/>
<table id="stockInfoTable" class="custTable">
    <tbody>
    <tr>
        <td><span class="colHead">Daily High:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('high')}" type="currency" currencyCode="USD" /></span>
        </td>
        <td><span class="colHead">Daily Low:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('low')}" type="currency" currencyCode="USD" /></span>
        </td>
        <td><span class="colHead">Open:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('open')}" type="currency" currencyCode="USD" /> at <g:formatDate date="${new Date(stockInfo.get('openTime'))}" format="dd-MM-yyyy HH:mm:ss z" /></span>
        </td>
        <td style="border-left:1px solid #DFDFDF"><span class="colHead">Last Price:</span>
            <span <g:if test="${lastPrice > openPrice}">class="colInfo greenTxt"</g:if><g:else>class="colInfo redTxt"</g:else>><g:formatNumber number="${stockInfo.get('latestPrice')}" type="currency" currencyCode="USD" /></span>
        </td>
        <td><span class="colHead">Daily Volume:</span> <span class="colInfo">${stockInfo.get('latestVolume')}</span></td>
        <td rowspan="2" style="border-left:1px solid #DFDFDF;vertical-align:middle;text-align:center"><input id="buyStockBtn" onClick="javascript:getBuyStockModal()" type="button" class="btn btn-primary" value="Buy"></td>
    </tr>
    <tr>
        <td><span class="colHead">52W High:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('week52High')}" type="currency" currencyCode="USD" /></span>
        </td>
        <td><span class="colHead">52W Low:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('week52Low')}" type="currency" currencyCode="USD" /></span>
        </td>
        <td><span class="colHead">Close:</span>
            <span class="colInfo"><g:formatNumber number="${stockInfo.get('close')}" type="currency" currencyCode="USD" /> at <g:formatDate date="${new Date(stockInfo.get('closeTime'))}" format="dd-MM-yyyy HH:mm:ss z" /></span>
        </td>
        <td colspan="2" style="border-left:1px solid #DFDFDF;text-align:center"><span class="colInfo"><g:formatDate date="${new Date(stockInfo.get('latestUpdate'))}" format="dd-MM-yyyy HH:mm:ss z" /></span></td>
    </tr>
    </tbody>
</table>