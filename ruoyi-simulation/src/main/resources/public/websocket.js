var websocket = new WebSocket("ws://localhost:8080/simulation/websocket/");
var audioData = [];
var recording = false;
var mediaRecorder = null;
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
websocket.onmessage = function(e){
    var data = e.data;
    const buffer = new Float32Array(data.sound);
    const arrayBuffer = audioCtx.createBuffer(1,4096,48000);
    const channelData = arrayBuffer.getChannelData(0);
    for(let i=0;i<4096;i++){
        channelData[i] = buffer[i];
    }
    const source = audioCtx.createBufferSource();
    source.buffer = arrayBuffer;
    const gainNode = audioCtx.createGain();
    source.connect(gainNode);
    gainNode.connect(audioCtx.destination);
    gainNode.gain.setValueAtTime(1, audioCtx.currentTime);
    source.start();
}
/**
 * 发送消息
 * 如："创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1"
 */
function sendVoice(){
    recording==false;
    mediaRecorder.stop();
    setTimeout(function(){
        var blob = new Blob(audioData, {"type":"audio/webm;codecs=opus"});
        websocket.send(blob);
    },5000);
}
/**
 * 记录语音
 *
 */
function recordVoice(){
    window.navigator.mediaDevices.getUserMedia({
        audio: true,
        video: false
    }).then(function(stream){
        recoding = true;
        mediaRecorder = new MediaRecorder(stream);
        mediaRecorder.start();
        mediaRecorder.ondataavailable = function(e){
            audioData.push(e.data);
        };
        setTimeout(function(){
            if(recording==true){
                sendVoice()
                alert("录音时间不能超过60秒!");
            }
        },60000);
    }).catch(function(message){
        console.error("错误信息："+message);
    });
}