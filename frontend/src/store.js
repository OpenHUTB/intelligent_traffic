import { configureStore } from '@reduxjs/toolkit'
import animationReducer from './stores/animationSlice'
import lightTimerReducer from './stores/lightTimerSlice'
import infoReducer from './stores/trafficInfoSlice'
// import junctionReducer from "./stores/junctionInfoSlice";
import rankReducer from './stores/TrafficJunctionRankSlice'
import junctionControlReducer from './stores/junctionControlSlice'
import parameterReducer from './stores/parameterSlice'
import ControlModuleReducer from './stores/digitalTwin/controlModuleSlice'
import JamIndexReducer from './stores/digitalTwin/jamIndexSlice'
import analysisReducer from './stores/digitalTwin/analysisSlice'
import OptimiseOverviewReducer from './stores/digitalTwin/optimiseOverviewSlice'
import wholeIndexReducer from './stores/digitalTwin/wholeIndexSlice'
import signalRoadReducer from './stores/digitalTwin/signalRoadSlice'
import signalJunctionReducer from './stores/digitalTwin/signalJunctionSlice'
import scrollAlertReducer from './stores/digitalTwin/scrollAlertSlice'
import LightControlReducer from './stores/junctionLight/lightControlSlice'
import ControlStrategyReducer from './stores/junctionLight/controlStrategySlice'
import junctionInfoReudcer from './stores/junctionLight/junctionInfoSlice'
import evaluationReducer from './stores/junctionLight/evaluationSlice'
import resultTrackReducer from './stores/junctionLight/resultTrackSlice'
import carDataReducer from './stores/junctionLight/carDataSlice'
import imageDataReducer from './stores/junctionLight/imageDataSlice'
import aiMessageReducer from './stores/junctionLight/aiMessageSlice'
import mapReducer from './stores/junctionLight/mapSlice'
import positionReducer from './stores/junctionLight/positionSlice'
import timeShowReducer from './stores/junctionLight/timeShowSlice'

const store = configureStore({
  reducer: {
    animation: animationReducer,
    lightTimer: lightTimerReducer,
    trafficInfo: infoReducer,
    trafficRank: rankReducer,
    junctionControl: junctionControlReducer,
    parameters: parameterReducer,
    controlModule: ControlModuleReducer,
    jamIndex: JamIndexReducer,
    analysis: analysisReducer,
    optimiseOverview: OptimiseOverviewReducer,
    wholeIndex: wholeIndexReducer,
    signalRoad: signalRoadReducer,
    signalJunction: signalJunctionReducer,
    scrollAlert: scrollAlertReducer,
    lightControl: LightControlReducer,
    controlStrategy: ControlStrategyReducer,
    junctionInfo: junctionInfoReudcer,
    evaluation: evaluationReducer,
    resultTrack: resultTrackReducer,
    carData: carDataReducer,
    imageData: imageDataReducer,
    aiMessage: aiMessageReducer,
    map: mapReducer,
    position: positionReducer,
    timeShow: timeShowReducer,
  },
})

export default store
