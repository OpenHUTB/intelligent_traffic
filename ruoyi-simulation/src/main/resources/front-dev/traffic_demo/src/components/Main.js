import React, { useEffect, useState } from 'react';
import LeftTop from './container/plan-view/sidebar-left/Top';
import LeftBottom from './container/plan-view/sidebar-left/Bottom';
import RightTop from './container/plan-view/sidebar-right/Top';
import RightBottom from './container/plan-view/sidebar-right/Bottom';

import data from '../scenario.json'

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
      <div className="centre">
        <section id="player" className="mainStream">
          <section id="introductionArea" className="intro"></section>
          <section id="audioArea" className="audioPlay"></section>
          <div id="progressArea">
            <div className="progress">
              <div className="progress-bar progress-bar-striped bg-success" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
          </div>
        </section>
        <div className="funcArea">
          <nav className="funcBtn">
            <button id="record" className="btn">录制</button>
            <button id="stop" className="btn">停止</button>
            <button id="send" className="btn">发送</button>
          </nav>
          <div className="displayArea">
            <section className="voiceDetect">
              <textarea id="tips"></textarea>
            </section>
          </div>
        </div>
      </div>
      <div className="rightSide">
        <RightTop randomTrafficViolation={randomTrafficViolation} randomCompare={randomCompare} />
        <RightBottom />
      </div>
    </main>
  )
}
