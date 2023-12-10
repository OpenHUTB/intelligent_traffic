import React from 'react'
import option1 from 'assets/img/scheme1.png';
import option2 from 'assets/img/scheme2.png';
import option3 from 'assets/img/scheme3.png';
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
export default function Index() {
    return (
        <section className="options-comparison">
            <div className="title"><span className='svg'><NavIcon /></span><span>优化方案展示</span></div>

            <div className='video-container'>
                <img src={option1} alt="" />
                <img src={option2} alt="" />
                <img src={option3} alt="" />
            </div>
        </section>
    )
}
