package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

public class WhoAmIFrame extends JInternalFrame
{
  ApplicationContext aContext = new  ApplicationContext();

  User user;
  OperatorProfile op;
  JScrollPane ScrollPane1 = new JScrollPane();
  JTabbedPane TabbedPane1 = new JTabbedPane();

  WhoAmICommonPanel commonP = new WhoAmICommonPanel();
  WhoAmIPasswordPanel passP = new WhoAmIPasswordPanel();

  public WhoAmIFrame()
  {
	 this.setName(LangModelAdmin.getString("menuViewWhoAmI"));
	 this.setTitle(LangModelAdmin.getString("menuViewWhoAmI"));
	 try
	 {
		jbInit();
	 }
	 catch(Exception e)
	 {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception
  {
	 ScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
	 this.setClosable(true);
	 this.setIconifiable(true);
	 this.setResizable(true);
	 TabbedPane1.setPreferredSize(new Dimension(250, 170));

	 this.getContentPane().add(ScrollPane1, BorderLayout.CENTER);
	 ScrollPane1.getViewport().add(TabbedPane1, null);

	 this.TabbedPane1.add(commonP.getName(), commonP);
	 this.TabbedPane1.add(passP.getName(), passP);
  }

  public WhoAmIFrame(ApplicationContext aContext)
  {
	 this();
	 this.setApplicationContext(aContext);
	 if(user != null && op != null)
	 {
		setData();
	 }
  }

  public void setApplicationContext(ApplicationContext aContext)
  {
	 this.aContext = aContext;
	 String userID = this.aContext.getSessionInterface().getUserId();
	 user = (User)Pool.get(User.typ, userID);
	 if(user != null)
	 {
		if(user.type.equals(OperatorProfile.typ))
		  op = (OperatorProfile)Pool.get(user.type, user.object_id);
	 }
  }

  private boolean setData()
  {
	 if(user == null || op == null)
		return false;

	 this.commonP.setData(this.user, this.op);
	 this.passP.setData(this.user, this.op);
	 this.passP.setAContext(this.aContext);
	 if(user.id.equals("sys"))
		return true;
	 return false;
  }
}