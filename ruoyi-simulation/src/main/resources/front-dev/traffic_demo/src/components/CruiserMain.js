import React from 'react';
import Centre from './Centre.js';
import CustomMap from './container/cruiser-view/Map/Map';

import '../css/junction.scss';

export default function CruiserMain() {
    return (
        <main>
            <div className="leftSide">
                <CustomMap />
            </div>
            {/* <Centre /> */}
            <div className="rightSide">

            </div>
        </main>
    )
}