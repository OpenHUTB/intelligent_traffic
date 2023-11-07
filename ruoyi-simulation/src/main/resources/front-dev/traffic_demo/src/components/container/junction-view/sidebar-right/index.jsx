
import React, { useEffect, useState } from 'react'
import { ReactComponent as LeftIcon } from '../../../../assets/icon/icon-left.svg';
import { ReactComponent as ForwardIcon } from '../../../../assets/icon/icon-forward.svg';

import { ReactComponent as NavIcon } from '../../../../assets/icon/icon-nav.svg';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Form from 'react-bootstrap/Form';
import './style.scss';
export default function Index() {
    //traffic light
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
        }, [time, isGreen, initialTime]);

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


    //buttons 
    const [activeIndex, setActiveIndex] = useState(null);
    const buttons = ['东西直行', '东西左转', '南北直行', '南北左转'];
    const handleButtonClick = (index) => {
        setActiveIndex(index);
    };


    // range bar value setting
    const [values, setValues] = useState({
        bar1: forwardEast.time,
        bar2: leftEast.time,
        bar3: forwardNorth.time,
        bar4: leftNorth.time,
    });

    const handleChange = (bar, newValue) => {
        if (bar === activeIndex) {
            setValues(prevValues => ({
                ...prevValues,
                [`bar${bar + 1}`]: newValue
            }));
        }
    };

    // Function to calculate background style for a bar
    const getBackgroundStyle = (value) => {
        const thumbPosition = ((value) / 99) * 100;
        return `linear-gradient(to right, #5cac5f, green ${thumbPosition}%, #da6e0a ${thumbPosition}%, red 100%)`;
    };




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

            <div className="lights-optimize-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>信号灯优化控制</span></div>
                <div className="optimize-methods">
                    <label htmlFor="customSelect" className='select-label'>调优方式:</label>
                    <Form.Select id="customSelect" size="sm" className='select-custom'>
                        <option>手动调优</option>
                        <option>自动调优</option>
                    </Form.Select>
                </div>
                <section className="seconds-control-container">
                    <div className="control-buttons">
                        <ButtonGroup size="sm" className="mb-2 custom-button-group">
                            {buttons.map((label, index) => (
                                <Button
                                    key={index}
                                    className={activeIndex === index ? 'active-btn' : ''}
                                    onClick={() => handleButtonClick(index)}
                                >
                                    {label}
                                </Button>
                            ))}
                        </ButtonGroup>
                    </div>
                    <div className="rangeBars-container">
                        {Object.entries(values).map(([barKey, value], index) => (
                            <div className="singleBar" key={barKey}>
                                <input
                                    type="range"
                                    className="custom-range"
                                    min="0"
                                    max="99"
                                    step="1"
                                    value={value}
                                    onChange={(e) => handleChange(index, e.target.value)}
                                    disabled={activeIndex !== index}
                                    style={{
                                        background: getBackgroundStyle(value)
                                    }}
                                />
                                <span className="select-value">{value}</span>
                            </div>
                        ))}
                    </div>
                </section>


            </div>
        </section>
    );
};

