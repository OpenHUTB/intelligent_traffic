import React from 'react';
import { Link } from 'react-router-dom';
import "../css/header.scss";

export default function Header() {
    return (
        <header className='header'>
            <ul className='nav-left'>
                <li><Link to='/'>路况概览</Link></li>
                <li><Link to='/Junction'>路口场景</Link></li>
                <li>车辆跟踪</li>
            </ul>
            <div className="title">
                <h2 className="title-1">智慧交通大模型</h2>
                <h3 className="title-2">湘江实验室</h3>

            </div>
        </header>
    )
}
