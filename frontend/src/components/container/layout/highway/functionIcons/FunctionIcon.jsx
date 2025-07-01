import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import styles from '../css/functionIcons.module.scss';
import { ReactComponent as Camera } from 'assets/icon/icon-camera.svg';
import { ReactComponent as Parking } from 'assets/icon/icon-park.svg';
import { ReactComponent as PoliceCar } from 'assets/icon/icon-police-car.svg';
import { ReactComponent as Bus } from 'assets/icon/icon-bus.svg';
import { ReactComponent as Police } from 'assets/icon/icon-human.svg';
import { ReactComponent as Alarm } from 'assets/icon/icon-warning.svg';

export default function FunctionIcons() {
    return (
        <div className={styles.iconContainer}>
            <div className={styles.title}>功能图标</div>
            <div className={styles.functionIcons}>
                <div className={styles.iconsColumns}>
                    <span className={styles.icon}>
                        <span className={styles.text}><Camera />摄像头</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                    <span className={styles.icon}>
                        <span className={styles.text}><Parking />停车场</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                    <span className={styles.icon}>
                        <span className={styles.text}> <PoliceCar />执法车辆</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                </div>
                <div className={styles.iconsColumns}>
                    <span className={styles.icon}>
                        <span className={styles.text}><Bus />公交站</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                    <span className={styles.icon}>
                        <span className={styles.text}><Police />执勤交警</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                    <span className={styles.icon}>
                        <span className={styles.icon}><Alarm />告警事件</span>
                        <Form.Check aria-label="option 1" defaultChecked />
                    </span>
                </div>
            </div>
        </div>

    )
}
