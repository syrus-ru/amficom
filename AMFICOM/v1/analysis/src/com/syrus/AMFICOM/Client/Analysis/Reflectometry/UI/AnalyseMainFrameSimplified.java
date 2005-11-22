
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
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.report.DestinationModules;

public class AnalyseMainFrameSimplified extends AbstractMainFrame implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener {

	// FIXME: saa: security bypass in all references to this fiels
	public static final boolean	DEBUG		= System.getProperty("amficom.debug.nonstrict", "false").equals("true");

	ClientAnalysisManager		aManager	= new ClientAnalysisManager();

	TraceSelectorFrame			selectFrame;
	PrimaryParametersFrame		paramFrame;
	OverallStatsFrame			statsFrame;
	EventsFrame					eventsFrame;
	AnalysisFrame				analysisFrame;
	MarkersInfoFrame			mInfoFrame;
	AnalysisSelectionFrame		anaSelectFrame;

	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public AnalyseMainFrameSimplified(ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("AnalyseTitle"), new AnalyseMainMenuBar(aContext
				.getApplicationModel()), new AnalyseMainToolBar(false));

		this.setWindowArranger(new WindowArranger(this) {

			@Override
			public void arrange() {
				AnalyseMainFrameSimplified f = (AnalyseMainFrameSimplified) this.mainframe;

				int w = f.desktopPane.getSize().width;
				int h = f.desktopPane.getSize().height;

				normalize(f.paramFrame);
				normalize(f.selectFrame);
				normalize(f.statsFrame);
				normalize(f.mInfoFrame);
				normalize(f.analysisFrame);
				normalize(f.eventsFrame);
				normalize(f.anaSelectFrame);

				f.paramFrame.setSize(w / 6, h / 4);
				f.statsFrame.setSize(w / 6, h / 4);
				f.mInfoFrame.setSize(w / 6, h / 4);
				f.selectFrame.setSize(w / 6, h - f.mInfoFrame.getHeight() - f.statsFrame.getHeight()
						- f.paramFrame.getHeight());
				f.analysisFrame.setSize(5 * w / 6, 3 * h / 4);
				f.eventsFrame.setSize(w / 2, h / 4);
				f.anaSelectFrame.setSize(w / 3, h / 4);

				f.selectFrame.setLocation(5 * w / 6, 0);
				f.paramFrame.setLocation(5 * w / 6, f.selectFrame.getHeight());
				f.mInfoFrame.setLocation(5 * w / 6, f.selectFrame.getHeight() + f.paramFrame.getHeight());
				f.statsFrame.setLocation(5 * w / 6, f.selectFrame.getHeight() + f.mInfoFrame.getHeight()
						+ f.paramFrame.getHeight());
				f.analysisFrame.setLocation(0, 0);
				f.anaSelectFrame.setLocation(0, f.analysisFrame.getHeight());
				f.eventsFrame.setLocation(w / 3, f.analysisFrame.getHeight());
			}
		});

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				// init_module();
				desktopPane.setPreferredSize(desktopPane.getSize());
				AnalyseMainFrameSimplified.this.windowArranger.arrange();

				AnalyseMainFrameSimplified.this.analysisFrame.grabFocus();
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
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		
		this.selectFrame = new TraceSelectorFrame(super.dispatcher);
		this.desktopPane.add(this.selectFrame);
		
		this.paramFrame = new PrimaryParametersFrame();
		this.desktopPane.add(this.paramFrame);
		this.tables.add(this.paramFrame);
		
		this.statsFrame = new OverallStatsFrame(super.dispatcher);
		this.desktopPane.add(this.statsFrame);
		this.tables.add(this.statsFrame);
		
		this.eventsFrame = new EventsFrame(this.aContext, false);
		this.desktopPane.add(this.eventsFrame);
		this.tables.add(this.eventsFrame);
		
		this.analysisFrame = new AnalysisFrame(super.dispatcher);
		this.desktopPane.add(this.analysisFrame);
		this.graphs.add(this.analysisFrame);
		
		this.mInfoFrame = new MarkersInfoFrame(super.dispatcher);
		this.desktopPane.add(this.mInfoFrame);
		
		this.anaSelectFrame = new AnalysisSelectionFrame(this.aContext);
		this.desktopPane.add(this.anaSelectFrame);
		this.tables.add(this.anaSelectFrame);
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

		aModel.setCommand("menuFileOpen", new FileOpenCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(this.aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand());
		aModel.setCommand("menuFileAddCompare", new FileAddCommand(this.aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(null, this.aContext));

		//aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(this.aContext));
		aModel.setCommand("menuTraceDownload", new LoadTraceFromDatabaseCommand(super.dispatcher, this.aContext));
//		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand());
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(null, this.aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand());
//		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand());
//		aModel.setCommand("menuTraceReferenceSet", new TraceOpenReferenceCommand(this.aContext));
//		aModel.setCommand("menuTraceReferenceMakeCurrent", new TraceMakeCurrentCommand(this.aContext));
//		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(this.aContext));

		aModel.setCommand("menuMakeCurrentTracePrimary", new MakeCurrentTracePrimaryCommand());

		// XXX temporary not allowed, uncomment after create frames like AnalyseMainFrame 
		aModel.setVisible("menuReport", false);
		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, 
				DestinationModules.ANALYSIS);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);
		
		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(this.windowArranger));
		aModel.setCommand("menuWindowTraceSelector", new ShowWindowCommand(this.selectFrame));
		aModel.setCommand("menuWindowPrimaryParameters", new ShowWindowCommand(this.paramFrame));
		aModel.setCommand("menuWindowOverallStats", new ShowWindowCommand(this.statsFrame));
		aModel.setCommand("menuWindowEvents", new ShowWindowCommand(this.eventsFrame));
		aModel.setCommand("menuWindowAnalysis", new ShowWindowCommand(this.analysisFrame));
		aModel.setCommand("menuWindowMarkersInfo", new ShowWindowCommand(this.mInfoFrame));
		aModel.setCommand("menuWindowAnalysisSelection", new ShowWindowCommand(this.anaSelectFrame));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");
	}

	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		aModel.setEnabled("menuFile", true);
		aModel.setEnabled("menuTrace", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setEnabled("menuFileOpen", AnalyseMainFrameSimplified.DEBUG);

		aModel.setVisible("menuTestSetup", false);
//		aModel.setVisible("menuNetStudy", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);
		aModel.setVisible("menuWindowNoiseFrame", false);
		aModel.setVisible("menuWindowFilteredFrame", false);
		aModel.setVisible("menuWindowDetailedEvents", false);
		aModel.setVisible("menuWindowDerivHistoFrame", false);
	}

	@Override
	public void loggedIn() {
		PermissionManager.resetCache();

		final ApplicationModel aModel = this.aContext.getApplicationModel();

		final boolean readFilePermitted =
			PermissionManager.isPermitted(Operation.READ_TRACE_FILE);
		aModel.setEnabled("menuFileOpen", readFilePermitted);
		aModel.setEnabled("menuFileOpenAs", readFilePermitted);
		aModel.setEnabled("menuFileOpenAsBellcore", readFilePermitted);
		aModel.setEnabled("menuFileOpenAsWavetek", readFilePermitted);
		aModel.setEnabled("menuTraceDownload",
				PermissionManager.isPermitted(Operation.LOAD_TRACE));
		aModel.setEnabled("menuHelpAbout", true);
		aModel.fireModelChanged("");

	}

	@Override
	public void loggedOut() {
		PermissionManager.resetCache();

		final ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuFileOpen", false);
		aModel.setEnabled("menuFileOpenAs", false);
		aModel.setEnabled("menuFileOpenAsBellcore", false);
		aModel.setEnabled("menuFileOpenAsWavetek", false);
		aModel.setEnabled("menuFileAddCompare", false);
		aModel.setEnabled("menuTraceDownload", false);
		aModel.setEnabled("menuTraceAddCompare", false);
//		aModel.setEnabled("menuTraceDownloadEtalon", false);

//		aModel.setEnabled("menuAnalyseUpload", false);
//		aModel.setEnabled("menuSaveEtalon", false);
//		aModel.setEnabled("menuSaveThresholds", false);
//		aModel.setEnabled("menuAnalyseSaveCriteria", false);
		aModel.setEnabled("menuSaveTestSetup", false);
		aModel.setEnabled("menuSaveTestSetupAs", false);
		aModel.setEnabled("menuHelpAbout", false);
		aModel.fireModelChanged("");		
	}
	
	void setActiveRefId(String id) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId", id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
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
			aModel.setEnabled("menuFileSave", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAll", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAs", saveFilePermitted);
			aModel.setEnabled("menuFileSaveAsText", saveFilePermitted);
			aModel.setEnabled("menuFileClose", true);
			aModel.setEnabled("menuFileAddCompare",
					PermissionManager.isPermitted(Operation.READ_TRACE_FILE));

//			aModel.setEnabled("menuTraceDownloadEtalon", true);
//			aModel.setEnabled("menuTraceUpload", true);
			aModel.setEnabled("menuTraceClose", true);
//			aModel.setEnabled("menuTraceCurrentMakeReference", true);
//			aModel.setEnabled("menuTraceReference", true);
//			aModel.setEnabled("menuTraceCurrent", true);
			aModel.setEnabled("menuTraceAddCompare",
					PermissionManager.isPermitted(Operation.LOAD_TRACE));
//			aModel.setEnabled("menuAnalyseUpload", true);

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
		} else if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		} else {
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.fireModelChanged(new String[] { "menuTraceRemoveCompare", "menuTraceRemoveCompare"});
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
			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		}
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuTraceRemoveCompare", "menuFileRemoveCompare"});
		}
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

//		aModel.setEnabled("menuTraceDownloadEtalon", false);
		aModel.setEnabled("menuTraceClose", false);
//		aModel.setEnabled("menuTraceCloseEtalon", false);
//		aModel.setEnabled("menuTraceCurrentMakeReference", false);
		aModel.setEnabled("menuTraceAddCompare", false);
		aModel.setEnabled("menuFileRemoveCompare", false);
		aModel.setEnabled("menuTraceRemoveCompare", false);
//		aModel.setEnabled("menuTraceUpload", false);
//		aModel.setEnabled("menuTraceReference", false);
//		aModel.setEnabled("menuTraceCurrent", false);
//		aModel.setEnabled("menuAnalyseUpload", false);

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
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
		} else {
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.setEnabled("menuTraceRemoveCompare", true);
			setActiveRefId(id);
		}
		aModel.setEnabled("menuMakeCurrentTracePrimary", Heap.isTraceSecondary(id));
		aModel.fireModelChanged(new String[] { "menuFileRemoveCompare", "menuTraceRemoveCompare", "menuMakeCurrentTracePrimary"});
	}
}
