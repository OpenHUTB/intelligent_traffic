const stop = document.getElementById("stop")
const play = document.getElementById("play")
const send = document.getElementById("send")
const canvas = document.getElementById("canvas")
const ctx = canvas.getContext("2d")
canvas.width = 400
canvas.height = 150
let recorder = null

// 录制
record.addEventListener("click", () => {
  if(recorder !== null) recorder.close()
  Recorder.get((rec) => {
    recorder = rec
    recorder.start()
  })
})

// 停止
stop.addEventListener("click", () => {
  if(recorder === null) return alert("请先录音")
  recorder.stop()
})

send.addEventListener("click",function(){
    if(recorder === null) return alert("请先录音");
    var blob = recorder.getBlob();
    websocket.send(blob);
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
    let gradient = ctx.createLinearGradient(0, 0, 4, 150)
    gradient.addColorStop(1, 'pink')
    gradient.addColorStop(0.5, 'blue')
    gradient.addColorStop(0, 'red')
    let drawing = function() {
        let array = new Uint8Array(analyser.frequencyBinCount)
        analyser.getByteFrequencyData(array)
        ctx.clearRect(0, 0, 400, 150)
        for(let i = 0; i < array.length; i++) {
            let _height = array[i]
            if(!top[i] || (_height > top[i])) {//帽头落下
                top[i] = _height
            } else {
                top[i] -= 1
            }
            ctx.fillRect(i * 20, 150 - _height, 4, _height)
            ctx.fillRect(i * 20, 150 - top[i] -6.6, 4, 3.3)//绘制帽头
            ctx.fillStyle = gradient
        }
        requestAnimationFrame(drawing);
    }
    drawing();
}