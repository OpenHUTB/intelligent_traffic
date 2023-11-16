import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import "../css/centre.scss"

import videoForPlan from '../assets/videos/plan-view.mp4';
import videoForJunction from '../assets/videos/junction-view.mp4';

export default function Centre({ video }) {
    const location = useLocation();
    const [currentVideo, setCurrentVideo] = useState(video);

    useEffect(() => {
        if (location.pathname === '/') {
            setCurrentVideo(videoForPlan); // Change this to your actual video source for the Junction
        } else {
            setCurrentVideo(videoForJunction); // Change this to your actual video source for the Plan
        }
    }, [location]);

    return (
        <div className="centre">
            <section id="player" className="mainStream">
                {/* Conditional rendering of video based on the currentVideo state */}
                <video src={currentVideo} autoPlay loop></video>
                {/* Rest of your Centre component */}
            </section>
        </div>
    )
}
