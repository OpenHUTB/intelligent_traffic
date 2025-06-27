// imageDataSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = [
  {
    src: 'https://via.placeholder.com',
    title: '岳麓大道口',
    time: '15:22:33',
  },
  {
    src: 'https://via.placeholder.com',
    title: '岳麓大道口',
    time: '15:22:33',
  }
];

const imageDataSlice = createSlice({
  name: 'imageData',
  initialState,
  reducers: {
    setImages: (state, action) => {
      return action.payload; // 直接返回新的images数组
    },
  },
});

export const { setImages } = imageDataSlice.actions;
export default imageDataSlice.reducer;