import React, { useState, useEffect } from 'react'
import './index.scss'
import AMapLoader from '@amap/amap-jsapi-loader'
import TimeIcon from 'assets/image/time.png'
import { ReactComponent as SunIcon } from 'assets/icon/icon-sun.svg'
import { ReactComponent as CloudIcon } from 'assets/icon/icon-cloud.svg'
import { ReactComponent as RainIcon } from 'assets/icon/icon-rain.svg'
import { ReactComponent as SnowIcon } from 'assets/icon/icon-snow.svg'

export default function Weather() {
  const [currentTime, setCurrentTime] = useState(
    new Date().toLocaleTimeString()
  )
  const [weatherStr, setWeatherStr] = useState('')
  const date = new Date()
    .toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    })
    .replace(/\//g, '.')
  const dayOfWeek = new Date().toLocaleDateString('zh-CN', {
    weekday: 'long',
  })
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date().toLocaleTimeString())
    }, 1000)

    return () => {
      clearInterval(timer)
    }
  }, [])

  return (
    <div className='weather-time-container'>
      <div className='time-contianer'>
        <div className='date'>
          <img className='timeIcon' src={TimeIcon} alt='' />
          <div className='date'>{date}</div>
          <div className='time'>{currentTime}</div>
        </div>
      </div>
    </div>
  )
}
