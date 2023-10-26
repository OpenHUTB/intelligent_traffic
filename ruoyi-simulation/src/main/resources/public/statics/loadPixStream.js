// Copyright Epic Games, Inc. All Rights Reserved.

var webRtcPlayerObj = null;
var print_stats = false;
var print_inputs = false;
var connect_on_load = false;

var is_reconnection = false;
var ws;
const WS_OPEN_STATE = 1;

var qualityControlOwnershipCheckBox;
var matchViewportResolution;
// TODO: Remove this - workaround because of bug causing UE to crash when switching resolutions too quickly
var lastTimeResized = new Date().getTime();
var resizeTimeout;

var onDataChannelConnected;
var responseEventListeners = new Map();

var shouldShowPlayOverlay = true;
// A freeze frame is a still JPEG image shown instead of the video.
var freezeFrame = {
	receiving: false,
	size: 0,
	jpeg: undefined,
	height: 0,
	width: 0,
	valid: false
};

// Optionally detect if the user is not interacting (AFK) and disconnect them.
var afk = {
	enabled: false,   // Set to true to enable the AFK system.
	warnTimeout: 120,   // The time to elapse before warning the user they are inactive.
	closeTimeout: 10,   // The time after the warning when we disconnect the user.

	active: false,   // Whether the AFK system is currently looking for inactivity.
	overlay: undefined,   // The UI overlay warning the user that they are inactive.
	warnTimer: undefined,   // The timer which waits to show the inactivity warning overlay.
	countdown: 0,   // The inactivity warning overlay has a countdown to show time until disconnect.
	countdownTimer: undefined,   // The timer used to tick the seconds shown on the inactivity warning overlay.
}

// If the user focuses on a UE4 input widget then we show them a button to open
// the on-screen keyboard. JavaScript security means we can only show the
// on-screen keyboard in response to a user interaction.
var editTextButton = undefined;

// A hidden input text box which is used only for focusing and opening the
// on-screen keyboard.
var hiddenInput = undefined;

var t0 = Date.now();
function log(str) {
	console.log(`${Math.floor(Date.now() - t0)}: ` + str);
}

function setupHtmlEvents() {
	//Window events
	window.addEventListener('orientationchange', onOrientationChange);

	let prioritiseQualityCheckbox = document.getElementById('prioritise-quality-tgl');
	let qualityParamsSubmit = document.getElementById('quality-params-submit');

	if (prioritiseQualityCheckbox !== null) {
		prioritiseQualityCheckbox.onchange = function (event) {
			if (prioritiseQualityCheckbox.checked) {
				// TODO: This state should be read from the UE Application rather than from the initial values in the HTML
				let lowBitrate = document.getElementById('low-bitrate-text').value;
				let highBitrate = document.getElementById('high-bitrate-text').value;
				let minFPS = document.getElementById('min-fps-text').value;

				let initialDescriptor = {
					PrioritiseQuality: 1,
					LowBitrate: lowBitrate,
					HighBitrate: highBitrate,
					MinFPS: minFPS
				};
				// TODO: The descriptor should be sent as is to a generic handler on the UE side
				// but for now we're just sending it as separate console commands
				//emitUIInteraction(initialDescriptor);
				sendQualityConsoleCommands(initialDescriptor);
				console.log(initialDescriptor);

				qualityParamsSubmit.onclick = function (event) {
					let lowBitrate = document.getElementById('low-bitrate-text').value;
					let highBitrate = document.getElementById('high-bitrate-text').value;
					let minFPS = document.getElementById('min-fps-text').value;
					let descriptor = {
						PrioritiseQuality: 1,
						LowBitrate: lowBitrate,
						HighBitrate: highBitrate,
						MinFPS: minFPS
					};
					//emitUIInteraction(descriptor);
					sendQualityConsoleCommands(descriptor);
					console.log(descriptor);
				};
			} else { // Prioritise Quality unchecked
				let initialDescriptor = {
					PrioritiseQuality: 0
				};
				//emitUIInteraction(initialDescriptor);
				sendQualityConsoleCommands(initialDescriptor);
				console.log(initialDescriptor);

				qualityParamsSubmit.onclick = null;
			}
		};
	}

	let showFPSButton = document.getElementById('show-fps-button');
	if (showFPSButton !== null) {
		showFPSButton.onclick = function (event) {
			let consoleDescriptor = {
				Console: 'stat fps'
			};
			emitUIInteraction(consoleDescriptor);
		};
	}

	let matchViewportResolutionCheckBox = document.getElementById('match-viewport-res-tgl');
	if (matchViewportResolutionCheckBox !== null) {
		matchViewportResolutionCheckBox.onchange = function (event) {
			matchViewportResolution = matchViewportResolutionCheckBox.checked;
		};
	}
}

function sendQualityConsoleCommands(descriptor) {
	if (descriptor.PrioritiseQuality !== null) {
		let command = 'Streamer.PrioritiseQuality ' + descriptor.PrioritiseQuality;
		let consoleDescriptor = {
			Console: command
		};
		emitUIInteraction(consoleDescriptor);
	}

	if (descriptor.LowBitrate !== null) {
		let command = 'Streamer.LowBitrate ' + descriptor.LowBitrate;
		let consoleDescriptor = {
			Console: command
		};
		emitUIInteraction(consoleDescriptor);
	}

	if (descriptor.HighBitrate !== null) {
		let command = 'Streamer.HighBitrate ' + descriptor.HighBitrate;
		let consoleDescriptor = {
			Console: command
		};
		emitUIInteraction(consoleDescriptor);
	}

	if (descriptor.MinFPS !== null) {
		var command = 'Streamer.MinFPS ' + descriptor.MinFPS;
		let consoleDescriptor = {
			Console: command
		};
		emitUIInteraction(consoleDescriptor);
	}
}

function setOverlay(htmlClass, htmlElement, onClickFunction) {
	var videoPlayOverlay = document.getElementById('videoPlayOverlay');
	if (!videoPlayOverlay) {
		var playerDiv = document.getElementById('player');
		videoPlayOverlay = document.createElement('div');
		videoPlayOverlay.id = 'videoPlayOverlay';
		playerDiv.appendChild(videoPlayOverlay);
	}

	// Remove existing html child elements so we can add the new one
	while (videoPlayOverlay.lastChild) {
		videoPlayOverlay.removeChild(videoPlayOverlay.lastChild);
	}

	if (htmlElement)
		videoPlayOverlay.appendChild(htmlElement);

	if (onClickFunction) {
		videoPlayOverlay.addEventListener('click', function onOverlayClick(event) {
			onClickFunction(event);
			videoPlayOverlay.removeEventListener('click', onOverlayClick);
		});
	}

	// Remove existing html classes so we can set the new one
	var cl = videoPlayOverlay.classList;
	for (var i = cl.length - 1; i >= 0; i--) {
		cl.remove(cl[i]);
	}

	videoPlayOverlay.classList.add(htmlClass);
}

function showTextOverlay(text) {
	var textOverlay = document.createElement('div');
	textOverlay.id = 'messageOverlay';
	textOverlay.innerHTML = text ? text : '';
	setOverlay('textDisplayState', textOverlay);
}

function updateAfkOverlayText() {
	afk.overlay.innerHTML = '<center>No activity detected<br>Disconnecting in ' + afk.countdown + ' seconds<br>Click to continue<br></center>';
}

function showAfkOverlay() {
	// Pause the timer while the user is looking at the inactivity warning overlay.
	stopAfkWarningTimer();

	// Show the inactivity warning overlay.
	afk.overlay = document.createElement('div');
	afk.overlay.id = 'afkOverlay';
	setOverlay('clickableState', afk.overlay, event => {
		// The user clicked so start the timer again and carry on.
		clearInterval(afk.countdownTimer);
	});

	afk.countdown = afk.closeTimeout;
	updateAfkOverlayText();

	if (inputOptions.controlScheme == ControlSchemeType.LockedMouse) {
		document.exitPointerLock();
	}

	afk.countdownTimer = setInterval(function () {
		afk.countdown--;
		if (afk.countdown == 0) {
			// The user failed to click so disconnect them.
			ws.close();
		} else {
			// Update the countdown message.
			updateAfkOverlayText();
		}
	}, 1000);
}

// Stop the timer which when elapsed will warn the user they are inactive.
function stopAfkWarningTimer() {
	afk.active = false;
}

function createWebRtcOffer() {
	if (webRtcPlayerObj) {
		console.log('Creating offer');
		webRtcPlayerObj.createOffer();
	} else {
		console.log('WebRTC player not setup, cannot create offer');
	}
}

function sendInputData(data) {
	if (webRtcPlayerObj) {
		webRtcPlayerObj.send(data);
	}
}

function addResponseEventListener(name, listener) {
	responseEventListeners.set(name, listener);
}

function removeResponseEventListener(name) {
	responseEventListeners.remove(name);
}

// Must be kept in sync with PixelStreamingProtocol::EToClientMsg C++ enum.
const ToClientMessageType = {
	QualityControlOwnership: 0,
	Response: 1,
	Command: 2,
	FreezeFrame: 3,
	UnfreezeFrame: 4,
	VideoEncoderAvgQP: 5
};

var VideoEncoderQP = "N/A";

function setupWebRtcPlayer(htmlElement, config) {
	webRtcPlayerObj = new webRtcPlayer({ peerConnectionOptions: config.peerConnectionOptions });
	htmlElement.appendChild(webRtcPlayerObj.video);

	webRtcPlayerObj.onWebRtcOffer = function (offer) {
		if (ws && ws.readyState === WS_OPEN_STATE) {
			let offerStr = JSON.stringify(offer);
			console.log(`-> SS: offer:\n${offerStr}`);
			ws.send(offerStr);
		}
	};

	webRtcPlayerObj.onWebRtcCandidate = function (candidate) {
		if (ws && ws.readyState === WS_OPEN_STATE) {
			console.log(`-> SS: iceCandidate\n${JSON.stringify(candidate, undefined, 4)}`);
			ws.send(JSON.stringify({ type: 'iceCandidate', candidate: candidate }));
		}
	};

	registerInputs(webRtcPlayerObj.video);

	// On a touch device we will need special ways to show the on-screen keyboard.
	if ('ontouchstart' in document.documentElement) {
		createOnScreenKeyboardHelpers(htmlElement);
	}

	createWebRtcOffer();

	return webRtcPlayerObj.video;
}

function onWebRtcAnswer(webRTCData) {
	webRtcPlayerObj.receiveAnswer(webRTCData);

	let printInterval = 5 * 60 * 1000; /*Print every 5 minutes*/
	let nextPrintDuration = printInterval;

	webRtcPlayerObj.onAggregatedStats = (aggregatedStats) => {
		let numberFormat = new Intl.NumberFormat(window.navigator.language, { maximumFractionDigits: 0 });
		let timeFormat = new Intl.NumberFormat(window.navigator.language, { maximumFractionDigits: 0, minimumIntegerDigits: 2 });

		// Calculate duration of run
		let runTime = (aggregatedStats.timestamp - aggregatedStats.timestampStart) / 1000;
		let timeValues = [];
		let timeDurations = [60, 60];
		for (let timeIndex = 0; timeIndex < timeDurations.length; timeIndex++) {
			timeValues.push(runTime % timeDurations[timeIndex]);
			runTime = runTime / timeDurations[timeIndex];
		}
		timeValues.push(runTime);

		let runTimeSeconds = timeValues[0];
		let runTimeMinutes = Math.floor(timeValues[1]);
		let runTimeHours = Math.floor([timeValues[2]]);

		receivedBytesMeasurement = 'B';
		receivedBytes = aggregatedStats.hasOwnProperty('bytesReceived') ? aggregatedStats.bytesReceived : 0;
		let dataMeasurements = ['kB', 'MB', 'GB'];
		for (let index = 0; index < dataMeasurements.length; index++) {
			if (receivedBytes < 100 * 1000)
				break;
			receivedBytes = receivedBytes / 1000;
			receivedBytesMeasurement = dataMeasurements[index];
		}
	};

	webRtcPlayerObj.aggregateStats(1 * 1000 /*Check every 1 second*/);

	//let displayStats = () => { webRtcPlayerObj.getStats( (s) => { s.forEach(stat => { console.log(JSON.stringify(stat)); }); } ); }
	//var displayStatsIntervalId = setInterval(displayStats, 30 * 1000);
}

function onWebRtcIce(iceCandidate) {
	if (webRtcPlayerObj)
		webRtcPlayerObj.handleCandidateFromServer(iceCandidate);
}

var styleWidth;
var styleHeight;
var styleTop;
var styleLeft;
var styleCursor = 'default';
var styleAdditional;

const ControlSchemeType = {
	// A mouse can lock inside the WebRTC player so the user can simply move the
	// mouse to control the orientation of the camera. The user presses the
	// Escape key to unlock the mouse.
	LockedMouse: 0,

	// A mouse can hover over the WebRTC player so the user needs to click and
	// drag to control the orientation of the camera.
	HoveringMouse: 1
};

var inputOptions = {
	// The control scheme controls the behaviour of the mouse when it interacts
	// with the WebRTC player.
	controlScheme: ControlSchemeType.LockedMouse,

	// Browser keys are those which are typically used by the browser UI. We
	// usually want to suppress these to allow, for example, UE4 to show shader
	// complexity with the F5 key without the web page refreshing.
	suppressBrowserKeys: true,

	// UE4 has a faketouches option which fakes a single finger touch when the
	// user drags with their mouse. We may perform the reverse; a single finger
	// touch may be converted into a mouse drag UE4 side. This allows a
	// non-touch application to be controlled partially via a touch device.
	fakeMouseWithTouches: false
};

function updateVideoStreamSize() {
	if (!matchViewportResolution) {
		return;
	}

	var now = new Date().getTime();
	if (now - lastTimeResized > 1000) {
		var playerElement = document.getElementById('player');
		if (!playerElement)
			return;

		let descriptor = {
			Console: 'setres ' + playerElement.clientWidth + 'x' + playerElement.clientHeight
		};
		emitUIInteraction(descriptor);
		console.log(descriptor);
		lastTimeResized = new Date().getTime();
	}
	else {
		console.log('Resizing too often - skipping');
		clearTimeout(resizeTimeout);
		resizeTimeout = setTimeout(updateVideoStreamSize, 1000);
	}
}

// Fix for bug in iOS where windowsize is not correct at instance or orientation change
// https://github.com/dimsemenov/PhotoSwipe/issues/1315
var _orientationChangeTimeout;
function onOrientationChange(event) {
	clearTimeout(_orientationChangeTimeout);
	_orientationChangeTimeout = setTimeout(function () {
	}, 500);
}

// Must be kept in sync with PixelStreamingProtocol::EToUE4Msg C++ enum.
const MessageType = {

	/**********************************************************************/

	/*
	 * Control Messages. Range = 0..49.
	 */
	IFrameRequest: 0,
	RequestQualityControl: 1,
	MaxFpsRequest: 2,
	AverageBitrateRequest: 3,
	StartStreaming: 4,
	StopStreaming: 5,

	/**********************************************************************/

	/*
	 * Input Messages. Range = 50..89.
	 */

	// Generic Input Messages. Range = 50..59.
	UIInteraction: 50,
	Command: 51,

	// Keyboard Input Message. Range = 60..69.
	KeyDown: 60,
	KeyUp: 61,
	KeyPress: 62,

	// Mouse Input Messages. Range = 70..79.
	MouseEnter: 70,
	MouseLeave: 71,
	MouseDown: 72,
	MouseUp: 73,
	MouseMove: 74,
	MouseWheel: 75,

	// Touch Input Messages. Range = 80..89.
	TouchStart: 80,
	TouchEnd: 81,
	TouchMove: 82

	/**************************************************************************/
};

// A generic message has a type and a descriptor.
function emitDescriptor(messageType, descriptor) {
	// Convert the dscriptor object into a JSON string.
	let descriptorAsString = JSON.stringify(descriptor);

	// Add the UTF-16 JSON string to the array byte buffer, going two bytes at
	// a time.
	let data = new DataView(new ArrayBuffer(1 + 2 + 2 * descriptorAsString.length));
	let byteIdx = 0;
	data.setUint8(byteIdx, messageType);
	byteIdx++;
	data.setUint16(byteIdx, descriptorAsString.length, true);
	byteIdx += 2;
	for (i = 0; i < descriptorAsString.length; i++) {
		data.setUint16(byteIdx, descriptorAsString.charCodeAt(i), true);
		byteIdx += 2;
	}
	sendInputData(data.buffer);
}

// A UI interation will occur when the user presses a button powered by
// JavaScript as opposed to pressing a button which is part of the pixel
// streamed UI from the UE4 client.
function emitUIInteraction(descriptor) {
	emitDescriptor(MessageType.UIInteraction, descriptor);
}


function emitCommand(descriptor) {
	emitDescriptor(MessageType.Command, descriptor);
}

var playerElementClientRect = undefined;
var normalizeAndQuantizeUnsigned = undefined;
var normalizeAndQuantizeSigned = undefined;

function setupNormalizeAndQuantize() {
	let playerElement = document.getElementById('player');
	let videoElement = playerElement.getElementsByTagName("video");

	if (playerElement && videoElement.length > 0) {
		let playerAspectRatio = playerElement.clientHeight / playerElement.clientWidth;
		let videoAspectRatio = videoElement[0].videoHeight / videoElement[0].videoWidth;

		if (playerAspectRatio > videoAspectRatio) {
			if (print_inputs) {
				console.log('Setup Normalize and Quantize for playerAspectRatio > videoAspectRatio');
			}
			let ratio = playerAspectRatio / videoAspectRatio;
			// Unsigned.
			normalizeAndQuantizeUnsigned = (x, y) => {
				let normalizedX = x / playerElement.clientWidth;
				let normalizedY = ratio * (y / playerElement.clientHeight - 0.5) + 0.5;
				if (normalizedX < 0.0 || normalizedX > 1.0 || normalizedY < 0.0 || normalizedY > 1.0) {
					return {
						inRange: false,
						x: 65535,
						y: 65535
					};
				} else {
					return {
						inRange: true,
						x: normalizedX * 65536,
						y: normalizedY * 65536
					};
				}
			};
			unquantizeAndDenormalizeUnsigned = (x, y) => {
				let normalizedX = x / 65536;
				let normalizedY = (y / 65536 - 0.5) / ratio + 0.5;
				return {
					x: normalizedX * playerElement.clientWidth,
					y: normalizedY * playerElement.clientHeight
				};
			};
			// Signed.
			normalizeAndQuantizeSigned = (x, y) => {
				let normalizedX = x / (0.5 * playerElement.clientWidth);
				let normalizedY = (ratio * y) / (0.5 * playerElement.clientHeight);
				return {
					x: normalizedX * 32767,
					y: normalizedY * 32767
				};
			};
		} else {
			if (print_inputs) {
				console.log('Setup Normalize and Quantize for playerAspectRatio <= videoAspectRatio');
			}
			let ratio = videoAspectRatio / playerAspectRatio;
			// Unsigned.
			normalizeAndQuantizeUnsigned = (x, y) => {
				let normalizedX = ratio * (x / playerElement.clientWidth - 0.5) + 0.5;
				let normalizedY = y / playerElement.clientHeight;
				if (normalizedX < 0.0 || normalizedX > 1.0 || normalizedY < 0.0 || normalizedY > 1.0) {
					return {
						inRange: false,
						x: 65535,
						y: 65535
					};
				} else {
					return {
						inRange: true,
						x: normalizedX * 65536,
						y: normalizedY * 65536
					};
				}
			};
			unquantizeAndDenormalizeUnsigned = (x, y) => {
				let normalizedX = (x / 65536 - 0.5) / ratio + 0.5;
				let normalizedY = y / 65536;
				return {
					x: normalizedX * playerElement.clientWidth,
					y: normalizedY * playerElement.clientHeight
				};
			};
			// Signed.
			normalizeAndQuantizeSigned = (x, y) => {
				let normalizedX = (ratio * x) / (0.5 * playerElement.clientWidth);
				let normalizedY = y / (0.5 * playerElement.clientHeight);
				return {
					x: normalizedX * 32767,
					y: normalizedY * 32767
				};
			};
		}
	}
}

function registerInputs(playerElement) {
	if (!playerElement)
		return;
}

function createOnScreenKeyboardHelpers(htmlElement) {
	if (document.getElementById('hiddenInput') === null) {
		hiddenInput = document.createElement('input');
		hiddenInput.id = 'hiddenInput';
		hiddenInput.maxLength = 0;
		htmlElement.appendChild(hiddenInput);
	}

	if (document.getElementById('editTextButton') === null) {
		editTextButton = document.createElement('button');
		editTextButton.id = 'editTextButton';
		editTextButton.innerHTML = 'edit text';
		htmlElement.appendChild(editTextButton);

		// Hide the 'edit text' button.
		editTextButton.classList.add('hiddenState');

		editTextButton.addEventListener('click', function () {
			// Show the on-screen keyboard.
			hiddenInput.focus();
		});
	}
}

function showOnScreenKeyboard(command) {
	if (command.showOnScreenKeyboard) {
		// Show the 'edit text' button.
		editTextButton.classList.remove('hiddenState');
		// Place the 'edit text' button near the UE4 input widget.
		let pos = unquantizeAndDenormalizeUnsigned(command.x, command.y);
		editTextButton.style.top = pos.y.toString() + 'px';
		editTextButton.style.left = (pos.x - 40).toString() + 'px';
	} else {
		// Hide the 'edit text' button.
		editTextButton.classList.add('hiddenState');
		// Hide the on-screen keyboard.
		hiddenInput.blur();
	}
}

// Browser keys do not have a charCode so we only need to test keyCode.
function isKeyCodeBrowserKey(keyCode) {
	// Function keys or tab key.
	return keyCode >= 112 && keyCode <= 123 || keyCode === 9;
}
const SpecialKeyCodes = {
	BackSpace: 8,
	Shift: 16,
	Control: 17,
	Alt: 18,
	RightShift: 253,
	RightControl: 254,
	RightAlt: 255
};

function getKeyCode(e) {
	if (e.keyCode === SpecialKeyCodes.Shift && e.code === 'ShiftRight') return SpecialKeyCodes.RightShift;
	else if (e.keyCode === SpecialKeyCodes.Control && e.code === 'ControlRight') return SpecialKeyCodes.RightControl;
	else if (e.keyCode === SpecialKeyCodes.Alt && e.code === 'AltRight') return SpecialKeyCodes.RightAlt;
	else return e.keyCode;
}

function registerKeyboardEvents() {
	document.onkeydown = function (e) {
		if (print_inputs) {
			console.log(`key down ${e.keyCode}, repeat = ${e.repeat}`);
		}
		sendInputData(new Uint8Array([MessageType.KeyDown, getKeyCode(e), e.repeat]).buffer);
		// Backspace is not considered a keypress in JavaScript but we need it
		// to be so characters may be deleted in a UE4 text entry field.
		if (e.keyCode === SpecialKeyCodes.BackSpace) {
			document.onkeypress({ charCode: SpecialKeyCodes.BackSpace });
		}
		if (inputOptions.suppressBrowserKeys && isKeyCodeBrowserKey(e.keyCode)) {
			e.preventDefault();
		}
	};

	document.onkeyup = function (e) {
		if (print_inputs) {
			console.log(`key up ${e.keyCode}`);
		}
		sendInputData(new Uint8Array([MessageType.KeyUp, getKeyCode(e)]).buffer);
		if (inputOptions.suppressBrowserKeys && isKeyCodeBrowserKey(e.keyCode)) {
			e.preventDefault();
		}
	};

	document.onkeypress = function (e) {
		if (print_inputs) {
			console.log(`key press ${e.charCode}`);
		}
		let data = new DataView(new ArrayBuffer(3));
		data.setUint8(0, MessageType.KeyPress);
		data.setUint16(1, e.charCode, true);
		sendInputData(data.buffer);
	};
}

function onExpandOverlay_Click(/* e */) {
	let overlay = document.getElementById('overlay');
	overlay.classList.toggle("overlay-shown");
}

function connect() {
	"use strict";

	window.WebSocket = window.WebSocket || window.MozWebSocket;

	if (!window.WebSocket) {
		alert('Your browser doesn\'t support WebSocket');
		return;
	}

	//ws = new WebSocket(window.location.href.replace('http://', 'ws://').replace('https://', 'wss://'));
    ws = new WebSocket("ws://"+window.location.hostname);
    console.log("readyState:"+ws.readyState)
	ws.onmessage = function (event) {
	    console.log("onmessage readyState:"+ws.readyState)
		console.log(`<- SS: ${event.data}`);
		var msg = JSON.parse(event.data);
		if (msg.type === 'config') {
			onConfig(msg);
		} else if (msg.type === 'answer') {
			onWebRtcAnswer(msg);
		} else if (msg.type === 'iceCandidate') {
			onWebRtcIce(msg.candidate);
		} else {
			console.log(`invalid SS message type: ${msg.type}`);
		}
	};

	ws.onerror = function (event) {
		console.log(`WS error: ${JSON.stringify(event)}`);
	};

	ws.onclose = function (event) {
		console.log(`WS closed: ${JSON.stringify(event.code)} - ${event.reason}`);
		ws = undefined;
		is_reconnection = true;

		// destroy `webRtcPlayerObj` if any
		let playerDiv = document.getElementById('player');
		if (webRtcPlayerObj) {
			playerDiv.removeChild(webRtcPlayerObj.video);
			webRtcPlayerObj.close();
			webRtcPlayerObj = undefined;
		}
	};
	return ws;
}

// Config data received from WebRTC sender via the Cirrus web server
function onConfig(config) {
	let playerDiv = document.getElementById('player');
	let playerElement = setupWebRtcPlayer(playerDiv, config);
	if(playerElement){
	    //隐藏进度条
        $("#progressArea .progress-bar").css({"width": "0%"});
        $("#progressArea").css({"display": "none"});
	    playerElement.play();//播放像素流
	}
}

function load() {
	setupHtmlEvents();
	registerKeyboardEvents();
}
