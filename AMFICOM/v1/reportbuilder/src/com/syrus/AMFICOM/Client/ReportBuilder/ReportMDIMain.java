package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Component;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.text.SimpleDateFormat;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.SessionInterface;

import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;

import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;

import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.General.UI.ProgressBar;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplateImplementationPanel;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.General.Report.SelectReportsPanel;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;

import com.syrus.io.IniFile;

import com.syrus.AMFICOM.Client.Optimize.Report.OptimizeReportsTreeModel;
/**
 * <p>Description: Главное окно редактора шаблонов отчёта</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportMDIMain extends JFrame implements OperationListener
{
	private Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	static IniFile iniFile;
	static String iniFileName = "Template.properties";

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public boolean isTemplateSchemeMode = true;

	public ReportToolBar toolBar = new ReportToolBar(this);
	public InnerReportToolBar innerToolBar = new InnerReportToolBar(this);

	public JInternalFrame layoutScrollPane = null;

	public SelectReportsPanel selectReportsPanel = null;
	public ReportTemplatePanel layoutWOCPanel = null;
	public ReportTemplateImplementationPanel layoutWCPanel = null;

	public JInternalFrame additionalPanel = null;

	BorderLayout borderLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	ReportMenuBar menuBar = new ReportMenuBar();

	public ReportMDIMain(ApplicationContext aContext)
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

	public ReportMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		if (javax.swing.UIManager.getLookAndFeel().getClass().equals(
				javax.swing.plaf.metal.MetalLookAndFeel.class))
		{
			setUndecorated(true);
			getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		}

		desktopPane.setBackground(SystemColor.control.darker().darker());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH));

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(),(int)screenSize.getHeight() - 24);

		setLocation(0, 0);

		this.setTitle(LangModelReport.getString("label_rtbWindowTitle"));
		this.addComponentListener(new TemplateMDIMain_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.darkGray);
		desktopPane.setLayout(null);
		desktopPane.setBackground(Color.darkGray);


//		statusBar.add();
		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add(StatusBarModel.field_status);
		statusBar.add(StatusBarModel.field_server);
		statusBar.add(StatusBarModel.field_session);
		statusBar.add(StatusBarModel.field_user);
		statusBar.add(StatusBarModel.field_time);

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(toolBar, BorderLayout.NORTH);

		toolBar.setDisp(Environment.the_dispatcher);
		innerToolBar.setDisp(Environment.the_dispatcher);
		this.setJMenuBar(menuBar);

		toolBar.setTemplateToolBarState(false);
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

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
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
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

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
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

					setSessionOpened();
				}
			}
			else if(cce.SESSION_CLOSED)
			{
				// Closing of the existing windows
				//---------------------------------------------------------------
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);

					setSessionClosed();
				}
			}
			else if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText(StatusBarModel.field_status, LangModel.getString("statusReady"));
					statusBar.setText(StatusBarModel.field_server, aContext.getConnectionInterface().getServiceURL());
				}
			}
			else if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText(StatusBarModel.field_status, LangModel.getString("statusDisconnected"));
					statusBar.setText(StatusBarModel.field_server, LangModel.getString("statusNoConnection"));

					setConnectionClosed();

				}
			}
			else if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModelReport.getString("statusError"));
					statusBar.setText("server", LangModelReport.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			else if(cce.DOMAIN_SELECTED)
			{
				setSessionOpened();
			}
		}

		if (ae.getActionCommand().equals(SelectReportsPanel.ev_additionalPaneCreated))
		{
			try
			{
				if (this.additionalPanel != null)
				{
					desktopPane.remove(additionalPanel);
					additionalPanel.dispose();
				}
				additionalPanel = (JInternalFrame)ae.getSource();
				desktopPane.add(additionalPanel);
				additionalPanel.setSize(selectReportsPanel.getWidth(),460);

				selectReportsPanel.setSize(
								selectReportsPanel.getWidth(),
								desktopPane.getHeight() - additionalPanel.getHeight());

				additionalPanel.setLocation(
						selectReportsPanel.getX(),
						selectReportsPanel.getY() + selectReportsPanel.getHeight());

				additionalPanel.setVisible(true);
			}
			catch (java.lang.IllegalArgumentException eee)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"Can't create filer panel!",
						LangModelReport.getString("label_error"),
						JOptionPane.ERROR_MESSAGE);

				selectReportsPanel.setSize(
						selectReportsPanel.getWidth(),
						desktopPane.getHeight());

				desktopPane.remove(additionalPanel);
			}
		}

		if (ae.getActionCommand().equals(
			SelectReportsPanel.ev_closingAdditionalPanel))
		{
			if (selectReportsPanel != null)
				selectReportsPanel.setSize(
							  selectReportsPanel.getWidth(),
							  desktopPane.getHeight());
			desktopPane.remove(additionalPanel);
		}
	}

	public void setConnectionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}

	public void setSessionOpened()
	{
		aContext.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR,true));
//		statusBar.enableProgressBar(true);

		ReportBuilder.invokeAsynchronously(new Runnable() {
			public void run() {
				ApplicationModel aModel = aContext.getApplicationModel();

				aModel.disable("menuSessionNew");
				aModel.enable("menuSessionClose");
				aModel.enable("menuSessionOptions");
				aModel.enable("menuSessionChangePassword");
				aModel.enable("menuSessionDomain");

				DataSourceInterface dsi = aContext.getDataSourceInterface();
				new ConfigDataSourceImage(dsi).LoadNet();
				new SchemeDataSourceImage(dsi).LoadNetDirectory();
				new ConfigDataSourceImage(dsi).LoadISM();
				new SchemeDataSourceImage(dsi).LoadSchemeProto();
				new SchemeDataSourceImage(dsi).LoadSchemes();
				new MapDataSourceImage(dsi).LoadProtoElements();
				new MapDataSourceImage(dsi).LoadMaps();

				new SurveyDataSourceImage(dsi).GetAlarms();

/*				new SurveyDataSourceImage(dsi).LoadParameterTypes();
				new SurveyDataSourceImage(dsi).LoadTestTypes();
				new SurveyDataSourceImage(dsi).LoadAnalysisTypes();
				new SurveyDataSourceImage(dsi).LoadEvaluationTypes();
				new SurveyDataSourceImage(dsi).LoadModelingTypes();
*/
				toolBar.setTemplateToolBarState(true);
				setTemplate(null);

				aModel.fireModelChanged("");
//				aContext.getDispatcher().notify(new OperationEvent("",0,ReportBuilder.ev_stopProgressBar));
        aContext.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR,false));
//				statusBar.enableProgressBar(false);
			}
		},
		"Идёт загрузка. Пожалуйста, подождите.");
	}

	public void setTemplate(ReportTemplate rt)
	{
		desktopPane.removeAll();

		try
		{
			if (rt == null)
				layoutWOCPanel = new ReportTemplatePanel(this);
			else
				layoutWOCPanel = new ReportTemplatePanel(this,rt);

			int selectReportsPaneMinSize = 280;

			layoutWOCPanel.setSize(
					new Dimension(layoutWOCPanel.reportTemplate.size.width,2000));
			layoutWOCPanel.setPreferredSize(
					new Dimension(layoutWOCPanel.reportTemplate.size.width,2000));

			selectReportsPanel = new SelectReportsPanel(
					desktopPane,
					aContext,
					this,
					this.layoutWOCPanel.reportTemplate,
					new AvailableReportsTreeModel(aContext));

			layoutScrollPane = new JInternalFrame();
			layoutScrollPane.getContentPane().add(innerToolBar,BorderLayout.NORTH);

			JScrollPane scrPane = new JScrollPane(layoutWOCPanel);
			layoutScrollPane.getContentPane().add(scrPane, BorderLayout.CENTER);
			desktopPane.add(layoutScrollPane);
			layoutScrollPane.setTitle(LangModelReport.getString("label_templateScheme"));
			layoutScrollPane.setFrameIcon(
					new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));


			if ((layoutWOCPanel.reportTemplate.size.width + selectReportsPaneMinSize)
					< desktopPane.getWidth())
			{
				int srPanelWidth =
						desktopPane.getWidth() - layoutWOCPanel.reportTemplate.size.width - 33;
				selectReportsPanel.setSize(
						new Dimension(
						srPanelWidth,
						desktopPane.getHeight()));

				layoutScrollPane.setSize(
						new Dimension(
						layoutWOCPanel.getWidth() + 33,
						desktopPane.getHeight()));

				layoutScrollPane.setLocation(srPanelWidth, 0);
			}
			else
			{
				selectReportsPanel.setSize(
						new Dimension(
						selectReportsPaneMinSize,
						desktopPane.getHeight()));

				layoutScrollPane.setSize(
						new Dimension(
						desktopPane.getWidth() - selectReportsPaneMinSize,
						desktopPane.getHeight()));

				layoutScrollPane.setLocation(selectReportsPaneMinSize, 0);
			}

			layoutWOCPanel.setImagableRect();

			desktopPane.add(selectReportsPanel);
			selectReportsPanel.setLocation(0, 0);
			selectReportsPanel.setVisible(true);
			selectReportsPanel.repaint();

			layoutScrollPane.setVisible(true);
			layoutScrollPane.setClosable(true);
			layoutScrollPane.setResizable(true);
			layoutScrollPane.repaint();

			aContext.getDispatcher().register(this,SelectReportsPanel.ev_additionalPaneCreated);
			aContext.getDispatcher().register(this,com.syrus.AMFICOM.Client.General.Filter.SetRestrictionsWindow.ev_lsWindowCreated);
			aContext.getDispatcher().register(this,com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane.state_filterClosed);
			aContext.getDispatcher().register(this,SelectReportsPanel.ev_closingAdditionalPanel);
		}
		catch (Exception exc)
		{
		}
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.enable("menuSessionNew");
		aModel.disable("menuSessionClose");
		aModel.disable("menuSessionOptions");
		aModel.disable("menuSessionChangePassword");
		aModel.disable("menuSessionDomain");

		aModel.fireModelChanged("");
		toolBar.setTemplateToolBarState(false);
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

		Environment.the_dispatcher.unregister(this, "equipitem_changed");
		Environment.the_dispatcher.unregister(this, "portItem_changed");

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

class TemplateMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	ReportMDIMain adaptee;

	TemplateMDIMain_this_componentAdapter(ReportMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}
