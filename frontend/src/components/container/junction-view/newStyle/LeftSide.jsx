import React from 'react';
import styles from './css/leftSide.module.scss';
import Evaluation from './Evaluation';
import ResultTrack from './ResultTrack';
import JunctionCapture from './JunctionCapture';
export default function LeftSide(props) {




    return (
        <section className={styles.leftSide}>
            <Evaluation />
            <ResultTrack />
            <JunctionCapture />
        </section >

    )
}
