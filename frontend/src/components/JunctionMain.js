import React, { useEffect } from 'react'
import Centre from './Centre.js'
// import LeftSlider from './container/junction-view/sidebar-left';
// import RightSlider from './container/junction-view/sidebar-right';
import LeftSide from 'components/container/junction-view/newStyle/LeftSide.jsx'
import RightSide from 'components/container/junction-view/newStyle/RightSide.jsx'
// import BigMap from 'components/container/junction-view/newStyle/BigMap.jsx'
import '../css/junction.scss'
import { useSelector } from 'react-redux'
import { setBigMapShow } from 'stores/junctionLight/mapSlice'
export default function JunctionMain() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <main>
      <div className='leftSide'>
        {/* <LeftSlider /> */}
        <LeftSide />
      </div>

      <div className='rightSide'>
        <RightSide />
        {/* <RightSlider /> */}
      </div>
    </main>
  )
}
