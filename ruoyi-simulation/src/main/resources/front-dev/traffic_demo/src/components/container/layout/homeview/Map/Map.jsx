import React from 'react'
import AMapLoader from '@amap/amap-jsapi-loader';
import { useEffect } from 'react';
import styles from '../css/map.module.scss';
export default function Map() {

    useEffect(() => {
        AMapLoader.load({
            key: "d8ab80d5c581443cc3862879e172edff",
            version: "2.0",
            plugins: ['AMap.PolygonEditor', 'AMap.PolyEditor'], // plguins for map
        }).then((AMap) => {

            const map = new AMap.Map("container", {
                // set the center of the map
                resizeEnable: true,
                center: [113.497119, 27.645700],
                zoom: 15,
            });
            const path = [
                [112.916965, 28.228268],
                [112.938825, 28.227897],
                [112.937134, 28.217825],
                [112.910641, 28.218536]

            ]

            const polygon = new AMap.Polygon({
                path: path,
                strokeColor: "green",
                strokeWeight: 6,
                strokeOpacity: 0.2,
                fillOpacity: 0.4,
                fillColor: 'rgba(251, 218, 115, 0.2)',
                zIndex: 50,
                bubble: true,
            })
            // map.add([polygon])
            map.setFitView()
            const polyEditor = new AMap.PolyEditor(map, polygon);
            polyEditor.open();
        });

    }, []);


    return (
        <div className={styles.mapContainer}>
            <div className={styles.title}>地图概览</div>
            <div id="container" style={{ height: "250px", width: "500px" }}></div>
        </div>


    )
}