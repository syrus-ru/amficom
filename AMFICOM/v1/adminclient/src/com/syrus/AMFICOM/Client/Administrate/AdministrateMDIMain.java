/*
 * $Id: AdministrateMDIMain.java,v 1.3 2004/09/27 16:20:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.WhoAmIFrame;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Admin.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Command.Window.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 16:20:13 $
 * @module admin_v1
 */
public class AdministrateMDIMain extends JFrame implements OperationListener {
  private Dispatcher internal_dispatcher = new Dispatcher();
  public ApplicationContext aContext = new ApplicationContext();


  static SimpleDateFormat sdf =
		new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private BorderLayout borderLayout = new BorderLayout();
	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	private JDesktopPane desktopPane = new JDesktopPane();
	private JPanel statusBarPanel = new JPanel();
	private StatusBarModel statusBar = new StatusBarModel(0);
	private AdministrateMenuBar menuBar = new AdministrateMenuBar();
	private AdministrateToolBar toolBar = new AdministrateToolBar();

	public AdministrateMDIMain(ApplicationContext aContext) {
		jbInit();
		Environment.addWindow(this);
		setContext(aContext);
	}

	/**
	 * @deprecated
	 */
	public AdministrateMDIMain() {
		this(new ApplicationContext());
	}

	private void jbInit() {
	 enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	 setContentPane(mainPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		this.setTitle(LangModelAdmin.getString("AppTitle"));
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				init_module();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				internal_dispatcher.unregister(AdministrateMDIMain.this, "contextchange");
				Environment.getDispatcher().unregister(AdministrateMDIMain.this, "contextchange");
				aContext.getApplicationModel().getCommand("menuExit").execute();
			}
		});

	 mainPanel.setLayout(new BorderLayout());
	 mainPanel.setBackground(Color.darkGray);
	 desktopPane.setLayout(null);
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

	/**
	 * @deprecared
	 */
	public void SetDefaults() {
	}

	/**
	 * @deprecated
	 */
	public void init_module() {
	 ApplicationModel aModel = aContext.getApplicationModel();

	 statusBar.distribute();
	 statusBar.setWidth("status", 100);
	 statusBar.setWidth("server", 250);
	 statusBar.setWidth("session", 200);
	 statusBar.setWidth("user", 100);
	 statusBar.setWidth("time", 50);

	 statusBar.setText("status", LangModel.getString("statusReady"));
	 statusBar.setText("server", LangModel.getString("statusNoConnection"));
	 statusBar.setText("session", LangModel.getString("statusNoSession"));
	 statusBar.setText("user", LangModel.getString("statusNoUser"));
	 statusBar.setText("time", " ");
	 statusBar.organize();

		statusBar.addDispatcher(Environment.getDispatcher());
		statusBar.addDispatcher(internal_dispatcher);

	 aContext.setDispatcher(internal_dispatcher);

		internal_dispatcher.register(this, "contextchange");
		Environment.getDispatcher().register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
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


		aModel.setCommand("menuUserProfile", new OpenObjectFrameCommand(desktopPane, aContext, "operatorprofile", new StubDisplayModel(new String[] {"id", "name", "user_id", "created", "login"}, new String[] {"Идентификатор", "Название", "Владелец", "Создан", "Пользователь"}), OperatorProfile.class));

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

		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuArchitecture", true);
		aModel.setEnabled("menuUser", true);
		aModel.setEnabled("menuAccess", true);

		aModel.fireModelChanged("");

		ConnectionInterface connection = ConnectionInterface.getInstance();
		if (connection.isConnected())
			internal_dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_OPENED_EVENT));
		if (SessionInterface.getActiveSession() != null) {
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			if (aContext.getSessionInterface().isOpened())
				internal_dispatcher.notify(new ContextChangeEvent(aContext.getSessionInterface(), ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(connection));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	public void setContext(ApplicationContext applicationContext) {
		this.aContext = applicationContext;
		ApplicationModel applicationModel = applicationContext.getApplicationModel();
		if (applicationModel == null) {
			applicationModel = ApplicationModel.getInstance();
			applicationContext.setApplicationModel(applicationModel);
		}
		setModel(applicationModel);
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
		ApplicationModel aModel = aContext.getApplicationModel();
		if(cce.SESSION_OPENED)
		{
		  SessionInterface ssi = (SessionInterface)cce.getSource();
		  if(aContext.getSessionInterface().equals(ssi))
		  {

			 Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Подгрузка административных объектов..."));

			 DataSourceInterface dataSource = aContext.getDataSource();

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

			 statusBar.setText("status", LangModel.getString("statusReady"));
			 statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
			 statusBar.setText("user", aContext.getSessionInterface().getUser());

			 Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, " "));

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
		  if (aContext.getSessionInterface().equals(ssi))
		  {
			 setSessionClosed();
			 statusBar.setText("status", LangModel.getString("statusReady"));
			 statusBar.setText("session", LangModel.getString("statusNoSession"));
			 statusBar.setText("user", LangModel.getString("statusNoUser"));
		  }
		}
			if (cce.CONNECTION_OPENED) {
				setConnectionOpened();
				statusBar.setText("status", LangModel.getString("statusReady"));
				statusBar.setText("server", ConnectionInterface.getInstance().getServerName());
			}
			if (cce.CONNECTION_CLOSED) {
				statusBar.setText("status", LangModel.getString("statusError"));
				statusBar.setText("server", LangModel.getString("statusConnectionError"));
				statusBar.setText("status", LangModel.getString("statusDisconnected"));
				statusBar.setText("server", LangModel.getString("statusNoConnection"));
				setConnectionClosed();
			}
			if (cce.CONNECTION_FAILED) {
				statusBar.setText("status", LangModel.getString("statusError"));
				statusBar.setText("server", LangModel.getString("statusConnectionError"));
				setConnectionFailed();
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

		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuViewNavigator", true);


		aModel.setEnabled("menuViewWhoAmI", true);
		aModel.setEnabled("menuAccessDomain", true);
		aModel.setEnabled("menuAccessMaintain", true);
		aModel.setEnabled("menuArchitectureAgent", true);
		aModel.setEnabled("menuArchitectureClient", true);
		aModel.setEnabled("menuArchitectureServer", true);
		aModel.setEnabled("menuViewOpenAll", true);
		aModel.setEnabled("menuViewOpenObjectsWindow", true);
		aModel.setEnabled("menuAccessModul", true);
		aModel.setEnabled("menuUser", true);
		aModel.setEnabled("menuUserCategory", true);
		aModel.setEnabled("menuUserGroup", true);
		aModel.setEnabled("menuUserRole", true);
		aModel.setEnabled("menuUserPrivilege", true);
		aModel.setEnabled("menuUserProfile", true);

		aModel.setEnabled("menuSessionNew", false);

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

		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);


		aModel.setEnabled("menuViewNavigator", false);

		aModel.setEnabled("menuViewWhoAmI", false);
		aModel.setEnabled("menuAccessDomain", false);
		aModel.setEnabled("menuAccessMaintain", false);
		aModel.setEnabled("menuArchitectureAgent", false);
		aModel.setEnabled("menuArchitectureClient", false);
		aModel.setEnabled("menuArchitectureServer", false);

		aModel.setEnabled("menuViewOpenAll", false);
		aModel.setEnabled("menuViewOpenObjectsWindow", false);
		aModel.setEnabled("menuAccessModul", false);
		aModel.setEnabled("menuUserCategory", false);
		aModel.setEnabled("menuUserGroup", false);
		aModel.setEnabled("menuUserRole", false);
		aModel.setEnabled("menuUserPrivilege", false);
		aModel.setEnabled("menuUserProfile", false);

		aModel.setEnabled("menuSessionNew", true);

	 aModel.fireModelChanged("");
  }

  public Dispatcher getInternalDispatcher()
  {
	 return internal_dispatcher;
  }


	protected void processWindowEvent(WindowEvent e) {
		int id = e.getID();
		if (id == WindowEvent.WINDOW_ACTIVATED)
			Environment.setActiveWindow(this);
		else if (id == WindowEvent.WINDOW_CLOSING) {
			internal_dispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}
