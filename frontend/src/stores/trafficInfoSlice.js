import { createSlice } from "@reduxjs/toolkit";

const infoSlice = createSlice({
    name: "trafficInfo",
    initialState: {
        index: 0,
        road: 0,
        congestion: 0,
        time: 0,
    },
    reducers: {
        setInfo: (state, action) => {
            return { ...state, ...action.payload }
        },
    },
});

export const { setInfo } = infoSlice.actions;
export default infoSlice.reducer;
