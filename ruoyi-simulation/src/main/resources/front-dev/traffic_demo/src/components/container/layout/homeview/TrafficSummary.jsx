import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { ReactComponent as OnRoadCar } from 'assets/icon/icon-onroadCar.svg';
import { ReactComponent as People } from 'assets/icon/icon-people.svg';
import { ReactComponent as Capacity } from 'assets/icon/icon-capacity.svg';

export default function Index() {
    const dispatch = useDispatch();
    const junctionInfo = useSelector((state) => state.junctionInfo);
    console.log(junctionInfo);
    useEffect(() => {

    }, [])

    return (
        <div className="pie-container">
            <div className={styles.title}>区域概览</div>
            <div id="traffic-monitor-piechart" >
                <div className='SVG-container'>
                    <OnRoadCar /><People /><Capacity />
                </div>
                <div className="description">
                    <span>在途车辆</span>
                    <span>行人数量</span>
                    <span>路网饱和度</span>
                </div>
                <div className="textData">
                    <span id='grade' className='number'>{ }</span>
                    <span className="number"> { }<span className="unit">m</span></span>
                    <span className="number"> { } <span className="unit">次</span></span>
                    <span className="number"> { } <span className="unit">s</span></span>
                </div>
            </div>
        </div>
    )
}
