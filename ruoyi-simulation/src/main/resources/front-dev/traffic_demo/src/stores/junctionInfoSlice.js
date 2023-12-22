import { createSlice } from "@reduxjs/toolkit";

const junctionInfoSlice = createSlice({
    name: "junctionInfoSlice",
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

export const { setJunctioninfo } = junctionInfoSlice.actions;
export default junctionInfoSlice.reducer;