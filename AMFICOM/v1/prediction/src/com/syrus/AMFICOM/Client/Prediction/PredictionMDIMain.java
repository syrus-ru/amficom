package com.syrus.AMFICOM.Client.Prediction;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalysisFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.DetailedEventsFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.EventsFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.HistogrammFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MarkersInfoFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.NoiseHistogrammPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.OverallStatsFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.PrimaryParametersFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ReportTable;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScalableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleResizableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TraceSelectorFrame;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AddTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateAnalysisReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Prediction.CountPredictedReflectogramm;
import com.syrus.AMFICOM.Client.General.Command.Prediction.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Prediction.SavePredictionCommand;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModel;
import com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence.TimeDependenceFrame;
import com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence.TimeDependenceTable;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.util.Log;

public class PredictionMDIMain extends AbstractMainFrame implements BsHashChangeListener, PrimaryTraceListener {
	private static final long serialVersionUID = 6748721407305415134L;

	public static final String	SELECTOR_FRAME				= "selectFrame";
	public static final String	PRIMARY_PARAMETERS_FRAME	= "paramFrame";
	public static final String	STATS_FRAME					= "statsFrame";
	public static final String	EVENTS_FRAME				= "eventsFrame";
	public static final String	MARKERS_INFO_FRAME			= "MarkersInfoFrame";
	public static final String	ANALYSIS_FRAME				= "analysisFrame";
	public static final String	DETAILED_EVENTS_FRAME		= "DetailedEventsFrame";
	public static final String	HISTOGRAMM_FRAME			= "HistogrammFrame";
	public static final String	TD_FRAME			= "TimeDependanceFrame";
	public static final String	TD_TABLE			= "TimeDependanceTable";
	
	UIDefaults					frames;
	NoiseHistogrammPanel noiseHistogrammPanel;	

	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public PredictionMDIMain(final ApplicationContext aContext) {
		super(aContext, LangModelPrediction.getString("AppTitle"), 
				new PredictionMenuBar(aContext.getApplicationModel()), 
				new PredictionToolBar());

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				PredictionMDIMain.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
						PredictionMDIMain.this);
				aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
			}
		});
	}

	public PredictionMDIMain()
	{
		this(new ApplicationContext());
	}

	protected void initFrames() {
		this.frames = new UIDefaults();
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		
		this.frames.put(SELECTOR_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Level.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(PredictionMDIMain.this.dispatcher);
				desktopPane.add(selectFrame);
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
				desktopPane.add(paramFrame);
				PredictionMDIMain.this.tables.add(paramFrame);
				return paramFrame;
			}
		});

		this.frames.put(STATS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | STATS_FRAME", Level.FINEST);
				OverallStatsFrame statsFrame = new OverallStatsFrame(PredictionMDIMain.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return STATS_FRAME;
					}
				};
				desktopPane.add(statsFrame);
				PredictionMDIMain.this.tables.add(statsFrame);
				return statsFrame;
			}
		});

		super.setWindowArranger(new WindowArranger(PredictionMDIMain.this) {
					@Override
					public void arrange() {
						PredictionMDIMain f = (PredictionMDIMain) this.mainframe;

						int w = f.desktopPane.getSize().width;
						int h = f.desktopPane.getSize().height;
						int minh = Math.min(205, h / 4);

						JInternalFrame selectFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.SELECTOR_FRAME);
						JInternalFrame paramFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.PRIMARY_PARAMETERS_FRAME);
						JInternalFrame statsFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.STATS_FRAME);
						JInternalFrame eventsFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.EVENTS_FRAME);
						JInternalFrame detailedEvFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.DETAILED_EVENTS_FRAME);
						JInternalFrame analysisFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.ANALYSIS_FRAME);
						JInternalFrame mInfoFrame = (JInternalFrame) f.frames.get(PredictionMDIMain.MARKERS_INFO_FRAME);
						JInternalFrame dhf = (JInternalFrame) f.frames.get(PredictionMDIMain.HISTOGRAMM_FRAME);
						JInternalFrame tdf = (JInternalFrame) f.frames.get(PredictionMDIMain.TD_FRAME);
						JInternalFrame tdt = (JInternalFrame) f.frames.get(PredictionMDIMain.TD_TABLE);

						normalize(paramFrame);
						normalize(selectFrame);
						normalize(statsFrame);
						normalize(mInfoFrame);
						normalize(analysisFrame);
						normalize(eventsFrame);
						normalize(detailedEvFrame);
						normalize(dhf);
						normalize(tdf);
						normalize(tdt);
						
						paramFrame.setSize(w/6, minh);
						mInfoFrame.setSize(w/6, minh);
						selectFrame.setSize(w/6, minh);
						statsFrame.setSize(w/6, minh);
						tdt.setSize(w/3, minh);
						analysisFrame.setSize(w/2, h - 2 * minh);
						tdf.setSize(w/2, h - 2 * minh);
						eventsFrame.setSize(w/2, minh);
						detailedEvFrame.setSize(w/6, minh);
						dhf.setSize(w/3, minh);

						paramFrame.setLocation(w/6, 0);
						mInfoFrame.setLocation(w/2, 0);
						selectFrame.setLocation(0, 0);
						statsFrame.setLocation(w/3, 0);
						tdt.setLocation(2*w/3, 0);
						analysisFrame.setLocation(0, minh);
						tdf.setLocation(w/2, minh);
						eventsFrame.setLocation(0, h - minh);
						detailedEvFrame.setLocation(w/2, h - minh);
						dhf.setLocation(2*w/3, h - minh);					
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
				PredictionMDIMain.this.desktopPane.add(eventsFrame);
				PredictionMDIMain.this.tables.add(eventsFrame);
				return eventsFrame;
			}
		});

		this.frames.put(DETAILED_EVENTS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | DETAILED_EVENTS_FRAME", Level.FINEST);
				DetailedEventsFrame detailedEvFrame = new DetailedEventsFrame();
				desktopPane.add(detailedEvFrame);
				return detailedEvFrame;
			}
		});

		this.frames.put(ANALYSIS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Level.FINEST);
				AnalysisFrame analysisFrame = new AnalysisFrame(PredictionMDIMain.this.dispatcher)  {
					@Override
					public String getReportTitle() {
						return ANALYSIS_FRAME;
					}
				};
				desktopPane.add(analysisFrame);
				PredictionMDIMain.this.graphs.add(analysisFrame);
				return analysisFrame;
			}
		});

		this.frames.put(MARKERS_INFO_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | MARKERS_INFO_FRAME", Level.FINEST);
				MarkersInfoFrame mInfoFrame = new MarkersInfoFrame(PredictionMDIMain.this.dispatcher) ;
				desktopPane.add(mInfoFrame);
				return mInfoFrame;
			}
		});

		this.frames.put(HISTOGRAMM_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | HISTOGRAMM_FRAME", Level.FINEST);
				HistogrammFrame histogrammFrame = new HistogrammFrame(PredictionMDIMain.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return HISTOGRAMM_FRAME;
					}
				};
				PredictionMDIMain.this.desktopPane.add(histogrammFrame);
				PredictionMDIMain.this.graphs.add(histogrammFrame);
				return histogrammFrame;
			}
		});
		
		this.frames.put(TD_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TD_FRAME", Level.FINEST);
				TimeDependenceFrame timeDependenceFrame = new TimeDependenceFrame(PredictionMDIMain.this.dispatcher) {
					@Override
					public String getReportTitle() {
						return TD_FRAME;
					}
				};
				PredictionMDIMain.this.desktopPane.add(timeDependenceFrame);
				PredictionMDIMain.this.graphs.add(timeDependenceFrame);
				return timeDependenceFrame;
			}
		});
		
		this.frames.put(TD_TABLE, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TD_FRAME", Level.FINEST);
				TimeDependenceTable timeDependenceTable = new TimeDependenceTable() {
					@Override
					public String getReportTitle() {
						return TD_TABLE;
					}
				};
				PredictionMDIMain.this.desktopPane.add(timeDependenceTable);
				PredictionMDIMain.this.tables.add(timeDependenceTable);
				return timeDependenceTable;
			}
		});
	}

	@Override
	public void initModule() {
		super.initModule();
		initFrames();

		Heap.addBsHashListener(this);
		Heap.addPrimaryTraceListener(this);
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setCommand(PredictionApplicationModel.MENU_VIEW_DATA_LOAD, new LoadTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION, new CountPredictedReflectogramm(this.aContext));
		aModel.setCommand(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION,  new SavePredictionCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_FILE_CLOSE, new FileCloseCommand());

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, DestinationModules.PREDICTION);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);
		aModel.setCommand(PredictionApplicationModel.MENU_REPORT_CREATE, rc);

		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_TRACESELECTOR, this.getLazyCommand(SELECTOR_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, this.getLazyCommand(PRIMARY_PARAMETERS_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_OVERALLSTATS, this.getLazyCommand(STATS_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_EVENTS, this.getLazyCommand(EVENTS_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_ANALYSIS, this.getLazyCommand(ANALYSIS_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_MARKERSINFO, this.getLazyCommand(MARKERS_INFO_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_HISTOGRAMM, this.getLazyCommand(HISTOGRAMM_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_TD_FRAME, this.getLazyCommand(TD_FRAME));
		aModel.setCommand(PredictionApplicationModel.MENU_WINDOW_TD_TABLE, this.getLazyCommand(TD_TABLE));
		
		setDefaultModel(aModel);

		aModel.fireModelChanged("");
	}
	
	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = PredictionMDIMain.this.frames.get(key);
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
		aModel.setEnabled(PredictionApplicationModel.MENU_REPORT, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);
	}
	
	@Override
	public void loggedIn() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
//		aModel.setEnabled("menuNetStudy", true);
		
		aModel.setEnabled(PredictionApplicationModel.MENU_VIEW_DATA_LOAD, true);
		
		aModel.fireModelChanged("");
	}

	@Override
	public void loggedOut() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		setDefaultModel(aModel);
		// TODO hide frames
		aModel.fireModelChanged("");
	}

	public void bsHashAdded(String key) {
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			TimeDependenceTable tdTable = (TimeDependenceTable) PredictionMDIMain.this.frames.get(PredictionMDIMain.TD_TABLE);
			tdTable.setVisible(true);
			
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_REPORT_CREATE, true);
			aModel.fireModelChanged();
		} else if (key.equals(Heap.MODELED_TRACE_KEY)) {
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION, true);
			aModel.fireModelChanged();
		}
	}
	
	public void bsHashRemoved(String key) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		ScalableFrame analysisFrame = (ScalableFrame)this.frames.get(ANALYSIS_FRAME);
		analysisFrame.removeGraph(key);
		analysisFrame.updScales();
		
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
			aModel.fireModelChanged();
		}
	}
	
	public void bsHashRemovedAll() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.setEnabled(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION, false);
		aModel.setEnabled(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
		aModel.setEnabled(PredictionApplicationModel.MENU_REPORT_CREATE, false);
		aModel.fireModelChanged();
	}
	
	public void currentTraceChanged(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
		} else {
			aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, true);
			setActiveRefId(id);
		}
		aModel.fireModelChanged();
	}
	
	public void primaryTraceCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (true) // XXX: if(isCreated)
		{
			final boolean saveFilePermitted = true;
//					PermissionManager.isPermitted(Operation.SAVE_TRACE_FILE);
			aModel.setEnabled("menuFileSave", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAll", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAs", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAsText", saveFilePermitted);
			aModel.setEnabled("menuFileClose", true);
			aModel.setEnabled("menuFileAddCompare", true);
//					PermissionManager.isPermitted(Operation.READ_TRACE_FILE));

			aModel.setEnabled("menuViewModelSave", true);
			
			aModel.setEnabled("menuTraceClose", true);
			aModel.setEnabled("menuTraceAddCompare", true);
//					PermissionManager.isPermitted(Operation.LOAD_TRACE));
			
			aModel.setEnabled("menuReportCreate", true);

			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_ANALYSIS, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_EVENTS, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_HISTOGRAMM, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_MARKERSINFO, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_OVERALLSTATS, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_TD_FRAME, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_TD_TABLE, true);
			aModel.setEnabled(PredictionApplicationModel.MENU_WINDOW_TRACESELECTOR, true);
			aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		}
		aModel.fireModelChanged();
	}
	
	public void primaryTraceRemoved() {
		// TODO Auto-generated method stub
		
	}

	void setActiveRefId (String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
	}
}
