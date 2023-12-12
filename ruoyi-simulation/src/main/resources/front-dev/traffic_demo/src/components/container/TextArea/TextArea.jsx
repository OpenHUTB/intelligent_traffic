import React, { useEffect, useRef, useState } from 'react';
import * as THREE from 'three';
import { useLocation } from 'react-router-dom';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { useDispatch, useSelector } from 'react-redux';
import { setAnimationIndex } from 'stores/animationSlice';
import './index.scss';

export default function TextArea() {
    const dispatch = useDispatch();
    const animationIndex = useSelector(state => state.animation.animationIndex);
    const animationRef = useRef(null);
    // handle the animation play and hide.
    const [isPlay, setIsPlay] = useState(false);
    const getAnimationClassName = () => {
        return `text-container ${isPlay ? 'play' : ''}`
    }
    const positions = {
        home0: { right: "0", top: "40%", width: "10rem", height: "15rem" },
        home1: { right: "-15%", top: "55%", transform: "translate(-50%, -50%)", width: "35rem", height: "35rem" },
        junction0: { right: "20%", bottom: "0", width: "10rem", height: "15rem" },
        junction1: { right: "21%", bottom: "1rem", transform: "translate(0 0)", width: "35rem", height: "35rem" },
        plan0: { right: "5rem", bottom: "0", width: "10rem", height: "15rem" },
        plan1: { right: "21%", bottom: "1rem", transform: "translate(0 0)", width: "35rem", height: "35rem" },
    }

    const glbPaths = {
        idle: "GLB/IP_Anim_Idle_All.glb",
        welcome: "GLB/IP_Anim_DaZhaoHu.glb",
        conversation: "GLB/IP_Anim_DuiHua.glb",
        startTask: "GLB/IP_Anim_JieShouRenWu.glb",
        endTask: "GLB/IP_Anim_WanChengRenWu.glb",
    }
    // Get the current location and set the initial position.
    const location = useLocation().pathname.toLowerCase();
    let initalPosition = {};
    if (location.includes("junction")) {
        initalPosition = positions.junction0;
    } else if (location.includes("plan")) {
        initalPosition = positions.plan0;
    } else {
        initalPosition = positions.home0;
    }
    const [position, setPosition] = useState(initalPosition);


    useEffect(() => {

        // // socket connection
        // const handleWebSocket = (event) => {
        //     const data = JSON.parse(event.data);
        //     console.log(data.animationIndex);
        //     dispatch(setAnimationIndex(data.animationIndex));

        // };   
        const handleAnimationIndexChange = (event) => {
            const newAnimationIndex = event.detail.animationIndex;
            if (newAnimationIndex !== animationIndex) {
                dispatch(setAnimationIndex(newAnimationIndex));
            }
        };

        window.addEventListener('animationIndexChanged', handleAnimationIndexChange);

        if (animationIndex === 0) {
            // Toggle animation visibility
            setIsPlay(prevState => setIsPlay(false));
            setPosition(prevState => {
                if (location.includes("junction")) {
                    return positions.junction0;
                } else if (location.includes("plan")) {
                    return positions.plan0;
                }
                else {
                    return positions.home0;
                }
            });
        } else if (animationIndex >= 1) {
            setIsPlay(prevState => setIsPlay(true));
            setPosition(prevState => {
                if (location.includes("junction")) {
                    return positions.junction1;
                } else if (location.includes("plan")) {
                    return positions.plan1;
                }
                else {
                    return positions.home1;
                }
            });
        }


        let camera, scene, renderer;
        const clock = new THREE.Clock();
        let mixer, glbPath;
        switch (animationIndex) {
            case 0:
                glbPath = glbPaths.idle;
                break;
            case 1:
                glbPath = glbPaths.welcome;
                break;
            case 2:
                glbPath = glbPaths.conversation;
                break;
            case 3:
                glbPath = glbPaths.startTask;
                break;
            case 4:
                glbPath = glbPaths.endTask;
                break;
            default:
                glbPath = glbPaths.idle;
                break;
        }
        const animationContianer = animationRef.current;

        function init() {
            //creat the scene
            scene = new THREE.Scene();
            scene.background = null;

            //creat the camera
            camera = new THREE.PerspectiveCamera(75, animationContianer.clientWidth / animationContianer.clientHeight, 1, 1000);
            camera.position.set(0, 0, 10);

            //creat the renderer
            renderer = new THREE.WebGLRenderer({ alpantialias: true, alpha: true });
            renderer.setSize(animationContianer.clientWidth, animationContianer.clientHeight);
            renderer.setPixelRatio(window.devicePixelRatio);
            renderer.shadowMap.enabled = true;
            animationContianer.appendChild(renderer.domElement);

            // Add a light source & shadow
            const hemiLight = new THREE.HemisphereLight(0xcccccc, 0xffffff, 1, 2);
            hemiLight.position.set(0, 300, 0);
            scene.add(hemiLight);

            const dirLight = new THREE.DirectionalLight(0xcccccc);
            dirLight.position.set(0, 300, 100);
            dirLight.castShadow = true;
            scene.add(dirLight);

            // Add an ambient light
            const ambientLight = new THREE.AmbientLight(0xffffff, 2); // soft white light
            scene.add(ambientLight);

            // Model loading fbx
            // const manager = new THREE.LoadingManager();
            // manager.addHandler(/\.tga$/i, new TGALoader());
            // Model loading glb

            const loader = new GLTFLoader();
            loader.load(glbPath, function (gltf) {
                const model = gltf.scene;
                model.position.set(0, -6, 0);
                mixer = new THREE.AnimationMixer(model);
                gltf.animations.forEach((clip) => {
                    const action = mixer.clipAction(clip);
                    if (isPlay) {
                        action.setLoop(THREE.LoopOnce);
                        action.clampWhenFinished = true;

                    } else {
                        action.setLoop(THREE.LoopRepeat)
                    }

                    action.play();
                });

                model.traverse(function (child) {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                    }
                    if (child.isSkinnedMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                        let material = child.material;
                        if (Array.isArray(material)) {
                            // Sometimes the material is an array of materials
                            material.forEach((mat) => {
                                mat.depthWrite = true;
                            });
                        } else {
                            // Single material
                            material.depthWrite = true;
                        }
                    }
                });
                model.scale.set(12, 12, 12);
                scene.add(model);

            });

            const controls = new OrbitControls(camera, renderer.domElement);
            controls.target.set(0, 0, 0);
            controls.update();
        }

        function animate() {
            requestAnimationFrame(animate);
            const delta = clock.getDelta();
            if (mixer) mixer.update(delta);
            renderer.render(scene, camera);
        }

        init();
        animate();

        return () => {
            if (animationRef.current) {
                animationRef.current.removeChild(renderer.domElement);

            }
            window.removeEventListener('animationIndexChanged', handleAnimationIndexChange);
        };
    }, [isPlay, location, animationIndex, dispatch]);

    return (
        <section className="voiceDetect" style={position}>
            <div className={getAnimationClassName()}><textarea id="tips" defaultValue={"Hello 我是小轩"}></textarea></div>
            <div id="AnimationIP" ref={animationRef}></div>
        </section>
    );
}
