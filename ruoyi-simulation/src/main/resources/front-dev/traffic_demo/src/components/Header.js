import React from 'react';
import { Link } from 'react-router-dom';

export default function Header() {
    return (
        <header>
            <nav>
                <ul>
                    <li><Link to='/'>路况概览</Link></li>
                    <li><Link to='/Junction'>路口场景</Link></li>
                    <li></li>
                </ul>
            </nav>
            <h1 className="title">智慧交通大模型</h1>
            <nav>
                <ul>
                    <li>特情概览</li>
                </ul>
            </nav>
        </header>
    )
}
