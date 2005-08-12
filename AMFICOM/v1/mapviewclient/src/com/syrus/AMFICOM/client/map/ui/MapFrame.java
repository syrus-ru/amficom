/**
 * $Id: MapFrame.java,v 1.63 2005/08/12 12:17:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
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
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DoublePoint;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;
/**
 * Класс $RCSfile: MapFrame.java,v $ используется для управления отображеним топологический схемы.
 * Основой является объект типа MapView. Отображение осуществляется объектом 
 * NetMapViewer, который лежит в окне MapMainFrame. Управление действиями с
 * картой осуществляется через панель инструментов (MapToolBar), и с помощью
 * принимаемых событий от других окон (operationPerformed). Созданный объект 
 * окна карты хранится в пуле с ключом "environment", идентификатор 
 * "mapmainframe". существует только один объект 
 * 
 * 
 * 
 * @version $Revision: 1.63 $, $Date: 2005/08/12 12:17:59 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapFrame extends JInternalFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 1313547389360194239L;

	public static final String MAP_FRAME_SHOWN = "map_frame_shown";

	/**
	 * Внутренний для окна карты диспетчер сообщений
	 */
	protected Dispatcher internalDispatcher = new Dispatcher();

	/**
	 * Контекст приложения
	 */
	protected ApplicationContext aContext;

	/**
	 * обозреватель карты
	 */
	protected NetMapViewer mapViewer;

	/**
	 * Панель инструментов
	 */
	protected MapToolBar mapToolBar;

	/**
	 * Панель отображающая координты курсора и масштаб
	 */
	protected MapStatusBar mapStatusbar;

	/**
	 * Экземпляр класса. Поскольку вид карты отнимает слишком много ресурсов
	 * памяти, в одной Жаба-машине может быть только один экземпляр окна карты,
	 * разделяемый поочередно в разных модулях.
	 */
	protected static MapFrame singleton;

	private MapConnection mapConnection;

	public MapFrame(final ApplicationContext aContext) throws MapConnectionException, MapDataException, ApplicationException {
		super();

		// экземпляр обозревателя создается менеджером карты на основе
		// данных, записанных в файле Map.properties

		this.mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());
		this.mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		this.mapConnection.setView(MapPropertiesManager.getDataBaseView());
		this.mapConnection.setURL(MapPropertiesManager.getDataBaseURL());
		this.mapConnection.connect();

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

		this.jbInit();

		this.initModule();

		this.mapViewer.setScale(MapPropertiesManager.getZoom());
		this.mapViewer.setCenter(MapPropertiesManager.getCenter());

		this.setContext(aContext);
	}
	
	/**
	 * геттер
	 */
	public NetMapViewer getMapViewer() {
		return this.mapViewer;
	}

	private void jbInit() {
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").getScaledInstance(16,
				16,
				Image.SCALE_DEFAULT)));

		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setTitle(LangModelMap.getString("Map"));
		this.getContentPane().setLayout(new BorderLayout());

		// визуальный компонент обозревателя карты
		// обозреватель карты сам по себе не является компонентом, а содержит
		// компонент в себе
		final JComponent mapVisualComponent = this.mapViewer.getVisualComponent();

		this.mapToolBar = new MapToolBar(this.mapViewer);
		this.mapStatusbar = new MapStatusBar(this.mapViewer);
		this.getContentPane().add(this.mapToolBar, BorderLayout.NORTH);
		this.getContentPane().add(this.mapStatusbar, BorderLayout.SOUTH);
		this.getContentPane().add(mapVisualComponent, BorderLayout.CENTER);

		this.addComponentListener(new MapMainFrameComponentAdapter(this));
		this.addInternalFrameListener(new MapMainFrameInternalFrameAdapter(this));
	}
	
	/**
	 * Данные отображения сохраняются в файл в обозревателе карты
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
	 * Одноразовая инициализация окна карты
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
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.OTHER_SELECTED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_ELEMENT_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.NEED_SELECT, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.NEED_DESELECT, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.SELECTION_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.PLACE_ELEMENT, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_CENTER_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_SCALE_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.NEED_FULL_REPAINT, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.NEED_REPAINT, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.DESELECT_ALL, this);
				this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			}
		}

		this.getMapViewer().getLogicalNetLayer().setContext(aContext);

		if (aContext != null) {
			this.aContext = aContext;
			this.setModel(aContext.getApplicationModel());
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.OTHER_SELECTED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_ELEMENT_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_SELECT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_DESELECT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.SELECTION_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.PLACE_ELEMENT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CENTER_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_SCALE_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_FULL_REPAINT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_REPAINT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.DESELECT_ALL, this);
			aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
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
	 * геттер
	 */
	public Dispatcher getInternalDispatcher() {
		return this.internalDispatcher;
	}

	/**
	 * обработка событий
	 */
	public void propertyChange(final PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(ContextChangeEvent.TYPE)) {
			final ContextChangeEvent cce = (ContextChangeEvent) pce;
			if (cce.isDomainSelected()) {
				final Identifier di = LoginManager.getDomainId();
				if (this.getMapView() == null) {
					return;
				}
				final Identifier di2 = this.getMapView().getDomainId();
				if (!di.equals(di2)) {
					this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
				}
			}
		} else if (pce.getPropertyName().equals(MapEvent.MAP_VIEW_CENTER_CHANGED)) {
			final DoublePoint p = (DoublePoint) pce.getNewValue();
			this.mapStatusbar.showLatLong(p.getY(), p.getX());
		} else if (pce.getPropertyName().equals(MapEvent.MAP_VIEW_SCALE_CHANGED)) {
			final Double p = (Double) pce.getNewValue();
			this.mapStatusbar.showScale(p.doubleValue());
		} else if (pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED)) {
			this.mapToolBar.setEnableDisablePanel(true);
		} else if (pce.getPropertyName().equals(MapEvent.MAP_VIEW_DESELECTED)) {
			this.mapToolBar.setEnableDisablePanel(false);
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
		Log.debugMessage("Closing map", Level.INFO);
		this.setContext(null);
	}

	public boolean checkChangesPresent() {
		boolean changesPresent = false;

		final MapView mapView = this.getMapView();

		if (mapView != null) {
			if (mapView.isChanged() && getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP_VIEW)) {
				changesPresent = true;
			} else {
				final Map map = this.getMapView().getMap();
				if (map != null) {
					if (map.isChanged() && getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP)) {
						changesPresent = true;
					}
				}
			}
		}
		if (changesPresent) {
			final String message = "Есть несохраненные измененные объекты. Продолжить?";

			final int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					message,
					"Объект изменен",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (ret == JOptionPane.YES_OPTION) {
				// TODO cancel changes
				changesPresent = false;
			}
		}
		return changesPresent;
	}

	public Map getMap() {
		return this.getMapViewer().getLogicalNetLayer().getMapView().getMap();
	}

	public MapView getMapView() {
		return this.getMapViewer().getLogicalNetLayer().getMapView();
	}

	void thisInternalFrameActivated(final InternalFrameEvent e) {
		this.getMapViewer().getVisualComponent().grabFocus();

		if (this.aContext.getDispatcher() != null) {
			if (this.getMapView() != null) {
				this.aContext.getDispatcher().firePropertyChange(new MapEvent(this.getMapView(), MapEvent.MAP_VIEW_SELECTED));
				this.aContext.getDispatcher().firePropertyChange(new MapEvent(this.getMapView().getMap(), MapEvent.MAP_SELECTED));
			}
		}
	}

	void thisInternalFrameClosed(final InternalFrameEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		}
		this.closeMap();
	}

	void thisInternalFrameDeactivated(final InternalFrameEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_DESELECTED));
		}
	}

	void thisInternalFrameOpened(final InternalFrameEvent e) {
		this.getMapViewer().getVisualComponent().grabFocus();
	}

	void thisComponentShown(final ComponentEvent e) {
		this.mapViewer.getVisualComponent().firePropertyChange(MAP_FRAME_SHOWN, false, true);
		MapFrame.this.mapViewer.getVisualComponent().requestFocus();
	}

	void thisComponentHidden(final ComponentEvent e) {
		if (this.aContext.getDispatcher() != null) {
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		}
		this.closeMap();
	}

	@Override
	public void doDefaultCloseAction() {
		if (super.isMaximum())
			try {
				super.setMaximum(false);
			} catch (java.beans.PropertyVetoException ex) {
				ex.printStackTrace();
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

