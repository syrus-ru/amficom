/**
 * $Id: MapFrame.java,v 1.25 2005/02/18 12:19:46 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;

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
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;
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
 * @version $Revision: 1.25 $, $Date: 2005/02/18 12:19:46 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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


		MapConnection mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());
		mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		mapConnection.setView(MapPropertiesManager.getDataBaseView());
		mapConnection.setURL(MapPropertiesManager.getDataBaseURL());
		mapConnection.connect();

		this.mapViewer = NetMapViewer.create(MapPropertiesManager.getNetMapViewerClassName());
		this.mapViewer.setConnection(mapConnection);
		this.mapViewer.init();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		initModule();

		this.mapToolBar.setLogicalNetLayer(this.mapViewer.getLogicalNetLayer());
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

		JPanel toolBarPanel = new JPanel();
		JPanel mapPanel = new JPanel();
		
		// визуальный компонент обозревателя карты
		JComponent mapVisualComponent;
	
		// обозреватель карты сам по себе не является компонентом, а содержит 
		// компонент в себе
		mapVisualComponent = this.mapViewer.getVisualComponent();

		this.mapToolBar = new MapToolBar();
		toolBarPanel.setLayout(new BorderLayout());
		toolBarPanel.add(this.mapToolBar, BorderLayout.WEST);

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
		if(this.mapViewer != null)
			this.mapViewer.saveConfig();
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
		Environment.getDispatcher().register(this, ContextChangeEvent.type);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
				this.aContext.getDispatcher().unregister(this, MapEvent.NEED_FULL_REPAINT);
				this.aContext.getDispatcher().unregister(this, MapEvent.NEED_REPAINT);
				this.aContext.getDispatcher().unregister(this, MapEvent.DESELECT_ALL);
				this.aContext.getDispatcher().unregister(this, SchemeNavigateEvent.type);
				this.aContext.getDispatcher().unregister(this, CatalogNavigateEvent.type);
				this.aContext.getDispatcher().unregister(this, TreeListSelectionEvent.typ);
				this.aContext.getDispatcher().unregister(this, TreeDataSelectionEvent.type);
			}
		if(aContext != null)
		{
			this.aContext = aContext;
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
			aContext.getDispatcher().register(this, MapEvent.NEED_FULL_REPAINT);
			aContext.getDispatcher().register(this, MapEvent.NEED_REPAINT);
			aContext.getDispatcher().register(this, MapEvent.DESELECT_ALL);
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
	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )ae;
			if(cce.DOMAIN_SELECTED)
			{
				Identifier di = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
				if(getMapView() == null)
					return;
				Identifier di2 = getMapView().getDomainId();
				if(!di.equals(di2))
				{
//					setMapView(null);
					this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
				}
			}
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CENTER_CHANGED))
		{
			DoublePoint p = (DoublePoint )ae.getSource();
			this.mapToolBar.showLatLong(p.getX(), p.getY());
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SCALE_CHANGED))
		{
			Double p = (Double )ae.getSource();
			this.mapToolBar.showScale(p.doubleValue());
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			this.mapToolBar.setEnableDisablePanel(true);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_DESELECTED))
		{
			this.mapToolBar.setEnableDisablePanel(false);
		}
		else
		{
			getMapViewer().getLogicalNetLayer().operationPerformed(ae);
		}
	}

	public void setMapView( MapView mapView)
		throws MapConnectionException, MapDataException
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
					MapStorableObjectPool.putStorableObject(map);
				}
				catch (IllegalObjectEntityException e)
				{
					e.printStackTrace();
				}
				try
				{
					MapStorableObjectPool.flush(true);//save map
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
				+ " [" + LangModel.getString("MapView") + "] "
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
					if(scheme.changed())
						try
						{
							SchemeStorableObjectPool.flush(true);// save scheme
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

		if(this.aContext.getDispatcher() != null)
			if(getMapView() != null)
			{
				this.aContext.getDispatcher().notify(
					new MapEvent(getMapView(), MapEvent.MAP_VIEW_SELECTED));
				this.aContext.getDispatcher().notify(
					new MapEvent(getMapView().getMap(), MapEvent.MAP_SELECTED));
			}
	}

	void thisInternalFrameClosed(InternalFrameEvent e)
	{
		if(this.aContext.getDispatcher() != null)
			this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		closeMap();
	}

	void thisInternalFrameDeactivated(InternalFrameEvent e)
	{
		if(this.aContext.getDispatcher() != null)
		{
			this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
			this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_DESELECTED));
		}
	}

	void thisInternalFrameOpened(InternalFrameEvent e)
	{
//		this.grabFocus();
		getMapViewer().getVisualComponent().grabFocus();
	}

	void thisComponentShown(ComponentEvent e)
	{//empty
	}

	void thisComponentHidden(ComponentEvent e)
	{
		if(this.aContext.getDispatcher() != null)
			this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
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
	
}

