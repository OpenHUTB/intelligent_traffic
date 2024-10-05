import React from 'react';
import { Link } from 'react-router-dom';
import "../css/header.scss";
import Weather from 'components/container/Weather/Weather';
export default function Header() {
    return (
        <header className='header'>
            <ul className='nav-left'>


                <li><Link to='/'>首页</Link></li>
                <li><Link to='/plan'>规划预演</Link></li>
                <li><Link to='/junction'>路口优化</Link></li>
                <li><Link to='/highway'>高速路网</Link></li>
                {/* <li><Link to='/city'>城市路口</Link></li> */}
                <li><Link to='/digitalTwin'>城市孪生</Link></li>
            </ul>
            <div className="title">
                <h2 className="title-1"> </h2>
            </div>
            <Weather />
            <div className="logo">
            </div>
        </header>
    )
}
