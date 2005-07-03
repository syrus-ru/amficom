package com.syrus.AMFICOM.Client.Administrate;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;


public class AdministrateToolBar extends JToolBar implements ApplicationModelListener
{
  private ApplicationModel aModel;


  JButton buttonOpenSession = new JButton();
  JButton buttonCloseSession = new JButton();

  JButton buttonNavigator = new JButton();

  JButton buttonCurrentUser = new JButton();

  JButton buttonDomain = new JButton();
  JButton buttonAgent = new JButton();
  JButton buttonClient = new JButton();
  JButton buttonServer = new JButton();

  JButton buttonCategory = new JButton();
  JButton buttonProfile = new JButton();
  JButton buttonGroup = new JButton();
  JButton buttonCommand = new JButton();
  JButton buttonViewOpenAll = new JButton();

	JButton buttonMaintenance = new JButton();

  public final static int img_siz = 16;
  public final static int btn_siz = 24;

  public AdministrateToolBar()
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

  private void jbInit() throws Exception
  {
	 AnalyseMainToolBar_this_actionAdapter actionAdapter =
		  new AnalyseMainToolBar_this_actionAdapter(this);
	 Dimension buttonSize = new Dimension(btn_siz, btn_siz);

	 buttonOpenSession = new JButton();
	 buttonOpenSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonOpenSession.setText("");
	 buttonOpenSession.setMaximumSize(buttonSize);
	 buttonOpenSession.setPreferredSize(buttonSize);
	 buttonOpenSession.setToolTipText(LangModelAdmin.getString("menuSessionNew"));
	 buttonOpenSession.setName("menuSessionNew");
	 buttonOpenSession.addActionListener(actionAdapter);
	 add(buttonOpenSession);

	 buttonCloseSession = new JButton();
	 buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonCloseSession.setText("");
	 buttonCloseSession.setMaximumSize(buttonSize);
	 buttonCloseSession.setPreferredSize(buttonSize);
	 buttonCloseSession.setToolTipText(LangModelAdmin.getString("menuSessionClose"));
	 buttonCloseSession.setName("menuSessionClose");
	 buttonCloseSession.addActionListener(actionAdapter);
//    add(buttonCloseSession);

	 addSeparator();

	 buttonNavigator = new JButton();
	 buttonNavigator.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/object_navigator.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonNavigator.setText("");
	 buttonNavigator.setMaximumSize(buttonSize);
	 buttonNavigator.setPreferredSize(buttonSize);
	 buttonNavigator.setToolTipText(LangModelAdmin.getString("menuViewNavigator"));
	 buttonNavigator.setName("menuViewNavigator");
	 buttonNavigator.addActionListener(actionAdapter);
	 add(buttonNavigator);


	 buttonCurrentUser = new JButton();
	 buttonCurrentUser.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/whami.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonCurrentUser.setText("");
	 buttonCurrentUser.setMaximumSize(buttonSize);
	 buttonCurrentUser.setPreferredSize(buttonSize);
	 buttonCurrentUser.setToolTipText(LangModelAdmin.getString("menuViewWhoAmI"));
	 buttonCurrentUser.setName("menuViewWhoAmI");
	 buttonCurrentUser.addActionListener(actionAdapter);
	 add(buttonCurrentUser);


	 buttonViewOpenAll = new JButton();
	 buttonViewOpenAll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openall.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonViewOpenAll.setText("");
	 buttonViewOpenAll.setMaximumSize(buttonSize);
	 buttonViewOpenAll.setPreferredSize(buttonSize);
	 buttonViewOpenAll.setToolTipText(LangModelAdmin.getString("menuViewOpenAll"));
	 buttonViewOpenAll.setName("menuViewOpenAll");
	 buttonViewOpenAll.addActionListener(actionAdapter);
//    add(buttonViewOpenAll);


	 addSeparator();

	 buttonDomain = new JButton();
	 buttonDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonDomain.setText("");
	 buttonDomain.setMaximumSize(buttonSize);
	 buttonDomain.setPreferredSize(buttonSize);
	 buttonDomain.setToolTipText(LangModelAdmin.getString("menuAccessDomain"));
	 buttonDomain.setName("menuAccessDomain");
	 buttonDomain.addActionListener(actionAdapter);
	 add(buttonDomain);

	 buttonClient = new JButton();
	 buttonClient.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pc.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonClient.setText("");
	 buttonClient.setMaximumSize(buttonSize);
	 buttonClient.setPreferredSize(buttonSize);
	 buttonClient.setToolTipText(LangModelAdmin.getString("menuArchitectureClient"));
	 buttonClient.setName("menuArchitectureClient");
	 buttonClient.addActionListener(actionAdapter);
	 add(buttonClient);

	 buttonServer = new JButton();
	 buttonServer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/server.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonServer.setText("");
	 buttonServer.setMaximumSize(buttonSize);
	 buttonServer.setPreferredSize(buttonSize);
	 buttonServer.setToolTipText(LangModelAdmin.getString("menuArchitectureServer"));
	 buttonServer.setName("menuArchitectureServer");
	 buttonServer.addActionListener(actionAdapter);
	 add(buttonServer);

	 buttonAgent = new JButton();
	 buttonAgent.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/agent.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonAgent.setText("");
	 buttonAgent.setMaximumSize(buttonSize);
	 buttonAgent.setPreferredSize(buttonSize);
	 buttonAgent.setToolTipText(LangModelAdmin.getString("menuArchitectureAgent"));
	 buttonAgent.setName("menuArchitectureAgent");
	 buttonAgent.addActionListener(actionAdapter);
	 add(buttonAgent);

	 addSeparator();

	 buttonCategory = new JButton();
	 buttonCategory.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/category.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonCategory.setText("");
	 buttonCategory.setMaximumSize(buttonSize);
	 buttonCategory.setPreferredSize(buttonSize);
	 buttonCategory.setToolTipText(LangModelAdmin.getString("menuUserCategory"));
	 buttonCategory.setName("menuUserCategory");
	 buttonCategory.addActionListener(actionAdapter);
	 add(buttonCategory);

	 buttonCommand = new JButton();
	 buttonCommand.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/command.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonCommand.setText("");
	 buttonCommand.setMaximumSize(buttonSize);
	 buttonCommand.setPreferredSize(buttonSize);
	 buttonCommand.setToolTipText(LangModelAdmin.getString("menuAccessModul"));
	 buttonCommand.setName("menuAccessModul");
	 buttonCommand.addActionListener(actionAdapter);
	 add(buttonCommand);


	 buttonGroup = new JButton();
	 buttonGroup.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/groups.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonGroup.setText("");
	 buttonGroup.setMaximumSize(buttonSize);
	 buttonGroup.setPreferredSize(buttonSize);
	 buttonGroup.setToolTipText(LangModelAdmin.getString("menuUserGroup"));
	 buttonGroup.setName("menuUserGroup");
	 buttonGroup.addActionListener(actionAdapter);
	 add(buttonGroup);


	 buttonProfile = new JButton();
	 buttonProfile.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/profiles.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	 buttonProfile.setText("");
	 buttonProfile.setMaximumSize(buttonSize);
	 buttonProfile.setPreferredSize(buttonSize);
	 buttonProfile.setToolTipText(LangModelAdmin.getString("menuUserProfile"));
	 buttonProfile.setName("menuUserProfile");
	 buttonProfile.addActionListener(actionAdapter);
	 add(buttonProfile);

	addSeparator();

	buttonMaintenance = new JButton();
	buttonMaintenance.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/maintain.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	buttonMaintenance.setText("");
	buttonMaintenance.setMaximumSize(buttonSize);
	buttonMaintenance.setPreferredSize(buttonSize);
	buttonMaintenance.setToolTipText(LangModelAdmin.getString("menuAccessMaintain"));
	buttonMaintenance.setName("menuAccessMaintain");
	buttonMaintenance.addActionListener(actionAdapter);
	add(buttonMaintenance);


  }

  public void setModel(ApplicationModel a)
  {
	 aModel = a;
  }

  public ApplicationModel getModel()
  {
	 return aModel;
  }

  public void modelChanged(String e[])
  {
	 buttonOpenSession.setVisible(aModel.isVisible("menuSessionNew"));
	 buttonOpenSession.setEnabled(aModel.isEnabled("menuSessionNew"));
	 buttonCloseSession.setVisible(aModel.isVisible("menuSessionClose"));
	 buttonCloseSession.setEnabled(aModel.isEnabled("menuSessionClose"));

	 buttonNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
	 buttonNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));

	 buttonDomain.setVisible(aModel.isVisible("menuAccessDomain"));
	 buttonDomain.setEnabled(aModel.isEnabled("menuAccessDomain"));

	 buttonAgent.setVisible(aModel.isVisible("menuArchitectureAgent"));
	 buttonAgent.setEnabled(aModel.isEnabled("menuArchitectureAgent"));

	 buttonClient.setVisible(aModel.isVisible("menuArchitectureClient"));
	 buttonClient.setEnabled(aModel.isEnabled("menuArchitectureClient"));

	 buttonServer.setVisible(aModel.isVisible("menuArchitectureServer"));
	 buttonServer.setEnabled(aModel.isEnabled("menuArchitectureServer"));

	 buttonCategory.setVisible(aModel.isVisible("menuUserCategory"));
	 buttonCategory.setEnabled(aModel.isEnabled("menuUserCategory"));

	 buttonCommand.setVisible(aModel.isVisible("menuAccessModul"));
	 buttonCommand.setEnabled(aModel.isEnabled("menuAccessModul"));

	 buttonCategory.setVisible(aModel.isVisible("menuUserCategory"));
	 buttonCategory.setEnabled(aModel.isEnabled("menuUserCategory"));

	 buttonGroup.setVisible(aModel.isVisible("menuUserGroup"));
	 buttonGroup.setEnabled(aModel.isEnabled("menuUserGroup"));

	 buttonProfile.setVisible(aModel.isVisible("menuUserProfile"));
	 buttonProfile.setEnabled(aModel.isEnabled("menuUserProfile"));

	 buttonViewOpenAll.setVisible(aModel.isVisible("menuViewOpenAll"));
	 buttonViewOpenAll.setEnabled(aModel.isEnabled("menuViewOpenAll"));

	 buttonCurrentUser.setVisible(aModel.isVisible("menuViewWhoAmI"));
	 buttonCurrentUser.setEnabled(aModel.isEnabled("menuViewWhoAmI"));

	buttonMaintenance.setVisible(aModel.isVisible("menuAccessMaintain"));
	buttonMaintenance.setEnabled(aModel.isEnabled("menuAccessMaintain"));
  }



  public void this_actionPerformed(ActionEvent e)
  {
	 if(aModel == null)
		return;
	 AbstractButton jb = (AbstractButton )e.getSource();
	 String s = jb.getName();
	 Command command = aModel.getCommand(s);
	 command = (Command )command.clone();
	 command.execute();
  }
}

class AnalyseMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
  AdministrateToolBar adaptee;

  AnalyseMainToolBar_this_actionAdapter(AdministrateToolBar adaptee)
  {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
//    System.out.println("AdministrateToolBar: actionPerformed");
	 adaptee.this_actionPerformed(e);
  }
}