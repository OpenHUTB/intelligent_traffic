// ./stores/parameterSlice.js

import { createSlice } from '@reduxjs/toolkit';

// 定义初始状态
const initialState = {
    parameters: {},
};

// 创建 slice
const parameterSlice = createSlice({
    name: 'parameters',
    initialState,
    reducers: {
        updateParameters(state, action) {
            state.parameters = {
                ...state.parameters,
                ...action.payload,
            };
        },
    },
});

// 导出 actions 和 reducer
export const { updateParameters } = parameterSlice.actions;
export default parameterSlice.reducer;