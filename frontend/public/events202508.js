function updateDistrctSpeed(infoObject) {
  const event = new CustomEvent('updateDistrctSpeed', {
    detail: infoObject,
  })
  window.dispatchEvent(event)
}
