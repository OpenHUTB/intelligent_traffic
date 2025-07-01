// trafficMonitorSlice.js

import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    congestionIndex: 3, // Initial value as per your original useState
    speed: 50,          // Initial value as per your original useState
};

const wholeIndexSlice = createSlice({
    name: 'wholeIndexSlice',
    initialState,
    reducers: {
        setWholeIndex: (state, action) => {
            const { congestionIndex, speed } = action.payload;
            state.congestionIndex = congestionIndex;
            state.speed = speed;
        },
    },
});

export const { setWholeIndex } = wholeIndexSlice.actions;

export default wholeIndexSlice.reducer;