import React from 'react'
import styles from './css//leftside.module.scss';
import WholeIndex from './wholeIndex/WholeIndex';
import SignalJunction from './signalJunction/SignalJunction';
import SignalRoad from './signalRoad/SignalRoad';
import JamIndex from './jamIndex/JamIndex';
export default function LeftSide() {
    return (
        <div className={styles.leftside}>

            <WholeIndex />
            <JamIndex />
            <SignalRoad />
            <SignalJunction />
        </div>
    )
}
