/*-
* $Id: ScheduleMainFrame.java,v 1.48 2005/12/20 10:01:07 arseniy Exp $
*
* Copyright ¿ 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.Schedule.UI.ElementsTreeFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.PlanFrame;
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
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.48 $, $Date: 2005/12/20 10:01:07 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class ScheduleMainFrame extends AbstractMainFrame {

	private static final long	serialVersionUID		= 3257563988626848055L;

	public static final String	PARAMETERS_FRAME		= "parametersFrame";
	public static final String	PLAN_FRAME				= "planFrame";
	public static final String	PROPERTIES_FRAME		= "propertiesFrame";
//	public static final String	SAVE_PARAMETERS_FRAME	= "saveParametersFrame";
	public static final String	TABLE_FRAME				= "tableFrame";
	public static final String	TIME_PARAMETERS_FRAME	= "timeParametersFrame";
	public static final String	TREE_FRAME				= "treeFrame";

	UIDefaults					frames;

	public ScheduleMainFrame() {
		this(new ApplicationContext());
	}

	@SuppressWarnings("serial")
	public ScheduleMainFrame(final ApplicationContext aContext) {
		super(aContext, I18N.getString("Scheduler.Text.Scheduler.Title"), new ScheduleMainMenuBar(aContext
				.getApplicationModel()), new AbstractMainToolBar() {
			// nothing
		});
		
		final Dispatcher dispatcher1 = this.dispatcher;
		
		final JDesktopPane desktopPane1 = this.desktopPane;

		
		this.setWindowArranger(new WindowArranger(this) {

			private int timeFrameHeight = 0;
			
			@Override
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
//				JInternalFrame saveFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.SAVE_PARAMETERS_FRAME));
				JInternalFrame tableFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TABLE_FRAME));

				normalize(planFrame);
				normalize(paramsFrame);
				normalize(propsFrame);
				normalize(treeFrame);
				normalize(timeFrame);
//				normalize(saveFrame);
				normalize(tableFrame);

				treeFrame.setSize(w / 5, h);

				propsFrame.pack();
				propsFrame.setSize(w / 5, propsFrame.getHeight());				
				
				timeFrame.pack();
				if (this.timeFrameHeight == 0) {
					this.timeFrameHeight = 3 * timeFrame.getHeight() / 2;
				}
				timeFrame.setSize(w / 5, this.timeFrameHeight);
//				saveFrame.pack();
//				saveFrame.setSize(w / 5, saveFrame.getHeight());
				tableFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), 2 * h / 5);
//				timeFrame.setSize(w / 5, h / 3);
				planFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), h - tableFrame.getHeight());

				treeFrame.setLocation(0, 0);
				timeFrame.setLocation(w - timeFrame.getWidth(), h - timeFrame.getHeight());
//				saveFrame.setLocation(w - saveFrame.getWidth(), h - timeFrame.getHeight() - saveFrame.getHeight());
				propsFrame.setLocation(w - propsFrame.getWidth(), 0);
				paramsFrame.setSize(w / 5, h - timeFrame.getHeight() - (propsFrame.getY() + propsFrame.getHeight()));
				paramsFrame.setLocation(w - paramsFrame.getWidth(), propsFrame.getY() + propsFrame.getHeight());
				tableFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), planFrame.getHeight());

				planFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), 0);

			}
		});

		
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				desktopPane1.setPreferredSize(desktopPane1.getSize());
				final ApplicationModel aModel = aContext.getApplicationModel();
				aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			}
			
			
		});
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispatcher1.removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
				aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT)
						.execute();
			}
		});

		// PlanLayeredPanel panel = new PlanLayeredPanel();
		

	}

//	private Command getLazyCommand(final Object windowKey) {
//		final String commandKey = windowKey.toString() + "_COMMAND";
//		if (this.frames == null) {
//			this.frames = new UIDefaults();
//		}
//		this.frames.put(commandKey, new UIDefaults.LazyValue() {
//
//			public Object createValue(UIDefaults defaults) {
//				return new ShowWindowCommand((JInternalFrame) defaults.get(windowKey));
//			}
//		});
//		return new LazyCommand(this.frames, commandKey);
//	}

	private void logged(final boolean loggedIn) {
		

		ElementsTreeFrame treeFrame = (ElementsTreeFrame) this.frames.get(TREE_FRAME);
		if (loggedIn) {
			treeFrame.init();
		}

		if (loggedIn) {
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.TEST_CODE);
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE);
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.PARAMETERSET_CODE);
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE);

			this.dispatcher.firePropertyChange(
				new PropertyChangeEvent(this, SchedulerModel.COMMAND_CLEAN, null, null));			
		} else {
			final SchedulerModel schedulerModel = 
				(SchedulerModel) this.aContext.getApplicationModel();
			schedulerModel.unselectTests(this);
//			StorableObjectPool.clean();
		}

		((Component) this.frames.get(PARAMETERS_FRAME)).setVisible(loggedIn);
		((Component) this.frames.get(PROPERTIES_FRAME)).setVisible(loggedIn);
		treeFrame.setVisible(loggedIn);
		((Component) this.frames.get(TIME_PARAMETERS_FRAME)).setVisible(loggedIn);
		((Component) this.frames.get(PLAN_FRAME)).setVisible(loggedIn);
//		((Component) this.frames.get(SAVE_PARAMETERS_FRAME)).setVisible(loggedIn);
		((Component) this.frames.get(TABLE_FRAME)).setVisible(loggedIn);
		// testFilterFrame.setVisible(loggedIn);		
		this.setEnableViewItems(loggedIn);
	}
	
	@Override
	public void loggedIn() {
		this.logged(true);
	}

	@Override
	public void loggedOut() {
		Log.debugMessage(Log.DEBUGLEVEL09);
		this.logged(false);
	}

	@Override
	protected void initModule() {
		super.initModule();

		final JDesktopPane desktopPane1 = this.desktopPane;
		
		this.frames = new UIDefaults();
		this.frames.put(PLAN_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				PlanFrame frame = new PlanFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestParametersFrame frame = new TestParametersFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(PROPERTIES_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestRequestFrame frame = new TestRequestFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(TIME_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TimeParametersFrame frame = new TimeParametersFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});

		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				ElementsTreeFrame frame = new ElementsTreeFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});

//		this.frames.put(SAVE_PARAMETERS_FRAME, new UIDefaults.LazyValue() {
//
//			public Object createValue(UIDefaults table) {
//				SaveParametersFrame frame = new SaveParametersFrame(aContext);
//				desktopPane1.add(frame);
//				return frame;
//			}
//		});

		this.frames.put(TABLE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TableFrame frame = new TableFrame(getContext());
				desktopPane1.add(frame);
				return frame;
			}
		});
		
		final ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PLAN, this.getShowWindowLazyCommand(this.frames, PLAN_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TREE, this.getShowWindowLazyCommand(this.frames, TREE_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, this.getShowWindowLazyCommand(this.frames, PARAMETERS_FRAME));
//		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, this.getShowWindowLazyCommand(this.frames, SAVE_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, this.getShowWindowLazyCommand(this.frames, PROPERTIES_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TIME, this.getShowWindowLazyCommand(this.frames, TIME_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TABLE, this.getShowWindowLazyCommand(this.frames, TABLE_FRAME));
		aModel.fireModelChanged();
	}
	
	@Override
	protected void disposeModule() {
		super.disposeModule();
		
		this.frames.clear();
		this.frames = null;
		
	}

	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
//		Log.debugMessage(Log.FINEST);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, false);
//		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, false);

		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, false);
	}
	
	private void setEnableViewItems(boolean enable) {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, enable);
//		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, enable);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, enable);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, enable);
		
		aModel.fireModelChanged("");
	}
}
