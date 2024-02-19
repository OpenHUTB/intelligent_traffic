var awakeInfo = {};
var played = false;
$(function () {
    var imgsData = ['./statics/huiyan/imgs/play2.png','./statics/huiyan/imgs/play3.png','./statics/huiyan/imgs/play4.png','./statics/huiyan/imgs/play1.png'];
    var i = 0;
    function imgF() {
        if(i > imgsData.length){i = 0;}
        $('#intercomBegin').attr('src',imgsData[i]);
        i++;
    }
    var begin = $('#intercomBegin');
    var huiyan = null; //实现WebSocket
    var record = null; //多媒体对象，用来处理音频
    function init(rec) {record = rec;}
    //录音对象
    var Recorder = function(stream) {
        var sampleBits = 16; //输出采样数位 8, 16
        var sampleRate = 16000; //输出采样率
        var context = new AudioContext();
        var audioInput = context.createMediaStreamSource(stream);
        var recorder = context.createScriptProcessor(4096, 1, 1);
        var audioData = {
            size: 0, //录音文件长度
            buffer: [], //录音缓存
            inputSampleRate: 48000, //输入采样率
            inputSampleBits: 16, //输入采样数位 8, 16
            outputSampleRate: sampleRate, //输出采样数位
            oututSampleBits: sampleBits, //输出采样率
            clear: function() {
                this.buffer = [];
                this.size = 0;
            },
            input: function(data) {
                this.buffer.push(new Float32Array(data));
                this.size += data.length;
            },
            compress: function() { //合并压缩
                //合并
                var data = new Float32Array(this.size);
                var offset = 0;
                for (var i = 0; i < this.buffer.length; i++) {
                    data.set(this.buffer[i], offset);
                    offset += this.buffer[i].length;
                }
                //压缩
                var compression = parseInt(this.inputSampleRate / this.outputSampleRate);
                var length = data.length / compression;
                var result = new Float32Array(length);
                var index = 0,
                    j = 0;
                while (index < length) {
                    result[index] = data[j];
                    j += compression;
                    index++;
                }
                return result;
            },
            encodePCM: function() { //这里不对采集到的数据进行其他格式处理，如有需要均交给服务器端处理。
                var sampleRate = Math.min(this.inputSampleRate, this.outputSampleRate);
                var sampleBits = Math.min(this.inputSampleBits, this.oututSampleBits);
                var bytes = this.compress();
                var dataLength = bytes.length * (sampleBits / 8);
                var buffer = new ArrayBuffer(dataLength);
                var data = new DataView(buffer);
                var offset = 0;
                for (var i = 0; i < bytes.length; i++, offset += 2) {
                    var s = Math.max(-1, Math.min(1, bytes[i]));
                    data.setInt16(offset, s < 0 ? s * 0x8000 : s * 0x7FFF, true);
                }
                return new Blob([data]);
            }
        };
        var sendData = function() { //对以获取的数据进行处理(分包)
            var reader = new FileReader();
            reader.readAsArrayBuffer(audioData.encodePCM());
            reader.onloadend = function() {
                var binary = reader.result;
                huiyan.send(binary);
            };
            audioData.clear(); //每次发送完成则清理掉旧数据
        };
        this.start = function() {
            audioInput.connect(recorder);
            recorder.connect(context.destination);
        };
        this.stop = function() {
            recorder.disconnect();
        };
        this.getBlob = function() {
            return audioData.encodePCM();
        };
        this.clear = function() {
            audioData.clear();
        };
        recorder.onaudioprocess = function(e) {
            var inputBuffer = e.inputBuffer.getChannelData(0);
            audioData.input(inputBuffer);
            sendData();
        }
    };
    /** WebSocket **/
    function useWebSocket() {
        huiyan = new WebSocket("ws:"+window.location.hostname+":7100/ws/v1");
        huiyan.binaryType = 'arraybuffer';
        huiyan.onopen = function() {
            var params = {
                "header": {
                    "namespace": "SpeechTranscriber",
                    "name": "StartTranscription",
                    "task_id": "0220a729ac9d4c9997f51592ecc83847"
                },
                "payload": {
                    "lang_type": "zh-cmn-Hans-CN",
                    "format": "pcm",
                    "sample_rate": 16000,
                    "enable_intermediate_result": true,
                    "enable_punctuation_prediction": true,
                    "enable_inverse_text_normalization": true,
                    "max_sentence_silence": 800,
                    "enable_words": false,
                    "hotwords_id": "HWA4BB98A8",
		            "hotwords_weight": 1
                }
            };
            if (huiyan.readyState == 1) {
                huiyan.send(JSON.stringify(params));
                connect();
            }
        };
        var index = null;
        var text = '';
        huiyan.onmessage = function(result) {
            //播放像素流中的内容
            if (played==false&&webRtcPlayerObj){
                webRtcPlayerObj.video.play();
                played=true;
            }
            var data = JSON.parse(result.data);
            if(data.header.status != '00000'){
                huiyan.close();
                record.stop();
                return;
            }else{
                record.start();
            }
            var name = data.header.name;
            if(name == 'TranscriptionStarted'){
                setInterval(imgF,200);
            }
            //获取一条语音识别句子的编号
            index = data.payload.index;
            //获取语音识别的结果
            text = data.payload.result;
            //console.log(text);
            operatorMessage(text,index,name);
        };
        huiyan.onerror = function(err) {
            alert('连接失败');
        }
    }
    function startRecord(){
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
        if (!navigator.getUserMedia) {
            alert('浏览器不支持音频输入');
        } else {
            navigator.getUserMedia({
                audio: true
            },
            function(mediaStream) {
                init(new Recorder(mediaStream));
                useWebSocket();
            },
            function(error) {
                console.log(error);
                switch (error.message || error.name) {
                    case 'PERMISSION_DENIED':
                    case 'PermissionDeniedError':
                        console.info('用户拒绝提供信息。');
                        break;
                    case 'NOT_SUPPORTED_ERROR':
                    case 'NotSupportedError':
                        console.info('浏览器不支持硬件设备。');
                        break;
                    case 'MANDATORY_UNSATISFIED_ERROR':
                    case 'MandatoryUnsatisfiedError':
                        console.info('无法发现指定的硬件设备。');
                        break;
                    default:
                        console.info('无法打开麦克风。异常信息:' + (error.code || error.name));
                    break;
                }
            });
        }

    }
    /** 开始录音 **/
    startRecord();
});
/**
 * 处理huiyan解析的结果
 */
function operatorMessage(text,index,name){
    if(name == "TranscriptionResultChanged"||name == "SentenceEnd"){
        //判断语音识别结果中是否包含唤醒词-xiaoxuan,nihao
        var flag = checkAwaken(text);
        //判断你好小轩是否相邻
        if(index!=awakeInfo.index&&flag==true){
            playAudio("reply.wav");
            sessionStorage['listenerStatus'] = "AWAKENED";
            var tips = document.getElementById("tips");
            tips.value="唉，我在";
            awakeInfo.index = index;
            return;
        }
        //再已经被唤醒的情况下，获取语音命令文本
        var location = checkName(text);
        if(index!=awakeInfo.index&&location!=-1&&sessionStorage['listenerStatus']=="AWAKENED"){
            //console.log("-----------------------进入if-----------------------");
            var command = text;
            do{
                command = command.substring(location+4);
                location = checkName(command);
            }while(location!=-1);
            command = trimPunctuation(command);
            var t = sessionStorage['time'];
            if(name=="TranscriptionResultChanged"){
                //console.log("-----------------------TranscriptionResultChanged-----------------------");
                if(command.length>=20||(command==awakeInfo.tempContent&&command.length>=4)){
                    awakeInfo.index = index;
                    simulation.send(command);
                    //console.log("------------------------------"+command+"-------------------------------");
                    awakeInfo.tempContent = null;
                    return;
                }
                awakeInfo.tempContent = command;
            }else if(name=="SentenceEnd"&&command.length>=4){
                //console.log("-----------------------SentenceEnd-----------------------");
                simulation.send(command);
                //console.log("------------------------------"+command+"-------------------------------");
                awakeInfo.tempContent = null;
            }
        }
    }
}
/**
 * 修剪字符串两端的标点符号
 */
function trimPunctuation(command){
    var reg = new RegExp("[\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b\uff01\u3010\u3011\uffe5]");
    var index = -1;
    for(var i=0;i<command.length;i++){
        index=i;
        if(!reg.test(command[i])){
            break;
        }
    }
    if(index!=-1){
        command = command.substring(index);
    }
    index = -1;
    for(var i=command.length-1;i>=0;i--){
        index=i;
        if(!reg.test(command[i])){
            break;
        }
    }
    if(index!=-1){
        command = command.substring(0,index+1);
    }
    return command;
}
/**
 * 检测语音中是否包含关键字
 */
function checkAwaken(text){
    if(text.indexOf("小轩你好")!=-1||text.indexOf("你好小轩")!=-1||text.indexOf("你好,小轩")!=-1||text.indexOf("小轩,你好")!=-1||text.indexOf("你好，小轩")!=-1||text.indexOf("小轩，你好")!=-1){
        return true;
    }
    index = text.indexOf("晓轩");
    if(text.indexOf("晓轩你好")!=-1||text.indexOf("你好晓轩")!=-1||text.indexOf("你好,晓轩")!=-1||text.indexOf("晓轩,你好")!=-1||text.indexOf("你好，晓轩")!=-1||text.indexOf("晓轩，你好")!=-1){
        return true;
    }
    index = text.indexOf("小萱");
    if(text.indexOf("小萱你好")!=-1||text.indexOf("你好小萱")!=-1||text.indexOf("你好,小萱")!=-1||text.indexOf("小萱,你好")!=-1||text.indexOf("你好，小萱")!=-1||text.indexOf("小萱，你好")!=-1){
        return true;
    }
    index = text.indexOf("小宣");
    if(text.indexOf("小宣你好")!=-1||text.indexOf("你好小宣")!=-1||text.indexOf("你好,小宣")!=-1||text.indexOf("小宣,你好")!=-1||text.indexOf("你好，小宣")!=-1||text.indexOf("小宣，你好")!=-1){
        return true;
    }
    index = text.indexOf("晓萱");
    if(text.indexOf("晓萱你好")!=-1||text.indexOf("你好晓萱")!=-1||text.indexOf("你好,晓萱")!=-1||text.indexOf("晓萱,你好")!=-1||text.indexOf("你好，晓萱")!=-1||text.indexOf("晓萱，你好")!=-1){
        return true;
    }
    index = text.indexOf("");
    if(text.indexOf("小瑄你好")!=-1||text.indexOf("你好小瑄")!=-1||text.indexOf("你好,小瑄")!=-1||text.indexOf("小瑄,你好")!=-1||text.indexOf("你好，小瑄")!=-1||text.indexOf("小瑄，你好")!=-1){
        return true;
    }
    return false;
}
/**
 * 检测语音中是否包含关键字
 */
function checkName(text){
    var index = text.indexOf("小轩同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("晓轩同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("小萱同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("小宣同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("晓萱同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("小瑄同学");
    if(index!=-1){
        return index;
    }
    index = text.indexOf("小");
    var location = text.indexOf("同学");
    if(index!=-1&&location!=-1&&index+2==location&&text.indexOf("小爱同学")==-1){
        return index;
    }
    return -1;
}
