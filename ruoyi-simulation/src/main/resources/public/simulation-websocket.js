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
    console.log("----------------------------simulation收到消息-----------------------------");
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
    console.log(signal);
    if(signal=="TRAFFIC_INDIRECTION"){
        console.log(JSON.stringify(result));
    }else if(signal=="TRAFFIC_LIGHT_INSTRUCTION"){
        if(window.location.href.indexOf("#/junction")==-1){
            window.location.hash="#/junction";
        }
        var lightInfo = result.lightInfo;
        var serial = parseInt(lightInfo.serial);
        var second = parseInt(lightInfo.second);
        var carCount = parseInt(lightInfo.carCount);
        var roadLength = parseInt(lightInfo.roadLength);
        var congestion = parseInt(lightInfo.congestion);
        var waitTime = parseInt(lightInfo.waitTime);
        setTimeout(function(){
            updateLight(serial, second, true);
            console.log(carCount, roadLength, congestion, waitTime);

           const newNumbers = [carCount, roadLength, congestion, waitTime];

           const elements = document.querySelectorAll('.number');
           elements.forEach((el, index) => {
               if (index < newNumbers.length) {
                   el.childNodes.forEach(node => {
                       if (node.nodeType === 3) {
                           const text = node.textContent.replace(/\s/g, '');
                           if (text === '0') {
                               node.textContent = node.textContent.replace('0', newNumbers[index].toString());
                           }
                       }
                   });
               }
           });
        },1500);
    }else if(signal!=null&&window.location.href.indexOf("#/junction")!=-1){
        window.location.hash="";
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
