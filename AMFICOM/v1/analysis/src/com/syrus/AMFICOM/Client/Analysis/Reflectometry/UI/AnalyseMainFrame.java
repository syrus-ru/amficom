
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class AnalyseMainFrame extends AbstractMainFrame implements BsHashChangeListener, PrimaryTraceListener,
		PrimaryRefAnalysisListener, EtalonMTMListener, CurrentTraceChangeListener {

	ClientAnalysisManager		aManager					= new ClientAnalysisManager();

	public static final String	NOISE_FRAME					= "noiseFrame";
	
	public static final String	NOISE_HISTOGRAMM_FRAME				= "noiseHistogrammFrame";
	
	public static final String	SELECTOR_FRAME				= "selectFrame";

	public static final String	PRIMARY_PARAMETERS_FRAME	= "paramFrame";

	public static final String	STATS_FRAME					= "statsFrame";

	public static final String	EVENTS_FRAME				= "eventsFrame";

	public static final String	MARKERS_INFO_FRAME			= "MarkersInfoFrame";

	public static final String	ANALYSIS_FRAME				= "analysisFrame";

	public static final String	ANALYSIS_SELECTION_FRAME	= "AnalysisSelectionFrame";

	public static final String	DETAILED_EVENTS_FRAME		= "DetailedEventsFrame";

	public static final String	HISTOGRAMM_FRAME			= "HistogrammFrame";

	UIDefaults					frames;

	CreateAnalysisReportCommand	analysisReportCommand;
	NoiseHistogrammPanel noiseHistogrammPanel;	

	public AnalyseMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("AnalyseExtTitle"), new AnalyseMainMenuBar(aContext
				.getApplicationModel()), new AnalyseMainToolBar());

		this.addComponentListener(new ComponentAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
			 */
			public void componentShown(ComponentEvent e) {
				initModule();
				AnalyseMainFrame.this.desktopPane.setPreferredSize(AnalyseMainFrame.this.desktopPane.getSize());
				AnalyseMainFrame.this.windowArranger.arrange();
			}
		}
			);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				AnalyseMainFrame.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
					AnalyseMainFrame.this);
				Environment.getDispatcher()
						.removePropertyChangeListener(ContextChangeEvent.TYPE, AnalyseMainFrame.this);
				aContext.getApplicationModel().getCommand("menuExit").execute();
			}
		});

		this.frames = new UIDefaults();
		this.frames.put(SELECTOR_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Log.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(AnalyseMainFrame.this.dispatcher);
				desktopPane.add(selectFrame);
				return selectFrame;
			}
		});

		this.frames.put(PRIMARY_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | PRIMARY_PARAMETERS_FRAME", Log.FINEST);
				PrimaryParametersFrame paramFrame = new PrimaryParametersFrame();
				desktopPane.add(paramFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, paramFrame);
				return paramFrame;
			}
		});

		this.frames.put(STATS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | STATS_FRAME", Log.FINEST);
				OverallStatsFrame statsFrame = new OverallStatsFrame(AnalyseMainFrame.this.dispatcher);
				desktopPane.add(statsFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, statsFrame);
				return statsFrame;
			}
		});

		this.frames.put(NOISE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | NOISE_FRAME", Log.FINEST);
				ScalableFrame noiseFrame = new ScalableFrame(new ScalableLayeredPanel());
				noiseFrame.setTitle(LangModelAnalyse.getString("Noise level"));
				desktopPane.add(noiseFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL, noiseFrame);
				return noiseFrame;
			}
		});

//		this.frames.put(FILTERED_FRAME, new UIDefaults.LazyValue() {
//
//			public Object createValue(UIDefaults table) {
//				Log.debugMessage(".createValue | FILTERED_FRAME", Log.FINEST);
//				ScalableFrame filteredFrame = new ScalableFrame(new ScalableLayeredPanel());
//				filteredFrame.setTitle(LangModelAnalyse.getString("filteredTitle"));
//				desktopPane.add(filteredFrame);
//				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL,
//					filteredFrame);
//				return filteredFrame;
//			}
//		});
		
		this.frames.put(NOISE_HISTOGRAMM_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | NOISE_HISTOGRAMM_FRAME", Log.FINEST);
				
				ScalableLayeredPanel layeredPanel = new ScalableLayeredPanel();
				noiseHistogrammPanel = new NoiseHistogrammPanel(layeredPanel);
				layeredPanel.setGraphPanel(noiseHistogrammPanel);
				ScalableFrame noiseHistoFrame = new ScalableFrame(layeredPanel);
				noiseHistoFrame.setTitle(LangModelAnalyse.getString("noiseHistoTitle"));
				desktopPane.add(noiseHistoFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL,
						noiseHistoFrame);
				return noiseHistoFrame;
			}
		});

		super.setWindowArranger(new WindowArranger(AnalyseMainFrame.this) {

					public void arrange() {
						AnalyseMainFrame f = (AnalyseMainFrame) this.mainframe;

						int w = f.desktopPane.getSize().width;
						int h = f.desktopPane.getSize().height;
						int minh = Math.min(205, h / 4);

						JInternalFrame selectFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.SELECTOR_FRAME);
						JInternalFrame paramFrame = (JInternalFrame) f.frames
								.get(AnalyseMainFrame.PRIMARY_PARAMETERS_FRAME);
						JInternalFrame statsFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.STATS_FRAME);
						JInternalFrame noiseFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.NOISE_FRAME);
						JInternalFrame noiseHistoFrame = (JInternalFrame) f.frames.get(AnalyseMainFrame.NOISE_HISTOGRAMM_FRAME);
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
						normalize(noiseHistoFrame);

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
						noiseHistoFrame.setSize(w / 3, (h - 2 * minh) / 2);

						paramFrame.setLocation(w / 6, 0);
						selectFrame.setLocation(0, 0);
						statsFrame.setLocation(w / 3, 0);
						mInfoFrame.setLocation(w / 2, 0);
						analysisFrame.setLocation(0, minh);
						anaSelectFrame.setLocation(2 * w / 3, 0);
						noiseFrame.setLocation(2 * w / 3, minh);
						noiseHistoFrame.setLocation(2 * w / 3, minh + noiseFrame.getHeight());
						eventsFrame.setLocation(0, minh + analysisFrame.getHeight());
						detailedEvFrame.setLocation(eventsFrame.getWidth(), minh + analysisFrame.getHeight());
						dhf.setLocation(eventsFrame.getWidth() + detailedEvFrame.getWidth(), minh
								+ analysisFrame.getHeight());
					}
			});

		this.frames.put(EVENTS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EVENTS_FRAME", Log.FINEST);
				EventsFrame eventsFrame = new EventsFrame(aContext);
				desktopPane.add(eventsFrame);
				analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE, eventsFrame);
				return eventsFrame;
			}
		});

		this.frames.put(DETAILED_EVENTS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | DETAILED_EVENTS_FRAME", Log.FINEST);
				DetailedEventsFrame detailedEvFrame = new DetailedEventsFrame();
				desktopPane.add(detailedEvFrame);
				return detailedEvFrame;
			}
		});

		this.frames.put(ANALYSIS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Log.FINEST);
				PathElementsFrame analysisFrame = new PathElementsFrame(aContext, AnalyseMainFrame.this.dispatcher);
				desktopPane.add(analysisFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL,
					analysisFrame);
				return analysisFrame;
			}
		});

		this.frames.put(MARKERS_INFO_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | MARKERS_INFO_FRAME", Log.FINEST);
				MarkersInfoFrame mInfoFrame = new MarkersInfoFrame(AnalyseMainFrame.this.dispatcher);
				desktopPane.add(mInfoFrame);
				return mInfoFrame;
			}
		});

		this.frames.put(ANALYSIS_SELECTION_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_SELECTION_FRAME", Log.FINEST);
				AnalysisSelectionFrame analysisSelectionFrame = new AnalysisSelectionFrame(aContext);
				desktopPane.add(analysisSelectionFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.TABLE,
					analysisSelectionFrame);
				return analysisSelectionFrame;
			}
		});

		this.frames.put(HISTOGRAMM_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | HISTOGRAMM_FRAME", Log.FINEST);
				HistogrammFrame histogrammFrame = new HistogrammFrame(AnalyseMainFrame.this.dispatcher);
				desktopPane.add(histogrammFrame);
				AnalyseMainFrame.this.analysisReportCommand.setParameter(CreateAnalysisReportCommand.PANEL,
					histogrammFrame);
				return histogrammFrame;
			}
		});		
	}

	public AnalyseMainFrame() {
		this(new ApplicationContext());
	}

	public void initModule() {
		super.initModule();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		Heap.addBsHashListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addPrimaryTraceListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		Environment.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		aModel.setCommand("menuSessionNew", new OpenSessionCommand(Environment.getDispatcher()));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher()));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(this.aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(),
																				this.aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(),
																						this.aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), this.aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(this.aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuFileAddCompare", new FileAddCommand(this.aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(null, this.aContext));

		aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(this.aContext));
		aModel.setCommand("menuCreateTestSetup", new CreateTestSetupCommand(this.aContext));
		aModel.setCommand("menuLoadTestSetup", new LoadTestSetupCommand(this.aContext));
		aModel.setCommand("menuSaveTestSetup", new SaveTestSetupCommand(this.aContext, SaveTestSetupCommand.ETALON
				+ SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuSaveTestSetupAs", new SaveTestSetupAsCommand(this.aContext, SaveTestSetupCommand.ETALON
				+ SaveTestSetupCommand.CRITERIA));
		aModel.setCommand("menuNetStudy", new NetStudyCommand());

		aModel.setCommand("menuTraceDownload", new LoadTraceFromDatabaseCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand());
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(null, this.aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand());
		aModel.setCommand("menuTraceReferenceSet", new TraceOpenReferenceCommand(this.aContext));
		aModel.setCommand("menuTraceReferenceMakeCurrent", new TraceMakeCurrentCommand(this.aContext));
		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(this.aContext));

		this.analysisReportCommand = new CreateAnalysisReportCommand(this.aContext);
		// analysisReportCommand.setParameter(CreateAnalysisReportCommand.TYPE,
		// ReportTemplate.rtt_Survey);
		aModel.setCommand("menuReportCreate", this.analysisReportCommand);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(this.windowArranger));

		aModel.setCommand("menuWindowTraceSelector", this.getLazyCommand(SELECTOR_FRAME));
		aModel.setCommand("menuWindowPrimaryParameters", this.getLazyCommand(PRIMARY_PARAMETERS_FRAME));
		aModel.setCommand("menuWindowOverallStats", this.getLazyCommand(STATS_FRAME));
		aModel.setCommand("menuWindowNoiseFrame", this.getLazyCommand(NOISE_FRAME));
		aModel.setCommand("menuWindowFilteredFrame", this.getLazyCommand(NOISE_HISTOGRAMM_FRAME));
		aModel.setCommand("menuWindowEvents", this.getLazyCommand(EVENTS_FRAME));
		aModel.setCommand("menuWindowDetailedEvents", this.getLazyCommand(DETAILED_EVENTS_FRAME));
		aModel.setCommand("menuWindowAnalysis", this.getLazyCommand(ANALYSIS_FRAME));
		aModel.setCommand("menuWindowMarkersInfo", this.getLazyCommand(MARKERS_INFO_FRAME));
		aModel.setCommand("menuWindowAnalysisSelection", this.getLazyCommand(ANALYSIS_SELECTION_FRAME));
		aModel.setCommand("menuWindowDerivHistoFrame", this.getLazyCommand(HISTOGRAMM_FRAME));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		// if (ClientSessionEnvironment.getInstance().sessionEstablished()) {
		// this.dispatcher.firePropertyChange(new ContextChangeEvent(this,
		// ContextChangeEvent.SESSION_OPENED_EVENT), true);
		// }
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = AnalyseMainFrame.this.frames.get(key);
					if (object instanceof JInternalFrame) {
						System.out.println("init getLazyCommand for " + key);
						this.command = new ShowWindowCommand(object);
					}
				}
				return this.command;
			}

			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}

	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		aModel.setEnabled("menuFile", true);
		aModel.setEnabled("menuTrace", true);
		aModel.setEnabled("menuTestSetup", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setEnabled("menuFileOpen", AnalyseMainFrameSimplified.DEBUG);

		aModel.setVisible("menuAnalyseSaveCriteria", false);
		aModel.setVisible("menuSaveEtalon", false);
		aModel.setVisible("menuSaveThresholds", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);
	}

	public void setDomainSelected() {
		super.setDomainSelected();

		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuTraceDownload", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuNetStudy", true);
		aModel.fireModelChanged("");

	}

	public void setSessionClosed() {
		super.setSessionClosed();
		ApplicationModel aModel = this.aContext.getApplicationModel();

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

	}

	void updFrames() {
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		RefAnalysis ra = Heap.getRefAnalysisPrimary();
		if (bs == null || ra == null) {
			closeFrames();
		} else {
			double deltaX = bs.getResolution();

//			double[] filtered = Heap.getRefAnalysisPrimary().filtered;
			double[] noise = Heap.getRefAnalysisPrimary().noise;

			ScalableFrame noiseFrame = (ScalableFrame) this.frames.get(NOISE_FRAME);
			ScalableFrame noiseHistoFrame = (ScalableFrame) this.frames.get(NOISE_HISTOGRAMM_FRAME);

			noiseFrame.setGraph(noise, deltaX, false, Heap.PRIMARY_TRACE_KEY);
			noiseFrame.updScales();
			noiseFrame.setVisible(true);
			
			noiseHistogrammPanel.updateHistogrammData();
			noiseHistoFrame.updScales();
			noiseHistoFrame.setVisible(true);
		}
	}

	void setActiveRefId(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId", id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
			// ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			// SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// ColorManager.saveIni();
			this.aManager.saveIni();
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			this.aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}

	public void bsHashAdded(String key,
							BellcoreStructure bs) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String id = key;
		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			// do nothing special here: special code is performed in
			// primaryTraceCUpdated()
		} else if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		} else {
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.fireModelChanged("");
		}
	}

	public void bsHashRemoved(String key) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String id = key;
		if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		}
		Heap.updateCurrentTraceWhenBSRemoved();
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuTraceRemoveCompare"});
		}
	}

	private void closeFrames() {
		ScalableFrame noiseFrame = (ScalableFrame) this.frames.get(NOISE_FRAME);
		noiseFrame.setVisible(false);
		ScalableFrame filteredFrame = (ScalableFrame) this.frames.get(NOISE_HISTOGRAMM_FRAME);
		filteredFrame.setVisible(false);
	}

	public void bsHashRemovedAll() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
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
		updFrames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener#primaryTraceCUpdated()
	 */
	public void primaryTraceCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
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

		//updFrames();
	}

	public void primaryTraceRemoved() {
	}

	public void primaryRefAnalysisCUpdated() {
		updFrames();
	}

	public void primaryRefAnalysisRemoved() {
		updFrames();
	}

	public void etalonMTMCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", true);
		aModel.fireModelChanged("");
	}

	public void etalonMTMRemoved() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", false);
		aModel.fireModelChanged(new String[] { "menuTraceCloseEtalon"});
	}

	public void currentTraceChanged(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare", "menuTraceRemoveCompare"});
		} else {
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare", "menuTraceRemoveCompare"});
			setActiveRefId(id);
		}
	}
}
