import React from 'react';
import Header from './components/Header';
import PlanMain from './components/PlanMain';
import JunctionMain from './components/JunctionMain';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
export default function App() {
  return (
    <Router>
      <div id="layout">
        <Header />
        <Routes>
          <Route path="/" element={<PlanMain />} />
          <Route path="/junction" element={<JunctionMain />} />
        </Routes>
      </div>
    </Router>
  )
}
