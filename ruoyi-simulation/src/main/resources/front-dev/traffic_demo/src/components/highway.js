import React from 'react';
import styles from 'css/home.module.scss';
import LeftSide from 'components/container/layout/homeview/LeftSide';
import RightSide from 'components/container/layout/homeview/RightSide';
export default function Home() {

    return (
        <main>
            <div id="twinContainer">
                {/* <video id="twin" muted src={twinVideo} ></video> */}
            </div>
            <div className={styles.leftSide}>
                <LeftSide />
            </div>
            <div className={styles.rightSide}>
                <RightSide />
            </div>
            <div className={styles.bottomSide}>
            </div>
        </main>
    );
}
