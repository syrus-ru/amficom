package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;

public class ScheduleMainMenuBar extends JMenuBar {

	public static final String			MENU_SESSION					= "menuSession";
	public static final String			MENU_SESSION_NEW				= "menuSessionNew";
	public static final String			MENU_SESSION_CLOSE				= "menuSessionClose";
	public static final String			MENU_SESSION_OPTIONS			= "menuSessionOptions";
	public static final String			MENU_SESSION_CONNECTION			= "menuSessionConnection";
	public static final String			MENU_SESSION_CHANGE_PASSWORD	= "menuSessionChangePassword";
	public static final String			MENU_SESSION_DOMAIN				= "menuSessionDomain";
	
	public static final String			MENU_EXIT						= "menuExit";

	public static final String			MENU_VIEW						= "menuView";
	public static final String			MENU_VIEW_PLAN					= "menuViewPlan";
	public static final String			MENU_VIEW_TIME					= "menuViewTime";
	public static final String			MENU_VIEW_TREE					= "menuViewTree";
	public static final String			MENU_VIEW_PARAMETERS			= "menuViewParameters";
	public static final String			MENU_VIEW_PROPERTIES			= "menuViewProperties";
	public static final String			MENU_VIEW_TABLE					= "menuViewTable";
	public static final String			MENU_VIEW_SAVE_PARAMETERS		= "menuViewSaveParameters";

	public static final String			MENU_HELP						= "menuHelp";
	public static final String			MENU_HELP_ABOUT					= "menuHelpAbout";

	ApplicationModel					aModel;

	JMenu								menuSession						= new JMenu();
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
		this.menuSession.setName(MENU_SESSION);

		final JMenuItem menuSessionNew = new JMenuItem();
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		menuSessionNew.setName(MENU_SESSION_NEW);
		menuSessionNew.addActionListener(actionAdapter);

		final JMenuItem menuSessionClose = new JMenuItem();
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName(MENU_SESSION_CLOSE);
		menuSessionClose.addActionListener(actionAdapter);

		final JMenuItem menuSessionOptions = new JMenuItem();
		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		menuSessionOptions.setName(MENU_SESSION_OPTIONS);
		menuSessionOptions.addActionListener(actionAdapter);

		final JMenuItem menuSessionConnection = new JMenuItem();
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName(MENU_SESSION_CONNECTION);
		menuSessionConnection.addActionListener(actionAdapter);

		final JMenuItem menuSessionChangePassword = new JMenuItem();
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName(MENU_SESSION_CHANGE_PASSWORD);
		menuSessionChangePassword.addActionListener(actionAdapter);

		final JMenuItem menuSessionDomain = new JMenuItem();
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName(MENU_SESSION_DOMAIN);
		menuSessionDomain.addActionListener(actionAdapter);

		JMenuItem menuExit = new JMenuItem();
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName(MENU_EXIT);
		menuExit.addActionListener(actionAdapter);

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

		this.menuSession.add(menuSessionNew);
		this.menuSession.add(menuSessionClose);
		this.menuSession.add(menuSessionOptions);
		this.menuSession.add(menuSessionChangePassword);
		this.menuSession.addSeparator();
		this.menuSession.add(menuSessionConnection);
		this.menuSession.addSeparator();
		this.menuSession.add(menuSessionDomain);
		this.menuSession.addSeparator();
		this.menuSession.add(menuExit);

		this.add(this.menuSession);
		this.add(this.menuView);
		this.add(this.menuHelp);

		this.applicationModelListener = new ApplicationModelListener() {

			public void modelChanged(String e[]) {
				ScheduleMainMenuBar.this.menuSession
						.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION));
				ScheduleMainMenuBar.this.menuSession
						.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION));
				menuSessionNew.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION_NEW));
				menuSessionNew.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION_NEW));
				menuSessionClose.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION_CLOSE));
				menuSessionClose.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION_CLOSE));
				menuSessionOptions.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION_OPTIONS));
				menuSessionOptions.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION_OPTIONS));
				menuSessionConnection.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION_CONNECTION));
				menuSessionConnection.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION_CONNECTION));
				menuSessionChangePassword.setVisible(ScheduleMainMenuBar.this.aModel
						.isVisible(MENU_SESSION_CHANGE_PASSWORD));
				menuSessionChangePassword.setEnabled(ScheduleMainMenuBar.this.aModel
						.isEnabled(MENU_SESSION_CHANGE_PASSWORD));
				menuSessionDomain.setVisible(ScheduleMainMenuBar.this.aModel.isVisible(MENU_SESSION_DOMAIN));
				menuSessionDomain.setEnabled(ScheduleMainMenuBar.this.aModel.isEnabled(MENU_SESSION_DOMAIN));

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