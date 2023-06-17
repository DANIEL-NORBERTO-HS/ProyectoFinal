package com.example.proyectoosiris.ui.modelos3d

import android.net.Uri
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable
import org.w3c.dom.Node

class AR {
   /* // Create a Sceneform SceneView and add it to the layout
    SceneView sceneView = new SceneView(this);
    layout.addView(sceneView);

// Create a scene and set a camera position
    Scene scene = sceneView.getScene();
    scene.getCamera().setWorldPosition(new Vector3(0.0f, 0.0f, 5.0f));

// Create a ModelRenderable from a GLB asset
    ModelRenderable.builder()
    .setSource(this, Uri.parse("andy_dance.glb"))
    .build()
    .thenAccept(renderable -> {
        // Create a node and set the renderable and position
        Node node = new Node();
        node.setRenderable(renderable);
        node.setWorldPosition(new Vector3(0.0f, 0.0f, 0.0f));

        // Add the node to the scene
        scene.addChild(node);
    });
    // Create an ArFragment and add it to the layout
    ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

// Set an OnTapArPlaneListener to handle user taps on planes
    arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
        // Create an Anchor from the hitResult
        Anchor anchor = hitResult.createAnchor();

        // Create a ModelRenderable from a GLB asset
        ModelRenderable.builder()
            .setSource(this, Uri.parse("andy_dance.glb"))
            .build()
            .thenAccept(modelRenderable -> run {
            // Create an AnchorNode and set the anchor and renderable
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setRenderable(modelRenderable);

            // Add the AnchorNode to the ArFragment's scene
            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });
    });*/
}