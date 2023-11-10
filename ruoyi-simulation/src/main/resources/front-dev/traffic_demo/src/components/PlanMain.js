import Centre from 'components/Centre';
import TrafficFlow from 'components/container/plan-view/sidebar-left/TrafficFlow/TrafficFlow';
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
    </main>
  );
}
