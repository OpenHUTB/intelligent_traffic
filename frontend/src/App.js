import React from 'react'
import { HashRouter as Router, Route, Routes } from 'react-router-dom'
import Header from 'components/Header'
import PlanMain from 'components/PlanMain'
import JunctionMain from 'components/JunctionMain'
import CruiserMain from 'components/CruiserMain'

import TextArea from 'components/container/TextArea/TextArea'
import HighwayMain from 'components/highway'
import CityMain from 'components/city'

import TrafficDetect from 'pages/TrafficDetect/TrafficDetect'
import Nav from 'components/Nav'
import DigitalTwinPage from 'pages/DigitalTwin/DigitalTwin'
import TrafficFlow from 'pages/TrafficFlow/TrafficFlow'

export default function App() {
  // 智慧交通监测
  let Home = () => {
    return (
      <>
        <Header />
        <Nav />
        <TrafficDetect />
        <TextArea />
      </>
    )
  }

  // 智慧路口信控优化
  const Junction = () => {
    return (
      <>
        {/* <Header />
        <Nav />
        <JunctionMain />
        <TextArea /> */}
      </>
    )
  }
  // 交通流量智能分析
  const Plan = () => {
    return (
      <>
        <Header />
        <Nav />
        <TrafficFlow></TrafficFlow>
        <TextArea />
      </>
    )
  }
  const Cruiser = () => {
    return (
      <>
        <Header />
        <Nav />
        <CruiserMain />
        <TextArea />
      </>
    )
  }
  const Highway = () => {
    return (
      <>
        <Header />
        <Nav />
        <HighwayMain />
        <TextArea />
      </>
    )
  }
  const City = () => {
    return (
      <>
        <Header />
        <Nav />
        <CityMain />
        <TextArea />
      </>
    )
  }
  const DigitalTwin = () => {
    return (
      <>
        <Header />
        <Nav />
        <DigitalTwinPage />
        <TextArea />
      </>
    )
  }
  return (
    <Router>
      <div id='layout'>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/plan' element={<Plan />} />
          <Route path='/junction' element={<Junction />} />
          <Route path='/cruiser' element={<Cruiser />} />
          <Route path='/highway' element={<Highway />} />
          <Route path='/city' element={<City />} />
          <Route path='/digitalTwin' element={<DigitalTwin />} />
        </Routes>
      </div>
    </Router>
  )
}
