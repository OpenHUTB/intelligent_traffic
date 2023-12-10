import { configureStore } from "@reduxjs/toolkit";
import animationReducer from "./stores/animationSlice";
import lightTimerReducer from "./stores/lightTimerSlice";
import infoReducer from "./stores/trafficInfoSlice";
const store = configureStore({
    reducer: {
        animation: animationReducer,
        lightTimer: lightTimerReducer,
        trafficInfo: infoReducer,
    },
});

export default store;