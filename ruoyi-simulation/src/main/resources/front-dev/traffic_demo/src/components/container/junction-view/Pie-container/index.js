import React, { useEffect } from 'react';
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
        window.addEventListener('lightTimerChanged', (event) => {
            const key = Object.keys(event.detail)[0];
            const isGreen = event.detail[key].isGreen;
            if (isGreen) {
                dispatch(setJunctioninfo({ grade: 'A', queueLength: 5, parkingTimes: 3, averageDelay: 28 }));
            } else {
                dispatch(setJunctioninfo({ grade: 'c', queueLength: 8.5, parkingTimes: 5, averageDelay: 52 }));
            }
        })
        window.addEventListener('junctionInfoChanged', handleJunctionInfo);
        return () => {
            window.removeEventListener('junctionInfoChanged', handleJunctionInfo);
        }
    }, [junctionInfo, dispatch])

    return (
        <div className="pie-container">
            <div className="title"><span className='svg'><NavIcon /></span><span>路口运行信息</span></div>
            <div id="traffic-monitor-piechart" >
                <div className='SVG-container'>
                    <Car /><LineUp /><Parking /><Total />
                </div>
                <div className="description">
                    <span>红灯次数</span>
                    <span>排队长度</span>
                    <span>等待次数</span>
                    <span>平均车速</span>
                </div>
                <div className="textData">
                    <span id='grade' className='number'>{junctionInfo.grade}</span>
                    <span className="number"> {junctionInfo.queueLength}<span className="unit">m</span></span>
                    <span className="number"> {junctionInfo.parkingTimes} <span className="unit">次</span></span>
                    <span className="number"> {junctionInfo.averageDelay} <span className="unit">s</span></span>
                </div>
            </div>
        </div>
    )
}
