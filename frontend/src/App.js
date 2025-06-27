import React from 'react'
import { HashRouter as Router, Route, Routes } from 'react-router-dom'
import Header from 'components/Header'
import PlanMain from 'components/PlanMain'
import JunctionMain from 'components/JunctionMain'
import CruiserMain from 'components/CruiserMain'
import HomePage from 'components/home'
import TextArea from 'components/container/TextArea/TextArea'
import HighwayMain from 'components/highway'
import CityMain from 'components/city'
import DigitalTwinMain from 'components/DigitalTwin'
import TrafficDetect from 'pages/TrafficDetect/TrafficDetect'
import Nav from 'components/Nav'
export default function App() {
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
  const Junction = () => {
    return (
      <>
        <Header />
        <Nav />
        <JunctionMain />
        <TextArea />
      </>
    )
  }
  const Plan = () => {
    return (
      <>
        <Header />
        <Nav />
        <PlanMain />
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
        <DigitalTwinMain />
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
