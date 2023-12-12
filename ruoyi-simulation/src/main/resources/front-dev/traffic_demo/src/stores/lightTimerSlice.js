import { createSlice } from "@reduxjs/toolkit";

const lightTimerSlice = createSlice({
    name: "lightTimer",
    initialState: {
        light1: { seconds: 1, isGreen: true },
        light2: { seconds: 1, isGreen: false },
        light3: { seconds: 1, isGreen: true },
        light4: { seconds: 1, isGreen: false },
    },
    reducers: {
        setLightTimer: (state, action) => {
            const { light, value } = action.payload;
            state[light] = value;
        },
    },
});

export const { setLightTimer } = lightTimerSlice.actions;
export default lightTimerSlice.reducer;