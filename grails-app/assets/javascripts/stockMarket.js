$( document ).ready(function() {
    $('.select2').select2({
        placeholder:'Select A Ticker...',
        width:'resolve',
        theme: "classic",
        minimumInputLength:"2"
    });

    setInterval(function(){
        refreshUserInfo();
    }, 30000);
});

function getStockInfo() {
    $.ajax({
        type: 'POST',
        async: true,
        data: {'ticker': $('#companyList').val()},
        url: "/stockMarket/getStockInfo",
        success: function (data) {
            $('#stockInfoDiv').empty().append(data);
        }
    });
}

function addFunds(){
    if(!$('#amountFundsAdd').val().match(/^((\d{0,3}(,\d{3})+)|\d+)(\.\d{2})?$/) || $('#amountFundsAdd').val() <= 0){
        alert("Invalid Amount Entered");
        return;
    }
    $.ajax({
        type: 'POST',
        async: true,
        data: {'userId': $('#userId').val(), 'amount': $('#amountFundsAdd').val()},
        url: "/stockMarket/addFunds",
        success: function (data) {
            $('#userBalance').empty().append(data);
            if($('#totalValueCell').length) {
                var newTotal = parseFloat($('#totalValueCell').text().replace(/[^0-9.]/g,'')) + parseFloat($('#amountFundsAdd').val());
                var newTotalCurr = '$' + parseFloat(newTotal).toFixed(2).replace(/(\d)(?=(\d{3})+$)/g, "$1,");
                $('#totalValueCell').empty().append(newTotalCurr);
            }
            $('#addFundsModal').modal('hide');
            clearModals();
        }
    });
}

function withdrawFunds(){
    if(!$('#amountFundsWithdraw').val().match(/^((\d{0,3}(,\d{3})+)|\d+)(\.\d{2})?$/) || $('#amountFundsWithdraw').val() <= 0){
        alert("Invalid Amount Entered");
        return;
    }

    if($('#amountFundsWithdraw').val() > parseFloat($('#userBalance').text().replace(/[^0-9.]/g,''))){
        alert("Not Enough Funds To Withdraw");
        return;
    }
    $.ajax({
        type: 'POST',
        async: true,
        data: {'userId': $('#userId').val(), 'amount': $('#amountFundsWithdraw').val()},
        url: "/stockMarket/withdrawFunds",
        success: function (data) {
            $('#userBalance').empty().append(data);
            if($('#totalValueCell').length) {
                var newTotal = parseFloat($('#totalValueCell').text().replace(/[^0-9.]/g,'')) - parseFloat($('#amountFundsWithdraw').val());
                var newTotalCurr = '$' + parseFloat(newTotal).toFixed(2).replace(/(\d)(?=(\d{3})+$)/g, "$1,");
                $('#totalValueCell').empty().append(newTotalCurr);
            }
            $('#withdrawFundsModal').modal('hide');
            clearModals();
        }
    });
}

function getBuyStockModal(){
    var ticker = $('#companyList').val();
    $('#buyStockTitle').text("Buy " + ticker);
    $('#buyStockHeader').text("Enter amount of " + ticker + " to buy:")
    $('#buyStockModal').modal('show');
}

function buyStock(){
    if(!$('#buyStockAmount').val().match(/^((\d{0,3}(,\d{3})+)|\d+)$/) || $('#buyStockAmount').val() <= 0){
        alert("Invalid Amount Entered");
        return;
    }
    $.ajax({
        type: 'POST',
        async: true,
        data: {'userId': $('#userId').val(), 'amount': $('#buyStockAmount').val(), 'ticker': $('#companyList').val()},
        url: "/stockMarket/buyStock",
        success: function (data) {
            if(data.error){
                alert(data.error);
                return;
            }
            $('#userBalance').empty().append(data.balance);
            $('#userStockDiv').empty().append(data.userStockTemplate)
            $('#buyStockModal').modal('hide');
            clearModals();
        }
    });
}

function getSellStockModal(ticker){
    $('#sellStockTitle').text("Sell " + ticker);
    $('#sellStockHeader').text("Enter amount of " + ticker + " to sell:")
    $('#sellStockModal').modal('show');
    $('#sellStockModalBtn').attr('onclick', 'sellStock("' + ticker + '")');
}

function sellStock(ticker){
    if(!$('#sellStockAmount').val().match(/^((\d{0,3}(,\d{3})+)|\d+)$/) || $('#sellStockAmount').val() <= 0){
        alert("Invalid Amount Entered");
        return;
    }
    $.ajax({
        type: 'POST',
        async: true,
        data: {'userId': $('#userId').val(), 'amount': $('#sellStockAmount').val(), 'ticker': ticker},
        url: "/stockMarket/sellStock",
        success: function (data) {
            if(data.error){
                alert(data.error);
                return;
            }
            $('#userBalance').empty().append(data.balance);
            $('#userStockDiv').empty().append(data.userStockTemplate)
            $('#sellStockModal').modal('hide');
            clearModals();
        }
    });
}

function refreshUserInfo(){
    if($('#userId').length == 0 || $('#userId').val() == ''){
        return;
    }
    $.ajax({
        type: 'POST',
        async: true,
        data: {'userId': $('#userId').val()},
        url: "/stockMarket/scheduledRefresh",
        success: function (data) {
            $('#userBalance').empty().append(data.balance);
            $('#userStockDiv').empty().append(data.userStockTemplate);
        }
    });
}

function clearModals(){
    $('#buyStockAmount').val('');
    $('#sellStockAmount').val('');
    $('#amountFundsAdd').val('');
    $('#amountFundsWithdraw').val('');
}
