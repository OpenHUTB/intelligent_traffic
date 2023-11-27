import React, { useEffect, useRef, useState } from 'react';
import * as THREE from 'three';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader.js';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { TGALoader } from 'three/examples/jsm/loaders/TGALoader.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import './index.scss';

export default function TextArea(props) {
    console.log(props);
    const animationRef = useRef(null);
    // handle the animation play and hide.
    const [isPlay, setIsPlay] = useState(true);
    const getAnimationClassName = () => {
        return `text-container ${isPlay ? 'play' : ''}`
    }
    const inactivePosition = { right: "20%", bottom: "0", width: "10rem", height: "10rem" };
    const activePosition = { left: "50%", top: "65%", transform: "translate(-50%, -50%)", width: "30rem", height: "30rem" };
    const [position, setPosition] = useState(activePosition);

    const handleClick = (event) => {
        const { clientX, clientY } = event;
        const { innerWidth, innerHeight } = window;
        const margin = 50; // Define corner area
        const isCornerClick = (
            (clientX < margin && clientY < margin) ||
            (clientX > innerWidth - margin && clientY < margin) ||
            (clientX < margin && clientY > innerHeight - margin) ||
            (clientX > innerWidth - margin && clientY > innerHeight - margin)
        );

        if (isCornerClick) {
            setIsPlay(prevState => !prevState); // Toggle animation visibility
            setPosition(prevState => {
                if (prevState.width === inactivePosition.width) {
                    return activePosition;
                } else {
                    return inactivePosition;
                }
            });
        }
    }


    useEffect(() => {
        let camera, scene, renderer;
        const clock = new THREE.Clock();
        let mixer;
        window.addEventListener('click', handleClick);
        function init() {
            camera = new THREE.PerspectiveCamera(45, animationRef.current.clientWidth / animationRef.current.clientHeight, 1, 2000);
            camera.position.set(0, 0, 0);


            scene = new THREE.Scene();
            scene.background = null;
            // scene.fog = new THREE.Fog(0xa0a0a0, 200, 1000);

            const hemiLight = new THREE.HemisphereLight(0xffffff, 0xffffff, 1, 2);
            hemiLight.position.set(0, 300, 0);
            scene.add(hemiLight);

            const dirLight = new THREE.DirectionalLight(0xffffff);
            dirLight.position.set(0, 300, 100);
            dirLight.castShadow = false;
            scene.add(dirLight);
            // Add an ambient light
            const ambientLight = new THREE.AmbientLight(0xffffff, 2); // soft white light
            scene.add(ambientLight);

            // Model loading fbx
            // const manager = new THREE.LoadingManager();
            // manager.addHandler(/\.tga$/i, new TGALoader());
            // const loader = new FBXLoader(manager);
            // Model loading glb
            const loader = new GLTFLoader();
            loader.load('IP_test/IP_Anim_test01.glb', function (gltf) {
                const model = gltf.scene;
                console.log(model);
                mixer = new THREE.AnimationMixer(model);
                gltf.animations.forEach((clip) => {
                    mixer.clipAction(clip).play();
                });

                model.traverse(function (child) {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                    }
                });

                if (isPlay) {
                    model.scale.set(3, 3, 3);
                } else {
                    model.scale.set(2, 2, 2);
                    console.log(model.scale);
                }
                scene.add(model);

            });

            renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
            renderer.setPixelRatio(window.devicePixelRatio);
            renderer.setSize(animationRef.current.clientWidth, animationRef.current.clientHeight);
            renderer.shadowMap.enabled = true;
            animationRef.current.appendChild(renderer.domElement);

            const controls = new OrbitControls(camera, renderer.domElement);
            controls.target.set(0, 100, 0);
            controls.update();

            // Stats
            // stats = new Stats();
            // animationRef.current.appendChild(stats.dom);

            window.addEventListener('resize', onWindowResize);
        }

        function onWindowResize() {
            camera.aspect = animationRef.current.clientWidth / animationRef.current.clientHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(animationRef.current.clientWidth, animationRef.current.clientHeight);
        }

        function animate() {
            requestAnimationFrame(animate);

            const delta = clock.getDelta();
            if (mixer) mixer.update(delta);

            renderer.render(scene, camera);
            // stats.update();
        }

        init();
        animate();

        return () => {
            window.removeEventListener('resize', onWindowResize);
            if (animationRef.current) {
                animationRef.current.removeChild(renderer.domElement);
            }
            window.removeEventListener('click', handleClick)
        };
    }, [isPlay]);

    return (
        <section className="voiceDetect" style={position}>
            <div className={getAnimationClassName()}><textarea id="tips" defaultValue={"Hello 我是小轩"}></textarea></div>
            <div id="AnimationIP" ref={animationRef}></div>
        </section>
    );
}
