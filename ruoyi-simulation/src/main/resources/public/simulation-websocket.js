var socketURL = "ws:"+window.location.hostname+":"+window.location.port+"/simulation/websocket/";
var simulation = new WebSocket(socketURL);
var digital_simulation = false;
/**
 * 连接建立成功的回调方法
 */
simulation.onopen = function(event){
    console.log("----------------------------simulation连接已建立----------------------------");
}
/**
 * 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接
 */
window.onbeforeunload = function(){
    simulation.close();
}
/**
 * 接收到消息的回调方法
 */
simulation.onmessage = function(resultData){
    //console.log("----------------------------simulation收到消息-----------------------------");
    var result = JSON.parse(resultData.data);
    var message = result.message;
    var sound = result.sound;
    var signal = result.signal;
    //播放音频
    playAudio(sound);
    if(message!=null&&message!=""){
        var tips = document.getElementById("tips");
        tips.value=message;
        getVoice(message);
    }
    if(signal=="DIGITAL_SIMULATION"){
        twinPlay(true);
        console.log("video display call");
        digital_simulation = true;
    }
    if(signal=="TRAFFIC_INDIRECTION"){
        var indirectionMap = result.data;
        setTrafficIndirection(indirectionMap);
    }else if(signal=="JUNCTION_CONTROL"){
        sessionStorage['trafficData'] = JSON.stringify(result.data);
        window.location.hash="/junction";
        setTimeout(function(){
            lightControlDataChanged(result.data);
        },"500");
    }
}
/**
 * 播放音频
 * params 音频文件在服务器中的文件名
 */
function playAudio(sound,callback){
    if(sound==null){
        return;
    }
    updateAnimation(1);
    var audioArea = document.getElementById("audioArea");
    audioArea.innerHTML="";
    var audio = document.createElement("audio");
    audioArea.appendChild(audio);
    audio.autoplay = "autoplay";
    audio.src="simulation/file/stream?filename="+sound;
    audio.play();
    audio.addEventListener("ended",function(){
        var introductionArea = document.getElementById("introductionArea");
        if(callback!=null){
            callback();
        }
        setTimeout(function(){
            updateAnimation(0);
        },1000);
    });
}
