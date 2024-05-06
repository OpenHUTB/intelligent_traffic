import React from 'react';
import { HashRouter as Router, Route, Routes } from 'react-router-dom';
import Header from 'components/Header';
import PlanMain from 'components/PlanMain';
import JunctionMain from 'components/JunctionMain';
import CruiserMain from 'components/CruiserMain';
import HomePage from 'components/home';
import TextArea from 'components/container/TextArea/TextArea';
import HighwayMain from 'components/highway';
export default function App() {
  const Home = () => {
    return (
      <>
        <Header />
        <HomePage />
        <TextArea />
      </>
    );
  };
  const Junction = () => {
    return (
      <>
        <Header />
        <JunctionMain />
        <TextArea />
      </>
    );
  };
  const Plan = () => {
    return (
      <>
        <Header />
        <PlanMain />
        <TextArea />
      </>
    );
  };
  const Cruiser = () => {
    return (
      <>
        <Header />
        <CruiserMain />
        <TextArea />
      </>
    );
  };
  const Highway = () => {
    return (
      <>
        <Header />
        <HighwayMain />
        <TextArea />
      </>
    );
  }
  return (
    <Router>
      <div id="layout">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/plan" element={<Plan />} />
          <Route path="/junction" element={<Junction />} />
          <Route path="/cruiser" element={<Cruiser />} />
          <Route path="/highway" element={<Highway />} />
        </Routes>

      </div>
    </Router>
  )
}
