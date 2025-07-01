function controlModuleChanged(infoObject) {
    const event = new CustomEvent('controlModuleChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function jamIndexChanged(infoObject) {
    const event = new CustomEvent('jamIndexChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function violationListChanged(infoObject) {
    const event = new CustomEvent('violationListChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function optimiseListChanged(infoObject) {
    const event = new CustomEvent('optimiseListChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function optimiseOverviewChanged(infoObject) {
    const event = new CustomEvent('optimiseOverviewChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function wholeIndexChanged(infoObject) {
    const event = new CustomEvent('wholeIndexChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function signalRoadDataChanged(infoObject) {
    const event = new CustomEvent('signalRoadDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}

function signalJunctionDataChanged(infoObject) {
    const event = new CustomEvent('signalJunctionDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}
function scrollAlertDataChanged(infoObject) {
    const event = new CustomEvent('scrollAlertDataChanged', { detail: infoObject });
    window.dispatchEvent(event);
}