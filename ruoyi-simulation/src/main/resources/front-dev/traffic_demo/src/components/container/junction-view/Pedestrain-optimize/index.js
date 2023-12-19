import React, { useRef, useEffect, useState } from 'react'
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import './index.scss';
export default function Index() {

    const staticListItems = [{
        name: "二次过街",
        index: "否",
    },
    {
        name: "人机冲突指数",
        index: "52",
    },
    {
        name: "人非冲突指数",
        index: "54",
    },
    {
        name: "人均延误",
        index: "65s",
    },
    {
        name: "服务水平",
        index: "C",
    },
    {
        name: "等待空间",
        index: "B",
    },
    {
        name: "过街空间",
        index: "1",
    }];
    const [data, setData] = useState(staticListItems);
    const [overall, setOverall] = useState(52);
    useEffect(() => {
        window.addEventListener('lightTimerChanged', (event) => {
            const key = Object.keys(event.detail)[0];
            const isGreen = event.detail[key].isGreen;
            if (isGreen) {
                setData(
                    [{
                        name: "二次过街",
                        index: "否",
                    },
                    {
                        name: "人机冲突指数",
                        index: "23",
                    },
                    {
                        name: "人非冲突指数",
                        index: "10",
                    },
                    {
                        name: "人均延误",
                        index: "15s",
                    },
                    {
                        name: "服务水平",
                        index: "A",
                    },
                    {
                        name: "等待空间",
                        index: "A",
                    },
                    {
                        name: "过街空间",
                        index: "10",
                    }]
                );
                setOverall(85);
            } else {
                setData(
                    [{
                        name: "二次过街",
                        index: "是",
                    },
                    {
                        name: "人机冲突指数",
                        index: "61",
                    },
                    {
                        name: "人非冲突指数",
                        index: "52",
                    },
                    {
                        name: "人均延误",
                        index: "45s",
                    },
                    {
                        name: "服务水平",
                        index: "C",
                    },
                    {
                        name: "等待空间",
                        index: "C",
                    },
                    {
                        name: "过街空间",
                        index: "4",
                    }]
                );
                setOverall(42);
            }
        })
    }, [data, overall]);
    const renderList = data.map((item, index) => {
        return (
            <div className="list-item" key={index}>
                <span className="name">{item.name}</span>
                <span className="index">{item.index}</span>
            </div>
        )
    });
    // const scrollContainer = useRef(null);
    // useEffect(() => {
    //     const startAutoScroll = () => {
    //         const container = scrollContainer.current;
    //         const scrollAmount = 2.5; // Adjust for faster/slower scrolling

    //         const interval = setInterval(() => {
    //             // When you've scrolled to the end of the original content, reset to the top
    //             if (container.scrollTop >= (container.scrollHeight / 2)) {
    //                 container.scrollTop = 0; // Set to start without user noticing
    //             } else {
    //                 container.scrollTop += scrollAmount;
    //             }
    //         }, 50); // Adjust the interval for faster/slower scrolling

    //         return () => clearInterval(interval); // Cleanup on component unmount
    //     }

    //     startAutoScroll();
    // }, []);
    return (
        < div className="pedestrain-optimization-container" >
            <div className="title"><span className='svg'><NavIcon /></span><span>行人过街设置优化</span></div>
            <main className="pedestrian-main">
                <section className="pedestrain-scroll-container">
                    <div className='nav-container'>
                        <span>行人过街指标</span>
                        <span>评价</span>
                    </div>
                    <div className="list-container">
                        {renderList}
                        {/* {renderList} */}
                    </div>
                </section>
                <section className="pedestrian-index">
                    <span className="number">{overall}</span>
                    <span className="description">行人过街综合评分</span>
                </section>
            </main>
        </div >
    )
}
