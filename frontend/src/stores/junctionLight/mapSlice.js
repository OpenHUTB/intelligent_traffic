// imageDataSlice.js
import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  bigMapShow: false,
  smallMapShow: true,
}

const mapSlice = createSlice({
  name: 'map',
  initialState,
  reducers: {
    setBigMapShow: (state, action) => {
      state.bigMapShow = action.payload
    },
    setSmallMapShow: (state, action) => {
      state.smallMapShow = action.payload
    },
  },
})
export const { setBigMapShow, setSmallMapShow } = mapSlice.actions
export default mapSlice.reducer
