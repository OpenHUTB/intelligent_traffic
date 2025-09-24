// evaluationSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  preIndex: 10,      // 
  optIndex: 5,     // 
  preSpeed: 15,  // 
  optSpeed: 25,
  preContent: "工作日场景，晴天模式，手动调控，直行与左转同级控制",// 
  optContent: "周末晚高峰场景，夜晚雨天模式， 自动调优，直行优先" //
};

const evaluationSlice = createSlice({
  name: 'evaluation',
  initialState,
  reducers: {
    setEvaluationData: (state, action) => {
      const { preIndex, optIndex, preSpeed, optSpeed, preContent, optContent } = action.payload;
      state.preIndex = preIndex;
      state.optIndex = optIndex;
      state.preSpeed = preSpeed;
      state.optSpeed = optSpeed;
      state.preContent = preContent;
      state.optContent = optContent;
    },
  },
});

export const { setEvaluationData } = evaluationSlice.actions;
export default evaluationSlice.reducer;