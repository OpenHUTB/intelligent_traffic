import { configureStore } from "@reduxjs/toolkit";
import animationReducer from "./stores/animationSlice";
import lightTimerReducer from "./stores/lightTimerSlice";
import infoReducer from "./stores/trafficInfoSlice";
import junctionReducer from "./stores/junctionInfoSlice";
import rankReducer from "./stores/TrafficJunctionRankSlice";
import junctionControlReducer from "./stores/junctionControlSlice";
const store = configureStore({
    reducer: {
        animation: animationReducer,
        lightTimer: lightTimerReducer,
        trafficInfo: infoReducer,
        junctionInfo: junctionReducer,
        trafficRank: rankReducer,
        junctionControl: junctionControlReducer,
    },
});

export default store;