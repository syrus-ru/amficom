package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.*;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.general.*;
import com.syrus.io.BellcoreStructure;

public class AnalyseMainFrameSimplified extends JFrame
implements bsHashChangeListener, OperationListener, EtalonMTMListener, CurrentTraceChangeListener
{
	public static final boolean DEBUG = System.getProperty("amficom.debug.nonstrict", "false").equals("true");

	public ApplicationContext aContext;
	private Dispatcher internal_dispatcher = new Dispatcher();

	ClientAnalysisManager aManager = new ClientAnalysisManager();
	JPanel mainPanel = new JPanel();
	AnalyseMainToolBar toolBar = new AnalyseMainToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	AnalyseMainMenuBar menuBar = new AnalyseMainMenuBar();

	TraceSelectorFrame selectFrame;
	PrimaryParametersFrame paramFrame;
	OverallStatsFrame statsFrame;
	EventsFrame eventsFrame;
	AnalysisFrame analysisFrame;
	MarkersInfoFrame mInfoFrame;
	AnalysisSelectionFrame anaSelectFrame;

	ArrayList tables = new ArrayList();
	ArrayList graphs = new ArrayList();

	public AnalyseMainFrameSimplified(ApplicationContext aContext)
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

	public AnalyseMainFrameSimplified()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		this.addComponentListener(new AnalyseMainFrameSimplified_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		setContentPane(mainPanel);
		setResizable(true);
		setTitle(LangModelAnalyse.getString("AnalyseTitle"));
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

		eventsFrame = new EventsFrame(internal_dispatcher);
		desktopPane.add(eventsFrame);
		tables.add(eventsFrame);

		analysisFrame = new AnalysisFrame(internal_dispatcher);
		desktopPane.add(analysisFrame);
		graphs.add(analysisFrame);

		mInfoFrame = new MarkersInfoFrame(internal_dispatcher);
		desktopPane.add(mInfoFrame);

		anaSelectFrame = new AnalysisSelectionFrame(aContext);
		desktopPane.add(anaSelectFrame);
		tables.add(anaSelectFrame);

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
		internal_dispatcher.register(this, "contextchange");
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		Environment.getDispatcher().register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileAddCompare",new FileAddCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));

		aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(aContext));
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
		rc.setParameter(CreateAnalysisReportCommand.TYPE, ReportTemplate.rtt_Analysis);
		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new AnalysisWindowArranger(this)));
		aModel.setCommand("menuWindowTraceSelector", new ShowFrameCommand(desktopPane, selectFrame));
		aModel.setCommand("menuWindowPrimaryParameters", new ShowFrameCommand(desktopPane, paramFrame));
		aModel.setCommand("menuWindowOverallStats", new ShowFrameCommand(desktopPane, statsFrame));
		aModel.setCommand("menuWindowEvents", new ShowFrameCommand(desktopPane, eventsFrame));
		aModel.setCommand("menuWindowAnalysis", new ShowFrameCommand(desktopPane, analysisFrame));
		aModel.setCommand("menuWindowMarkersInfo", new ShowFrameCommand(desktopPane, mInfoFrame));
		aModel.setCommand("menuWindowAnalysisSelection", new ShowFrameCommand(desktopPane, anaSelectFrame));

		setDefaultModel(aModel);
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

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuFile", true);
		aModel.setEnabled("menuTrace", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setEnabled("menuFileOpen", AnalyseMainFrameSimplified.DEBUG); // XXX: saa: security bypass

		aModel.setVisible("menuTestSetup", false);
		aModel.setVisible("menuNetStudy", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);
		aModel.setVisible("menuWindowNoiseFrame", false);
		aModel.setVisible("menuWindowFilteredFrame", false);
		aModel.setVisible("menuWindowDetailedEvents", false);
		aModel.setVisible("menuWindowDerivHistoFrame", false);
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
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					setSessionOpened();
					statusBar.setText("status", LangModel.getString("statusReady"));
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
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
					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));
					setConnectionClosed();
				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
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

	public void setSessionOpened()
	{
		Checker checker = new Checker(aContext.getDataSource());
		if(!checker.checkCommand(Checker.enterAnalysisModul))
		{
			JOptionPane.showMessageDialog(this, "������������ ���� ��� ������ � ������� �������.", "������", JOptionPane.OK_OPTION);
			return;
		}
//		DataSourceInterface dataSource = aContext.getDataSourceInterface();
//		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
//		new SurveyDataSourceImage(dataSource).LoadTestTypes();
//		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
//		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
//		new SurveyDataSourceImage(dataSource).LoadModelingTypes();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuSessionNew", false);
		aModel.fireModelChanged("");
		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		internal_dispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuTraceDownload", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.fireModelChanged("");

		try
		{
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			statusBar.setText("domain", domain.getName());
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
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
		aModel.setEnabled("menuHelpAbout", false);
		aModel.fireModelChanged("");

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();
		desktopPane.setPreferredSize(desktopPane.getSize());
		new AnalysisWindowArranger(this).arrange();

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
			ColorManager.saveIni();
			aManager.saveIni();
			internal_dispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener#bsHashAdded(java.lang.String, com.syrus.io.BellcoreStructure)
	 */
	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		String id = key;
		if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
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

			aModel.setEnabled("menuReportCreate", true);

			aModel.setEnabled("menuWindowArrange", true);
			aModel.setEnabled("menuWindowTraceSelector", true);
			aModel.setEnabled("menuWindowPrimaryParameters", true);
			aModel.setEnabled("menuWindowOverallStats", true);
			aModel.setEnabled("menuWindowEvents", true);
			aModel.setEnabled("menuWindowAnalysis", true);
			aModel.setEnabled("menuWindowMarkersInfo", true);
			aModel.setEnabled("menuWindowAnalysisSelection", true);

			aModel.fireModelChanged("");

			String name = Heap.getBSPrimaryTrace().title;
			setTitle(LangModelAnalyse.getString("AnalyseTitle") + ": " + name);
		}
		else if (id.equals(Heap.REFERENCE_TRACE_KEY))
		{
			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
			aModel.fireModelChanged(new String [] {"menuTraceReferenceMakeCurrent"});
		}
		else
		{
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.fireModelChanged(new String [] {"menuTraceRemoveCompare", "menuTraceRemoveCompare"});
	}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener#bsHashRemoved(java.lang.String)
	 */
	public void bsHashRemoved(String key)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		String id = key;
		if (id.equals(Heap.REFERENCE_TRACE_KEY))
		{
			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
			aModel.fireModelChanged(new String [] {"menuTraceReferenceMakeCurrent"});
		}
		Heap.updateCurrentTraceWhenBSRemoved();
		if (!Heap.hasSecondaryBS())
		{
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String [] {"menuTraceRemoveCompare", "menuFileRemoveCompare"});
		}
	}

	public void bsHashRemovedAll()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
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

		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowArrange", false);
		aModel.setEnabled("menuWindowTraceSelector", false);
		aModel.setEnabled("menuWindowPrimaryParameters", false);
		aModel.setEnabled("menuWindowOverallStats", false);
		aModel.setEnabled("menuWindowEvents", false);
		aModel.setEnabled("menuWindowAnalysis", false);
		aModel.setEnabled("menuWindowMarkersInfo", false);
		aModel.setEnabled("menuWindowAnalysisSelection", false);

		aModel.fireModelChanged("");
		setTitle(LangModelAnalyse.getString("AnalyseTitle"));
	}

	public void etalonMTMCUpdated()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", true);
		aModel.fireModelChanged(new String [] {"menuTraceCloseEtalon"});
	}

	public void etalonMTMRemoved()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", false);
		aModel.fireModelChanged(new String [] {"menuTraceCloseEtalon"});
	}

	public void currentTraceChanged(String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
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

class AnalysisWindowArranger extends WindowArranger
{
	public AnalysisWindowArranger(AnalyseMainFrameSimplified mainframe)
	{
		super(mainframe);
	}

	public void arrange()
	{
		AnalyseMainFrameSimplified f = (AnalyseMainFrameSimplified)mainframe;

		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

		normalize(f.paramFrame);
		normalize(f.selectFrame);
		normalize(f.statsFrame);
		normalize(f.mInfoFrame);
		normalize(f.analysisFrame);
		normalize(f.eventsFrame);
		normalize(f.anaSelectFrame);

		f.paramFrame.setSize(w/6, h / 4);
		f.statsFrame.setSize(w/6, h / 4);
		f.mInfoFrame.setSize(w/6, h / 4);
		f.selectFrame.setSize(w/6, h - f.mInfoFrame.getHeight() - f.statsFrame.getHeight() - f.paramFrame.getHeight());
		f.analysisFrame.setSize(5*w/6, 3 * h / 4);
		f.eventsFrame.setSize(w/2, h / 4);
		f.anaSelectFrame.setSize(w/3, h / 4);

		f.selectFrame.setLocation(5*w/6, 0);
		f.paramFrame.setLocation(5*w/6, f.selectFrame.getHeight());
		f.mInfoFrame.setLocation(5*w/6, f.selectFrame.getHeight()+ f.paramFrame.getHeight());
		f.statsFrame.setLocation(5*w/6, f.selectFrame.getHeight() + f.mInfoFrame.getHeight() + f.paramFrame.getHeight());
		f.analysisFrame.setLocation(0, 0);
		f.anaSelectFrame.setLocation(0, f.analysisFrame.getHeight());
		f.eventsFrame.setLocation(w/3, f.analysisFrame.getHeight());
	}
}

class AnalyseMainFrameSimplified_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	AnalyseMainFrameSimplified adaptee;

	AnalyseMainFrameSimplified_this_componentAdapter(AnalyseMainFrameSimplified adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}