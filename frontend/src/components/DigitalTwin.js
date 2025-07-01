import React from 'react';
import styles from 'css/home.module.scss';
import LeftSide from 'components/container/layout/digitalTwin/LeftSide';
import RightSide from 'components/container/layout/digitalTwin/RightSide';
export default function DigitalTwin() {

    return (
        <main>
            <div >
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
