/**
 * $Id: MapFrame.java,v 1.13 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.CatalogNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Module;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.CenterSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.HandPanCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.MapModeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.MeasureDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.MoveFixedCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.MoveToCenterCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.ShowNodesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.ZoomBoxCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.ZoomInCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.ZoomOutCommand;
import com.syrus.AMFICOM.Client.Map.Command.Navigate.ZoomToPointCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;

import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
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
 * @version $Revision: 1.13 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapFrame extends JInternalFrame 
		implements OperationListener, Module
{
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
	 * Экземпляр класса. Поскольку вид карты отнимает слишком много 
	 * ресурсов памяти, в одной Жаба-машине может быть только один
	 * экземпляр окна карты, разделяемый поочередно в разных модулях.
	 */
	protected static MapFrame singleton;

	protected MapFrame(ApplicationContext aContext)
	{
		this();
		setContext(aContext);
	}
	
	protected MapFrame()
	{
		super();

		// экземпляр обозревателя создается менеджером карты на основе
		// данных, записанных в файле Map.properties

		mapViewer = NetMapViewer.create(MapPropertiesManager.getNetMapViewerClassName());
//		mapViewer = MapPropertiesManager.getNetMapViewer();

		MapConnection mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());

		mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		mapConnection.setView(MapPropertiesManager.getDataBaseView());

//		MapConnection mapConnection = MapPropertiesManager.getConnection();
		mapConnection.connect();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		initModule();
		mapViewer.setConnection(mapConnection);
		mapToolBar.setLogicalNetLayer(mapViewer.getLogicalNetLayer());
	}
	
	public static MapFrame getMapMainFrame()
	{
		if(singleton == null)
			singleton = new MapFrame();
		return singleton;
	}
	
	/**
	 * геттер
	 */
	public NetMapViewer getMapViewer()
	{
		return mapViewer;
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

		JPanel toolBarPanel = new JPanel();
		JPanel mapPanel = new JPanel();
		
		// визуальный компонент обозревателя карты
		JComponent mapVisualComponent;
	
		// обозреватель карты сам по себе не является компонентом, а содержит 
		// компонент в себе
		mapVisualComponent = mapViewer.getVisualComponent();

		mapToolBar = new MapToolBar();
		toolBarPanel.setLayout(new BorderLayout());
		toolBarPanel.add(mapToolBar, BorderLayout.WEST);

		mapPanel.setLayout(new BorderLayout());
		mapPanel.add(mapVisualComponent, BorderLayout.CENTER);

		this.getContentPane().add(toolBarPanel, BorderLayout.NORTH);
		this.getContentPane().add(mapPanel, BorderLayout.CENTER);

		this.addComponentListener(new MapMainFrameComponentAdapter(this));
		this.addInternalFrameListener(new MapMainFrameInternalFrameAdapter(this));
	}
	
	/**
	 * Данные отображения сохраняются в файл в обозревателе карты
	 */
	public void saveConfig()
	{
		if(mapViewer != null)
			mapViewer.saveConfig();
	}

	public void finalizeModule()
	{
		setContext(null);
		Environment.getDispatcher().unregister(this, ContextChangeEvent.type);
	}

	/**
	 * Одноразовая инициализация окна карты
	 */	
	public void initModule()
	{
	    // load values from properties file
		mapViewer.init();
		Environment.getDispatcher().register(this, ContextChangeEvent.type);
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	public void setCommands(ApplicationModel aModel)
	{
		aModel.setCommand(MapApplicationModel.OPERATION_CENTER_SELECTION, new CenterSelectionCommand(null));
		aModel.setCommand(MapApplicationModel.MODE_NODE_LINK, new MapModeCommand(null, MapApplicationModel.MODE_NODE_LINK, MapState.SHOW_NODE_LINK));
		aModel.setCommand(MapApplicationModel.MODE_LINK, new MapModeCommand(null, MapApplicationModel.MODE_LINK, MapState.SHOW_PHYSICAL_LINK));
		aModel.setCommand(MapApplicationModel.MODE_CABLE_PATH, new MapModeCommand(null, MapApplicationModel.MODE_CABLE_PATH, MapState.SHOW_CABLE_PATH));
		aModel.setCommand(MapApplicationModel.MODE_PATH, new MapModeCommand(null, MapApplicationModel.MODE_PATH, MapState.SHOW_MEASUREMENT_PATH));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_IN, new ZoomInCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_OUT, new ZoomOutCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT, new ZoomToPointCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_ZOOM_BOX, new ZoomBoxCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER, new MoveToCenterCommand(null));
		aModel.setCommand(MapApplicationModel.MODE_NODES, new ShowNodesCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_HAND_PAN, new HandPanCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE, new MeasureDistanceCommand(null));
		aModel.setCommand(MapApplicationModel.OPERATION_MOVE_FIXED, new MoveFixedCommand(null));

		aModel.fireModelChanged();
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_ELEMENT_CHANGED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_ELEMENT_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_ELEMENT_DESELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_NAVIGATE);
				this.aContext.getDispatcher().unregister(this, MapEvent.PLACE_ELEMENT);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CENTER_CHANGED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SCALE_CHANGED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_CHANGED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CHANGED);
				this.aContext.getDispatcher().unregister(this, SchemeNavigateEvent.type);
				this.aContext.getDispatcher().unregister(this, CatalogNavigateEvent.type);
				this.aContext.getDispatcher().unregister(this, TreeListSelectionEvent.typ);
				this.aContext.getDispatcher().unregister(this, TreeDataSelectionEvent.type);
			}
		if(aContext != null)
		{
			this.aContext = aContext;
			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
			setModel(aContext.getApplicationModel());
			aContext.getDispatcher().register(this, MapEvent.MAP_ELEMENT_CHANGED);
			aContext.getDispatcher().register(this, MapEvent.MAP_ELEMENT_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_ELEMENT_DESELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_NAVIGATE);
			aContext.getDispatcher().register(this, MapEvent.PLACE_ELEMENT);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CENTER_CHANGED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SCALE_CHANGED);
			aContext.getDispatcher().register(this, MapEvent.MAP_CHANGED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CHANGED);
			aContext.getDispatcher().register(this, SchemeNavigateEvent.type);
			aContext.getDispatcher().register(this, CatalogNavigateEvent.type);
			aContext.getDispatcher().register(this, TreeListSelectionEvent.typ);
			aContext.getDispatcher().register(this, TreeDataSelectionEvent.type);
		}
		
		this.getMapViewer().getLogicalNetLayer().setContext(aContext);
	}

	/**
	 * геттер
	 */
	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		setCommands(aModel);
		aModel.addListener(mapToolBar);
		mapToolBar.setModel(aModel);
	    aModel.fireModelChanged();
	}

	/**
	 * геттер
	 */
	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	/**
	 * геттер
	 */
	public Dispatcher getInternalDispatcher()
	{
		return internalDispatcher;
	}

	/**
	 * обработка событий
	 */
	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )ae;
			if(cce.DOMAIN_SELECTED)
			{
				String di = aContext.getSessionInterface().getDomainId();
				if(getMapView() == null)
					return;
				String di2 = getMapView().getDomainId();
				if(!di.equals(di2))
				{
					setMapView(null);
					aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
				}
			}
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CENTER_CHANGED))
		{
			DoublePoint p = (DoublePoint )ae.getSource();
			mapToolBar.showLatLong(p.x, p.y);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SCALE_CHANGED))
		{
			Double p = (Double )ae.getSource();
			mapToolBar.showScale(p.doubleValue());
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			mapToolBar.setEnableDisablePanel(true);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_DESELECTED))
		{
			mapToolBar.setEnableDisablePanel(false);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(CatalogNavigateEvent.type))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.PLACE_ELEMENT))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_CHANGED))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_SELECTED))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_DESELECTED))
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
	}

	public void setMapView( MapView mapView)
	{
		getMapViewer().getLogicalNetLayer().setMapView(mapView);

	}

	 void setMap( Map map)
	{
		getMapViewer().getLogicalNetLayer().setMap(map);
	}

	public void closeMap()
	{
		System.out.println("Closing map");
		setMapView(null);
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
				+ " [" + LangModelMap.getString("nodemap") + "] "
				+ "изменен. Сохранить?";
				
			String title = "Сохранение объекта";

			int ret = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					message,
					title,
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
				getContext().getDataSource().SaveMap(map.getId());
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
				+ " [" + LangModel.getString("node" + MapView.typ) + "] "
				+ "изменен. Сохранить?";
				
			String title = "Сохранение объекта";

			int ret = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					message,
					title,
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
				getContext().getDataSource().SaveMapView(mapView.getId());
				for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
				{
					Scheme scheme = (Scheme )it.next();
					if(scheme.isChanged())
						getContext().getDataSource().SaveScheme(scheme.getId());
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

	/**
	 * используется для востанивления класса из базы данных
	 */
	public void createFromPool(Map map, LogicalNetLayer logical)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "createFromPool(" + logical + ")");
		
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

		if(aContext.getDispatcher() != null)
			if(getMapView() != null)
			{
				aContext.getDispatcher().notify(
					new MapEvent(getMapView(), MapEvent.MAP_VIEW_SELECTED));
				aContext.getDispatcher().notify(
					new MapEvent(getMapView().getMap(), MapEvent.MAP_SELECTED));
			}
	}

	void thisInternalFrameClosed(InternalFrameEvent e)
	{
		if(aContext.getDispatcher() != null)
			aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		closeMap();
	}

	void thisInternalFrameDeactivated(InternalFrameEvent e)
	{
		if(aContext.getDispatcher() != null)
		{
			aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
			aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_DESELECTED));
		}
	}

	void thisInternalFrameOpened(InternalFrameEvent e)
	{
//		this.grabFocus();
		getMapViewer().getVisualComponent().grabFocus();
	}

	void thisComponentShown(ComponentEvent e)
	{
	}

	void thisComponentHidden(ComponentEvent e)
	{
		if(aContext.getDispatcher() != null)
			aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
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
			adaptee.thisComponentShown(e);
		}
	
		public void componentHidden(ComponentEvent e)
		{
			adaptee.thisComponentHidden(e);
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
			adaptee.thisInternalFrameActivated(e);
		}
		public void internalFrameClosed(InternalFrameEvent e)
		{
			adaptee.thisInternalFrameClosed(e);
		}
		public void internalFrameDeactivated(InternalFrameEvent e)
		{
			adaptee.thisInternalFrameDeactivated(e);
		}
		public void internalFrameOpened(InternalFrameEvent e)
		{
			adaptee.thisInternalFrameOpened(e);
		}
	}
	
}

