package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Schematics.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.UI.*;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.general.*;

public class ElementsEditorMainFrame extends JFrame
																				implements OperationListener
{
	public ApplicationContext aContext;
	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private Dispatcher internal_dispatcher = new Dispatcher();

	JPanel mainPanel = new JPanel();
	ElementsEditorToolBar toolBar = new ElementsEditorToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	ElementsEditorMenuBar menuBar = new ElementsEditorMenuBar();

	PrimaryElementsEditorFrame editorFrame;
//	ElementsPanel epanel;
	SchemeViewerFrame ugoFrame;
//	UgoPanel upanel;
	PropsFrame propsFrame;
	ElementsListFrame elementsListFrame;
	//ElementsUploadListFrame elementsListFrame;
	JInternalFrame treeFrame;

	UgoTabbedPane ugoPane;
	ElementsTabbedPane elementsTab;

	public ElementsEditorMainFrame(ApplicationContext aContext)
	{
		super();
		setContext(aContext);
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
	}

	public ElementsEditorMainFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		this.addComponentListener(new ElementsEditorMainFrame_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		setContentPane(mainPanel);
		setResizable(true);
		setTitle(LangModelSchematics.getString("ElementsEditorTitle"));
		setJMenuBar(menuBar);

		mainPanel.setLayout(new BorderLayout());
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

		elementsTab = new ElementsTabbedPane(aContext);
		editorFrame = new PrimaryElementsEditorFrame(aContext, elementsTab);
		editorFrame.setTitle(LangModelSchematics.getString("elementsMainTitle"));
		desktopPane.add(editorFrame);

		ugoPane = new UgoTabbedPane(aContext);
		ugoFrame = new SchemeViewerFrame(aContext, ugoPane);
		ugoFrame.setTitle(LangModelSchematics.getString("elementsUGOTitle"));
		desktopPane.add(ugoFrame);

		propsFrame = new PropsFrame(aContext, true);
		desktopPane.add(propsFrame);

		elementsListFrame = new ElementsListFrame(aContext, true);
		//elementsListFrame = new ElementsUploadListFrame(aContext, true);
		desktopPane.add(elementsListFrame);

		//SchemeTreeFrame treeFrame = new SchemeTreeFrame(aContext, SchemeTreeFrame.COMPONENT);
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

//		new ViewElementsNavigatorCommand(desktopPane, aContext, "").execute();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		//treeFrame.setVisible(true);
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
		internal_dispatcher.register(this, "contextchange");
		Environment.getDispatcher().register(this, "contextchange");

		setContext(aContext);
		setDefaultModel(aModel);

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuComponentNew", new ComponentNewCommand(aContext, elementsTab.getPanel().getGraph(), ugoPane.getPanel().getGraph()));
		aModel.setCommand("menuComponentSave", new ComponentSaveCommand(aContext, elementsTab.getPanel().getGraph(), ugoPane.getPanel().getGraph()));

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new ElementsEditorWindowArranger(this)));
		aModel.setCommand("menuWindowTree", new ShowFrameCommand(desktopPane, treeFrame));
		aModel.setCommand("menuWindowScheme", new ShowFrameCommand(desktopPane, editorFrame));
		aModel.setCommand("menuWindowCatalog", new ShowCatalogFrameCommand(aContext, desktopPane));
		aModel.setCommand("menuWindowUgo", new ShowFrameCommand(desktopPane, ugoFrame));
		aModel.setCommand("menuWindowProps", new ShowFrameCommand(desktopPane, propsFrame));
		aModel.setCommand("menuWindowList", new ShowFrameCommand(desktopPane, elementsListFrame));

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

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuComponent", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuSessionDomain", false);
		aModel.setVisible("menuSchemeExport", false);
		aModel.setVisible("menuSchemeImport", false);
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
//		DataSourceInterface dataSource = aContext.getDataSourceInterface();
//		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
//		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
//		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
//		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
//		new MapDataSourceImage(dataSource).loadProtoElements();
		//new SchemeDataSourceImage(dataSource).LoadSchemes();
//    new ConfigDataSourceImage(dataSource).LoadNet();
//		new ConfigDataSourceImage(dataSource).LoadISM();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuSessionNew", false);
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
		propsFrame.setVisible(true);
		elementsListFrame.setVisible(true);
		treeFrame.setVisible(true);

		ElementsTreeModel model = new ElementsTreeModel(aContext);
		ElementsNavigatorPanel utp = new ElementsNavigatorPanel(aContext, internal_dispatcher, model);
		utp.getTree().setRootVisible(false);
		//UniTreePanel utp = new UniTreePanel(internal_dispatcher, aContext, model);
		//utp.setBorder(BorderFactory.createLoweredBevelBorder());
		treeFrame.getContentPane().removeAll();
		treeFrame.getContentPane().add(utp, BorderLayout.CENTER);
		treeFrame.updateUI();
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuComponentNew", true);
		aModel.setEnabled("menuComponentSave", true);
		aModel.fireModelChanged("");

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			statusBar.setText("domain", domain.getName());
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuTrace", false);
		aModel.setEnabled("menuAnalyseUpload", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuSessionNew", true);
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

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		new ElementsEditorWindowArranger(this).arrange();
	}

	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.unregister(this, "contextchange");
		Environment.getDispatcher().unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
			//ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			//SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			this_windowClosing(e);

			return;
		}
		super.processWindowEvent(e);
	}
}

class ElementsEditorWindowArranger extends WindowArranger
{
	public ElementsEditorWindowArranger(ElementsEditorMainFrame mainframe)
	{
		super(mainframe);
	}

	public void arrange()
	{
		ElementsEditorMainFrame f = (ElementsEditorMainFrame)mainframe;

		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

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

		f.editorFrame.setSize(3*w/5, h);
		f.ugoFrame.setSize(w/5, 2 * h / 5);
		f.elementsListFrame.setSize(w/5, 3 * h / 10);
		f.propsFrame.setSize(w/5, 3 * h / 10);
		f.treeFrame.setSize(w/5, h);

		f.editorFrame.setLocation(w/5, 0);
		f.ugoFrame.setLocation(4*w/5, 3 * h / 5);
		f.elementsListFrame.setLocation(4*w/5, 0);
		f.propsFrame.setLocation(4*w/5, 3*h/10);
		f.treeFrame.setLocation(0, 0);
	}
}

class ElementsEditorMainFrame_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	ElementsEditorMainFrame adaptee;

	ElementsEditorMainFrame_this_componentAdapter(ElementsEditorMainFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}

