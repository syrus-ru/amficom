/*
 * $Id: OperatorProfilePane.java,v 1.3 2004/08/20 12:05:00 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2004/08/20 12:05:00 $
 * @module generalclient_v1
 */
public class OperatorProfilePane extends PropertiesPanel
{
  ApplicationContext aContext = new ApplicationContext();
  SimpleDateFormat sdf = new SimpleDateFormat();
  User user;
  User operatorProfileUser;
  NewUpDater updater;

  OperatorProfileGeneralPanel genPanel = new OperatorProfileGeneralPanel();
//  OperatorProfileGroupsPanel gPanel = new OperatorProfileGroupsPanel();
  TwoListsPanel groupsPanel = new TwoListsPanel("Подключенные группы", "Неподключенные группы", OperatorGroup.typ);
  OperatorProfileOtherPanel othPanel = new OperatorProfileOtherPanel();
	OperatorProfilePasswordPanel passPanel = new OperatorProfilePasswordPanel();


  JTabbedPane jtp = new JTabbedPane();

  OperatorProfile profile;

  BorderLayout borderLayout1 = new BorderLayout();

  public OperatorProfilePane()
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

  public OperatorProfilePane(OperatorProfile profile)
  {
    this();
    setObjectResource(profile);
  }

  private void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(500, 500));
    this.setLayout(borderLayout1);
    jtp.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jtp_stateChanged(e);
      }
    });
    this.add(jtp, BorderLayout.CENTER);
    jtp.add(genPanel.getName(), genPanel);
		jtp.add(passPanel, passPanel.getName());
    jtp.add("Группы", groupsPanel);
    jtp.add(othPanel.getName(), othPanel);

  }

  public ObjectResource getObjectResource()
  {
    return profile;
  }

  public void setObjectResource(ObjectResource or)
  {
    this.operatorProfileUser = (User)Pool.get(User.typ, ((OperatorProfile)or).user_id);

    ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
                                   Pool.get("ObjectFrame", "AdministrateObjectFrame");
    if(f!=null)
    {
      f.setTitle("Профили");
    }

    if(!Checker.checkCommand(user, Checker.readProfileInfo))
    {
      this.showTheWindow(false);
      setData(or);
      return;
    }
    this.showTheWindow(true);
    setData(or);
  }

  private void setData(ObjectResource or)
  {

    this.profile = (OperatorProfile )or;
    profile.updateLocalFromTransferable();
    this.operatorProfileUser = (User)Pool.get(User.typ, profile.user_id);
    user.updateLocalFromTransferable();

    genPanel.setObjectResource(profile);
    groupsPanel.setObjectResource(profile);
    othPanel.setObjectResource(profile);
		passPanel.setObjectResource(profile);
  }


  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    genPanel.setContext(aContext);
    groupsPanel.setContext(aContext);
    othPanel.setContext(aContext);
		passPanel.setContext(aContext);
    this.user = (User)(Pool.get(User.typ,
                                aContext.getSessionInterface().getUserId()));
    updater = new NewUpDater(this.aContext);
  }


  public boolean modify()
  {
    if(!Checker.checkCommand(user, Checker.modifyProfile))
    {
      this.setData(profile);
      return false;
    }



    this.showTheWindow(true);
    DataSourceInterface dataSource = aContext.getDataSourceInterface();
    genPanel.modify();
//    gPanel.modify();
    groupsPanel.modify(profile.group_ids);

    othPanel.modify();
		passPanel.modify();

    if(profile.password == null || profile.password.equals(""))
    {
      String error = "Не установлен пароль пользователя.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }

    profile.name = profile.login;
    profile.modified_by = user.id;
    Date d = new Date();
    profile.modified = d.getTime();

    Pool.put(OperatorProfile.typ, profile.id, profile);


    operatorProfileUser.last_login = profile.last_login;
    operatorProfileUser.login = profile.login;

    {
      operatorProfileUser.group_ids = new ArrayList();
      for(int i=0; i<profile.group_ids.size(); i++)
      {
        operatorProfileUser.group_ids.add(profile.group_ids.get(i));
      }

      operatorProfileUser.category_ids = new ArrayList();
      for(int i=0; i<profile.category_ids.size(); i++)
      {
        operatorProfileUser.category_ids.add(profile.category_ids.get(i));
      }
    }
    Pool.put(User.typ, operatorProfileUser.id, operatorProfileUser);
    operatorProfileUser.updateLocalFromTransferable();


    if(!NewUpDater.checkName(profile))
      return false;

    updater.updateObjectResources(profile, false);
    updater.updateObjectResources(operatorProfileUser, false);

    profile.setTransferableFromLocal();
    operatorProfileUser.setTransferableFromLocal();

    aContext.getDataSourceInterface().SaveUser(operatorProfileUser.id);
    aContext.getDataSourceInterface().SaveOperatorProfile(profile.id);



    setData(profile);
    return true;
  }

  public boolean save()
  {
    return modify();
  }

  public boolean create()
  {
    if(!Checker.checkCommand(user, Checker.addProfile))
    {
      this.showTheWindow(false);
      return false;
    }

    this.showTheWindow(true);
    if(user == null)
      return false;
    DataSourceInterface dataSource = aContext.getDataSourceInterface();

    profile = new OperatorProfile();
    profile.id = dataSource.GetUId(OperatorProfile.typ);
    profile.login = profile.id;
    profile.name = profile.id;
    profile.created_by = this.user.getId();
    profile.owner_id = this.user.getId();
    profile.modified_by = this.user.getId();
    Date d = new Date(); // Setting of the creation time
    profile.created = d.getTime();
    profile.modified= d.getTime();
    d.setYear(d.getYear() + 1);
    profile.disabled= d.getTime();


    operatorProfileUser = new User();
    operatorProfileUser.id = dataSource.GetUId(User.typ);
    operatorProfileUser.type = OperatorProfile.typ;
    operatorProfileUser.object_id = profile.id;
    profile.user_id = operatorProfileUser.id;

    profile.setTransferableFromLocal();
    operatorProfileUser.setTransferableFromLocal();

    Pool.put(User.typ, operatorProfileUser.id, operatorProfileUser);
    Pool.put(OperatorProfile.typ, profile.id, profile);
    profile.updateLocalFromTransferable();
    setData(profile);


    aContext.getDataSourceInterface().SaveUser(operatorProfileUser.id);
    aContext.getDataSourceInterface().SaveOperatorProfile(profile.id);
    return true;
  }

  public boolean delete()
  {
    if(profile.login.equals("sys") &&  profile.id.equals("sysuser"))
    {
      String error = " Пользователь SYS не может быть удален \n ни при каких условиях.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }

//    Checking, if the user to be deleted is logged in
    String []loggedUsersIds;
    try
    {
      loggedUsersIds = aContext.getDataSourceInterface().GetLoggedUserIds();
    }
    catch (Exception ex)
    {
      return false;
    }
    if(loggedUsersIds == null)
    {
//      System.out.println("Error: Array of the logged users is not set.");
      return false;
    }

    for(int i=0; i<loggedUsersIds.length; i++)
    {
//      System.out.println(loggedUsersIds[i]);
      if(operatorProfileUser.getId().equals(loggedUsersIds[i]))
      {
        String error = " Пользователь " + loggedUsersIds[i] +
                       " активен и на данный момент \n." +
                       " не может быть удален.";
        JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
        return false;
      }
    }


    if(Checker.checkCommand(user, Checker.removeProfile))
    {
//      System.out.println("Removing of the object "+profile.getName());

      profile.group_ids = new ArrayList();
      profile.category_ids = new ArrayList();
      operatorProfileUser.group_ids = new ArrayList();
      operatorProfileUser.category_ids = new ArrayList();

      Pool.put(OperatorProfile.typ, profile.id, profile);
      Pool.put(User.typ, operatorProfileUser.id, operatorProfileUser);

      updater.updateObjectResources(profile, true);
      updater.updateObjectResources(operatorProfileUser, true);

      String[]s = new String[1];
      s[0] = profile.id;
      this.aContext.getDataSourceInterface().RemoveOperatorProfile(s);
      s[0] = operatorProfileUser.id;
      this.aContext.getDataSourceInterface().RemoveUser(s);
      Pool.remove(profile);
      Pool.remove(operatorProfileUser);

      return true;
    }
    return false;
  }

  public boolean open()
  {
    return true;
  }

  public boolean cancel()
  {
    this.setData(profile);
    return true;
  }

	void jtp_stateChanged(ChangeEvent e)
	{
		this.passPanel.setLogin(this.genPanel.profileLogin.getText());
	}

  void showTheWindow(boolean key)
  {
    this.jtp.setVisible(key);
    repaint();
  }


}

