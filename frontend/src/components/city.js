import React from 'react';
import styles from 'css/home.module.scss';
import LeftSide from 'components/container/layout/city/LeftSide';
import RightSide from 'components/container/layout/city/RightSide';
export default function City() {

    return (
        <main>
            <div >
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
