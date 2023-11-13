import React from 'react'
import "../css/centre.scss"
export default function Centre() {
    return (
        <div className="centre">
            <section id="player" className="mainStream">
                <section id="introductionArea" className="intro"></section>
                <section id="audioArea" className="audioPlay"></section>
                <div id="progressArea">
                    <div className="progress">
                        <div className="progress-bar progress-bar-striped bg-success" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                    </div>
                </div>
            </section>
            <div className="funcArea">
                <div className="displayArea">
                    <section className="voiceDetect">
                        <textarea id="tips"></textarea>
                    </section>
                </div>
            </div>
        </div>
    )
}
