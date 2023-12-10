
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

        return { time, isGreen, setTime, setIsGreen };

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


    const southNorth_forward = useTrafficLight(lightTimer.light1, true);
    const southNorth_left = useTrafficLight(lightTimer.light2, false);
    const eastWest_forward = useTrafficLight(lightTimer.light3, true);
    const eastWest_left = useTrafficLight(lightTimer.light4, false);

    // range bar value setting
    const [values, setValues] = useState({
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
            dispatch(setLightTimer({ light: `light${bar + 1}`, value: newValue }));
        }
    };

    // Update the Redux state when the light timer changes
    useEffect(() => {
        const handleLightTimerChange = (event) => {
            const key = Object.keys(event.detail)[0]; // Get the key of the changed light timer
            const newValue = event.detail[key];

            // Extract the light number from the key and update the Redux state
            const lightNumber = key.replace('light', '');
            dispatch(setLightTimer({ light: `light${lightNumber}`, value: parseInt(newValue, 10) }));
            setValues(prevValues => ({
                ...prevValues,
                [`bar${lightNumber}`]: newValue
            }));
        };

        // Attach the event listener
        window.addEventListener('lightTimerChanged', handleLightTimerChange);
    }, [lightTimer, dispatch, values]);
    // Function to calculate background style for a bar
    const getBackgroundStyle = (value) => {
        const thumbPosition = ((value) / 99) * 100;
        return `linear-gradient(to right, #5cac5f, green ${thumbPosition}%, #da6e0a ${thumbPosition}%, red 100%)`;
    };
    useEffect(() => { }, [lightTimer]);



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
            {/* <PedestrainOptimize />
            <StrategyCompare /> */}
        </section>
    );
};

