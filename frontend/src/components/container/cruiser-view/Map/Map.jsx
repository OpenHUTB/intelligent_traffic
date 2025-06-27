import React from 'react'
import AMapLoader from '@amap/amap-jsapi-loader';
import { useEffect } from 'react';
import './Map.scss'
export default function Map() {

    useEffect(() => {
        AMapLoader.load({
            key: "d8ab80d5c581443cc3862879e172edff",
            version: "2.0",
            plugins: ['AMap.MoveAnimation', 'AMap.Geocoder'], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
        }).then((AMap) => {

            AMap.plugin('AMap.MoveAnimation', () => {
                const lineArr = [[116.478935, 39.997761], [116.478939, 39.997825],
                [116.478912, 39.998549], [116.478912, 39.998549], [116.478998, 39.998555],
                [116.478998, 39.998555], [116.479282, 39.99856], [116.479658, 39.998528],
                [116.480151, 39.998453], [116.480784, 39.998302], [116.480784, 39.998302],
                [116.481149, 39.998184], [116.481573, 39.997997], [116.481863, 39.997846],
                [116.482072, 39.997718], [116.482362, 39.997718], [116.483633, 39.998935],
                [116.48367, 39.998968], [116.484648, 39.999861]];

                const map = new AMap.Map("container", {
                    // 设置地图容器id
                    resizeEnable: true,
                    center: [116.397428, 39.90923],
                    zoom: 17,
                    viewMode: '3D' // 初始化地图中心点位置
                });

                // 绘制轨迹
                const marker = new AMap.Marker({
                    map: map,
                    position: lineArr[0],
                    icon: "https://a.amap.com/jsapi_demos/static/demo-center-v2/car.png",
                    offset: new AMap.Pixel(-13, -26),
                });

                // 绘制轨迹
                const polyline = new AMap.Polyline({
                    map: map,
                    path: lineArr,
                    showDir: true,
                    strokeColor: "#28F",  //线颜色
                    // strokeOpacity: 1,     //线透明度
                    strokeWeight: 6,      //线宽
                    // strokeStyle: "solid"  //线样式
                });

                const passedPolyline = new AMap.Polyline({
                    map: map,
                    strokeColor: "#AF5",  //线颜色
                    strokeWeight: 6,      //线宽
                });

                marker.on('moving', (e) => {
                    passedPolyline.setPath(e.passedPath);
                    map.setCenter(e.target.getPosition(), true)
                });

                map.setFitView();
                // Start the animation when the component is mounted
                startAnimation(marker, lineArr);
                console.log('地图加载成功');
            })
        });

    }, []);


    const startAnimation = (marker, lineArr) => {
        marker.moveAlong(lineArr, {
            duration: 1000,
            autoRotation: true,
        });
    };

    return (
        <div id="container" style={{ height: "500px", width: "300px" }}></div>
    )
}