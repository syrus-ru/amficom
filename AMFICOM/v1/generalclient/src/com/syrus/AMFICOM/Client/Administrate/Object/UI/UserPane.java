package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;


import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

public class UserPane extends PropertiesPanel implements OperationListener
{

  User user;
  ApplicationContext aContext = new ApplicationContext();
  Dispatcher dispatcher;
  User loggedUser;
  BorderLayout borderLayout1 = new BorderLayout();
  UserPanel up = new UserPanel();


  public UserPane()
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

  public UserPane(User user)
  {
    this();
    this.setObjectResource(user);
  }


  private void jbInit()
  {
    this.setPreferredSize(new Dimension(500, 500));
    this.setLayout(borderLayout1);

//    this.add(up, BorderLayout.CENTER);
    this.add(up);
  }

  public boolean save()
  {
    return false;
  }

  public boolean modify()
  {
    return false;
  }

  public boolean cancel()
  {
    return false;
  }

  public boolean create()
  {
    RejectDialog rd = new RejectDialog
    (" Ошибка. Пользователь создается только \n посредством создания соответствующего профиля.");
    return false;
  }

  public boolean open()
  {
    return false;
  }

  public boolean delete()
  {
    RejectDialog rd = new RejectDialog
    (" Ошибка. Пользователь может быть удален только \n посредством удаления соответствующего профиля.");
    return false;
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    String userID = this.aContext.getSessionInterface().getUserId();
    this.dispatcher = this.aContext.getDispatcher();
    this.loggedUser = (User)Pool.get(User.typ, userID);

    this.dispatcher.register(this, User.typ+"updated");
  }

  public boolean setObjectResource(ObjectResource or)
  {
    ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
             Pool.get("ObjectFrame", "AdministrateObjectFrame");
    if(f!=null)
    {
      f.setTitle("Пользователи");
    }

    if(!Checker.checkCommand(loggedUser, Checker.readUserInfo))
    {
      this.showTheWindow(false);
      setData(or);
      return false;
    }
    this.showTheWindow(true);
    setData(or);
    return true;
  }

  private void setData(ObjectResource or)
  {
    this.user = (User)or;
    user.updateLocalFromTransferable();
    up.setObjectResource(or);
  }

  public ObjectResource getObjectResource()
  {
	  return user;
  }


  private void showTheWindow(boolean key)
  {
    this.up.setVisible(key);
    this.repaint();
  }

  public void operationPerformed(OperationEvent oe)
  {
    if(oe.getActionCommand().equals(User.typ+"updated") && user != null)
    {
      user = (User)Pool.get(User.typ, user.id);
      this.setData(user);
    }
  }


}
