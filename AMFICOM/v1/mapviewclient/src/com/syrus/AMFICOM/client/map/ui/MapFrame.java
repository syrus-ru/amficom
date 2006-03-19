/*-
 * $$Id: MapFrame.java,v 1.92 2006/03/19 14:43:58 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.MapImageRenderer;
import com.syrus.AMFICOM.client.map.MapImageRendererFactory;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapChooserCommand;
import com.syrus.AMFICOM.client.map.command.navigate.CenterSelectionCommand;
import com.syrus.AMFICOM.client.map.command.navigate.HandPanCommand;
import com.syrus.AMFICOM.client.map.command.navigate.MapModeCommand;
import com.syrus.AMFICOM.client.map.command.navigate.MeasureDistanceCommand;
import com.syrus.AMFICOM.client.map.command.navigate.MoveFixedCommand;
import com.syrus.AMFICOM.client.map.command.navigate.MoveToCenterCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ShowIndicationCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ShowNodesCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomBoxCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomInCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomOutCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomToPointCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeSampleData;
import com.syrus.util.Log;

class TestSliderListener implements ChangeListener, PropertyChangeListener {
	
	Identifier markerId = null;
	final MapFrame mapFrame;
	private final JSlider slider;
	boolean notInitialized = true;
	
	public TestSliderListener(MapFrame mapFrame, JSlider slider) {
		this.mapFrame = mapFrame;
		this.slider = slider;
		this.mapFrame.getContext().getDispatcher().addPropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
		this.mapFrame.getContext().getDispatcher().addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
	}

	public void stateChanged(ChangeEvent e) {
		try {
			Dispatcher dispatcher = this.mapFrame.aContext.getDispatcher();
			if(this.markerId == null) {
				this.markerId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_CODE);
				dispatcher.firePropertyChange(new MarkerEvent(
						this, 
						MarkerEvent.MARKER_CREATED_EVENT, 
						this.markerId,
						this.slider.getValue(),
						SchemeSampleData.scheme1path0.getId(),
						null), false);
			}
			dispatcher.firePropertyChange(new MarkerEvent(
					this, 
					MarkerEvent.MARKER_SELECTED_EVENT, 
					this.markerId), false);
			dispatcher.firePropertyChange(new MarkerEvent(
					this, 
					MarkerEvent.MARKER_MOVED_EVENT, 
					this.markerId,
					((JSlider)(e.getSource())).getValue(),
					SchemeSampleData.scheme1path0.getId(),
					null), false);
		} catch(IdentifierGenerationException e1) {
			Log.errorMessage(e1);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent) evt;
			if(mapEvent.getMapEventType().equals(MapEvent.MAP_FRAME_SHOWN)
					&& false
					&& this.notInitialized) {
				new ProcessingDialog(new Runnable() {
				
					public void run() {
						Log.debugMessage("waiting for SchemeSampleData...", Log.DEBUGLEVEL09); //$NON-NLS-1$
						while(!SchemeSampleData.loaded) {
							try {
								Thread.sleep(100);
							} catch(InterruptedException e) {
								//nothing
							}
						}
						Log.debugMessage(" placing elements...", Log.DEBUGLEVEL09); //$NON-NLS-1$
						TestSliderListener.this.notInitialized = false;
						NetMapViewer netMapViewer = TestSliderListener.this.mapFrame.getMapViewer();

						TestSliderListener.this.mapFrame.getMapView().addScheme(SchemeSampleData.scheme1);

						PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, new Point(50, 200));
						startcommand.setNetMapViewer(netMapViewer);
						startcommand.execute();
				
						PlaceSchemeElementCommand intercommand1 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, new Point(200, 50));
						intercommand1.setNetMapViewer(netMapViewer);
						intercommand1.execute();
				
						PlaceSchemeElementCommand intercommand2 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element2, new Point(400, 50));
						intercommand2.setNetMapViewer(netMapViewer);
						intercommand2.execute();
				
						PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, new Point(300, 250));
						endcommand.setNetMapViewer(netMapViewer);
						endcommand.execute();
						Log.debugMessage("OK!", Log.DEBUGLEVEL09); //$NON-NLS-1$
					}
				
				}, "ўа наху€рю ху€риков..."); //$NON-NLS-1$
			}
		}
		else if(evt.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			MarkerEvent markerEvent = (MarkerEvent) evt;
//			if(markerEvent.getMarkerId() != this.markerId) {
//				return;
//			}
			Dispatcher dispatcher = this.mapFrame.aContext.getDispatcher();
			switch(markerEvent.getMarkerEventType()) {
				case MarkerEvent.MARKER_DELETED_EVENT:
					this.markerId = null;
					break;
				case MarkerEvent.MARKER_CREATED_EVENT:
					dispatcher.firePropertyChange(new MarkerEvent(
							this, 
							MarkerEvent.MARKER_DELETED_EVENT, 
							this.markerId), false);
					this.markerId = markerEvent.getMarkerId();
					break;
				case MarkerEvent.MARKER_MOVED_EVENT:
					this.slider.setValue((int) markerEvent.getOpticalDistance());
					break;
			}
		}
		
	}
}

/**
 *  ласс $RCSfile: MapFrame.java,v $ используетс€ дл€ управлени€ отображеним топологический схемы.
 * ќсновой €вл€етс€ объект типа MapView. ќтображение осуществл€етс€ объектом 
 * NetMapViewer, который лежит в окне MapMainFrame. ”правление действи€ми с
 * картой осуществл€етс€ через панель инструментов (MapToolBar), и с помощью
 * принимаемых событий от других окон (operationPerformed). —озданный объект 
 * окна карты хранитс€ в пуле с ключом "environment", идентификатор 
 * "mapmainframe". существует только один объект 
 * 
 * @version $Revision: 1.92 $, $Date: 2006/03/19 14:43:58 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapFrame extends JInternalFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 1313547389360194239L;
	public static final String	NAME = "mapFrame"; //$NON-NLS-1$

	public static final String MAP_FRAME_SHOWN = "map_frame_shown"; //$NON-NLS-1$

	/**
	 *  онтекст приложени€
	 */
	protected ApplicationContext aContext;

	/**
	 * обозреватель карты
	 */
	protected NetMapViewer mapViewer;

	/**
	 * ѕанель инструментов
	 */
	protected MapToolBar mapToolBar;

	/**
	 * ѕанель отображающа€ координты курсора и масштаб
	 */
	protected MapStatusBar mapStatusbar;

	/**
	 * Ёкземпл€р класса. ѕоскольку вид карты отнимает слишком много ресурсов
	 * пам€ти, в одной ∆аба-машине может быть только один экземпл€р окна карты,
	 * раздел€емый поочередно в разных модул€х.
	 */
	protected static MapFrame singleton;

	private MapConnection mapConnection;

	public MapFrame(final ApplicationContext aContext) throws MapConnectionException, MapDataException, ApplicationException {
		super();

		// экземпл€р обозревател€ создаетс€ менеджером карты на основе
		// данных, записанных в файле Map.properties

		this.mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());
		this.mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		this.mapConnection.setView(MapPropertiesManager.getDataBaseView());
		this.mapConnection.setURL(MapPropertiesManager.getDataBaseURL());
			try {
				this.mapConnection.connect();
			} catch(MapConnectionException e) {
				int result = ViewMapChooserCommand.chooseMap(this.mapConnection);
				if(result == JOptionPane.OK_OPTION) {
//					this.mapConnection.connect();
				}
				else {
					throw e;
				}
			}

		final MapImageLoader loader = this.mapConnection.createImageLoader();
		final MapContext mapContext = this.mapConnection.createMapContext();
		final MapCoordinatesConverter converter = this.mapConnection.createCoordinatesConverter();

		final MapImageRenderer renderer = MapImageRendererFactory.create(MapPropertiesManager.getMapImageRendererClassName(),
				converter,
				mapContext,
				loader);

		final LogicalNetLayer logicalNetLayer = new LogicalNetLayer(aContext, converter, mapContext);

		this.mapViewer = NetMapViewer.create(MapPropertiesManager.getNetMapViewerClassName(), logicalNetLayer, mapContext, renderer);

		this.mapViewer.init();

		logicalNetLayer.setMapViewController(MapViewController.createInstance(this.mapViewer));

		this.mapToolBar = new MapToolBar(this.mapViewer);
		this.mapStatusbar = new MapStatusBar(this.mapViewer);

		this.setContext(aContext);

		this.jbInit();

		this.initModule();

		this.mapViewer.setScale(MapPropertiesManager.getZoom());
		this.mapViewer.setCenter(MapPropertiesManager.getCenter());

	}
	
	/**
	 * геттер
	 */
	public NetMapViewer getMapViewer() {
		return this.mapViewer;
	}

	private void jbInit() {
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").getScaledInstance(16, //$NON-NLS-1$
				16,
				Image.SCALE_DEFAULT)));

		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setTitle(I18N.getString(MapEditorResourceKeys.TITLE_MAP));

		// визуальный компонент обозревател€ карты
		// обозреватель карты сам по себе не €вл€етс€ компонентом, а содержит
		// компонент в себе
		final JComponent mapVisualComponent = this.mapViewer.getVisualComponent();

		this.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.mapToolBar, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(mapVisualComponent, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.getContentPane().add(this.mapStatusbar, constraints);

//		final JSlider testSlider = new JSlider(JSlider.HORIZONTAL, 1, 3100, 1);
//		
//		constraints.gridx = 0;
//		constraints.gridy = 3;
//		constraints.gridwidth = 1;
//		constraints.gridheight = 1;
//		constraints.weightx = 0;
//		constraints.weighty = 0;
//		constraints.anchor = GridBagConstraints.WEST;
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.getContentPane().add(testSlider, constraints);
//
//		TestSliderListener testSliderListener = new TestSliderListener(this, testSlider);
//		testSlider.addChangeListener(testSliderListener);
		
		this.addComponentListener(new MapMainFrameComponentAdapter(this));
		this.addInternalFrameListener(new MapMainFrameInternalFrameAdapter(this));
	}
	
	/**
	 * ƒанные отображени€ сохран€ютс€ в файл в обозревателе карты
	 */
	public void saveConfig() {
		if(this.mapViewer != null) {
			this.mapViewer.saveConfig();
		}
	}

	public void finalizeModule() {
		this.setContext(null);
		Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
	}

	/**
	 * ќдноразова€ инициализаци€ окна карты
	 */
	public void initModule() {
		Environment.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}

	public void setCommands(final ApplicationModel aModel) {
		aModel.setCommand(MapApplicationModel.OPERATION_CENTER_SELECTION, new CenterSelectionCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.MODE_NODE_LINK, new MapModeCommand(aModel,
				this.mapViewer,
				MapApplicationModel.MODE_NODE_LINK,
				MapState.SHOW_NODE_LINK));
		aModel.setCommand(MapApplicationModel.MODE_LINK, new MapModeCommand(aModel,
				this.mapViewer,
				MapApplicationModel.MODE_LINK,
				MapState.SHOW_PHYSICAL_LINK));
		aModel.setCommand(MapApplicationModel.MODE_CABLE_PATH, new MapModeCommand(aModel,
				this.mapViewer,
				MapApplicationModel.MODE_CABLE_PATH,
				MapState.SHOW_CABLE_PATH));
		aModel.setCommand(MapApplicationModel.MODE_PATH, new MapModeCommand(aModel,
				this.mapViewer,
				MapApplicationModel.MODE_PATH,
				MapState.SHOW_MEASUREMENT_PATH));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_IN, new ZoomInCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_OUT, new ZoomOutCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT, new ZoomToPointCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_BOX, new ZoomBoxCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER, new MoveToCenterCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.MODE_NODES, new ShowNodesCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.MODE_INDICATION, new ShowIndicationCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_HAND_PAN, new HandPanCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE, new MeasureDistanceCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_FIXED, new MoveFixedCommand(aModel, this.mapViewer));

		aModel.fireModelChanged();
	}

	public void setContext(final ApplicationContext aContext) {
		if (this.aContext != null) {
			if (this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
				this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			}
		}

		this.getMapViewer().getLogicalNetLayer().setContext(aContext);

		if (aContext != null) {
			this.aContext = aContext;
			this.setModel(aContext.getApplicationModel());
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
			aContext.getDispatcher().addPropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
			aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			
			aContext.getDispatcher().addPropertyChangeListener("MeasurementStoped", this);
			aContext.getDispatcher().addPropertyChangeListener("MeasurementStarted", this);
		}

	}

	/**
	 * геттер
	 */
	public ApplicationContext getContext() {
		return this.aContext;
	}

	public void setModel(final ApplicationModel aModel) {
		this.setCommands(aModel);
		aModel.addListener(this.mapToolBar);
		this.mapToolBar.setModel(aModel);
		aModel.fireModelChanged();
	}

	/**
	 * геттер
	 */
	public ApplicationModel getModel() {
		return this.aContext.getApplicationModel();
	}

	/**
	 * обработка событий
	 */
	public void propertyChange(final PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent )pce;
			String mapEventType = mapEvent.getMapEventType();

			if (mapEventType.equals(MapEvent.MAP_VIEW_CENTER_CHANGED)) {
				final DoublePoint p = (DoublePoint) pce.getNewValue();
				this.mapStatusbar.showLatLong(p.getY(), p.getX());
			} else if (mapEventType.equals(MapEvent.MAP_VIEW_SCALE_CHANGED)) {
				final Double p = (Double) pce.getNewValue();
				this.mapStatusbar.showScale(p.doubleValue());
			} else if (mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				this.mapToolBar.setEnableDisablePanel(true);
			} else if (mapEventType.equals(MapEvent.MAP_VIEW_DESELECTED)) {
				this.mapToolBar.setEnableDisablePanel(false);
			} else {
				this.getMapViewer().propertyChange(pce);
			}
		} else {
			this.getMapViewer().propertyChange(pce);
		}
	}

	public void setMapView(final MapView mapView) throws MapConnectionException, MapDataException {
		this.getMapViewer().getLogicalNetLayer().setMapView(mapView);
		if (mapView != null) {
			this.mapViewer.setScale(mapView.getScale());
			this.mapViewer.setCenter(mapView.getCenter());
		}
		this.mapViewer.repaint(true);
	}

	public void closeMap() {
		Log.debugMessage("Closing map", Level.INFO); //$NON-NLS-1$
//		this.setContext(null);
	}

	public boolean checkChangesPresent() {
		boolean changesPresent = false;

		final MapView mapView = this.getMapView();

		if (mapView != null) {
			if (mapView.isChanged()) {
				if(getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP_VIEW)) {
					changesPresent = true;
				}
				else {
					cleanChangedObjects();
				}
			} else {
				final Map map = mapView.getMap();
				if (map != null) {
					if (map.isChanged()) {
						if(getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP)) {
							changesPresent = true;
						}
						else {
							cleanChangedObjects();
						}
					}
				}
			}
		}
		if (changesPresent) {
			if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_SCHEME)) {
				cleanChangedObjects();
				changesPresent = false;
			}
			else {
				final int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						I18N.getString(MapEditorResourceKeys.MESSAGE_UNSAVED_ELEMENTS_PRESENT),
						I18N.getString(MapEditorResourceKeys.TITLE_OBJECT_WAS_CHANGED),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (ret == JOptionPane.YES_OPTION) {
					cleanChangedObjects();
					changesPresent = false;
				}
			}
		}
		return changesPresent;
	}

	private void cleanChangedObjects() {
		final MapView mapView = this.getMapView();
		final Map map = mapView.getMap();
		// TODO should clean not only changes in map, mapview, mapview.schemes
		// but also in underlaying objects

		StorableObjectPool.cleanChangedStorableObjects(
				Collections.singleton(map));
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllCollectors());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllMarks());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllNodeLinks());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllPhysicalLinks());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllSiteNodes());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getAllTopologicalNodes());
		StorableObjectPool.cleanChangedStorableObjects(
				map.getMaps());

		StorableObjectPool.cleanChangedStorableObjects(
				Collections.singleton(mapView));

		// it's work faster
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SCHEME_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SCHEMEELEMENT_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SCHEMECABLELINK_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.CABLECHANNELINGITEM_CODE);
		
//		final Set<Scheme> schemes = mapView.getSchemes();
//		for(Scheme scheme : schemes) {
//			try {
//				StorableObjectPool.cleanChangedStorableObjects(scheme.getReverseDependencies(false));
//			} catch(ApplicationException e) {
//				Log.errorMessage(e);
//			}
//		}
	}

	public Map getMap() {
		return this.getMapViewer().getLogicalNetLayer().getMapView().getMap();
	}

	public MapView getMapView() {
		return this.getMapViewer().getLogicalNetLayer().getMapView();
	}

	void thisInternalFrameActivated(@SuppressWarnings("unused")final InternalFrameEvent e) {
		this.getMapViewer().getVisualComponent().grabFocus();

		if (this.aContext.getDispatcher() != null) {
			if (this.getMapView() != null) {
				this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SELECTED, this.getMapView()));
				this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_SELECTED, this.getMapView().getMap()));
			}
		}
	}

	void thisInternalFrameClosed(@SuppressWarnings("unused")final InternalFrameEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		}
		this.closeMap();
	}

	void thisInternalFrameDeactivated(@SuppressWarnings("unused")final InternalFrameEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_DESELECTED));
		}
	}

	void thisInternalFrameOpened(@SuppressWarnings("unused")final InternalFrameEvent e) {
		this.getMapViewer().getVisualComponent().grabFocus();
	}

	void thisComponentShown(@SuppressWarnings("unused")final ComponentEvent e) {
		this.mapViewer.getVisualComponent().firePropertyChange(MAP_FRAME_SHOWN, false, true);
		MapFrame.this.mapViewer.getVisualComponent().requestFocus();
	}

	void thisComponentHidden(@SuppressWarnings("unused")final ComponentEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		}
		this.closeMap();
	}

	@Override
	public void doDefaultCloseAction() {
		if (this.checkChangesPresent()) {
			return;
		}
		if (super.isMaximum())
			try {
				super.setMaximum(false);
			} catch (java.beans.PropertyVetoException ex) {
				Log.errorMessage(ex);
			}
		super.doDefaultCloseAction();
	}

	private class MapMainFrameComponentAdapter extends java.awt.event.ComponentAdapter {
		MapFrame adaptee;

		MapMainFrameComponentAdapter(final MapFrame adaptee) {
			this.adaptee = adaptee;
		}

		@Override
		public void componentShown(final ComponentEvent e) {
			this.adaptee.thisComponentShown(e);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			this.adaptee.thisComponentHidden(e);
		}
	}

	private class MapMainFrameInternalFrameAdapter extends javax.swing.event.InternalFrameAdapter {
		MapFrame adaptee;

		MapMainFrameInternalFrameAdapter(final MapFrame adaptee) {
			this.adaptee = adaptee;
		}

		@Override
		public void internalFrameActivated(final InternalFrameEvent e) {
			this.adaptee.thisInternalFrameActivated(e);
		}

		@Override
		public void internalFrameClosed(final InternalFrameEvent e) {
			this.adaptee.thisInternalFrameClosed(e);
		}

		@Override
		public void internalFrameDeactivated(final InternalFrameEvent e) {
			this.adaptee.thisInternalFrameDeactivated(e);
		}

		@Override
		public void internalFrameOpened(final InternalFrameEvent e) {
			this.adaptee.thisInternalFrameOpened(e);
		}
	}

	/**
	 * @return Returns the mapConnection.
	 */
	public MapConnection getMapConnection() {
		return this.mapConnection;
	}

}

