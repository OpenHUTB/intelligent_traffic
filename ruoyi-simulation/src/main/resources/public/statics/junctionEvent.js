function controlStrategyDataChanged(infoObject) {
    const event = new CustomEvent('controlStrategyDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function lightControlDataChanged(infoObject){
    const event = new CustomEvent('lightControlDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function junctionInfoDataChanged(infoObject){
    const event = new CustomEvent('junctionInfoDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function evaluationDataChanged(infoObject){
    const event = new CustomEvent('evaluationDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function junctionCarDataChanged(infoObject){
    const event = new CustomEvent('junctionCarDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function junctionImageDataChanged(infoObject){
    const event = new CustomEvent('junctionImageDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function resultTrackDataChanged(infoObject){
    const event = new CustomEvent('resultTrackDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}
 
function preVideo(){
    const preVideo = document.getElementById('PRE');
    preVideo.loop = true;
    preVideo.play();
}

function optVideo(){
    const optVideo = document.getElementById('OPT');
    optVideo.loop = true;
    optVideo.play();
}

function updateMessage(aiMessage){
    const event = new CustomEvent('aiResponseUpdate', { detail: aiMessage });
    window.dispatchEvent(event);
}