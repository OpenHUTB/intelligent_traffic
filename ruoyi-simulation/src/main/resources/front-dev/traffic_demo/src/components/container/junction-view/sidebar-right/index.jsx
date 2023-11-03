
import React, { useEffect, useState } from 'react'
import { ReactComponent as LeftIcon } from '../../../../assets/icon/icon-left.svg';
import { ReactComponent as FowardIcon } from '../../../../assets/icon/icon-forward.svg';
import './style.scss';
export default function Index() {
    const initialGreenTime = 60;
    const initialRedTime = 100;
    const [greenTime, setGreenTime] = useState(initialGreenTime);
    const [redTime, setRedTime] = useState(initialRedTime);

    useEffect(() => {
        const greenInterval = setInterval(() => {
            if (greenTime > 0) {
                setGreenTime(greenTime - 1);
            }
        }, 1000);

        const redInterval = setInterval(() => {
            if (redTime > 0) {
                setRedTime(redTime - 1);
            }
        }, 1000);

        return () => {
            clearInterval(greenInterval);
            clearInterval(redInterval);
        };
    }, [greenTime, redTime]);

    return (
        <section className="slider-right">
            <div className="lights-container">
                <div className="arrow-container north">
                    <div className="arrow-left">
                        <LeftIcon />
                        <div className="time">{greenTime}</div>
                    </div>
                    <div className="arrow-left">
                        <FowardIcon />
                        <div className="time">{redTime}</div>
                    </div>

                </div>
                <div className="arrow-container west">
                    <div className="arrow-left">
                        <LeftIcon />
                        <div className="time">{greenTime}</div>
                    </div>
                    <div className="arrow-left">
                        <FowardIcon />
                        <div className="time">{redTime}</div>
                    </div>
                </div>
                <div className="arrow-container south">
                    <div className="arrow-left">
                        <LeftIcon />
                        <div className="time">{greenTime}</div>
                    </div>
                    <div className="arrow-left">
                        <FowardIcon />
                        <div className="time">{redTime}</div>
                    </div>
                </div>
                <div className="arrow-container east">
                    <div className="arrow-left">
                        <LeftIcon />
                        <div className="time">{greenTime}</div>
                    </div>
                    <div className="arrow-left">
                        <FowardIcon />
                        <div className="time">{redTime}</div>
                    </div>
                </div>

            </div>
        </section>
    );
};

