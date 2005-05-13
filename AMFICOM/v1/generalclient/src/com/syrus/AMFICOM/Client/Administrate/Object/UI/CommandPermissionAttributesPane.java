/*
 * $Id: CommandPermissionAttributesPane.java,v 1.8 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.*;
import java.util.Date;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
public final class CommandPermissionAttributesPane extends JPanel implements ObjectResourcePropertiesPane {
	private static CommandPermissionAttributesPane instance = null;

  CommandPermissionAttributes cpa;
  CommandPermissionAttributesPanel cpap =
		new CommandPermissionAttributesPanel();

  NewUpDater updater;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public CommandPermissionAttributesPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public CommandPermissionAttributesPane(CommandPermissionAttributes cpa) {
		super();
		setObjectResource(cpa);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(cpap, BorderLayout.CENTER);
	}

  public boolean save()
  {
	 return modify();
  }

  public boolean cancel()
  {
	 this.setData(this.cpa);
	 return true;
  }

  public boolean modify()
  {
	 if(!Checker.checkCommand(user, Checker.modifyExec))
	 {
		this.setData(cpa);
		return false;
	 }
	 if(cpa.getId().equals(""))
		return false;



	 this.cpap.modify();
	 Date d = new Date();
	 cpa.modified_by = user.getId();
	 cpa.modified = d.getTime();


	 if(!NewUpDater.checkName(cpa))
		return false;

	 updater.updateObjectResources(this.cpa, false);
	 Pool.put(CommandPermissionAttributes.class.getName(), cpa.getId(), cpa);
	 this.aContext.getDataSource().SaveExec(cpa.getId());

	 this.setData(cpa);

	 return true;
  }
  public boolean create() // new command (Exec) CAN NOT BE CREATED !!!
  {
	 String error = "Ошибка: Команда не может быть создана.";
	 JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
	 return false;
  }
  public boolean open()
  {
	 return true;
  }
  public boolean delete() // COMMAND CAN NOT BE DELETED !!!!
  {

	 String error = "Ошибка: Команда не может быть удалена.";
	 JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
	 return false;
  }
  public void setContext(ApplicationContext aContext)
  {
	 this.aContext = aContext;
	 this.user = (User)Pool.get(User.class.getName(),
										 this.aContext.getSessionInterface().getUserId());

	 this.updater = new NewUpDater(aContext);
  }

  public void setObjectResource(StorableObject or)
  {
	 ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
											  Pool.get("ObjectFrame", "AdministrateObjectFrame");
	 if(f != null)
	 {
		f.setTitle("Команды");
	 }

	 if(!Checker.checkCommand(user, Checker.readExecInfo))
	 {
		this.showTheWindow(false);
		setData(or);
		return;
	 }

	 this.showTheWindow(true);
	 setData(or);
  }

  private void setData(StorableObject or)
  {
	 cpa = (CommandPermissionAttributes)or;
	 cpa.updateLocalFromTransferable();
	 this.cpap.setObjectResource(or);
  }

  public StorableObject getObjectResource()
  {
	 return cpa;
  }


  void showTheWindow(boolean key)
  {
	 this.cpap.setVisible(key);
	 repaint();
  }

	public static CommandPermissionAttributesPane getInstance() {
		if (instance == null)
			synchronized (CommandPermissionAttributesPane.class) {
				if (instance == null)
					instance = new CommandPermissionAttributesPane();
			}
		return instance;
	}
}
