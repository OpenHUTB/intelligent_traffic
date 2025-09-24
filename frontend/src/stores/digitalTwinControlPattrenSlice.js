import { createSlice } from "@reduxjs/toolkit";

const digitalTwinControlPatternSlice = createSlice({
    name: "digitalTwinControlPatternSlice",
    initialState: {
        auto: "14",
        flexible: "7",
        manual: "2",
    },
    reducers: {
        setDigitalTwinControlPattern: (state, action) => {
            const { name, status, controlPattern } = action.payload;
            state.name = name;
            state.status = status;
            state.controlPattern = controlPattern;
        },
    },
});

export const { digitalTwinControlPattern } = digitalTwinControlPatternSlice.actions;
export default digitalTwinControlPatternSlice.reducer;