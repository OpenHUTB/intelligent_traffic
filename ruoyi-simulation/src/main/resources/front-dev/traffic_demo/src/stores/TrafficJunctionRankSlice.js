import { createSlice } from "@reduxjs/toolkit";

const infoSlice = createSlice({
    name: "trafficRank",
    initialState: {
        trafficJunctions: [
            {
                name: "枫林三路",
                status: "严重拥堵",
            },
            {
                name: "咸嘉湖路",
                status: "中度拥堵",
            },
            {
                name: "桐梓坡路",
                status: "轻度拥堵",
            },
            {
                name: "玉兰路",
                status: "正常",
            }]
    },
    reducers: {
        setInfo: (state, action) => {
            state.trafficJunctions = action.payload;
        },
    },
});

export const { setInfo } = infoSlice.actions;
export default infoSlice.reducer;