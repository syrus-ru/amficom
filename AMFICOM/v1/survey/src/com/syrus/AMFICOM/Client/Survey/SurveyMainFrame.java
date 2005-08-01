package com.syrus.AMFICOM.Client.Survey;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.Command.Survey.OpenAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenEvaluationCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenExtendedAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenMapEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenPrognosisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchedulerCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchemeEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchemeViewCommand;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmFrame;
import com.syrus.AMFICOM.Client.Survey.UI.ObserverTreeModel;
import com.syrus.AMFICOM.Client.Survey.UI.ResultFrame;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeSelectionListener;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.resource.LangModelSurvey;
import com.syrus.AMFICOM.resource.SurveyResourceKeys;
import com.syrus.util.Log;

public class SurveyMainFrame extends AbstractMainFrame {

	// sdf = UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
	
	UIDefaults					frames;
	
	public static final String	ALARM_FRAME	= "alarmFrame"; //$NON-NLS-1$
	public static final String	ALARM_POPUP_FRAME	= "alarmPopupFrame"; //$NON-NLS-1$
	public static final String	TREE_FRAME = "treeFrame"; //$NON-NLS-1$

	SchemeTabbedPane schemePane;
//	private CreateSurveyReportCommand csrCommand = null;

	//	SchemeAlarmUpdater schemeAlarmUpdater = null;

	public SurveyMainFrame(final ApplicationContext aContext) {
		super(aContext, 
				LangModelSurvey.getString(SurveyResourceKeys.APPLICATION_TITLE), 
				new SurveyMenuBar(aContext.getApplicationModel()), 
				new SurveyToolBar());
	}

	public SurveyMainFrame() {
		this(new ApplicationContext());
	}
	
	protected void initFrames() {
		this.frames = new UIDefaults();
		this.schemePane = new SchemeTabbedPane(this.aContext);
		this.schemePane.setEditable(false);
		this.schemePane.setToolBarVisible(false);
		
		this.frames.put(SchemeViewerFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | SCHEME_VIEWER_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(SurveyMainFrame.this.aContext, SurveyMainFrame.this.schemePane);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame("Title");
				desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, aContext);
				return generalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame("Title");
				desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, aContext);
				return characteristicFrame;
			}
		});
		
		this.frames.put(ResultFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue RESULT_FRAME", Level.FINEST);
				ResultFrame resultFrame = new ResultFrame(SurveyMainFrame.this.aContext);
				desktopPane.add(resultFrame);
				return resultFrame;
			}
		});
		
		this.frames.put(ALARM_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue ALARM_FRAME", Level.FINEST);
				AlarmFrame alarmFrame = new AlarmFrame(aContext);
				desktopPane.add(alarmFrame);
				return alarmFrame;
			}
		});
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

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
				
				ObserverTreeModel model = new ObserverTreeModel(SurveyMainFrame.this.aContext);
				IconedTreeUI treeUI = new IconedTreeUI(model.getRoot());
				new SchemeTreeSelectionListener(treeUI, SurveyMainFrame.this.aContext);
				TreeFilterUI tfUI = new TreeFilterUI(treeUI, new FilterPanel());

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		super.setWindowArranger(new WindowArranger(SurveyMainFrame.this) {

			public void arrange() {
				SurveyMainFrame f = (SurveyMainFrame) mainframe;

				int w = f.desktopPane.getSize().width;
				int h = f.desktopPane.getSize().height;

				JInternalFrame resultFrame = null;
				JInternalFrame alarmFrame = null;
				JInternalFrame editorFrame = null;
				JInternalFrame generalFrame = null;
				JInternalFrame characteristicsFrame = null;
				JInternalFrame treeFrame = null;
				
				for (Component component : desktopPane.getComponents()) {
					if (TREE_FRAME.equals(component.getName()))
						treeFrame = (JInternalFrame)component;
					else if (ResultFrame.NAME.equals(component.getName()))
						resultFrame = (JInternalFrame)component;
					else if (ALARM_FRAME.equals(component.getName()))
						alarmFrame = (JInternalFrame)component;
					else if (SchemeViewerFrame.NAME.equals(component.getName()))
						editorFrame = (JInternalFrame)component;
					else if (GeneralPropertiesFrame.NAME.equals(component.getName()))
						generalFrame = (JInternalFrame)component;
					else if (CharacteristicPropertiesFrame.NAME.equals(component.getName()))
						characteristicsFrame = (JInternalFrame)component;
				}
				
				if (editorFrame != null) {
					normalize(editorFrame);
					editorFrame.setSize(3 * w / 5, 3 * h / 4);
					editorFrame.setLocation(w / 5, h / 4);
				}
				if (generalFrame != null) {
					normalize(generalFrame);
					generalFrame.setSize(w/5, h / 2);
					generalFrame.setLocation(4*w/5, h / 4);
				}
				if (characteristicsFrame != null) {
					normalize(characteristicsFrame);
					characteristicsFrame.setSize(w/5, h / 4);
					characteristicsFrame.setLocation(4*w/5, 3 * h / 4);
				}
				if (resultFrame != null) {
					normalize(resultFrame);
					resultFrame.setSize(w * 2 / 5, h / 4);			
					resultFrame.setLocation(w * 3 / 5, 0);
				}
				if (alarmFrame != null) {
					normalize(alarmFrame);
					alarmFrame.setSize(w * 3 / 5, h / 4);
					alarmFrame.setLocation(0, 0);
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
		
		aModel.setEnabled("menuStart", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuReport", true);
	}

	protected void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand("menuStartScheduler", new OpenSchedulerCommand(aContext));
		aModel.setCommand("menuStartAnalize", new OpenAnalysisCommand(aContext));
		aModel.setCommand("menuStartAnalizeExt", new OpenExtendedAnalysisCommand(aContext));
		aModel.setCommand("menuStartEvaluation", new OpenEvaluationCommand(aContext));
		aModel.setCommand("menuStartPrognosis", new OpenPrognosisCommand(aContext));
		aModel.setCommand("menuStartMapEditor", new OpenMapEditorCommand(aContext));
		aModel.setCommand("menuStartSchemeEditor", new OpenSchemeEditorCommand(aContext));
		
		aModel.setCommand(ApplicationModel.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(this.windowArranger));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(SchemeViewerFrame.NAME));
		aModel.setCommand("menuWindowResults", this.getLazyCommand(ResultFrame.NAME));
		aModel.setCommand("menuWindowAlarms", this.getLazyCommand(ALARM_FRAME));
		aModel.setCommand("menuWindowMeasurements", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowCharacteristics", this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand("menuWindowProperties", this.getLazyCommand(GeneralPropertiesFrame.NAME));

		aModel.setCommand("menuViewScheme", new OpenSchemeViewCommand(aContext));
//		aModel.setCommand("menuViewMap", new OpenMapViewCommand(aContext));
		
//		csrCommand = new CreateSurveyReportCommand(aContext);
//		csrCommand.setParameter(this);
//		aModel.setCommand("menuTemplateReport", csrCommand);

		aModel.fireModelChanged();
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = SurveyMainFrame.this.frames.get(key);
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
			this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
			this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
		}
		
		super.setContext(aContext);
//		this.aContext = aContext;
		
		if(aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
			this.aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
		}
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(MapEvent.MAP_FRAME_SHOWN)) {
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuViewMapEditor", true);
			aModel.setEnabled("menuViewMapClose", true);
			aModel.fireModelChanged();

//				System.out.println("starting attribute reload thread");
//				myalarmTread = new ReloadAttributes((MapFrame) frame);//Запускаем
//																			// thread
//				myalarmTread.start();
		} else if (ae.getPropertyName().equals(MapEvent.MAP_VIEW_CLOSED)) {
			for (int i = 0; i < desktopPane.getComponents().length; i++) {
/*				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapFrame) {
					((MapFrame) comp).setVisible(false);
					((MapFrame) comp).setMapView(null);
					((MapFrame) comp).setContext(null);
				} else if (comp instanceof MapPropertyFrame)
					((MapPropertyFrame) comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
						((MapElementsFrame) comp).setVisible(false);*/
			}
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuViewMapEditor", false);
			aModel.setEnabled("menuViewMapClose", false);
			aModel.fireModelChanged();

//			if (myalarmTread != null) myalarmTread.stop_running();
//			myalarmTread = null;
		}
		super.propertyChange(ae);
	}

	public void setSessionOpened() {
		super.setSessionOpened();
	}

	public void setDomainSelected() {
		super.setDomainSelected();
		/*
		 * if(alarmChecker != null) { alarmChecker.stop_running(); alarmChecker =
		 * null; }
		 */
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuStartScheduler", true);
		aModel.setEnabled("menuStartAnalize", true);
		aModel.setEnabled("menuStartAnalizeExt", true);
		aModel.setEnabled("menuStartEvaluation", true);
		aModel.setEnabled("menuStartPrognosis", true);
		aModel.setEnabled("menuStartSchemeEditor", true);
		aModel.setEnabled("menuStartMapEditor", true);

		aModel.setEnabled("menuViewMap", true);
		aModel.setEnabled("menuViewScheme", true);
		
		aModel.setEnabled("menuWindowMap", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowCharacteristics", true);
		aModel.setEnabled("menuWindowProperties", true);
		aModel.setEnabled("menuWindowMeasurements", true);
		aModel.setEnabled("menuWindowResults", true);
		aModel.setEnabled("menuWindowAlarms", true);

		aModel.setEnabled("menuTemplateReport", true);

		aModel.fireModelChanged();

		//		alarmChecker = new AlarmChecker(internalDispatcher, aContext, this,
		// desktopPane);
		//		alarmChecker.start();
	}

	public void setSessionClosed() {
		super.setSessionClosed();
		/*
		 * if(alarmChecker != null) { alarmChecker.stop_running(); alarmChecker =
		 * null; }
		 */
		setFramesVisible(false);
	}
}
