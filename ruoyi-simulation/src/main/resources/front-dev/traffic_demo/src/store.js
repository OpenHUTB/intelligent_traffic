import { configureStore } from "@reduxjs/toolkit";
import animationReducer from "./stores/animationSlice";
import lightTimerReducer from "./stores/lightTimerSlice";
import infoReducer from "./stores/trafficInfoSlice";
import junctionReducer from "./stores/junctionInfoSlice";

const store = configureStore({
    reducer: {
        animation: animationReducer,
        lightTimer: lightTimerReducer,
        trafficInfo: infoReducer,
        junctionInfo: junctionReducer,
    },
});

export default store;