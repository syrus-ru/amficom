
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.ANALYSIS_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.ANALYSIS_SELECTION_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.EVENTS_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.MARKERS_INFO_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.PRIMARY_PARAMETERS_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.SELECTOR_FRAME;
import static com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame.STATS_FRAME;

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
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileAddCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsBellcoreCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsWavetekCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAsTextCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.MakeCurrentTracePrimaryCommand;
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

public class AnalyseMainFrameSimplified extends AbstractMainFrame implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener {

	// FIXME: saa: security bypass in all references to this fiels
	public static final boolean	DEBUG		= System.getProperty("amficom.debug.nonstrict", "false").equals("true");

	ClientAnalysisManager		aManager	= new ClientAnalysisManager();

	UIDefaults					frames;
	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public AnalyseMainFrameSimplified(ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("AnalyseTitle"), new AnalyseMainMenuBar(aContext
				.getApplicationModel()), new AnalyseMainToolBar());

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				// init_module();
				AnalyseMainFrameSimplified.this.desktopPane.setPreferredSize(AnalyseMainFrameSimplified.this.desktopPane.getSize());
				AnalyseMainFrameSimplified.this.windowArranger.arrange();
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				AnalyseMainFrameSimplified.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
					AnalyseMainFrameSimplified.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE,
					AnalyseMainFrameSimplified.this);
				AnalyseMainFrameSimplified.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();

			}
		});

		PermissionManager.setAnalysisTranslation();
	}

	protected void initFrames() {
		this.frames = new UIDefaults();
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		final JDesktopPane desktop = AnalyseMainFrameSimplified.this.desktopPane;
		
		this.frames.put(SELECTOR_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Level.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(AnalyseMainFrameSimplified.this.dispatcher);
				desktop.add(selectFrame);
				return selectFrame;
			}
		});
		
		this.frames.put(PRIMARY_PARAMETERS_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | PRIMARY_PARAMETERS_FRAME", Level.FINEST);
				PrimaryParametersFrame paramFrame = new PrimaryParametersFrame() {
					@Override
					public String getReportTitle() {
						return PRIMARY_PARAMETERS_FRAME;
					}
				};
				desktop.add(paramFrame);
				AnalyseMainFrameSimplified.this.tables.add(paramFrame);
				return paramFrame;
			}
		});
		
		this.frames.put(STATS_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | STATS_FRAME", Level.FINEST);
				OverallStatsFrame statsFrame = new OverallStatsFrame(AnalyseMainFrameSimplified.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return STATS_FRAME;
					}
				};
				desktop.add(statsFrame);
				AnalyseMainFrameSimplified.this.tables.add(statsFrame);
				return statsFrame;
			}
		});
		
		this.frames.put(EVENTS_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EVENTS_FRAME", Level.FINEST);
				EventsFrame eventsFrame = new EventsFrame(aContext, false) {
					@Override
					public String getReportTitle() {
						return EVENTS_FRAME;
					}
				};
				desktop.add(eventsFrame);
				AnalyseMainFrameSimplified.this.tables.add(eventsFrame);
				return eventsFrame;
			}
		});
		
		this.frames.put(ANALYSIS_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Level.FINEST);
				AnalysisFrame analysisFrame = new AnalysisFrame(AnalyseMainFrameSimplified.this.dispatcher)  {
					@Override
					public String getReportTitle() {
						return ANALYSIS_FRAME;
					}
				};
				desktop.add(analysisFrame);
				AnalyseMainFrameSimplified.this.graphs.add(analysisFrame);
				return analysisFrame;
			}
		});
		
		this.frames.put(MARKERS_INFO_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | MARKERS_INFO_FRAME", Level.FINEST);
				MarkersInfoFrame mInfoFrame = new MarkersInfoFrame(AnalyseMainFrameSimplified.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return MARKERS_INFO_FRAME;
					}
				};
				AnalyseMainFrameSimplified.this.tables.add(mInfoFrame);
				desktop.add(mInfoFrame);
				return mInfoFrame;
			}
		});
		
		this.frames.put(ANALYSIS_SELECTION_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_SELECTION_FRAME", Level.FINEST);
				AnalysisSelectionFrame analysisSelectionFrame = new AnalysisSelectionFrame(aContext) {
					@Override
					public String getReportTitle() {
						return ANALYSIS_SELECTION_FRAME;
					}
				};
				desktop.add(analysisSelectionFrame);
				AnalyseMainFrameSimplified.this.tables.add(analysisSelectionFrame);
				return analysisSelectionFrame;
			}
		});
		
		this.setWindowArranger(new WindowArranger(this) {

			@Override
			public void arrange() {
				AnalyseMainFrameSimplified f = (AnalyseMainFrameSimplified) this.mainframe;

				int w = desktop.getSize().width;
				int h = desktop.getSize().height;

				JInternalFrame selectFrame = (JInternalFrame) f.frames.get(SELECTOR_FRAME);
				JInternalFrame paramFrame = (JInternalFrame) f.frames.get(PRIMARY_PARAMETERS_FRAME);
				JInternalFrame statsFrame = (JInternalFrame) f.frames.get(STATS_FRAME);
				JInternalFrame eventsFrame = (JInternalFrame) f.frames.get(EVENTS_FRAME);
				JInternalFrame analysisFrame = (JInternalFrame) f.frames.get(ANALYSIS_FRAME);
				JInternalFrame mInfoFrame = (JInternalFrame) f.frames.get(MARKERS_INFO_FRAME);
				JInternalFrame anaSelectFrame = (JInternalFrame) f.frames.get(ANALYSIS_SELECTION_FRAME);
				
				normalize(paramFrame);
				normalize(selectFrame);
				normalize(statsFrame);
				normalize(mInfoFrame);
				normalize(analysisFrame);
				normalize(eventsFrame);
				normalize(anaSelectFrame);

				paramFrame.setSize(w / 6, h / 4);
				statsFrame.setSize(w / 6, h / 4);
				mInfoFrame.setSize(w / 6, h / 4);
				selectFrame.setSize(w / 6, h - mInfoFrame.getHeight() - statsFrame.getHeight()
						- paramFrame.getHeight());
				analysisFrame.setSize(5 * w / 6, 3 * h / 4);
				eventsFrame.setSize(w / 2, h / 4);
				anaSelectFrame.setSize(w / 3, h / 4);

				selectFrame.setLocation(5 * w / 6, 0);
				paramFrame.setLocation(5 * w / 6, selectFrame.getHeight());
				mInfoFrame.setLocation(5 * w / 6, selectFrame.getHeight() + paramFrame.getHeight());
				statsFrame.setLocation(5 * w / 6, selectFrame.getHeight() + mInfoFrame.getHeight()
						+ paramFrame.getHeight());
				analysisFrame.setLocation(0, 0);
				anaSelectFrame.setLocation(0, analysisFrame.getHeight());
				eventsFrame.setLocation(w / 3, analysisFrame.getHeight());
			}
		});
	}
	
	public AnalyseMainFrameSimplified() {
		this(new ApplicationContext());
	}

	@Override
	public void initModule() {
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
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT, new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_CLOSE, new FileCloseCommand());
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE, new FileAddCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD, new LoadTraceFromDatabaseCommand(this.dispatcher, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY, new MakeCurrentTracePrimaryCommand());

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, 
				DestinationModules.ANALYSIS);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);
		
		aModel.setCommand(AnalyseApplicationModel.MENU_REPORT_CREATE, rc);

		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, this.getLazyCommand(SELECTOR_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, this.getLazyCommand(PRIMARY_PARAMETERS_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, this.getLazyCommand(STATS_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_EVENTS, this.getLazyCommand(EVENTS_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, this.getLazyCommand(ANALYSIS_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, this.getLazyCommand(MARKERS_INFO_FRAME));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, this.getLazyCommand(ANALYSIS_SELECTION_FRAME));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = AnalyseMainFrameSimplified.this.frames.get(key);
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

		aModel.setVisible(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_MEASUREMENTSETUP, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_NOISE, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_FILTERED, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM, false);
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
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String id = key;
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

			aModel.setEnabled(AnalyseApplicationModel.MENU_REPORT_CREATE, true);

			aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_EVENTS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, true);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener#bsHashRemoved(java.lang.String)
	 */
	public void bsHashRemoved(String key) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String id = key;
		if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
//			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
//			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		}
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE, false);
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
			aModel.fireModelChanged("");
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

		aModel.setEnabled(AnalyseApplicationModel.MENU_REPORT_CREATE, false);

		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_EVENTS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, false);

		aModel.fireModelChanged("");
	}

	public void etalonMTMCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
//		aModel.setEnabled("menuTraceCloseEtalon", true);
//		aModel.fireModelChanged(new String[] { "menuTraceCloseEtalon"});
	}

	public void etalonMTMRemoved() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
//		aModel.setEnabled("menuTraceCloseEtalon", false);
//		aModel.fireModelChanged(new String[] { "menuTraceCloseEtalon"});
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
