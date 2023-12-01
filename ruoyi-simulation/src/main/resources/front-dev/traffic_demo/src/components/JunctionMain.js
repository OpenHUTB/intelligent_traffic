import React from 'react';
import Centre from './Centre.js';
import LeftSlider from './container/junction-view/sidebar-left';
import RightSlider from './container/junction-view/sidebar-right';
import '../css/junction.scss';
export default function JunctionMain() {
    return (
        <main>
            <div className="leftSide">
                <LeftSlider />
                {/* <LeftBottom /> */}
            </div>
            {/* <Centre /> */}
            <div className="rightSide">
                <RightSlider />
            </div>
        </main>
    )
}
