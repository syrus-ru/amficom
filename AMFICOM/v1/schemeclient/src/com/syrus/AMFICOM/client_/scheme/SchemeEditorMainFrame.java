/*-
 * $Id: SchemeEditorMainFrame.java,v 1.7 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Schematics.UI.ElementsNavigatorPanel;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.*;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.ui.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;

public class SchemeEditorMainFrame extends JFrame implements PropertyChangeListener {
	public ApplicationContext aContext;
	static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss");
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

	SchemeViewerFrame editorFrame;
	GeneralPropertiesFrame generalFrame;
	CharacteristicPropertiesFrame characteristicFrame;
	AdditionalPropertiesFrame additionalFrame;
	JInternalFrame treeFrame;

	ArrayList graphs = new ArrayList();
	SchemeTabbedPane schemeTab;

	public SchemeEditorMainFrame(ApplicationContext aContext) {
		super();
		this.aContext = aContext;
		this.aContext.setDispatcher(internal_dispatcher);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Environment.addWindow(this);
		setContext(aContext);
	}

	public SchemeEditorMainFrame() {
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception {
		this.addComponentListener(new SchemeEditorMainFrame_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
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

		schemeTab = new SchemeTabbedPane(aContext);
		editorFrame = new SchemeViewerFrame(aContext, schemeTab);
		editorFrame.setClosable(false);
		editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
		desktopPane.add(editorFrame);

		SchemeToolBar schemeToolBar = new SchemeToolBar(schemeTab, additionalFrame, aContext);
		schemeToolBar.createToolBar();
		schemeTab.setToolBar(schemeToolBar);

		generalFrame = new GeneralPropertiesFrame("Title");
		desktopPane.add(generalFrame);
		new SchemeEventHandler(generalFrame, aContext);

		characteristicFrame = new CharacteristicPropertiesFrame("Title");
		desktopPane.add(characteristicFrame);
		new SchemeEventHandler(characteristicFrame, aContext);
		
		additionalFrame = new AdditionalPropertiesFrame("Title");
		desktopPane.add(additionalFrame);
		new SchemeEventHandler(additionalFrame, aContext);

		treeFrame = new JInternalFrame();
		treeFrame.setIconifiable(true);
		treeFrame.setClosable(true);
		treeFrame.setResizable(true);
		treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		treeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
		treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
		treeFrame.getContentPane().setLayout(new BorderLayout());
		desktopPane.add(treeFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width,
				screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);
	}

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
		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.addPropertyChangeListener(CreatePathEvent.TYPE, this);
		internal_dispatcher.addPropertyChangeListener(SchemeEvent.TYPE, this);
		// internal_dispatcher.register(this, CatalogNavigateEvent.type);
		internal_dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		Environment.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		setContext(aContext);
		setDefaultModel(aModel);

		aModel.setCommand("menuSessionNew", new OpenSessionCommand(Environment
				.getDispatcher()));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment
				.getDispatcher()));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));

		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(
				Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword",
						new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment
				.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		// SchemePanel epanel = (SchemePanel)schemeTab.getPanel();

		aModel.setCommand("menuSchemeNew", new SchemeNewCommand(aContext));
		aModel.setCommand("menuSchemeLoad", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(aContext, schemeTab));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(aContext, schemeTab));
		// aModel.setCommand("menuInsertToCatalog", new
		// InsertToCatalogCommand(aContext, epanel.getGraph()));
		// aModel.setCommand("menuInsertToCatalog", new
		// InsertToCatalogCommand(aContext,
		// schemeTab));
//		aModel.setCommand("menuSchemeExport", new SchemeToFileCommand(Environment
//				.getDispatcher(), aContext));
		// aModel.setCommand("menuSchemeImport", new
		// SchemeFromFileCommand(Environment.getDispatcher(), aContext));

		// TODO разобраться с созданием пути
		/*
		aModel.setCommand("menuPathNew", new PathNewCommand(aContext, schemeTab));
		aModel.setCommand("menuPathEdit", new PathEditCommand(aContext, schemeTab));
		aModel.setCommand("menuPathAddStart", new PathSetStartCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathAddEnd", new PathSetEndCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathAddLink", new PathAddLinkCommand(aContext,
				schemeTab));

		aModel.setCommand("menuPathRemoveLink", new PathRemoveLinkCommand(aContext,
				scheme_graph));
		aModel.setCommand("menuPathAutoCreate", new PathAutoCreateCommand(aContext,
				schemeTab.getPanel()));

		aModel.setCommand("menuPathSave", new PathSaveCommand(aContext, schemeTab));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathDelete", new PathDeleteCommand(aContext,
				schemeTab));*/

//		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
//		for (Iterator it = graphs.iterator(); it.hasNext();)
//			rc.setParameter(CreateSchemeReportCommand.PANEL, it.next());
//		rc.setParameter(CreateSchemeReportCommand.TYPE, ReportTemplate.rtt_Scheme);
//		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(
				new SchemeEditorWindowArranger(this)));
		aModel.setCommand("menuWindowTree", new ShowFrameCommand(desktopPane,
				treeFrame));
		aModel.setCommand("menuWindowScheme", new ShowFrameCommand(desktopPane,
				editorFrame));
		aModel.setCommand("menuWindowUgo", new ShowFrameCommand(desktopPane,
				additionalFrame));
		aModel.setCommand("menuWindowProps", new ShowFrameCommand(desktopPane,
				generalFrame));
		aModel.setCommand("menuWindowList", new ShowFrameCommand(desktopPane,
				characteristicFrame));

		aModel.fireModelChanged("");

		if (ClientSessionEnvironment.getInstance().sessionEstablished()) {
			internal_dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_OPENED_EVENT), true);
		}
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext.getApplicationModel() == null)
			aContext.setApplicationModel(ApplicationModel.getInstance());
		setModel(aContext.getApplicationModel());
	}

	public ApplicationContext getContext() {
		return aContext;
	}

	public void setModel(ApplicationModel aModel) {
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel() {
		return aContext.getApplicationModel();
	}

	void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuScheme", true);
		aModel.setEnabled("menuPath", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		// aModel.setVisible("menuSchemeExport", false);
		// aModel.setVisible("menuSchemeImport", false);

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher() {
		return internal_dispatcher;
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals("contextchange")) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			
			System.out.println("perform context change \""
					+ "\" at " + this.getTitle());
			/*
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					setSessionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", sdf.format(new Date(aContext
							.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", ConnectionInterface.getInstance()
							.getServerName());
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel
							.getString("statusConnectionError"));

					statusBar
							.setText("status", LangModel.getString("statusDisconnected"));
					statusBar
							.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();
				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel
							.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if (cce.DOMAIN_SELECTED) {
				setDomainSelected();
			}
			*/
		} else if (ae.getPropertyName().equals(CreatePathEvent.TYPE)) {
			CreatePathEvent cpe = (CreatePathEvent) ae;
			if (cpe.CREATE_PATH || cpe.EDIT_PATH) {
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", true);
				aModel.setEnabled("menuPathAddStart", true);
				aModel.setEnabled("menuPathAddEnd", true);
				aModel.setEnabled("menuPathAddLink", true);
				aModel.setEnabled("menuPathCancel", true);
				aModel.setEnabled("menuPathAutoCreate", true);
				aModel.getCommand("menuPathAutoCreate").setParameter("panel",
						schemeTab.getCurrentPanel());
				aModel.fireModelChanged("");
			}
			if (cpe.CANCEL_PATH_CREATION || cpe.SAVE_PATH) {
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
			if (cpe.PE_SELECTED) {
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", true);
					aModel.fireModelChanged("");
				}
			} else {
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", false);
					aModel.fireModelChanged("");
				}
			}
		} else if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			ObjectSelectedEvent sne = (ObjectSelectedEvent) ae;
			if (sne.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathDelete", true);
				aModel.fireModelChanged("");
			} else {
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathDelete", false);
				aModel.fireModelChanged("");
			}
		}
		/*
		 * else if (ae.getActionCommand().equals(CatalogNavigateEvent.type)) {
		 * CatalogNavigateEvent cne = (CatalogNavigateEvent)ae; if
		 * (cne.CATALOG_PATH_SELECTED) { ApplicationModel aModel =
		 * aContext.getApplicationModel(); aModel.setEnabled("menuPathEdit", true);
		 * aModel.setEnabled("menuPathDelete", true); aModel.fireModelChanged(""); }
		 * if (cne.CATALOG_PATH_DESELECTED) { ApplicationModel aModel =
		 * aContext.getApplicationModel(); aModel.setEnabled("menuPathEdit", false);
		 * aModel.setEnabled("menuPathDelete", false); aModel.fireModelChanged(""); } }
		 */
		else if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent se = (SchemeEvent)ae;
			if (se.isType(SchemeEvent.OPEN_SCHEME)) {
				Scheme scheme = (Scheme)se.getObject();
				schemeTab.openScheme(scheme);
			} 
			else if (se.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
				Identifier se_id = (Identifier) ae.getSource();
				SchemeElement sel = (SchemeElement)se.getObject();
				schemeTab.openSchemeElement(sel);
			}
		}
	}

	public void setConnectionOpened() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setConnectionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setSessionOpened() {
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
		internal_dispatcher.firePropertyChange(new ContextChangeEvent(this,
				ContextChangeEvent.DOMAIN_SELECTED_EVENT));

		editorFrame.setVisible(true);
		additionalFrame.setVisible(true);
		generalFrame.setVisible(true);
		characteristicFrame.setVisible(true);
		treeFrame.setVisible(true);

		SchemeTreeModel model = new SchemeTreeModel(aContext);

		ElementsNavigatorPanel utp = new ElementsNavigatorPanel(aContext,
				internal_dispatcher, model);
		// UniTreePanel utp = new UniTreePanel(internal_dispatcher, aContext,
		// model);
		// utp.setBorder(BorderFactory.createLoweredBevelBorder());
		// utp.getTree().setRootVisible(false);
		treeFrame.getContentPane().removeAll();
		treeFrame.getContentPane().add(utp, BorderLayout.CENTER);
		treeFrame.updateUI();
	}

	public void setDomainSelected() {
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
			Identifier domain_id = LoginManager.getDomainId();
			Domain domain = (Domain) AdministrationStorableObjectPool
					.getStorableObject(domain_id, true);
			statusBar.setText(StatusBarModel.FIELD_DOMAIN, domain.getName());
		} catch (ApplicationException ex) {
		}
	}

	public void setSessionClosed() {
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
		additionalFrame.setVisible(false);
		generalFrame.setVisible(false);
		characteristicFrame.setVisible(false);
		treeFrame.setVisible(false);

		statusBar.setText("domain", "Домен не установлен");
	}

	void this_componentShown(ComponentEvent e) {
		init_module();

		desktopPane.setPreferredSize(desktopPane.getSize());
		new SchemeEditorWindowArranger(this).arrange();
	}

	void this_windowClosing(WindowEvent e) {
		UgoPanel p = schemeTab.getCurrentPanel();
		while (p != null) {
			/*
			 * if (p[i].getGraph().getSchemeElement() != null &&
			 * p[i].getGraph().isGraphChanged()) { schemeTab.selectPanel(p[i]); int
			 * res = JOptionPane.showConfirmDialog( Environment.getActiveWindow(),
			 * "Элемент \"" + p[i].getGraph().getSchemeElement().getName() + "\" был
			 * изменен. Сохранить схему перед закрытием?", "Подтверждение",
			 * JOptionPane.YES_NO_CANCEL_OPTION); if (res == JOptionPane.OK_OPTION) {
			 * SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab,
			 * ugoPane); ssc.execute(); if (ssc.ret_code == SchemeSaveCommand.CANCEL)
			 * return; } else if (res == JOptionPane.CANCEL_OPTION) return; }
			 */
			if (!schemeTab.removePanel(p))
				return;
			p = schemeTab.getCurrentPanel();
		}

		/*
		 * p = schemeTab.getAllPanels(); for (int i = 0; i < p.length; i++) {
		 * schemeTab.selectPanel(p[i]);
		 * 
		 * if (p[i].getGraph().getScheme() != null &&
		 * p[i].getGraph().isGraphChanged()) { schemeTab.selectPanel(p[i]); int res =
		 * JOptionPane.showConfirmDialog( Environment.getActiveWindow(), "Схема \"" +
		 * p[i].getGraph().getScheme().getName() + "\" была изменена. Сохранить
		 * схему перед закрытием?", "Подтверждение",
		 * JOptionPane.YES_NO_CANCEL_OPTION); if (res == JOptionPane.OK_OPTION) {
		 * SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab,
		 * ugoPane); ssc.execute(); if (ssc.ret_code == SchemeSaveCommand.CANCEL)
		 * return; } else if (res == JOptionPane.CANCEL_OPTION) return; } if
		 * (!schemeTab.removePanel(p[i])) return; }
		 */
		internal_dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, this);
		Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this_windowClosing(e);
			return;
		}
		super.processWindowEvent(e);
	}
}

class SchemeEditorWindowArranger extends WindowArranger {
	public SchemeEditorWindowArranger(SchemeEditorMainFrame mainframe) {
		super(mainframe);
	}

	public void arrange() {
		SchemeEditorMainFrame f = (SchemeEditorMainFrame) mainframe;
		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

		// f.editorFrame.setVisible(true);
		// f.ugoFrame.setVisible(true);
		// f.elementsListFrame.setVisible(true);
		// f.propsFrame.setVisible(true);
		// f.treeFrame.setVisible(true);

		normalize(f.editorFrame);
		normalize(f.additionalFrame);
		normalize(f.generalFrame);
		normalize(f.characteristicFrame);
		normalize(f.treeFrame);

		f.editorFrame.setSize(11 * w / 20, h);
		f.additionalFrame.setSize(w / 4, 3 * h / 10);
		f.generalFrame.setSize(w / 4, 4 * h / 10);
		f.characteristicFrame.setSize(w / 4, 3 * h / 10);
		f.treeFrame.setSize(w / 5, h);

		f.editorFrame.setLocation(w / 5, 0);
		f.additionalFrame.setLocation(3 * w / 4, 7 * h / 10);
		f.generalFrame.setLocation(3 * w / 4, 0);
		f.characteristicFrame.setLocation(3 * w / 4, 4 * h / 10);
		f.treeFrame.setLocation(0, 0);
	}
}

class SchemeEditorMainFrame_this_componentAdapter extends
		java.awt.event.ComponentAdapter {
	SchemeEditorMainFrame adaptee;

	SchemeEditorMainFrame_this_componentAdapter(SchemeEditorMainFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e) {
		adaptee.this_componentShown(e);
	}
}
