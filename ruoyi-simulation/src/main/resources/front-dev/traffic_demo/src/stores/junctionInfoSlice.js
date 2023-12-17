import { createSlice } from "@reduxjs/toolkit";

const junctionInfoSlice = createSlice({
    name: "junctionInfoSlice",
    initialState: {
        grade: "A",
        queueLength: 20.7,
        parkingTimes: 15,
        averageDelay: 23,
    },
    reducers: {
        setJunctioninfo: (state, action) => {
            return { ...state, ...action.payload }
        },
    },
});

export const { setJunctioninfo } = junctionInfoSlice.actions;
export default junctionInfoSlice.reducer;