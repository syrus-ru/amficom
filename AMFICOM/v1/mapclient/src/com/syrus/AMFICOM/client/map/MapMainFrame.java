package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Command.Map.*;
import com.syrus.AMFICOM.Client.General.Command.MapNav.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.io.*;

import com.ofx.mapViewer.SxMapLayerEvent;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentEvent;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import oracle.jdeveloper.layout.XYConstraints;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;

public class MapMainFrame extends JInternalFrame implements OperationListener
{
	public MapMenuBar mapMenuBar;

	Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext;
	public JFrame mapJFrame;

	public static IniFile iniFile;
	static String iniFileName = "Map.properties";//Фаил откуда загружаются данные

	public String data_base_path;
	public String data_base_name;

	public String DEFAULT_data_base_path = "localhost:/d:/spatialfx2.0/data/mosfinaldb";
	public String DEFAULT_data_base_name = "moscow";

	JPanel toolBarPanel = new JPanel();
	JPanel elementsPanel = new JPanel();
	JPanel mapPanel = new JPanel();

	public ElementPaneToolBar elementPaneToolBar = new ElementPaneToolBar();
	public MapScrollPane myMapScrollPane;//прокрутчик для обозревателя карты
	public JScrollPane elementsScrollPane = new JScrollPane();

	public NetMapViewer myMapViewer;//обозреватель карты
	public MapToolBar mapToolBar;//Меню для работы с картой

	public AnimateThread animateThread;
/*
	public void addElementsPanel(JPanel panel)
	{
//		mapFrame.add(panel);
	}
*/
	public MapMainFrame(ApplicationContext aContext)
	{
		super();

		mapJFrame = (JFrame)(this.getParent());

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
		init_module();
	}

	public MapMainFrame(Vector panelElements, ApplicationContext aContext)
	{
		this(aContext);
		setProtoElements(panelElements);
	}

	public void setProtoElements(Vector panelElements)
	{
		elementPaneToolBar.setProtoElements(panelElements);
	}

	private void jbInit() throws Exception
	{
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")
				.getScaledInstance(16, 16, Image.SCALE_DEFAULT)));
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setTitle(LangModelMap.getString("AppTitle"));
		this.addComponentListener(new MapMain_this_componentAdapter(this));

		myMapViewer = NetMapViewer.createNetMapViewer(this);
		myMapScrollPane = new MapScrollPane(myMapViewer);

		mapToolBar = new MapToolBar(myMapViewer.lnl);

		elementsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		elementsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		toolBarPanel.setLayout(new BorderLayout());
		toolBarPanel.add(mapToolBar, BorderLayout.WEST);

		mapPanel.setLayout(new BorderLayout());
		mapPanel.add(myMapScrollPane, BorderLayout.CENTER);

		elementsPanel.setLayout(new BorderLayout());
		elementsPanel.add(elementsScrollPane, BorderLayout.CENTER);

//		mapMenuBar = new MapMenuBar();

		this.getContentPane().setLayout(new BorderLayout());
		this.setBounds(0,0,600,400);
		this.addInternalFrameListener(new MapMainFrame_this_internalFrameAdapter(this));
//		this.setJMenuBar(mapMenuBar);

		MapMainFrame_this_KeyAdapter myKeyAdapter = new MapMainFrame_this_KeyAdapter(this);
		this.addContainerListener(new ContainerAdapter()
			{
				public void componentRemoved(ContainerEvent e)
				{
					this_componentRemoved(e);
				}
			});

		myMapScrollPane.addKeyListener(myKeyAdapter);
		this.getContentPane().add(toolBarPanel, BorderLayout.NORTH);
		this.getContentPane().add(mapPanel, BorderLayout.CENTER);
//		this.getContentPane().add(elementsPanel, BorderLayout.SOUTH);

		mapToolBar.add(elementPaneToolBar, new XYConstraints(590, 0, 100, 29));

/*
        this.addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
				aContext.getDispatcher().notify(new OperationEvent(this, 0, "mapcloseevent"));
            }
        });	
*/		
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
	    // load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
			setFromIniFile();
		}
		catch(java.io.IOException e)
		{
			setDefaults();
		}
		setMap( data_base_path, data_base_name);

		Environment.the_dispatcher.register(this, ContextChangeEvent.type);

		setCommands(aModel);

		aModel.setCommand("menuMapNew", new MapNewCommand(this, aContext));
		aModel.setCommand("menuMapClose", new MapCloseCommand(this));
		aModel.setCommand("menuMapOpen", new MapOpenCommand((JDesktopPane )this.getParent(), this, aContext));
		aModel.setCommand("menuMapSave", new MapSaveCommand(this, aContext));

		aModel.setCommand("menuMapOptions", new MapSelectMapViewerCommand(this));

		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.setEnabled("menuEditUndo", false);
		aModel.setEnabled("menuEditRedo", false);
		aModel.setEnabled("menuEditCut", false);
		aModel.setEnabled("menuEditCopy", false);
		aModel.setEnabled("menuEditPaste", false);
		aModel.setEnabled("menuEditSelect", false);

		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled("menuViewRefresh", false);

		aModel.setEnabled("menuNavigate", false);

		aModel.setEnabled("menuElementCatalogue", false);
		aModel.setEnabled("menuElementGroup", false);
		aModel.setEnabled("menuElementUngroup", false);
		aModel.setEnabled("menuElementAlign", false);

		aModel.setEnabled("menuHelpContents", false);
		aModel.setEnabled("menuHelpFind", false);
		aModel.setEnabled("menuHelpTips", false);
		aModel.setEnabled("menuHelpCourse", false);
		aModel.setEnabled("menuHelpHelp", false);
		aModel.setEnabled("menuHelpAbout", false);

		aModel.fireModelChanged("");

		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
//		if(!aModel.isEnabled("mapActionShowProto"))
//			this.getContentPane().remove(elementsPanel);
//			mapToolBar.remove(elementPaneToolBar);
	}

	public void setCommands(ApplicationModel aModel)
	{
		aModel.setCommand("mapActionCenterSelection", new CenterSelectionCommand(null));
		aModel.setCommand("mapModeNodeLink", new MapModeCommand(null, "mapModeNodeLink", MapContext.SHOW_NODE_LINK));
		aModel.setCommand("mapModeLink", new MapModeCommand(null, "mapModeLink", MapContext.SHOW_PHYSICALLINK));
		aModel.setCommand("mapModePath", new MapModeCommand(null, "mapModePath", MapContext.SHOW_TRANSMISSION_PATH));
		aModel.setCommand("mapActionZoomIn", new ZoomInCommand(null));
		aModel.setCommand("mapActionZoomOut", new ZoomOutCommand(null));
		aModel.setCommand("mapActionZoomToPoint", new ZoomToPointCommand(null));
		aModel.setCommand("mapActionZoomBox", new ZoomBoxCommand(null));
		aModel.setCommand("mapActionMoveToCenter", new MoveToCenterCommand(null));
		aModel.setCommand("mapModeViewNodes", new ShowNodesCommand(null));
		aModel.setCommand("mapActionHandPan", new HandPanCommand(null));
	}

	public void setMap( String myDataBasePath , String myBataBaseName )
	{
		myMapViewer.setMap(myDataBasePath , myBataBaseName);
	}

	public void closeMap()
	{
		System.out.println("Closing map");
		setMapContext(null);
		setContext(null);
//		myMapViewer.closeMap();
//		com.ofx.base.SxEnvironment.singleton().getQuery().close();
	}

//Установить значения из инициализационного файла
	public void setFromIniFile()
	{
		double last_long;
		double last_lat;

		data_base_path = iniFile.getValue("data_base_path");
		data_base_name = iniFile.getValue("data_base_name");
	}

	public void setDefaults()
	{
		data_base_path = DEFAULT_data_base_path;
		data_base_name = DEFAULT_data_base_name;
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext!= null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().unregister(this, "mapnavigate");
				this.aContext.getDispatcher().unregister(this, "placeElement");
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
			elementPaneToolBar.setVisible(aContext.getApplicationModel().isVisible("mapActionShowProto"));
			aContext.getDispatcher().register(this, "mapnavigate");
			aContext.getDispatcher().register(this, "placeElement");
			aContext.getDispatcher().register(this, SchemeNavigateEvent.type);
			aContext.getDispatcher().register(this, CatalogNavigateEvent.type);
			aContext.getDispatcher().register(this, TreeListSelectionEvent.typ);
			aContext.getDispatcher().register(this, TreeDataSelectionEvent.type);
		}
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		setCommands(aModel);
//		aModel.addListener(mapMenuBar);
		aModel.addListener(mapToolBar);
//	    mapMenuBar.setModel(aModel);
		mapToolBar.setModel(aModel);
//		mapPanel.setModel();

		if (aContext.getApplicationModel().isEnabled("mapActionShowProto"))
			elementPaneToolBar.setVisible(true);
		else
			elementPaneToolBar.setVisible(false);

	    aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(lnl() == null)
			return;
		if(ae.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )ae;
			if(cce.DOMAIN_SELECTED)
			{
				String di = aContext.getSessionInterface().getDomainId();
				if(getMapContext() == null)
					return;
				String di2 = getMapContext().getDomainId();
				if(!di.equals(di2))
				{
					this.setMapContext(null);
					aContext.getDispatcher().notify(new OperationEvent(this, 0, "mapcloseevent"));
				}
			}
		}
		if(ae.getActionCommand().equals("mapnavigate"))
		{
			MapNavigateEvent mne = (MapNavigateEvent )ae;

//Здесь принимаюттся собитыя по создению и управлению маркером
			if(mne.DATA_MARKER_CREATED)
			{
//				System.out.println("event_DATA_MARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp != null && tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapMarker marker;
				if(path != null)
				{
					marker = new MapMarker(
						mne.marker_id,
	                    lnl().getMapContext(),
	                    new Rectangle(14, 14),
						"",
						mne.distance,
						path);
					lnl().getMapContext().markers.add(marker);
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_EVENTMARKER_CREATED)
			{
//				System.out.println("event_DATA_EVENTMARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapEventMarker marker;
				if(path != null)
				{
					marker = new MapEventMarker(
						mne.marker_id,
	                    lnl().getMapContext(),
	                    new Rectangle(14, 14),
						"",
						mne.distance,
						path);
					marker.descriptor = mne.descriptor;
					lnl().getMapContext().markers.add(marker);
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_ALARMMARKER_CREATED)
			{
//				System.out.println("event_DATA_ALARMMARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapAlarmMarker marker = null;
				if(path != null)
				{
					for(Iterator it = lnl().getMapContext().markers.iterator(); it.hasNext();)
					{
						try
						{
							marker = (MapAlarmMarker )it.next();
							if(marker.path_id.equals(path.getId()))
								break;
							marker = null;
						}
						catch(Exception ex)
						{
						}
					}
					if(marker == null)
					{
						marker = new MapAlarmMarker(
							mne.marker_id,
							lnl().getMapContext(),
							new Rectangle(14, 14),
							"",
							mne.distance,
							path,
							mne.linkID);
						marker.descriptor = mne.descriptor;
						lnl().getMapContext().markers.add(marker);
					}
					else
					{
						marker.id = mne.marker_id;
					}
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}

				boolean found = false;

				MapPhysicalLinkElement link = findMapLinkByCableLink(mne.linkID);
				if(link != null)
				{
					link.setAlarmState(true);
					link.select();
				}
				else
				{
					MapEquipmentNodeElement node = findMapElementByCableLink(mne.linkID);
					if(node != null)
					{
						node.setAlarmState(true);
						node.select();
					}
				}
			}
			if(mne.DATA_MARKER_MOVED)
			{
//				System.out.println("event_DATA_MARKER_MOVED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
				{
					if(marker.spd == null)
						marker.spd = (SchemePathDecompositor )mne.spd;
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_MARKER_SELECTED)
			{
//				System.out.println("event_DATA_MARKER_SELECTED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
					marker.getMessage_Marker_Selected();
			}
			if(mne.DATA_MARKER_DESELECTED)
			{
//				System.out.println("event_DATA_MARKER_DESELECTED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
					marker.getMessage_Marker_Deselected();
			}
			if(mne.DATA_MARKER_DELETED)
			{
//				System.out.println("event_DATA_MARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
					lnl().getMapContext().markers.remove(marker);
			}
			if(mne.DATA_EVENTMARKER_DELETED)
			{
//				System.out.println("event_DATA_EVENTMARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
					lnl().getMapContext().markers.remove(marker);
			}
			if(mne.DATA_ALARMMARKER_DELETED)
			{
//				System.out.println("event_DATA_ALARMMARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapAlarmMarker marker = (MapAlarmMarker )lnl().getMapContext().getMarker(mne.marker_id);
				if(marker != null)
				{
					lnl().getMapContext().markers.remove(marker);

					MapPhysicalLinkElement link = findMapLinkByCableLink(marker.link_id);
					if(link != null)
					{
						link.setAlarmState(false);
						link.deselect();
					}
					else
					{
						MapEquipmentNodeElement node = findMapElementByCableLink(marker.link_id);
						if(node != null)
						{
							node.setAlarmState(false);
							node.deselect();
						}
					}
				}
			}
			if(mne.MAP_PATH_SELECTED)
				if(lnl().perform_processing)
				{
//					System.out.println("event_DATA_MAP_PATH_SELECTED");
					MapTransmissionPathElement mtpe = 
							lnl().getMapContext().getMapTransmissionPathElement(mne.mappathID);
					if(mtpe != null)
						mtpe.select();
				}
			if(mne.MAP_PATH_DESELECTED)
				if(lnl().perform_processing)
				{
//					System.out.println("event_DATA_MAP_PATH_DESELECTED");
					MapTransmissionPathElement mtpe = 
							lnl().getMapContext().getMapTransmissionPathElement(mne.mappathID);
					if(mtpe != null)
						mtpe.deselect();
				}
			if(mne.MAP_ELEMENT_SELECTED)
				if(lnl().perform_processing)
				{
//					System.out.println("event_MAP_ELEMENT_SELECTED");
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.select();
				}
			if(mne.MAP_ELEMENT_DESELECTED)
				if(lnl().perform_processing)
				{
//					System.out.println("event_MAP_ELEMENT_DESELECTED");
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.deselect();
				}
			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
		if(ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)ae;

			DataSet data = tdse.getDataSet();
			int n = tdse.getSelectionNumber();

			if (n != -1)
			{
				try 
				{
					MapElement me = (MapElement )data.get(n);
					me.select();
					lnl().postDirtyEvent();
					lnl().postPaintEvent();
				} 
				catch (Exception ex) 
				{
				} 
			}
		}
		if(ae.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			try 
			{
				MapElement me = (MapElement )ae.getSource();
				me.select();
				lnl().postDirtyEvent();
				lnl().postPaintEvent();
			} 
			catch (Exception ex) 
			{
			} 
		}
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
			if(lnl().perform_processing)
		{
			SchemeNavigateEvent sne = (SchemeNavigateEvent )ae;

			if(sne.SCHEME_ELEMENT_SELECTED)
			{
//				System.out.println("SCHEME_ELEMENT_SELECTED");
				SchemeElement[] ses = (SchemeElement[] )sne.getSource();
				Scheme s = (Scheme )Pool.get(Scheme.typ, this.getMapContext().scheme_id);

				for(Iterator it = lnl().getMapContext().getNodes().iterator(); it.hasNext();)
				{
					Object obj = it.next();
					if(	obj instanceof MapEquipmentNodeElement )
					{
						MapEquipmentNodeElement mape = (MapEquipmentNodeElement )obj;
						for(int i = 0; i < ses.length; i++)
						{
							SchemeElement se = s.getTopologicalElement(ses[i].getId());
							if(mape.element_id.equals(se.getId()))
								mape.select();
						}
					}
				}
			}

			if(sne.SCHEME_PATH_SELECTED)
			{
//				System.out.println("SCHEME_PATH_SELECTED");
				SchemePath[] sps = (SchemePath[] )sne.getSource();
				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					for(int i = 0; i < sps.length; i++)
						if(mappath.PATH_ID.equals(sps[i].getId()))
							mappath.select();
				}
			}

			if(sne.SCHEME_CABLE_LINK_SELECTED)
			{
//				System.out.println("SCHEME_CABLE_LINK_SELECTED");
				SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
				for(Iterator it = lnl().getMapContext().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					for(int i = 0; i < scs.length; i++)
						if(link.LINK_ID.equals(scs[i].getId()))
							link.select();
				}
			}

			if(sne.SCHEME_ELEMENT_DESELECTED)
			{
//				System.out.println("SCHEME_ELEMENT_DESELECTED");
				SchemeElement[] ses = (SchemeElement[] )sne.getSource();
				Scheme s = (Scheme )Pool.get(Scheme.typ, this.getMapContext().scheme_id);

				for(Iterator it = lnl().getMapContext().getNodes().iterator(); it.hasNext();)
				{
					Object obj = it.next();
					if(	obj instanceof MapEquipmentNodeElement )
					{
						MapEquipmentNodeElement mape = (MapEquipmentNodeElement )obj;
						for(int i = 0; i < ses.length; i++)
						{
							SchemeElement se = s.getTopologicalElement(ses[i].getId());
							if(mape.element_id.equals(se.getId()))
								mape.deselect();
						}
					}
				}
			}

			if(sne.SCHEME_PATH_DESELECTED)
			{
//				System.out.println("SCHEME_PATH_DESELECTED");
				SchemePath[] sps = (SchemePath[] )sne.getSource();
				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					for(int i = 0; i < sps.length; i++)
						if(mappath.PATH_ID.equals(sps[i].getId()))
							mappath.deselect();
				}
			}

			if(sne.SCHEME_CABLE_LINK_DESELECTED)
			{
//				System.out.println("SCHEME_CABLE_LINK_DESELECTED");
				SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
				for(Iterator it = lnl().getMapContext().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					for(int i = 0; i < scs.length; i++)
						if(link.LINK_ID.equals(scs[i].getId()))
							link.deselect();
				}
			}

			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
		if(ae.getActionCommand().equals(CatalogNavigateEvent.type))
			if(lnl().perform_processing)
		{
			CatalogNavigateEvent cne = (CatalogNavigateEvent )ae;

			if(cne.CATALOG_EQUIPMENT_SELECTED)
			{
//				System.out.println("CATALOG_EQUIPMENT_SELECTED");
			}

			if(cne.CATALOG_PATH_SELECTED)
			{
//				System.out.println("CATALOG_PATH_SELECTED");
				TransmissionPath[] tps = (TransmissionPath[] )cne.getSource();
				for(Iterator it = lnl().getMapContext().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
							for(int i = 0; i < tps.length; i++)
								if(sp.path_id.equals(tps[i].getId()))
									mappath.select();
					}
				}
			}

			if(cne.CATALOG_CABLE_LINK_SELECTED)
			{
//				System.out.println("CATALOG_CABLE_LINK_SELECTED");
				CableLink[] cs = (CableLink[] )cne.getSource();
				for(Iterator it = lnl().getMapContext().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					if(link.LINK_ID != null && !link.LINK_ID.equals(""))
					{
						SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
						if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
							for(int i = 0; i < cs.length; i++)
								if(scl.cable_link_id.equals(cs[i].getId()))
									link.select();
					}
				}
			}
			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
		if(ae.getActionCommand().equals("placeElement"))
		{
			MapSchemeElementLabel el = (MapSchemeElementLabel )ae.getSource();
			if(el.sElement instanceof SchemeElement)
			{
				SchemeElement se = (SchemeElement )el.sElement;
//				lnl().placeElement(se, se.mpe, new SxDoublePoint(se.getLong(), se.getLat()));
			}
			else
			if(el.sElement instanceof SchemeCableLink)
			{
				SchemeCableLink scl = (SchemeCableLink )el.sElement;
				lnl().placeElement(scl, scl.mplpe);
			}
			else
			if(el.sElement instanceof SchemePath)
			{
				SchemePath sp = (SchemePath )el.sElement;
				lnl().placeElement(sp, sp.mtppe);
			}
			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
	}

	public MapPhysicalLinkElement findMapLinkByCableLink(String link_id)
	{
		for(Iterator it = lnl().getMapContext().getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
				if(scl != null)
					if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
						if(scl.cable_link_id.equals(link_id))
							return link;
			}
		}
		return null;
	}

	public MapEquipmentNodeElement findMapElementByCableLink(String link_id)
	{
		SchemeElement se = null;
		Scheme sc = (Scheme )Pool.get(Scheme.typ, this.getMapContext().scheme_id);
		Hashtable ht = Pool.getHash(SchemeCableLink.typ);
		if(ht != null)
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				SchemeCableLink scl = (SchemeCableLink )enum.nextElement();
				if(scl != null)
					if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
						if(scl.cable_link_id.equals(link_id))
						{
							se = sc.getSchemeElementByCablePort(scl.source_port_id);
							se = sc.getTopologicalElement(se);
//							se = sc.getTopLevelElement(se);
							break;
						}
			}
		if(se == null)
		{
			Hashtable ht2 = Pool.getHash(SchemeLink.typ);
			if(ht2 != null)
				for(Enumeration enum = ht2.elements(); enum.hasMoreElements();)
				{
					SchemeLink sl = (SchemeLink )enum.nextElement();
					if(sl != null)
						if(sl.link_id != null && !sl.link_id.equals(""))
							if(sl.link_id.equals(link_id))
							{
								se = sc.getSchemeElementByPort(sl.source_port_id);
								se = sc.getTopLevelElement(se);
								break;
							}
				}
		}
		if(se != null)
		{
			for(Iterator it = lnl().getMapContext().getMapEquipmentNodeElements().iterator(); it.hasNext();)
			{
				MapEquipmentNodeElement node = (MapEquipmentNodeElement )it.next();
				if(node.element_id != null && node.element_id.equals(se.getId()))
					return node;
			}
		}
		return null;
	}

	public LogicalNetLayer lnl()
	{
		return myMapViewer.lnl;
	}

//Установка MapContext
	public void setMapContext( MapContext myMapContext)
	{
		if(animateThread != null)
			animateThread.stop_running();
		if(aContext.getDispatcher() != null)
			if(myMapContext != null)
			{
//				System.out.println("notify at " + aContext.getDispatcher());
				aContext.getDispatcher().notify(
						new OperationEvent(myMapContext, 0, "mapselectevent"));
			}
			else
			{
				aContext.getDispatcher().notify(
						new OperationEvent(this, 0, "mapdeselectevent"));
			}

		if(myMapContext == null)
		{
			lnl().setMapContext( null);
			elementPaneToolBar.setEnableDisablePanel(false);
			mapToolBar.setEnableDisablePanel(false);
		}
		else
		{
			lnl().setEnabled(true);
			elementPaneToolBar.setEnableDisablePanel(true);
			mapToolBar.setEnableDisablePanel(true);

			myMapContext.createFromPool( myMapViewer.lnl);
			lnl().viewer.setCenter(
					myMapContext.longitude,
					myMapContext.latitude);
			lnl().setMapContext( myMapContext);

//	mapPanel.myMapViewer.lnl.getMapContext().zoom(myMapContext.zoomFactor);
			myMapScrollPane.upDateScroll();

			Iterator e = myMapContext.getAllElements().iterator();
			while (e.hasNext())
			{
				MapElement mapElement = (MapElement)e.next();
				mapElement.setMapContext( myMapContext);
			}

			e = myMapContext.getNodeLinks().iterator();
			while (e.hasNext())
			{
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
				nodeLink.finalUpdate();
			}

			if(aContext != null)
				if(aContext.getApplicationModel() != null)
					if (aContext.getApplicationModel().isEnabled("mapActionIndication"))
				{
					animateThread = new AnimateThread(lnl());
					animateThread.start();
				}
		}

		lnl().postDirtyEvent();
		lnl().postPaintEvent();
	}

	public MapContext getMapContext()
	{
		return lnl().getMapContext();
	}

	void this_componentShown(ComponentEvent e)
	{
//		init_module();
	}

	void this_internalFrameActivated(InternalFrameEvent e)
	{
		this.grabFocus();

		if(aContext.getDispatcher() != null)
			if(getMapContext() != null)
				aContext.getDispatcher().notify(new OperationEvent(getMapContext(), 0, "mapselectevent"));

	}

	void this_internalFrameClosed(InternalFrameEvent e)
	{
		aContext.getDispatcher().notify(new OperationEvent(this, 0, "mapcloseevent"));
		closeMap();
	}

	void this_internalFrameDeactivated(InternalFrameEvent e)
	{
	}

	void this_internalFrameOpened(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_componentHidden(ComponentEvent e)
	{
		aContext.getDispatcher().notify(new OperationEvent(this, 0, "mapcloseevent"));
		closeMap();
	}

	private void this_componentRemoved(ContainerEvent e)
	{
		closeMap();
		aContext.getDispatcher().notify(new OperationEvent(this, 0, "mapcloseevent"));
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
}

class MapMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	MapMainFrame adaptee;

	MapMain_this_componentAdapter(MapMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}

	public void componentHidden(ComponentEvent e)
	{
		adaptee.this_componentHidden(e);
	}
}

class MapMainFrame_this_internalFrameAdapter extends javax.swing.event.InternalFrameAdapter
{
	MapMainFrame adaptee;

	MapMainFrame_this_internalFrameAdapter(MapMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}
	public void internalFrameActivated(InternalFrameEvent e)
	{
		adaptee.this_internalFrameActivated(e);
	}
	public void internalFrameClosed(InternalFrameEvent e)
	{
		adaptee.this_internalFrameClosed(e);
	}
	public void internalFrameDeactivated(InternalFrameEvent e)
	{
		adaptee.this_internalFrameDeactivated(e);
	}
	public void internalFrameOpened(InternalFrameEvent e)
	{
		adaptee.this_internalFrameOpened(e);
	}
}

class MapMainFrame_this_KeyAdapter extends java.awt.event.KeyAdapter
{
	MapMainFrame adaptee;

	MapMainFrame_this_KeyAdapter(MapMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	//Обработка событий от клавиатуры
	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();

		//Устанавливаем флаги состояний
		if (ke.isAltDown())
		{
			adaptee.lnl().setActionMode(LogicalNetLayer.ALT_LINK_ACTION_MODE);
		}

		if (ke.isShiftDown() && ke.isControlDown())
		{
			adaptee.lnl().setActionMode(LogicalNetLayer.FIXDIST_ACTION_MODE);
		}
		else
		{
			if (ke.isShiftDown())
			{
				adaptee.lnl().setActionMode(LogicalNetLayer.SELECT_ACTION_MODE);
			}
			if(ke.isControlDown())
			{
				adaptee.lnl().setActionMode(LogicalNetLayer.MOVE_ACTION_MODE);
			}
		}

		if (code == KeyEvent.VK_DELETE)
		{
			adaptee.lnl().delete();
		}
	}

	public void keyReleased(KeyEvent ke)
	{
		adaptee.lnl().setActionMode(LogicalNetLayer.NULL_ACTION_MODE);
	}

	public void keyTyped(KeyEvent ke)
	{
	}
}