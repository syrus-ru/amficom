
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Analysis.*;
import com.syrus.AMFICOM.Client.General.Command.Model.*;
import com.syrus.AMFICOM.Client.General.Command.Model.MapCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.io.BellcoreStructure;

public class ModelMDIMain extends JFrame implements OperationListener
{
	private Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	public JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	ModelMainToolBar toolBar = new ModelMainToolBar();
	ModelMenuBar menuBar = new ModelMenuBar();
	TransData transData;

	public RefModelParamsFrame paramsFrame;
	TraceSelectorFrame selectorFrame;
	PrimaryParametersFrame paramFrame;

	HashMap traces = new HashMap();
	ScalableFrame analysisFrame;
	MapMarkersLayeredPanel mmlp;
	MapMarkersPanel mmp;
	public MapElementsFrame mapElementsFrame;
	public MapPropertyFrame mapPropertyFrame;
	public MapFrame mapframe;

	PropsFrame propsFrame;
	ElementsListFrame elementsListFrame;
	SchemeTabbedPane schemeTab;
	SchemeViewerFrame schemeFrame;
	SchemeGraph graph;

//	public MapView mapView;
//	public GetMapElementStructure elementStructure;
	public Checker checker;

	ArrayList tables = new ArrayList();
	ArrayList graphs = new ArrayList();

	public ModelMDIMain(ApplicationContext aContext)
	{
		super();
		setContext(aContext);
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Environment.addWindow(this);
	}

	public ModelMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);
		this.setTitle(LangModelModel.getString("AppTitle"));
		this.addComponentListener(new ModelMDIMain_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		mainPanel.setLayout(new BorderLayout());
		desktopPane.setBackground(SystemColor.control.darker().darker());

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add("status");
		statusBar.add("server");
		statusBar.add("domain");
		statusBar.add("session");
		statusBar.add("user");
		statusBar.add("time");

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		this.setJMenuBar(menuBar);

		paramsFrame = new RefModelParamsFrame(aContext);
		desktopPane.add(paramsFrame);
		tables.add(paramsFrame);

		selectorFrame = new TraceSelectorFrame(internal_dispatcher);
		desktopPane.add(selectorFrame);

		paramFrame = new PrimaryParametersFrame(internal_dispatcher);
		desktopPane.add(paramFrame);
		tables.add(paramFrame);

		transData = new TransData ();
		desktopPane.add(transData);

		mmlp = new MapMarkersLayeredPanel(internal_dispatcher);
		analysisFrame = new ScalableFrame(mmlp)
		{
			public String getReportTitle()
			{
				return LangModelAnalyse.getString("analysisTitle");
			}
		};
		analysisFrame.setTitle(LangModelAnalyse.getString("analysisTitle"));
		graphs.add(analysisFrame);
		desktopPane.add(analysisFrame);
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 100);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("domain", 200);
		statusBar.setWidth("session", 200);
		statusBar.setWidth("user", 100);
		statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
		statusBar.setText("time", " ");
		statusBar.organize();

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.register(this, "mapopenevent");
		internal_dispatcher.register(this, "mapcloseevent");
		internal_dispatcher.register(this, "contextchange");
		internal_dispatcher.register(this, SchemeElementsEvent.type);
		internal_dispatcher.register(this, RefChangeEvent.typ);
		internal_dispatcher.register(this, "addschemeelementevent");
		internal_dispatcher.register(this, "addschemeevent");

		Environment.getDispatcher().register(this, "contextchange");


		aModel.setCommand("menuSessionOpen", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewMapOpen", new MapModelOpenCommand(internal_dispatcher, aContext, this));
		aModel.setCommand("menuViewMapEdit", new MapViewOpenCommand(desktopPane, mapframe, aContext));
		aModel.setCommand("menuViewMapClose", new MapCloseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuViewPerformModeling", new PerformModelingCommand(internal_dispatcher, this, aContext));
		aModel.setCommand("menuViewModelSave", new SaveModelingCommand(internal_dispatcher, aContext, "primarytrace"));
		aModel.setCommand("menuViewModelLoad", new LoadModelingCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuViewSchemeOpen", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuViewSchemeEdit", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuViewSchemeClose", new SchemeCloseCommand(aContext, graph));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileAddCompare",new FileAddCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));

		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(internal_dispatcher, aContext));

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(aContext);
		for (Iterator it = tables.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		for (Iterator it = graphs.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		rc.setParameter(CreateAnalysisReportCommand.TYPE, ReportTemplate.rtt_Modeling);
		aModel.setCommand("menuReportCreate", rc);

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuReport", true);

//    aModel.setEnabled("menuViewMapOpen", false);
//    aModel.setEnabled("menuViewMapEdit", false);
//    aModel.setEnabled("menuViewMapClose", false);
//    aModel.setEnabled("menuViewModelLoad", false);
//    aModel.setEnabled("menuViewModelSave", false);
//    aModel.setEnabled("menuViewSchemeOpen", false);
//    aModel.setEnabled("menuViewMapOpen", false);

		aModel.fireModelChanged("");

		if(ConnectionInterface.getInstance() != null)
		{
			if(ConnectionInterface.getInstance().isConnected())
				internal_dispatcher.notify(new ContextChangeEvent(
						ConnectionInterface.getInstance(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			if(aContext.getSessionInterface().isOpened())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(ConnectionInterface.getInstance()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}



	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		aContext.setDispatcher(internal_dispatcher);
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(ApplicationModel.getInstance());
		setModel(aContext.getApplicationModel());
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		aModel.fireModelChanged("");
	}

	void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);

		double delta_x = bs.getResolution();
		double[] y = bs.getTraceData();

		if (id.equals("primarytrace"))
		{
			if (mmp != null)
			{
				mmp.removeAllMarkers();
				mmp.init(y, delta_x);
			}
			else
			{
				mmp = new MapMarkersPanel(mmlp, internal_dispatcher, y, delta_x);
				analysisFrame.setGraph(mmp, true, "primarytrace");
			}
			com.syrus.AMFICOM.general.corba.Identifier path_id =
					(com.syrus.AMFICOM.general.corba.Identifier)Pool.get("activecontext", "activepathid");
			mmp.setSchemePathId(path_id);

			new InitialAnalysisCommand().execute();//1
			mmp.updEvents("primarytrace");
			analysisFrame.setTitle(bs.title);
		}
		else
		{
			SimpleGraphPanel p = new SimpleGraphPanel(y, delta_x);
			analysisFrame.addGraph(p, id);
			analysisFrame.updScales();
			traces.put(id, p);
		}
	}

	public void removeTrace (String id)
	{
		if (id.equals("all"))
		{
			mmlp.removeAllGraphPanels();
			traces = new HashMap();
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
			if (p != null)
			{
				mmlp.removeGraphPanel(p);
				traces.remove(id);
				mmlp.updScale2fit();
			}
		}
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		if (ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					aModel.setEnabled("menuFileSave", true);
					aModel.setEnabled("menuFileSaveAll", true);
					aModel.setEnabled("menuFileSaveAs", true);
					aModel.setEnabled("menuFileSaveAsText", true);
					aModel.setEnabled("menuFileClose", true);
					aModel.setEnabled("menuFileAddCompare", true);
					aModel.setEnabled("menuReportCreate", true);

					aModel.setEnabled("menuTraceClose", true);
					aModel.setEnabled("menuTraceAddCompare", true);
					aModel.setEnabled("menuViewModelSave", true);
					aModel.setEnabled("menuViewPerformModeling", true);
					updTraceFrames();

					aModel.fireModelChanged("");
				}
				else
				{
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.fireModelChanged("");
				}
				addTrace(id);
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				analysisFrame.removeGraph(id);
				if (Pool.getMap("bellcorestructure") == null)
				{
					aModel.setEnabled("menuFileSave", false);
					aModel.setEnabled("menuFileSaveAll", false);
					aModel.setEnabled("menuFileSaveAs", false);
					aModel.setEnabled("menuFileSaveAsText", false);
					aModel.setEnabled("menuFileClose", false);
					aModel.setEnabled("menuFileAddCompare", false);
					aModel.setEnabled("menuFileRemoveCompare", false);

					aModel.setEnabled("menuReportCreate", false);
					aModel.setEnabled("menuViewModelSave", false);
//          aModel.setEnabled("menuViewPerformModeling", false);

					aModel.setEnabled("menuTraceClose", false);
					aModel.setEnabled("menuTraceAddCompare", false);
					aModel.setEnabled("menuFileRemoveCompare", false);
					aModel.setEnabled("menuTraceRemoveCompare", false);
//          aModel.setEnabled("menuTraceReference", false);
 //         aModel.setEnabled("menuTraceCurrent", false);
					aModel.fireModelChanged("");

					paramsFrame.setVisible(false);
					analysisFrame.setVisible(false);
					transData.setVisible(false);
					selectorFrame.setVisible(false);
					paramFrame.setVisible(false);
				}
				else
				{
					Iterator it = Pool.getMap("bellcorestructure").keySet().iterator();
					String nextId = (String)it.next();
					if (nextId.equals("primarytrace"))
					{
						if (!it.hasNext())
						{
							aModel.setEnabled("menuFileRemoveCompare", false);
							aModel.setEnabled("menuTraceRemoveCompare", false);
							aModel.fireModelChanged("");
						}
						else
							nextId = (String)it.next();
					}
					internal_dispatcher.notify(new RefChangeEvent(nextId, RefChangeEvent.SELECT_EVENT));
				}
				removeTrace(id);
			}
			if(rce.SELECT)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					aModel.setEnabled("menuFileRemoveCompare", false);
					aModel.setEnabled("menuTraceRemoveCompare", false);

					if( Pool.get("bellcorestructure", "primarytrace") != null &&
							((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace")).title.startsWith("Модель"))
						aModel.setEnabled("menuViewModelSave", true);
					else
						aModel.setEnabled("menuViewModelSave", false);

					aModel.fireModelChanged("");
				}
				else
				{
					aModel.setEnabled("menuViewModelSave", false);

					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.fireModelChanged("");
					setActiveRefId(id);
				}
			}
		}
		if(ae.getActionCommand().equals("mapopenevent"))
		{
			aModel.setEnabled("menuViewPerformModeling", true);

			Dimension dim = desktopPane.getSize();
			updTraceFrames();

			mapframe.setSize(4 * dim.width / 5, 3 * dim.height / 5);
			mapframe.setLocation(0, 2 * dim.height / 5);
			mapElementsFrame.toFront();
			mapPropertyFrame.toFront();
			mapframe.toFront();

			aModel.setEnabled("menuViewMapClose", true);
			aModel.setEnabled("menuViewMapEdit", true);
			aModel.setEnabled("menuViewModelLoad", true);
			aModel.fireModelChanged("");

			List schemes = (List)ae.getSource();
			paramsFrame.setModelingSchemes(schemes);
		}
		if(ae.getActionCommand().equals("mapcloseevent"))
		{

//			if (mapframe != null)
//				mapframe.setVisible(false);
//			if (mapPropertyFrame != null)
//				mapPropertyFrame.setVisible(false);
//			if (mapElementsFrame != null)
//				mapElementsFrame.setVisible(false);
			for(int i = 0; i < desktopPane.getComponents().length; i++)
			{
							Component comp = desktopPane.getComponent(i);
							if (comp instanceof MapFrame)
							{
											((MapFrame)comp).setVisible(false);
//											((MapMainFrame)comp).dispose();
											((MapFrame)comp).setMapView(null);
											((MapFrame)comp).setContext(null);
							}
							else
							if (comp instanceof MapPropertyFrame)
											((MapPropertyFrame)comp).setVisible(false);
//												((MapPropertyFrame)comp).dispose();
							else
							if (comp instanceof MapElementsFrame)
											((MapElementsFrame)comp).setVisible(false);
//												((MapElementsFrame)comp).dispose();
			}

			aModel.setEnabled("menuViewMapClose", false);
			aModel.setEnabled("menuViewMapEdit", false);
			aModel.fireModelChanged("");
		}
		if(ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			aModel.setEnabled("menuViewPerformModeling", true);
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.OPEN_PRIMARY_SCHEME)
			{
				updTraceFrames();

				if (propsFrame == null)
				{
					propsFrame = new PropsFrame(aContext, true);
					propsFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
				}
				desktopPane.add(propsFrame);

				if (elementsListFrame == null)
				{
					elementsListFrame = new ElementsListFrame(aContext, false);
					elementsListFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
				}
				desktopPane.add(elementsListFrame);

				//panel.operationPerformed(ae);

				if (schemeFrame == null)
				{
					schemeTab = new SchemeTabbedPane(aContext);
					schemeFrame = new SchemeViewerFrame(aContext, schemeTab)
					{
						protected void fireInternalFrameEvent(int id)
						{
							if (id == InternalFrameEvent.INTERNAL_FRAME_CLOSING)
							{
								elementsListFrame.setVisible(false);
								propsFrame.setVisible(false);
//								elementsListFrame.dispose();
//								propsFrame.dispose();
								super.fireInternalFrameEvent(id);
							}
						}
					};
					schemeFrame.setResizable(true);
					schemeFrame.setClosable(true);
					schemeFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
					schemeFrame.setMaximizable(true);
					schemeFrame.setIconifiable(true);
					schemeFrame.setTitle(LangModelModel.getString("elementsMainTitle"));
				}
				desktopPane.add(schemeFrame);

				//graph = panel.getGraph();

				Dimension dim = desktopPane.getSize();

				propsFrame.setVisible(true);
				elementsListFrame.setVisible(true);
				schemeFrame.setVisible(true);

				propsFrame.toFront();
				elementsListFrame.toFront();
				schemeFrame.toFront();

				propsFrame.setSize(dim.width / 5, 3 * dim.height / 10);
				propsFrame.setLocation(4 * dim.width / 5, 2  * dim.height / 5);
				elementsListFrame.setSize(dim.width / 5, 3 * dim.height / 10);
				elementsListFrame.setLocation(4 * dim.width / 5, 7 * dim.height / 10);
				schemeFrame.setSize(4 * dim.width / 5, 3 * dim.height / 5);
				schemeFrame.setLocation(0, 2 * dim.height / 5);

				aModel.setEnabled("menuViewSchemeClose", true);
				aModel.setEnabled("menuViewSchemeEdit", true);
				aModel.setEnabled("menuViewModelLoad", true);
				aModel.fireModelChanged("");

				Scheme scheme = (Scheme)see.obj;
				paramsFrame.setModelingScheme(scheme);
			}
			if(see.CLOSE_SCHEME)
			{
				propsFrame.dispose();
				elementsListFrame.dispose();
				schemeFrame.dispose();

				aModel.setEnabled("menuViewSchemeClose", false);
				aModel.setEnabled("menuViewSchemeEdit", false);
				aModel.fireModelChanged("");
			}
		}
/**/
		if (ae.getActionCommand().equals("addschemeevent"))
		{
			com.syrus.AMFICOM.general.corba.Identifier scheme_id =
					(com.syrus.AMFICOM.general.corba.Identifier)ae.getSource();
			try {
				Scheme scheme = (Scheme)SchemeStorableObjectPool.getStorableObject(
								scheme_id, true);
				schemeTab.openScheme(scheme);
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		else if (ae.getActionCommand().equals("addschemeelementevent"))
		{
			com.syrus.AMFICOM.general.corba.Identifier se_id =
					(com.syrus.AMFICOM.general.corba.Identifier)ae.getSource();
			try {
				SchemeElement se = (SchemeElement)SchemeStorableObjectPool.getStorableObject(se_id, true);
				schemeTab.openSchemeElement(se);
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
/**/
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();

				if(aContext.getSessionInterface().equals(ssi))
				{
//					aContext.setSessionInterface(ssi);
//					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

					setSessionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", ConnectionInterface.getInstance().getServerName());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();
				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
				try
				{
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					statusBar.setText("domain", domain.getName());
				}
				catch(ApplicationException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	void updTraceFrames()
	{
		Dimension dim = desktopPane.getSize();

		int twoByoneFive = 2*dim.height/5;
		int half = dim.height/2;

		transData.setSize (dim.width / 5, (int)(twoByoneFive*.5));
		transData.setLocation(4 * dim.width / 5, 0);

		paramsFrame.setSize (dim.width / 5, (int)(twoByoneFive*.5));
		paramsFrame.setLocation(4 * dim.width / 5,  (int)(twoByoneFive*.5));

		analysisFrame.setSize (19 * dim.width / 30 , 2 * dim.height / 5);
		analysisFrame.setLocation(dim.width / 6, 0);

		selectorFrame.setSize (dim.width / 6 , (int)(twoByoneFive*.4));
		selectorFrame.setLocation(0, 0);

		paramFrame.setSize (dim.width / 6 , (int)(twoByoneFive*.6));
		paramFrame.setLocation(0, (int)(twoByoneFive*.4));

		paramsFrame.setVisible(true);
		analysisFrame.setVisible(true);
		transData.setVisible(true);
		selectorFrame.setVisible(true);
		paramFrame.setVisible(true);
	}

	void setActiveRefId (String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId", id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
	}

	public void setConnectionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.fireModelChanged("");
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionOpen", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}

	public void setSessionOpened()
	{
		this.checker = new Checker(aContext.getDataSource());
		if(!checker.checkCommand(checker.enterReflectoModelingWindow))
			return;

//		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
//		new SurveyDataSourceImage(dataSource).LoadTestTypes();
//		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
//		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
//		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
//		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

//		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
//		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
//		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
//		new MapDataSourceImage(dataSource).LoadProtoElements();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.setEnabled("menuSessionOpen", false);

		aModel.fireModelChanged("");

		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		internal_dispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	public void setDomainSelected()
	{
//		new SchemeDataSourceImage(dataSource).LoadSchemes();
//		new ConfigDataSourceImage(dataSource).LoadNet();
//		new ConfigDataSourceImage(dataSource).LoadISM();
//		new MapDataSourceImage(dataSource).loadMaps();

		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.setEnabled("menuViewMapOpen", true);
		aModel.setEnabled("menuViewSchemeOpen", true);

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuViewModelLoad", true);

		aModel.fireModelChanged("");
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setAllItemsEnabled(false);

		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.setEnabled("menuViewMapOpen", false);
		aModel.setEnabled("menuViewSchemeOpen", false);
		aModel.setEnabled("menuViewModelSave", false);
		aModel.setEnabled("menuViewPerformModeling", false);
		aModel.setEnabled("menuViewModelLoad", false);
		aModel.setEnabled("menuFileOpen", false);
		aModel.setEnabled("menuFileOpen", false);

		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelpView", true);

		aModel.fireModelChanged("menuFileOpenAsBellcore");
		aModel.fireModelChanged("menuFileOpenAsWavetek");
		aModel.fireModelChanged("menuFileSaveAs");
		aModel.fireModelChanged("menuFileSave");
	}


	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();
		desktopPane.setPreferredSize(desktopPane.getSize());
	}

	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.getDispatcher().unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
			//ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			//SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));

			internal_dispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}

class ModelMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	ModelMDIMain adaptee;

	ModelMDIMain_this_componentAdapter(ModelMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}


