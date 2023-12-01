import React from 'react';
import Header from 'components/Header';
import PlanMain from 'components/PlanMain';
import JunctionMain from 'components/JunctionMain';
import CruiserMain from 'components/CruiserMain';
import { HashRouter as Router, Route, Routes } from 'react-router-dom';
import FirstMinute from 'components/FirstMinute';
import FirstHeader from 'components/FirstHeader';
import './websoketManager';
import TextArea from 'components/container/TextArea/TextArea';
export default function App() {
  const href = window.location.href;
  const Home = () => {
    return (
      <>
        <FirstHeader />
        <FirstMinute />
      </>
    );
  };
  const Junction = () => {
    return (
      <>
        <Header />
        <JunctionMain />
      </>
    );
  };
  const Plan = () => {
    return (
      <>
        <Header />
        <PlanMain />
      </>
    );
  };
  const Cruiser = () => {
    return (
      <>
        <Header />
        <CruiserMain />
      </>
    );
  };
  return (
    <Router>
      <div id="layout">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/plan" element={<Plan />} />
          <Route path="/junction" element={<Junction />} />
          <Route path="/cruiser" element={<Cruiser />} />
        </Routes>
        <TextArea href={href} />
      </div>
    </Router>
  )
}
