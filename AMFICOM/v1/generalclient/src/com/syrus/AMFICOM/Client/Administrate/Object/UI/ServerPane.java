/*
 * $Id: ServerPane.java,v 1.5 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.*;
import java.util.Date;
import javax.swing.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
public final class ServerPane extends JPanel implements ObjectResourcePropertiesPane {
	private static ServerPane instance = null;

  Server server;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  Checker checker;

  ServerPanel serverPanel = new ServerPanel();

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public ServerPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public ServerPane(StorableObject or) {
		this();
		setObjectResource(or);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(serverPanel, BorderLayout.CENTER);
	}

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    String userId = aContext.getSessionInterface().getUserId();
    user = (User)Pool.get(User.class.getName(), userId);
    checker = new Checker(user);
  }

  public void setObjectResource(StorableObject or)
  {
    if(!checker.checkCommand(Checker.readServerInfo))
      return;

    if(or == null)
      return;
    server = (Server)or;
    serverPanel.setObjectResource(server);

  }

  public StorableObject getObjectResource()
  {
    return server;
  }

  public boolean save()
  {
    if(!checker.checkCommand(Checker.modifyServerInfo))
      return false;

    server = (Server)serverPanel.getModifiedObjectResource();

    if(!NewUpDater.checkName(server))
      return false;

    Pool.put(Server.class.getName(), server.getId(), server);
    aContext.getDataSource().SaveServer(server.getId());
    return true;
  }

  public boolean create()
  {
    if(true)
    {
      String error = "Сервер не может быть создан.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }


    if(!checker.checkCommand(Checker.createServer))
      return false;

    String id = aContext.getDataSource().GetUId(Server.class.getName());

    server = new Server();
    server.getId() = id;
    Date d = new Date();
    server.created = d.getTime();
    server.modified = d.getTime();

    Pool.put(Server.class.getName(), server.getId(), server);


    setObjectResource(server);

    save();

    return true;
  }


  public boolean open()
  {
    return true;
  }


  public boolean delete()
  {
    if(true)
    {
      String error = "Сервер не может быть удален.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }

    if(!checker.checkCommand(Checker.deleteServer))
      return false;

    String []s = new String[1];

    s[0] = server.getId();
    aContext.getDataSource().RemoveServer(s);
    Pool.remove(Server.class.getName(), server.getId());
    return true;
  }

  public boolean modify()
  {
    return save();
  }

	public boolean cancel() {
		return false;
	}

	public static ServerPane getInstance() {
		if (instance == null)
			synchronized (ServerPane.class) {
				if (instance == null)
					instance = new ServerPane();
			}
		return instance;
	}
}
