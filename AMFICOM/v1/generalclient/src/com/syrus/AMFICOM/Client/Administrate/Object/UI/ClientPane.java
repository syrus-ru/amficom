/*
 * $Id: ClientPane.java,v 1.5 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
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
public final class ClientPane extends JPanel implements ObjectResourcePropertiesPane {
	private static ClientPane instance = null;

  Client client;

  ApplicationContext aContext = new ApplicationContext();
  Dispatcher dispatcher = new Dispatcher();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  Checker checker;

  ClientPanel clientPanel = new ClientPanel();

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public ClientPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public ClientPane(StorableObject or) {
		this();
		setObjectResource(or);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(clientPanel, BorderLayout.CENTER);
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
    if(!checker.checkCommand(Checker.readClientInfo))
      return;

    if(or == null)
      return;
    client = (Client )or;
    clientPanel.setObjectResource(client);
  }

  public StorableObject getObjectResource()
  {
    return client;
  }

  public boolean save()
  {
    if(!checker.checkCommand(Checker.modifyClientInfo))
      return false;

    client = (Client)clientPanel.getModifiedObjectResource();

    if(!NewUpDater.checkName(client))
      return false;

    Pool.put(Client.class.getName(), client.getId(), client);
    aContext.getDataSource().SaveClient(client.getId());
    return true;
  }

  public boolean create()
  {
    if(true)
    {
      String error = "?????? ?? ????? ???? ??????.";
      JOptionPane.showMessageDialog(null, error, "??????", JOptionPane.OK_OPTION);
      return false;
    }


    if(!checker.checkCommand(Checker.createClient))
      return false;
    String id = aContext.getDataSource().GetUId(Client.class.getName());

    client = new Client();
    client.getId() = id;
    Date d = new Date();
    client.created = d.getTime();
    client.modified = d.getTime();

    Pool.put(Client.class.getName(), client.getId(), client);

    setObjectResource(client);

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
      String error = "?????? ?? ????? ???? ??????.";
      JOptionPane.showMessageDialog(null, error, "??????", JOptionPane.OK_OPTION);
      return false;
    }

    if(!checker.checkCommand(Checker.deleteClient))
      return false;

    String []s = new String[1];

    s[0] = client.getId();
    aContext.getDataSource().RemoveClient(s);
    Pool.remove(Client.class.getName(), client.getId());

    return true;
  }

  public boolean modify()
  {
    return save();
  }

	public boolean cancel() {
		return false;
	}

	public static ClientPane getInstance() {
		if (instance == null)
			synchronized (ClientPane.class) {
				if (instance == null)
					instance = new ClientPane();
			}
		return instance;
	}
}
