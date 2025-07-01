import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  averageDelay: 2,
  congestionMileage: 7.011,
  averageSpeed: 5.211,
  delayDirection: true,
  mileageDirection: false,
  speedDirection: false,
};

const optimiseOverviewSlice = createSlice({
  name: 'optimiseOverview',
  initialState,
  reducers: {
    setOptimiseOverview: (state, action) => {
      const { averageDelay, congestionMileage, averageSpeed, delayDirection, mileageDirection,speedDirection } = action.payload;
      state.averageDelay = averageDelay;
      state.congestionMileage = congestionMileage;
      state.averageSpeed = averageSpeed;
      state.delayDirection = delayDirection;
      state.mileageDirection = mileageDirection;
      state.speedDirection = speedDirection;
    },
  },
});

export const { setOptimiseOverview } = optimiseOverviewSlice.actions;

export default optimiseOverviewSlice.reducer;