import { createSlice } from '@reduxjs/toolkit'

const infoSlice = createSlice({
  name: 'trafficInfo',
  initialState: {
    index: 2.1,
    road: 2,
    congestion: 2,
    time: 5.5,
  },
  reducers: {
    setInfo: (state, action) => {
      const payload = action.payload || {}
      // merge only keys that are defined in payload to avoid overwriting with undefined
      Object.keys(payload).forEach((key) => {
        if (payload[key] !== undefined) state[key] = payload[key]
      })
      return state
    },
  },
})

export const { setInfo } = infoSlice.actions
export default infoSlice.reducer
