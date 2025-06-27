import { createSlice } from "@reduxjs/toolkit";

const jamIndexSlice = createSlice({
    name: "controlModule",
    initialState: [{
        name: "旺龙路",
        speed: 22,
        trend: 18.88,
        index: 7,
    }, {
        name: "旺龙路",
        speed: 25,
        trend: 288,
        index: 6
    }],
    reducers: {
        setJamIndex: (state, action) => {
            return [...action.payload];
        },
    },
});

export const { setJamIndex } = jamIndexSlice.actions;
export default jamIndexSlice.reducer;