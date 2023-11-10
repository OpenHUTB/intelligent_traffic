
import React, { useEffect, useState, useRef } from 'react'
import { ReactComponent as LeftIcon } from '../../../../assets/icon/icon-left.svg';
import { ReactComponent as ForwardIcon } from '../../../../assets/icon/icon-forward.svg';

import { ReactComponent as NavIcon } from '../../../../assets/icon/icon-nav.svg';
import option1 from '../../../../assets/img/scheme1.png';
import option2 from '../../../../assets/img/scheme2.png';
import option3 from '../../../../assets/img/scheme3.png';

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
    },
    ];

    const renderList = staticListItems.map((item, index) => {
        return (
            <div className="list-item" key={index}>
                <span className="name">{item.name}</span>
                <span className="index">{item.index}</span>
            </div>
        )
    });
    console.log(renderList);
    return (
        <section className="slider-right">

            <div className="lights-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>信号灯组运行状态</span></div>
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

            {/* predestrain optimization */}
            <div className="pedestrain-optimization-container">
                <div className="title"><span className='svg'><NavIcon /></span><span>行人过街设置优化</span></div>
                <main className="pedestrian-main">
                    <section className="pedestrain-scroll-container">
                        <div className='nav-container'>
                            <span>行人过街指标</span>
                            <span>评价</span>
                        </div>
                        <div className="list-container" ref={scrollContainer}>
                            {renderList}
                            {renderList}
                        </div>
                    </section>
                    <section className="pedestrian-index">
                        <span className="number">71</span>
                        <span className="description">行人过街综合评分</span>
                    </section>
                </main>
            </div>
            <section className="options-comparison">
                <div className="title"><span className='svg'><NavIcon /></span><span>优化方案展示</span></div>

                <div className='video-container'>
                    <img src={option1} alt="" />
                    <img src={option2} alt="" />
                    <img src={option3} alt="" />
                </div>
            </section>
        </section>
    );
};

