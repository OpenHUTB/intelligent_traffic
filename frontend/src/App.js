import React from 'react'
import { HashRouter as Router, Route, Routes } from 'react-router-dom'
import Header from 'components/Header'
import TextArea from 'components/container/TextArea/TextArea'
import TrafficDetect from 'pages/TrafficDetect/TrafficDetect'
import Nav from 'components/Nav'
import DigitalTwinPage from 'pages/DigitalTwin/DigitalTwin'
import TrafficFlow from 'pages/TrafficFlow/TrafficFlow'
import JunctionLight from 'pages/JunctionLight/JunctionLight'
import CruiserPage from 'pages/CruiserPage/CruiserPage'
import GPT from 'pages/GPT/GPT'
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
        <Header />
        <Nav />
        <JunctionLight></JunctionLight>
        <TextArea />
      </>
    )
  }
  // 交通流量智能分析
  const FlowAI = () => {
    return (
      <>
        <Header />
        <Nav />
        <TrafficFlow></TrafficFlow>
        <TextArea />
      </>
    )
  }
  // 实景三维动态巡查
  const Cruiser = () => {
    return (
      <>
        <Header />
        <Nav />
        <CruiserPage />
        <TextArea />
      </>
    )
  }

  // 智慧交通数字孪生
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
  // 智慧问答辅助决策
  const Gpt = () => {
    return (
      <>
        <Header />
        <Nav />
        <GPT />
        <TextArea />
      </>
    )
  }
  return (
    <Router>
      <div id='layout'>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/flowai' element={<FlowAI />} />
          <Route path='/junction' element={<Junction />} />
          <Route path='/cruiser' element={<Cruiser />} />
          <Route path='/gpt' element={<Gpt />} />
          <Route path='/digitalTwin' element={<DigitalTwin />} />
        </Routes>
      </div>
    </Router>
  )
}
