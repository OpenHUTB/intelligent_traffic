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
 * 接收到消息的回调方法
 */
websocket.onmessage = function(result){
    result = JSON.parse(data);
    var data = result.data;
    var sound = data.sound;
    var graph = data.graph;
    if(sound!=null){
        recorder.play(audio, ctx)
        recorder.draw(ctx)
    }
}