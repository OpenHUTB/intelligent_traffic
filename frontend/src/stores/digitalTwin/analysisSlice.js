import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    violationList: [
        { name: '在线路口', value: 185 },
        { name: '离线路口', value: 15 },
    ],
    optimiseList: [
        { name: '优化路口占比(%)', value: 76 },
        { name: '优化次数(次)', value: 8 },
        { name: '服务车次（万辆）', value: 0.3 },
        { name: '路网车辆 (万辆)', value: 1 },
    ],
};

const analysisSlice = createSlice({
    name: 'analysis',
    initialState,
    reducers: {
        setViolationList: (state, action) => {
            state.violationList = action.payload; // Ensure payload is an array
        },
        setOptimiseList: (state, action) => {
            state.optimiseList = action.payload; // Ensure payload is an array
        },
    },
});

export const { setViolationList, setOptimiseList } = analysisSlice.actions;

export default analysisSlice.reducer;