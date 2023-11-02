import React from 'react';
import Centre from './Centre.js';
import LeftTop from './container/junction-view/sidebar-left/Top';
import LeftBottom from './container/junction-view/sidebar-left/Bottom';
import RightTop from './container/junction-view/sidebar-right/Top';
import RightBottom from './container/junction-view/sidebar-right/Bottom';
import '../css/junction.scss';
export default function JunctionMain() {
    return (
        <main>
            <div className="leftSide">
                <LeftTop />
                {/* <LeftBottom /> */}
            </div>
            <Centre />
            <div className="rightSide">
                {/* <RightTop />
                <RightBottom /> */}
            </div>
        </main>
    )
}
