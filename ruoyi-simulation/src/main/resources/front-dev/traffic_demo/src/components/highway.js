import React from 'react';
import styles from 'css/home.module.scss';
import LeftSide from 'components/container/layout/highway/LeftSide';
import RightSide from 'components/container/layout/highway/RightSide';
export default function Highway() {

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
