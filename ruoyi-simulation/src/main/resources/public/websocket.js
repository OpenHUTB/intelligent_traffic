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
    var screen = streamSet.screen;
    var sound = streamSet.sound;
    var graph = streamSet.graph;
    var progress = streamSet.progress;
    if(sound!=null){
        var audioArea = document.getElementById("audioArea");
        audioArea.innerHTML="";
        var audio = document.createElement("audio");
        audioArea.appendChild(audio);
        audio.autoplay = "autoplay";
        audio.src="simulation/file/stream?filename="+sound;
        audio.play();
        draw(audio);
        audio.addEventListener("ended",function(){
             var introductionArea = document.getElementById("introductionArea");
             introductionArea.innerHTML="";
        });
    }
    if(graph!=null){
        var introductionArea = document.getElementById("introductionArea");
        introductionArea.innerHTML="";
        var video = document.createElement("video");
        introductionArea.appendChild(video);
        video.autoplay = "autoplay";
        video.muted="muted"
        video.src="simulation/file/stream?filename="+graph;
        video.addEventListener("canplay",function(e){
            video.play();
        });
        video.addEventListener("ended",function(e){
            video.remove();
        });
    }
    if(progress!=null){
        //设置进度条值
        $("#progressArea .progress-bar").css({"width": progress+"%"});
    }
    if(screen!=null){
        connect();
    }
    if(message!=null&&message!=""){
        var tips = document.getElementById("tips");
        if(tips.value==null||tips.value==""){
            tips.value=message;
        }else{
            tips.value=tips.value+"\n"+message;
        }
        tips.scrollTop = tips.scrollHeight;
    }
}