import React from 'react';
import "../css/FirstHeader.scss";
import Weather from 'components/container/Weather/Weather';

export default function Header() {
    return (
        <header className='header-first'>
            <div className="title">
                <h2 className="title-1"></h2>
                {/* <h3 className="title-2">湘江实验室</h3> */}
            </div>
            <Weather />
            <div className="logo">
            </div>
        </header>
    )
}
