package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

public class CommandPermissionAttributesPane extends PropertiesPanel
{


  CommandPermissionAttributes cpa;
  CommandPermissionAttributesPanel cpap =
		new CommandPermissionAttributesPanel();

  NewUpDater updater;

  ApplicationContext aContext = new ApplicationContext();
  BorderLayout borderLayout1 = new BorderLayout();
  User user;

  public CommandPermissionAttributesPane() {
	 try
	 {
		jbInit();
	 }
	 catch(Exception ex) {
		ex.printStackTrace();
	 }
  }

  public CommandPermissionAttributesPane(CommandPermissionAttributes cpa)
  {
	 super();
	 setObjectResource(cpa);
  }

  void jbInit() throws Exception
  {
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
	 if(cpa.id.equals(""))
		return false;



	 this.cpap.modify();
	 Date d = new Date();
	 cpa.modified_by = user.id;
	 cpa.modified = d.getTime();


	 if(!NewUpDater.checkName(cpa))
		return false;

	 updater.updateObjectResources(this.cpa, false);
	 Pool.put(CommandPermissionAttributes.typ, cpa.id, cpa);
	 this.aContext.getDataSourceInterface().SaveExec(cpa.id);

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
	 this.user = (User)Pool.get(User.typ,
										 this.aContext.getSessionInterface().getUserId());

	 this.updater = new NewUpDater(aContext);
  }

  public void setObjectResource(ObjectResource or)
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

  private void setData(ObjectResource or)
  {
	 cpa = (CommandPermissionAttributes)or;
	 cpa.updateLocalFromTransferable();
	 this.cpap.setObjectResource(or);
  }

  public ObjectResource getObjectResource()
  {
	 return cpa;
  }


  void showTheWindow(boolean key)
  {
	 this.cpap.setVisible(key);
	 repaint();
  }
}
