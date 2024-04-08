import React from 'react';
import twinVideo from '../assets/videos/twin-video.mp4';
import '../css/plan.scss';

export default function Home() {

    return (
        <main>
            <div id="twinContainer">
                <video id="twin" muted src={twinVideo} ></video>
            </div>
            {/* <Centre /> */}
            <div className="rightSide">
            </div>
            <div className='bottomSide'>

            </div>
        </main>
    );
}
