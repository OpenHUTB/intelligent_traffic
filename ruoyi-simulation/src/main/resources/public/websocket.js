var socketURL = "ws:"+window.location.hostname+":"+window.location.port+"/simulation/websocket/";
var websocket = new WebSocket(socketURL);
/**
 * 连接建立成功的回调方法
 */
websocket.onopen = function(event){
    console.log("连接已建立");
}
/**
 * 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接
 */
window.onbeforeunload = function(){
    websocket.close();
}
/**
 * 接收到消息的回调方法
 */
websocket.onmessage = function(resultData){
    var result = JSON.parse(resultData.data);
    var streamSet = result.data;
    var sound = streamSet.sound;
    var graph = streamSet.graph;
    var message = streamSet.message;
    if(sound!=null){
        var blob = new Blob([sound]);
        var url = window.URL.createObjectURL(blob);

    }
    if(graph!=null){
        var blob = new Blob([graph]);
        var url = window.URL.createObjectURL(blob);
        var screen = document.getElementById("screen");
        screen.src=url;
    }
    if(message!=null){
        var tips = document.getElementById("tips");
        tips.innerHTML=tips.innerHTML+"<br/>"+message;
    }
}