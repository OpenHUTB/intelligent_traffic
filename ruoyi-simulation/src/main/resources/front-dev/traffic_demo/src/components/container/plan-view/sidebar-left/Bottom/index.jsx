import React from 'react';
import './style.scss';
import { useEffect, useRef } from 'react';

export default function Index(props) {
  const scrollContainer = useRef(null);
  useEffect(() => {
    const startAutoScroll = () => {
      const container = scrollContainer.current;
      const scrollAmount = 2.5; // Adjust for faster/slower scrolling

      const interval = setInterval(() => {
        // When you've scrolled to the end of the original content, reset to the top
        if (container.scrollTop >= (container.scrollHeight / 2)) {
          container.scrollTop = 0; // Set to start without user noticing
        } else {
          container.scrollTop += scrollAmount;
        }
      }, 50); // Adjust the interval for faster/slower scrolling

      return () => clearInterval(interval); // Cleanup on component unmount
    }

    startAutoScroll();
  }, []);
  console.log(props.randomJunction);
  const renderList = props.randomJunction.map((item, index = 0) => {
    return (
      <div className="list-item" key={index}>
        <span className="rank">{index + 1}</span>
        <span className="name">{item.name}</span>
        <span className="index">{item.index}</span>
        <span className={"status " + ((item.status === "拥堵") ? "red" : (item.status === "畅通") ? "green" : "")}>{item.status}</span>
      </div>
    )
  })


  return (
    <section className="leftBottom">
      <div className="header">
        <span className="left-text"><span>各路口概况</span></span>
        <span className="right-text">(即时更新)</span>
      </div>
      <nav className="head">
        <ul>
          <li>序号</li>
          <li>路口名称</li>
          <li>拥堵指数</li>
          <li>路口状态</li>
        </ul>
      </nav>
      <div className="list-container" ref={scrollContainer}>
        {renderList}
        {renderList}
      </div>

    </section>

  )
}
