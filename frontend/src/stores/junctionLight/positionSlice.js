// 创建mapSlice.js
import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  selectedPosition: null, // 格式: [lng, lat]
}

const positionSlice = createSlice({
  name: 'position',
  initialState,
  reducers: {
    setSelectedPosition: (state, action) => {
      state.selectedPosition = action.payload
    },
    clearSelectedPosition: (state) => {
      state.selectedPosition = null
    },
  },
})

export const { setSelectedPosition, clearSelectedPosition } =
  positionSlice.actions
export default positionSlice.reducer
