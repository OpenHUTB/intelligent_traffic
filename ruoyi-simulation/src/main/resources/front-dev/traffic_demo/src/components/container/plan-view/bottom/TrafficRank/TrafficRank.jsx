import React, { useEffect, useRef } from 'react'
import './index.scss'
export default function TrafficRank() {

    // pedestrain optimization list
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


    const staticListItems = [{
        name: "枫林三路",
        status: "严重拥堵",
    },
    {
        name: "咸嘉湖路",
        status: "中度拥堵",
    },
    {
        name: "桐梓坡路",
        status: "轻度拥堵",
    },
    {
        name: "玉兰路",
        status: "正常",
    },
    {
        name: "枫林二路",
        status: "畅通",
    },
    {
        name: "岳麓大道",
        status: "畅通",
    },
    ];

    const renderList = staticListItems.map((item, index = 0) => {
        return (
            <div className="list-item" key={index}>
                <span className="rank">{index + 1}</span>
                <span className="name">{item.name}</span>
                <span className={"status " + ((item.status.includes("拥堵")) ? "red" : (item.status.includes("畅通")) ? "green" : "")}>{item.status}</span>
            </div >
        )
    })

    // console.log(renderList);

    return (
        <div className="traffic-rank">
            <div className="title">
                <span>辖区拥堵排名</span>
            </div>
            <div className="rank-container">
                <span>排名</span>
                <span className='street'>街道名称</span>
                <span>拥堵情况</span>
            </div>
            <div className="list-container" ref={scrollContainer}>
                {renderList}
                {renderList}
            </div>
        </div>
    )
}
