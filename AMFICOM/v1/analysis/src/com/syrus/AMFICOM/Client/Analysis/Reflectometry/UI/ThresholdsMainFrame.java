
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

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
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.report.DestinationModules;

public class ThresholdsMainFrame extends AbstractMainFrame
implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener {
	private static final long serialVersionUID = 2086348259538964334L;

	ClientAnalysisManager		aManager			= new ClientAnalysisManager();

	ThresholdsSelectionFrame	thresholdsSelectionFrame;
	TraceSelectorFrame			selectFrame;
	PrimaryParametersFrame		paramFrame;
	OverallStatsFrame			statsFrame;
	EventsFrame					eventsFrame;
	DetailedEventsFrame			detailedEvFrame;
	ThresholdsFrame				thresholdsFrame;
	MarkersInfoFrame			mInfoFrame;
	AnalysisSelectionFrame		anaSelectFrame;
	PathElementsFrame				analysisFrame;

	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public ThresholdsMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("ThresholdsTitle"), new AnalyseMainMenuBar(aContext
			.getApplicationModel()), new AnalyseMainToolBar());

		this.setWindowArranger( new WindowArranger(this) {

			@Override
			public void arrange() {
				ThresholdsMainFrame f = (ThresholdsMainFrame) this.mainframe;

				int w = f.desktopPane.getSize().width;
				int h = f.desktopPane.getSize().height;
				int minh = Math.min(205, h / 4);

				normalize(f.paramFrame);
				normalize(f.selectFrame);
				normalize(f.statsFrame);
				normalize(f.mInfoFrame);
				normalize(f.analysisFrame);
				normalize(f.thresholdsFrame);
				normalize(f.eventsFrame);
				normalize(f.detailedEvFrame);
				normalize(f.anaSelectFrame);
				normalize(f.thresholdsSelectionFrame);

				f.paramFrame.setSize(w / 6, minh);
				f.selectFrame.setSize(w / 6, minh);
				f.statsFrame.setSize(w / 6, minh);
				f.mInfoFrame.setSize(w / 6, minh);
				f.analysisFrame.setSize(2 * w / 3, h - 2 * minh);
				f.thresholdsFrame.setSize(w / 3, h - 2 * minh);
				f.eventsFrame.setSize(w / 2, minh);
				f.detailedEvFrame.setSize(w / 6, minh);
				f.anaSelectFrame.setSize(w / 3, minh);
				f.thresholdsSelectionFrame.setSize(w / 3, minh);

				f.paramFrame.setLocation(w / 6, 0);
				f.selectFrame.setLocation(0, 0);
				f.statsFrame.setLocation(w / 3, 0);
				f.mInfoFrame.setLocation(w / 2, 0);
				f.analysisFrame.setLocation(0, minh);
				f.thresholdsFrame.setLocation(f.analysisFrame.getWidth(), minh);

				f.anaSelectFrame.setLocation(2 * w / 3, 0);

				f.eventsFrame.setLocation(0, minh + f.analysisFrame.getHeight());
				f.detailedEvFrame.setLocation(f.eventsFrame.getWidth(), minh + f.analysisFrame.getHeight());
				f.thresholdsSelectionFrame.setLocation(f.eventsFrame.getWidth() + f.detailedEvFrame.getWidth(), minh
						+ f.analysisFrame.getHeight());
			}
		} 
			);

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				ThresholdsMainFrame.this.desktopPane.setPreferredSize(ThresholdsMainFrame.this.desktopPane.getSize());
				ThresholdsMainFrame.this.windowArranger.arrange();
				ThresholdsMainFrame.this.analysisFrame.grabFocus();
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
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		
		this.selectFrame = new TraceSelectorFrame(this.dispatcher);
		this.desktopPane.add(this.selectFrame);

		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		
		this.paramFrame = new PrimaryParametersFrame();
		this.desktopPane.add(this.paramFrame);
		this.tables.add(this.paramFrame);

		this.statsFrame = new OverallStatsFrame(this.dispatcher);
		this.desktopPane.add(this.statsFrame);
		this.tables.add(this.statsFrame);

		this.eventsFrame = new EventsFrame(this.aContext, true);
		this.desktopPane.add(this.eventsFrame);
		this.tables.add(this.eventsFrame);

		this.thresholdsSelectionFrame = new ThresholdsSelectionFrame(this.dispatcher);
		this.desktopPane.add(this.thresholdsSelectionFrame);
		this.tables.add(this.thresholdsSelectionFrame);

		this.detailedEvFrame = new DetailedEventsFrame();
		this.desktopPane.add(this.detailedEvFrame);

		this.thresholdsFrame = new ThresholdsFrame(this.dispatcher);
		this.desktopPane.add(this.thresholdsFrame);
		this.graphs.add(this.thresholdsFrame);

		this.mInfoFrame = new MarkersInfoFrame(this.dispatcher);
		this.desktopPane.add(this.mInfoFrame);

		this.anaSelectFrame = new AnalysisSelectionFrame(this.aContext);
		this.desktopPane.add(this.anaSelectFrame);
		this.tables.add(this.anaSelectFrame);

		this.analysisFrame = new PathElementsFrame(this.aContext, this.dispatcher);
		this.desktopPane.add(this.analysisFrame);
		this.graphs.add(this.analysisFrame);
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
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE, new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, new FileRemoveCommand(null, this.aContext));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY, new MakeCurrentTracePrimaryCommand());

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, 
				DestinationModules.EVALUATION);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);

		aModel.setCommand(AnalyseApplicationModel.MENU_REPORT_CREATE, rc);

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, new SavePathElementsCommand(this.aContext));
		
		// XXX temporary not allowed, uncomment after create frames like AnalyseMainFrame 
		aModel.setVisible(AnalyseApplicationModel.MENU_REPORT, false);

		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, new ShowWindowCommand(this.selectFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, new ShowWindowCommand(this.paramFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS, new ShowWindowCommand(this.statsFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_EVENTS, new ShowWindowCommand(this.eventsFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, new ShowWindowCommand(this.analysisFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO, new ShowWindowCommand(this.mInfoFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION, new ShowWindowCommand(this.anaSelectFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS, new ShowWindowCommand(this.detailedEvFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS, new ShowWindowCommand(this.thresholdsFrame));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION, new ShowWindowCommand(this.thresholdsSelectionFrame));

		aModel.setCommand(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, new CheckMismatchCommand());

		aModel.fireModelChanged("");
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

			this.thresholdsSelectionFrame.setVisible(true);
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
		// thresholdsFrame.setVisible(false);
		this.thresholdsSelectionFrame.setVisible(false);
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
