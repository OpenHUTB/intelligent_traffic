
import React, { useEffect, useState } from 'react'
import { ReactComponent as LeftIcon } from '../../../../assets/icon/icon-left.svg';
import { ReactComponent as ForwardIcon } from '../../../../assets/icon/icon-forward.svg';
import './style.scss';
import { ReactComponent as NavIcon } from '../../../../assets/icon/icon-nav.svg';


export default function Index() {

    function useTrafficLight(initialTime, initialIsGreen) {
        const [time, setTime] = useState(initialTime);
        const [isGreen, setIsGreen] = useState(initialIsGreen);
        useEffect(() => {
            if (time === 0) {
                setIsGreen((prevIsGreen) => !prevIsGreen);
                setTime(initialTime);
            }
            const timer = setTimeout(() => setTime(time - 1), 1000);
            return () => clearTimeout(timer);
        }, [time, isGreen]);

        return { time, isGreen, setTime, setIsGreen };
    }
    const ArrowDisplay = ({ isGreen, time, IconComponent }) => (
        <div className="arrow">
            <IconComponent className={isGreen ? 'green' : 'red'} />
            <div className={`time ${isGreen ? 'green' : 'red'}`}>{time}</div>
        </div>
    );

    const leftNorth = useTrafficLight(25, false);
    const forwardNorth = useTrafficLight(25, true);

    const leftSouth = useTrafficLight(25, false);
    const forwardSouth = useTrafficLight(25, true);

    const leftWest = useTrafficLight(20, false);
    const forwardWest = useTrafficLight(20, true);

    const leftEast = useTrafficLight(20, false);
    const forwardEast = useTrafficLight(20, true);
    return (
        <section className="slider-right">

            <div className="lights-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>实时态势监控</span></div>
                <div className="arrows">
                    <div className="arrow-container north">
                        <ArrowDisplay isGreen={leftNorth.isGreen} time={leftNorth.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={forwardNorth.isGreen} time={forwardNorth.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container south">
                        <ArrowDisplay isGreen={leftSouth.isGreen} time={leftSouth.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={forwardSouth.isGreen} time={forwardSouth.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container west">
                        <ArrowDisplay isGreen={leftWest.isGreen} time={leftWest.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={forwardWest.isGreen} time={forwardWest.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container east">
                        <ArrowDisplay isGreen={leftEast.isGreen} time={leftEast.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={forwardEast.isGreen} time={forwardEast.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-centre"></div>
                </div>
            </div>
        </section>
    );
};

