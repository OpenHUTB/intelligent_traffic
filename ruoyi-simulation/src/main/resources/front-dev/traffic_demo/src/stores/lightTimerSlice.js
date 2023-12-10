import { createSlice } from "@reduxjs/toolkit";

const lightTimerSlice = createSlice({
    name: "lightTimer",
    initialState: {
        light1: 5,
        light2: 6,
        light3: 7,
        light4: 8,
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