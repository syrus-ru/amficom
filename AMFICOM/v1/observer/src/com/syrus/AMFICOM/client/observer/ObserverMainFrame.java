package com.syrus.AMFICOM.client.observer;

import static com.syrus.AMFICOM.resource.ObserverResourceKeys.FRAME_ALARM;
import static com.syrus.AMFICOM.resource.ObserverResourceKeys.FRAME_RESULT;
import static com.syrus.AMFICOM.resource.ObserverResourceKeys.FRAME_TREE;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
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
import com.syrus.AMFICOM.client.observer.command.CreateObserverReportCommand;
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeAlarmHandler;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeSelectionListener;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;
import com.syrus.util.Log;

public class ObserverMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = -4447044860893666923L;

	UIDefaults					frames;
	SchemeTabbedPane schemePane;

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
		new SchemeAlarmHandler(this.aContext, this.schemePane);
		this.schemePane.setEditable(false);
//		this.schemePane.setToolBarVisible(false);
		
		this.frames.put(FRAME_EDITOR_MAIN, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SCHEME_VIEWER_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(ObserverMainFrame.this.aContext, ObserverMainFrame.this.schemePane);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				ObserverMainFrame.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));
				ObserverMainFrame.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(generalFrame, ObserverMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_ADDITIONAL_PROPERTIES));
				ObserverMainFrame.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(additionalFrame, ObserverMainFrame.this.aContext);
				return additionalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_CHARACTERISTICS));
				ObserverMainFrame.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, ObserverMainFrame.this.aContext);
				new MapPropertiesEventHandler(characteristicFrame, ObserverMainFrame.this.aContext);
				return characteristicFrame;
			}
		});
		
		this.frames.put(FRAME_RESULT, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue RESULT_FRAME", Level.FINEST);
				ResultFrame resultFrame = new ResultFrame(ObserverMainFrame.this.aContext);
				ObserverMainFrame.this.desktopPane.add(resultFrame);
				return resultFrame;
			}
		});
		
		this.frames.put(FRAME_ALARM, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue ALARM_FRAME", Level.FINEST);
				AlarmFrame alarmFrame = new AlarmFrame(ObserverMainFrame.this.aContext);
				ObserverMainFrame.this.desktopPane.add(alarmFrame);
				return alarmFrame;
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
				treeFrame.setTitle(I18N.getString(FRAME_TREE));
				
				ObserverTreeModel model = new ObserverTreeModel(ObserverMainFrame.this.aContext);
				IconedTreeUI iconedTreeUI = new IconedTreeUI(model.getRoot());
				JTree tree = iconedTreeUI.getTree();
				TreeFilterUI tfUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());

				new SchemeTreeSelectionListener(iconedTreeUI, ObserverMainFrame.this.aContext);

				MapViewTreeEventHandler mapViewTreeEventHandler = new MapViewTreeEventHandler(
						iconedTreeUI, 
						ObserverMainFrame.this.aContext, 
						model.getRoot());
				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
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
			@Override
			public void arrange() {
				final Dimension size = ObserverMainFrame.this.desktopPane.getSize();
				final int width = size.width;
				final int height = size.height;

				for (final Component component : ObserverMainFrame.this.desktopPane.getComponents()) {
					String componentName = component.getName();
					if (componentName == null) {
						Log.debugMessage("Name is null for component: "
								+ component,
								Level.SEVERE);
						continue;
						
					}
					componentName = componentName.intern();
					if (componentName == FRAME_TREE) {
						final JInternalFrame treeFrame = (JInternalFrame) component;
						normalize(treeFrame);
						treeFrame.setSize(width / 5, height);
						treeFrame.setLocation(0, 0);
					} else if (componentName == MapFrame.NAME) {
						final JInternalFrame mapFrame = (JInternalFrame) component;
						normalize(mapFrame);
						mapFrame.setSize(3 * width / 5, 3 * height / 4);
						mapFrame.setLocation(width / 5, height / 4);
					} else if (componentName == FRAME_RESULT) {
						final JInternalFrame resultFrame = (JInternalFrame) component;
						normalize(resultFrame);
						resultFrame.setSize(width * 2 / 5, height / 4);			
						resultFrame.setLocation(width * 3 / 5, 0);
					} else if (componentName == FRAME_ALARM) {
						final JInternalFrame alarmFrame = (JInternalFrame) component;
						normalize(alarmFrame);
						alarmFrame.setSize(width * 2 / 5, height / 4);
						alarmFrame.setLocation(width / 5, 0);
					} else if (componentName == FRAME_EDITOR_MAIN) {
						final JInternalFrame schemeFrame = (JInternalFrame) component;
						normalize(schemeFrame);
						schemeFrame.setSize(3 * width / 5, 3 * height / 4);
						schemeFrame.setLocation(width / 5, height / 4);
					} else if (componentName == GeneralPropertiesFrame.NAME) {
						final JInternalFrame generalFrame = (JInternalFrame) component;
						normalize(generalFrame);
						generalFrame.setSize(width / 5, height / 4);
						generalFrame.setLocation(4 * width / 5, height / 4);
					} else if (componentName == AdditionalPropertiesFrame.NAME) {
						final JInternalFrame additionalFrame = (JInternalFrame) component;
						normalize(additionalFrame);
						additionalFrame.setSize(width / 5, height / 4);
						additionalFrame.setLocation(4 * width / 5, 2 * height / 4);
					} else if (componentName == CharacteristicPropertiesFrame.NAME) {
						final JInternalFrame characteristicsFrame = (JInternalFrame) component;
						normalize(characteristicsFrame);
						characteristicsFrame.setSize(width / 5, height / 4);
						characteristicsFrame.setLocation(4 * width / 5, 3 * height / 4);
					}
				}
			}
		});
	}

	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		
		aModel.setEnabled(ObserverApplicationModel.MENU_START, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_OPEN, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_REPORT, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);
	}

	@Override
	protected void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setVisible(ObserverApplicationModel.MENU_START, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_ANALIZE, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_ANALIZE_EXT, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_EVALUATION, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_MAP_EDITOR, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_PROGNOSIS, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_SCHEDULER, false);
		aModel.setVisible(ObserverApplicationModel.MENU_START_SCHEME_EDITOR, false);
		
		aModel.setCommand(ObserverApplicationModel.MENU_START_SCHEDULER, new OpenSchedulerCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_ANALIZE, new OpenAnalysisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_ANALIZE_EXT, new OpenExtendedAnalysisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_EVALUATION, new OpenEvaluationCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_PROGNOSIS, new OpenPrognosisCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_MAP_EDITOR, new OpenMapEditorCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_START_SCHEME_EDITOR, new OpenSchemeEditorCommand(this.aContext));
		
		MapApplicationModelFactory mapApplicationModelFactory = new MapSurveyApplicationModelFactory();

		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_MAP, new ViewMapWindowCommand(this.desktopPane, this.aContext, mapApplicationModelFactory));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_SCHEME, this.getLazyCommand(FRAME_EDITOR_MAIN));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_RESULTS, this.getLazyCommand(FRAME_RESULT));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_ALARMS, this.getLazyCommand(FRAME_ALARM));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_NAVIGATOR, this.getLazyCommand(FRAME_TREE));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS, this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES, this.getLazyCommand(GeneralPropertiesFrame.NAME));
		aModel.setCommand(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES, this.getLazyCommand(AdditionalPropertiesFrame.NAME));
		aModel.setCommand(ApplicationModel.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(this.windowArranger));

		aModel.setCommand(ObserverApplicationModel.MENU_OPEN_SCHEME, new OpenSchemeViewCommand(this.aContext));
		aModel.setCommand(ObserverApplicationModel.MENU_OPEN_MAP, new OpenMapViewCommand(this.desktopPane, this.aContext, mapApplicationModelFactory));
		
		CreateObserverReportCommand	csrCommand = new CreateObserverReportCommand(this.aContext, this.desktopPane, this.schemePane);
		aModel.setCommand(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE, csrCommand);

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

			@Override
			public void execute() {
				this.getLazyCommand().execute();
			}
		};
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


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ose = (ObjectSelectedEvent)evt;
			if (ose.isSelected(ObjectSelectedEvent.SCHEME)) {
				Identifier id = ose.getIdentifiable().getId();
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.getCommand(ObserverApplicationModel.MENU_OPEN_MAP).setParameter("scheme_id", id);
				aModel.setEnabled(ObserverApplicationModel.MENU_OPEN_MAP, true);
				aModel.fireModelChanged("");
			}
		}
		super.propertyChange(evt);
	}

	@Override
	public void loggedIn() {
//		AlarmReceiver.getInstance().setContext(this.aContext);

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
		
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_MAP, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_SCHEME, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_NAVIGATOR, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_RESULTS, true);
		aModel.setEnabled(ObserverApplicationModel.MENU_VIEW_ALARMS, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);

		aModel.setEnabled(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE, true);

		aModel.fireModelChanged();

	}

	@Override
	public void loggedOut() {
		setFramesVisible(false);
		ApplicationModel aModel = this.aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.fireModelChanged();
	}
}
