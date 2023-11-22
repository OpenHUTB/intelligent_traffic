import React from 'react';
import Header from 'components/Header';
import PlanMain from 'components/PlanMain';
import JunctionMain from 'components/JunctionMain';
import CruiserMain from 'components/CruiserMain';
import { HashRouter as Router, Route, Routes } from 'react-router-dom';

export default function App() {
  return (
    <Router>
      <div id="layout">
        <Header />
        <Routes>
          <Route path="/" element={<PlanMain />} />
          <Route path="/junction" element={<JunctionMain />} />
          <Route path="/cruiser" element={<CruiserMain />} />
        </Routes>
      </div>
    </Router>
  )
}
