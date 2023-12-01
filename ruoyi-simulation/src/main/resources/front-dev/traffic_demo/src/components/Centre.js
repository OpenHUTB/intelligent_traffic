import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

import TextArea from './container/TextArea/TextArea';
import videoForJunction from '../assets/videos/junction-view.mp4';
import videoForStart from '../assets/videos/start-view.mp4';
import "../css/centre.scss";
export default function Centre({ }) {
    const location = useLocation();
    const [currentVideo, setCurrentVideo] = useState();
    const pathName = location.pathname;
    useEffect(() => {
        console.log(location.pathname);
        if (location.pathname === '/Junction') {
            setCurrentVideo(videoForJunction);
        } else if (location.pathname === '/Plan') {

            setCurrentVideo(videoForStart);
        } else if (location.pathname === '/Cruiser') {
            setCurrentVideo(videoForStart);
        } else {
            setCurrentVideo(videoForStart);
        }
    }, [location]);

    return (
        <>
            <div className="centre">
                <section id="player" className="mainStream">
                    {/* Conditional rendering of video based on the currentVideo state */}
                    <video src={currentVideo} autoPlay loop muted ></video>
                    {/* Rest of your Centre component */}
                </section>
            </div>
            <TextArea pathName={pathName} />
        </>


    )
}
