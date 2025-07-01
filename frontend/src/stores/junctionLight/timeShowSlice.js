// imageDataSlice.js
import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  timeProgressShow: false,
  timeChangeShow: false,
}

const timeShowSlice = createSlice({
  name: 'timeShow',
  initialState,
  reducers: {
    setTimeProgressShow: (state, action) => {
      state.timeProgressShow = action.payload
    },
    setTimeChangeShow: (state, action) => {
      state.timeChangeShow = action.payload
    },
  },
})
export const { setTimeProgressShow, setTimeChangeShow } = timeShowSlice.actions

export default timeShowSlice.reducer
