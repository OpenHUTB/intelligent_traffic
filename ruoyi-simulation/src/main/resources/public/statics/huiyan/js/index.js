function FullScreen(el) {
    var isFullscreen = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen;
    if (!isFullscreen) { //进入全屏,多重短路表达式
        (el.requestFullscreen && el.requestFullscreen()) ||
        (el.mozRequestFullScreen && el.mozRequestFullScreen()) ||
        (el.webkitRequestFullscreen && el.webkitRequestFullscreen()) || (el.msRequestFullscreen && el.msRequestFullscreen());

    } else { //退出全屏,三目运算符
        document.exitFullscreen ? document.exitFullscreen() :
            document.mozCancelFullScreen ? document.mozCancelFullScreen() :
                document.webkitExitFullscreen ? document.webkitExitFullscreen() : '';
    }
}
function toggleFullScreen(e) {
    let element = document.documentElement;
    var el = e.srcElement || e.target; //target兼容Firefox
    el.innerHTML == '全屏' ? el.innerHTML = '退出全屏' : el.innerHTML = '全屏';
    FullScreen(element);
}
$(function () {
    var imgsData = ['./res/imgs/play2.png','./res/imgs/play3.png','./res/imgs/play4.png','./res/imgs/play1.png'];
    var i = 0;
    function imgF() {
        if(i > imgsData.length){i = 0;}
        $('.playBtnImgs').attr('src',imgsData[i]);
        i++;
    }
    var t;
    var begin = $('#intercomBegin');
    var end = $('#intercomEnd');
    var clear = $('#clearScreen');
    var change = $('#changeLangtype');
    var msgSpan = $('#msg');
    var ws = null; //实现WebSocket
    var record = null; //多媒体对象，用来处理音频
    function init(rec) {record = rec;}
    var sourcediv = document.getElementById('source_text');
    var targetdiv = document.getElementById('target_text');
    // 初始为中文热词id
    var hotwords_id = "";
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
                ws.send(binary);
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
    var oldS = '';
    function useWebSocket() {
        var lang_type = $('#source_text').attr('data-lang_type').split(',')[0];
        ws = new WebSocket("ws://gxq.server.huiyan-tech.com:7100/ws/v1");
        ws.binaryType = 'arraybuffer';
        ws.onopen = function() {
            var sendData = {
                "header": {
                    "namespace": "SpeechTranscriber",
                    "name": "StartTranscription",
                    "task_id": "0220a729ac9d4c9997f51592ecc83847"
                },
                "payload": {
                    "lang_type": lang_type,
                    "format": "pcm",
                    "sample_rate": 16000,
                    "enable_intermediate_result": true,
                    "enable_punctuation_prediction": true,
                    "enable_inverse_text_normalization": true,
                    "max_sentence_silence": 800,
                    "enable_words": false,
                    "hotwords_id": hotwords_id,
		    "hotwords_weight": 0.8
                }
            };
            if (ws.readyState == 1) {
                ws.send(JSON.stringify(sendData));
            }
        };
        var i = -1;
        var newS = '';
        ws.onmessage = function(msg) {
            console.log(JSON.parse(msg.data))
            if(JSON.parse(msg.data).header.status != '00000'){
                ws.close();
                record.stop();
                return;
            }else{
                record.start();
            }
            if(JSON.parse(msg.data).header.name == 'TranscriptionStarted'){
                t = setInterval(imgF,200);
                begin.hide();
                end.show();
                clear.attr('disabled','disabled');
                change.attr('disabled','disabled');
                msgSpan.text('正在听写…');
                if($('#source_text').text() === '点击麦克风按钮开始'){
                    $('#source_text').text('现在可以开始说话');
                    $('#target_text').text('');
                }else if($('#source_text').text() === 'Press the MIC button to start transcription.'){
                    $('#source_text').text('Please speak now.');
                    $('#target_text').text('');
                }
            }
            var index = JSON.parse(msg.data).payload.index;
            if(i != index){
                oldS += newS;
            }
            newS = JSON.parse(msg.data).payload.result;
            // newS = newS.replace(/\s+/g,"");
            i = index;
            if(JSON.parse(msg.data).header.name == "TranscriptionResultChanged"){
                $('#source_text').html(oldS+`<span style="color: #f9f900;">`+newS+` </span>`);
                sourcediv.scrollTop = sourcediv.scrollHeight;
            }else if(JSON.parse(msg.data).header.name == "SentenceEnd"){
                if ($('#source_text').attr('data-lang_type').split(',')[1] === 'en') {
                    $('#source_text').html(oldS+' '+newS);
                } else {
                    $('#source_text').html(oldS+newS);
                }
                sourcediv.scrollTop = sourcediv.scrollHeight;
            }
        };
        ws.onerror = function(err) {
            alert('连接失败');
        }
    }
    /** 开始录音 **/
    begin.click(function () {
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
                }
            )
        }
    });
    /** 关闭录音 **/
    end.click(function () {
        end.hide();
        begin.show();
        clear.removeAttr('disabled');
        change.removeAttr('disabled');
        clearInterval(t);
        msgSpan.text('点击按钮开始');
        if (ws) {
            ws.close();
            record.stop();
            $('#source_text>span').css('color','#bababa');
        }
    });
    // 清屏
    $('#clearScreen').click(function () {
        var source_lang_type = $('#source_text').attr('data-lang_type');
        if(source_lang_type == 'zh-cmn-Hans-CN,zh'){
            $('#source_text').text('点击麦克风按钮开始听译');
            $('#target_text').text('Press the MIC button to start transcription.');
        }else if(source_lang_type == 'en-US,en'){
            $('#source_text').text('Press the MIC button to start transcription.');
            $('#target_text').text('点击麦克风按钮开始听译');
        }
        //$('#target_text').text('');
        oldS = '';
        end.click();
    });
    // 切换语种
    $('#changeLangtype').click(function () {
        var source_lang_type = $('#source_text').attr('data-lang_type');
        var target_lang_type = $('#target_text').attr('data-lang_type');
        if(source_lang_type == 'zh-cmn-Hans-CN,zh'){
            $('#source_text').text('Press the MIC button to start transcription.');
            $('#target_text').text('点击麦克风按钮开始');
            // 此处切换为英文热词id
            hotwords_id = '';
        }else if(source_lang_type == 'en-US,en'){
            $('#source_text').text('点击麦克风按钮开始');
            $('#target_text').text('Press the MIC button to start transcription.');
            // 此处切换为中文热词id
            hotwords_id = '';
        }
        //$('#target_text').text('');
        $('#source_text').attr('data-lang_type',target_lang_type);
        $('#target_text').attr('data-lang_type',source_lang_type);
        oldS = '';
        end.click();
    });
});
