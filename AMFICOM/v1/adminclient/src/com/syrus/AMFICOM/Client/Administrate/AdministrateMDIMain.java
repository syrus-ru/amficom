package com.syrus.AMFICOM.Client.Administrate;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Command.Window.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import com.syrus.io.*;
import com.syrus.AMFICOM.Client.General.Command.Admin.OpenWhoAmIFrameCommand;
import com.syrus.AMFICOM.Client.General.Command.Admin.OpenAllWindowsCommand;
import com.syrus.AMFICOM.Client.General.Command.Admin.ViewObjectNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Command.Admin.OpenObjectFrameCommand;
import com.syrus.AMFICOM.Client.General.Command.Admin.OpenMaintenanceCommand;

public class AdministrateMDIMain extends JFrame implements OperationListener
{
  private Dispatcher internal_dispatcher = new Dispatcher();
  public ApplicationContext aContext = new ApplicationContext();

  static IniFile iniFile;
  static String iniFileName = "Administrate.properties";

  static SimpleDateFormat sdf =
      new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

  BorderLayout borderLayout = new BorderLayout();

  JPanel mainPanel = new JPanel();
//	JPanel toolBarPanel = new JPanel();
  JScrollPane scrollPane = new JScrollPane();
  JViewport viewport = new JViewport();
  JDesktopPane desktopPane = new JDesktopPane();
  JPanel statusBarPanel = new JPanel();
  StatusBarModel statusBar = new StatusBarModel(0);
  AdministrateMenuBar menuBar = new AdministrateMenuBar();
  AdministrateToolBar toolBar = new AdministrateToolBar();

  public AdministrateMDIMain(ApplicationContext aContext)
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
    Environment.addWindow(this);
    setContext(aContext);
  }

  public AdministrateMDIMain()
  {
    this(new ApplicationContext());
  }

  private void jbInit() throws Exception
  {
//	setUndecorated(true);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    setContentPane(mainPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

//    setSize(new Dimension(960, 670));
    this.setTitle(LangModelAdmin.String("AppTitle"));
    this.addComponentListener(new AdministrateMDIMain_this_componentAdapter(this));
    this.addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        this_windowClosing(e);
      }
    });

    mainPanel.setLayout(new BorderLayout());//new FlowLayout());
    mainPanel.setBackground(Color.darkGray);
    desktopPane.setLayout(null);
//    desktopPane.setBackground(Color.darkGray);
    desktopPane.setBackground(SystemColor.control.darker().darker());


    statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    statusBarPanel.setLayout(new BorderLayout());
    statusBarPanel.add(statusBar, BorderLayout.CENTER);

    statusBar.add("status");
    statusBar.add("server");
    statusBar.add("session");
    statusBar.add("user");
    statusBar.add("time");

    viewport.setView(desktopPane);
    scrollPane.setViewport(viewport);
    scrollPane.setAutoscrolls(true);

//		desktopPane.setPreferredSize(new Dimension(1600,1200)); //very important

//		mainPanel.add(toolBarPanel, BorderLayout.NORTH);
    mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(toolBar, BorderLayout.NORTH);

    this.setJMenuBar(menuBar);
  }

  public void SetDefaults()
  {
  }

  public void init_module()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    statusBar.distribute();
    statusBar.setWidth("status", 100);
    statusBar.setWidth("server", 250);
    statusBar.setWidth("session", 200);
    statusBar.setWidth("user", 100);
    statusBar.setWidth("time", 50);

    statusBar.setText("status", LangModel.String("statusReady"));
    statusBar.setText("server", LangModel.String("statusNoConnection"));
    statusBar.setText("session", LangModel.String("statusNoSession"));
    statusBar.setText("user", LangModel.String("statusNoUser"));
    statusBar.setText("time", " ");
    statusBar.organize();

    //Setting of the dispatcher to te  status bar;
    statusBar.setDispatcher(Environment.the_dispatcher);
    statusBar.setDispatcher(internal_dispatcher);

    // load values from properties file
    try
    {
      iniFile = new IniFile(iniFileName);
//      System.out.println("read ini file " + iniFileName);
//			objectName = iniFile.getValue("objectName");
    }
    catch(java.io.IOException e)
    {
//      System.out.println("Error opening " + iniFileName + " - setting defaults");
      SetDefaults();
    }

    aContext.setDispatcher(internal_dispatcher);

    internal_dispatcher.register(this, "contextchange");
    Environment.the_dispatcher.register(this, "contextchange");

    aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
    aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
    aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
    aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
    aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
    aModel.setCommand("menuExit", new ExitCommand(this));

    aModel.setCommand("menuViewNavigator", new ViewObjectNavigatorCommand(
        internal_dispatcher,
        desktopPane,  "Навигатор объектов",  aContext));

    aModel.setCommand("menuViewOpenAll", new OpenAllWindowsCommand(
        desktopPane,
        this.aContext,
        "domain",
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        DomainForAdmin.class,
        this.internal_dispatcher,
        "Навигатор объектов",
        "Пользователи",
        new String[] {"domain", "operatorprofile", "operatorgroup", "operatorcategory", "comm_perm_attrib"},
        new String[] {}));


    aModel.setCommand("menuAccessDomain", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        "domain",
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        DomainForAdmin.class));

    aModel.setCommand("menuArchitectureAgent", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        Agent.typ,
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        Agent.class));

    aModel.setCommand("menuArchitectureClient", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        Client.typ,
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        Client.class));

    aModel.setCommand("menuArchitectureServer", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        Server.typ,
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        Server.class));



    aModel.setCommand("menuAccessModul", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        CommandPermissionAttributes.typ,
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        CommandPermissionAttributes.class));

    aModel.setCommand("menuViewWhoAmI", new
                      OpenWhoAmIFrameCommand(desktopPane, aContext));

    aModel.setCommand("menuViewOpenObjectsWindow", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        "domain",
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан"}),
        DomainForAdmin.class));

	aModel.setCommand("menuAccessMaintain", new OpenMaintenanceCommand(desktopPane, aContext, this));

    aModel.setCommand("menuUserCategory", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        "operatorcategory",
        new StubDisplayModel(
        new String[] {"id", "name", "description"},
        new String[] {"Идентификатор", "Имя", "Описание"}),
        OperatorCategory.class));

    aModel.setCommand("menuUserGroup", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        "operatorgroup",
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создана"}),
        OperatorGroup.class));


    aModel.setCommand("menuUserProfile", new OpenObjectFrameCommand(
        desktopPane,
        aContext,
        "operatorprofile",
        new StubDisplayModel(
        new String[] {"id", "name", "user_id", "created", "login"},
        new String[] {"Идентификатор", "Название", "Владелец", "Создан", "Пользователь"}),
        OperatorProfile.class));

    aModel.setCommand("menuWindow", new WindowCloseCommand(desktopPane));
    aModel.setCommand("menuWindowClose", new WindowCloseCommand(desktopPane));
    aModel.setCommand("menuWindowCloseAll", new WindowCloseAllCommand(desktopPane));
    aModel.setCommand("menuWindowTileHorizontal", new WindowTileHorizontalCommand(desktopPane));
    aModel.setCommand("menuWindowTileVertical", new WindowTileVerticalCommand(desktopPane));
    aModel.setCommand("menuWindowCascade", new WindowCascadeCommand(desktopPane));
    aModel.setCommand("menuWindowArrange", new WindowArrangeCommand(desktopPane));
    aModel.setCommand("menuWindowArrangeIcons", new WindowArrangeIconsCommand(desktopPane));
    aModel.setCommand("menuWindowMinimizeAll", new WindowMinimizeAllCommand(desktopPane));
    aModel.setCommand("menuWindowRestoreAll", new WindowRestoreAllCommand(desktopPane));
    aModel.setCommand("menuWindowList", new WindowListCommand(desktopPane));

    aModel.add("menuHelpAbout", new HelpAboutCommand(this));

    aModel.setAllItemsEnabled(false);
    aModel.setEnabled("menuSession", true);
    aModel.setEnabled("menuSessionNew", true);
    aModel.setEnabled("menuSessionConnection", true);
    aModel.setEnabled("menuExit", true);
    aModel.setEnabled("menuView", true);
    aModel.setEnabled("menuTools", true);
    aModel.setEnabled("menuHelp", true);
    aModel.setEnabled("menuHelpAbout", true);


    aModel.enable("menuView");
    aModel.enable("menuArchitecture");
    aModel.enable("menuUser");
    aModel.enable("menuAccess");

    aModel.fireModelChanged("");

    if(ConnectionInterface.getActiveConnection() != null)
    {
      aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
      if(aContext.getConnectionInterface().isConnected())
        internal_dispatcher.notify(new ContextChangeEvent(
            aContext.getConnectionInterface(),
            ContextChangeEvent.CONNECTION_OPENED_EVENT));
    }
    else
    {
      aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
      ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//			new CheckConnectionCommand(internal_dispatcher, aContext).execute();
    }
    if(SessionInterface.getActiveSession() != null)
    {
      aContext.setSessionInterface(SessionInterface.getActiveSession());
      aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
      if(aContext.getSessionInterface().isOpened())
        internal_dispatcher.notify(new ContextChangeEvent(
            aContext.getSessionInterface(),
            ContextChangeEvent.SESSION_OPENED_EVENT));
    }
    else
    {
      aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
      SessionInterface.setActiveSession(aContext.getSessionInterface());
    }
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    if(aContext.getApplicationModel() == null)
      aContext.setApplicationModel(new ApplicationModel());
    setModel(aContext.getApplicationModel());
  }

  public ApplicationContext getContext()
  {
    return aContext;
  }

  public void setModel(ApplicationModel aModel)
  {
    aModel.addListener(menuBar);
    aModel.addListener(toolBar);
    toolBar.setModel(aModel);
    menuBar.setModel(aModel);

    aModel.fireModelChanged("");
  }

  public ApplicationModel getModel()
  {
    return aContext.getApplicationModel();
  }

  public void operationPerformed(OperationEvent ae)
  {
    if(ae.getActionCommand().equals("contextchange"))
    {
      ContextChangeEvent cce = (ContextChangeEvent)ae;
//      System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
      ApplicationModel aModel = aContext.getApplicationModel();
      if(cce.SESSION_OPENED)
      {
        SessionInterface ssi = (SessionInterface)cce.getSource();
        if(aContext.getSessionInterface().equals(ssi))
        {
//					aContext.setSessionInterface(ssi);
//					aContext.setDataSourceInterface(Environment.getDefaultDataSourceInterface(aContext.getSessionInterface()));

          Environment.the_dispatcher.notify(new StatusMessageEvent("Подгрузка административных объектов..."));

          aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));
          DataSourceInterface dataSource = aContext.getDataSourceInterface();

          new ObjectDataSourceImage(dataSource).GetObjects();
          new ObjectDataSourceImage(dataSource).GetAdminObjects();

          Pool.put("Application_Context","Application_Context", aContext);

          Categories categ = new Categories(dataSource);
          Executables e = new Executables(dataSource);

          User user = (User)Pool.get(User.typ,
                                     aContext.getSessionInterface().getUserId());
          if(!Checker.checkCommand(user, Checker.openAdminWindow))
            return;

          setSessionOpened();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
          statusBar.setText("user", aContext.getSessionInterface().getUser());

          Environment.the_dispatcher.notify(new StatusMessageEvent(" "));

        }
      }
      if(cce.SESSION_CLOSED)
      {
        // Closing of the existing windows
        JInternalFrame frame = (JInternalFrame)Pool.get("Navigator", "ObjectNavigator");
        if(frame != null)
        {
          Pool.remove(frame);
          frame.dispose();
        }
        frame =
            (ObjectResourceCatalogFrame)Pool.get("ObjectFrame", "AdministrateObjectFrame");
        if(frame != null)
        {
          Pool.remove(frame);
          frame.dispose();
        }

        WhoAmIFrame wmi = (WhoAmIFrame)Pool.get("WhoAmI", "WhoAmIobject");
        if(wmi!=null)
        {
          Pool.remove(wmi);
          wmi.dispose();
        }

        //---------------------------------------------------------------
        SessionInterface ssi = (SessionInterface)cce.getSource();
        if(aContext.getSessionInterface().equals(ssi))
        {
          aContext.setDataSourceInterface(null);

          setSessionClosed();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("session", LangModel.String("statusNoSession"));
          statusBar.setText("user", LangModel.String("statusNoUser"));
        }
      }
      if(cce.CONNECTION_OPENED)
      {
        ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {
          setConnectionOpened();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
        }
      }
      if(cce.CONNECTION_CLOSED)
      {
        ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {
          statusBar.setText("status", LangModel.String("statusError"));
          statusBar.setText("server", LangModel.String("statusConnectionError"));

          statusBar.setText("status", LangModel.String("statusDisconnected"));
          statusBar.setText("server", LangModel.String("statusNoConnection"));

          setConnectionClosed();

        }
      }
      if(cce.CONNECTION_FAILED)
      {
        ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {
          statusBar.setText("status", LangModel.String("statusError"));
          statusBar.setText("server", LangModel.String("statusConnectionError"));

          setConnectionFailed();
        }
      }
    }
  }

  public void setConnectionOpened()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    aModel.setEnabled("menuSessionNew", true);
    aModel.setEnabled("menuSessionClose", false);
    aModel.setEnabled("menuSessionConnection", true);
    aModel.setEnabled("menuSessionChangePassword", false);

    aModel.fireModelChanged("");
  }

  public void setConnectionClosed()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    aModel.setEnabled("menuSessionNew", true);
    aModel.setEnabled("menuSessionClose", false);
    aModel.setEnabled("menuSessionOptions", false);
    aModel.setEnabled("menuSessionChangePassword", false);

    aModel.fireModelChanged("");
  }

  public void setConnectionFailed()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    aModel.setEnabled("menuSessionNew", false);
    aModel.setEnabled("menuSessionClose", false);
    aModel.setEnabled("menuSessionOptions", false);
    aModel.setEnabled("menuSessionChangePassword", false);

    aModel.fireModelChanged("");
  }

  public void setSessionOpened()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    aModel.enable("menuSessionClose");
    aModel.enable("menuSessionOptions");
    aModel.enable("menuSessionChangePassword");

    aModel.enable("menuView");
    aModel.enable("menuViewNavigator");


    aModel.enable("menuViewWhoAmI");
    aModel.enable("menuAccessDomain");
	aModel.enable("menuAccessMaintain");
    aModel.enable("menuArchitectureAgent");
    aModel.enable("menuArchitectureClient");
    aModel.enable("menuArchitectureServer");
    aModel.enable("menuViewOpenAll");
    aModel.enable("menuViewOpenObjectsWindow");
    aModel.enable("menuAccessModul");
    aModel.enable("menuUser");
    aModel.enable("menuUserCategory");
    aModel.enable("menuUserGroup");
    aModel.enable("menuUserRole");
    aModel.enable("menuUserPrivilege");
    aModel.enable("menuUserProfile");

    aModel.disable("menuSessionNew");

	new OpenAllWindowsCommand(
        desktopPane,
        aContext,
        "domain",
        new StubDisplayModel(
				new String[] {"id", "name", "user_id", "created"},
				new String[] {"Идентификатор", "Название", "Владелец", "Создан"} ),
        DomainForAdmin.class,
        internal_dispatcher,
        "Навигатор объектов",
        "Пользователи",
        new String[] {
				"domain", 
				"operatorprofile", 
				"operatorgroup", 
				"operatorcategory", 
				"comm_perm_attrib"},
        new String[] {}).execute();;

    aModel.fireModelChanged("");
  }

  public void setSessionClosed()
  {
    ApplicationModel aModel = aContext.getApplicationModel();

    aModel.disable("menuSessionClose");
    aModel.disable("menuSessionOptions");
    aModel.disable("menuSessionChangePassword");


    aModel.disable("menuViewNavigator");

    aModel.disable("menuViewWhoAmI");
    aModel.disable("menuAccessDomain");
	aModel.disable("menuAccessMaintain");
    aModel.disable("menuArchitectureAgent");
    aModel.disable("menuArchitectureClient");
    aModel.disable("menuArchitectureServer");

    aModel.disable("menuViewOpenAll");
    aModel.disable("menuViewOpenObjectsWindow");
    aModel.disable("menuAccessModul");
    aModel.disable("menuUserCategory");
    aModel.disable("menuUserGroup");
    aModel.disable("menuUserRole");
    aModel.disable("menuUserPrivilege");
    aModel.disable("menuUserProfile");

    aModel.enable("menuSessionNew");

    aModel.fireModelChanged("");
  }

  public Dispatcher getInternalDispatcher()
  {
    return internal_dispatcher;
  }

  void this_componentShown(ComponentEvent e)
  {
    init_module();
  }

  void this_windowClosing(WindowEvent e)
  {
    internal_dispatcher.unregister(this, "contextchange");
    Environment.the_dispatcher.unregister(this, "contextchange");
    aContext.getApplicationModel().getCommand("menuExit").execute();
  }
  void this_windowResizing(WindowEvent e)
  {
//    System.out.println("RESIZED!!!");
  }

  protected void processWindowEvent(WindowEvent e)
  {
    if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
    {
		Environment.setActiveWindow(this);
//      ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//      SessionInterface.setActiveSession(aContext.getSessionInterface());
    }
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      internal_dispatcher.unregister(this, "contextchange");
      Environment.the_dispatcher.unregister(this, "contextchange");
      aContext.getApplicationModel().getCommand("menuExit").execute();
      return;
    }
    super.processWindowEvent(e);
  }
}

class AdministrateMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
  AdministrateMDIMain adaptee;

  AdministrateMDIMain_this_componentAdapter(AdministrateMDIMain adaptee)
  {
    this.adaptee = adaptee;
  }

  public void componentShown(ComponentEvent e)
  {
    adaptee.this_componentShown(e);
  }





}