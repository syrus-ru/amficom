/**
 * $Id: MapFrame.java,v 1.47 2005/06/16 10:57:21 krupenn Exp $
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
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
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
import com.syrus.AMFICOM.client.map.command.navigate.ShowNodesCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomBoxCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomInCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomOutCommand;
import com.syrus.AMFICOM.client.map.command.navigate.ZoomToPointCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
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
 * @version $Revision: 1.47 $, $Date: 2005/06/16 10:57:21 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapFrame extends JInternalFrame 
		implements PropertyChangeListener
{
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
	 * Панель с доступными для добавления объектами карты
	 */
	protected MapElementsBarPanel mapElementsPanel;

	/**
	 * Панель отображающая координты курсора и масштаб
	 */
	protected MapStatusBar mapStatusbar;
	
	/**
	 * Экземпляр класса. Поскольку вид карты отнимает слишком много 
	 * ресурсов памяти, в одной Жаба-машине может быть только один
	 * экземпляр окна карты, разделяемый поочередно в разных модулях.
	 */
	protected static MapFrame singleton;

	private MapConnection mapConnection;

	protected MapFrame(ApplicationContext aContext)
		throws MapConnectionException, MapDataException
	{
		this();
		setContext(aContext);
	}
	
	public MapFrame()
		throws MapConnectionException, MapDataException
	{
		super();

		// экземпляр обозревателя создается менеджером карты на основе
		// данных, записанных в файле Map.properties

		this.mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());
		this.mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		this.mapConnection.setView(MapPropertiesManager.getDataBaseView());
		this.mapConnection.setURL(MapPropertiesManager.getDataBaseURL());
		this.mapConnection.connect();
		
		MapImageLoader loader = this.mapConnection.createImageLoader();
		MapContext mapContext = this.mapConnection.createMapContext();
		MapCoordinatesConverter converter = this.mapConnection.createCoordinatesConverter();

		MapImageRenderer renderer = MapImageRendererFactory.create(
				MapPropertiesManager.getMapImageRendererClassName(),
				loader); 

		LogicalNetLayer logicalNetLayer = new LogicalNetLayer(
				converter,
				mapContext);
		
		this.mapViewer = NetMapViewer.create(
				MapPropertiesManager.getNetMapViewerClassName(),
				logicalNetLayer,
				mapContext,
				renderer);

		this.mapViewer.init();
		
		logicalNetLayer.setMapViewController(MapViewController.createInstance(this.mapViewer));

		jbInit();

		initModule();
	}
	
	/**
	 * геттер
	 */
	public NetMapViewer getMapViewer()
	{
		return this.mapViewer;
	}

	private void jbInit()
	{
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/main/map_mini.gif")
				.getScaledInstance(16, 16, Image.SCALE_DEFAULT)));

		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setTitle(LangModelMap.getString("Map"));
		this.getContentPane().setLayout(new BorderLayout());

//		JPanel mapPanel = new JPanel();
		
		// визуальный компонент обозревателя карты
		JComponent mapVisualComponent;
	
		// обозреватель карты сам по себе не является компонентом, а содержит 
		// компонент в себе
		mapVisualComponent = this.mapViewer.getVisualComponent();

		this.mapToolBar = new MapToolBar(this.mapViewer);
		this.mapStatusbar = new MapStatusBar(this.mapViewer);
		this.mapElementsPanel = new MapElementsBarPanel();
		
//		mapPanel.setLayout(new BorderLayout());
//		mapPanel.add(mapVisualComponent, BorderLayout.CENTER);

		this.getContentPane().add(this.mapToolBar, BorderLayout.NORTH);
		this.getContentPane().add(this.mapStatusbar, BorderLayout.SOUTH);
		this.getContentPane().add(this.mapElementsPanel, BorderLayout.WEST);
//		this.getContentPane().add(mapPanel, BorderLayout.CENTER);
		this.getContentPane().add(mapVisualComponent, BorderLayout.CENTER);		
		
		this.addComponentListener(new MapMainFrameComponentAdapter(this));
		this.addInternalFrameListener(new MapMainFrameInternalFrameAdapter(this));
	}
	
	/**
	 * Данные отображения сохраняются в файл в обозревателе карты
	 */
	public void saveConfig()
	{
		if(this.mapViewer != null)
			this.mapViewer.saveConfig();
	}

	public void finalizeModule()
	{
		setContext(null);
		Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
	}

	/**
	 * Одноразовая инициализация окна карты
	 */	
	public void initModule()
	{
		Environment.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}

	public void setCommands(ApplicationModel aModel)
	{
		aModel.setCommand(MapApplicationModel.OPERATION_CENTER_SELECTION, new CenterSelectionCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.MODE_NODE_LINK, new MapModeCommand(aModel, this.mapViewer, MapApplicationModel.MODE_NODE_LINK, MapState.SHOW_NODE_LINK));
		aModel.setCommand(MapApplicationModel.MODE_LINK, new MapModeCommand(aModel, this.mapViewer, MapApplicationModel.MODE_LINK, MapState.SHOW_PHYSICAL_LINK));
		aModel.setCommand(MapApplicationModel.MODE_CABLE_PATH, new MapModeCommand(aModel, this.mapViewer, MapApplicationModel.MODE_CABLE_PATH, MapState.SHOW_CABLE_PATH));
		aModel.setCommand(MapApplicationModel.MODE_PATH, new MapModeCommand(aModel, this.mapViewer, MapApplicationModel.MODE_PATH, MapState.SHOW_MEASUREMENT_PATH));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_IN, new ZoomInCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_OUT, new ZoomOutCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT, new ZoomToPointCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_BOX, new ZoomBoxCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER, new MoveToCenterCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.MODE_NODES, new ShowNodesCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_HAND_PAN, new HandPanCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE, new MeasureDistanceCommand(aModel, this.mapViewer));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_FIXED, new MoveFixedCommand(aModel, this.mapViewer));

		aModel.fireModelChanged();
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_ELEMENT_CHANGED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
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

		this.getMapViewer().getLogicalNetLayer().setContext(aContext);

		if(aContext != null)
		{
			this.aContext = aContext;
			setModel(aContext.getApplicationModel());
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_ELEMENT_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.PLACE_ELEMENT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CENTER_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_SCALE_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_FULL_REPAINT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.NEED_REPAINT, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.DESELECT_ALL, this);
			aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			
			this.mapElementsPanel.setContext(this.aContext);		
			this.mapElementsPanel.setEnableDisablePanel(true);
		}
		
	}

	/**
	 * геттер
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		setCommands(aModel);
		aModel.addListener(this.mapToolBar);
		this.mapToolBar.setModel(aModel);
	    aModel.fireModelChanged();
	}

	/**
	 * геттер
	 */
	public ApplicationModel getModel()
	{
		return this.aContext.getApplicationModel();
	}

	/**
	 * геттер
	 */
	public Dispatcher getInternalDispatcher()
	{
		return this.internalDispatcher;
	}

	/**
	 * обработка событий
	 */
	public void propertyChange(PropertyChangeEvent pce)
	{
		if(pce.getPropertyName().equals(ContextChangeEvent.TYPE))
		{
			ContextChangeEvent cce = (ContextChangeEvent )pce;
			if(cce.isDomainSelected())
			{
				Identifier di = LoginManager.getDomainId();
				if(getMapView() == null)
					return;
				Identifier di2 = getMapView().getDomainId();
				if(!di.equals(di2))
				{
//					setMapView(null);
					this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
				}
			}
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_CENTER_CHANGED))
		{
			DoublePoint p = (DoublePoint )pce.getSource();
			this.mapStatusbar.showLatLong(p.getY(), p.getX());
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SCALE_CHANGED))
		{
			Double p = (Double )pce.getSource();
			this.mapStatusbar.showScale(p.doubleValue());
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			this.mapToolBar.setEnableDisablePanel(true);
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_DESELECTED))
		{
			this.mapToolBar.setEnableDisablePanel(false);
		}
		else
		{
			getMapViewer().propertyChange(pce);
		}
	}

	public void setMapView( MapView mapView)
		throws MapConnectionException, MapDataException
	{
		getMapViewer().getLogicalNetLayer().setMapView(mapView);
		if(mapView != null) {
			this.mapViewer.getMapContext().setScale(mapView.getScale());
			this.mapViewer.getMapContext().setCenter(mapView.getCenter());

		}
		this.mapViewer.repaint(true);
	}

	 void setMap( Map map)
	{
		getMapViewer().getLogicalNetLayer().setMap(map);
	}

	public void closeMap()
	{
		System.out.println("Closing map");
		setContext(null);
	}

	public boolean checkCanCloseMap()
	{
		boolean canClose;
		
		if(getMapView() == null)
			return true;
	
		Map map = getMapView().getMap();
		
		if(map == null)
			return true;
		
		if(!getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP))
			return true;
		
		if(map.isChanged())
		{
			String message = "Объект " + map.getName() 
				+ " [" + LangModelMap.getString("Map") + "] "
				+ "изменен. Сохранить?";
				
			int ret = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					message,
					"Сохранение объекта",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if(ret == JOptionPane.CANCEL_OPTION)
			{
				canClose = false;
			}
			else
			if(ret == JOptionPane.NO_OPTION)
			{
				canClose = true;
			}
			else
			if(ret == JOptionPane.YES_OPTION)
			{
				try
				{
					StorableObjectPool.putStorableObject(map);
				}
				catch (IllegalObjectEntityException e)
				{
					e.printStackTrace();
				}
				try
				{
					StorableObjectPool.flush(map.getId(), true);//save map
				} catch(ApplicationException e) {
					e.printStackTrace();
				}
				canClose = true;
			}
			else
				canClose = false;
		}
		else
			canClose = true;
		return canClose;
	}

	public boolean checkCanCloseMapView()
	{
		boolean canClose;
	
		MapView mapView = getMapView();
		
		if(mapView == null)
			return true;
	
		if(!getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_SAVE_MAP_VIEW))
			return true;

		if(mapView.isChanged())
		{
			String message = "Объект " + mapView.getName() 
				+ " [" + LangModelMap.getString("MapView") + "] "
				+ "изменен. Сохранить?";
				
			int ret = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					message,
					"Сохранение объекта",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if(ret == JOptionPane.CANCEL_OPTION)
			{
				canClose = false;
			}
			else
			if(ret == JOptionPane.NO_OPTION)
			{
				canClose = true;
			}
			else
			if(ret == JOptionPane.YES_OPTION)
			{
//				getContext().getDataSource().SaveMapView(mapView.getId());
				for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
				{
					Scheme scheme = (Scheme )it.next();
					if(scheme.isChanged())
						try
						{
							StorableObjectPool.flush(scheme.getId(), true);// save scheme
						}
						catch (VersionCollisionException e)
						{
							e.printStackTrace();
						}
						catch (IllegalDataException e)
						{
							e.printStackTrace();
						}
						catch (CommunicationException e)
						{
							e.printStackTrace();
						}
						catch (DatabaseException e)
						{
							e.printStackTrace();
						}
						catch(ApplicationException aExc)
						{
							aExc.printStackTrace();
						}
				}
				canClose = true;
			}
			else
				canClose = false;
		}
		else
			canClose = true;
		return canClose;
	}

	public Map getMap()
	{
		return getMapViewer().getLogicalNetLayer().getMapView().getMap();
	}

	public MapView getMapView()
	{
		return getMapViewer().getLogicalNetLayer().getMapView();
	}

	void thisInternalFrameActivated(InternalFrameEvent e)
	{
//		this.grabFocus();
		getMapViewer().getVisualComponent().grabFocus();

		if(this.aContext.getDispatcher() != null)
			if(getMapView() != null)
			{
				this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(getMapView(), MapEvent.MAP_VIEW_SELECTED));
				this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(getMapView().getMap(), MapEvent.MAP_SELECTED));
			}
	}

	void thisInternalFrameClosed(InternalFrameEvent e)
	{
		if(this.aContext.getDispatcher() != null)
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		closeMap();
	}

	void thisInternalFrameDeactivated(InternalFrameEvent e)
	{
		if(this.aContext.getDispatcher() != null)
		{
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_DESELECTED));
		}
	}

	void thisInternalFrameOpened(InternalFrameEvent e)
	{
//		this.grabFocus();
		getMapViewer().getVisualComponent().grabFocus();
	}

	void thisComponentShown(ComponentEvent e)
	{
		this.mapViewer.getVisualComponent().firePropertyChange(MAP_FRAME_SHOWN, false, true);
		MapFrame.this.mapViewer.getVisualComponent().requestFocus();
	}

	void thisComponentHidden(ComponentEvent e)
	{
		if(this.aContext.getDispatcher() != null)
			this.aContext.getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		closeMap();
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
		try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
    }

	private class MapMainFrameComponentAdapter extends java.awt.event.ComponentAdapter
	{
		MapFrame adaptee;
	
		MapMainFrameComponentAdapter(MapFrame adaptee)
		{
			this.adaptee = adaptee;
		}
	
		public void componentShown(ComponentEvent e)
		{
			this.adaptee.thisComponentShown(e);
		}
	
		public void componentHidden(ComponentEvent e)
		{
			this.adaptee.thisComponentHidden(e);
		}
	}
	
	private class MapMainFrameInternalFrameAdapter extends javax.swing.event.InternalFrameAdapter
	{
		MapFrame adaptee;
	
		MapMainFrameInternalFrameAdapter(MapFrame adaptee)
		{
			this.adaptee = adaptee;
		}
		public void internalFrameActivated(InternalFrameEvent e)
		{
			this.adaptee.thisInternalFrameActivated(e);
		}
		public void internalFrameClosed(InternalFrameEvent e)
		{
			this.adaptee.thisInternalFrameClosed(e);
		}
		public void internalFrameDeactivated(InternalFrameEvent e)
		{
			this.adaptee.thisInternalFrameDeactivated(e);
		}
		public void internalFrameOpened(InternalFrameEvent e)
		{
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

