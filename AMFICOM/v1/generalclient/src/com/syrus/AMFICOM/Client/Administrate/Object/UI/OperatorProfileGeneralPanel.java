/*
 * $Id: OperatorProfileGeneralPanel.java,v 1.2 2004/08/17 15:02:50 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

import java.awt.*;
import java.awt.event.*;

import java.text.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import oracle.jdeveloper.layout.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:50 $
 * @module generalclient_v1
 */
public class OperatorProfileGeneralPanel extends GeneralPanel
{
/*
  public static final String []month = {"Январь", "Февраль", "Март",
    "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь",
    "Октябрь", "Ноябрь", "Декабрь"};
  public static final String []year = {"2003", "2004", "2005", "2006",
    "2007", "2008", "2009", "2010",
    "2011", "2012", "2013", "2014",
    "2015", "2016", "2017", "2018",
    "2019", "2020", "2021", "2022",};

  public static final String []day  = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
    "31"};
*/
  JLabel jLabelLogin = new JLabel();
  JLabel jLabelId = new JLabel();
  JLabel jLabelOwner = new JLabel();
  JLabel jLabelCreated = new JLabel();
  JLabel jLabelCreatedBy = new JLabel();
  JLabel jLabelModified = new JLabel();
  JLabel jLabelModifiedBy = new JLabel();
  JLabel jLabelLastSession = new JLabel();
  JLabel jLabelCategories = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabelLogFile = new JLabel();
  JLabel jLabelRemarks = new JLabel();

  ObjectResourceTextField profileLogin = new ObjectResourceTextField();
  ObjectResourceTextField profileId = new ObjectResourceTextField();
  ObjectResourceTextField profileCreated = new ObjectResourceTextField();
  ObjectResourceTextField profileCreatedBy = new ObjectResourceTextField();
  ObjectResourceTextField profileModified = new ObjectResourceTextField();
  ObjectResourceTextField profileModifiedBy = new ObjectResourceTextField();
  ObjectResourceTextField profileLastSession = new ObjectResourceTextField();
  ObjectResourceTextField profileLogFile = new ObjectResourceTextField();

  OrComboBox profileOwner = new OrComboBox();

  JEditorPane profileNotes = new JEditorPane();
  OperatorProfile profile;
  JScrollPane jScrollPane2 = new JScrollPane();
  JPanel jPanel1 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel2 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  TitledBorder titledBorder1;
  JPanel jPanelDeadDate = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel jLabel1ExpiredDate = new JLabel();
/*
  JComboBox jComboDay = new AComboBox();
  JComboBox jComboMonth = new AComboBox();
  JComboBox jComboYear = new AComboBox();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel jLabel18 = new JLabel();
*/
  private JCheckBox CheckBoxUnlimitedAccess = new JCheckBox();
  private JLabel jLabel19 = new JLabel();
  private TwoListsPanel categoriesPanel = new TwoListsPanel("Подключенные категории", "Неподключенные категории", OperatorCategory.typ);
  private TitledBorder titledBorder2;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

		JButton dateButton = new JButton("^");
		JSpinner dateSpinner = new DateSpinner();

  public OperatorProfileGeneralPanel()
  {
    super();
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public OperatorProfileGeneralPanel(OperatorProfile profile)
  {
    this();
    setObjectResource(profile);
  }

  private void jbInit() throws Exception
  {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    setName("Общие");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

    this.setLayout(gridBagLayout1);

    jLabelLogin.setText("Логин");
    jLabelId.setText("Идентификатор");
    jLabelOwner.setText("Владелец");
    jLabelCreated.setText("Дата создания");
    jLabelCreatedBy.setText("Кем создан");
    jLabelModified.setText("Дата изменения");
    jLabelModifiedBy.setText("Кем изменен");
    jLabelLastSession.setText("Последняя сессия");
    jLabelCategories.setText("Категории");
    jLabelLogFile.setText("Лог-файл");
    jLabelRemarks.setText("Примечания");

    jLabelLogin.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLogin.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelId.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelId.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelOwner.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelOwner.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreatedBy.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreatedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLastSession.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLastSession.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCategories.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCategories.setPreferredSize(new Dimension(DEF_WIDTH, 220));
    jLabelLogFile.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLogFile.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelRemarks.setMinimumSize(new Dimension(DEF_WIDTH, 60));
    jLabelRemarks.setPreferredSize(new Dimension(DEF_WIDTH, 150));

    Color disabledColor = jLabelLogin.getBackground();
		this.setSize(new Dimension(663, 640));
/*
    profileId.setEditable(false);
    profileId.setForeground(Color.gray);
//    profileNotes.setWrapStyleWord(true);
    profileCreated.setForeground(Color.gray);
    profileCreated.setEditable(false);
    profileCreatedBy.setForeground(Color.gray);
    profileCreatedBy.setEditable(false);
    profileModified.setForeground(Color.gray);
    profileModified.setEditable(false);
    profileModifiedBy.setForeground(Color.gray);
    profileModifiedBy.setEditable(false);
*/
    profileId.setEnabled(false);
    profileCreated.setEnabled(false);
    profileCreatedBy.setEnabled(false);
    profileModified.setEnabled(false);
    profileModifiedBy.setEnabled(false);
	
//    profileNotes.setWrapStyleWord(true);

    jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane2.setPreferredSize(new Dimension(461, 150));

    jPanelDeadDate.setLayout(flowLayout1);
//    jComboDay.setMinimumSize(new Dimension(40, 21));
//    jComboDay.setPreferredSize(new Dimension(40, 21));
    jPanelDeadDate.setPreferredSize(new Dimension(346, 30));
    jLabel1ExpiredDate.setMaximumSize(new Dimension(96, 21));
    jLabel1ExpiredDate.setMinimumSize(new Dimension(96, 21));
    jLabel1ExpiredDate.setPreferredSize(new Dimension(115, 30));
    jLabel1ExpiredDate.setText("Действителен до");
//    jLabel16.setText("День");
//    jComboYear.setMinimumSize(new Dimension(80, 21));
//    jComboYear.setPreferredSize(new Dimension(80, 21));
//    jLabel17.setText("   Месяц");
//    jComboMonth.setMinimumSize(new Dimension(90, 21));
//    jComboMonth.setPreferredSize(new Dimension(90, 21));
//    jLabel18.setText("   Год");
    CheckBoxUnlimitedAccess.setText("  ");
    CheckBoxUnlimitedAccess.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CheckBoxUnlimitedAccess_actionPerformed(e);
      }
    });
    jLabel19.setText("Бессрочно");
    profileLastSession.setForeground(Color.gray);
    profileLastSession.setEditable(false);
    categoriesPanel.setBorder(titledBorder2);
    categoriesPanel.setPreferredSize(new Dimension(461, 220));

	dateButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showCalendar();
			}
		});

    jPanelDeadDate.add(jLabel19, null);
    jPanelDeadDate.add(CheckBoxUnlimitedAccess, null);
		jPanelDeadDate.add(dateSpinner, null);
		jPanelDeadDate.add(dateButton, null);
//    jPanelDeadDate.add(jLabel16, null);
//    jPanelDeadDate.add(jComboDay, null);
//    jPanelDeadDate.add(jLabel17, null);
//    jPanelDeadDate.add(jComboMonth, null);
//    jPanelDeadDate.add(jLabel18, null);
//    jPanelDeadDate.add(jComboYear, null);
    jScrollPane2.getViewport().add(profileNotes, null);

    this.add(jLabelLogin, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(jLabelOwner, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreated, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreatedBy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModified, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModifiedBy, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelLastSession, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel1ExpiredDate, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCategories, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelLogFile, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelRemarks, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelId, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(profileLogin, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(profileOwner, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileCreated, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileCreatedBy, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileModified, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileModifiedBy, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileLastSession, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelDeadDate, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(categoriesPanel, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileLogFile, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane2, new GridBagConstraints(1, 10, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(profileId, new GridBagConstraints(1, 11, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 12, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
/*
    for(int i=0; i<day.length; i++)
    {
      jComboDay.addItem(day[i]);
    }
    for(int i=0; i<year.length; i++)
    {
      jComboYear.addItem(year[i]);
    }
    for(int i=0; i<month.length; i++)
    {
      jComboMonth.addItem(month[i]);
    }
*/
    CheckBoxUnlimitedAccess.setFocusPainted(false);
/*
    profileId.setBackground(Color.white);
    profileCreated.setBackground(Color.white);
    profileCreatedBy.setBackground(Color.white);
    profileModified.setBackground(Color.white);
    profileModifiedBy.setBackground(Color.white);
    profileLastSession.setBackground(Color.white);
*/

  }

  public ObjectResource getObjectResource()
  {
    return profile;
  }

  public void setObjectResource(ObjectResource or)
  {
    this.profile = (OperatorProfile )or;
    if(profile == null)
      return;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
//    System.out.println("set prop pane to " + profile.name);

    profileLogin.setText(profile.login);
    profileId.setText(profile.id);

    profileOwner.setTyp(User.typ);
    profileOwner.setSelectedTyp(User.typ, profile.owner_id);

    profileCreated.setText(sdf.format(new Date(profile.created)));
    profileCreatedBy.setTextNameByID(User.typ, profile.created_by);

    if(profile.disabled == 0)
    {
//      System.out.println("Profile.disabled (set object resource) = " +
//                         profile.disabled);
      this.CheckBoxUnlimitedAccess.setSelected(true);
      this.setDisabledDataAccess(true);
    }
    else
    {

//      System.out.println("Profile.disabled = " + profile.disabled);
      Date disableTime = new Date(profile.disabled);
/*	  
//      System.out.println("DIsable time is set to"+sdf.format(disableTime));
      this.jComboDay.setSelectedIndex(disableTime.getDate()-1);
      this.jComboMonth.setSelectedIndex(disableTime.getMonth());
      int indexYear = disableTime.getYear()-2003+1900;
      if(indexYear<0)
        indexYear=0;
      if(indexYear>10)
        indexYear=0;
      this.jComboYear.setSelectedIndex(indexYear);
*/
		dateSpinner.setValue(disableTime);
		
      this.CheckBoxUnlimitedAccess.setSelected(false);
      this.setDisabledDataAccess(false);
    }

    profileModified.setText(sdf.format(new Date(profile.modified)));
    profileModifiedBy.setTextNameByID(User.typ, profile.modified_by);
    profileLastSession.setText(sdf.format(new Date(profile.last_login)));
//		profileStatus.setText(profile.status);
//		profileState.setText(profile.state);
//		profileDisabled.setText(sdf.format(new Date(profile.disabled)));
//		profileDisabledComments.setText(profile.disabled_comments);
    profileLogFile.setText(profile.logfile);
    profileNotes.setText(profile.description);

    categoriesPanel.setObjectResource(profile);

  }

  public boolean modify()
  {
    profile.login = this.profileLogin.getText();
    profile.name = this.profileLogin.getText();

    profile.description = this.profileNotes.getText();
    profile.logfile = this.profileLogFile.getText();

    profile.owner_id =
        profileOwner.getSelectedId();

    if(this.CheckBoxUnlimitedAccess.isSelected())
    {
      profile.disabled =0;
//      System.out.println("Profile.disabled = " + profile.disabled);
    }
    else
    {
//		Calendar date_cal = Calendar.getInstance();
//		date_cal.setTime((Date)dateSpinner.getValue());
/*
      Date disableTime = new Date();
      disableTime.setDate(this.jComboDay.getSelectedIndex()+1);
      disableTime.setMonth(this.jComboMonth.getSelectedIndex());
      disableTime.setYear(this.jComboYear.getSelectedIndex()+2003-1900);
      disableTime.getYear();
//      disableTime.
      profile.disabled = disableTime.getTime();
*/
      profile.disabled = ((Date)dateSpinner.getValue()).getTime();

      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    }


    categoriesPanel.modify(profile.category_ids);

    return true;
  }

  public boolean save()
  {
    return true;
  }

	class CalendarFocusListener implements FocusListener
	{
		JDialog jd;
		public CalendarFocusListener(JDialog jd)
		{
			this.jd = jd;
		}
				
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) 
		{
			jd.setVisible(false);
		}
	}

	class CalendarKeyListener implements KeyListener
	{
		JDialog jd;
		public CalendarKeyListener(JDialog jd)
		{
			this.jd = jd;
		}

		public void keyPressed(KeyEvent e) 
		{
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				jd.setVisible(false);
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
/*
	class CalendarMouseListener implements MouseListener
	{
		JDialog jd;
		public CalendarMouseListener(JDialog jd)
		{
			this.jd = jd;
		}

		public void mouseClicked(MouseEvent e)
		{
				System.out.println("mouse clicked outside bounds");
//				jd.setVisible(false);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
*/
	void showCalendar()
	{
		Calendar cal = Calendar.getInstance();
		Date date = (Date)dateSpinner.getModel().getValue();
		cal.setTime(date);

		JDialog calendarDialog = CalendarUI.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
		calendarDialog.addFocusListener(new CalendarFocusListener(calendarDialog));
//		calendarDialog.addMouseListener(new CalendarMouseListener(calendarDialog));
		calendarDialog.addKeyListener(new CalendarKeyListener(calendarDialog));
		calendarDialog.setSize(new Dimension(200, 200));
		calendarDialog.setResizable(false);
		calendarDialog.setLocation(
			new Point(dateSpinner.getLocationOnScreen().x - 35,
			dateSpinner.getLocationOnScreen().y + dateSpinner.getBounds().height));
		calendarDialog.setVisible(true);
		if (((CalendarUI)calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
			dateSpinner.getModel().setValue(cal.getTime());
	}

  void CheckBoxUnlimitedAccess_actionPerformed(ActionEvent e)
  {
	setDisabledDataAccess(CheckBoxUnlimitedAccess.isSelected());
/*
    if(this.CheckBoxUnlimitedAccess.isSelected())
    {
      this.jComboDay.setEnabled(false);
      this.jComboMonth.setEnabled(false);
      this.jComboYear.setEnabled(false);

      Color c = this.getBackground();

      this.jComboDay.setBackground(c);
      this.jComboMonth.setBackground(c);
      this.jComboYear.setBackground(c);
    }
    else
    {
      this.jComboDay.setEnabled(true);
      this.jComboMonth.setEnabled(true);
      this.jComboYear.setEnabled(true);
      this.jComboDay.setBackground(Color.white);
      this.jComboMonth.setBackground(Color.white);
      this.jComboYear.setBackground(Color.white);
    }
*/
  }

  private void setDisabledDataAccess(boolean key)
  {
		dateSpinner.setEnabled(!key);
		dateButton.setEnabled(!key);
/*
    if(key)
    {
      this.jComboDay.setEnabled(false);
      this.jComboMonth.setEnabled(false);
      this.jComboYear.setEnabled(false);

      Color c = this.getBackground();

      this.jComboDay.setBackground(c);
      this.jComboMonth.setBackground(c);
      this.jComboYear.setBackground(c);
    }
    else
    {
      this.jComboDay.setEnabled(true);
      this.jComboMonth.setEnabled(true);
      this.jComboYear.setEnabled(true);
      this.jComboDay.setBackground(Color.white);
      this.jComboMonth.setBackground(Color.white);
      this.jComboYear.setBackground(Color.white);
    }
*/
  }

}

