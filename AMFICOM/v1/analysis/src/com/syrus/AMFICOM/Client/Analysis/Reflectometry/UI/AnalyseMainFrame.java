package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AddTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
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
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class AnalyseMainFrame extends JFrame implements BsHashChangeListener,
		PrimaryTraceListener, PrimaryMTMListener, OperationListener,
		EtalonMTMListener, CurrentTraceChangeListener
{
	public ApplicationContext aContext;

	Dispatcher internalDispatcher = new Dispatcher();

	ClientAnalysisManager aManager = new ClientAnalysisManager();

	JPanel mainPanel = new JPanel();

	AnalyseMainToolBar toolBar = new AnalyseMainToolBar();

	JScrollPane scrollPane = new JScrollPane();

	JViewport viewport = new JViewport();

	JDesktopPane desktopPane = new JDesktopPane();

	JPanel statusBarPanel = new JPanel();

	StatusBarModel statusBar = new StatusBarModel(0);

	AnalyseMainMenuBar menuBar = new AnalyseMainMenuBar();

	public static final String NOISE_FRAME = "noiseFrame";

	public static final String FILTERED_FRAME = "filteredFrame";

	public static final String SELECTOR_FRAME = "selectFrame";

	public static final String PRIMARY_PARAMETERS_FRAME = "paramFrame";

	public static final String STATS_FRAME = "statsFrame";

	public static final String EVENTS_FRAME = "eventsFrame";

	public static final String MARKERS_INFO_FRAME = "MarkersInfoFrame";

	public static final String ANALYSIS_FRAME = "analysisFrame";

	public static final String ANALYSIS_SELECTION_FRAME = "AnalysisSelectionFrame";

	public static final String DETAILED_EVENTS_FRAME = "DetailedEventsFrame";

	public static final String HISTOGRAMM_FRAME = "HistogrammFrame";
	
	public static final String WINDOW_ARRANGER = "analysisExtWindowArranger";
	
	UIDefaults frames;
	
	CreateAnalysisReportCommand analysisReportCommand;

	public AnalyseMainFrame(final ApplicationContext aContext)	{
		super();
		setContext(aContext);

		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				initModule();
				desktopPane.setPreferredSize(desktopPane.getSize());
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						arrange();
					}
				});

			}
		});
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				internalDispatcher.unregister(AnalyseMainFrame.this, "contextchange");
				Environment.getDispatcher().unregister(AnalyseMainFrame.this, "contextchange");
				aContext.getApplicationModel().getCommand("menuExit").execute();
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

		this.frames = new UIDefaults();
		this.frames.put(SELECTOR_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Log.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(internalDispatcher);
				desktopPane.add(selectFrame);
				return selectFrame;
			}
		});

		this.frames.put(PRIMARY_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | PRIMARY_PARAMETERS_FRAME", Log.FINEST);
				PrimaryParametersFrame paramFrame = new PrimaryParametersFrame(internalDispatcher);
				desktopPane.add(paramFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, paramFrame);
				return paramFrame;
			}
		});

		this.frames.put(STATS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | STATS_FRAME", Log.FINEST);
				OverallStatsFrame statsFrame = new OverallStatsFrame(internalDispatcher);
				desktopPane.add(statsFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, statsFrame);
				return statsFrame;
			}
		});

		this.frames.put(NOISE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | NOISE_FRAME", Log.FINEST);
				ScalableFrame noiseFrame = new ScalableFrame(new ScalableLayeredPanel());
				noiseFrame.setTitle(LangModelAnalyse.getString("Noise level"));
				desktopPane.add(noiseFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL, noiseFrame);
				return noiseFrame;
			}
		});

		this.frames.put(FILTERED_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | FILTERED_FRAME", Log.FINEST);
				ScalableFrame filteredFrame = new ScalableFrame(new ScalableLayeredPanel());
				filteredFrame.setTitle(LangModelAnalyse.getString("filteredTitle"));
				desktopPane.add(filteredFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL, filteredFrame);
				return filteredFrame;
			}
		});

		this.frames.put(WINDOW_ARRANGER, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | WINDOW_ARRANGER", Log.FINEST);
				return new WindowArranger(AnalyseMainFrame.this) {

					public void arrange() {
						AnalyseMainFrame f = (AnalyseMainFrame) mainframe;

						int w = f.desktopPane.getSize().width;
						int h = f.desktopPane.getSize().height;
						int minh = Math.min(205, h / 4);

						JInternalFrame selectFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.SELECTOR_FRAME);
						JInternalFrame paramFrame = (JInternalFrame) f.frames
								.get(AnalyseMainFrame.PRIMARY_PARAMETERS_FRAME);
						JInternalFrame statsFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.STATS_FRAME);
						JInternalFrame noiseFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.NOISE_FRAME);
						JInternalFrame filteredFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.FILTERED_FRAME);
						JInternalFrame eventsFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.EVENTS_FRAME);
						JInternalFrame detailedEvFrame = (JInternalFrame) f.frames
								.get(AnalyseMainFrame.DETAILED_EVENTS_FRAME);
						JInternalFrame analysisFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.ANALYSIS_FRAME);
						JInternalFrame mInfoFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.MARKERS_INFO_FRAME);
						JInternalFrame anaSelectFrame = (JInternalFrame) f.frames
								.get(AnalyseMainFrame.ANALYSIS_SELECTION_FRAME);
						JInternalFrame dhf = (JInternalFrame) f.frames.get(AnalyseMainFrame.HISTOGRAMM_FRAME);

						normalize(paramFrame);
						normalize(selectFrame);
						normalize(statsFrame);
						normalize(mInfoFrame);
						normalize(analysisFrame);
						normalize(eventsFrame);
						normalize(detailedEvFrame);
						normalize(anaSelectFrame);
						normalize(dhf);
						normalize(noiseFrame);
						normalize(filteredFrame);

						paramFrame.setSize(w / 6, minh);
						selectFrame.setSize(w / 6, minh);
						statsFrame.setSize(w / 6, minh);
						mInfoFrame.setSize(w / 6, minh);
						analysisFrame.setSize(2 * w / 3, h - 2 * minh);
						eventsFrame.setSize(w / 2, minh);
						detailedEvFrame.setSize(w / 6, minh);
						anaSelectFrame.setSize(w / 3, minh);
						dhf.setSize(w / 3, minh);
						noiseFrame.setSize(w / 3, (h - 2 * minh) / 2);
						filteredFrame.setSize(w / 3, (h - 2 * minh) / 2);

						paramFrame.setLocation(w / 6, 0);
						selectFrame.setLocation(0, 0);
						statsFrame.setLocation(w / 3, 0);
						mInfoFrame.setLocation(w / 2, 0);
						analysisFrame.setLocation(0, minh);
						anaSelectFrame.setLocation(2 * w / 3, 0);
						filteredFrame.setLocation(2 * w / 3, minh);
						noiseFrame.setLocation(2 * w / 3, minh + filteredFrame.getHeight());
						eventsFrame.setLocation(0, minh + analysisFrame.getHeight());
						detailedEvFrame.setLocation(eventsFrame.getWidth(), minh + analysisFrame.getHeight());
						dhf.setLocation(eventsFrame.getWidth() + detailedEvFrame.getWidth(), minh
								+ analysisFrame.getHeight());
					}

				};
			}
		});

		// ConcavitiesFrame concFrame = new
		// ConcavitiesFrame(internal_dispatcher);
		// desktopPane.add(concFrame);

		this.frames.put(EVENTS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EVENTS_FRAME", Log.FINEST);
				EventsFrame eventsFrame = new EventsFrame(internalDispatcher);
				desktopPane.add(eventsFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, eventsFrame);
				return eventsFrame;
			}
		});

		this.frames.put(DETAILED_EVENTS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | DETAILED_EVENTS_FRAME", Log.FINEST);
				DetailedEventsFrame detailedEvFrame = new DetailedEventsFrame(internalDispatcher);
				desktopPane.add(detailedEvFrame);
				return detailedEvFrame;
			}
		});

		this.frames.put(ANALYSIS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Log.FINEST);
				PathElementsFrame analysisFrame = new PathElementsFrame(aContext, internalDispatcher);
				desktopPane.add(analysisFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL, analysisFrame);
				return analysisFrame;
			}
		});

		this.frames.put(MARKERS_INFO_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | MARKERS_INFO_FRAME", Log.FINEST);
				MarkersInfoFrame mInfoFrame = new MarkersInfoFrame(internalDispatcher);
				desktopPane.add(mInfoFrame);
				return mInfoFrame;
			}
		});

		this.frames.put(ANALYSIS_SELECTION_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_SELECTION_FRAME", Log.FINEST);
				AnalysisSelectionFrame analysisSelectionFrame = new AnalysisSelectionFrame(aContext);
				desktopPane.add(analysisSelectionFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, analysisSelectionFrame);
				return analysisSelectionFrame;
			}
		});

		this.frames.put(HISTOGRAMM_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | HISTOGRAMM_FRAME", Log.FINEST);
				HistogrammFrame histogrammFrame = new HistogrammFrame(internalDispatcher);
				desktopPane.add(histogrammFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL, histogrammFrame);
				return histogrammFrame;
			}
		});

		// dhf = new DerivHistoFrame(internal_dispatcher);

		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = localGraphicsEnvironment.getMaximumWindowBounds();
		this.setSize(new Dimension(maximumWindowBounds.width, maximumWindowBounds.height));
		this.setLocation(maximumWindowBounds.x, maximumWindowBounds.y);

		Environment.addWindow(this);
	}

	public AnalyseMainFrame()
	{
		this(new ApplicationContext());
	}


	public void initModule()
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

		aContext.setDispatcher(internalDispatcher);
		internalDispatcher.register(this, RefUpdateEvent.typ);
		internalDispatcher.register(this, "contextchange");
		Heap.addBsHashListener(this);
		Heap.addPrimaryMTMListener(this);
		Heap.addPrimaryTraceListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		Environment.getDispatcher().register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(
				Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(
				Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(
				aContext));
		aModel.setCommand("menuSessionConnection",
			new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword",
			new SessionChangePasswordCommand(Environment.getDispatcher(),
					aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(
				Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuFileOpenAsBellcore",
			new FileOpenAsBellcoreCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileOpenAsWavetek",
			new FileOpenAsWavetekCommand(internalDispatcher, aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuFileAddCompare", new FileAddCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(
				internalDispatcher, null, aContext));

		aModel.setCommand("menuAnalyseUpload",
			new SaveAnalysisCommand(aContext));
		aModel.setCommand("menuCreateTestSetup", new CreateTestSetupCommand(
				aContext));
		aModel.setCommand("menuLoadTestSetup", new LoadTestSetupCommand(
				aContext));
		aModel.setCommand("menuSaveTestSetup", new SaveTestSetupCommand(
				aContext, SaveTestSetupCommand.ETALON
						+ SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuSaveTestSetupAs", new SaveTestSetupAsCommand(
				aContext, SaveTestSetupCommand.ETALON
						+ SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuNetStudy", new NetStudyCommand());

		aModel.setCommand("menuTraceDownload",
			new LoadTraceFromDatabaseCommand(internalDispatcher, aContext));
		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand());
		aModel.setCommand("menuTraceAddCompare",
			new AddTraceFromDatabaseCommand(internalDispatcher, aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(
				internalDispatcher, null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(
				internalDispatcher, aContext));
		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand(
				aContext));
		aModel.setCommand("menuTraceReferenceSet",
			new TraceOpenReferenceCommand(internalDispatcher, aContext));
		aModel.setCommand("menuTraceReferenceMakeCurrent",
			new TraceMakeCurrentCommand(internalDispatcher, aContext));
		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(
				internalDispatcher, aContext));

		this.analysisReportCommand = new CreateAnalysisReportCommand(
				aContext);			
		analysisReportCommand.setParameter(CreateAnalysisReportCommand.TYPE,
			ReportTemplate.rtt_Survey);
		aModel.setCommand("menuReportCreate", analysisReportCommand);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new WindowArranger(this) {

			private WindowArranger	windowArranger;

			public void arrange() {
				if (windowArranger == null)
					windowArranger = (WindowArranger) frames.get(WINDOW_ARRANGER);
				windowArranger.arrange();
			}
		}));
		
		aModel.setCommand("menuWindowTraceSelector", this.getLazyCommand(SELECTOR_FRAME));
		aModel.setCommand("menuWindowPrimaryParameters", this.getLazyCommand(PRIMARY_PARAMETERS_FRAME));
		aModel.setCommand("menuWindowOverallStats", this.getLazyCommand(STATS_FRAME));
		aModel.setCommand("menuWindowNoiseFrame", this.getLazyCommand(NOISE_FRAME));
		aModel.setCommand("menuWindowFilteredFrame", this.getLazyCommand(FILTERED_FRAME));
		aModel.setCommand("menuWindowEvents", this.getLazyCommand(EVENTS_FRAME));
		aModel.setCommand("menuWindowDetailedEvents", this.getLazyCommand(DETAILED_EVENTS_FRAME));
		aModel.setCommand("menuWindowAnalysis", this.getLazyCommand(ANALYSIS_FRAME));
		aModel.setCommand("menuWindowMarkersInfo", this.getLazyCommand(MARKERS_INFO_FRAME));
		aModel.setCommand("menuWindowAnalysisSelection", this.getLazyCommand(ANALYSIS_SELECTION_FRAME));
		aModel.setCommand("menuWindowDerivHistoFrame", this.getLazyCommand(HISTOGRAMM_FRAME));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		if (ConnectionInterface.getInstance() != null)
		{
			if (ConnectionInterface.getInstance().isConnected())
				internalDispatcher.notify(new ContextChangeEvent(
						ConnectionInterface.getInstance(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if (SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			if (aContext.getSessionInterface().isOpened())
				internalDispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		} else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(ConnectionInterface.getInstance()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	private VoidCommand getLazyCommand(final Object key) {
		return new VoidCommand() {
			private Command command;
			private Command getLazyCommand() {
				if (this.command == null) {
				Object object = frames.get(key);
				if (object instanceof JInternalFrame) {
					System.out.println("init getLazyCommand for " + key);
					this.command = new ShowFrameCommand(
						desktopPane, (JInternalFrame)object);
				}
				}
				return command;
			}
			
			public Object clone() {
				return this.getLazyCommand().clone();
			}
			
			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}
	
	void setDefaultModel(ApplicationModel aModel)
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

		aModel.setEnabled("menuFileOpen", AnalyseMainFrameSimplified.DEBUG); // XXX:
																				// saa:
																				// security
																				// bypass

		aModel.setVisible("menuAnalyseSaveCriteria", false);
		aModel.setVisible("menuSaveEtalon", false);
		aModel.setVisible("menuSaveThresholds", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);	
	}
	
	void arrange() {
		WindowArranger analysisExtWindowArranger = (WindowArranger)frames.get(WINDOW_ARRANGER);
		analysisExtWindowArranger.arrange();
		AnalysisFrame analysisFrame = (AnalysisFrame)frames.get(ANALYSIS_FRAME);
		analysisFrame.grabFocus();
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		aContext.setDispatcher(internalDispatcher);
		if (aContext.getApplicationModel() == null)
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
		return internalDispatcher;
	}

	/*
	 * public void modelChanged(String[] params) {
	 * menuBar.setVisible(aModel.isVisible("menuBar"));
	 * toolBar.setVisible(aModel.isVisible("toolBar"));
	 * statusBar.setVisible(aModel.isVisible("statusBar")); }
	 */
	public void operationPerformed(OperationEvent ae)	{
		String actionCommand = ae.getActionCommand();
		if (actionCommand.equals("contextchange")) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					// aContext.setSessionInterface(ssi);
					// aContext.setDataSourceInterface(Environment.getDefaultDataSourceInterface(aContext.getSessionInterface()));

					setSessionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", ConnectionInterface.getInstance().getServerName());
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();
				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if (cce.DOMAIN_SELECTED) {
				setDomainSelected();
			}
		} else if (actionCommand.equals(RefUpdateEvent.typ)) {
			RefUpdateEvent rue = (RefUpdateEvent) ae;

			if (rue.analysisPerformed()) {
				updFrames(Heap.PRIMARY_TRACE_KEY);
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
//		this.arrange();
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
		// new SchemeDataSourceImage(aContext.getDataSource()).LoadSchemes();
		// new ConfigDataSourceImage(aContext.getDataSource()).LoadNet();
		// new ConfigDataSourceImage(aContext.getDataSource()).LoadISM();

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
		aModel.setEnabled("menuNetStudy", true);
		aModel.fireModelChanged("");

		try
		{
			Identifier domainId = ((RISDSessionInfo )aContext.getSessionInterface()).getDomainIdentifier();
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
				domainId, true);
			statusBar.setText("domain", domain.getName());
		} catch (ApplicationException ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void setSessionOpened()
	{
		Checker checker = new Checker(aContext.getDataSource());
		if (!checker.checkCommand(Checker.enterExtendedAnalysisModul))
		{
			JOptionPane.showMessageDialog(this,
				"Недостаточно прав для работы с модулем исследования.",
				"Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		// DataSourceInterface dataSource = aContext.getDataSourceInterface();
		// new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		// new SurveyDataSourceImage(dataSource).LoadTestTypes();
		// new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		// new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		// new SurveyDataSourceImage(dataSource).LoadModelingTypes();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuSessionNew", false);
		aModel.fireModelChanged("");
		Identifier domain_id = new Identifier(
				((RISDSessionInfo )aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		internalDispatcher.notify(new ContextChangeEvent(domain_id,
				ContextChangeEvent.DOMAIN_SELECTED_EVENT));
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
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		double deltaX = bs.getResolution();

		double[] filtered = Heap.getRefAnalysisByKey(id).filtered;
		double[] noise = Heap.getRefAnalysisByKey(id).noise;
		
		ScalableFrame noiseFrame = (ScalableFrame) this.frames.get(NOISE_FRAME);
		ScalableFrame filteredFrame = (ScalableFrame) this.frames.get(FILTERED_FRAME);

		noiseFrame.setGraph(noise, deltaX, false, id);
		noiseFrame.updScales();
		noiseFrame.setVisible(true);
		filteredFrame.setGraph(filtered, deltaX, true, id);
		filteredFrame.updScales();
		filteredFrame.setVisible(true);
	}

	void setActiveRefId(String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId",
			id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId",
			id);
	}	

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
			// ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			// SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
//			ColorManager.saveIni();
			aManager.saveIni();
			internalDispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		String id = key;
		if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
		{
			// do nothing special here: special code is performed in
			// primaryTraceCUpdated()
		} else if (id.equals(Heap.REFERENCE_TRACE_KEY))
		{
			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent" });
		} else
		{
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.fireModelChanged("");
		}
	}

	public void bsHashRemoved(String key)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		String id = key;
		if (id.equals(Heap.REFERENCE_TRACE_KEY))
		{
			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent" });
		}
		Heap.updateCurrentTraceWhenBSRemoved();
		if (! Heap.hasSecondaryBS())
		{
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuTraceRemoveCompare" });
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
		ScalableFrame noiseFrame = (ScalableFrame) this.frames.get(NOISE_FRAME);
		noiseFrame.setVisible(false);
		ScalableFrame filteredFrame = (ScalableFrame) this.frames.get(FILTERED_FRAME);
		filteredFrame.setVisible(false);

		setTitle(LangModelAnalyse.getString("AnalyseExtTitle"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener#primaryTraceCUpdated()
	 */
	public void primaryTraceCUpdated()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		if (true) // XXX: if(isCreated)
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
		}

		aModel.fireModelChanged("");

		String name = Heap.getBSPrimaryTrace().title;
		setTitle(LangModelAnalyse.getString("AnalyseExtTitle") + ": " + name);

		updFrames(Heap.PRIMARY_TRACE_KEY);
	}

	public void primaryTraceRemoved()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener#primaryMTMCUpdated()
	 */
	public void primaryMTMCUpdated()
	{
		// @todo Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener#primaryMTMRemoved()
	 */
	public void primaryMTMRemoved()
	{
		// @todo Auto-generated method stub

	}

	public void etalonMTMCUpdated()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", true);
		aModel.fireModelChanged("");
	}

	public void etalonMTMRemoved()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", false);
		aModel.fireModelChanged(new String[] { "menuTraceCloseEtalon" });
	}

	public void currentTraceChanged(String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
		{
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare",
					"menuTraceRemoveCompare" });
		} else
		{
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare",
					"menuTraceRemoveCompare" });
			setActiveRefId(id);
		}
	}
}
