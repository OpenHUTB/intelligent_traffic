import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { setControlModule } from 'stores/digitalTwin/controlModuleSlice';
import styles from '../css/controlModule.module.scss';

export default function ControlModule() {
    // const [adaptive, setAdaptive] = useState(14);
    // const [dynamic, setdynamic] = useState(7);
    // const [manual, setmanual] = useState(2);

    // useEffect(() => {
    //     const interval = setInterval(() => {
    //         setValue1((prev) => Math.max(prev + (Math.random() < 0.5 ? -1 : 1), 0));
    //         setValue2((prev) => Math.max(prev + (Math.random() < 0.5 ? -1 : 1), 0));
    //         setValue3((prev) => Math.max(prev + (Math.random() < 0.5 ? -1 : 1), 0));
    //     }, 4000);

    //     return () => clearInterval(interval);
    // }, []);
    const dispatch = useDispatch();
    const controlModuleState = useSelector((state) => state.controlModule);
    console.log("controlModule" + controlModuleState);
    useEffect(() => {
        const handleControlModule = (event) => {
            console.log(event.detail);
            // const key = Object.keys(event.detail);
            // const value = event.detail[key];
            // console.log(key, value);
            dispatch(setControlModule({ ...event.detail }));
        };
        window.addEventListener('controlModuleChanged', handleControlModule);
        return () => {
            window.removeEventListener('controlModuleChanged', handleControlModule);
        }
    }, [controlModuleState, dispatch])
    return (
        <div className={styles.controlModule}>
            <div className={styles.title}>
                <span>路口控制模式</span>
            </div>
            <div className={styles.contentContainer}>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>自适应控制</span>
                    <span className={styles.value}>{controlModuleState.adaptive}</span>
                </div>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>动态调控</span>
                    <span className={styles.value}>{controlModuleState.dynamic}</span>
                </div>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>人工调控</span>
                    <span className={styles.value}>{controlModuleState.manual}</span>
                </div>
            </div>
        </div>
    );
}