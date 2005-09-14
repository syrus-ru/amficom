package com.syrus.AMFICOM.client.observer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapPropertiesEventHandler;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeEventHandler;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeMouseListener;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.client.model.MapSurveyApplicationModelFactory;
import com.syrus.AMFICOM.client.model.ObserverApplicationModel;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.observer.command.OpenMapViewCommand;
import com.syrus.AMFICOM.client.observer.command.OpenSchemeViewCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenAnalysisCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenEvaluationCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenExtendedAnalysisCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenMapEditorCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenPrognosisCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenSchedulerCommand;
import com.syrus.AMFICOM.client.observer.command.start.OpenSchemeEditorCommand;
import com.syrus.AMFICOM.client.observer.ui.AlarmFrame;
import com.syrus.AMFICOM.client.observer.ui.ObserverTreeModel;
import com.syrus.AMFICOM.client.observer.ui.ResultFrame;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeSelectionListener;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;
import com.syrus.util.Log;

public class ObserverMainFrame extends AbstractMainFrame {

	// sdf = UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
	
	UIDefaults					frames;
	
	public static final String	ALARM_FRAME	= "alarmFrame"; //$NON-NLS-1$
	public static final String	ALARM_POPUP_FRAME	= "alarmPopupFrame"; //$NON-NLS-1$
	public static final String	TREE_FRAME = "treeFrame"; //$NON-NLS-1$

	SchemeTabbedPane schemePane;
//	private CreateSurveyReportCommand csrCommand = null;

	//	SchemeAlarmUpdater schemeAlarmUpdater = null;

	public ObserverMainFrame(final ApplicationContext aContext) {
		super(aContext, 
				LangModelObserver.getString(ObserverResourceKeys.APPLICATION_TITLE), 
				new ObserverMenuBar(aContext.getApplicationModel()), 
				new ObserverToolBar());
	}

	public ObserverMainFrame() {
		this(new ApplicationContext());
	}
	
	protected void initFrames() {
		this.frames = new UIDefaults();
		this.schemePane = new SchemeTabbedPane(this.aContext);
		this.schemePane.setEditable(false);
		this.schemePane.setToolBarVisible(false);
		
		this.frames.put(SchemeViewerFrame.NAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SCHEME_VIEWER_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(ObserverMainFrame.this.aContext, ObserverMainFrame.this.schemePane);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				ObserverMainFrame.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(LangModelGeneral.getString("Properties"));
				ObserverMainFrame.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(generalFrame, ObserverMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(LangModelMap.getString("AdditionalProperties"));
				ObserverMainFrame.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(additionalFrame, ObserverMainFrame.this.aContext);
				return additionalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(LangModelMap.getString("Characteristics"));
				ObserverMainFrame.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(characteristicFrame, ObserverMainFrame.this.aContext);
				return characteristicFrame;
			}
		});
		
		this.frames.put(ResultFrame.NAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue RESULT_FRAME", Level.FINEST);
				ResultFrame resultFrame = new ResultFrame(ObserverMainFrame.this.aContext);
				ObserverMainFrame.this.desktopPane.add(resultFrame);
				return resultFrame;
			}
		});
		
		this.frames.put(ALARM_FRAME, new UIDefaults.LazyValue() {
			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue ALARM_FRAME", Level.FINEST);
				AlarmFrame alarmFrame = new AlarmFrame(ObserverMainFrame.this.aContext);
				ObserverMainFrame.this.desktopPane.add(alarmFrame);
				return alarmFrame;
			}
		});
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			@SuppressWarnings("synthetic-access")
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setName(TREE_FRAME);
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
				
				ObserverTreeModel model = new ObserverTreeModel(ObserverMainFrame.this.aContext);
				IconedTreeUI iconedTreeUI = new IconedTreeUI(model.getRoot());
				JTree tree = iconedTreeUI.getTree();
				TreeFilterUI tfUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());

				new SchemeTreeSelectionListener(iconedTreeUI, ObserverMainFrame.this.aContext);

				MapViewTreeEventHandler mapViewTreeEventHandler = new MapViewTreeEventHandler(
						iconedTreeUI, 
						ObserverMainFrame.this.aContext, 
						model.getRoot());
				tree.addTreeSelectionListener(mapViewTreeEventHandler);
				tree.addTreeWillExpandListener(mapViewTreeEventHandler);
				tree.addMouseListener(new MapViewTreeMouseListener(tree, ObserverMainFrame.this.aContext));
				
				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				ObserverMainFrame.this.desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		super.setWindowArranger(new WindowArranger(ObserverMainFrame.this) {

			@SuppressWarnings("synthetic-access")
			public void arrange() {
				int w = ObserverMainFrame.this.desktopPane.getSize().width;
				int h = ObserverMainFrame.this.desktopPane.getSize().height;

				JInternalFrame resultFrame = null;
				JInternalFrame alarmFrame = null;
				JInternalFrame mapFrame = null;
				JInternalFrame schemeFrame = null;
				JInternalFrame generalFrame = null;
				JInternalFrame additionalFrame = null;
				JInternalFrame characteristicsFrame = null;
				JInternalFrame treeFrame = null;
				
				for (Component component : ObserverMainFrame.this.desktopPane.getComponents()) {
					if (TREE_FRAME.equals(component.getName()))
						treeFrame = (JInternalFrame)component;
					else if (MapFrame.NAME.equals(component.getName()))
						mapFrame = (JInternalFrame)component;
					else if (ResultFrame.NAME.equals(component.getName()))
						resultFrame = (JInternalFrame)component;
					else if (ALARM_FRAME.equals(component.getName()))
						alarmFrame = (JInternalFrame)component;
					else if (SchemeViewerFrame.NAME.equals(component.getName()))
						schemeFrame = (JInternalFrame)component;
					else if (GeneralPropertiesFrame.NAME.equals(component.getName()))
						generalFrame = (JInternalFrame)component;
					else if (AdditionalPropertiesFrame.NAME.equals(component.getName()))
						additionalFrame = (JInternalFrame)component;
					else if (CharacteristicPropertiesFrame.NAME.equals(component.getName()))
						characteristicsFrame = (JInternalFrame)component;
				}
				
				if (mapFrame != null) {
					normalize(schemeFrame);
					schemeFrame.setSize(3 * w / 5, 3 * h / 4);
					schemeFrame.setLocation(w / 5, h / 4);
				}
				if (schemeFrame != null) {
					normalize(schemeFrame);
					schemeFrame.setSize(3 * w / 5, 3 * h / 4);
					schemeFrame.setLocation(w / 5, h / 4);
				}
				if (generalFrame != null) {
					normalize(generalFrame);
					generalFrame.setSize(w / 5, h / 4);
					generalFrame.setLocation(4 * w / 5, h / 4);
				}
				if (additionalFrame != null) {
					normalize(additionalFrame);
					additionalFrame.setSize(w / 5, h / 4);
					additionalFrame.setLocation(4 * w / 5, 2 * h / 4);
				}
				if (characteristicsFrame != null) {
					normalize(characteristicsFrame);
					characteristicsFrame.setSize(w / 5, h / 4);
					characteristicsFrame.setLocation(4 * w / 5, 3 * h / 4);
				}
				if (resultFrame != null) {
					normalize(resultFrame);
					resultFrame.setSize(w * 2 / 5, h / 4);			
					resultFrame.setLocation(w * 3 / 5, 0);
				}
				if (alarmFrame != null) {
					normalize(alarmFrame);
					alarmFrame.setSize(w * 2 / 5, h / 4);
					alarmFrame.setLocation(w / 5, 0);
				}
				if (treeFrame != null) {
					normalize(treeFrame);
					treeFrame.setSize(w / 5, h);
					treeFrame.setLocation(0, 0);
				}
			}
		});
	}


	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		
		aModel.setEnabled(ObserverApplicationModel.MENU_START, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_OPEN, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_REPORT, true);
	}

	protected void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand(ObserverApplicationModel.MENU_START_SCHEDULER, new OpenSchedulerCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_ANALIZE, new OpenAnalysisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_ANALIZE_EXT, new OpenExtendedAnalysisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_EVALUATION, new OpenEvaluationCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_PROGNOSIS, new OpenPrognosisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_MAP_EDITOR, new OpenMapEditorCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_SCHEME_EDITOR, new OpenSchemeEditorCommand(this.aContext));
		
		MapApplicationModelFactory mapApplicationModelFactory = new MapSurveyApplicationModelFactory();

		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_MAP, new ViewMapWindowCommand(this.desktopPane, this.aContext, mapApplicationModelFactory));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_SCHEME, this.getLazyCommand(SchemeViewerFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_RESULTS, this.getLazyCommand(ResultFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_ALARMS, this.getLazyCommand(ALARM_FRAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_NAVIGATOR, this.getLazyCommand(TREE_FRAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS, this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES, this.getLazyCommand(GeneralPropertiesFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES, this.getLazyCommand(AdditionalPropertiesFrame.NAME));
		aModel.setCommand(ApplicationModel.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(this.windowArranger));

		aModel.setCommand(ObserverApplicationModel.MENU_OPEN_SCHEME, new OpenSchemeViewCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_OPEN_MAP, new OpenMapViewCommand(this.desktopPane, this.aContext, mapApplicationModelFactory));
		
//		csrCommand = new CreateSurveyReportCommand(aContext);
//		csrCommand.setParameter(this);
//		aModel.setCommand(SurveyApplicationModel.MENU_REPORT_BY_TEMPLATE, csrCommand);

		aModel.fireModelChanged();
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = ObserverMainFrame.this.frames.get(key);
					if (object instanceof JInternalFrame) {
						System.out.println("init getLazyCommand for " + key);
						this.command = new ShowWindowCommand((JInternalFrame)object);
					}
				}
				return this.command;
			}

			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
		}
		
		super.setContext(aContext);
		
		if(aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent) pce;

			if(mapEvent.getMapEventType().equals(MapEvent.MAP_FRAME_SHOWN)) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_MAP, true);
				aModel.fireModelChanged();
				this.windowArranger.arrange();
				
			} else if (pce.getPropertyName().equals(MapEvent.MAP_VIEW_CLOSED)) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_MAP, false);
				aModel.fireModelChanged();
				this.windowArranger.arrange();
			}
		}
		super.propertyChange(pce);
	}

	public void setSessionOpened() {
		super.setSessionOpened();
	}

	public void setDomainSelected() {
		super.setDomainSelected();

		Command command = new OpenSchemeViewCommand(this.aContext);
		command.execute();

		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(ObserverApplicationModel.MENU_START_SCHEDULER, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_ANALIZE, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_ANALIZE_EXT, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_EVALUATION, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_PROGNOSIS, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_SCHEME_EDITOR, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_START_MAP_EDITOR, true);

		aModel.setEnabled(ObserverApplicationModel.MENU_OPEN_SCHEME, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_OPEN_MAP, true);
		
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_MAP, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_SCHEME, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_NAVIGATOR, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_RESULTS, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_ALARMS, true);

		aModel.setEnabled(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE, true);

		aModel.fireModelChanged();

	}

	public void setSessionClosed() {
		super.setSessionClosed();

		setFramesVisible(false);
	}
}
