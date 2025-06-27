import { createSlice } from '@reduxjs/toolkit'

const aiMessageSlice = createSlice({
  name: 'aiMessage',
  initialState: '',
  reducers: {
    setAiMessage: (state, action) => action.payload,
  },
})
export const { setAiMessage } = aiMessageSlice.actions
export default aiMessageSlice.reducer
