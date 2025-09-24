import { createSlice } from '@reduxjs/toolkit'

const timeLabels = [
  '00:00',
  '01:00',
  '02:00',
  '03:00',
  '04:00',
  '05:00',
  '06:00',
  '07:00',
  '08:00',
  '09:00',
  '10:00',
  '11:00',
  '12:00',
]

const initialState = {
  timeLabels,
  current: [120, 115, 110, 108, 105, 100, 150, 180, 170, 160, 155, 150],
  weekly: [100, 95, 92, 90, 88, 85, 120, 140, 135, 130, 125, 120],
}

const flowAnalysisSlice = createSlice({
  name: 'flowAnalysis',
  initialState,
  reducers: {
    setFlowAnalysisSeries: (state, action) => {
      const { current, weekly } = action.payload || {}
      if (Array.isArray(current)) state.current = current
      if (Array.isArray(weekly)) state.weekly = weekly
    },
  },
})

export const { setFlowAnalysisSeries } = flowAnalysisSlice.actions
export default flowAnalysisSlice.reducer
