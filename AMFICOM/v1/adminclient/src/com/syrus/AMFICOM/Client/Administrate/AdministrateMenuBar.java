package com.syrus.AMFICOM.Client.Administrate;

import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class AdministrateMenuBar extends JMenuBar
    implements ApplicationModelListener
{
  private ApplicationModel aModel;

  JMenu menuSession = new JMenu();
  JMenuItem menuSessionNew = new JMenuItem();
  JMenuItem menuSessionClose = new JMenuItem();
  JMenuItem menuSessionOptions = new JMenuItem();
  JMenuItem menuSessionConnection = new JMenuItem();
  JMenuItem menuSessionChangePassword = new JMenuItem();
  JMenuItem menuSessionSave = new JMenuItem();
  JMenuItem menuSessionUndo = new JMenuItem();
  JMenuItem menuSessionDomain = new JMenuItem();
  JMenuItem menuExit = new JMenuItem();


  JMenu menuView = new JMenu();
  JMenuItem menuViewNavigator = new JMenuItem();
  JMenuItem menuViewWhoAmI = new JMenuItem();
  JMenuItem menuViewOpenObjectsWindow = new JMenuItem();
  JMenuItem menuViewOpenAll = new JMenuItem();


  JMenu menuArchitecture = new JMenu();
  JMenuItem menuArchitectureClient = new JMenuItem();
  JMenuItem menuArchitectureAgent = new JMenuItem();
  JMenuItem menuArchitectureServer = new JMenuItem();


  JMenu menuAccess = new JMenu();
  JMenuItem menuAccessDomain = new JMenuItem();
  JMenuItem menuAccessModul  = new JMenuItem();
  JMenuItem menuAccessMaintain = new JMenuItem();

  JMenu menuUser = new JMenu();
  JMenuItem menuUserCategory = new JMenuItem();
  JMenuItem menuUserGroup = new JMenuItem();
  JMenuItem menuUserProfile = new JMenuItem();


  JMenu menuHelp = new JMenu();
  JMenuItem menuHelpContents = new JMenuItem();
  JMenuItem menuHelpFind = new JMenuItem();
  JMenuItem menuHelpTips = new JMenuItem();
  JMenuItem menuHelpStart = new JMenuItem();
  JMenuItem menuHelpCourse = new JMenuItem();
  JMenuItem menuHelpHelp = new JMenuItem();
  JMenuItem menuHelpSupport = new JMenuItem();
  JMenuItem menuHelpLicense = new JMenuItem();
  JMenuItem menuHelpAbout = new JMenuItem();

  public AdministrateMenuBar()
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

  public AdministrateMenuBar(ApplicationModel aModel)
  {
    this();
    this.aModel = aModel;
  }

  private void jbInit() throws Exception
  {
    AdministrateMenuBar_this_actionAdapter actionAdapter =
        new AdministrateMenuBar_this_actionAdapter(this);

    menuSession.setText(LangModel.Text("menuSession"));
    menuSession.setName("menuSession");
    menuSessionNew.setText(LangModel.Text("menuSessionNew"));
    menuSessionNew.setName("menuSessionNew");
    menuSessionNew.addActionListener(actionAdapter);
    menuSessionClose.setText(LangModel.Text("menuSessionClose"));
    menuSessionClose.setName("menuSessionClose");
    menuSessionClose.addActionListener(actionAdapter);
    menuSessionOptions.setText(LangModel.Text("menuSessionOptions"));
    menuSessionOptions.setName("menuSessionOptions");
    menuSessionOptions.addActionListener(actionAdapter);
    menuSessionConnection.setText(LangModel.Text("menuSessionConnection"));
    menuSessionConnection.setName("menuSessionConnection");
    menuSessionConnection.addActionListener(actionAdapter);
    menuSessionChangePassword.setText(LangModel.Text("menuSessionChangePassword"));
    menuSessionChangePassword.setName("menuSessionChangePassword");
    menuSessionChangePassword.addActionListener(actionAdapter);
    menuSessionSave.setText(LangModel.Text("menuSessionSave"));
    menuSessionSave.setName("menuSessionSave");
    menuSessionSave.addActionListener(actionAdapter);
    menuSessionUndo.setText(LangModel.Text("menuSessionUndo"));
    menuSessionUndo.setName("menuSessionUndo");
    menuSessionUndo.addActionListener(actionAdapter);
    menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
    menuSessionDomain.setName("menuSessionDomain");
    menuSessionDomain.addActionListener(actionAdapter);
    menuExit.setText(LangModel.Text("menuExit"));
    menuExit.setName("menuExit");
    menuExit.addActionListener(actionAdapter);

    menuView.setText(LangModelAdmin.Text("menuView"));
    menuView.setName("menuView");
    menuViewNavigator.setText(LangModelAdmin.Text("menuViewNavigator"));
    menuViewNavigator.setName("menuViewNavigator");
    menuViewNavigator.addActionListener(actionAdapter);
    menuViewOpenAll.setText(LangModelAdmin.Text("menuViewOpenAll"));
    menuViewOpenAll.setName("menuViewOpenAll");
    menuViewOpenAll.addActionListener(actionAdapter);
    menuViewWhoAmI.setText(LangModelAdmin.Text("menuViewWhoAmI"));
    menuViewWhoAmI.setName("menuViewWhoAmI");
    menuViewWhoAmI.addActionListener(actionAdapter);
    menuViewOpenObjectsWindow.setText(LangModelAdmin.Text("menuViewOpenObjectsWindow"));
    menuViewOpenObjectsWindow.setName("menuViewOpenObjectsWindow");
    menuViewOpenObjectsWindow.addActionListener(actionAdapter);



    menuAccess.setText(LangModelAdmin.Text("menuAccess"));
    menuAccess.setName("menuAccess");
    menuAccessDomain.setText(LangModelAdmin.Text("menuAccessDomain"));
    menuAccessDomain.setName("menuAccessDomain");
    menuAccessDomain.addActionListener(actionAdapter);
    menuAccessModul.setText(LangModelAdmin.Text("menuAccessModul"));
    menuAccessModul.setName("menuAccessModul");
    menuAccessModul.addActionListener(actionAdapter);
    menuAccessMaintain.setText(LangModelAdmin.Text("menuAccessMaintain"));
    menuAccessMaintain.setName("menuAccessMaintain");
    menuAccessMaintain.addActionListener(actionAdapter);


    menuArchitecture.setText(LangModelAdmin.Text("menuArchitecture"));
    menuArchitecture.setName("menuArchitecture");
    menuArchitectureServer.setText(LangModelAdmin.Text("menuArchitectureServer"));
    menuArchitectureServer.setName("menuArchitectureServer");
    menuArchitectureServer.addActionListener(actionAdapter);
    menuArchitectureAgent.setText(LangModelAdmin.Text("menuArchitectureAgent"));
    menuArchitectureAgent.setName("menuArchitectureAgent");
    menuArchitectureAgent.addActionListener(actionAdapter);
    menuArchitectureClient.setText(LangModelAdmin.Text("menuArchitectureClient"));
    menuArchitectureClient.setName("menuArchitectureClient");
    menuArchitectureClient.addActionListener(actionAdapter);


    menuUser.setText(LangModelAdmin.Text("menuUser"));
    menuUser.setName("menuUser");
    menuUserCategory.setText(LangModelAdmin.Text("menuUserCategory"));
    menuUserCategory.setName("menuUserCategory");
    menuUserCategory.addActionListener(actionAdapter);
    menuUserGroup.setText(LangModelAdmin.Text("menuUserGroup"));
    menuUserGroup.setName("menuUserGroup");
    menuUserGroup.addActionListener(actionAdapter);
    menuUserProfile.setText(LangModelAdmin.Text("menuUserProfile"));
    menuUserProfile.setName("menuUserProfile");
    menuUserProfile.addActionListener(actionAdapter);

    menuHelp.setText(LangModel.Text("menuHelp"));
    menuHelp.setName("menuHelp");
    menuHelpContents.setText(LangModel.Text("menuHelpContents"));
    menuHelpContents.setName("menuHelpContents");
    menuHelpContents.addActionListener(actionAdapter);
    menuHelpFind.setText(LangModel.Text("menuHelpFind"));
    menuHelpFind.setName("menuHelpFind");
    menuHelpFind.addActionListener(actionAdapter);
    menuHelpTips.setText(LangModel.Text("menuHelpTips"));
    menuHelpTips.setName("menuHelpTips");
    menuHelpTips.addActionListener(actionAdapter);
    menuHelpStart.setText(LangModel.Text("menuHelpStart"));
    menuHelpStart.setName("menuHelpStart");
    menuHelpStart.addActionListener(actionAdapter);
    menuHelpCourse.setText(LangModel.Text("menuHelpCourse"));
    menuHelpCourse.setName("menuHelpCourse");
    menuHelpCourse.addActionListener(actionAdapter);
    menuHelpHelp.setText(LangModel.Text("menuHelpHelp"));
    menuHelpHelp.setName("menuHelpHelp");
    menuHelpHelp.addActionListener(actionAdapter);
    menuHelpSupport.setText(LangModel.Text("menuHelpSupport"));
    menuHelpSupport.setName("menuHelpSupport");
    menuHelpSupport.addActionListener(actionAdapter);
    menuHelpLicense.setText(LangModel.Text("menuHelpLicense"));
    menuHelpLicense.setName("menuHelpLicense");
    menuHelpLicense.addActionListener(actionAdapter);
    menuHelpAbout.setText(LangModel.Text("menuHelpAbout"));
    menuHelpAbout.setName("menuHelpAbout");
    menuHelpAbout.addActionListener(actionAdapter);

    menuSession.add(menuSessionNew);
    menuSession.add(menuSessionClose);
    menuSession.add(menuSessionOptions);
    menuSession.add(menuSessionChangePassword);
    menuSession.addSeparator();
    menuSession.add(menuSessionConnection);
    menuSession.add(menuSessionSave);
    menuSession.add(menuSessionUndo);
    menuSession.add(menuSessionDomain);
    menuSession.addSeparator();
    menuSession.add(menuExit);

		menuView.add(menuViewNavigator);
		menuView.add(menuViewWhoAmI);
    menuView.add(menuViewOpenObjectsWindow);
    menuView.addSeparator();
    menuView.add(menuViewOpenAll);

    menuUser.add(menuUserCategory);
    menuUser.add(menuUserGroup);
    menuUser.add(menuUserProfile);

    menuArchitecture.add(menuArchitectureClient);
    menuArchitecture.add(menuArchitectureAgent);
    menuArchitecture.add(menuArchitectureServer);

    menuAccess.add(menuAccessDomain);
    menuAccess.add(menuAccessModul);
	menuAccess.add(menuAccessMaintain);

    menuHelp.add(menuHelpContents);
    menuHelp.add(menuHelpFind);
    menuHelp.add(menuHelpTips);
    menuHelp.add(menuHelpStart);
    menuHelp.add(menuHelpCourse);
    menuHelp.addSeparator();
    menuHelp.add(menuHelpHelp);
    menuHelp.addSeparator();
    menuHelp.add(menuHelpSupport);
    menuHelp.add(menuHelpLicense);
    menuHelp.addSeparator();
    menuHelp.add(menuHelpAbout);

    this.add(menuSession);
    this.add(menuView);
    this.add(menuArchitecture);
    this.add(menuUser);
    this.add(menuAccess);
    this.add(menuHelp);
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
    int count = e.length;
    int i;


    menuSession.setVisible(aModel.isVisible("menuSession"));
    menuSession.setEnabled(aModel.isEnabled("menuSession"));

    menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
    menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));

    menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
    menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));

    menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
    menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));

    menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
    menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));

    menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
    menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));

    menuSessionSave.setVisible(aModel.isVisible("menuSessionSave"));
    menuSessionSave.setEnabled(aModel.isEnabled("menuSessionSave"));

    menuSessionUndo.setVisible(aModel.isVisible("menuSessionUndo"));
    menuSessionUndo.setEnabled(aModel.isEnabled("menuSessionUndo"));

    menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
    menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));

    menuExit.setVisible(aModel.isVisible("menuExit"));
    menuExit.setEnabled(aModel.isEnabled("menuExit"));


    menuView.setVisible(aModel.isVisible("menuView"));
    menuView.setEnabled(aModel.isEnabled("menuView"));
    menuViewNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
    menuViewNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));
    menuViewOpenAll.setVisible(aModel.isVisible("menuViewOpenAll"));
    menuViewOpenAll.setEnabled(aModel.isEnabled("menuViewOpenAll"));
    menuViewWhoAmI.setVisible(aModel.isVisible("menuViewWhoAmI"));
    menuViewWhoAmI.setEnabled(aModel.isEnabled("menuViewWhoAmI"));
    menuViewOpenObjectsWindow.setVisible(aModel.isVisible("menuViewOpenObjectsWindow"));
    menuViewOpenObjectsWindow.setEnabled(aModel.isEnabled("menuViewOpenObjectsWindow"));

    menuArchitecture.setVisible(aModel.isVisible("menuArchitecture"));
    menuArchitecture.setEnabled(aModel.isEnabled("menuArchitecture"));
    menuArchitectureServer.setVisible(aModel.isVisible("menuArchitectureServer"));
    menuArchitectureServer.setEnabled(aModel.isEnabled("menuArchitectureServer"));
    menuArchitectureClient.setVisible(aModel.isVisible("menuArchitectureClient"));
    menuArchitectureClient.setEnabled(aModel.isEnabled("menuArchitectureClient"));
    menuArchitectureAgent.setVisible(aModel.isVisible("menuArchitectureAgent"));
    menuArchitectureAgent.setEnabled(aModel.isEnabled("menuArchitectureAgent"));


    menuUser.setVisible(aModel.isVisible("menuUser"));
    menuUser.setEnabled(aModel.isEnabled("menuUser"));
    menuUserCategory.setVisible(aModel.isVisible("menuUserCategory"));
    menuUserCategory.setEnabled(aModel.isEnabled("menuUserCategory"));
    menuUserGroup.setVisible(aModel.isVisible("menuUserGroup"));
    menuUserGroup.setEnabled(aModel.isEnabled("menuUserGroup"));
    menuUserProfile.setVisible(aModel.isVisible("menuUserProfile"));
    menuUserProfile.setEnabled(aModel.isEnabled("menuUserProfile"));


    menuAccess.setVisible(aModel.isVisible("menuAccess"));
    menuAccess.setEnabled(aModel.isEnabled("menuAccess"));
    menuAccessDomain.setVisible(aModel.isVisible("menuAccessDomain"));
    menuAccessDomain.setEnabled(aModel.isEnabled("menuAccessDomain"));
    menuAccessModul.setVisible(aModel.isVisible("menuAccessModul"));
    menuAccessModul.setEnabled(aModel.isEnabled("menuAccessModul"));
    menuAccessMaintain.setVisible(aModel.isVisible("menuAccessMaintain"));
    menuAccessMaintain.setEnabled(aModel.isEnabled("menuAccessMaintain"));


    menuHelp.setVisible(aModel.isVisible("menuHelp"));
    menuHelp.setEnabled(aModel.isEnabled("menuHelp"));
    menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
    menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));
    menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
    menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));
    menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
    menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));
    menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
    menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));
    menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
    menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));
    menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
    menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));
    menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
    menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));
    menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
    menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));
    menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
    menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
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

class AdministrateMenuBar_this_actionAdapter
    implements java.awt.event.ActionListener
{
  AdministrateMenuBar adaptee;

  AdministrateMenuBar_this_actionAdapter(AdministrateMenuBar adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.this_actionPerformed(e);
  }
}
