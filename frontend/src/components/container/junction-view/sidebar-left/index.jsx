import React from 'react';
import './style.scss';
import TrafficFlow from '../Traffic-Flow/';
import PieContainer from '../Pie-container/';
import LineContainer from '../Line-container/';
import JunctionOverview from '../Traffic-Overview/JunctionOverview';
import PedestrainOptimize from '../Pedestrain-optimize';
export default function Index(props) {




    return (
        <section className="junction-leftTop">
            <PieContainer />
            <TrafficFlow />
            <LineContainer />
            {/* <JunctionOverview /> */}
            {/* <PedestrainOptimize /> */}
        </section >

    )
}
