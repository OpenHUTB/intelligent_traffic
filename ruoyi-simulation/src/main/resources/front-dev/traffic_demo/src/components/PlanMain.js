import TrafficJam from 'components/container/plan-view/bottom/TrafficJam/TrafficJam';
import AverageDelay from 'components/container/plan-view/bottom/AverageDelay/AverageDelay';
import TopViolation from 'components/container/plan-view/bottom/TopViolation/TopViolation';
import Overview from 'components/container/plan-view/bottom/Overview/Overview';
import ViolationOverview from 'components/container/plan-view/sidebar-right/ViolationOverview/ViolationOverview';
import FunctionIcons from 'components/container/plan-view/sidebar-right/FunctionIcons/FunctionIcons';
import React from 'react';
import '../css/plan-0126.scss';


export default function PlanMain() {

  return (
    <main>
      <div className="leftSide-main">
        {/* <TrafficFlow /> */}
        <Overview />
        {/* <TrafficRank /> */}
        <TopViolation />
        {/* <AverageDelay /> */}
        <TrafficJam />
      </div>
      {/* <Centre /> */}
      <div className="rightSide">
        <ViolationOverview />
        <FunctionIcons />
        {/* <TrafficRank /> */}
        <AverageDelay />
      </div>
      <div className='bottomSide'>
        {/* <Overview />
        <TrafficRank />
        <TopViolation />
        <AverageDelay />
        <TrafficJam /> */}
      </div>
    </main>
  );
}
