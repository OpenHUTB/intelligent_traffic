import React from 'react'
import AMapLoader from '@amap/amap-jsapi-loader'
import { useEffect, useRef } from 'react'
import styles from './css/map.module.scss'
import trafficIcon from 'assets/icon/trafficLight.png'
import { useSelector, useDispatch } from 'react-redux'
// import { updateMapDisplay } from 'stores/mapSlice';
const getSpeedColor = (speed) => {
  const value = parseInt(speed)
  if (value < 20) return '#FF4444'
  if (value < 40) return '#FFD700'
  return '#00FF00'
}

export default function Map() {
  const dispatch = useDispatch()
  // const mapDisplay = useSelector(state => state.map.mapDisplay);
  const selectedPosition = useSelector((state) => state.map.selectedPosition)
  const mapRef = useRef(null)
  const redDotMarkerRef = useRef(null)
  const mapDisplay = true
  const mapInstanceRef = useRef(null) // 新增：保存地图实例的引用
  const aMapLibRef = useRef(null) // 新增：保存 AMap 库的引用
  // ...pos数组保持不变...
  const pos = [
    [112.876533, 28.233528], //旺龙路与青山路交叉口
    [112.873317, 28.228683], //旺龙路与岳麓西大道交叉口
    [112.883476, 28.231231], //尖山路与青山路交叉口
    [112.891312, 28.230781], //望青路与青山路交叉口
    [112.885194, 28.236462], //旺龙路与尖山路交叉口
    [112.891585, 28.225725], //望青路与岳麓西大道交叉口
  ]

  // 定义路段速度数据
  const segmentSpeeds = [
    { position: [112.874925, 28.231105], speed: '25km/h', value: 25 },
    { position: [112.887528, 28.225882], speed: '35km/h', value: 35 },
    { position: [112.887394, 28.230496], speed: '18km/h', value: 18 },
    { position: [112.888371, 28.233413], speed: '42km/h', value: 42 },
    { position: [112.880524, 28.236542], speed: '30km/h', value: 30 },
  ]

  useEffect(() => {
    let map
    AMapLoader.load({
      key: 'd8ab80d5c581443cc3862879e172edff',
      version: '2.0',
      plugins: [
        'AMap.Polyline',
        'AMap.Marker',
        'AMap.Text',
        'AMap.Buildings',
        // 'AMap.Weather',
        'AMap.ControlBar',
        'AMap.MoveAnimation',
        'AMap.TileLayer', // 核心插件
        'AMap.TileLayer.Satellite', // 卫星图插件
        'AMap.TileLayer.RoadNet', // 路网插件
      ],
    }).then((AMap) => {
      aMapLibRef.current = AMap // 保存 AMap 库的引用
      map = new AMap.Map('container1', {
        resizeEnable: true,
        center: [112.883476, 28.231231],
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
        layers: [
          // 基础图层配置
          new AMap.TileLayer.Satellite(),
          //   new AMap.TileLayer.RoadNet(),
        ],
      })
      mapInstanceRef.current = map // 保存地图实例到 ref
      // 3D建筑物增强
      // 调整建筑物颜色
      // 修改建筑物配置
      const buildings = new AMap.Buildings({
        zIndex: 20,
        zooms: [16, 20],
        heightFactor: 2.5, // 增加高度比例
        color: 'rgb(26, 25, 25)', // 修改颜色
        color2: 'rgba(50, 50, 50, 1)', // 侧面颜色
        footColor: 'rgba(80, 80, 80, 1)', // 底部颜色
        path: 'https://a.amap.com/jsapi_demos/static/images/buildings/', // 自定义纹理
      })
      map.add(buildings)

      // 动态路径线
      const path = [
        [112.876533, 28.233528], // 旺龙路与青山路交叉口
        [112.873317, 28.228683], // 旺龙路与岳麓西大道交叉口
        [112.88257, 28.226039], // 中间点
        [112.891585, 28.225725], // 望青路与岳麓西大道
        // [112.883476,28.231231], // 尖山路与青山路
        [112.891312, 28.230781], // 望青路与青山路
        [112.889549, 28.234045], // 中间点1
        [112.887241, 28.236049], // 中间点2
        [112.885194, 28.236462], // 旺龙路与尖山路
        //返回初始点
        [112.879854, 28.236623], // 中间点
        [112.878064, 28.235917], // 中间点
        [112.876533, 28.233528], // 旺龙路与青山路交叉口
      ].map((p) => new AMap.LngLat(...p))

      const polyline = new AMap.Polyline({
        path: path,
        strokeColor: 'blue',
        strokeWeight: 14,
        lineJoin: 'round',
      })
      map.add(polyline)

      // 动态交通灯标记
      pos.forEach((p, i) => {
        const marker = new AMap.Marker({
          position: p,
          icon: new AMap.Icon({
            image: trafficIcon,
            size: new AMap.Size(25, 25),
            imageSize: new AMap.Size(25, 25),
          }),
          offset: new AMap.Pixel(-16, -16),
          animation: 'AMAP_ANIMATION_DROP',
          extData: { id: `light-${i + 1}` },
        })

        // 鼠标交互
        marker.on('mouseover', () => {
          marker.setzIndex(100)
          //   glowOverlay.show()
          polyline.setOptions({
            strokeColor: '#FF69B4',
            strokeWeight: 8,
          })
        })

        marker.on('mouseout', () => {
          //   glowOverlay.hide()
          polyline.setOptions({
            strokeColor: '#00FFFF',
            strokeWeight: 6,
          })
        })

        map.add(marker)
      })
      new AMap.Marker({
        position: [112.883476, 28.231231],
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
        offset: new AMap.Pixel(-10, -10), // 精确居中
      }).addTo(map)
      // 动态速度标签
      segmentSpeeds.forEach((segment) => {
        new AMap.Text({
          position: segment.position,
          text: segment.speed,
          style: {
            background: `linear-gradient(45deg, ${getSpeedColor(
              segment.value
            )}, #000)`,
            'border-radius': '15px',
            color: '#FFF',
            'font-size': '16px',
            'box-shadow': '0 4px 12px rgba(0,0,0,0.3)',
          },
          offset: new AMap.Pixel(0, -10),
          angle: -5,
        }).addTo(map)
      })

      // 修正后的动画逻辑
      const initAnimations = () => {
        // 自动旋转动画
        let rotation = -30
        const animateRotation = () => {
          rotation = (rotation + 0.1) % 360
          // map.setRotation(rotation)
          // requestAnimationFrame(animateRotation)
        }
        // 视角动画
        map.setStatus({
          animateEnable: true,
          center: [112.883476, 28.231231],
          zoom: 16,
          pitch: 55,
          rotation: -25,
          animationDuration: 2000,
        })

        // setTimeout(animateRotation, 2000)
      }
      map.on('complete', initAnimations)

      // 添加水面效果
      map.setFeatures(['bg', 'road', 'building', 'water'])

      // 添加卫星图层混合
      // 更推荐的图层混合方式
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

      map.add(baseLayers)
    })

    return () => {
      map?.destroy()
      mapInstanceRef.current = null // 清理时重置
      aMapLibRef.current = null // 清理时重置 AMap 库引用
    }
  }, [])

  return (
    <div className={styles.mapContainer}>
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
          height: '800px',
          width: '100%',
          borderRadius: '16px',
          boxShadow: '0 8px 24px rgba(0,255,255,0.2)',
          background: 'linear-gradient(45deg, #1a1a2e, #16213e)',
        }}
      />
    </div>
  )
}
