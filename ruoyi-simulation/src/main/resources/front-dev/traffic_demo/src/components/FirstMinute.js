import Centre from 'components/Centre';
// import TrafficFlow from 'components/container/plan-view/sidebar-left/TrafficFlow/TrafficFlow';
// import TrafficJam from 'components/container/plan-view/bottom/TrafficJam/TrafficJam';
// import AverageDelay from 'components/container/plan-view/bottom/AverageDelay/AverageDelay';
// import TrafficRank from 'components/container/plan-view/bottom/TrafficRank/TrafficRank';
// import TopViolation from 'components/container/plan-view/bottom/TopViolation/TopViolation';
// import Overview from 'components/container/plan-view/bottom/Overview/Overview';
// import ViolationOverview from 'components/container/plan-view/sidebar-right/ViolationOverview/ViolationOverview';
// import FunctionIcons from 'components/container/plan-view/sidebar-right/FunctionIcons/FunctionIcons';
import React from 'react';
import twinVideo from '../assets/videos/twin-video.mp4';
import '../css/plan.scss';

export default function PlanMain() {

    return (
        <main>
            <div id="twinContainer">
                <video id="twin" muted src={twinVideo}></video>
            </div>
            {/* <Centre /> */}
            <div className="rightSide">
            </div>
            <div className='bottomSide'>

            </div>
        </main>
    );
}
