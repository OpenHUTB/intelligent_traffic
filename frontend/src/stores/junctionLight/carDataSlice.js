// carDataSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = [
  {
    license: '湘A12345',
    averageTime: '1.5',
    averageSpeed: '5',
    averageParking: '2',
  },
  {
    license: '湘A12345',
    averageTime: '1.5',
    averageSpeed: '5',
    averageParking: '2',
  },
  {
    license: '湘A12345',
    averageTime: '1.5',
    averageSpeed: '5',
    averageParking: '2',
  },
  {
    license: '湘A12345',
    averageTime: '1.5',
    averageSpeed: '5',
    averageParking: '2',
  },
];

const carDataSlice = createSlice({
  name: 'carData',
  initialState,
  reducers: {
    setCarData: (state, action) => {
      return action.payload; // 直接返回新的carData数组
    },
  },
});

export const { setCarData } = carDataSlice.actions;
export default carDataSlice.reducer;