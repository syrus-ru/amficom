
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Model;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.PrimaryParametersFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScalableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleGraphPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TraceSelectorFrame;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AddTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileAddCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsBellcoreCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsWavetekCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAsTextCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.InitialAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.LoadModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.MapCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.MapModelOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.PerformModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.SaveModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.Module;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeTabbedPane;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Schematics.Elements.ElementsListFrame;
import com.syrus.AMFICOM.Client.Schematics.Elements.PropsFrame;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.io.BellcoreStructure;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.InternalFrameEvent;

public class ModelMDIMain extends JFrame implements OperationListener, Module
{
	private Dispatcher internalDispatcher = new Dispatcher();

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
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Environment.addWindow(this);

		aContext.setDispatcher(internalDispatcher);
		setContext(aContext);
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
		this.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					thisComponentShown(e);
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

		statusBar.add(StatusBarModel.FIELD_STATUS);
		statusBar.add(StatusBarModel.FIELD_SERVER);
		statusBar.add(StatusBarModel.FIELD_SESSION);
		statusBar.add(StatusBarModel.FIELD_USER);
		statusBar.add(StatusBarModel.FIELD_DOMAIN);
		statusBar.add(StatusBarModel.FIELD_TIME);

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

		selectorFrame = new TraceSelectorFrame(internalDispatcher);
		desktopPane.add(selectorFrame);

		paramFrame = new PrimaryParametersFrame(internalDispatcher);
		desktopPane.add(paramFrame);
		tables.add(paramFrame);

		transData = new TransData ();
		desktopPane.add(transData);

		mmlp = new MapMarkersLayeredPanel(internalDispatcher);
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

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setEnabled("menuSession", false);
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionConnection", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuExit", false);

		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuViewMapViewOpen", false);
		aModel.setEnabled("menuViewMapViewEdit", false);
		aModel.setEnabled("menuViewMapViewClose", false);
		aModel.setEnabled("menuViewSchemeOpen", false);
		aModel.setEnabled("menuViewSchemeEdit", false);
		aModel.setEnabled("menuViewSchemeClose", false);
		aModel.setEnabled("menuViewPerformModeling", false);
		aModel.setEnabled("menuViewModelSave", false);
		aModel.setEnabled("menuViewModelLoad", false);

		aModel.setEnabled("menuFileOpen", false);
		aModel.setEnabled("menuFileOpenAs", false);
		aModel.setEnabled("menuFileOpenAsBellcore", false);
		aModel.setEnabled("menuFileOpenAsWavetek", false);
		aModel.setEnabled("menuFileSave", false);
		aModel.setEnabled("menuFileSaveAll", false);
		aModel.setEnabled("menuFileSaveAs", false);
		aModel.setEnabled("menuFileSaveAsText", false);
		aModel.setEnabled("menuFileClose", false);
		aModel.setEnabled("menuFileAddCompare", false);
		aModel.setEnabled("menuFileRemoveCompare", false);

		aModel.setEnabled("menuTrace", false);
		aModel.setEnabled("menuTraceAddCompare", false);
		aModel.setEnabled("menuTraceRemoveCompare", false);
		aModel.setEnabled("menuTraceClose", false);

		aModel.setEnabled("menuReport", false);
		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuHelp", false);
		aModel.setEnabled("menuHelpContents", false);
		aModel.setEnabled("menuHelpFind", false);
		aModel.setEnabled("menuHelpTips", false);
		aModel.setEnabled("menuHelpStart", false);
		aModel.setEnabled("menuHelpCourse", false);
		aModel.setEnabled("menuHelpHelp", false);
		aModel.setEnabled("menuHelpSupport", false);
		aModel.setEnabled("menuHelpLicense", false);
		aModel.setEnabled("menuHelpAbout", false);

		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuReport", true);
	}

	public void finalizeModule()
	{
		setContext(null);
		Environment.getDispatcher().unregister(this, ContextChangeEvent.type);
		statusBar.removeDispatcher(Environment.getDispatcher());
	}

	public void initModule()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth(StatusBarModel.FIELD_STATUS, 200);
		statusBar.setWidth(StatusBarModel.FIELD_SERVER, 250);
		statusBar.setWidth(StatusBarModel.FIELD_SESSION, 200);
		statusBar.setWidth(StatusBarModel.FIELD_USER, 100);
		statusBar.setWidth(StatusBarModel.FIELD_DOMAIN, 150);
		statusBar.setWidth(StatusBarModel.FIELD_TIME, 50);

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
		statusBar.setText(StatusBarModel.FIELD_TIME, " ");
		statusBar.organize();
		statusBar.addDispatcher(Environment.getDispatcher());
		statusBar.addDispatcher(internalDispatcher);

		Environment.getDispatcher().register(this, ContextChangeEvent.type);

		setDefaultModel(aModel);

		aModel.setCommand("menuSessionOpen", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewMapOpen", new MapModelOpenCommand(this.desktopPane, aContext));
		aModel.setCommand("menuViewMapEdit", new MapViewOpenCommand(desktopPane, aContext));
		aModel.setCommand("menuViewMapClose", new MapCloseCommand(internalDispatcher, aContext));
		aModel.setCommand("menuViewPerformModeling", new PerformModelingCommand(internalDispatcher, this, aContext));
		aModel.setCommand("menuViewModelSave", new SaveModelingCommand(internalDispatcher, aContext, "primarytrace"));
		aModel.setCommand("menuViewModelLoad", new LoadModelingCommand(internalDispatcher, aContext));
		aModel.setCommand("menuViewSchemeOpen", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuViewSchemeEdit", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuViewSchemeClose", new SchemeCloseCommand(aContext, graph));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileAddCompare",new FileAddCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(internalDispatcher, null, aContext));

		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(internalDispatcher, aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(internalDispatcher, null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(internalDispatcher, aContext));

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(aContext);
		for (Iterator it = tables.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		for (Iterator it = graphs.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		rc.setParameter(CreateAnalysisReportCommand.TYPE, ReportTemplate.rtt_Modeling);

		aModel.setCommand("menuReportCreate", rc);

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.fireModelChanged("");

		if(ConnectionInterface.getInstance() != null)
		{
			if(ConnectionInterface.getInstance().isConnected())
				internalDispatcher.notify(new ContextChangeEvent(
						ConnectionInterface.getInstance(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			if(aContext.getSessionInterface().isOpened())
				internalDispatcher.notify(new ContextChangeEvent(
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
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().unregister(this, ContextChangeEvent.type);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_FRAME_SHOWN);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CLOSED);
				this.aContext.getDispatcher().unregister(this, SchemeElementsEvent.type);
				this.aContext.getDispatcher().unregister(this, RefChangeEvent.typ);
				this.aContext.getDispatcher().unregister(this, "addschemeelementevent");
				this.aContext.getDispatcher().unregister(this, "addschemeevent");

				statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			this.aContext = aContext;
			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(ApplicationModel.getInstance());
			setModel(aContext.getApplicationModel());

			aContext.getDispatcher().register(this, ContextChangeEvent.type);
			aContext.getDispatcher().register(this, MapEvent.MAP_FRAME_SHOWN);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CLOSED);
			aContext.getDispatcher().register(this, SchemeElementsEvent.type);
			aContext.getDispatcher().register(this, RefChangeEvent.typ);
			aContext.getDispatcher().register(this, "addschemeelementevent");
			aContext.getDispatcher().register(this, "addschemeevent");
	
			statusBar.addDispatcher(this.aContext.getDispatcher());
		}
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

		aModel.fireModelChanged();
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
				mmp = new MapMarkersPanel(mmlp, internalDispatcher, y, delta_x);
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

					aModel.fireModelChanged();
				}
				else
				{
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.fireModelChanged();
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
					aModel.fireModelChanged();

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
							aModel.fireModelChanged();
						}
						else
							nextId = (String)it.next();
					}
					internalDispatcher.notify(new RefChangeEvent(nextId, RefChangeEvent.SELECT_EVENT));
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

					aModel.fireModelChanged();
				}
				else
				{
					aModel.setEnabled("menuViewModelSave", false);

					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.fireModelChanged();
					setActiveRefId(id);
				}
			}
		}
		if(ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			mapframe = (MapFrame)ae.getSource();
		}
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			aModel.setEnabled("menuViewPerformModeling", true);

			Dimension dim = desktopPane.getSize();
			updTraceFrames();

			mapframe.setSize(4 * dim.width / 5, 3 * dim.height / 5);
			mapframe.setLocation(0, 2 * dim.height / 5);
			mapElementsFrame.toFront();
			mapPropertyFrame.toFront();
			mapframe.toFront();

			aModel.setEnabled("menuViewMapViewClose", true);
			aModel.setEnabled("menuViewMapViewEdit", true);
			aModel.setEnabled("menuViewModelLoad", true);
			aModel.fireModelChanged();

			MapView mapView = (MapView )ae.getSource();
			List schemes = mapView.getSchemes();
			paramsFrame.setModelingSchemes(schemes);
		}
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for(int i = 0; i < desktopPane.getComponents().length; i++)
			{
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapFrame)
				{
					((MapFrame)comp).setVisible(false);
					((MapFrame)comp).setContext(null);
				}
				else if (comp instanceof MapPropertyFrame)
					((MapPropertyFrame)comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
					((MapElementsFrame)comp).setVisible(false);
			}

			aModel.setEnabled("menuViewMapViewClose", false);
			aModel.setEnabled("menuViewMapViewEdit", false);
			aModel.fireModelChanged();
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
				aModel.fireModelChanged();

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
				aModel.fireModelChanged();
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
		if(ae.getActionCommand().equals(ContextChangeEvent.type))
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

					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
					statusBar.setText(StatusBarModel.FIELD_SESSION, sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText(StatusBarModel.FIELD_USER, aContext.getSessionInterface().getUser());
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					setSessionClosed();

					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
					statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
					statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
				}
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
					statusBar.setText(StatusBarModel.FIELD_SERVER, ConnectionInterface.getInstance().getServerName());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
					statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));

					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusDisconnected"));
					statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));

					setConnectionClosed();
				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
					statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
				try
				{
					Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
					Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
							domainId, true);
					statusBar.setText(StatusBarModel.FIELD_DOMAIN, domain.getName());
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

		aModel.fireModelChanged();
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

		aModel.fireModelChanged();
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionOpen", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged();
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

		aModel.fireModelChanged();

		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		internalDispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
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

		aModel.setEnabled("menuViewMapViewOpen", true);
		aModel.setEnabled("menuViewSchemeOpen", true);

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuViewModelLoad", true);

		aModel.fireModelChanged();
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setAllItemsEnabled(false);

		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.setEnabled("menuViewMapViewOpen", false);
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
		return internalDispatcher;
	}

	void thisComponentShown(ComponentEvent e)
	{
		initModule();
		desktopPane.setPreferredSize(desktopPane.getSize());
	}

	void this_windowClosing(WindowEvent e)
	{
		internalDispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internalDispatcher.unregister(this, "contextchange");
		Environment.getDispatcher().unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			Command closeCommand = aContext.getApplicationModel().getCommand("menuExit");
			this.setContext(null);
			closeCommand.execute();

			return;
		}
		super.processWindowEvent(e);
	}
}


