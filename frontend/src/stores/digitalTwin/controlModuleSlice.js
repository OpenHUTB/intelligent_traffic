import { createSlice } from "@reduxjs/toolkit";

const controlModuleSlice = createSlice({
    name: "controlModule",
    initialState: {
        adaptive: 14,
        dynamic: 7,
        manual: 2,
    },
    reducers: {
        setControlModule: (state, action) => {
            const { adaptive, dynamic, manual } = action.payload;
            state.adaptive = adaptive;
            state.dynamic = dynamic;
            state.manual = manual;
        },
    },
});

export const { setControlModule } = controlModuleSlice.actions;
export default controlModuleSlice.reducer;