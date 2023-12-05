import Centre from 'components/Centre';
import TrafficFlow from 'components/container/plan-view/sidebar-left/TrafficFlow/TrafficFlow';
import TrafficJam from 'components/container/plan-view/bottom/TrafficJam/TrafficJam';
import AverageDelay from 'components/container/plan-view/bottom/AverageDelay/AverageDelay';
import TrafficRank from 'components/container/plan-view/bottom/TrafficRank/TrafficRank';
import TopViolation from 'components/container/plan-view/bottom/TopViolation/TopViolation';
import Overview from 'components/container/plan-view/bottom/Overview/Overview';
import ViolationOverview from 'components/container/plan-view/sidebar-right/ViolationOverview/ViolationOverview';
import FunctionIcons from 'components/container/plan-view/sidebar-right/FunctionIcons/FunctionIcons';
import React from 'react';
import '../css/plan.scss';


export default function PlanMain() {

  return (
    <main>
      <div className="leftSide">
        <TrafficFlow />
        <Overview />
        <AverageDelay />
        <TrafficRank />


      </div>
      {/* <Centre /> */}
      <div className="rightSide">
        <ViolationOverview />
        <FunctionIcons />
        {/* <TopViolation /> */}
      </div>
      <div className='bottomSide'>
        {/* <TrafficJam /> */}



      </div>
    </main>
  );
}
