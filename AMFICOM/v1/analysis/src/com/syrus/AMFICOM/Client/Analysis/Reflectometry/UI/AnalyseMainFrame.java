package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.JRootPane;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AddTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileAddCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsBellcoreCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsWavetekCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAsTextCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadEtalonCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.NetStudyCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.OptionsSetColorsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.RemoveEtalonCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.TraceMakeCurrentCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.TraceOpenReferenceCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ArrangeWindowCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ShowFrameCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;

import com.syrus.AMFICOM.analysis.AnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.io.BellcoreStructure;

public class AnalyseMainFrame extends JFrame
		implements OperationListener
{
	public ApplicationContext aContext;
	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private Dispatcher internal_dispatcher = new Dispatcher();

	AnalysisManager aManager = new AnalysisManager();
	JPanel mainPanel = new JPanel();
	AnalyseMainToolBar toolBar = new AnalyseMainToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	AnalyseMainMenuBar menuBar = new AnalyseMainMenuBar();

	ScalableFrame noiseFrame;
	ScalableFrame filteredFrame;

	TraceSelectorFrame selectFrame;
	PrimaryParametersFrame paramFrame;
	OverallStatsFrame statsFrame;
	EventsFrame eventsFrame;
	MarkersInfoFrame mInfoFrame;
	AnalysisFrame analysisFrame;
	AnalysisSelectionFrame anaSelectFrame;
	DetailedEventsFrame detailedEvFrame;
	HistogrammFrame dhf;

	ArrayList tables = new ArrayList();
	ArrayList graphs = new ArrayList();

	public AnalyseMainFrame(ApplicationContext aContext)//ApplicationModel aModel)
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

	public AnalyseMainFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		this.addComponentListener(new AnalyseMainFrame_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		setContentPane(mainPanel);
		setResizable(true);
		setTitle(LangModelAnalyse.getString("AnalyseExtTitle"));
		setJMenuBar(menuBar);

		mainPanel.setLayout(new BorderLayout());
		desktopPane.setBackground(SystemColor.control.darker().darker());

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add("status");
		statusBar.add("server");
		statusBar.add("session");
		statusBar.add("user");
		statusBar.add("domain");
		statusBar.add("time");

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		selectFrame = new TraceSelectorFrame(internal_dispatcher);
		desktopPane.add(selectFrame);

		paramFrame = new PrimaryParametersFrame(internal_dispatcher);
		desktopPane.add(paramFrame);
		tables.add(paramFrame);

		statsFrame = new OverallStatsFrame(internal_dispatcher);
		desktopPane.add(statsFrame);
		tables.add(statsFrame);

		noiseFrame = new ScalableFrame(new ScalableLayeredPanel());
		noiseFrame.setTitle(LangModelAnalyse.getString("noiseTitle"));
		desktopPane.add(noiseFrame);
		graphs.add(noiseFrame);

		filteredFrame = new ScalableFrame(new ScalableLayeredPanel());
		filteredFrame.setTitle(LangModelAnalyse.getString("filteredTitle"));
		desktopPane.add(filteredFrame);
		graphs.add(filteredFrame);

//		ConcavitiesFrame concFrame = new ConcavitiesFrame(internal_dispatcher);
//		desktopPane.add(concFrame);

		eventsFrame = new EventsFrame(internal_dispatcher);
		desktopPane.add(eventsFrame);
		tables.add(eventsFrame);

		detailedEvFrame = new DetailedEventsFrame(internal_dispatcher);
		desktopPane.add(detailedEvFrame);

		analysisFrame = new AnalysisFrame(internal_dispatcher);
		desktopPane.add(analysisFrame);
		graphs.add(analysisFrame);

		mInfoFrame = new MarkersInfoFrame(internal_dispatcher);
		desktopPane.add(mInfoFrame);

		anaSelectFrame = new AnalysisSelectionFrame(aContext);
		desktopPane.add(anaSelectFrame);
		tables.add(anaSelectFrame);

		//dhf = new DerivHistoFrame(internal_dispatcher);
		dhf = new HistogrammFrame(internal_dispatcher);
		desktopPane.add(dhf);
		graphs.add(dhf);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);

		setSize(frameSize);
		setLocation(0, 0);
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 100);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("session", 200);
		statusBar.setWidth("user", 100);
		statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.register(this, RefChangeEvent.typ);
		internal_dispatcher.register(this, RefUpdateEvent.typ);
		internal_dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileAddCompare",new FileAddCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));

		aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(internal_dispatcher, aContext, "primarytrace"));
		aModel.setCommand("menuCreateTestSetup", new CreateTestSetupCommand(aContext, "primarytrace"));
		aModel.setCommand("menuLoadTestSetup", new LoadTestSetupCommand(aContext, "primarytrace"));
		aModel.setCommand("menuSaveTestSetup", new SaveTestSetupCommand(aContext, "primarytrace",
				SaveTestSetupCommand.ETALON + SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuSaveTestSetupAs", new SaveTestSetupAsCommand(aContext, "primarytrace",
				SaveTestSetupCommand.ETALON + SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuNetStudy", new NetStudyCommand());

		aModel.setCommand("menuTraceDownload", new LoadTraceFromDatabaseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand(aContext));
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand(aContext));
		aModel.setCommand("menuTraceReferenceSet", new TraceOpenReferenceCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceReferenceMakeCurrent", new TraceMakeCurrentCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(internal_dispatcher, aContext));

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(aContext);
		for (Iterator it = tables.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		for (Iterator it = graphs.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		rc.setParameter(CreateAnalysisReportCommand.TYPE, ReportTemplate.rtt_Survey);
		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new AnalysisExtWindowArranger(this)));
		aModel.setCommand("menuWindowTraceSelector", new ShowFrameCommand(desktopPane, selectFrame));
		aModel.setCommand("menuWindowPrimaryParameters", new ShowFrameCommand(desktopPane, paramFrame));
		aModel.setCommand("menuWindowOverallStats", new ShowFrameCommand(desktopPane, statsFrame));
		aModel.setCommand("menuWindowNoiseFrame", new ShowFrameCommand(desktopPane, noiseFrame));
		aModel.setCommand("menuWindowFilteredFrame", new ShowFrameCommand(desktopPane, filteredFrame));
		aModel.setCommand("menuWindowEvents", new ShowFrameCommand(desktopPane, eventsFrame));
		aModel.setCommand("menuWindowDetailedEvents", new ShowFrameCommand(desktopPane, detailedEvFrame));
		aModel.setCommand("menuWindowAnalysis", new ShowFrameCommand(desktopPane, analysisFrame));
		aModel.setCommand("menuWindowMarkersInfo", new ShowFrameCommand(desktopPane, mInfoFrame));
		aModel.setCommand("menuWindowAnalysisSelection", new ShowFrameCommand(desktopPane, anaSelectFrame));
		aModel.setCommand("menuWindowDerivHistoFrame", new ShowFrameCommand(desktopPane, dhf));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getConnectionInterface(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		else
		{
			aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//			new CheckConnectionCommand(internal_dispatcher, aContext).execute();
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
			if(aContext.getSessionInterface().isOpened())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuFile", true);
		aModel.setEnabled("menuTrace", true);
		aModel.setEnabled("menuTestSetup", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuAnalyseSaveCriteria", false);
		aModel.setVisible("menuSaveEtalon", false);
		aModel.setVisible("menuSaveThresholds", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		aContext.setDispatcher(internal_dispatcher);
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
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

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}
/*
	public void modelChanged(String[] params)
	{
		menuBar.setVisible(aModel.isVisible("menuBar"));
		toolBar.setVisible(aModel.isVisible("toolBar"));
		statusBar.setVisible(aModel.isVisible("statusBar"));
	}
*/
	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			ApplicationModel aModel = aContext.getApplicationModel();
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
//					aContext.setSessionInterface(ssi);
//					aContext.setDataSourceInterface(Environment.getDefaultDataSourceInterface(aContext.getSessionInterface()));
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

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
					aContext.setDataSourceInterface(null);

					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
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
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
			}
		}

		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			ApplicationModel aModel = aContext.getApplicationModel();
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
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

					aModel.setEnabled("menuTraceDownloadEtalon", true);
					aModel.setEnabled("menuTraceUpload", true);
					aModel.setEnabled("menuTraceClose", true);
					aModel.setEnabled("menuTraceCurrentMakeReference", true);
					aModel.setEnabled("menuTraceReference", true);
					aModel.setEnabled("menuTraceCurrent", true);
					aModel.setEnabled("menuTraceAddCompare", true);
					aModel.setEnabled("menuAnalyseUpload", true);

					aModel.setEnabled("menuCreateTestSetup", true);
					aModel.setEnabled("menuSaveTestSetup", true);
					aModel.setEnabled("menuSaveTestSetupAs", true);
					aModel.setEnabled("menuLoadTestSetup", true);
					aModel.setEnabled("menuAnalyseSaveCriteria", true);

					aModel.setEnabled("menuReportCreate", true);

					aModel.setEnabled("menuWindowArrange", true);
					aModel.setEnabled("menuWindowTraceSelector", true);
					aModel.setEnabled("menuWindowPrimaryParameters", true);
					aModel.setEnabled("menuWindowOverallStats", true);
					aModel.setEnabled("menuWindowNoiseFrame", true);
					aModel.setEnabled("menuWindowFilteredFrame", true);
					aModel.setEnabled("menuWindowEvents", true);
					aModel.setEnabled("menuWindowDetailedEvents", true);
					aModel.setEnabled("menuWindowAnalysis", true);
					aModel.setEnabled("menuWindowMarkersInfo", true);
					aModel.setEnabled("menuWindowAnalysisSelection", true);
					aModel.setEnabled("menuWindowDerivHistoFrame", true);

					aModel.fireModelChanged("");

					String name = ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace")).title;
					setTitle(LangModelAnalyse.getString("AnalyseExtTitle") + ": " + name);

					updFrames(id);
				}
				else if (id.equals("referencetrace"))
				{
					aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
					aModel.fireModelChanged(new String [] {"menuTraceReferenceMakeCurrent"});
				}
				else
				{
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.fireModelChanged("");
				}
			}
			if(rce.OPEN_ETALON)
			{
				aModel.setEnabled("menuTraceCloseEtalon", true);
				aModel.fireModelChanged("");
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (Pool.getHash("bellcorestructure") == null)
				{
					aModel.setEnabled("menuFileSave", false);
					aModel.setEnabled("menuFileSaveAll", false);
					aModel.setEnabled("menuFileSaveAs", false);
					aModel.setEnabled("menuFileSaveAsText", false);
					aModel.setEnabled("menuFileClose", false);
					aModel.setEnabled("menuFileAddCompare", false);
					aModel.setEnabled("menuFileRemoveCompare", false);

					aModel.setEnabled("menuTraceDownloadEtalon", false);
					aModel.setEnabled("menuTraceClose", false);
					aModel.setEnabled("menuTraceCloseEtalon", false);
					aModel.setEnabled("menuTraceCurrentMakeReference", false);
					aModel.setEnabled("menuTraceAddCompare", false);
					aModel.setEnabled("menuFileRemoveCompare", false);
					aModel.setEnabled("menuTraceRemoveCompare", false);
					aModel.setEnabled("menuTraceUpload", false);
					aModel.setEnabled("menuTraceReference", false);
					aModel.setEnabled("menuTraceCurrent", false);
					aModel.setEnabled("menuAnalyseUpload", false);

					aModel.setEnabled("menuCreateTestSetup", false);
					aModel.setEnabled("menuSaveTestSetup", false);
					aModel.setEnabled("menuSaveTestSetupAs", false);
					aModel.setEnabled("menuLoadTestSetup", false);
					aModel.setEnabled("menuAnalyseSaveCriteria", false);

					aModel.setEnabled("menuReportCreate", false);

					aModel.setEnabled("menuWindowArrange", false);
					aModel.setEnabled("menuWindowTraceSelector", false);
					aModel.setEnabled("menuWindowPrimaryParameters", false);
					aModel.setEnabled("menuWindowOverallStats", false);
					aModel.setEnabled("menuWindowNoiseFrame", false);
					aModel.setEnabled("menuWindowFilteredFrame", false);
					aModel.setEnabled("menuWindowEvents", false);
					aModel.setEnabled("menuWindowDetailedEvents", false);
					aModel.setEnabled("menuWindowAnalysis", false);
					aModel.setEnabled("menuWindowMarkersInfo", false);
					aModel.setEnabled("menuWindowAnalysisSelection", false);
					aModel.setEnabled("menuWindowDerivHistoFrame", false);
					aModel.setEnabled("menuWindowThresholdsSelection", false);
					aModel.setEnabled("menuWindowThresholds", false);

					aModel.fireModelChanged("");
					noiseFrame.setVisible(false);
					filteredFrame.setVisible(false);

					setTitle(LangModelAnalyse.getString("AnalyseExtTitle"));
				}
				else
				{
					if (id.equals("referencetrace"))
					{
						aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
						aModel.fireModelChanged(new String [] {"menuTraceReferenceMakeCurrent"});
					}
					Enumeration enum = Pool.getHash("bellcorestructure").keys();
					String nextId = (String)enum.nextElement();
					if (nextId.equals("primarytrace"))
					{
						if (!enum.hasMoreElements())
						{
							aModel.setEnabled("menuFileRemoveCompare", false);
							aModel.setEnabled("menuTraceRemoveCompare", false);
							aModel.fireModelChanged(new String [] {"menuTraceRemoveCompare"});
						}
						else
							nextId = (String)enum.nextElement();
					}
					internal_dispatcher.notify(new RefChangeEvent(nextId, RefChangeEvent.SELECT_EVENT));
				}
			}
			if(rce.CLOSE_ETALON)
			{
				aModel.setEnabled("menuTraceCloseEtalon", false);
				aModel.fireModelChanged(new String [] {"menuTraceCloseEtalon"});
			}
			if(rce.SELECT)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					aModel.setEnabled("menuFileRemoveCompare", false);
					aModel.setEnabled("menuTraceRemoveCompare", false);
					aModel.fireModelChanged(new String [] {"menuFileRemoveCompare", "menuTraceRemoveCompare"});
				}
				else
				{
					aModel.setEnabled("menuFileRemoveCompare", true);
					aModel.setEnabled("menuTraceRemoveCompare", true);
					aModel.fireModelChanged(new String [] {"menuFileRemoveCompare", "menuTraceRemoveCompare"});
					setActiveRefId(id);
				}
			}
		}

		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if(rue.ANALYSIS_PERFORMED)
			{
				String id = (String)(rue.getSource());
				if (id.equals("primarytrace"))
					updFrames(id);
			}
		}
	}

	public void setConnectionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuTraceDownload", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuNetStudy", true);
		aModel.fireModelChanged("");

		String domain_id = aContext.getSessionInterface().getDomainId();
		statusBar.setText("domain", Pool.getName("domain", domain_id));
	}

	public void setSessionOpened()
	{
		Checker checker = new Checker(aContext.getDataSourceInterface());
		if(!checker.checkCommand(checker.enterExtendedAnalysisModul))
		{
			JOptionPane.showMessageDialog(this, "Недостаточно прав для работы с модулем исследования.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		new SurveyDataSourceImage(dataSource).LoadModelingTypes();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionDomain");
		aModel.setEnabled("menuSessionNew", false);
		aModel.fireModelChanged("");
		String domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals(""))
			internal_dispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuSessionNew", true);

		aModel.setEnabled("menuFileOpen", false);
		aModel.setEnabled("menuFileOpenAs", false);
		aModel.setEnabled("menuFileOpenAsBellcore", false);
		aModel.setEnabled("menuFileOpenAsWavetek", false);
		aModel.setEnabled("menuFileAddCompare", false);
		aModel.setEnabled("menuTraceDownload", false);
		aModel.setEnabled("menuTraceAddCompare", false);
		aModel.setEnabled("menuTraceDownloadEtalon", false);

		aModel.setEnabled("menuAnalyseUpload", false);
		aModel.setEnabled("menuSaveEtalon", false);
		aModel.setEnabled("menuSaveThresholds", false);
		aModel.setEnabled("menuAnalyseSaveCriteria", false);
		aModel.setEnabled("menuSaveTestSetup", false);
		aModel.setEnabled("menuSaveTestSetupAs", false);
		aModel.setEnabled("menuNetStudy", false);
		aModel.setEnabled("menuHelpAbout", false);
		aModel.fireModelChanged("");

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	void updFrames(String id)
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		double delta_x = bs.getDeltaX();
//		double[] y = bs.getTraceData();

		double[] filtered = ((RefAnalysis)Pool.get("refanalysis", id)).filtered;
		double[] noise = ((RefAnalysis)Pool.get("refanalysis", id)).noise;
		//double[] normalyzed = ((RefAnalysis)Pool.get("refanalysis", id)).normalyzed;

		noiseFrame.setGraph(noise, delta_x, false, id);
		noiseFrame.updScales();
		noiseFrame.setVisible(true);
		filteredFrame.setGraph(filtered, delta_x, true, id);
		filteredFrame.updScales();
		filteredFrame.setVisible(true);
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		new AnalysisExtWindowArranger(this).arrange();
		analysisFrame.grabFocus();
	}

	void setActiveRefId (String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId", id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
	}

	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
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
			ColorManager.saveIni();
			aManager.saveIni();
			internal_dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}

class AnalysisExtWindowArranger extends WindowArranger
{
	public AnalysisExtWindowArranger(AnalyseMainFrame mainframe)
	{
		super(mainframe);
	}

	public void arrange()
	{
		AnalyseMainFrame f = (AnalyseMainFrame)mainframe;

		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;
		int minh = Math.min(205, h / 4);

		normalize(f.paramFrame);
		normalize(f.selectFrame);
		normalize(f.statsFrame);
		normalize(f.mInfoFrame);
		normalize(f.analysisFrame);
		normalize(f.eventsFrame);
		normalize(f.detailedEvFrame);
		normalize(f.anaSelectFrame);
		normalize(f.dhf);
		normalize(f.noiseFrame);
		normalize(f.filteredFrame);

		f.paramFrame.setSize(w/6, minh);
		f.selectFrame.setSize(w/6, minh);
		f.statsFrame.setSize(w/6, minh);
		f.mInfoFrame.setSize(w/6, minh);
		f.analysisFrame.setSize(2*w/3, h - 2 * minh);
		f.eventsFrame.setSize(w/2, minh);
		f.detailedEvFrame.setSize(w/6, minh);
		f.anaSelectFrame.setSize(w/3, minh);
		f.dhf.setSize(w/3, minh);
		f.noiseFrame.setSize(w/3, (h - 2 * minh)/2);
		f.filteredFrame.setSize(w/3, (h - 2 * minh)/2);

		f.paramFrame.setLocation(w/6, 0);
		f.selectFrame.setLocation(0, 0);
		f.statsFrame.setLocation(w/3, 0);
		f.mInfoFrame.setLocation(w/2, 0);
		f.analysisFrame.setLocation(0, minh);
		f.anaSelectFrame.setLocation(2*w/3, 0);
		f.filteredFrame.setLocation(2*w/3, minh);
		f.noiseFrame.setLocation(2*w/3, minh + f.filteredFrame.getHeight());
		f.eventsFrame.setLocation(0, minh + f.analysisFrame.getHeight());
		f.detailedEvFrame.setLocation(f.eventsFrame.getWidth(), minh + f.analysisFrame.getHeight());
		f.dhf.setLocation(f.eventsFrame.getWidth()+f.detailedEvFrame.getWidth(), minh + f.analysisFrame.getHeight());
	}
}

class AnalyseMainFrame_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	AnalyseMainFrame adaptee;

	AnalyseMainFrame_this_componentAdapter(AnalyseMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}
