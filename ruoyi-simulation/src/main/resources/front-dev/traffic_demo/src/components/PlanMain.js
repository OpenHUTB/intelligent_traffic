import React, { useEffect, useState } from 'react';
import Centre from './Centre.js';
import LeftTop from './container/plan-view/sidebar-left/Top';
import LeftBottom from './container/plan-view/sidebar-left/Bottom';
import RightTop from './container/plan-view/sidebar-right/Top';
import RightBottom from './container/plan-view/sidebar-right/Bottom';
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
