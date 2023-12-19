import { createSlice } from "@reduxjs/toolkit";

const trafficFlowSlice = createSlice({
    name: "trafficFlowSlice",
    initialState: {
        grade: "B",
        queueLength: 5.1,
        parkingTimes: 3,
        averageDelay: 40,
    },
    reducers: {
        setJunctioninfo: (state, action) => {
            return { ...state, ...action.payload }
        },
    },
});
