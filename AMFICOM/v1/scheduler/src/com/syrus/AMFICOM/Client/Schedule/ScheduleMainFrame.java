
package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.UI.ElementsTreeFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.PlanFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.SaveParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TableFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestRequestFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TimeParametersFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.LazyCommand;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

public class ScheduleMainFrame extends AbstractMainFrame {

	private static final long	serialVersionUID		= 3257563988626848055L;

	public static final String	PARAMETERS_FRAME		= "parametersFrame";
	public static final String	PLAN_FRAME				= "planFrame";
	public static final String	PROPERTIES_FRAME		= "propertiesFrame";
	public static final String	SAVE_PARAMETERS_FRAME	= "saveParametersFrame";
	public static final String	TABLE_FRAME				= "tableFrame";
	public static final String	TIME_PARAMETERS_FRAME	= "timeParametersFrame";
	public static final String	TREE_FRAME				= "treeFrame";

	UIDefaults					frames;

	public ScheduleMainFrame() {
		this(new ApplicationContext());
	}

	public ScheduleMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelSchedule.getString("Scheduling_AMFICOM"), new ScheduleMainMenuBar(aContext
				.getApplicationModel()), new AbstractMainToolBar() {});

		final ApplicationContext aContext1 = this.aContext;
		final Dispatcher dispatcher1 = this.dispatcher;
		
		final JDesktopPane desktopPane1 = this.desktopPane;

		
		this.setWindowArranger(new WindowArranger(this) {

			public void arrange() {
				ScheduleMainFrame f = (ScheduleMainFrame) this.mainframe;

				int w = desktopPane1.getSize().width;
				int h = desktopPane1.getSize().height;

				// int minWidth = w / 5;

				JInternalFrame paramsFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PARAMETERS_FRAME));
				JInternalFrame propsFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PROPERTIES_FRAME));
				JInternalFrame treeFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TREE_FRAME));
				JInternalFrame timeFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TIME_PARAMETERS_FRAME));
				JInternalFrame planFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PLAN_FRAME));
				JInternalFrame saveFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.SAVE_PARAMETERS_FRAME));
				JInternalFrame tableFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TABLE_FRAME));

				normalize(planFrame);
				normalize(paramsFrame);
				normalize(propsFrame);
				normalize(treeFrame);
				normalize(timeFrame);
				normalize(saveFrame);
				normalize(tableFrame);

				treeFrame.setSize(w / 5, h);

				propsFrame.pack();
				propsFrame.setSize(w / 5, propsFrame.getHeight());
				saveFrame.pack();
				saveFrame.setSize(w / 5, saveFrame.getHeight());
				tableFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), 2 * h / 5);
				timeFrame.setSize(w / 5, h / 3);
				planFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), h - tableFrame.getHeight());

				treeFrame.setLocation(0, 0);
				timeFrame.setLocation(w - timeFrame.getWidth(), h - timeFrame.getHeight());
				saveFrame.setLocation(w - saveFrame.getWidth(), h - timeFrame.getHeight() - saveFrame.getHeight());
				propsFrame.setLocation(w - propsFrame.getWidth(), 0);
				paramsFrame.setSize(w / 5, saveFrame.getY() - (propsFrame.getY() + propsFrame.getHeight()));
				paramsFrame.setLocation(w - paramsFrame.getWidth(), propsFrame.getY() + propsFrame.getHeight());
				tableFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), planFrame.getHeight());

				planFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), 0);

			}
		});

		
		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				desktopPane1.setPreferredSize(desktopPane1.getSize());
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispatcher1.removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
				aContext1.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT)
						.execute();
			}
		});

		// PlanLayeredPanel panel = new PlanLayeredPanel();
		this.frames = new UIDefaults();
		this.frames.put(PLAN_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				PlanFrame frame = new PlanFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestParametersFrame frame = new TestParametersFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(PROPERTIES_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestRequestFrame frame = new TestRequestFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(TIME_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TimeParametersFrame frame = new TimeParametersFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				ElementsTreeFrame frame = new ElementsTreeFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(SAVE_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				SaveParametersFrame frame = new SaveParametersFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(TABLE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TableFrame frame = new TableFrame(aContext);
				desktopPane1.add(frame);
				return frame;
			}
		});

	}

	private Command getLazyCommand(final Object windowKey) {
		final String commandKey = windowKey.toString() + "_COMMAND";
		if (this.frames == null) {
			this.frames = new UIDefaults();
		}
		this.frames.put(commandKey, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults defaults) {
				return new ShowWindowCommand(defaults.get(windowKey));
			}
		});
		return new LazyCommand(this.frames, commandKey);
	}

	public void setDomainSelected() {
		super.setDomainSelected();

		ElementsTreeFrame treeFrame = (ElementsTreeFrame) this.frames.get(TREE_FRAME);
		treeFrame.init();

		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.TEST_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MEASUREMENTSETUP_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SET_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);

		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_CLEAN, null, null));

		((Component) this.frames.get(PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(PROPERTIES_FRAME)).setVisible(true);
		treeFrame.setVisible(true);
		((Component) this.frames.get(TIME_PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(PLAN_FRAME)).setVisible(true);
		((Component) this.frames.get(SAVE_PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(TABLE_FRAME)).setVisible(true);
		// testFilterFrame.setVisible(true);
	}

	protected void initModule() {
		super.initModule();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PLAN, this.getLazyCommand(PLAN_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TREE, this.getLazyCommand(TREE_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, this.getLazyCommand(PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, this.getLazyCommand(SAVE_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, this.getLazyCommand(PROPERTIES_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TIME, this.getLazyCommand(TIME_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TABLE, this.getLazyCommand(TABLE_FRAME));
		aModel.fireModelChanged();
	}
	
	protected void disposeModule() {
		super.disposeModule();
		
		this.frames.clear();
		this.frames = null;
		
	}

	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
//		Log.debugMessage("ScheduleMainFrame.setDefaultModel | ", Log.FINEST);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, false);

		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, false);
	}

	public void setConnectionOpened() {
		super.setConnectionOpened();
//		Log.debugMessage("ScheduleMainFrame.setConnectionOpened | ", Log.FINEST);
		this.setEnableViewItems(true);
		
	}

	public void setSessionOpened() {
		super.setSessionOpened();		
//		Log.debugMessage("ScheduleMainFrame.setConnectionOpened | ", Log.FINEST);
		this.setEnableViewItems(true);
	}
	
	private void setEnableViewItems(boolean enable) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, enable);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, enable);
		
		aModel.fireModelChanged("");
	}

	public void setConnectionClosed() {
		super.setConnectionClosed();
//		Log.debugMessage("ScheduleMainFrame.setConnectionClosed | ", Log.FINEST);
		this.setEnableViewItems(false);

	}
}
