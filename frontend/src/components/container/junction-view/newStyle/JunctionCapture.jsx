import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { setCarData } from 'stores/junctionLight/carDataSlice'
import { setImages } from 'stores/junctionLight/imageDataSlice'
import preVideo from 'assets/videos/PRE.mp4'
import optVideo from 'assets/videos/OPT.mp4'
import styles from './css/junctionCapture.module.scss'

export default function JunctionCapture() {
  const dispatch = useDispatch()

  // 从Redux中获取carData和images数据
  const carData = useSelector((state) => state.carData)
  const images = useSelector((state) => state.imageData)

  useEffect(() => {
    const handleCarDataChanged = (event) => {
      console.log('Junction Car Data Changed:', event.detail)
      // event.detail 应该是一个数组: [{license: 'xxx', averageTime: 'xx', averageSpeed: 'xx', averageParking: 'xx'}, ...]
      dispatch(setCarData(event.detail))
    }

    const handleImageDataChanged = (event) => {
      console.log('Junction Image Data Changed:', event.detail)
      // event.detail 应为一个数组: [{src: 'xxx', title: 'xxx', time: 'xx:xx:xx'}, ...]
      dispatch(setImages(event.detail))
    }

    window.addEventListener('junctionCarDataChanged', handleCarDataChanged)
    window.addEventListener('junctionImageDataChanged', handleImageDataChanged)

    return () => {
      window.removeEventListener('junctionCarDataChanged', handleCarDataChanged)
      window.removeEventListener(
        'junctionImageDataChanged',
        handleImageDataChanged
      )
    }
  }, [dispatch])

  // 当图片加载失败时调用的函数，将图片替换为public/images/1.jpg
  const handleImageError = (e) => {
    console.log('要求写死不更改图片路径，所以不处理图片加载失败的情况')
  }

  const renderList = carData.map((item, index) => (
    <div className={styles.listItem} key={index}>
      <span className={styles.license}>{item.license}</span>
      <span className={styles.averageTime}>行程时间 {item.averageTime},</span>
      <span className={styles.averageSpeed}>
        平均车速 {item.averageSpeed} km/h
      </span>
      <span className={styles.averageParking}>
        停车 {item.averageParking} 次
      </span>
    </div>
  ))

  const renderImages = images.map((img, index) => (
    <div className={styles.imgContainer} key={index}>
      <img src={img.src} onError={handleImageError} />
      <span className={styles.imgTitle}>
        {img.title} : {img.time}
      </span>
    </div>
  ))

  return (
    <div className={styles.junctionCapture}>
      <header className={styles.title}>
        <span>控制算法孪生推演</span>
      </header>
      {/* <section className={styles.list}>
                {renderList}
            </section> */}
      <section className={styles.pictures}>
        {/* {renderImages}
         */}
        <div className={styles.imgContainer}>
          <video id='PRE' muted src={preVideo}></video>
          {/* <span className={styles.imgTitle}>控制方案（优化前）</span> */}
        </div>
        <div className={styles.imgContainer}>
          <video id='OPT' muted src={optVideo}></video>
          {/* <span className={styles.imgTitle}>控制方案（优化后）</span> */}
        </div>
      </section>
    </div>
  )
}
