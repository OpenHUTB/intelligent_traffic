import { createSlice } from '@reduxjs/toolkit'
const districtSpeedSlice = createSlice({
  name: 'districtSpeed',
  initialState: {
    congestionIndex: 3,
    speed: 30,
  },
  reducers: {
    setDistrictSpeed: (state, action) => {
      return { ...state, ...action.payload }
    },
  },
})

export const { setDistrictSpeed } = districtSpeedSlice.actions

export default districtSpeedSlice.reducer
