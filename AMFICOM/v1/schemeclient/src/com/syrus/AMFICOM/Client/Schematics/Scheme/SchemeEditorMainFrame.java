package com.syrus.AMFICOM.Client.Schematics.Scheme;

import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Schematics.UI.*;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.client_.scheme.ui.*;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.client_.scheme.ui.GeneralPropertiesFrame;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

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
	SchemeViewerFrame ugoFrame;
	GeneralPropertiesFrame generalFrame;
//	ElementsListFrame elementsListFrame;
	CharacteristicPropertiesFrame characteristicFrame;
	JInternalFrame treeFrame;
	ArrayList graphs = new ArrayList();

	UgoTabbedPane ugoPane;
	SchemeTabbedPane schemeTab;

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
//	 Get the currently installed look and feel
    UIDefaults uidefs = UIManager.getLookAndFeelDefaults();
    
    // Retrieve the keys. Can't use an iterator since the map
    // may be modified during the iteration. So retrieve all at once.
    String[] keys = (String[])uidefs.keySet().toArray(new String[0]);
    for (int i = 0; i < keys.length; i++)
    	System.out.println(keys[i]);
    
    for (int i=0; i<keys.length; i++) {
        Object v = uidefs.get(keys[i]);
    
        if (v instanceof Integer) {
            int intVal = uidefs.getInt(keys[i]);
        } else if (v instanceof Boolean) {
            boolean boolVal = uidefs.getBoolean(keys[i]);
        } else if (v instanceof String) {
            String strVal = uidefs.getString(keys[i]);
        } else if (v instanceof Dimension) {
            Dimension dimVal = uidefs.getDimension(keys[i]);
        } else if (v instanceof Insets) {
            Insets insetsVal = uidefs.getInsets(keys[i]);
        } else if (v instanceof Color) {
            Color colorVal = uidefs.getColor(keys[i]);
        } else if (v instanceof Font) {
            Font fontVal = uidefs.getFont(keys[i]);
        } else if (v instanceof Border) {
            Border borderVal = uidefs.getBorder(keys[i]);
        } else if (v instanceof Icon) {
            Icon iconVal = uidefs.getIcon(keys[i]);
        } else if (v instanceof javax.swing.text.JTextComponent.KeyBinding[]) {
            javax.swing.text.JTextComponent.KeyBinding[] keyBindsVal =
                (javax.swing.text.JTextComponent.KeyBinding[])uidefs.get(keys[i]);
        } else if (v instanceof InputMap) {
            InputMap imapVal = (InputMap)uidefs.get(keys[i]);
        } else {
            // Unknown type
        }
    }

    

		
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

		schemeTab = new SchemeTabbedPane(aContext);

		editorFrame = new PrimarySchemeEditorFrame(aContext, schemeTab);
		editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
		desktopPane.add(editorFrame);
//		graphs.add(panel);

		ugoPane = new UgoTabbedPane(aContext);
//		upanel = new UgoPanel(aContext);
		ugoPane.getPanel().setUgoInsertable(false);
		ugoFrame = new SchemeViewerFrame(aContext, ugoPane)
		{
			protected void closeFrame()
			{
				ugoPane.getPanel().getGraph().setScheme(null);
			}
		};
		ugoFrame.setTitle(LangModelSchematics.getString("elementsUGOTitle"));
		desktopPane.add(ugoFrame);
		//graphs.add(upanel);

//		scheme_graph = epanel.getGraph();

		generalFrame = new GeneralPropertiesFrame("Title", aContext);
		desktopPane.add(generalFrame);

//		elementsListFrame = new ElementsListFrame(aContext, true);
//		desktopPane.add(elementsListFrame);
		
		characteristicFrame = new CharacteristicPropertiesFrame("Title", aContext);
		desktopPane.add(characteristicFrame);

		//SchemeTreeFrame treeFrame = new SchemeTreeFrame(aContext, SchemeTreeFrame.SCHEME);
		//desktopPane.add(treeFrame);
		treeFrame = new JInternalFrame();
		treeFrame.setIconifiable(true);
		treeFrame.setClosable(true);
		treeFrame.setResizable(true);
		treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
//		internal_dispatcher.register(this, CatalogNavigateEvent.type);
		internal_dispatcher.register(this, "contextchange");
		internal_dispatcher.register(this, "addschemeevent");
		internal_dispatcher.register(this, "addschemeelementevent");
		Environment.getDispatcher().register(this, "contextchange");

		setContext(aContext);
		setDefaultModel (aModel);

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));

		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

//		SchemePanel epanel = (SchemePanel)schemeTab.getPanel();

		aModel.setCommand("menuSchemeNew", new SchemeNewCommand(aContext));
		aModel.setCommand("menuSchemeLoad", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(aContext, schemeTab, ugoPane));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(aContext, schemeTab, ugoPane));
		//aModel.setCommand("menuInsertToCatalog", new InsertToCatalogCommand(aContext, epanel.getGraph()));
//		aModel.setCommand("menuInsertToCatalog", new InsertToCatalogCommand(aContext, schemeTab));
		aModel.setCommand("menuSchemeExport", new SchemeToFileCommand(Environment.getDispatcher(), aContext));
//		aModel.setCommand("menuSchemeImport", new SchemeFromFileCommand(Environment.getDispatcher(), aContext));

		aModel.setCommand("menuPathNew", new PathNewCommand(aContext, schemeTab));
		aModel.setCommand("menuPathEdit", new PathEditCommand(aContext, schemeTab));
		aModel.setCommand("menuPathAddStart", new PathSetStartCommand(aContext, schemeTab));
		aModel.setCommand("menuPathAddEnd", new PathSetEndCommand(aContext, schemeTab));
		aModel.setCommand("menuPathAddLink", new PathAddLinkCommand(aContext, schemeTab));

		aModel.setCommand("menuPathRemoveLink", new PathRemoveLinkCommand(aContext, scheme_graph));
		aModel.setCommand("menuPathAutoCreate", new PathAutoCreateCommand(aContext, schemeTab.getPanel()));

		aModel.setCommand("menuPathSave", new PathSaveCommand(aContext, schemeTab));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(aContext, schemeTab));
		aModel.setCommand("menuPathDelete", new PathDeleteCommand(aContext, schemeTab));

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
		aModel.setCommand("menuWindowProps", new ShowFrameCommand(desktopPane, generalFrame));
		aModel.setCommand("menuWindowList", new ShowFrameCommand(desktopPane, characteristicFrame));

		aModel.fireModelChanged("");

		if(ConnectionInterface.getInstance() != null)
		{
			if(ConnectionInterface.getInstance().isConnected())
				internal_dispatcher.notify(new ContextChangeEvent(
						ConnectionInterface.getInstance(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			if(aContext.getSessionInterface().isOpened())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(ConnectionInterface.getInstance()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(ApplicationModel.getInstance());
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

//		aModel.setVisible("menuSchemeExport", false);
//		aModel.setVisible("menuSchemeImport", false);

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
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
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
					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", ConnectionInterface.getInstance().getServerName());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();
				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci))
				{
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
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
				aModel.setEnabled("menuPathAutoCreate", true);
				aModel.getCommand("menuPathAutoCreate").setParameter("panel", schemeTab.getPanel());
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
				aModel.setEnabled("menuPathAutoCreate", false);
				aModel.setEnabled("menuPathCancel", false);
				aModel.fireModelChanged("");
			}
			if(cpe.PE_SELECTED)
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel"))
				{
					aModel.setEnabled("menuPathRemoveLink", true);
					aModel.fireModelChanged("");
				}
			}
			else
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel"))
				{
					aModel.setEnabled("menuPathRemoveLink", false);
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
			else
			{
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathDelete", false);
				aModel.fireModelChanged("");
			}
		}
	/*	else if (ae.getActionCommand().equals(CatalogNavigateEvent.type))
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
		}*/
		else if (ae.getActionCommand().equals("addschemeevent"))
		{
			com.syrus.AMFICOM.general.Identifier scheme_id =
					(com.syrus.AMFICOM.general.Identifier)ae.getSource();
			try {
				Scheme scheme = (Scheme)SchemeStorableObjectPool.getStorableObject(
								scheme_id, true);
				schemeTab.openScheme(scheme);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (ae.getActionCommand().equals("addschemeelementevent"))
		{
			com.syrus.AMFICOM.general.Identifier se_id =
					(com.syrus.AMFICOM.general.Identifier)ae.getSource();
			try {
				SchemeElement se = (SchemeElement)SchemeStorableObjectPool.getStorableObject(se_id, true);
				schemeTab.openSchemeElement(se);
			}
			catch (ApplicationException ex1) {
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
		aModel.setEnabled("menuSessionDomain", true);
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
		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		internal_dispatcher.notify(new ContextChangeEvent(domain_id, ContextChangeEvent.DOMAIN_SELECTED_EVENT));

		editorFrame.setVisible(true);
		ugoFrame.setVisible(true);
		generalFrame.setVisible(true);
		characteristicFrame.setVisible(true);
		treeFrame.setVisible(true);

		SchemeTreeModel model = new SchemeTreeModel(aContext);
		ElementsNavigatorPanel utp = new ElementsNavigatorPanel(aContext, internal_dispatcher, model);
		//UniTreePanel utp = new UniTreePanel(internal_dispatcher, aContext, model);
		//utp.setBorder(BorderFactory.createLoweredBevelBorder());
//		utp.getTree().setRootVisible(false);
		treeFrame.getContentPane().removeAll();
		treeFrame.getContentPane().add(utp, BorderLayout.CENTER);
		treeFrame.updateUI();
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.setEnabled("menuSchemeNew", true);
		aModel.setEnabled("menuSchemeLoad", true);
		aModel.setEnabled("menuSchemeSave", true);
		aModel.setEnabled("menuSchemeSaveAs", true);
		aModel.setEnabled("menuInsertToCatalog", true);
		aModel.setEnabled("menuPathNew", true);
		aModel.setEnabled("menuReportCreate", true);

		aModel.fireModelChanged();

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			statusBar.setText(StatusBarModel.FIELD_DOMAIN, domain.getName());
		}
		catch (ApplicationException ex) {
		}

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
		generalFrame.setVisible(false);
		characteristicFrame.setVisible(false);
		treeFrame.setVisible(false);

		statusBar.setText("domain", "����� �� ����������");
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		new SchemeEditorWindowArranger(this).arrange();
	}

	void this_windowClosing(WindowEvent e)
	{
		UgoPanel[] p = schemeTab.getAllPanels();
		for (int i = 0; i < p.length; i++)
		{
			schemeTab.selectPanel(p[i]);
			/*
			if (p[i].getGraph().getSchemeElement() != null && p[i].getGraph().isGraphChanged())
			{
				schemeTab.selectPanel(p[i]);
				int res = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						"������� \"" + p[i].getGraph().getSchemeElement().getName() +
						"\" ��� �������. ��������� ����� ����� ���������?",
						"�������������",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (res == JOptionPane.OK_OPTION)
				{
					SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab, ugoPane);
					ssc.execute();
					if (ssc.ret_code == SchemeSaveCommand.CANCEL)
						return;
				}
				else if (res == JOptionPane.CANCEL_OPTION)
					return;
			}*/
			if (!schemeTab.removePanel(p[i]))
				return;
		}

		p = schemeTab.getAllPanels();
		for (int i = 0; i < p.length; i++)
		{
			schemeTab.selectPanel(p[i]);
			/*
			if (p[i].getGraph().getScheme() != null && p[i].getGraph().isGraphChanged())
			{
				schemeTab.selectPanel(p[i]);
				int res = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						"����� \"" + p[i].getGraph().getScheme().getName() +
						"\" ���� ��������. ��������� ����� ����� ���������?",
						"�������������",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (res == JOptionPane.OK_OPTION)
				{
					SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab, ugoPane);
					ssc.execute();
					if (ssc.ret_code == SchemeSaveCommand.CANCEL)
						return;
				}
				else if (res == JOptionPane.CANCEL_OPTION)
					return;
			}*/
			if (!schemeTab.removePanel(p[i]))
				return;
		}
		internal_dispatcher.unregister(this, "contextchange");
		Environment.getDispatcher().unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
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
		normalize(f.generalFrame);
		normalize(f.characteristicFrame);
		normalize(f.treeFrame);
		if (catalogFrame != null)
			normalize(catalogFrame);

		f.editorFrame.setSize(11*w/20, h);
		f.ugoFrame.setSize(w/4, 3 * h / 10);
		f.generalFrame.setSize(w/4, 4 * h / 10);
		f.characteristicFrame.setSize(w/4, 3 * h / 10);
		f.treeFrame.setSize(w/5, h);
		if (catalogFrame != null)
			catalogFrame.setSize(3*w/5, h);

		f.editorFrame.setLocation(w/5, 0);
		f.ugoFrame.setLocation(3*w/4, 7 * h / 10);
		f.generalFrame.setLocation(3*w/4, 0);
		f.characteristicFrame.setLocation(3*w/4, 4*h/10);
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

