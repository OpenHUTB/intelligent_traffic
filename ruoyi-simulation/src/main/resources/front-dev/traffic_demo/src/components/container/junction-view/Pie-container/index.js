import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setJunctioninfo } from 'stores/junctionInfoSlice';
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { ReactComponent as Car } from 'assets/icon/car.svg';
import { ReactComponent as LineUp } from 'assets/icon/Lineup1.svg';
import { ReactComponent as Parking } from 'assets/icon/parking.svg';
import { ReactComponent as Total } from 'assets/icon/time2.svg';
export default function Index() {
    const dispatch = useDispatch();
    const junctionInfo = useSelector((state) => state.junctionInfo);
    console.log(junctionInfo);

    useEffect(() => {
        const handleJunctionInfo = (event) => {
            console.log(event.detail);
            const key = Object.keys(event.detail)[0];
            const value = event.detail[key];
            console.log(key, value);
            dispatch(setJunctioninfo({ [key]: value }));
        };
        window.addEventListener('junctionInfoChanged', handleJunctionInfo);
        return () => {
            window.removeEventListener('junctionInfoChanged', handleJunctionInfo);
        }
    }, [junctionInfo, dispatch])
    //set the traffic monitor pie chart;
    // const Counter = ({ counts, time = 500 }) => {
    //     const [count, setCount] = useState({junctionInfo});
    //     useEffect(() => {
    //         let startTime = Date.now();
    //         let duration = time;
    //         const timer = setInterval(() => {
    //             setCount(() => {
    //                 let after = ((Date.now() - startTime) / duration * counts).toFixed(1);
    //                 if (after > counts) {
    //                     clearInterval(timer);
    //                     after = counts;
    //                 }
    //                 return after;
    //             });
    //         }, 16);
    //         return () => clearInterval(timer);
    //     }, [counts]);
    //     return count;
    // }
    return (
        <div className="pie-container">
            <div className="title"><span className='svg'><NavIcon /></span><span>路口运行信息</span></div>
            <div id="traffic-monitor-piechart" >
                <div className='SVG-container'>
                    <Car /><LineUp /><Parking /><Total />
                </div>
                <div className="description">
                    <span>路口评级</span>
                    <span>排队长度</span>
                    <span>停车次数</span>
                    <span>平均延误</span>
                </div>
                <div className="textData">
                    <span id='grade' className='number'>{junctionInfo.grade}</span>
                    {/* <span className="number"> <Counter counts={20.7} /> <span className="unit">m</span></span>
                    <span className="number"><Counter counts={15} /> <span className="unit">次</span></span>
                    <span className="number"><Counter counts={23.5} /> <span className="unit">s</span></span> */}
                    <span className="number"> {junctionInfo.queueLength}<span className="unit">m</span></span>
                    <span className="number"> {junctionInfo.parkingTimes} <span className="unit">次</span></span>
                    <span className="number"> {junctionInfo.averageDelay} <span className="unit">s</span></span>
                </div>
            </div>
        </div>
    )
}
