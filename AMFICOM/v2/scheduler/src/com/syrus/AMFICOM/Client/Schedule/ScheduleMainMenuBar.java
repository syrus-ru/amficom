package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;

public class ScheduleMainMenuBar extends JMenuBar {

	public static final String			MENU_VIEW					= "menuView";
	public static final String			MENU_VIEW_PLAN				= "menuViewPlan";
	public static final String			MENU_VIEW_TIME				= "menuViewTime";
	public static final String			MENU_VIEW_TREE				= "menuViewTree";
	public static final String			MENU_VIEW_PARAMETERS		= "menuViewParameters";
	public static final String			MENU_VIEW_PROPERTIES		= "menuViewProperties";
	public static final String			MENU_VIEW_TABLE				= "menuViewTable";
	public static final String			MENU_VIEW_SAVE_PARAMETERS	= "menuViewSaveParameters";
	
	public static final String			MENU_HELP					= "menuHelp";
	public static final String			MENU_HELP_ABOUT				= "menuHelpAbout";

	ApplicationModel					aModel;

	JMenu								menuSession					= new JMenu();
	JMenuItem							menuSessionNew				= new JMenuItem();
	JMenuItem							menuSessionClose			= new JMenuItem();
	JMenuItem							menuSessionOptions			= new JMenuItem();
	JMenuItem							menuSessionConnection		= new JMenuItem();
	JMenuItem							menuSessionChangePassword	= new JMenuItem();
	JMenuItem							menuSessionDomain			= new JMenuItem();
	JMenuItem							menuExit					= new JMenuItem();

	private JMenu						menuView;
	private JMenu						menuHelp;

	private ApplicationModelListener	applicationModelListener;

	public ScheduleMainMenuBar() {

		ActionListener actionAdapter = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (ScheduleMainMenuBar.this.aModel == null)
					return;
				AbstractButton jb = (AbstractButton) e.getSource();
				String s = jb.getName();
				Command command = ScheduleMainMenuBar.this.aModel.getCommand(s);
				command = (Command) command.clone();
				command.execute();
			}
		};

		this.menuSession.setText(LangModel.getString("menuSession"));
		this.menuSession.setName("menuSession");
		this.menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		this.menuSessionNew.setName("menuSessionNew");
		this.menuSessionNew.addActionListener(actionAdapter);
		this.menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		this.menuSessionClose.setName("menuSessionClose");
		this.menuSessionClose.addActionListener(actionAdapter);
		this.menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		this.menuSessionOptions.setName("menuSessionOptions");
		this.menuSessionOptions.addActionListener(actionAdapter);
		this.menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		this.menuSessionConnection.setName("menuSessionConnection");
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		this.menuSessionChangePassword.setName("menuSessionChangePassword");
		this.menuSessionChangePassword.addActionListener(actionAdapter);
		this.menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		this.menuSessionDomain.setName("menuSessionDomain");
		this.menuSessionDomain.addActionListener(actionAdapter);
		this.menuExit.setText(LangModel.getString("menuExit"));
		this.menuExit.setName("menuExit");
		this.menuExit.addActionListener(actionAdapter);

		this.menuView = new JMenu(LangModelSchedule.getString("View"));
		this.menuView.setName(MENU_VIEW);

		final JMenuItem menuViewPlanItem = new JMenuItem(LangModelSchedule.getString("Plan.Title"));
		menuViewPlanItem.setName(MENU_VIEW_PLAN);
		menuViewPlanItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewPlanItem);

		final JMenuItem menuViewTimeItem = new JMenuItem(LangModelSchedule.getString("TemporalType.Title"));
		menuViewTimeItem.setName(MENU_VIEW_TIME);
		menuViewTimeItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewTimeItem);

		final JMenuItem menuViewTreeItem = new JMenuItem(LangModelSchedule.getString("Comonents_Tree"));
		menuViewTreeItem.setName(MENU_VIEW_TREE);
		menuViewTreeItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewTreeItem);

		final JMenuItem menuViewParametersItem = new JMenuItem(LangModelSchedule.getString("Measurement_options"));
		menuViewParametersItem.setName(MENU_VIEW_PARAMETERS);
		menuViewParametersItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewParametersItem);

		final JMenuItem menuViewPropertiesItem = new JMenuItem(LangModelSchedule.getString("TestOptions"));
		menuViewPropertiesItem.setName(MENU_VIEW_PROPERTIES);
		menuViewPropertiesItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewPropertiesItem);

		final JMenuItem menuViewTableItem = new JMenuItem(LangModelSchedule.getString("Tests_status_and_characters"));
		menuViewTableItem.setName(MENU_VIEW_TABLE);
		menuViewTableItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewTableItem);

		final JMenuItem menuViewSaveParametersItem = new JMenuItem(LangModelSchedule.getString("Saving_options"));
		menuViewSaveParametersItem.setName(MENU_VIEW_SAVE_PARAMETERS);
		menuViewSaveParametersItem.addActionListener(actionAdapter);
		this.menuView.add(menuViewSaveParametersItem);
		
		this.menuHelp = new JMenu(LangModelSchedule.getString("Help"));
		this.menuHelp.setName(MENU_HELP);
		
		final JMenuItem menuHelpAboutItem = new JMenuItem(LangModelSchedule.getString("About_program"));
		menuHelpAboutItem.setName(MENU_HELP_ABOUT);
		this.menuHelp.add(menuHelpAboutItem);
		

		this.menuSession.add(this.menuSessionNew);
		this.menuSession.add(this.menuSessionClose);
		this.menuSession.add(this.menuSessionOptions);
		this.menuSession.add(this.menuSessionChangePassword);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionConnection);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionDomain);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuExit);

		this.add(this.menuSession);
		this.add(this.menuView);
		this.add(this.menuHelp);

		this.applicationModelListener = new ApplicationModelListener() {

			public void modelChanged(String e[]) {
				ScheduleMainMenuBar.this.menuSession.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSession"));
				ScheduleMainMenuBar.this.menuSession.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSession"));
				ScheduleMainMenuBar.this.menuSessionNew.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionNew"));
				ScheduleMainMenuBar.this.menuSessionNew.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionNew"));
				ScheduleMainMenuBar.this.menuSessionClose.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionClose"));
				ScheduleMainMenuBar.this.menuSessionClose.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionClose"));
				ScheduleMainMenuBar.this.menuSessionOptions.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionOptions"));
				ScheduleMainMenuBar.this.menuSessionOptions.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionOptions"));
				ScheduleMainMenuBar.this.menuSessionConnection.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionConnection"));
				ScheduleMainMenuBar.this.menuSessionConnection.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionConnection"));
				ScheduleMainMenuBar.this.menuSessionChangePassword.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionChangePassword"));
				ScheduleMainMenuBar.this.menuSessionChangePassword.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionChangePassword"));
				ScheduleMainMenuBar.this.menuSessionDomain.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible("menuSessionDomain"));
				ScheduleMainMenuBar.this.menuSessionDomain.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled("menuSessionDomain"));

				menuViewPlanItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_PLAN));
				menuViewPlanItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_PLAN));

				menuViewTimeItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_TIME));
				menuViewTimeItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_TIME));

				menuViewTreeItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_TREE));
				menuViewTreeItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_TREE));

				menuViewParametersItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_PARAMETERS));
				menuViewParametersItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_PARAMETERS));

				menuViewPropertiesItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_PROPERTIES));
				menuViewPropertiesItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_PROPERTIES));

				menuViewTableItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_VIEW_TABLE));
				menuViewTableItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_VIEW_TABLE));

				menuViewSaveParametersItem.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible(MENU_VIEW_SAVE_PARAMETERS));
				menuViewSaveParametersItem.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled(MENU_VIEW_SAVE_PARAMETERS));

				menuHelpAboutItem.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_HELP_ABOUT));
				menuHelpAboutItem.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_HELP_ABOUT));

			}

		};
	}

	public ScheduleMainMenuBar(ApplicationModel aModel) {
		this();
		this.aModel = aModel;
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	/**
	 * @return Returns the applicationModelListener.
	 */
	public ApplicationModelListener getApplicationModelListener() {
		return this.applicationModelListener;
	}
}