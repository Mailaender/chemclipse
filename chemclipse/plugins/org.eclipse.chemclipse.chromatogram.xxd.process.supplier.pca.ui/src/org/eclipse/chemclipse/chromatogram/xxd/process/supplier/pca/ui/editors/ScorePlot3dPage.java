/*******************************************************************************
 * Copyright (c) 2013, 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Rafael Aguayo - initial API and implementation
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.editors;

import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.chart3d.Axes;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.chart3d.Chart3DData;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.chart3d.Chart3DScatter;
import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.chart3d.ChartLegend;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class ScorePlot3dPage {

	private Axes axes;
	private Chart3DData data;
	private FXCanvas fxCanvas;
	private ChartLegend legend;
	private final double rotateModifier = 10;
	private Chart3DScatter scatter;

	public ScorePlot3dPage(PcaEditor pcaEditor, TabFolder tabFolder, FormToolkit formToolkit) {
		initialize(tabFolder, formToolkit);
		data = new Chart3DData(pcaEditor);
		axes = new Axes(data);
		scatter = new Chart3DScatter(data);
		legend = new ChartLegend(data);
	}

	private void createScene() {

		PerspectiveCamera camera = new PerspectiveCamera(true);
		Group root = new Group();
		AmbientLight ambientlight = new AmbientLight();
		Group mainGroup = new Group();
		root.getChildren().addAll(mainGroup, ambientlight, camera);
		if(!data.isEmpty()) {
			/*
			 * update data
			 */
			data.update(1, 2, 3);
			legend.update();
			axes.update();
			scatter.update();
			/*
			 * rotate chart
			 */
			Group objects = new Group();
			objects.getChildren().addAll(scatter.getScarter(), axes.createAxes());
			Rotate rotate = new Rotate(180, 0, 0, 0, Rotate.X_AXIS);
			objects.getTransforms().add(rotate);
			/*
			 * set camera
			 */
			camera.setTranslateZ(-4000);
			camera.setNearClip(0.01);
			camera.setFarClip(100000.0);
			mainGroup.getChildren().add(objects);
		}
		Point sizeScene = fxCanvas.getParent().getSize();
		/*
		 * legend
		 */
		VBox boxLegend = legend.getLegend();
		/*
		 * build central subscene, which contains chart
		 */
		SubScene mainScene = new SubScene(root, sizeScene.x - boxLegend.getWidth(), sizeScene.y, true, SceneAntialiasing.BALANCED);
		mainScene.setFill(Color.WHITE);
		mainScene.setCamera(camera);
		makeZoomable(mainScene, mainGroup);
		mousePressedOrMoved(mainScene, mainGroup);
		/*
		 * create scene
		 */
		BorderPane pane = new BorderPane(mainScene, null, boxLegend, null, null);
		Scene scene = new Scene(pane, sizeScene.x, sizeScene.y);
		fxCanvas.setScene(scene);
		pane.setCenter(mainScene);
		/*
		 * adjust size composite
		 */
		fxCanvas.getParent().layout(true);
	}

	private void initialize(TabFolder tabFolder, FormToolkit formToolkit) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("3D View");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());
		Composite parent = new Composite(composite, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		parent.setLayout(fillLayout);
		/*
		 * JavaFX
		 */
		fxCanvas = new FXCanvas(parent, SWT.NONE);
		/*
		 * update scene after resize
		 */
		parent.addListener(SWT.Resize, (event) -> createScene());
		tabItem.setControl(composite);
	}

	public void makeZoomable(SubScene scene, Group group) {

		final double MAX_SCALE = 200000.0;
		final double MIN_SCALE = 0.1;
		scene.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {

				double delta = 1.2;
				double scale = group.getScaleX();
				if(event.getDeltaY() < 0) {
					scale /= delta;
				} else {
					scale *= delta;
				}
				scale = (scale < MAX_SCALE ? scale : MAX_SCALE);
				scale = (MIN_SCALE < scale ? scale : MIN_SCALE);
				group.setScaleX(scale);
				group.setScaleY(scale);
			}
		});
	}

	private void mousePressedOrMoved(SubScene sceneRoot, Group group) {

		Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		group.getTransforms().addAll(xRotate, yRotate);
		sceneRoot.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {

			private double mouseXold = 0;
			private double mouseYold = 0;

			@Override
			public void handle(MouseEvent event) {

				if(event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					double mouseXnew = event.getSceneX();
					double mouseYnew = event.getSceneY();
					if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
						double pitchRotate = xRotate.getAngle() + (mouseYnew - mouseYold) / rotateModifier;
						xRotate.setAngle(pitchRotate);
						double yawRotate = yRotate.getAngle() - (mouseXnew - mouseXold) / rotateModifier;
						yRotate.setAngle(yawRotate);
					}
					mouseXold = mouseXnew;
					mouseYold = mouseYnew;
				}
			}
		});
	}

	public void update() {

		createScene();
	}
}
