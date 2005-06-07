
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Heap;
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
import com.syrus.AMFICOM.Client.General.Command.Analysis.OptionsSetColorsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.RemoveEtalonCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveTestSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.TraceMakeCurrentCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.TraceOpenReferenceCommand;
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
import com.syrus.io.BellcoreStructure;

public class ThresholdsMainFrame extends AbstractMainFrame implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener {


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
	AnalysisFrame				analysisFrame;

	List					tables;
	List					graphs;

	public ThresholdsMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("ThresholdsTitle"), new AnalyseMainMenuBar(aContext
			.getApplicationModel()), new AnalyseMainToolBar());	
		
		this.setWindowArranger( new WindowArranger(this) {

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
			/* (non-Javadoc)
			 * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
			 */
			public void componentShown(ComponentEvent e) {
				initModule();
				ThresholdsMainFrame.this.desktopPane.setPreferredSize(ThresholdsMainFrame.this.desktopPane.getSize());
				ThresholdsMainFrame.this.windowArranger.arrange();
				ThresholdsMainFrame.this.analysisFrame.grabFocus();
			}
		}
			);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				aContext.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ThresholdsMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ThresholdsMainFrame.this);
				ThresholdsMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
			}
		});

		this.selectFrame = new TraceSelectorFrame(this.dispatcher);
		this.desktopPane.add(this.selectFrame);

		this.tables = new LinkedList();
		this.graphs = new LinkedList();
		
		this.paramFrame = new PrimaryParametersFrame();
		this.desktopPane.add(this.paramFrame);
		this.tables.add(this.paramFrame);

		this.statsFrame = new OverallStatsFrame(this.dispatcher);
		this.desktopPane.add(this.statsFrame);
		this.tables.add(this.statsFrame);

		this.eventsFrame = new EventsFrame(aContext);
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

		this.anaSelectFrame = new AnalysisSelectionFrame(aContext);
		this.desktopPane.add(this.anaSelectFrame);
		this.tables.add(this.anaSelectFrame);

		this.analysisFrame = new AnalysisFrame(this.dispatcher);
		this.desktopPane.add(this.analysisFrame);
		this.graphs.add(this.analysisFrame);
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		CreateAnalysisReportCommand rc = (CreateAnalysisReportCommand) aModel.getCommand("menuReportCreate");
		for (Iterator it = this.tables.iterator(); it.hasNext();) {
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		}
		for (Iterator it = this.graphs.iterator(); it.hasNext();) {
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		}

	}

	public ThresholdsMainFrame() {
		this(new ApplicationContext());
	}

	protected void initModule() {
		super.initModule();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		
		aModel.setCommand("menuFileOpen", new FileOpenCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(this.aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuFileAddCompare", new FileAddCommand(this.aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(null, this.aContext));

		aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(this.aContext));
		aModel.setCommand("menuSaveTestSetup", new SaveTestSetupCommand(this.aContext, SaveTestSetupCommand.CRITERIA
				+ SaveTestSetupCommand.ETALON));// +
												// SaveTestSetupCommand.THRESHOLDS));
		aModel.setCommand("menuSaveTestSetupAs", new SaveTestSetupAsCommand(this.aContext, SaveTestSetupCommand.CRITERIA
				+ SaveTestSetupCommand.ETALON));// +
												// SaveTestSetupCommand.THRESHOLDS));
		aModel.setCommand("menuCreateTestSetup", new CreateTestSetupCommand(this.aContext));
		aModel.setCommand("menuLoadTestSetup", new LoadTestSetupCommand(this.aContext));

		aModel.setCommand("menuTraceDownload", new LoadTraceFromDatabaseCommand(this.dispatcher, this.aContext));
		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand());
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(null, this.aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand());
		aModel.setCommand("menuTraceReferenceSet", new TraceOpenReferenceCommand(this.aContext));
		aModel.setCommand("menuTraceReferenceMakeCurrent", new TraceMakeCurrentCommand(this.aContext));
		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(this.aContext));

		// rc.setParameter(CreateAnalysisReportCommand.TYPE,
		// ReportTemplate.rtt_Evaluation);
		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext);
		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(super.windowArranger));
		aModel.setCommand("menuWindowTraceSelector", new ShowWindowCommand(this.selectFrame));
		aModel.setCommand("menuWindowPrimaryParameters", new ShowWindowCommand(this.paramFrame));
		aModel.setCommand("menuWindowOverallStats", new ShowWindowCommand(this.statsFrame));
		aModel.setCommand("menuWindowEvents", new ShowWindowCommand(this.eventsFrame));
		aModel.setCommand("menuWindowDetailedEvents", new ShowWindowCommand(this.detailedEvFrame));
		aModel.setCommand("menuWindowAnalysis", new ShowWindowCommand(this.analysisFrame));
		aModel.setCommand("menuWindowMarkersInfo", new ShowWindowCommand(this.mInfoFrame));
		aModel.setCommand("menuWindowAnalysisSelection", new ShowWindowCommand(this.anaSelectFrame));
		aModel.setCommand("menuWindowThresholds", new ShowWindowCommand(this.thresholdsFrame));
		aModel.setCommand("menuWindowThresholdsSelection", new ShowWindowCommand(this.thresholdsSelectionFrame));

		aModel.fireModelChanged("");
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

		aModel.setVisible("menuAnalyseUpload", false);
		aModel.setVisible("menuAnalyseSaveCriteria", false);
		aModel.setVisible("menuSaveEtalon", false);
		aModel.setVisible("menuSaveThresholds", false);
		aModel.setVisible("menuWindowNoiseFrame", false);
		aModel.setVisible("menuWindowFilteredFrame", false);
		aModel.setVisible("menuWindowDerivHistoFrame", false);
		aModel.setVisible("menuNetStudy", false);
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
		aModel.setEnabled("menuSaveTestSetupAll", false);

		aModel.fireModelChanged("");
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
			this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
			return;
		}
		super.processWindowEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener#bsHashAdded(java.lang.String,
	 *      com.syrus.io.BellcoreStructure)
	 */
	public void bsHashAdded(String key,
							BellcoreStructure bs) {
		String id = key;
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
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

			aModel.setEnabled("menuCreateTestSetup", true);
			aModel.setEnabled("menuSaveTestSetup", true);
			aModel.setEnabled("menuSaveTestSetupAs", true);
			aModel.setEnabled("menuLoadTestSetup", true);
			aModel.setEnabled("menuAnalyseSaveCriteria", true);
			aModel.setEnabled("menuSaveEtalon", true);
			aModel.setEnabled("menuSaveThresholds", true);

			aModel.setEnabled("menuReportCreate", true);

			aModel.setEnabled("menuWindowArrange", true);
			aModel.setEnabled("menuWindowTraceSelector", true);
			aModel.setEnabled("menuWindowPrimaryParameters", true);
			aModel.setEnabled("menuWindowOverallStats", true);
			aModel.setEnabled("menuWindowEvents", true);
			aModel.setEnabled("menuWindowDetailedEvents", true);
			aModel.setEnabled("menuWindowAnalysis", true);
			aModel.setEnabled("menuWindowMarkersInfo", true);
			aModel.setEnabled("menuWindowAnalysisSelection", true);
			aModel.setEnabled("menuWindowThresholdsSelection", true);
			aModel.setEnabled("menuWindowThresholds", true);

			aModel.fireModelChanged("");

			this.thresholdsSelectionFrame.setVisible(true);
		} else if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
			aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		} else {
			aModel.setEnabled("menuFileRemoveCompare", true);
			aModel.setEnabled("menuTraceRemoveCompare", true);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare", "menuTraceRemoveCompare"});
		}
	}

	public void bsHashRemoved(String key) {
		String id = key;
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (id.equals(Heap.REFERENCE_TRACE_KEY)) {
			aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
			aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
		}
		Heap.updateCurrentTraceWhenBSRemoved();
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuFileRemoveCompare", "menuTraceRemoveCompare"});
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

		aModel.setEnabled("menuCreateTestSetup", false);
		aModel.setEnabled("menuSaveTestSetup", false);
		aModel.setEnabled("menuSaveTestSetupAs", false);
		aModel.setEnabled("menuLoadTestSetup", false);
		aModel.setEnabled("menuAnalyseSaveCriteria", false);
		aModel.setEnabled("menuSaveEtalon", false);
		aModel.setEnabled("menuSaveThresholds", false);

		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowArrange", false);
		aModel.setEnabled("menuWindowTraceSelector", false);
		aModel.setEnabled("menuWindowPrimaryParameters", false);
		aModel.setEnabled("menuWindowOverallStats", false);
		aModel.setEnabled("menuWindowEvents", false);
		aModel.setEnabled("menuWindowDetailedEvents", false);
		aModel.setEnabled("menuWindowAnalysis", false);
		aModel.setEnabled("menuWindowMarkersInfo", false);
		aModel.setEnabled("menuWindowAnalysisSelection", false);
		aModel.setEnabled("menuWindowThresholdsSelection", false);
		aModel.setEnabled("menuWindowThresholds", false);

		aModel.fireModelChanged("");
		// thresholdsFrame.setVisible(false);
		this.thresholdsSelectionFrame.setVisible(false);
	}

	public void etalonMTMCUpdated() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled("menuTraceCloseEtalon", true);
		aModel.fireModelChanged(new String[] { "menuTraceCloseEtalon"});
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
