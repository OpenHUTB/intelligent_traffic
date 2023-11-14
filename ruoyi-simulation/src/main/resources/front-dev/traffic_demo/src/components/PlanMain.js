import Centre from 'components/Centre';
import TrafficFlow from 'components/container/plan-view/sidebar-left/TrafficFlow/TrafficFlow';
import TrafficJam from 'components/container/plan-view/bottom/TrafficJam/TrafficJam';
import AverageDelay from 'components/container/plan-view/bottom/AverageDelay/AverageDelay';
import '../css/plan.scss';


export default function Main() {

  return (
    <main>
      <div className="leftSide">
        <TrafficFlow />
      </div>
      <Centre />
      <div className="rightSide">

      </div>
      <div className='bottomSide'>
        <TrafficJam />
        <AverageDelay />
      </div>
    </main>
  );
}
