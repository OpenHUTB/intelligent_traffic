import React from 'react'
// import OperationDetect from './OperationDetect'
import FunctionICons from './functionIcons/FunctionIcon'
import Map from '../homeview/Map/Map'
import ScrollAlert from './scrollAlert/ScrollAlert'
export default function RightSide() {
    return (
        <div>
            <FunctionICons />
            <Map />
            <ScrollAlert />
        </div>
    )
}