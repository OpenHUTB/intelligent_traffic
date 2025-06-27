import React from 'react'
// import Weather from './weather/Weather';
import TrafficInfoGuage from './TrafficInfo-Guage'
import TotalTrafficVolume from './TotalTrafficVolume'
import TrafficSpeed from './TrafficSpeed'
import TrafficCompare from '../homeview/TrafficCompare'
import styles from './css//leftside.module.scss'
import OperationDetect from './OperationDetect'
import twinVideo from 'assets/videos/twin.mp4'
import Guage from './Guage'
import Map from '../homeview/Map/Map'
import TrafficSummary from '../homeview/TrafficSummary'
export default function LeftSide() {
  return (
    <div className={styles.leftside}>
      <div id='twinContainer' className={styles.twinContainer}>
        <div className={styles.title}>实时监控</div>
        <video id='twin' muted src={twinVideo}></video>
      </div>
      <Guage />
      {/* <Weather /> */}
      {/* <TrafficInfoGuage /> */}
      {/* <TotalTrafficVolume /> */}
      {/* <TrafficSpeed /> */}
      <OperationDetect />
      {/* <TrafficCompare /> */}
      <TrafficSummary />
      {/* <Map /> */}
    </div>
  )
}
