import React from 'react';
import { Link } from 'react-router-dom';
import "../css/header.scss";

export default function Header() {
    return (
        <header className='header'>
            <ul>
                <li><Link to='/'>路况概览</Link></li>
                <li><Link to='/Junction'>路口场景</Link></li>
                <li></li>
            </ul>
            <h1 className="title">智慧交通大模型</h1>
            <ul>
                <li></li>
                <li></li>
                <li></li>
            </ul>
        </header>
    )
}
