var websocket = new WebSocket("ws://localhost:8080/simulation/websocket/");
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
 * 发送消息
 * 如："创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1"
 */
function send(){
    var message = $("[name='command']").val();
    websocket.send(message);
    $("[name='command']").val("");
}
/**
 * 接收到消息的回调方法
 */
websocket.onmessage = function(e){
    var message = e.data;
    $(".area").append("<p>"+message+"</p>");
}