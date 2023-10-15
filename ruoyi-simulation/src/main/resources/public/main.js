const record = document.getElementById("record")
const canvas = document.getElementById("canvas")
const download = document.getElementById("download")
const ctx = canvas.getContext("2d")
canvas.width = 400
canvas.height = 120
let recorder = null;
var interval = null;
/**
 * 开始录音
 */
function start(){
    //开始录音操作
    if(recorder !== null) {
        recorder.close();
    }
    Recorder.get((rec) => {
        recorder = rec;
        recorder.start();
    });
}
/**
 * 停止录音
 */
function end(){
    if(recorder != null){
        //停止录音，并将录音生成的文件，
        recorder.stop();
    }
}
/**
 * 发送录音
 */
function send(){
    if(recorder != null){
        //停止录音，并将录音生成的文件，
        var blob = recorder.getBlob();
        websocket.send(blob);
        const src = recorder.wavSrc();
        download.setAttribute("href", src)
        $(download).click();
    }
}
record.addEventListener("click",function(){
    start();
    interval = setInterval(function(){
        end();
        send();
        start();
    },5000);
});

/**
 * 绘制音乐播放的频谱
 */
function draw(audio){
    let audioContext = window.AudioContext || window.webkitAudioContext
    const context = new audioContext()
    const source = context.createMediaElementSource(audio);
    const analyser = context.createAnalyser();
    analyser.fftsize = 2048;
    source.connect(analyser);
    analyser.connect(context.destination);
    const bufferLength = analyser.frequencyBinCount // 返回的是 analyser的fftsize的一半
    const top = new Uint8Array(bufferLength);
    let gradient = ctx.createLinearGradient(0, 0, 4, 120)
    gradient.addColorStop(1, 'pink')
    gradient.addColorStop(0.5, 'blue')
    gradient.addColorStop(0, 'red')
    let drawing = function() {
        let array = new Uint8Array(analyser.frequencyBinCount)
        analyser.getByteFrequencyData(array)
        ctx.clearRect(0, 0, 400, 120)
        for(let i = 0; i < array.length; i++) {
            let _height = array[i]
            if(!top[i] || (_height > top[i])) {//帽头落下
                top[i] = _height
            } else {
                top[i] -= 1
            }
            ctx.fillRect(i * 20, 120 - _height, 4, _height)
            ctx.fillRect(i * 20, 120 - top[i] -6.6, 4, 3.3)//绘制帽头
            ctx.fillStyle = gradient
        }
        requestAnimationFrame(drawing);
    }
    drawing();
}