const record = document.getElementById("record")
const stop = document.getElementById("stop")
const play = document.getElementById("play")
const send = document.getElementById("send")
const audio = document.getElementById("audio")
const downWav = document.getElementById("downWav")
const downPcm = document.getElementById("downPcm")
const canvas = document.getElementById("canvas")
const ctx = canvas.getContext("2d")
canvas.width = 600
canvas.height = 200 
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