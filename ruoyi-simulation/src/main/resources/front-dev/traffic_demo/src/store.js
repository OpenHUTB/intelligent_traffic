import { configureStore } from "@reduxjs/toolkit";
import animationReducer from "./stores/animationSlice";

const store = configureStore({
    reducer: {
        animation: animationReducer,
    },
});

export default store;