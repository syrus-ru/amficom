package com.syrus.AMFICOM.Client.Administrate.Object.UI;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;


public class ServerPane  extends PropertiesPanel
{
  Server server;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  Checker checker;

  ServerPanel serverPanel = new ServerPanel();

  public ServerPane()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public ServerPane(ObjectResource or)
  {
    this();
    setObjectResource(or);
  }

  void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(500, 500));
    this.setLayout(borderLayout1);

    this.setBorder(BorderFactory.createRaisedBevelBorder());

    this.add(serverPanel, BorderLayout.CENTER);
//    this.add(pac, BorderLayout.SOUTH);
  }



  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    String userId = aContext.getSessionInterface().getUserId();
    user = (User)Pool.get(User.typ, userId);
    checker = new Checker(user);
  }


  public boolean setObjectResource(ObjectResource or)
  {
    if(!checker.checkCommand(Checker.readServerInfo))
      return false;

    if(or == null)
      return false;
    server = (Server)or;
    serverPanel.setObjectResource(server);

    return true;
  }

  public ObjectResource getObjectResource()
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

    Pool.put(Server.typ, server.id, server);
    aContext.getDataSourceInterface().SaveServer(server.id);
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

    String id = aContext.getDataSourceInterface().GetUId(Server.typ);

    server = new Server();
    server.id = id;
    Date d = new Date();
    server.created = d.getTime();
    server.modified = d.getTime();

    Pool.put(Server.typ, server.id, server);


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

    s[0] = server.id;
    aContext.getDataSourceInterface().RemoveServer(s);
    Pool.remove(Server.typ, server.id);
    return true;
  }

  public boolean modify()
  {
    return save();
  }
}