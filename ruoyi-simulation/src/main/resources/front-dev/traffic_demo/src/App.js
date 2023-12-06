import React from 'react';
import Header from 'components/Header';
import PlanMain from 'components/PlanMain';
import JunctionMain from 'components/JunctionMain';
import CruiserMain from 'components/CruiserMain';
import { HashRouter as Router, Route, Routes } from 'react-router-dom';
import FirstMinute from 'components/FirstMinute';
import FirstHeader from 'components/FirstHeader';
import TextArea from 'components/container/TextArea/TextArea';

export default function App() {
  const Home = () => {
    return (
      <>
        <FirstHeader />
        <FirstMinute />
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
  return (
    <Router>
      <div id="layout">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/plan" element={<Plan />} />
          <Route path="/junction" element={<Junction />} />
          <Route path="/cruiser" element={<Cruiser />} />
        </Routes>

      </div>
    </Router>
  )
}
