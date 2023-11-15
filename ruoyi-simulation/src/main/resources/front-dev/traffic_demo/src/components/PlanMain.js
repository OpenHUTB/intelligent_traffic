import Centre from 'components/Centre';
import TrafficFlow from 'components/container/plan-view/sidebar-left/TrafficFlow/TrafficFlow';
import TrafficJam from 'components/container/plan-view/bottom/TrafficJam/TrafficJam';
import AverageDelay from 'components/container/plan-view/bottom/AverageDelay/AverageDelay';
import TrafficRank from 'components/container/plan-view/bottom/TrafficRank/TrafficRank';
import TopViolation from 'components/container/plan-view/bottom/TopViolation/TopViolation';
import Overview from 'components/container/plan-view/bottom/Overview/Overview';
import ViolationOverview from 'components/container/plan-view/sidebar-right/ViolationOverview';
import '../css/plan.scss';


export default function Main() {

  return (
    <main>
      <div className="leftSide">
        <TrafficFlow />
      </div>
      <Centre />
      <div className="rightSide">
        <ViolationOverview />
      </div>
      <div className='bottomSide'>
        <TrafficJam />
        <AverageDelay />
        <TrafficRank />
        <TopViolation />
        <Overview />
      </div>
    </main>
  );
}
