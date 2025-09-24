import React from 'react'
import AMapLoader from '@amap/amap-jsapi-loader'
import { useEffect, useRef } from 'react'
import styles from './index.module.scss'
import trafficIcon from 'assets/icon/trafficLight.png'
import alertIcon from 'assets/image/alertIcon.png'
import { useSelector, useDispatch } from 'react-redux'

const getSpeedColor = (speed) => {
  const value = parseInt(speed)
  if (isNaN(value)) return '#888888'
  if (value < 20) return '#FF4444'
  if (value < 40) return '#FFD700'
  return '#00FF00'
}

// 验证并清理坐标数据
const validateAndCleanCoordinates = (coords) => {
  if (!Array.isArray(coords) || coords.length !== 2) return null
  const [lng, lat] = coords
  const cleanLng = parseFloat(lng)
  const cleanLat = parseFloat(lat)

  if (isNaN(cleanLng) || isNaN(cleanLat)) return null
  if (cleanLng < -180 || cleanLng > 180) return null
  if (cleanLat < -90 || cleanLat > 90) return null

  return [cleanLng, cleanLat]
}

export default function Amap({ isVisible = true }) {
  const dispatch = useDispatch()
  const selectedPosition = useSelector(
    (state) => state.map?.selectedPosition || null
  )
  const mapRef = useRef(null)
  const redDotMarkerRef = useRef(null)
  const mapInstanceRef = useRef(null)
  const aMapLibRef = useRef(null)
  const markersRef = useRef([])
  const polylineRef = useRef(null) // 添加polyline的ref

  // 完整的位置数据
  const pos = [
    [112.876533, 28.233528], //旺龙路与青山路交叉口
    [112.873317, 28.228683], //旺龙路与岳麓西大道交叉口
    [112.883476, 28.231231], //尖山路与青山路交叉口
    [112.891312, 28.230781], //望青路与青山路交叉口
    [112.885194, 28.236462], //旺龙路与尖山路交叉口
    [112.891585, 28.225725], //望青路与岳麓西大道交叉口
  ]
    .map(validateAndCleanCoordinates)
    .filter(Boolean)

  // 完整的路段速度数据
  const segmentSpeeds = [
    {
      position: [112.88986559863608, 28.233521759437362],
      speed: '25km/h',
      value: 25,
    },
    {
      position: [112.88162777880416, 28.236600120864875],
      speed: '35km/h',
      value: 35,
    },
    {
      position: [112.89138394718731, 28.22981448138176],
      speed: '18km/h',
      value: 18,
    },
    {
      position: [112.88309825500113, 28.228717157479597],
      speed: '42km/h',
      value: 42,
    },
    {
      position: [112.8796528812412, 28.232420220010862],
      speed: '30km/h',
      value: 30,
    },
  ]
    .map((segment) => {
      const cleanPos = validateAndCleanCoordinates(segment.position)
      return cleanPos ? { ...segment, position: cleanPos } : null
    })
    .filter(Boolean)

  // 使用 segmentSpeeds 的坐标作为黄色点位
  const yellowPositions = segmentSpeeds.map((s) => s?.position).filter(Boolean)

  // 当地图变为可见时，触发resize
  useEffect(() => {
    if (isVisible && mapInstanceRef.current) {
      setTimeout(() => {
        mapInstanceRef.current.getSize()
        mapInstanceRef.current.resize()
      }, 100)
    }
  }, [isVisible])

  useEffect(() => {
    let map = null
    let isDestroyed = false
    let handleMapClick = null // 引用以便卸载
    // 发送坐标到后端的工具函数
    const sendCoordinates = async (lng, lat) => {
      try {
        const payload = { longitude: lng, latitude: lat }
        console.log('Sending coordinates to backend:', payload)
        const res = await fetch(
          'http://localhost:8080/simulation/section/transferJunction',
          {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
          }
        )
        if (!res.ok) {
          console.warn('Backend responded with non-OK status', res.status)
        } else {
          const data = await res.json().catch(() => null)
          console.log('Backend response:', data ?? 'no json body')
        }
      } catch (err) {
        console.error('Failed to send coordinates:', err)
      }
    }

    const initMap = async () => {
      try {
        const AMap = await AMapLoader.load({
          key: 'd8ab80d5c581443cc3862879e172edff',
          version: '2.0',
          plugins: [
            'AMap.Polyline',
            'AMap.Marker',
            'AMap.Text',
            'AMap.Buildings',
            'AMap.ControlBar',
            'AMap.MoveAnimation',
            'AMap.TileLayer',
            'AMap.TileLayer.Satellite',
            'AMap.TileLayer.RoadNet',
          ],
        })

        if (isDestroyed) return

        aMapLibRef.current = AMap

        // 验证中心点坐标
        const centerCoord = validateAndCleanCoordinates([112.883476, 28.231231])
        if (!centerCoord) {
          console.error('Invalid center coordinates')
          return
        }

        // 创建地图
        map = new AMap.Map('container1', {
          resizeEnable: true,
          center: centerCoord,
          zoom: 16,
          viewMode: '3D',
          pitch: 40,
          rotation: 25,
          mapStyle: 'amap://styles/6b3bfb6d0d8ae9758a0beb4b5c900f3a', // 科技蓝主题
          buildingAnimation: true,
          features: ['bg', 'road', 'building', 'water'],
          // 增强光照效果
          light: {
            color: 'white', // 光源颜色
            intensity: 1.2, // 光照强度
            direction: [1, 0, -0.5], // 光照方向
          },
        })

        if (isDestroyed) {
          map?.destroy()
          return
        }

        mapInstanceRef.current = map

        // 地图点击事件: 获取经纬度并发送到后端
        handleMapClick = (e) => {
          if (!e || !e.lnglat) return
          // 高德返回的是 AMap.LngLat 实例
          const lng = Number(e.lnglat.getLng?.() ?? e.lnglat.lng)
          const lat = Number(e.lnglat.getLat?.() ?? e.lnglat.lat)
          if (isNaN(lng) || isNaN(lat)) {
            console.warn('Invalid click coordinates:', e.lnglat)
            return
          }
          sendCoordinates(lng, lat)
        }
        map.on('click', handleMapClick)

        // 等待地图完全加载后再添加覆盖物
        map.on('complete', () => {
          if (isDestroyed) return

          try {
            // 3D建筑物增强
            const buildings = new AMap.Buildings({
              zIndex: 20,
              zooms: [16, 20],
              heightFactor: 2.5, // 增加高度比例
              color: 'rgb(26, 25, 25)', // 修改颜色
              color2: 'rgba(50, 50, 50, 1)', // 侧面颜色
              footColor: 'rgba(80, 80, 80, 1)', // 底部颜色
            })
            map.add(buildings)

            // 动态路径线
            const pathCoords = [
              [112.876533, 28.233528], // 旺龙路与青山路交叉口
              [112.873317, 28.228683], // 旺龙路与岳麓西大道交叉口
              [112.88257, 28.226039], // 中间点
              [112.891585, 28.225725], // 望青路与岳麓西大道
              [112.891312, 28.230781], // 望青路与青山路
              [112.889549, 28.234045], // 中间点1
              [112.887241, 28.236049], // 中间点2
              [112.885194, 28.236462], // 旺龙路与尖山路
              //返回初始点
              [112.879854, 28.236623], // 中间点
              [112.878064, 28.235917], // 中间点
              [112.876533, 28.233528], // 旺龙路与青山路交叉口
            ]
              .map(validateAndCleanCoordinates)
              .filter(Boolean)

            let polyline = null // 在这个作用域中定义polyline

            if (pathCoords.length > 1) {
              const validPath = pathCoords.map(
                ([lng, lat]) => new AMap.LngLat(lng, lat)
              )

              polyline = new AMap.Polyline({
                path: validPath,
                strokeColor: 'blue',
                strokeWeight: 14,
                lineJoin: 'round',
              })
              map.add(polyline)
              markersRef.current.push(polyline)
              polylineRef.current = polyline // 保存到ref中
            }

            // 动态交通灯标记
            pos.forEach((coord, i) => {
              if (!coord) return

              try {
                const [lng, lat] = coord
                const marker = new AMap.Marker({
                  position: new AMap.LngLat(lng, lat),
                  icon: new AMap.Icon({
                    image: trafficIcon,
                    size: new AMap.Size(25, 25),
                    imageSize: new AMap.Size(25, 25),
                  }),
                  offset: new AMap.Pixel(-16, -16),
                  animation: 'AMAP_ANIMATION_DROP',
                  extData: { id: `light-${i + 1}` },
                })

                // 鼠标交互 - 使用ref中的polyline
                marker.on('mouseover', () => {
                  marker.setzIndex(100)
                  if (polylineRef.current) {
                    polylineRef.current.setOptions({
                      strokeColor: '#FF69B4',
                      strokeWeight: 8,
                    })
                  }
                })

                marker.on('mouseout', () => {
                  if (polylineRef.current) {
                    polylineRef.current.setOptions({
                      strokeColor: '#00FFFF',
                      strokeWeight: 6,
                    })
                  }
                })

                map.add(marker)
                markersRef.current.push(marker)
              } catch (error) {
                console.warn(`创建交通灯标记 ${i} 失败:`, error, coord)
              }
            })

            // 添加中心脉冲点
            try {
              const centerMarker = new AMap.Marker({
                position: new AMap.LngLat(centerCoord[0], centerCoord[1]),
                content: `
                  <div style="
                    width: 20px;
                    height: 20px;
                    background: #ff4757;
                    border: 2px solid white;
                    border-radius: 50%;
                    box-shadow: 0 0 12px rgba(255,71,87,0.6);
                    animation: pulse 1.5s infinite;
                  ">
                    <style>
                      @keyframes pulse {
                        0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(255,71,87,0.7); }
                        70% { transform: scale(1); box-shadow: 0 0 0 12px rgba(255,71,87,0); }
                        100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(255,71,87,0); }
                      }
                    </style>
                  </div>
                `,
                offset: new AMap.Pixel(-10, -10),
              })
              map.add(centerMarker)
              markersRef.current.push(centerMarker)
            } catch (error) {
              console.warn('创建中心点标记失败:', error)
            }

            // 移除速度文本展示，改为使用黄色点（下方已有黄色点标记逻辑）

            // 使用警告图标标记（alertIcon）
            try {
              yellowPositions.forEach((coord, idx) => {
                const [lng, lat] = coord
                const alertMarker = new AMap.Marker({
                  position: new AMap.LngLat(lng, lat),
                  icon: new AMap.Icon({
                    image: alertIcon,
                    size: new AMap.Size(36, 36),
                    imageSize: new AMap.Size(36, 36),
                  }),
                  offset: new AMap.Pixel(-12, -12),
                  zIndex: 130,
                })
                map.add(alertMarker)
                markersRef.current.push(alertMarker)
              })
            } catch (error) {
              console.warn('创建警告图标标记失败:', error)
            }

            // 修正后的动画逻辑
            const initAnimations = () => {
              // 视角动画
              map.setStatus({
                animateEnable: true,
                center: centerCoord,
                zoom: 16,
                pitch: 55,
                rotation: -25,
                animationDuration: 2000,
              })
            }

            // 添加水面效果
            map.setFeatures(['bg', 'road', 'building', 'water'])

            // 添加卫星图层混合
            try {
              const baseLayers = [
                new AMap.TileLayer.Satellite({
                  detectRetina: true, // 视网膜屏优化
                  zIndex: 1,
                }),
                new AMap.TileLayer.RoadNet({
                  zIndex: 10,
                  opacity: 0.9,
                }),
              ]
              // 分开添加，避免一次性数组添加导致潜在销毁问题
              baseLayers.forEach((layer) => map.add(layer))
              markersRef.current.push(...baseLayers)
            } catch (layerErr) {
              console.warn('添加底图图层失败:', layerErr)
            }

            // 延迟执行动画
            setTimeout(initAnimations, 1000)
          } catch (error) {
            console.error('地图初始化过程中出错:', error)
          }
        })

        // 添加错误处理
        map.on('error', (error) => {
          console.error('地图错误:', error)
        })
      } catch (error) {
        console.error('AMap 加载失败:', error)
      }
    }

    initMap()

    return () => {
      isDestroyed = true

      // 卸载点击事件
      if (mapInstanceRef.current && handleMapClick) {
        try {
          mapInstanceRef.current.off('click', handleMapClick)
        } catch (e) {
          console.warn('移除点击事件失败:', e)
        }
      }

      // 清理所有标记
      if (markersRef.current.length > 0) {
        try {
          if (
            mapInstanceRef.current &&
            typeof mapInstanceRef.current.remove === 'function'
          ) {
            // 过滤掉已经被移除的对象
            const overlays = markersRef.current.filter(Boolean)
            if (overlays.length) mapInstanceRef.current.remove(overlays)
          }
        } catch (error) {
          console.warn('批量移除覆盖物时出错:', error)
          // 回退逐个移除
          markersRef.current.forEach((ov) => {
            try {
              mapInstanceRef.current?.remove?.(ov)
            } catch (_) {}
          })
        }
        markersRef.current = []
      }

      // 清理polyline引用
      polylineRef.current = null

      // 销毁地图
      if (map && typeof map.destroy === 'function') {
        try {
          map.destroy()
        } catch (error) {
          console.warn('销毁地图时出错:', error)
        }
      }

      mapInstanceRef.current = null
      aMapLibRef.current = null
    }
  }, [])

  return (
    <div className={styles.mapContainer}>
      {/* 可以取消注释添加图例 */}
      {/* <div className={styles.title}>
        <h2>区域3D地图</h2>
        <div className={styles.legend}>
          <span style={{ background: '#FF4444' }}>拥堵</span>
          <span style={{ background: '#FFD700' }}>缓行</span>
          <span style={{ background: '#00FF00' }}>畅通</span>
        </div>
      </div> */}
      <div
        id='container1'
        style={{
          height: '100%',
          width: '100%',
          borderRadius: '16px',
          boxShadow: '0 8px 24px rgba(0,255,255,0.2)',
          background: 'linear-gradient(45deg, #1a1a2e, #16213e)',
        }}
      />
    </div>
  )
}
