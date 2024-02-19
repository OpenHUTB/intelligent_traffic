import React from 'react';
import './style.scss';
import TrafficFlow from '../Traffic-Flow/';
import PieContainer from '../Pie-container/';
import LineContainer from '../Line-container/';
import Overview from '../Traffic-Overview/Overview';
import PedestrainOptimize from '../Pedestrain-optimize';
export default function Index(props) {




    return (
        <section className="junction-leftTop">
            <PieContainer />
            {/* <TrafficFlow /> */}
            <LineContainer />
            {/* <Overview /> */}
            <PedestrainOptimize />
        </section >

    )
}
