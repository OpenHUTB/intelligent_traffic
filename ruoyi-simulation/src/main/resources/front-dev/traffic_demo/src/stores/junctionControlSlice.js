import { createSlice } from "@reduxjs/toolkit";

const junctionControlSlice = createSlice({
    name: "junctionControlSlice",
    initialState: {
        name: "旺龙路与岳麓大道交叉口",
        status: "在线",
        controlPattern: "预设方案一",
    },
    reducers: {
        setJunctionControl: (state, action) => {
            const { name, status, controlPattern } = action.payload;
            state.name = name;
            state.status = status;
            state.controlPattern = controlPattern;
        },
    },
});

export const { setJunctionControl } = junctionControlSlice.actions;
export default junctionControlSlice.reducer;