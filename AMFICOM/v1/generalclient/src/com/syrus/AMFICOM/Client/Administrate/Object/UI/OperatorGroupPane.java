/*
 * $Id: OperatorGroupPane.java,v 1.6 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
public final class OperatorGroupPane extends JPanel implements ObjectResourcePropertiesPane {
	private static OperatorGroupPane instance = null;

  OperatorGroupGeneralPanel genPanel = new OperatorGroupGeneralPanel();
  TwoListsPanel usersPanel = new TwoListsPanel("Подключенные пользователи",
                                                  "Неподключенные пользователи", User.class.getName());

  ApplicationContext aContext = new ApplicationContext();
  NewUpDater updater;
  User user;
  JTabbedPane jtp = new JTabbedPane();
  BorderLayout borderLayout1 = new BorderLayout();

  SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

  OperatorGroup group;

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public OperatorGroupPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public OperatorGroupPane(OperatorGroup group) {
		this();
		setObjectResource(group);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.add(jtp, BorderLayout.CENTER);
		jtp.add(genPanel.getName(), genPanel);
		jtp.add("Пользователи", usersPanel);
	}

  public ObjectResource getObjectResource()
  {
    return group;
  }

  public void setObjectResource(ObjectResource or)
  {
    ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
                                   Pool.get("ObjectFrame", "AdministrateObjectFrame");
    if(f!=null)
    {
      f.setTitle("Группы");
    }

    if(!Checker.checkCommand(user, Checker.readGroupInfo))
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
    this.group = (OperatorGroup )or;
    group.updateLocalFromTransferable();
    genPanel.setObjectResource(group);
    usersPanel.setObjectResource(group);
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    genPanel.setContext(aContext);
    usersPanel.setContext(aContext);
    this.user = (User)Pool.get(User.class.getName(),
                               aContext.getSessionInterface().getUserId());

//    this.dispatcher = aContext.getDispatcher();
//    this.dispatcher.register(this, OperatorGroup.class.getName()+"updated");

    this.updater = new NewUpDater(this.aContext);

  }

  public boolean modify()
  {
    if(!Checker.checkCommand(user, Checker.modifyGroup))
    {
//	    this.showTheWindow(false);
      this.setData(group);
      return false;
    }



    genPanel.modify();
    usersPanel.modify(group.user_ids);

    Date d = new Date();
    group.modified = d.getTime();
    group.modified_by = user.getId();

    if(!NewUpDater.checkName(group))
      return false;

    updater.updateObjectResources(group, false);
    Pool.put(OperatorGroup.class.getName(), group.getId(), group);
		aContext.getDataSource().SaveGroup(group.getId());

    this.setData(group);
    return true;
  }

  public boolean save()
  {
    return modify();
  }

  public boolean create()
  {
    if(user == null)
      return false;
    if(!Checker.checkCommand(user, Checker.addGroup))
    {
      this.showTheWindow(false);
      return false;
    }
    this.showTheWindow(true);
		DataSourceInterface dataSource = aContext.getDataSource();
    group = new OperatorGroup(); //creating of the new group
    group.getId() = dataSource.GetUId(OperatorGroup.class.getName());
    group.owner_id = user.getId(); // setting of the user (owner)
    group.created_by = user.getId();
    group.modified_by = user.getId();
    Date d = new Date(); // Setting of the creation time
    group.created = d.getTime();
    group.modified = d.getTime();
    Pool.put(OperatorGroup.class.getName(), group.getId(), group);

    setData(group);

		this.aContext.getDataSource().SaveGroup(group.getId());
		return true;
	}

  public boolean delete()
  {
    if(Checker.checkCommand(user, Checker.removeGroup))
    {
      this.group.user_ids = new ArrayList();

      updater.updateObjectResources(group, true);

      Pool.put(OperatorGroup.class.getName(), group.getId(), group);

      String[] s = new String[1];
      s[0] = group.getId();
			this.aContext.getDataSource().RemoveGroup(s);
			Pool.remove(group);
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
    this.setData(group);
    return true;
  }


  void showTheWindow(boolean key)
  {
    this.jtp.setVisible(key);
    repaint();
  }

	public static OperatorGroupPane getInstance() {
		if (instance == null)
			synchronized (OperatorGroupPane.class) {
				if (instance == null)
					instance = new OperatorGroupPane();
			}
		return instance;
	}
}

