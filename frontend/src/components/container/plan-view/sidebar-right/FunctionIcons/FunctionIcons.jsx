import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import './index.scss';
import { ReactComponent as Camera } from 'assets/icon/icon-camera.svg';
import { ReactComponent as Parking } from 'assets/icon/icon-park.svg';
import { ReactComponent as PoliceCar } from 'assets/icon/icon-police-car.svg';
import { ReactComponent as Bus } from 'assets/icon/icon-bus.svg';
import { ReactComponent as Police } from 'assets/icon/icon-human.svg';
import { ReactComponent as Alarm } from 'assets/icon/icon-warning.svg';

export default function FunctionIcons() {
    return (
        <div className="function-icons">
            <div className="icons-columns">
                <span className="icon">
                    <span className="text"><Camera />摄像头</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
                <span className="icon">
                    <span className="text"><Parking />停车场</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
                <span className="icon">
                    <span className="text"> <PoliceCar />执法车辆</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
            </div>
            <div className="icons-columns">
                <span className="icon">
                    <span className="text"><Bus />公交站</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
                <span className="icon">
                    <span className="text"><Police />执勤交警</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
                <span className="icon">
                    <span className="text"><Alarm />告警事件</span>
                    <Form.Check aria-label="option 1" defaultChecked />
                </span>
            </div>
        </div>
    )
}
