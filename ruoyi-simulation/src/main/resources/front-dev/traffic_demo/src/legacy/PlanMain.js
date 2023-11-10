import React, { useEffect, useState } from 'react';
import Centre from '../components/Centre.js';
import LeftTop from '../components/container/plan-view/sidebar-left/Top/index.jsx';
import LeftBottom from '../components/container/plan-view/sidebar-left/Bottom/index.jsx';
import RightTop from '../components/container/plan-view/sidebar-right/Top/index.jsx';
import RightBottom from '../components/container/plan-view/sidebar-right/Bottom/index.jsx';
import '../css/plan.scss';
import data from '../scenario.json';

function getRandomItem(array) {
  const randomIndex = Math.floor(Math.random() * array.length);
  return array[randomIndex];
}

function getRandomJunction(arr, count) {
  const shuffled = [...arr].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, count);
}
export default function Main() {
  const [randomJunction, setRandomJunction] = useState(getRandomJunction(data.junctionData, 5));
  const [randomRoadStatus, setRandomRoadStatus] = useState(getRandomItem(data.roadStatus));
  const [randomTrafficViolation, setRandomTrafficViolation] = useState(getRandomItem(data.trafficViolation));
  const [randomCompare, setRandomCompare] = useState(getRandomItem(data.compare));

  useEffect(() => {
    const interval = setInterval(() => {
      setRandomJunction(getRandomJunction(data.junctionData, 5));
      setRandomRoadStatus(getRandomItem(data.roadStatus));
      setRandomTrafficViolation(getRandomItem(data.trafficViolation));
      setRandomCompare(getRandomItem(data.compare));
    }, 10000);

    // Clear the interval when the component unmounts
    return () => clearInterval(interval);
  }, []);
  return (
    <main>
      <div className="leftSide">
        <LeftTop randomRoadStatus={randomRoadStatus} />
        <LeftBottom randomJunction={randomJunction} />
      </div>
      <Centre />
      <div className="rightSide">
        <RightTop randomTrafficViolation={randomTrafficViolation} randomCompare={randomCompare} />
        <RightBottom />
      </div>
    </main>
  );
}
