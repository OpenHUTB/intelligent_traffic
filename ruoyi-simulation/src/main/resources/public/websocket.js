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
    console.log("----------------------------收到消息-----------------------------");
    var result = JSON.parse(resultData.data);
    console.log(result);
    var message = result.msg;
    var streamSet = result.data;
    var sound = streamSet.sound;
    var graph = streamSet.graph;
    if(sound!=null){
        var audioArea = document.getElementById("audioArea");
        var audio = document.createElement("audio");
        audioArea.appendChild(audio);
        audio.autoplay = "autoplay";
        audio.src="simulation/file/stream?filename="+sound;
        audio.play();
        draw(audio);
    }
    if(graph!=null){
        var introductionArea = document.getElementById("introductionArea");
        var video = document.createElement("video");
        introductionArea.appendChild(video);
        video.autoplay = "autoplay";
        video.src="simulation/file/stream?filename="+graph;
        video.play();
        video(audio);
    }
    if(screen!=null){
        var screen = document.getElementById("screen");
        var blob = new Blob([graph]);
        var url = window.URL.createObjectURL(blob);
        screen.src=url;
        screen.play();
    }
    if(message!=null){
        var tips = document.getElementById("tips");
        if(tips.value==null||tips.value==""){
            tips.value=message;
        }else{
            tips.value=tips.value+"\n"+message;
        }
    }
}