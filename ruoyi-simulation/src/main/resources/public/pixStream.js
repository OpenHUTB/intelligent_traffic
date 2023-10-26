$(function(){
    var ws = connect();
    var interval = setInterval(function() {
        if (ws.readyState == 2 || ws.readyState == 3) {
            ws = connect();
        }
    },500);
});