// junctionInfoSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  district1: '岳麓西大道',
  district2: '旺龙路',
  carLanes: 3,
  pedestrianLanes: 1,
};

const junctionInfoSlice = createSlice({
  name: 'junctionInfo',
  initialState,
  reducers: {
    setJunctionInfo: (state, action) => {
      const { district1, district2, carLanes, pedestrianLanes } = action.payload;
      if (district1 !== undefined) state.district1 = district1;
      if (district2 !== undefined) state.district2 = district2;
      if (carLanes !== undefined) state.carLanes = carLanes;
      if (pedestrianLanes !== undefined) state.pedestrianLanes = pedestrianLanes;
    },
  },
});

export const { setJunctionInfo } = junctionInfoSlice.actions;
export default junctionInfoSlice.reducer;