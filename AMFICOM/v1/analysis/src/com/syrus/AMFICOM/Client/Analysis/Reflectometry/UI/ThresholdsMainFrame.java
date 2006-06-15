
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_ANALYSIS_MAIN;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_ANALYSIS_SELECTION;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_DETAILED_EVENTS;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_EVENTS;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_HISTOGRAMM;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_MARKERS_INFO;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_NOISE;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_NOISE_HISTOGRAMM;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_OVERALL_STATS;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_PRIMARY_PARAMETERS;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_THRESHOLDS;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_THRESHOLDS_SELECTION;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_TRACE_SELECTOR;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager.Operation;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AddTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CheckMismatchCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileAddCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsBellcoreCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsWavetekCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAllTracesCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAsTextCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.MakeCurrentTracePrimaryCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SavePathElementsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.util.Log;

public class ThresholdsMainFrame extends AbstractMainFrame
implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener {
	private static final long serialVersionUID = 2086348259538964334L;

	ClientAnalysisManager		aManager			= new ClientAnalysisManager();

	UIDefaults					frames;
	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public ThresholdsMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("ThresholdsTitle"), new AnalyseMainMenuBar(aContext
			.getApplicationModel()), new AnalyseMainToolBar());

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ThresholdsMainFrame.this.desktopPane.setPreferredSize(ThresholdsMainFrame.this.desktopPane.getSize());
				ThresholdsMainFrame.this.windowArranger.arrange();
			}
		}
			);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				aContext.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ThresholdsMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ThresholdsMainFrame.this);
				ThresholdsMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
			}
		});

		PermissionManager.setEvaluationTranslation();
		
		// XXX: debug code
		if (System.getProperty("amficom.debug.partialHeight", "false").equals("true"))
			this.setSize(getSize().width, getSize().height * 3 / 4);
	}

	protected void initFrames() {
		this.frames = new UIDefaults();
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		final JDesktopPane desktop = ThresholdsMainFrame.this.desktopPane;
		
		this.frames.put(FRAME_TRACE_SELECTOR, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Level.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(ThresholdsMainFrame.this.dispatcher);
				desktop.add(selectFrame);
				return selectFrame;
			}
		});
		
		this.frames.put(FRAME_PRIMARY_PARAMETERS, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | PRIMARY_PARAMETERS_FRAME", Level.FINEST);
				PrimaryParametersFrame paramFrame = new PrimaryParametersFrame() {
					@Override
					public String getReportTitle() {
						return FRAME_PRIMARY_PARAMETERS;
					}
				};
				desktop.add(paramFrame);
				ThresholdsMainFrame.this.tables.add(paramFrame);
				return paramFrame;
			}
		});

		this.frames.put(FRAME_OVERALL_STATS, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | STATS_FRAME", Level.FINEST);
				OverallStatsFrame statsFrame = new OverallStatsFrame(ThresholdsMainFrame.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return FRAME_OVERALL_STATS;
					}
				};
				desktop.add(statsFrame);
				ThresholdsMainFrame.this.tables.add(statsFrame);
				return statsFrame;
			}
		});

		this.frames.put(FRAME_EVENTS, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EVENTS_FRAME", Level.FINEST);
				EventsFrame eventsFrame = new EventsFrame(aContext, false) {
					@Override
					public String getReportTitle() {
						return FRAME_EVENTS;
					}
				};
				desktop.add(eventsFrame);
				ThresholdsMainFrame.this.tables.add(eventsFrame);
				return eventsFrame;
			}
		});

		this.frames.put(FRAME_DETAILED_EVENTS, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | DETAILED_EVENTS_FRAME", Level.FINEST);
				DetailedEventsFrame detailedEvFrame = new DetailedEventsFrame();
				desktop.add(detailedEvFrame);
				return detailedEvFrame;
			}
		});
		
		this.frames.put(FRAME_THRESHOLDS, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | THRESHOLDS_FRAME", Level.FINEST);
				ThresholdsFrame thresholdsFrame = new ThresholdsFrame(ThresholdsMainFrame.this.dispatcher)  {
					@Override
					public String getReportTitle() {
						return FRAME_THRESHOLDS;
					}
				};
				desktop.add(thresholdsFrame);
				ThresholdsMainFrame.this.graphs.add(thresholdsFrame);
				return thresholdsFrame;
			}
		});

		this.frames.put(FRAME_THRESHOLDS_SELECTION, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | THRESHOLDS_SELECTION_FRAME", Level.FINEST);
				ThresholdsSelectionFrame thresholdsSelectionFrame = new ThresholdsSelectionFrame(ThresholdsMainFrame.this.dispatcher)  {
					@Override
					public String getReportTitle() {
						return FRAME_THRESHOLDS_SELECTION;
					}
				};
				desktop.add(thresholdsSelectionFrame);
				ThresholdsMainFrame.this.tables.add(thresholdsSelectionFrame);
				return thresholdsSelectionFrame;
			}
		});

		this.frames.put(FRAME_ANALYSIS_MAIN, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Level.FINEST);
				PathElementsFrame analysisFrame = new PathElementsFrame(ThresholdsMainFrame.this.aContext, ThresholdsMainFrame.this.dispatcher)  {
					@Override
					public String getReportTitle() {
						return FRAME_ANALYSIS_MAIN;
					}
				};
				desktop.add(analysisFrame);
				ThresholdsMainFrame.this.graphs.add(analysisFrame);
				return analysisFrame;
			}
		});

		this.frames.put(FRAME_MARKERS_INFO, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | MARKERS_INFO_FRAME", Level.FINEST);
				MarkersInfoFrame mInfoFrame = new MarkersInfoFrame(ThresholdsMainFrame.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return FRAME_MARKERS_INFO;
					}
				};
				ThresholdsMainFrame.this.tables.add(mInfoFrame);
				desktop.add(mInfoFrame);
				return mInfoFrame;
			}
		});
		
		this.frames.put(FRAME_ANALYSIS_SELECTION, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_SELECTION_FRAME", Level.FINEST);
				AnalysisSelectionFrame analysisSelectionFrame = new AnalysisSelectionFrame(aContext) {
					@Override
					public String getReportTitle() {
						return FRAME_ANALYSIS_SELECTION;
					}
				};
				desktop.add(analysisSelectionFrame);
				ThresholdsMainFrame.this.tables.add(analysisSelectionFrame);
				return analysisSelectionFrame;
			}
		});
		
		this.setWindowArranger( new WindowArranger(this) {
			@Override
			public void arrange() {
				ThresholdsMainFrame f = (ThresholdsMainFrame) this.mainframe;
				
				int w = f.desktopPane.getSize().width;
				int h = f.desktopPane.getSize().height;
				int minh = Math.min(207, h / 4);
				
				JInternalFrame selectFrame = (JInternalFrame) f.frames.get(FRAME_TRACE_SELECTOR);
				JInternalFrame paramFrame = (JInternalFrame) f.frames.get(FRAME_PRIMARY_PARAMETERS);
				JInternalFrame statsFrame = (JInternalFrame) f.frames.get(FRAME_OVERALL_STATS);
				JInternalFrame eventsFrame = (JInternalFrame) f.frames.get(FRAME_EVENTS);
				JInternalFrame detailedEvFrame = (JInternalFrame) f.frames.get(FRAME_DETAILED_EVENTS);
				JInternalFrame analysisFrame = (JInternalFrame) f.frames.get(FRAME_ANALYSIS_MAIN);
				JInternalFrame mInfoFrame = (JInternalFrame) f.frames.get(FRAME_MARKERS_INFO);
				JInternalFrame anaSelectFrame = (JInternalFrame) f.frames.get(FRAME_ANALYSIS_SELECTION);
				JInternalFrame dhf = (JInternalFrame) f.frames.get(FRAME_HISTOGRAMM);
				JInternalFrame thresholdsFrame = (JInternalFrame) f.frames.get(FRAME_THRESHOLDS);
				JInternalFrame thresholdsSelectionFrame = (JInternalFrame) f.frames.get(FRAME_THRESHOLDS_SELECTION);
				
				normalize(paramFrame);
				normalize(selectFrame);
				normalize(statsFrame);
				normalize(mInfoFrame);
				normalize(analysisFrame);
				normalize(thresholdsFrame);
				normalize(eventsFrame);
				normalize(detailedEvFrame);
				normalize(anaSelectFrame);
				normalize(thresholdsSelectionFrame);
				
				paramFrame.setSize(w / 6, minh);
				selectFrame.setSize(w / 6, minh);
				statsFrame.setSize(w / 6, minh);
				mInfoFrame.setSize(w / 6, minh);
				analysisFrame.setSize(2 * w / 3, h - 2 * minh);
				thresholdsFrame.setSize(w / 3, h - 2 * minh);
				eventsFrame.setSize(w / 2, minh);
				detailedEvFrame.setSize(w / 6, minh);
				anaSelectFrame.setSize(w / 3, minh);
				thresholdsSelectionFrame.setSize(w / 3, minh);
				
				paramFrame.setLocation(w / 6, 0);
				selectFrame.setLocation(0, 0);
				statsFrame.setLocation(w / 3, 0);
				mInfoFrame.setLocation(w / 2, 0);
				analysisFrame.setLocation(0, minh);
				thresholdsFrame.setLocation(analysisFrame.getWidth(), minh);
				
				anaSelectFrame.setLocation(2 * w / 3, 0);
				
				eventsFrame.setLocation(0, minh + analysisFrame.getHeight());
				detailedEvFrame.setLocation(eventsFrame.getWidth(), minh + analysisFrame.getHeight());
				thresholdsSelectionFrame.setLocation(eventsFrame.getWidth() + detailedEvFrame.getWidth(), minh
						+ analysisFrame.getHeight());
			}
		});
	}
	
	public ThresholdsMainFrame() {
		this(new ApplicationContext());
	}

	@Override
	protected void initModule() {
		super.initModule();
		PermissionManager.setCacheable(true);
		initFrames();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_OPEN, new FileOpenCommand(this.dispatcher, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE, new FileOpenAsBellcoreCommand(this.dispatcher, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK, new FileOpenAsWavetekCommand(this.dispatcher, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_SAVE, new FileSaveCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_SAVE_ALL, new FileSaveAllTracesCommand());
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT, new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_CLOSE, new FileCloseCommand());
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE, new FileAddCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE, 
				new CreateTestSetupCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE, 
				new SaveTestSetupCommand(this.aContext, SaveTestSetupCommand.CRITERIA + SaveTestSetupCommand.ETALON));
		aModel.setCommand(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS, 
				new SaveTestSetupAsCommand(this.aContext, SaveTestSetupCommand.CRITERIA + SaveTestSetupCommand.ETALON));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD, new LoadTraceFromDatabaseCommand(this.dispatcher, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD, new LoadModelingCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY, new MakeCurrentTracePrimaryCommand());

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, 
				DestinationModules.EVALUATION);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);

		aModel.setCommand(AnalyseApplicationModel.MENU_REPORT_CREATE, rc);
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, new SavePathElementsCommand(this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, this.getLazyCommand(FRAME_TRACE_SELECTOR));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, this.getLazyCommand(FRAME_PRIMARY_PARAMETERS));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, this.getLazyCommand(FRAME_OVERALL_STATS));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_EVENTS, this.getLazyCommand(FRAME_EVENTS));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS, this.getLazyCommand(FRAME_DETAILED_EVENTS));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, this.getLazyCommand(FRAME_ANALYSIS_MAIN));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, this.getLazyCommand(FRAME_MARKERS_INFO));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, this.getLazyCommand(FRAME_ANALYSIS_SELECTION));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM, this.getLazyCommand(FRAME_HISTOGRAMM));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS, this.getLazyCommand(FRAME_THRESHOLDS));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION, this.getLazyCommand(FRAME_THRESHOLDS_SELECTION));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, new CheckMismatchCommand());

		aModel.fireModelChanged("");
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = ThresholdsMainFrame.this.frames.get(key);
					if (object instanceof JInternalFrame) {
						System.out.println("init getLazyCommand for " + key);
						this.command = new ShowWindowCommand((JInternalFrame) object);
					}
				}
				return this.command;
			}

			@Override
			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}
	
	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);

		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE, true);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE, true);
		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP, true);
		aModel.setEnabled(AnalyseApplicationModel.MENU_REPORT, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);

		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN, AnalyseMainFrameSimplified.DEBUG);

		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_NOISE, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_FILTERED, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, true);
		
		aModel.setVisible(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, true);
	}

	@Override
	public void loggedIn() {
		PermissionManager.refresh();

		final ApplicationModel aModel = this.aContext.getApplicationModel();
		final boolean readFilePermitted =
			PermissionManager.isPermitted(Operation.READ_TRACE_FILE);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN, readFilePermitted);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_AS, readFilePermitted);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE, readFilePermitted);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK, readFilePermitted);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD,
				PermissionManager.isPermitted(Operation.LOAD_TRACE));
		aModel.setEnabled(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD,
				PermissionManager.isPermitted(Operation.LOAD_TRACE));
		
		aModel.fireModelChanged("");
	}

	@Override
	public void loggedOut() {
		PermissionManager.refresh();

		final ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_AS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS, false);

		aModel.fireModelChanged("");
	}


	void setActiveRefId(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.getCommand(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE).setParameter("activeRefId", id);
		aModel.getCommand(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE).setParameter("activeRefId", id);
	}


	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.aManager.saveIni();
		}
		super.processWindowEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener#bsHashAdded(java.lang.String,
	 *      com.syrus.io.BellcoreStructure)
	 */
	public void bsHashAdded(String key) {
		String id = key;
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			final boolean saveFilePermitted =
					PermissionManager.isPermitted(Operation.SAVE_TRACE_FILE);

			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE, saveFilePermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_ALL, saveFilePermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_AS, saveFilePermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT, saveFilePermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE,
					PermissionManager.isPermitted(Operation.READ_TRACE_FILE));

			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE,
					PermissionManager.isPermitted(Operation.LOAD_TRACE));

			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, true);
			
			final boolean saveMSPermitted =
					PermissionManager.isPermitted(Operation.SAVE_MEASUREMENT_SETUP);
			aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE, saveMSPermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE, saveMSPermitted);
			aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS, saveMSPermitted);
//			aModel.setEnabled("menuLoadTestSetup", true);
//			aModel.setEnabled("menuAnalyseSaveCriteria", true);
//			aModel.setEnabled("menuSaveEtalon", true);
//			aModel.setEnabled("menuSaveThresholds", true);

			aModel.setEnabled(AnalyseApplicationModel.MENU_REPORT_CREATE, true);

			aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_EVENTS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION, true);

			aModel.fireModelChanged("");
		} else if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
//			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
//			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		} else {
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, true);
			aModel.fireModelChanged(new String[] { "" });
		}
	}

	public void bsHashRemoved(String key) {
		String id = key;
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
//			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
//			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		}
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, false);
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
			aModel.fireModelChanged(new String[] { "" });
		}
	}

	public void bsHashRemovedAll() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_ALL, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_AS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS, false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_REPORT_CREATE, false);

		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_EVENTS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION, false);

		aModel.fireModelChanged("");
	}

	public void etalonMTMCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
//		aModel.setEnabled("menuTraceCloseEtalon", true);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, true);
		aModel.fireModelChanged( "" );
	}

	public void etalonMTMRemoved() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
//		aModel.setEnabled("menuTraceCloseEtalon", false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, false);
		aModel.fireModelChanged( "" );
	}

	public void currentTraceChanged(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, false);
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
		} else {
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, true);
			setActiveRefId(id);
		}
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY, Heap.isTraceSecondary(id));
		aModel.fireModelChanged(new String[] { "" });
	}
}
