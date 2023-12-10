import React, { useEffect, useState } from 'react';
import './style.scss';

import TrafficFlow from '../Traffic-Flow/';
import PieContainer from '../Pie-container/';
import LineContainer from '../Line-container/';
import Overview from '../Traffic-Overview/Overview';
export default function Index(props) {




    return (
        <section className="junction-leftTop">
            {/* <TrafficFlow />
            <PieContainer />
            <LineContainer /> */}
            <Overview />
        </section >

    )
}
