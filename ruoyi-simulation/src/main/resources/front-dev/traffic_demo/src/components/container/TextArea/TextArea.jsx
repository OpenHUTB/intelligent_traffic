import React, { useEffect, useRef } from 'react';
import * as THREE from 'three';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader.js';
import Stats from 'three/addons/libs/stats.module.js';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { TGALoader } from 'three/examples/jsm/loaders/TGALoader.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import './index.scss';

export default function TextArea() {
    const animationRef = useRef(null);

    useEffect(() => {
        let camera, scene, renderer;
        const clock = new THREE.Clock();
        let mixer;

        function init() {
            camera = new THREE.PerspectiveCamera(45, animationRef.current.clientWidth / animationRef.current.clientHeight, 1, 2000);
            camera.position.set(0, 100, 300);

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
            // // Ground
            // const mesh = new THREE.Mesh(new THREE.PlaneGeometry(2000, 2000), new THREE.MeshPhongMaterial({ color: 0x999999, depthWrite: false }));
            // mesh.rotation.x = -Math.PI / 2;
            // mesh.receiveShadow = true;
            // scene.add(mesh);

            // Model loading fbx
            const manager = new THREE.LoadingManager();
            manager.addHandler(/\.tga$/i, new TGALoader());
            const loader = new FBXLoader(manager);
            loader.load('IP_test/IP_Anim01.fbx', function (object) {
                console.log(object)
                // object.mixer = new THREE.AnimationMixer(object);
                console.log(object.animations)

                mixer = new THREE.AnimationMixer(object);
                const action = mixer.clipAction(object.animations[0]);
                action.timeScale = 0.8
                action.play();

                object.traverse(function (child) {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                    }
                });
                object.scale.set(3, 3, 3);
                scene.add(object);

            });
            // model loading gltf
            // const manager = new THREE.LoadingManager();
            // manager.addHandler(/\.tga$/i, new TGALoader());
            // const loader = new GLTFLoader(manager);
            // loader.load('animations/IP_out/IP.gltf', function (gltf) {
            //     const model = gltf.scene;
            //     console.log(gltf);
            //     console.log(gltf.animations); // 打印动画数组

            //     mixer = new THREE.AnimationMixer(model);
            //     if (gltf.animations.length) {
            //         const action = mixer.clipAction(gltf.animations[0]);
            //         action.timeScale = 0.8;
            //         action.play();
            //     }

            //     model.traverse(function (child) {
            //         if (child.isMesh) {
            //             child.castShadow = true;
            //             child.receiveShadow = true;
            //         }
            //     });

            //     model.scale.set(2.5, 2.5, 2.5); // 根据需要调整比例
            //     scene.add(model);
            // });


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
        };
    }, []);

    return (
        <section className="voiceDetect">
            <div className='text-container'><span id="tips">Hello，我是小轩</span></div>
            <div id="AnimationIP" ref={animationRef}></div>
        </section>
    );
}
