import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import styles from '../css/nav.module.scss'

export default function Header() {
  const location = useLocation()

  const navItems = [
    { path: '/', label: '智慧交通监测' },
    { path: '/flowai', label: '交通流量智能分析' },
    { path: '/junction', label: '智慧路口信控优化' },
    { path: '/digitalTwin', label: '智慧交通数字孪生' },
    { path: '/gpt', label: '智慧问答辅助决策' },
    { path: '/cruiser', label: '实景三维动态巡查' },
  ]

  const getActiveIndex = () => {
    const currentIndex = navItems.findIndex(
      (item) => item.path === location.pathname
    )
    return currentIndex !== -1 ? currentIndex : 0
  }

  return (
    <div className={styles.nav}>
      <ul className={styles.navList}>
        {navItems.map((item, index) => (
          <li
            key={index}
            className={getActiveIndex() === index ? styles.active : ''}
          >
            <Link to={item.path}>{item.label}</Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
