
import React, { useEffect, useState, useRef } from 'react'
// import PedestrainOptimize from '../Pedestrain-optimize';
// import StrategyCompare from '../Strategy-Compare';
import { ReactComponent as LeftIcon } from '../../../../assets/icon/icon-left.svg';
import { ReactComponent as ForwardIcon } from '../../../../assets/icon/icon-forward.svg';
import { ReactComponent as NavIcon } from '../../../../assets/icon/icon-nav.svg';

import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Form from 'react-bootstrap/Form';
import { useSelector, useDispatch } from 'react-redux';
import { setLightTimer } from 'stores/lightTimerSlice';
import './style.scss';


export default function Index() {
    const dispatch = useDispatch();
    const lightTimer = useSelector(state => state.lightTimer);
    // southNorth_left: light1,southNorth_forward: light2,eastWest_left: light3,eastWest_forward: light4,

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

        const resetTimer = (newTime, isGreen) => {
            setTime(newTime);
            setIsGreen(isGreen);
        }
        return { time, isGreen, setTime, setIsGreen, resetTimer };
    }

    const ArrowDisplay = ({ isGreen, time, IconComponent }) => (
        <div className="arrow">
            <IconComponent className={isGreen ? 'green' : 'red'} />
            <div className={`time ${isGreen ? 'green' : 'red'}`}>{time}</div>
        </div>
    );

    //buttons 
    const [activeIndex, setActiveIndex] = useState(null);
    const buttons = ['东西直行', '东西左转', '南北直行', '南北左转'];
    const handleButtonClick = (index) => {
        setActiveIndex(index);
    };


    const southNorth_forward = useTrafficLight(lightTimer.light1.seconds, lightTimer.light1.isGreen);
    const southNorth_left = useTrafficLight(lightTimer.light2.seconds, lightTimer.light2.isGreen);
    const eastWest_forward = useTrafficLight(lightTimer.light3.seconds, lightTimer.light3.isGreen);
    const eastWest_left = useTrafficLight(lightTimer.light4.seconds, lightTimer.light4.isGreen);

    // range bar value setting
    const [values, setValues] = useState({
        bar1: eastWest_forward.time,
        bar2: eastWest_left.time,
        bar3: southNorth_forward.time,
        bar4: southNorth_left.time,
    });
    //raqnge bar value for green light setting
    const [redTimer, setRedTimer] = useState({
        bar1: eastWest_forward.time,
        bar2: eastWest_left.time,
        bar3: southNorth_forward.time,
        bar4: southNorth_left.time,
    });

    const handleChange = (bar, newValue) => {
        if (bar === activeIndex) {
            setValues(prevValues => ({
                ...prevValues,
                [`bar${bar + 1}`]: newValue
            }));
            switch (bar) {
                case 0:
                    southNorth_forward.resetTimer(newValue, true);
                    break;
                case 1:
                    southNorth_left.resetTimer(newValue, true);
                    break;
                case 2:
                    eastWest_forward.resetTimer(newValue, true);
                    break;
                case 3:
                    eastWest_left.resetTimer(newValue, true);
                    break;
                default:
                    break;
            }
            dispatch(setLightTimer({ light: `light${bar + 1}`, value: { 'seconds': newValue, 'isGreen': true } }));
        }
    };
    const handleChange2 = (bar, newValue) => {
        if (bar === activeIndex) {
            setRedTimer(prevValues => ({
                ...prevValues,
                [`bar${bar + 1}`]: newValue
            }));
        }
    };

    // Function to calculate background style for a bar
    const getBackgroundStyle = (value) => {
        const thumbPosition = ((value) / 150) * 100;
        return `linear-gradient(to right,  green 0%, green ${thumbPosition}%,  rgba(0,0,0,0) ${thumbPosition + 1}% ,  rgba(0,0,0,0) 100%)`;
    };

    const getBackgroundStyleRed = (value) => {
        const thumbPosition = ((value) / 150) * 100;
        return `linear-gradient(to left,  red 0%, red ${thumbPosition}%,  rgba(0,0,0,0) ${thumbPosition + 1}% ,  rgba(0,0,0,0) 100%)`;
    };

    // Update the Redux state when the light timer changes
    useEffect(() => {
        const HandleLightTimerChange = (event) => {
            const key = Object.keys(event.detail)[0]; // Get the key of the changed light timer
            const seconds = event.detail[key].seconds;
            const isGreen = event.detail[key].isGreen;

            // Extract the light number from the key and update the Redux state
            // Reset the countdown timer here
            switch (key) {
                case 'light1':
                    southNorth_forward.resetTimer(seconds, isGreen);
                    break;
                case 'light2':
                    southNorth_left.resetTimer(seconds, isGreen);
                    break;
                case 'light3':
                    eastWest_forward.resetTimer(seconds, isGreen);
                    break;
                case 'light4':
                    eastWest_left.resetTimer(seconds, isGreen);
                    break;
                default:
                    break;
            }
            const lightNumber = key.replace('light', '');
            dispatch(setLightTimer({ light: `light${lightNumber}`, value: { 'seconds': seconds, 'isGreen': isGreen } }));
            if (isGreen) {

                setValues(prevValues => ({
                    ...prevValues,
                    [`bar${lightNumber}`]: seconds
                }));
            } else {
                setRedTimer(prevValues => ({
                    ...prevValues,
                    [`bar${lightNumber}`]: seconds
                }));
            }

        };



        // Attach the event listener
        window.addEventListener('lightTimerChanged', HandleLightTimerChange);
        return () => window.removeEventListener('lightTimerChanged', HandleLightTimerChange);



    }, [southNorth_forward, southNorth_left, eastWest_forward, eastWest_left, dispatch, values, redTimer]);

    return (
        <section className="slider-right">

            <div className="lights-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>信号灯组运行状态</span></div>
                <div className="arrows">
                    <div className="arrow-container north">
                        <ArrowDisplay isGreen={southNorth_left.isGreen} time={southNorth_left.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={southNorth_forward.isGreen} time={southNorth_forward.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container south">
                        <ArrowDisplay isGreen={southNorth_left.isGreen} time={southNorth_left.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={southNorth_forward.isGreen} time={southNorth_forward.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container west">
                        <ArrowDisplay isGreen={eastWest_left.isGreen} time={eastWest_left.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={eastWest_forward.isGreen} time={eastWest_forward.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-container east">
                        <ArrowDisplay isGreen={eastWest_left.isGreen} time={eastWest_left.time} IconComponent={LeftIcon} />
                        <ArrowDisplay isGreen={eastWest_forward.isGreen} time={eastWest_forward.time} IconComponent={ForwardIcon} />
                    </div>
                    <div className="arrow-centre"></div>
                </div>
            </div>

            <div className="lights-optimize-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>信号灯优化控制</span></div>
                <div className="optimize-methods">
                    <label htmlFor="customSelect" className='select-label'>调优方式:</label>
                    <Form.Select id="customSelect" size="sm" className='select-custom'>
                        <option>自动调优</option>
                        <option>手动调优</option>
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
                                    max="150"
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

                        {/* {redBar} */}
                    </div>
                    <div className="rangeBars-container2">
                        {Object.entries(redTimer).map(([barKey, redTime], index) => {
                            return (
                                <div className="singleBar" key={barKey}>
                                    <input
                                        type="range"
                                        className="custom-range-2"
                                        min="0"
                                        max="150"
                                        step="1"
                                        value={150 - redTime}
                                        onChange={(e) => handleChange2(index, e.target.value)}
                                        style={{
                                            background: getBackgroundStyleRed(redTime)
                                        }}
                                    />
                                    <span className="select-value red">{redTime}</span>
                                </div>
                            );
                        })}
                    </div>
                </section>
            </div>
            {/* <PedestrainOptimize />
            <StrategyCompare /> */}
        </section>
    );
};

