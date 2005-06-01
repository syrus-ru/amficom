
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
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileAddCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsBellcoreCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenAsWavetekCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileRemoveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveAsTextCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadEtalonCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadTraceFromDatabaseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.OptionsSetColorsCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.RemoveEtalonCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.SaveAnalysisCommand;
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
import com.syrus.AMFICOM.client.model.ExitCommand;
import com.syrus.AMFICOM.client.model.OpenSessionCommand;
import com.syrus.AMFICOM.client.model.SessionChangePasswordCommand;
import com.syrus.AMFICOM.client.model.SessionCloseCommand;
import com.syrus.AMFICOM.client.model.SessionConnectionCommand;
import com.syrus.AMFICOM.client.model.SessionDomainCommand;
import com.syrus.AMFICOM.client.model.SessionOptionsCommand;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.io.BellcoreStructure;

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

	List						tables;
	List						graphs;

	public AnalyseMainFrameSimplified(ApplicationContext aContext) {
		super(aContext, LangModelAnalyse.getString("AnalyseTitle"), new AnalyseMainMenuBar(aContext
				.getApplicationModel()), new AnalyseMainToolBar());

		this.setWindowArranger(new WindowArranger(this) {

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

			public void componentShown(ComponentEvent e) {
				// init_module();
				desktopPane.setPreferredSize(desktopPane.getSize());
				AnalyseMainFrameSimplified.this.windowArranger.arrange();

				AnalyseMainFrameSimplified.this.analysisFrame.grabFocus();
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				AnalyseMainFrameSimplified.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
					AnalyseMainFrameSimplified.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE,
					AnalyseMainFrameSimplified.this);
				AnalyseMainFrameSimplified.this.aContext.getApplicationModel().getCommand("menuExit").execute();

			}
		});

		if (this.tables == null) {
			this.tables = new LinkedList();
		}
		if (this.graphs == null) {
			this.graphs = new LinkedList();
		}

		this.selectFrame = new TraceSelectorFrame(super.dispatcher);
		this.desktopPane.add(this.selectFrame);

		this.paramFrame = new PrimaryParametersFrame();
		this.desktopPane.add(this.paramFrame);
		this.tables.add(this.paramFrame);

		this.statsFrame = new OverallStatsFrame(super.dispatcher);
		this.desktopPane.add(this.statsFrame);
		this.tables.add(this.statsFrame);

		this.eventsFrame = new EventsFrame();
		this.desktopPane.add(this.eventsFrame);
		this.tables.add(this.eventsFrame);

		this.analysisFrame = new AnalysisFrame(super.dispatcher);
		this.desktopPane.add(this.analysisFrame);
		this.graphs.add(this.analysisFrame);

		this.mInfoFrame = new MarkersInfoFrame(super.dispatcher);
		this.desktopPane.add(this.mInfoFrame);

		this.anaSelectFrame = new AnalysisSelectionFrame(aContext);
		this.desktopPane.add(this.anaSelectFrame);
		this.tables.add(this.anaSelectFrame);
	}

	public AnalyseMainFrameSimplified() {
		this(new ApplicationContext());
	}

	public void initModule() {
		super.initModule();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		Heap.addBsHashListener(this);
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

		aModel.setCommand("menuFileOpen", new FileOpenCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(this.aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(this.aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuFileAddCompare", new FileAddCommand(this.aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(null, this.aContext));

		aModel.setCommand("menuAnalyseUpload", new SaveAnalysisCommand(this.aContext));
		aModel.setCommand("menuTraceDownload", new LoadTraceFromDatabaseCommand(super.dispatcher, this.aContext));
		aModel.setCommand("menuTraceDownloadEtalon", new LoadEtalonCommand());
		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(this.aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(null, this.aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand(this.aContext));
		aModel.setCommand("menuTraceCloseEtalon", new RemoveEtalonCommand());
		aModel.setCommand("menuTraceReferenceSet", new TraceOpenReferenceCommand(this.aContext));
		aModel.setCommand("menuTraceReferenceMakeCurrent", new TraceMakeCurrentCommand(this.aContext));
		aModel.setCommand("menuOptionsColor", new OptionsSetColorsCommand(this.aContext));

		if (this.tables == null) {
			this.tables = new LinkedList();
		}
		if (this.graphs == null) {
			this.graphs = new LinkedList();
		}

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext);
		for (Iterator it = this.tables.iterator(); it.hasNext();) {
			rc.setParameter(CreateAnalysisReportCommand.TABLE, it.next());
		}
		for (Iterator it = this.graphs.iterator(); it.hasNext();) {
			rc.setParameter(CreateAnalysisReportCommand.PANEL, it.next());
		}
		// rc.setParameter(CreateAnalysisReportCommand.TYPE,
		// ReportTemplate.rtt_Analysis);
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

	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		aModel.setEnabled("menuFile", true);
		aModel.setEnabled("menuTrace", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setEnabled("menuFileOpen", AnalyseMainFrameSimplified.DEBUG);

		aModel.setVisible("menuTestSetup", false);
		aModel.setVisible("menuNetStudy", false);
		aModel.setVisible("menuWindowThresholdsSelection", false);
		aModel.setVisible("menuWindowThresholds", false);
		aModel.setVisible("menuWindowNoiseFrame", false);
		aModel.setVisible("menuWindowFilteredFrame", false);
		aModel.setVisible("menuWindowDetailedEvents", false);
		aModel.setVisible("menuWindowDerivHistoFrame", false);
	}

	public void setDomainSelected() {
		super.setDomainSelected();
		ApplicationModel aModel = this.aContext.getApplicationModel();
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
		aModel.setEnabled("menuHelpAbout", false);
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
			// ConnectionInterface.setActiveConnection(this.aContext.getConnectionInterface());
			// SessionInterface.setActiveSession(this.aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// ColorManager.saveIni();
			this.aManager.saveIni();
			super.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			this.aContext.getApplicationModel().getCommand("menuExit").execute();
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
		ApplicationModel aModel = this.aContext.getApplicationModel();
		String id = key;
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
		Heap.updateCurrentTraceWhenBSRemoved();
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
