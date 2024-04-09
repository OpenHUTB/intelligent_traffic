import React from 'react';
import styles from 'css/home.module.scss';

export default function Home() {

    return (
        <main>
            <div id="twinContainer">
                {/* <video id="twin" muted src={twinVideo} ></video> */}
            </div>
            <div className={styles.leftSide}>
            </div>
            <div className={styles.rightSide}>
            </div>
            <div className={styles.bottomSide}>
            </div>
        </main>
    );
}
