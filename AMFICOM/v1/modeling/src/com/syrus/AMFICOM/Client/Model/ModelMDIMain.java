
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Model;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_ANALYSIS_MAIN;
import static com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys.FRAME_TRACE_SELECTOR;
import static com.syrus.AMFICOM.resource.ModelResourceKeys.FRAME_TRANS_DATA;
import static com.syrus.AMFICOM.resource.ModelResourceKeys.FRAME_TREE;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ReportTable;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScalableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleGraphPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleResizableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TraceSelectorFrame;
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
import com.syrus.AMFICOM.Client.General.Command.Analysis.LoadModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.SaveModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Model.SchemeOpenCommand;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.OpenLinkedMapViewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapPropertiesEventHandler;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.model.MapSurveyApplicationModelFactory;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeAlarmHandler;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeUI;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class ModelMDIMain extends AbstractMainFrame implements BsHashChangeListener, 
	PrimaryTraceListener,	PrimaryRefAnalysisListener, CurrentTraceChangeListener {
	private static final long serialVersionUID = 2212105265261102560L;

	ClientAnalysisManager		aManager					= new ClientAnalysisManager();
	
	SchemeTabbedPane schemePane;
	MapMarkersLayeredPanel layeredPanel;
	
	public UIDefaults					frames;
	
	List<ReportTable>					tables;
	List<SimpleResizableFrame>					graphs;

	public ModelMDIMain(final ApplicationContext aContext) {
		super(aContext, LangModelModel.getString("AppTitle"), //$NON-NLS-1$
				new ModelMenuBar(aContext.getApplicationModel()),
				new ModelMainToolBar());
		
		this.addComponentListener(new ComponentAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentShown(ComponentEvent e) {
				ModelMDIMain.this.desktopPane.setPreferredSize(ModelMDIMain.this.desktopPane.getSize());
			}
		}
		);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				ModelMDIMain.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
						ModelMDIMain.this);
				Environment.getDispatcher()
				.removePropertyChangeListener(ContextChangeEvent.TYPE, ModelMDIMain.this);
				aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
			}
		});
	}

	public ModelMDIMain() {
		this(new ApplicationContext());
	}

	protected void initFrames() {
		this.frames = new UIDefaults();
		this.schemePane = new SchemeTabbedPane(this.aContext);
		new SchemeAlarmHandler(this.aContext, this.schemePane);
		this.schemePane.setEditable(false);
		this.schemePane.setToolBarVisible(false);
		
		this.tables = new LinkedList<ReportTable>();
		this.graphs = new LinkedList<SimpleResizableFrame>();
		
		this.frames.put(FRAME_EDITOR_MAIN, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SCHEME_VIEWER_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(ModelMDIMain.this.aContext, ModelMDIMain.this.schemePane);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				ModelMDIMain.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));
				ModelMDIMain.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, ModelMDIMain.this.aContext);
				new MapPropertiesEventHandler(generalFrame, ModelMDIMain.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_ADDITIONAL_PROPERTIES));
				ModelMDIMain.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, ModelMDIMain.this.aContext);
				new MapPropertiesEventHandler(additionalFrame, ModelMDIMain.this.aContext);
				return additionalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_CHARACTERISTICS));
				ModelMDIMain.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, ModelMDIMain.this.aContext);
				new MapPropertiesEventHandler(characteristicFrame, ModelMDIMain.this.aContext);
				return characteristicFrame;
			}
		});
		
		this.frames.put(RefModelParamsFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | " + RefModelParamsFrame.NAME, Level.FINEST);
				RefModelParamsFrame paramsFrame = new RefModelParamsFrame(aContext);
				ModelMDIMain.this.desktopPane.add(paramsFrame);
				
				ModelMDIMain.this.tables.add(paramsFrame);
				
				return paramsFrame;
			}
		});
		
		this.frames.put(FRAME_TRACE_SELECTOR, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SELECTOR_FRAME", Level.FINEST);
				TraceSelectorFrame selectFrame = new TraceSelectorFrame(ModelMDIMain.this.dispatcher);
				desktopPane.add(selectFrame);
				return selectFrame;
			}
		});
		
		this.frames.put(FRAME_TRANS_DATA, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TRANS_DATA_FRAME", Level.FINEST);
				TransData transDataFrame = new TransData();
				desktopPane.add(transDataFrame);
				return transDataFrame;
			}
		});
		
		this.frames.put(FRAME_ANALYSIS_MAIN, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ANALYSIS_FRAME", Level.FINEST);
				ModelMDIMain.this.layeredPanel = new MapMarkersLayeredPanel(ModelMDIMain.this.dispatcher);
				
				ScalableFrame analysisFrame = new ScalableFrame(ModelMDIMain.this.layeredPanel) {
					@Override
					public String getReportTitle() {
						return FRAME_ANALYSIS_MAIN;
					}
				};
				analysisFrame.setTitle(LangModelAnalyse.getString("analysisTitle"));
				desktopPane.add(analysisFrame);
				ModelMDIMain.this.graphs.add(analysisFrame);
				return analysisFrame;
			}
		});
		
		this.frames.put(FRAME_TREE, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setName(FRAME_TREE);
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
				
				FullModelChildrenFactory model = new FullModelChildrenFactory(ModelMDIMain.this.aContext);
				SchemeTreeUI treeUI = new SchemeTreeUI(model.getRoot(), ModelMDIMain.this.aContext);
//				treeUI.getTreeUI().getTree().setRootVisible(false);
				TreeFilterUI tfUI = new TreeFilterUI(treeUI, new FilterPanel());
				

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				ModelMDIMain.this.desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		super.setWindowArranger(new WindowArranger(ModelMDIMain.this) {
			@Override
			public void arrange() {
				ModelMDIMain f = (ModelMDIMain)this.mainframe;

				int w = f.desktopPane.getSize().width;
				int h = f.desktopPane.getSize().height;
				int minh = Math.min(205, h / 4);

				JInternalFrame selectFrame = (JInternalFrame) f.frames.get(FRAME_TRACE_SELECTOR);
				JInternalFrame treeFrame = (JInternalFrame) f.frames.get(FRAME_TREE);
				JInternalFrame tdFrame = (JInternalFrame) f.frames.get(FRAME_TRANS_DATA);
				JInternalFrame analysisFrame = (JInternalFrame) f.frames.get(FRAME_ANALYSIS_MAIN);
				JInternalFrame modelParamsFrame = (JInternalFrame) f.frames.get(RefModelParamsFrame.NAME);
								
				JInternalFrame schemeFrame = (JInternalFrame) f.frames.get(FRAME_EDITOR_MAIN);
				JInternalFrame generalFrame = (JInternalFrame) f.frames.get(GeneralPropertiesFrame.NAME);
				JInternalFrame charFrame = (JInternalFrame) f.frames.get(CharacteristicPropertiesFrame.NAME);
				JInternalFrame additionalFrame = (JInternalFrame) f.frames.get(AdditionalPropertiesFrame.NAME);
				
				normalize(treeFrame);
				normalize(selectFrame);
				normalize(tdFrame);
				normalize(analysisFrame);
				normalize(modelParamsFrame);
				normalize(schemeFrame);
				normalize(generalFrame);
				normalize(charFrame);
				normalize(additionalFrame);
												
				analysisFrame.setSize(11 * w / 20, 2 * minh);
				selectFrame.setSize(w / 5, minh);
				treeFrame.setSize(w / 5, h - minh);
				tdFrame.setSize(w / 4, minh);
				modelParamsFrame.setSize(w / 4, minh);
				schemeFrame.setSize(11 * w / 20, h - 2 * minh);
				generalFrame.setSize(w / 4, 2 * (h - 2 * minh) / 3);
				charFrame.setSize(w / 4, (h - 2 * minh) / 3);
				additionalFrame.setSize(w / 4, (h - 2 * minh) / 3);
								
				selectFrame.setLocation(0, 0);
				treeFrame.setLocation(0, minh);
				analysisFrame.setLocation(w / 5, 0);
				tdFrame.setLocation(3 * w / 4, minh);
				modelParamsFrame.setLocation(3 * w / 4, 0);
				schemeFrame.setLocation(w / 5, 2 * minh);
				generalFrame.setLocation(3 * w / 4, 2 * minh);
				charFrame.setLocation(3 * w / 4, 2 * minh + (h - 2 * minh) / 2);
				additionalFrame.setLocation(3 * w / 4, 2 * minh + 2 * (h - 2 * minh) / 3);
				
				// optional windows
				for (final Component component : ModelMDIMain.this.desktopPane.getComponents()) {
					String componentName = component.getName();
					if (componentName == null) {
						Log.debugMessage("Name is null for component: "
								+ component,
								Level.FINER);
						continue;
						
					}
					componentName = componentName.intern();
					if (componentName == MapFrame.NAME) {
						final JInternalFrame mapFrame = (JInternalFrame) component;
						normalize(mapFrame);
						mapFrame.setSize(11 * w / 20, h - 2 * minh);
						mapFrame.setLocation(w / 5, 2 * minh);						
					} 
				}
			}				
		});
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {
			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = ModelMDIMain.this.frames.get(key);
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
	protected void setDefaultModel (ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);
	}

	@Override
	public void initModule() {
		super.initModule();
		initFrames();
		
		Heap.addBsHashListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addPrimaryTraceListener(this);
		Heap.addCurrentTraceChangeListener(this);
		
		ApplicationModel aModel = aContext.getApplicationModel();

		setDefaultModel(aModel);

		MapApplicationModelFactory mapApplicationModelFactory = new MapSurveyApplicationModelFactory();

		aModel.setCommand("menuViewMapViewOpen", new OpenLinkedMapViewCommand(this.desktopPane, this.aContext, mapApplicationModelFactory));
		
		aModel.setCommand("menuViewModelSave", new SaveModelingCommand(aContext, "primarytrace"));
		aModel.setCommand("menuViewModelLoad", new LoadModelingCommand(aContext));
		aModel.setCommand("menuViewSchemeOpen", new SchemeOpenCommand(aContext));

		aModel.setCommand("menuFileOpen", new FileOpenCommand(aContext.getDispatcher(), aContext));
		aModel.setCommand("menuFileOpenAsBellcore", new FileOpenAsBellcoreCommand(aContext.getDispatcher(), aContext));
		aModel.setCommand("menuFileOpenAsWavetek", new FileOpenAsWavetekCommand(aContext.getDispatcher(), aContext));
		aModel.setCommand("menuFileSave", new FileSaveCommand(aContext));
		aModel.setCommand("menuFileSaveAsText", new FileSaveAsTextCommand(aContext));
		aModel.setCommand("menuFileClose", new FileCloseCommand());
		aModel.setCommand("menuFileAddCompare",new FileAddCommand(aContext));
		aModel.setCommand("menuFileRemoveCompare", new FileRemoveCommand(null, aContext));

		aModel.setCommand("menuTraceAddCompare", new AddTraceFromDatabaseCommand(aContext));
		aModel.setCommand("menuTraceRemoveCompare", new FileRemoveCommand(null, aContext));
		aModel.setCommand("menuTraceClose", new FileCloseCommand());

		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(this.aContext, 
				DestinationModules.MODELING);
		rc.setParameter(CreateAnalysisReportCommand.TABLE, this.tables);
		rc.setParameter(CreateAnalysisReportCommand.PANEL, this.graphs);

		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, this.getLazyCommand(FRAME_TRACE_SELECTOR));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_TREE, this.getLazyCommand(FRAME_TREE));
		aModel.setCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, this.getLazyCommand(FRAME_ANALYSIS_MAIN));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_TRANS_DATA, this.getLazyCommand(FRAME_TRANS_DATA));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS, this.getLazyCommand(RefModelParamsFrame.NAME));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_SCHEME, this.getLazyCommand(FRAME_EDITOR_MAIN));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES, this.getLazyCommand(GeneralPropertiesFrame.NAME));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES, this.getLazyCommand(AdditionalPropertiesFrame.NAME));
		aModel.setCommand(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS, this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_MAP, this.getLazyCommand(MapFrame.NAME));
		
		aModel.fireModelChanged("");
	}

	@Override
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
		
		super.setContext(aContext);
		
		if(aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
	}

	public void bsHashAdded(String key) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		ScalableFrame analysisFrame = (ScalableFrame)this.frames.get(FRAME_ANALYSIS_MAIN);
		BellcoreStructure bs = Heap.getAnyPFTraceByKey(key).getBS();
		
		double delta_x = bs.getResolution();
		double[] y = bs.getTraceData();
		
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			this.layeredPanel.removeAllGraphPanels();
			
			MapMarkersPanel panel = new MapMarkersPanel(this.layeredPanel, this.aContext.getDispatcher(), y, delta_x);
			analysisFrame.setGraph(panel, true, key);

			if (bs.schemePathId != null) {
				panel.setSchemePathId(new Identifier(bs.schemePathId));
			}
			if (bs.monitoredElementId != null) {
				panel.setMonitoredElementId(new Identifier(bs.monitoredElementId));
			}
			panel.updEvents(key);
			updFrames();
		} else {
			if (key.equals(Heap.REFERENCE_TRACE_KEY)) {
				aModel.setEnabled("menuTraceReferenceMakeCurrent", true);
				aModel.fireModelChanged(new String[] { "menuTraceReferenceMakeCurrent"});
			} else {
				aModel.setEnabled("menuTraceRemoveCompare", true);
				aModel.setEnabled("menuFileRemoveCompare", true);
				aModel.fireModelChanged("");
			}
			
			SimpleGraphPanel p = new SimpleGraphPanel(y, delta_x);
			analysisFrame.addGraph(p, key);
			analysisFrame.updScales();
		}
	}

	public void bsHashRemoved(String key) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		ScalableFrame analysisFrame = (ScalableFrame)this.frames.get(FRAME_ANALYSIS_MAIN);
		analysisFrame.removeGraph(key);
		analysisFrame.updScales();
		
		if (!Heap.hasSecondaryBS()) {
			aModel.setEnabled("menuFileRemoveCompare", false);
			aModel.setEnabled("menuTraceRemoveCompare", false);
			aModel.fireModelChanged(new String[] { "menuTraceRemoveCompare"});
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
		
		aModel.setEnabled("menuViewModelSave", false);

		aModel.setEnabled("menuTraceClose", false);
		aModel.setEnabled("menuTraceAddCompare", false);
		aModel.setEnabled("menuFileRemoveCompare", false);
		aModel.setEnabled("menuTraceRemoveCompare", false);

		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowArrange", false);

		aModel.fireModelChanged("");
		updFrames();
	}
	
	void updFrames() {
//		PFTrace pf = Heap.getPFTracePrimary();
		RefAnalysis ra = Heap.getRefAnalysisPrimary();
		if (ra == null) {
			showTraceFrames(false);
		} else {
//			double[] filtered = Heap.getRefAnalysisPrimary().filtered;
			showTraceFrames(true);
		}
	}
	
	void showTraceFrames(boolean b) {
		JInternalFrame analysisFrame = (JInternalFrame) this.frames.get(FRAME_ANALYSIS_MAIN);
		analysisFrame.setVisible(b);
		JInternalFrame tdFrame = (JInternalFrame) this.frames.get(FRAME_TRANS_DATA);
		tdFrame.setVisible(b);
		JInternalFrame treeFrame = (JInternalFrame) this.frames.get(FRAME_TREE);
		treeFrame.setVisible(b);
		JInternalFrame selectFrame = (JInternalFrame) this.frames.get(FRAME_TRACE_SELECTOR);
		selectFrame.setVisible(b);
		JInternalFrame modelParamsFrame = (JInternalFrame) this.frames.get(RefModelParamsFrame.NAME);
		modelParamsFrame.setVisible(b);
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_TREE, b);
		aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_TRANS_DATA, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS, b);
		aModel.fireModelChanged("");
	}
	
	void showSchemeFrames(boolean b) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
	
		JInternalFrame schemeFrame = (JInternalFrame) this.frames.get(FRAME_EDITOR_MAIN);
		schemeFrame.setVisible(b);
		JInternalFrame generalFrame = (JInternalFrame) this.frames.get(GeneralPropertiesFrame.NAME);
		generalFrame.setVisible(b);
		JInternalFrame additionalFrame = (JInternalFrame) this.frames.get(AdditionalPropertiesFrame.NAME);
		additionalFrame.setVisible(b);
// XXX temporary not shown - characteristics work bad and no room on desktop
//		JInternalFrame characteristicsFrame = (JInternalFrame) this.frames.get(CharacteristicPropertiesFrame.NAME);
//		characteristicsFrame.setVisible(b);
		JInternalFrame mapFrame = MapDesktopCommand.findMapFrame(ModelMDIMain.this.desktopPane);
		if (mapFrame != null) {
			mapFrame.setVisible(b);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, b);
		}
		
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_SCHEME, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES, b);
		aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS, b);
		aModel.fireModelChanged("");
	}

	void setActiveRefId (String id)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.getCommand("menuFileRemoveCompare").setParameter("activeRefId", id);
		aModel.getCommand("menuTraceRemoveCompare").setParameter("activeRefId", id);
	}

	@Override
	public void loggedIn() {
		final ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuViewSchemeOpen", true);

		aModel.setEnabled("menuFileOpen", true);
		aModel.setEnabled("menuFileOpenAs", true);
		aModel.setEnabled("menuFileOpenAsBellcore", true);
		aModel.setEnabled("menuFileOpenAsWavetek", true);
		aModel.setEnabled("menuViewModelLoad", true);
		
		aModel.setEnabled("menuSessionOpen", false);

		aModel.fireModelChanged();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showTraceFrames(true);
				showSchemeFrames(true);
//				aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_TREE).execute();
//				aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_TRANS_DATA).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_SCHEME).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES).execute();
//				aModel.getCommand(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS).execute();
				
				aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
				aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
				aModel.fireModelChanged();
			}
		});
	}

	@Override
	public void loggedOut() {
		Heap.closeAll();
		
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuViewMapViewOpen", false);
		aModel.setEnabled("menuViewSchemeOpen", false);
		aModel.setEnabled("menuViewModelLoad", false);
		aModel.setEnabled("menuFileOpen", false);
		aModel.setEnabled("menuFileOpenAs", false);

		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
	
		showTraceFrames(false);
		showSchemeFrames(false);
		
		aModel.fireModelChanged();
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.aManager.saveIni();
		}
		super.processWindowEvent(e);
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

			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_TREE, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_TRANS_DATA, true);
			aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		}

		aModel.fireModelChanged("");
	}
	
		@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ose = (ObjectSelectedEvent)evt;
			if (ose.isSelected(ObjectSelectedEvent.SCHEME)) {
				Identifier id = ((Identifiable)ose.getSelectedObject()).getId();
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.getCommand("menuViewMapViewOpen").setParameter("scheme_id", id);
				aModel.setEnabled("menuViewMapViewOpen", true);
				aModel.fireModelChanged("");
			}
		}
		super.propertyChange(evt);
	}

	public void primaryTraceRemoved() {
		// TODO Auto-generated method stub
		
	}

	public void primaryRefAnalysisCUpdated() {
		updFrames();		
	}

	public void primaryRefAnalysisRemoved() {
		updFrames();
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
