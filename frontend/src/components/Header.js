import React from 'react'
import { Link } from 'react-router-dom'
import '../css/header.scss'
import Weather from 'components/container/Weather/Weather'
export default function Header() {
  return (
    <header className='header'>
      {/* <ul className='nav-left'>
        <li>
          <Link to='/'>智慧交通监测</Link>
        </li>
        <li>
          <Link to='/plan'>交通流量智能分析</Link>
        </li>
        <li>
          <Link to='/junction'>智慧路口信控优化</Link>
        </li>
        <li>
          <Link to='/highway'>智慧交通数字孪生</Link>
        </li>
        <li>
          <Link to='/city'>智慧问答辅助决策</Link>
        </li>
        <li>
          <Link to='/digitalTwin'>实景三维动态巡查</Link>
        </li>
      </ul> */}
      <div className='logo'></div>
      <div className='title'>
        <h2 className='title-1'>基于数字孪生的交通大模型</h2>
      </div>
      <div className='weather'>
        <Weather />
      </div>
    </header>
  )
}
