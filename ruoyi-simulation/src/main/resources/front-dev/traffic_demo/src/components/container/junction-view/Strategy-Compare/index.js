import React from 'react'
import option1 from 'assets/img/scheme1.png';
import option2 from 'assets/img/scheme2.png';
import option3 from 'assets/img/scheme3.png';
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import styles from './strategy.module.scss';
export default function Index() {
    return (
        <section className={styles.optionsComparison}>
            <div className={styles.title}><span className={styles.svg}><NavIcon /></span><span>优化方案展示</span></div>

            <div className={styles.videoContainer}>
                <img src={option1} alt="" />
                <img src={option2} alt="" />
                <img src={option3} alt="" />
            </div>
        </section>
    )
}
