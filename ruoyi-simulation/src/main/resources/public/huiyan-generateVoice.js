function getVoice(message){
    $.ajax({
        url: "http://"+window.location.hostname+":7200/stream/v1",
        data: JSON.stringify({
            "text":message,
            "lang_type":"zh-cmn-Hans-CN",
            "voice":"Meihui",
            "format":"wav",
            "volume": 90
        }),
        type: "POST",
        dataType: "json",
        contentType:"application/json",
        success:function(result){
            if(result.status=='00000'){
                var sound = result.data;
                playSound(sound);
            }
        }
    });
}
/**
 * 播放语音
 */
function playSound(sound,callback){
    updateAnimation(4);
    var audioArea = document.getElementById("audioArea");
    audioArea.innerHTML="";
    var audio = document.createElement("audio");
    audioArea.appendChild(audio);
    audio.autoplay = "autoplay";
    audio.src = "data:audio/wav;base64,"+sound;
    audio.play();
    audio.addEventListener("ended",function(){
        if(callback!=null){
            callback();
        }
        setTimeout(function(){
            updateAnimation(0);
        },1500);
    });
}