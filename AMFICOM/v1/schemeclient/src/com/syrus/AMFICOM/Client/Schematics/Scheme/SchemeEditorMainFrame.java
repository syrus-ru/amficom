package com.syrus.AMFICOM.Client.Schematics.Scheme;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Report.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;
import com.syrus.AMFICOM.Client.Schematics.UI.*;

public class SchemeEditorMainFrame extends JFrame
																				implements OperationListener
{
	public ApplicationContext aContext;
	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private Dispatcher internal_dispatcher = new Dispatcher();

	JPanel mainPanel = new JPanel();
	SchemeEditorToolBar toolBar = new SchemeEditorToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	SchemeEditorMenuBar menuBar = new SchemeEditorMenuBar();
	SchemeGraph scheme_graph;

	static int scheme_count = 0;
	PrimarySchemeEditorFrame editorFrame;
	SchemePanel epanel;
	SchemeViewerFrame ugoFrame;
	UgoPanel upanel;
	PropsFrame propsFrame;
	ElementsListFrame elementsListFrame;
	JInternalFrame treeFrame;
	ArrayList graphs = new ArrayList();

	public SchemeEditorMainFrame(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
		this.aContext.setDispatcher(internal_dispatcher);

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

	//	new ImportSchemeCommand(internal_dispatcher, aContext).execute();
	}

	public SchemeEditorMainFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		this.addComponentListener(new SchemeEditorMainFrame_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		setContentPane(mainPanel);
		setResizable(true);
		setTitle(LangModelSchematics.getString("SchemeEditorTitle"));
		setJMenuBar(menuBar);

		mainPanel.setLayout(new BorderLayout());
		setBackground(SystemColor.control);
		desktopPane.setLayout(null);
		desktopPane.setBackground(SystemColor.control.darker().darker());

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add("status");
		statusBar.add("server");
		statusBar.add("session");
		statusBar.add("user");
		statusBar.add("domain");
		statusBar.add("time");

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		//epanel = new SchemePanelNoEdition(aContext);
		epanel = new SchemePanel(aContext);
		editorFrame = new PrimarySchemeEditorFrame(aContext, epanel);
		editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
		desktopPane.add(editorFrame);
		graphs.add(epanel);

		upanel = new UgoPanel(aContext);
		upanel.setUgoInsertable(false);
		ugoFrame = new SchemeViewerFrame(aContext, upanel);
		ugoFrame = new SchemeViewerFrame(aContext, upanel)
		{
			protected void closeFrame()
			{
				panel.scheme = new Scheme();
			}
		};
		ugoFrame.setTitle(LangModelSchematics.getString("elementsUGOTitle"));
		desktopPane.add(ugoFrame);
		//graphs.add(upanel);

		scheme_graph = epanel.getGraph();

		propsFrame = new PropsFrame(aContext, true);
		desktopPane.add(propsFrame);

		elementsListFrame = new ElementsListFrame(aContext, true);
		desktopPane.add(elementsListFrame);

		//SchemeTreeFrame treeFrame = new SchemeTreeFrame(aContext, SchemeTreeFrame.SCHEME);
		//desktopPane.add(treeFrame);
		treeFrame = new JInternalFrame();
		treeFrame.setIconifiable(true);
		treeFrame.setClosable(true);
		treeFrame.setResizable(true);
		treeFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		treeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
		treeFrame.getContentPane().setLayout(new BorderLayout());
		desktopPane.add(treeFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

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

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.register(this, CreatePathEvent.typ);
		internal_dispatcher.register(this, SchemeNavigateEvent.type);
		internal_dispatcher.register(this, CatalogNavigateEvent.type);
		internal_dispatcher.register(this, "contextchange");
		internal_dispatcher.register(this, "addschemeevent");
		internal_dispatcher.register(this, "addschemeelementevent");
		Environment.the_dispatcher.register(this, "contextchange");

		setContext(aContext);
		setDefaultModel (aModel);

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));

		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuSchemeNew", new SchemeNewCommand(aContext, epanel, upanel));
		aModel.setCommand("menuSchemeLoad", new SchemeOpenCommand(aContext, scheme_graph));
		aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(aContext, epanel, upanel));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(aContext, epanel, upanel));
		//aModel.setCommand("menuInsertToCatalog", new InsertToCatalogCommand(aContext, epanel.getGraph()));
		aModel.setCommand("menuInsertToCatalog", new InsertToCatalogCommand(aContext, epanel, upanel, false));
		aModel.setCommand("menuSchemeExport", new SchemeToFileCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSchemeImport", new SchemeFromFileCommand(Environment.the_dispatcher, aContext));

		aModel.setCommand("menuPathNew", new PathNewCommand(aContext, epanel));
		aModel.setCommand("menuPathEdit", new PathEditCommand(aContext, epanel));
		aModel.setCommand("menuPathAddStart", new PathSetStartCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathAddEnd", new PathSetEndCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathAddLink", new PathAddLinkCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathRemoveLink", new PathRemoveLinkCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathUpdateLink", new PathUpdateLinkCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathSave", new PathSaveCommand(aContext, epanel, upanel));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathDelete", new PathDeleteCommand(aContext, epanel));

		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
		for (Iterator it = graphs.iterator(); it.hasNext();)
			rc.setParameter(CreateSchemeReportCommand.PANEL, it.next());
		rc.setParameter(CreateSchemeReportCommand.TYPE, ReportTemplate.rtt_Scheme);
		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new SchemeEditorWindowArranger(this)));
		aModel.setCommand("menuWindowTree", new ShowFrameCommand(desktopPane, treeFrame));
		aModel.setCommand("menuWindowScheme", new ShowFrameCommand(desktopPane, editorFrame));
		aModel.setCommand("menuWindowCatalog", new ShowCatalogFrameCommand(aContext, desktopPane));
		aModel.setCommand("menuWindowUgo", new ShowFrameCommand(desktopPane, ugoFrame));
		aModel.setCommand("menuWindowProps", new ShowFrameCommand(desktopPane, propsFrame));
		aModel.setCommand("menuWindowList", new ShowFrameCommand(desktopPane, elementsListFrame));

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
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuScheme", true);
		aModel.setEnabled("menuPath", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuSchemeExport", false);
		aModel.setVisible("menuSchemeImport", false);

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			ApplicationModel aModel = aContext.getApplicationModel();
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

					setSessionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);
					setSessionClosed();
					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();
					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));
					setConnectionClosed();
				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));
					setConnectionFailed();
				}
			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
				//aContext.getSessionInterface().getDomainId();
			}
		}
		else if (ae.getActionCommand().equals(CreatePathEvent.typ))
		{
			CreatePathEvent cpe = (CreatePathEvent)ae;
			if (cpe.CREATE_PATH || cpe.EDIT_PATH)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", true);
				aModel.setEnabled("menuPathAddStart", true);
				aModel.setEnabled("menuPathAddEnd", true);
				aModel.setEnabled("menuPathAddLink", true);
				aModel.setEnabled("menuPathCancel", true);
				aModel.fireModelChanged("");
			}
			if (cpe.CANCEL_PATH_CREATION || cpe.SAVE_PATH)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", false);
				aModel.setEnabled("menuPathAddStart", false);
				aModel.setEnabled("menuPathAddEnd", false);
				aModel.setEnabled("menuPathAddLink", false);
				aModel.setEnabled("menuPathRemoveLink", false);
				aModel.setEnabled("menuPathUpdateLink", false);
				aModel.setEnabled("menuPathCancel", false);
				aModel.fireModelChanged("");
			}
			if(cpe.PE_SELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel"))
				{
					aModel.setEnabled("menuPathRemoveLink", true);
					aModel.setEnabled("menuPathUpdateLink", true);
					aModel.fireModelChanged("");
				}
			}
			else
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel"))
				{
					aModel.setEnabled("menuPathRemoveLink", false);
					aModel.setEnabled("menuPathUpdateLink", false);
					aModel.fireModelChanged("");
				}
			}
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent sne = (SchemeNavigateEvent)ae;
			if (sne.SCHEME_PATH_SELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathDelete", true);
				aModel.fireModelChanged("");
			}
			if (sne.SCHEME_ALL_DESELECTED || sne.SCHEME_PATH_DESELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathDelete", false);
				aModel.fireModelChanged("");
			}
		}
		else if (ae.getActionCommand().equals(CatalogNavigateEvent.type))
		{
			CatalogNavigateEvent cne = (CatalogNavigateEvent)ae;
			if (cne.CATALOG_PATH_SELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathDelete", true);
				aModel.fireModelChanged("");
			}
			if (cne.CATALOG_PATH_DESELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathDelete", false);
				aModel.fireModelChanged("");
			}
		}
		else if (ae.getActionCommand().equals("addschemeevent"))
		{
			String scheme_id = (String)ae.getSource();
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			scheme.unpack();

			SchemePanel panel = new SchemePanel(aContext);
			panel.ignore_loading = true;
			//ElementsEditorFrame frame = new ElementsEditorFrame(aContext, panel);
			SchemeEditorFrame frame = new SchemeEditorFrame(aContext, panel);
			frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(scheme.getName());
			desktopPane.add(frame);
//			schemeFrames.add(frame);

			if (scheme_count > 10)
				scheme_count = 0;

			int w = desktopPane.getSize().width;
			int h = desktopPane.getSize().height;
			frame.setSize(3*w/5, h / 2);
			frame.setLocation(w/5, h / 2);//+ 25 * scheme_count
			scheme_count++;
			frame.setVisible(true);
			frame.toFront();

			panel.openScheme(scheme);
		}
		else if (ae.getActionCommand().equals("addschemeelementevent"))
		{
			String se_id = (String)ae.getSource();
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, se_id);
			se.unpack();

			SchemePanel panel = new SchemePanel(aContext);
//			ElementsPanel panel = new ElementsPanel(aContext);
//			panel.setGraphSize(new Dimension());
			ElementsEditorFrame frame = new ElementsEditorFrame(aContext, panel);
			frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(se.getName());
			desktopPane.add(frame);

			int w = desktopPane.getSize().width;
			int h = desktopPane.getSize().height;
			frame.setSize(3*w/5, h / 2);
			frame.setLocation(w/5, h / 2);//+ 25 * scheme_count
			scheme_count++;
			frame.setVisible(true);
			frame.toFront();

			panel.openSchemeElement(se);
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
		Checker checker = new Checker(aContext.getDataSourceInterface());
		if(!checker.checkCommand(checker.schemeEditing))
		{
			JOptionPane.showMessageDialog(this, "Недостаточно прав для работы с модулем схем.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		new SchemeDataSourceImage(dataSource).LoadSchemes();
		new ConfigDataSourceImage(dataSource).LoadNet();
		new ConfigDataSourceImage(dataSource).LoadISM();

		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new MapDataSourceImage(dataSource).LoadProtoElements();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionDomain");
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSchemeExport", true);
		aModel.setEnabled("menuSchemeImport", true);

		aModel.setEnabled("menuWindowArrange", true);
		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowCatalog", true);
		aModel.setEnabled("menuWindowUgo", true);
		aModel.setEnabled("menuWindowProps", true);
		aModel.setEnabled("menuWindowList", true);
		aModel.fireModelChanged("");
		String domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals(""))
			internal_dispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));

		editorFrame.setVisible(true);
		ugoFrame.setVisible(true);
		propsFrame.setVisible(true);
		elementsListFrame.setVisible(true);
		treeFrame.setVisible(true);

		SchemeTreeModel model = new SchemeTreeModel(aContext.getDataSourceInterface());
		ElementsNavigatorPanel utp = new ElementsNavigatorPanel(aContext, internal_dispatcher, model);
		//UniTreePanel utp = new UniTreePanel(internal_dispatcher, aContext, model);
		//utp.setBorder(BorderFactory.createLoweredBevelBorder());
		utp.getTree().setRootVisible(false);
		treeFrame.getContentPane().removeAll();
		treeFrame.getContentPane().add(utp, BorderLayout.CENTER);
		treeFrame.updateUI();
	}

	public void setDomainSelected()
	{
//		new ImportSchemeCommand(internal_dispatcher, aContext).execute();
//		new ExportSchemeCommand(internal_dispatcher, aContext).execute();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");

		aModel.enable("menuSchemeNew");
		aModel.enable("menuSchemeLoad");
		aModel.enable("menuSchemeSave");
		aModel.enable("menuSchemeSaveAs");
		aModel.enable("menuInsertToCatalog");
		aModel.enable("menuPathNew");
		aModel.enable("menuReportCreate");

		aModel.fireModelChanged("");

		String domain_id = aContext.getSessionInterface().getDomainId();
		statusBar.setText("domain", Pool.getName("domain", domain_id));

		internal_dispatcher.notify(new TreeListSelectionEvent("",  TreeListSelectionEvent.REFRESH_EVENT));
	}


	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuTrace", false);
		aModel.setEnabled("menuAnalyseUpload", false);
		aModel.setEnabled("menuSessionNew", true);

		aModel.setEnabled("menuSchemeNew", false);
		aModel.setEnabled("menuSchemeLoad", false);
		aModel.setEnabled("menuSchemeSave", false);
		aModel.setEnabled("menuSchemeSaveAs", false);
		aModel.setEnabled("menuInsertToCatalog", false);
		aModel.setEnabled("menuSchemeExport", false);
		aModel.setEnabled("menuSchemeImport", false);
		aModel.setEnabled("menuPathNew", false);
		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowArrange", false);
		aModel.setEnabled("menuWindowTree", false);
		aModel.setEnabled("menuWindowScheme", false);
		aModel.setEnabled("menuWindowCatalog", false);
		aModel.setEnabled("menuWindowUgo", false);
		aModel.setEnabled("menuWindowProps", false);
		aModel.setEnabled("menuWindowList", false);

		aModel.fireModelChanged("");

		editorFrame.setVisible(false);
		ugoFrame.setVisible(false);
		propsFrame.setVisible(false);
		elementsListFrame.setVisible(false);
		treeFrame.setVisible(false);

		statusBar.setText("domain", "Домен не установлен");
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		new SchemeEditorWindowArranger(this).arrange();
	}

	void this_windowClosing(WindowEvent e)
	{
		if(epanel.scheme != null && epanel.getGraph().isGraphChanged())
		{
			int res = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Схема была изменена. Сохранить схему перед закрытием?",
					"Подтверждение",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, epanel, upanel);
				ssc.execute();
				if (ssc.ret_code == SchemeSaveCommand.CANCEL)
					return;
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}

		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			this_windowClosing(e);
			return;
		}
		super.processWindowEvent(e);
	}
}
class SchemeEditorWindowArranger extends WindowArranger
{
	public SchemeEditorWindowArranger(SchemeEditorMainFrame mainframe)
	{
		super(mainframe);
	}

	public void arrange()
	{
		SchemeEditorMainFrame f = (SchemeEditorMainFrame)mainframe;
		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

		ObjectResourceCatalogFrame catalogFrame = null;
		JInternalFrame[] frames = f.desktopPane.getAllFrames();
		for (int i = 0; i < frames.length; i++)
			if (frames[i] instanceof ObjectResourceCatalogFrame)
			{
				catalogFrame = (ObjectResourceCatalogFrame)frames[i];
				break;
			}

//		f.editorFrame.setVisible(true);
//		f.ugoFrame.setVisible(true);
//		f.elementsListFrame.setVisible(true);
//		f.propsFrame.setVisible(true);
//		f.treeFrame.setVisible(true);

		normalize(f.editorFrame);
		normalize(f.ugoFrame);
		normalize(f.elementsListFrame);
		normalize(f.propsFrame);
		normalize(f.treeFrame);
		if (catalogFrame != null)
			normalize(catalogFrame);

		f.editorFrame.setSize(3*w/5, h);
		f.ugoFrame.setSize(w/5, 2 * h / 5);
		f.elementsListFrame.setSize(w/5, 3 * h / 10);
		f.propsFrame.setSize(w/5, 3 * h / 10);
		f.treeFrame.setSize(w/5, h);
		if (catalogFrame != null)
			catalogFrame.setSize(3*w/5, h);

		f.editorFrame.setLocation(w/5, 0);
		f.ugoFrame.setLocation(4*w/5, 3 * h / 5);
		f.elementsListFrame.setLocation(4*w/5, 0);
		f.propsFrame.setLocation(4*w/5, 3*h/10);
		f.treeFrame.setLocation(0, 0);
		if (catalogFrame != null)
			catalogFrame.setLocation(w/5, 0);
	}
}


class SchemeEditorMainFrame_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	SchemeEditorMainFrame adaptee;

	SchemeEditorMainFrame_this_componentAdapter(SchemeEditorMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}