package com.syrus.AMFICOM.Client.Prediction;

import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Analysis.*;
import com.syrus.AMFICOM.Client.General.Command.Prediction.*;
import com.syrus.AMFICOM.Client.General.Command.Prediction.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.io.BellcoreStructure;

public class PredictionMDIMain extends JFrame implements OperationListener
{
//Temporary Levchenko' data
	double delta_x;

// Levchenko Alexandre GUI;
	TimeDependenceFrame timeDependenceFrame;
	TimeDependenceTable timeDependenceTable;

	TraceSelectorFrame selectFrame;
	PrimaryParametersFrame paramFrame;
	OverallStatsFrame statsFrame;
	EventsFrame eventsFrame;
	MarkersInfoFrame mInfoFrame;
	AnalysisFrame analysisFrame;
	DetailedEventsFrame detailedEvFrame;
	HistogrammFrame histoFrame;

// Levchenko Alexandre math;
	ReflectoEventStatistics reflectoEventStatistics;

	ArrayList tables = new ArrayList();
	ArrayList graphs = new ArrayList();

	private Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	public JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	PredictionMenuBar menuBar = new  PredictionMenuBar();
	PredictionToolBar toolBar = new PredictionToolBar();

	String predictedModelId = new String();

	public PredictionMDIMain(ApplicationContext aContext)
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
		setContext(aContext);
	}

	public PredictionMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);
		setTitle(LangModelPrediction.getString("AppTitle"));
		addComponentListener(new StatisticsMDIMain_this_componentAdapter(this));
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

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

		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(toolBar, BorderLayout.NORTH);
		setJMenuBar(menuBar);

		mInfoFrame = new MarkersInfoFrame(internal_dispatcher);
		desktopPane.add(mInfoFrame);

		selectFrame = new TraceSelectorFrame(internal_dispatcher);
		desktopPane.add(selectFrame);

		paramFrame = new PrimaryParametersFrame(internal_dispatcher);
		desktopPane.add(paramFrame);
		tables.add(paramFrame);

		statsFrame = new OverallStatsFrame(internal_dispatcher);
		desktopPane.add(statsFrame);
		tables.add(statsFrame);

		detailedEvFrame = new DetailedEventsFrame(internal_dispatcher);
		desktopPane.add(detailedEvFrame);

		analysisFrame = new AnalysisFrame(internal_dispatcher);
		analysisFrame.updScales();
		desktopPane.add(analysisFrame);
		graphs.add(analysisFrame);

		eventsFrame = new EventsFrame(internal_dispatcher);
		desktopPane.add(eventsFrame);
		tables.add(eventsFrame);

		timeDependenceFrame = new TimeDependenceFrame(internal_dispatcher);
		desktopPane.add(timeDependenceFrame);
		graphs.add(timeDependenceFrame);

		timeDependenceTable = new TimeDependenceTable(internal_dispatcher);
		desktopPane.add(timeDependenceTable, null);
		tables.add(timeDependenceTable);

		histoFrame = new HistogrammFrame(internal_dispatcher);
		desktopPane.add(histoFrame, null);
		graphs.add(histoFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);

		setSize(frameSize);
		setLocation(0, 0);
	}

	public void setDomainSelected()
	{
//		DataSourceInterface dataSource = aContext.getDataSourceInterface();
//		new SurveyDataSourceImage(dataSource).GetTests();
//
//		Hashtable tests = Pool.getHash(Test.typ);
//		if (tests != null)
//			for(Enumeration enum = tests.elements(); enum.hasMoreElements();)
//			{
//				Test test = (Test )enum.nextElement();
//				dataSource.GetTestResult(test.getId());
//			}
//

			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuViewDataLoad", true);
			aModel.fireModelChanged("");

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			statusBar.setText("domain", domain.getName());
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 100);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("domain", 250);
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

		internal_dispatcher.register(this, RefChangeEvent.typ);
		internal_dispatcher.register(this, "contextchange");
		Environment.getDispatcher().register(this, "contextchange");

		aModel.setCommand("menuSessionOpen", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewDataLoad", new LoadTraceFromDatabaseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuViewCountPrediction", new CountPredictedReflectogramm(aContext, internal_dispatcher));
		aModel.setCommand("menuViewSavePrediction",  new SavePredictionCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(internal_dispatcher, null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(internal_dispatcher, aContext));

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(aContext);
		for (Iterator it = tables.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		for (Iterator it = graphs.iterator(); it.hasNext();)
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		rc.setParameter(CreateAnalysisReportCommand.TYPE, ReportTemplate.rtt_Prediction);
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
		aModel.addListener(menuBar);
		menuBar.setModel(aModel);

		aModel.addListener(toolBar);
		toolBar.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ)) //Setting of the data
		{
			ApplicationModel aModel = aContext.getApplicationModel();
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
					reflectoEventStatistics = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
					if(bs == null)
						return;

					//delta_x = bs.getDeltaX();
					//double[] y = bs.getTraceData();

					timeDependenceFrame.setTrace("primarytrace");
					timeDependenceFrame.setVisible(true);

					analysisFrame.setVisible(true);
					eventsFrame.setVisible(true);
					mInfoFrame.setVisible(true);
					histoFrame.setVisible(true);
					timeDependenceTable.setVisible(true);

					aModel.setEnabled("menuTraceClose", true);
					aModel.setEnabled("menuViewCountPrediction", true);
					aModel.setEnabled("menuTraceAddCompare", true);
					aModel.setEnabled("menuReportCreate", true);
					aModel.fireModelChanged("");
				}
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				analysisFrame.removeGraph(id);
				if (Pool.getMap("bellcorestructure") == null)
				{
					aModel.setEnabled("menuViewCountPrediction", false);
					aModel.setEnabled("menuViewSavePrediction", false);
					aModel.setEnabled("menuTraceClose", false);
					aModel.setEnabled("menuTraceAddCompare", false);
					aModel.setEnabled("menuTraceRemoveCompare", false);
					aModel.setEnabled("menuReportCreate", false);
					aModel.fireModelChanged("");

					timeDependenceFrame.setVisible(false);
					timeDependenceTable.setVisible(false);
					selectFrame.setVisible(false);
					paramFrame.setVisible(false);
					statsFrame.setVisible(false);
					eventsFrame.setVisible(false);
					mInfoFrame.setVisible(false);
					analysisFrame.setVisible(false);
					detailedEvFrame.setVisible(false);
					histoFrame.setVisible(false);
				}
				else
				{
					Iterator it = Pool.getMap("bellcorestructure").keySet().iterator();
					aModel.setEnabled("menuViewSavePrediction", false);
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
			}
			if(rce.SELECT)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					aModel.setEnabled("menuTraceRemoveCompare", false);
					aModel.setEnabled("menuViewSavePrediction", false);
					aModel.fireModelChanged("");
				}
				else
				{
					aModel.setEnabled("menuTraceRemoveCompare", true);

					if(Pool.get("predictionTime", id) != null)
						aModel.setEnabled("menuViewSavePrediction", true);
					else
						aModel.setEnabled("menuViewSavePrediction", false);

					predictedModelId = id;
					Pool.put("predictedModel", "id", predictedModelId);
					aModel.fireModelChanged("");
					setActiveRefId(id);
				}
			}
		}

		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + getTitle());
			ApplicationModel aModel = aContext.getApplicationModel();
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
			}
		}
	}

	void setActiveRefId (String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
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
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
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
		Checker checker = new Checker(aContext.getDataSource());
		if(!checker.checkCommand(checker.enterExtendedAnalysisModul))
		{
			JOptionPane.showMessageDialog(this, "Недостаточно прав для работы с модулем прогнозирования.", "Ошибка", JOptionPane.OK_OPTION);
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
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuSessionOpen", false);
		aModel.fireModelChanged("");

		setDomainSelected();
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setAllItemsAccessible(false);

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionOpen", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);


		aModel.setEnabled("menuSessionOpen", true);



		aModel.fireModelChanged("");
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		int w = desktopPane.getSize().width;
		int h = desktopPane.getSize().height;
		int minh = Math.min(205, h / 4);

		paramFrame.setSize(w/6, minh);
		mInfoFrame.setSize(w/6, minh);
		selectFrame.setSize(w/6, minh);
		statsFrame.setSize(w/6, minh);
		timeDependenceTable.setSize(w/3, minh);
		analysisFrame.setSize(w/2, h - 2 * minh);
		timeDependenceFrame.setSize(w/2, h - 2 * minh);
		eventsFrame.setSize(w/2, minh);
		detailedEvFrame.setSize(w/6, minh);
		histoFrame.setSize(w/3, minh);

		paramFrame.setLocation(w/6, 0);
		mInfoFrame.setLocation(w/2, 0);
		selectFrame.setLocation(0, 0);
		statsFrame.setLocation(w/3, 0);
		timeDependenceTable.setLocation(2*w/3, 0);
		analysisFrame.setLocation(0, minh);
		timeDependenceFrame.setLocation(w/2, minh);
		eventsFrame.setLocation(0, h - minh);
		detailedEvFrame.setLocation(w/2, h - minh);
		histoFrame.setLocation(2*w/3, h - minh);
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
			internal_dispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}

class StatisticsMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	PredictionMDIMain adaptee;

	StatisticsMDIMain_this_componentAdapter(PredictionMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}
