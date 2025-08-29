function controlModuleChanged(infoObject) {
  const event = new CustomEvent('controlModuleChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function jamIndexChanged(infoObject) {
  const event = new CustomEvent('jamIndexChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function violationListChanged(infoObject) {
  const event = new CustomEvent('violationListChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function optimiseListChanged(infoObject) {
  const event = new CustomEvent('optimiseListChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function optimiseOverviewChanged(infoObject) {
  const event = new CustomEvent('optimiseOverviewChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function wholeIndexChanged(infoObject) {
  const event = new CustomEvent('wholeIndexChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function signalRoadDataChanged(infoObject) {
  const event = new CustomEvent('signalRoadDataChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function signalJunctionDataChanged(infoObject) {
  const event = new CustomEvent('signalJunctionDataChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}
function scrollAlertDataChanged(infoObject) {
  const event = new CustomEvent('scrollAlertDataChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}
function updateDistrictSpeed(infoObject) {
  const event = new CustomEvent('updateDistrictSpeed', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function flowAnalysisChanged(infoObject) {
  // infoObject 期待形如 { anyKey: { current:[...], weekly:[...] } }
  const event = new CustomEvent('flowAnalysisChanged', { detail: infoObject })
  window.dispatchEvent(event)
}

function flowRuntimeChanged(infoObject) {
  // 如果直接传入数组，就直接使用
  const detail = Array.isArray(infoObject) ? infoObject : infoObject
  const event = new CustomEvent('flowRuntimeChanged', { detail: detail })
  window.dispatchEvent(event)
}

function junctionGreenFlowChanged(infoObject) {
  const event = new CustomEvent('junctionGreenFlowChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function junctionOptChanged(infoObject) {
  const event = new CustomEvent('junctionOptChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function junctionTimeprogressChanged(infoObject) {
  const event = new CustomEvent('junctionTimeprogressChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function junctionOptstrategyChanged(infoObject) {
  const event = new CustomEvent('junctionOptstrategyChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function junctionOptResultChanged(infoObject) {
  const event = new CustomEvent('junctionOptResultChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}

function optSelectChanged(infoObject) {
  const event = new CustomEvent('optSelectChanged', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}
