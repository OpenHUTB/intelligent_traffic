import { createSlice } from '@reduxjs/toolkit'

// Utility to generate a random time (HH:MM) for today
const generateRandomTimeToday = () => {
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const randomMs = Math.floor(Math.random() * (now.getTime() - today.getTime()))
  const randomTime = new Date(today.getTime() + randomMs)
  return randomTime.toLocaleTimeString('zh-CN', { hour12: false }).slice(0, 5)
}

const initialState = {
  listItems: [
    {
      name: '超低速',
      position: '112.874239，28.235942',
      number: '沪AXXXX',
      status: '普通',
      isAlert: '是',
      isDeal: '否',
      speed: '0',
      time: '07:11',
    },
    {
      name: '异常停车',
      position: '112.877688，28.232243',
      number: '湘AXXXX',
      status: '高',
      isAlert: '是',
      isDeal: '否',
      speed: '0',
      time: '09:01',
    },
    {
      name: '超低速',
      status: '普通',
      position: '112.885978，28.233347',
      number: '湘AXXXX',
      isAlert: '是',
      isDeal: '否',
      speed: '5',
      time: '23:11',
    },
    {
      name: '超低速',
      status: '普通',
      position: '112.876215，28.240124',
      number: '湘EXXXX',
      isAlert: '否',
      isDeal: '否',
      speed: '7',
      time: '09:28',
    },
    {
      name: '异常停车',
      status: '普通',
      position: '112.884459，28.237053',
      number: '湘BXXXX',
      isAlert: '是',
      isDeal: '否',
      speed: '0',
      time: '17:11',
    },
  ],
}

// const initialState = {
//   listItems: [],
// }

const scrollAlertSlice = createSlice({
  name: 'highwayScrollAlert',
  initialState,
  reducers: {
    setHighwayScrollAlertList: (state, action) => {
      state.listItems = action.payload
    },
    addHighwayScrollAlertItem: (state, action) => {
      state.listItems.unshift(action.payload)
    },
    updateHighwayScrollAlertItem: (state, action) => {
      const { index, data } = action.payload
      if (index >= 0 && index < state.listItems.length) {
        state.listItems[index] = { ...state.listItems[index], ...data }
      }
    },
    clearHighwayScrollAlert: (state) => {
      state.listItems = []
    },
  },
})

export const {
  setHighwayScrollAlertList,
  addHighwayScrollAlertItem,
  updateHighwayScrollAlertItem,
  clearHighwayScrollAlert,
} = scrollAlertSlice.actions

export default scrollAlertSlice.reducer
