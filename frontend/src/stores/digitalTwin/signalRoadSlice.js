// signalRoadSlice.js

import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    listItems: [
        {
            name: '青山路',
            index: 1,
            speed: 20,
            trend: 18.53,
            trendDirection: true,
        },
        {
            name: '旺龙路',
            index: 3,
            speed: 30,
            trend: 20.53,
            trendDirection: true,
        },
        {
            name: '岳麓西大道',
            index: 4,
            speed: 40,
            trend: 15.53,
            trendDirection: false,
        },
        {
            name: '尖山路',
            index: 2,
            speed: 50,
            trend: 11.53,
            trendDirection: false,
        },
    ],
};

const signalRoadSlice = createSlice({
    name: 'signalRoad',
    initialState,
    reducers: {
        setListItems: (state, action) => {
            state.listItems = action.payload;
        },
    },
});

export const { setListItems } = signalRoadSlice.actions;

export default signalRoadSlice.reducer;