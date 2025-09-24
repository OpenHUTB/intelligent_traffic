// controlStrategySlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  day: '星期一', // 初始状态, 可根据需要调整
  time: '平峰(09:00-16:00)', // 初始时段值
  controlMode1: '主路优先',  // 对应第一个select的初始值
  controlMode2: '自动调控',  // 对应第二个select的初始值
};

const controlStrategySlice = createSlice({
  name: 'controlStrategy',
  initialState,
  reducers: {
    setDay: (state, action) => {
      state.day = action.payload;
    },
    setTime: (state, action) => {
      state.time = action.payload;
    },
    setControlMode1: (state, action) => {
      state.controlMode1 = action.payload;
    },
    setControlMode2: (state, action) => {
      state.controlMode2 = action.payload;
    },
    setControlStrategyData: (state, action) => {
      const { day, time, controlMode1, controlMode2 } = action.payload;
      if (day !== undefined) state.day = day;
      if (time !== undefined) state.time = time;
      if (controlMode1 !== undefined) state.controlMode1 = controlMode1;
      if (controlMode2 !== undefined) state.controlMode2 = controlMode2;
    }
  },
});

export const { setDay, setTime, setControlMode1, setControlMode2, setControlStrategyData } = controlStrategySlice.actions;

export default controlStrategySlice.reducer;