// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.io.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;//���� ������� ��������
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;

import java.awt.geom.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

public class OptimizeMDIMain extends JFrame implements OperationListener
{
  private Dispatcher internal_dispatcher = new Dispatcher();
  public ApplicationContext aContext = new ApplicationContext();

  static IniFile iniFile;
  static String iniFileName = "Optimize.properties";

  static SimpleDateFormat sdf =	new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

  BorderLayout borderLayout = new BorderLayout();

  JPanel mainPanel = new JPanel();
  JPanel toolBarPanel = new JPanel();
  public JScrollPane scrollPane = new JScrollPane();
  JViewport viewport = new JViewport();
  public JDesktopPane desktopPane = new JDesktopPane();
  JPanel statusBarPanel = new JPanel();
  StatusBarModel statusBar = new StatusBarModel(0);
  OptimizeMenuBar menuBar = new OptimizeMenuBar();

  public MapContext mapContext;
  //public ISMMapContext ismMapContext;//  ismMapContext ������ �� ������������, �� � ���� � ��� ������-�� ��� ��� ������
  public String domain_id = null;

  // <Vit>
  public Scheme scheme;
  public OpticalOptimizerContext optimizerContext;
  public KISselectionFrame kisSelectFrame;           // ������ (����), ��������� ���������� � ��������������� � ����� ������������
  public IterationsHistoryFrame iterHistFrame;       // ���� ������� ���� �����������
  public OpticalOptimizationParamsFrame paramsFrame; // ���� ������� ���������� �����������
  public ViewSolutionFrame solutionFrame;            // ���� ��������� ����� �������� ������ �� �������
  public NodesOptimizePropertiesFrame nodesModeFrame;// ���� ������� ������� ����� ( fixed , active )
  public RibsOptimizePropertiesFrame ribsModeFrame;  // ���� ������� ������� ���� ( active )
  public ViewSchemeFrame schemeFrame;                // ���� ����������� �����
  public MapMainFrame mapFrame;                      // ���� ����������� �����
  private int opened_scheme_num = 0; // ���������� �������� ����
  public boolean map_is_opened = false; // ��������� �������� �����
  public OptimizeMainToolBar mainToolBar; // �������� � ��������, ������������ ������� ����
  // </Vit>

  private CreateOptimizeReportCommand corCommand = null;
    //-------------------------------------------------------------------------------------------------------------
    public OptimizeMDIMain(ApplicationContext aContext)
    { super();
      try
      { jbInit();
      }
      catch (Exception e)	{e.printStackTrace();}

      Environment.addWindow(this);
      setContext(aContext);
    }
    //-------------------------------------------------------------------------------------------------------------
    public OptimizeMDIMain()
    { this(new ApplicationContext());
    }
    //-------------------------------------------------------------------------------------------------------------
    private void jbInit() throws Exception
    { this.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/design.gif"));
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      setContentPane(this.mainPanel);
      //������������� ���� �� ���� �����
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      setSize(dim.width, dim.height - 24);// 24 - ������ �������� �����
      setLocation(0,0);
      this.setTitle(LangModelOptimize.getString("AppTitle"));
      this.addComponentListener(new OptimizeMDIMain_this_componentAdapter(this));
      this.addWindowListener(new java.awt.event.WindowAdapter()
      { public void windowClosing(WindowEvent e)
        { this_windowClosing(e);
        }
      });
      mainPanel.setLayout(new BorderLayout());
      mainPanel.setBackground(Color.darkGray);
      desktopPane.setLayout(null);
      desktopPane.setBackground(SystemColor.control.darker().darker());//desktopPane.setBackground(Color.darkGray);

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
      viewport.setView(desktopPane);
      scrollPane.setViewport(viewport);
      scrollPane.setAutoscrolls(true);

      mainPanel.add(toolBarPanel, BorderLayout.NORTH);
      mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
      mainPanel.add(scrollPane, BorderLayout.CENTER);
      //scrollPane.add(desktopPane);

      this.setJMenuBar(menuBar);

      mainToolBar = new OptimizeMainToolBar();// ������������ � init_module()
      mainPanel.add(mainToolBar, BorderLayout.NORTH);
    }
    //-------------------------------------------------------------------------------------------------------------
    public void SetDefaults(){}
    //-------------------------------------------------------------------------------------------------------------
    public void init_module()
    { ApplicationModel aModel = aContext.getApplicationModel();

      statusBar.distribute();
      statusBar.setWidth("status", 300);
      statusBar.setWidth("server", 250);
      statusBar.setWidth("session", 200);
      statusBar.setWidth("user", 100);
      statusBar.setWidth("domain", 150);
      statusBar.setWidth("time", 50);
      statusBar.setText("status", LangModel.String("statusReady"));
      statusBar.setText("status", LangModel.String("statusReady"));
      statusBar.setText("server", LangModel.String("statusNoConnection"));
      statusBar.setText("session", LangModel.String("statusNoSession"));
      statusBar.setText("user", LangModel.String("statusNoUser"));
      statusBar.setText("domain", LangModel.String("statusNoDomain"));
      statusBar.setText("time", " ");
      statusBar.organize();
      // load values from properties file
      try
      { iniFile = new IniFile(iniFileName);
        System.out.println("read ini file " + iniFileName);
      }
      catch(java.io.IOException e)
      { System.out.println("Error opening " + iniFileName + " - setting defaults");
        SetDefaults();
      }
      aContext.setDispatcher(internal_dispatcher);
      statusBar.setDispatcher(Environment.the_dispatcher);
      statusBar.setDispatcher(internal_dispatcher);

      internal_dispatcher.register(this, "mapopened");
      internal_dispatcher.register(this, "map_close");// ��� ������ mapcloseevent
      internal_dispatcher.register(this, "mapcloseevent");
      internal_dispatcher.register(this, "scheme_is_opened");
      internal_dispatcher.register(this, "showallevent");
      internal_dispatcher.register(this, "close_all");

      internal_dispatcher.register(this, "startevent");
      internal_dispatcher.register(this, "stopevent");
      internal_dispatcher.register(this, "solution_updated_event");
      internal_dispatcher.register(this, "solution_overwrite_event");
      internal_dispatcher.register(this, "scheme_updated_event");

      internal_dispatcher.register(this, "print_scheme");

      internal_dispatcher.register(this, "contextchange");
      internal_dispatcher.register(this, "mapjframeshownevent");

      internal_dispatcher.register(this, "addschemeevent");
      internal_dispatcher.register(this, "addschemeelementevent");

      Environment.the_dispatcher.register(this, "contextchange");
      Environment.the_dispatcher.register(this, "mapaddschemeevent");
      Environment.the_dispatcher.register(this, "mapaddschemeelementevent");

      aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
      aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
      aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
      aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
      aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
      aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
      aModel.setCommand("menuExit", new ExitCommand(this));

      // ������� �������������� ����� ��� �����������
      aModel.setCommand("menuMapOpen", new MapOptOpenCommand(internal_dispatcher, aContext, this));
      // ������� ���������� ����� ���� ( ��������, � ����� �� ����� ���������� ������� ����������������� ����������� )
      aModel.setCommand("menuSchemeOpen", new SchemeOpenCommand(internal_dispatcher, aContext, this) );
      // ��������� ����� �� ���� �����������, ������� �� ��� �������� ( �������� ��������������� ���� � �������� ����� )
      aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(internal_dispatcher, aContext, this));
      //aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand_2(internal_dispatcher, aContext, this));
      aModel.setCommand("menuSchemeSaveAs", new SaveSolutionAsCommand(internal_dispatcher, aContext, this)); //  �� �� ���� ���������� � �-� ?
      // ��������� ����� ������� � ���������� ������������ ���� �� ����� ����� ������������ ������ ( ��� ������ ������� )
      aModel.setCommand("menuLoadSm", new LoadSolutionCommand(internal_dispatcher, aContext, this)); // �� �� ���� ���������� � �-� ?
      //������� ����� �� ���� �����, ������� � ��� ����� ����������
      aModel.setCommand("menuClearScheme", new SchemePathsClearCommand(internal_dispatcher, aContext, this));
      
      aModel.setCommand("menuViewMap", new ViewMapCommand(internal_dispatcher, desktopPane, aContext, new MapOptimizeApplicationModelFactory(), this));
      aModel.setCommand("menuViewScheme", new ViewSchemeCommand(internal_dispatcher, desktopPane, aContext, new MapOptimizeApplicationModelFactory(), this));
      //�������� ������� ����� � �����
      aModel.setCommand("menuViewMapElProperties", new ViewOptMapPropertiesCommand(desktopPane, aContext));
      aModel.setCommand("menuViewSchElProperties", new ViewSchElPropFrameCommand(internal_dispatcher, aContext, this));
      aModel.setCommand("menuViewGraph", new ViewGraphCommand(internal_dispatcher, desktopPane, aContext, optimizerContext, this));
      aModel.setCommand("menuViewSolution", new ViewSolutionCommand(internal_dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuViewKIS", new ViewKISCommand(internal_dispatcher, desktopPane, aContext, this));
      
      aModel.setCommand("menuViewParams", new ViewParamCommand(internal_dispatcher, desktopPane, aContext, optimizerContext, this));
      aModel.setCommand("menuViewMode", new ViewModeCommand(internal_dispatcher, desktopPane, aContext, this));
      // �������� ��� �������� ���� (�� ������ ������ �������� event "showallevent")
      aModel.setCommand("menuViewShowall", new ViewShowallCommand(internal_dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeStart", new OptimizeStartCommand(internal_dispatcher, this) );
      aModel.setCommand("menuOptimizeStop", new OptimizeStopCommand(internal_dispatcher, optimizerContext, this) );
      aModel.setCommand("menuOptimizeCriteriaPriceLoad", new LoadPricelistCommand(internal_dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceSave", new SavePricelistCommand(internal_dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceSaveas", new SaveasPricelistCommand(internal_dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceClose", new ClosePricelistCommand(internal_dispatcher, desktopPane, aContext, this));
      // ��������� ������ ����������� ( ���������\������������ )
      aModel.setCommand("menuOptimizeModeUnidirect", new SetOptimizeModeCommand(  0, internal_dispatcher, desktopPane, aContext, this ));
      aModel.setCommand("menuOptimizeModeBidirect", new SetOptimizeModeCommand(  1, internal_dispatcher, desktopPane, aContext, this ));
      
      corCommand = new CreateOptimizeReportCommand(aContext);
      corCommand.setMainWindow(this);
      aModel.setCommand("menuReportCreate", corCommand);
      
      aModel.setCommand("menuOptimizeModeBidirect", new SetOptimizeModeCommand(  1, internal_dispatcher, desktopPane, aContext, this ));

      aModel.add("menuHelpAbout", new HelpAboutCommand(this));//, new OptimizeMDIMain_AboutBoxPanel1()));

      aModel.setAllItemsEnabled(false);
      aModel.enable("menuSession");
      aModel.enable("menuSessionNew");
      aModel.enable("menuSessionConnection");
      aModel.enable("menuExit");
      aModel.enable("menuView");
      aModel.enable("menuHelp");
      aModel.enable("menuHelpAbout");

      aModel.enable("menuScheme"); // ���� "�����"
      // aModel.disable("menuSchemeOpen");
      // aModel.disable("menuSchemeSave");
      // aModel.disable("menuSchemeSaveAs");
      // aModel.disable("menuSchemeClose");
      aModel.enable("menuView"); // ���� "���"
      //  aModel.disable("menuViewMap");
      //  aModel.disable("menuViewScheme");
      //  aModel.disable("menumenuViewMapElProperties");
      //  aModel.disable("menumenuViewSchElProperties");
      aModel.enable("menuOptimize"); // ���� "�����������"
      //  aModel.disable("menuOptimizeStart");
      //  aModel.disable("menuOptimizeStop");
      //  aModel.disable("menuViewSolution");
      //  aModel.disable("menuViewKIS");
      //  aModel.disable("menuViewMode");
      //  aModel.disable("menuViewParams");
      //  aModel.disable("menuViewShowall");
      //  aModel.disable("menuViewMap");
      //  aModel.disable("menuViewMapElProperties");
      //  aModel.disable("menuViewSchElProperties");
      //  aModel.enable("menuViewScheme");
      //  aModel.enable("menuViewParams");
      //  aModel.enable("menuViewOptions");
      //  aModel.enable("menuViewMode");
      //  aModel.enable("menuViewSolution");
      //  aModel.enable("menuViewGraph");
      //  aModel.enable("menuViewShowall");

      //  aModel.enable("menuMapOpen");
      //  aModel.enable("menuSchemeOpen");
      //  aModel.enable("menuSchemeSave");
      //  aModel.enable("menuSchemeClose");
      aModel.enable("menuReport");

      aModel.fireModelChanged("");

      if(ConnectionInterface.getActiveConnection() != null)
      { aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
        if(aContext.getConnectionInterface().isConnected())
          internal_dispatcher.notify(new ContextChangeEvent( aContext.getConnectionInterface(),
                                         ContextChangeEvent.CONNECTION_OPENED_EVENT));
      }
      else
      { aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
        ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
      }
      if(SessionInterface.getActiveSession() != null)
      { aContext.setSessionInterface(SessionInterface.getActiveSession());
        aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
        if(aContext.getSessionInterface().isOpened())
        {  internal_dispatcher.notify( new ContextChangeEvent(
                                       aContext.getSessionInterface(), ContextChangeEvent.SESSION_OPENED_EVENT));
        }
      }
      else
      { aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
        SessionInterface.setActiveSession(aContext.getSessionInterface());
      }
      // init_module ����� �������� ������ ����� ����, ��� ����� ���������� aContext � ��������
      mainToolBar.init_module(aContext);
    }
    //--------------------------------------------------------------------------------------------------------------
    public void setContext(ApplicationContext aContext)
    {	this.aContext = aContext;
    if(aContext.getApplicationModel() == null)
      aContext.setApplicationModel(new ApplicationModel());
    setModel(aContext.getApplicationModel());
    }
    //-------------------------------------------------------------------------------------------------------------
    public ApplicationContext getContext()
    {	return aContext;
    }
    //--------------------------------------------------------------------------------------------------------------
    // ��������� � ������������ � ������� ������ �� 15 ������ 2003 ����
    public void dispose()
    { internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
      internal_dispatcher.unregister(this, "contextchange");
      Environment.the_dispatcher.unregister(this, "contextchange");
      super.dispose();
    }
    //--------------------------------------------------------------------------------------------------------------
    public void setModel(ApplicationModel aModel)
    { aModel.addListener(menuBar);
      menuBar.setModel(aModel);
      aModel.fireModelChanged("");
    }
    //--------------------------------------------------------------------------------------------------------------
    public ApplicationModel getModel()
    {	return aContext.getApplicationModel();
    }
    //--------------------------------------------------------------------------------------------------------------
    public void operationPerformed(OperationEvent ae)
    {	ApplicationModel aModel = aContext.getApplicationModel();
    //---------------------
 	if(ae.getActionCommand().equals("mapopened"))// �������: ������� ����� �����
 	{	 DataSourceInterface dataSource = aContext.getDataSourceInterface();
     	 //  ��������� �������� ������������� ����������� ��� �������� �����
         // ��� ��� ��� �������� ����� ������������� ����������� � �����

	       aModel.enable("menuScheme");
	       aModel.enable("menuMapOpen");
	       aModel.enable("menuSchemeOpen");
	       aModel.enable("menuSchemeSave");
	       aModel.enable("menuSchemeSaveAs");
	       aModel.enable("menuLoadSm");
	       aModel.enable("menuClearScheme");
	       aModel.enable("menuSchemeClose");
	       aModel.enable("menuView");
	       aModel.enable("menuViewMap");
	       aModel.enable("menuViewScheme");
	       aModel.enable("menuViewMapElProperties");
	       aModel.enable("menuViewSchElProperties");
	       aModel.enable("menuViewKIS");
	       aModel.enable("menuViewGraph");
	       aModel.enable("menuViewSolution");
	       aModel.enable("menuViewParams");
	       aModel.enable("menuViewMode");
	       aModel.enable("menuViewShowall");
	       aModel.enable("menuOptimize");
	       aModel.enable("menuOptimizeCriteria");
	       aModel.enable("menuOptimizeMode");
	       aModel.enable("menuOptimizeModeUnidirect");
	       aModel.enable("menuOptimizeModeBidirect");
	       aModel.enable("menuOptimizeCriteriaPrice");
	       aModel.enable("menuOptimizeCriteriaPriceLoad");
	       aModel.enable("menuOptimizeCriteriaPriceSave");
	       aModel.enable("menuOptimizeCriteriaPriceSaveas");
	       aModel.enable("menuOptimizeCriteriaPriceClose");
	       aModel.enable("menuOptimizeStart");
	       aModel.disable("menuOptimizeStop");
	       aModel.fireModelChanged("");
	
	       opened_scheme_num++; // ������� �������� ����
	       map_is_opened = true;
    }
    //--------------------------------------
    else if(ae.getActionCommand().equals("scheme_is_opened"))// �������: ������� ����� �����
    {	 DataSourceInterface dataSource = aContext.getDataSourceInterface();
       //��� ������ ����������� ����� ����� , ����� ������������ ��� �������� ��� ���������� �������� �����������
       optimizerContext = new OpticalOptimizerContext(this);
       // ��� �������� ����� ���� ������������� optimizerContext � ������ ���� ������, ������� � ��� ���������;
       aModel.getCommand("menuOptimizeStart").setParameter("optimizerContext", optimizerContext);
       aModel.getCommand("menuOptimizeStop").setParameter("optimizerContext", optimizerContext);
       aModel.getCommand("menuViewGraph").setParameter("optimizerContext", optimizerContext);
       aModel.getCommand("menuViewSolution").setParameter("optimizerContext", optimizerContext);
       aModel.getCommand("menuViewParams").setParameter("optimizerContext", optimizerContext);
       aModel.getCommand("menuViewShowall").setParameter("optimizerContext", optimizerContext);
       //����� ���� �� ���������� ��� �������� ����� �����: �������� ������� ��������������� ��� ������� ����
       // ht2 ������ �������������� ��������� � ������ ����, ������ ht1. ������ ����� ������ ����� ��� ���� ������� ����
       Hashtable ht2 = (Hashtable)Pool.getHash(EquipmentType.typ);
       // ���� ������� ���� �� RTU � ����������� (��� �������� ���� ����� ����������)
       kisSelectFrame = new KISselectionFrame( optimizerContext, ht2, this);
       this.desktopPane.add(kisSelectFrame, null);
       kisSelectFrame.setVisible(false);// ���� ��� �� ������ ���� ����� (�� ������ ������ ��������������)
       //������ ���� ��� �����
       schemeFrame = new ViewSchemeFrame(this);
       desktopPane.add(schemeFrame);
       schemeFrame.setVisible(false);// �� ���������� ������� "�������� �����" ���� ��������
       // ���� ����� ��� �������� ����, �� ���������� ��
       optimizerContext.original_paths = new ArrayList(); // ������ � ����� ������, ���� �� ����� ������, ��� ����� ��������� ����, ��� � ����� ���������� �� ���� �����
       optimizerContext.originally_lconnected_nodes = new ArrayList();
       if(scheme.paths.size() != 0)
       { System.out.println("event scheme_is_opened: Loaded scheme already contains scheme path(s).");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "����� ��� �������� ���� ������������.", "��������!", javax.swing.JOptionPane.WARNING_MESSAGE );
         SchemePath sp;
         System.out.println("Loading initial paths and links ...");
         for(Enumeration paths = scheme.paths.elements(); paths.hasMoreElements();)
         { sp = (SchemePath)paths.nextElement();
           // ���������� ��������� ���� ������������
           optimizerContext.original_paths.add(sp);
           // ����������� originally_lconnected_nodes (��������� �� ����, ���� ����� � �������� ����������� ��� ����)
           for( Enumeration pes = sp.links.elements(); pes.hasMoreElements(); )
           { PathElement pe = (PathElement)pes.nextElement();
             if(!pe.is_cable)//���� ��� �� ������, � ����
             { SchemeLink sl = (SchemeLink)Pool.get( SchemeLink.typ, pe.link_id);
               String se1_id = ((SchemeElement)scheme.getSchemeElementByPort(sl.source_port_id)).id;
               String se2_id = ((SchemeElement)scheme.getSchemeElementByPort(sl.target_port_id)).id;
               String sl_id = sl.id;
               // ��������� ������ � ���, ��� ��� �������� ��� ��������� ������
               optimizerContext.originally_lconnected_nodes.add(se1_id);
               optimizerContext.originally_lconnected_nodes.add(se2_id);
               optimizerContext.originally_lconnected_nodes.add(sl_id);
             }
           }
         }
         System.out.println("Loading initial paths and links - done");
       }
       kisSelectFrame.setVisible(true); // ����� ���������� ������ �������������� ����� ������� �������

       aModel.enable("menuScheme");
       aModel.enable("menuMapOpen");
       aModel.enable("menuSchemeOpen");
       aModel.enable("menuSchemeSave");
       aModel.enable("menuSchemeSaveAs");
       aModel.enable("menuLoadSm");
       aModel.enable("menuClearScheme");
       aModel.enable("menuSchemeClose");
       aModel.enable("menuView");
       aModel.disable("menuViewMap");
       aModel.enable("menuViewScheme");
       aModel.disable("menuViewMapElProperties");
       aModel.enable("menuViewSchElProperties");
       aModel.enable("menuViewKIS");
       aModel.enable("menuViewGraph");
       aModel.enable("menuViewSolution");
       aModel.enable("menuViewParams");
       aModel.enable("menuViewMode");
       aModel.enable("menuViewShowall");
       aModel.enable("menuOptimize");
       aModel.enable("menuOptimizeCriteria");
       aModel.enable("menuOptimizeCriteriaPrice");
       aModel.enable("menuOptimizeCriteriaPriceLoad");
       aModel.enable("menuOptimizeCriteriaPriceSave");
       aModel.enable("menuOptimizeCriteriaPriceSaveas");
       aModel.enable("menuOptimizeCriteriaPriceClose");
       aModel.enable("menuOptimizeMode");
       aModel.setSelected("menuOptimizeModeUnidirect", true);// �� ��������� ����� "�������������" ������������
       aModel.setSelected("menuOptimizeModeBidirect", false);
       aModel.enable("menuOptimizeModeUnidirect");
       aModel.enable("menuOptimizeModeBidirect");
       aModel.enable("menuOptimizeStart");
       aModel.disable("menuOptimizeStop");
       aModel.enable("menuReport");
       aModel.enable("menuReportCreate");
       aModel.fireModelChanged("");

       opened_scheme_num++; // ������� �������� ����
     }
    //--------------------------------------
    else if(ae.getActionCommand().equals("mapjframeshownevent"))// ��������� � ���, ��� ��������� ���� ���� �� ����� jframe
    {	 JInternalFrame frame = (JInternalFrame )ae.getSource();
       System.out.println("Map j frame shown " + frame.getTitle());
       //��������  ���� ������� ����, ����� ��� �� ������������� ������ (�������� ������, ��� ����� ����������)
       if(kisSelectFrame != null)// ���� ���� ���� �������, �� ����������� ��� (���� �� ����������� , ���� �� ��������� ��� ���������������� ����� ����)
       { kisSelectFrame.place();
       }
    }
    //--------------------------------------
    else if(ae.getActionCommand().equals("mapframeshownevent"))//��������� � ���, ��� ��������� ���� ����� frame (jframe != frame)
    { 	 System.out.println("Map frame shown ");
    }
    //--------------------------------------
    else if(ae.getActionCommand().equals("mapjframeopenevent"))//��������� � ���, ��� ��������� ���� ������ ������ �� ����� ���������� �������
    {  System.out.println("Topology solution opened");
    }
    //--------------------------------------
    else if(ae.getActionCommand().equals("map_close") || ae.getActionCommand().equals("mapcloseevent"))
    { System.out.println("Map is closing... ");
      for(int i = 0; i<desktopPane.getComponents().length; i++)
      { Component comp = desktopPane.getComponent(i);
        if(comp instanceof MapMainFrame)
        { ( (MapMainFrame)comp).setVisible(false);
          ( (MapMainFrame)comp).setMapContext(null);// ����������� �����
          ( (MapMainFrame)comp).setContext(null);  // ������������ �� �������
        }
        else if(comp instanceof MapPropertyFrame)
        { ( (MapPropertyFrame)comp).setVisible(false);
        }
        else if(comp instanceof MapElementsFrame)
        { ( (MapElementsFrame)comp).setVisible(false);
        }
      }
    }
    //--------------------------------------
    // ������� "������\���������� �����������"
    else if(ae.getActionCommand().equals("startevent"))
    { 	aModel.disable("menuOptimizeCriteria");// ��������� ��������� ����� ����
        //aModel.disable("menuOptimizeMode");
        aModel.disable("menuOptimizeStart");
        aModel.enable("menuOptimizeStop");
        aModel.disable("menuMapOpen");    // ����� ������ ����������� ��������� ����� ����� ������
        aModel.disable("menuSchemeOpen"); // ����� ������ ����������� ��������� ����� ����� ������
        aModel.disable("menuLoadSm");     // ����� ������ ����������� ��������� ������� ������
        aModel.fireModelChanged("");
        //���� ��� ������ �����������, �� ���� � ����� ����� ������ ������
        if(kisSelectFrame != null)
        {  kisSelectFrame.jComboBox_wave.setEnabled(false);
           kisSelectFrame.jTextField2.setEditable(false);//���� �� ������ ������
           kisSelectFrame.jTextField4.setEditable(false);//���� �� ������ ������
        }
    }
    //--------------------------------------
    // ������� "������������� �����������"
    else if(ae.getActionCommand().equals("stopevent"))
    { 	aModel.enable("menuOptimizeStart");
        aModel.disable("menuOptimizeStop");
        aModel.fireModelChanged("");
    }
    //--------------------------------------
    // �������� ����� �� ������ ������� � ����������� �������� �����
    else if(ae.getActionCommand().equals("solution_updated_event"))
    { Vector new_sps = optimizerContext.solution.paths; // ��� ���� ������ �������
      SchemePath sp; // ���� ���� �� �������
      // ������� ��� ���� �� ����
      for(Enumeration ps = scheme.paths.elements(); ps.hasMoreElements();)
      { sp = (SchemePath)ps.nextElement();
        Pool.remove(SchemePath.typ, sp.getId());
      }
      // ������� ��� ���� �� �����
      schemeFrame.schemePanel.removeAllPathsFromScheme();
      // ��������������� �������� ���� �� ����� � � ����
      if(optimizerContext.original_paths != null) // ���� ���������� �� ����� ���� ����
      { for(Iterator ops = optimizerContext.original_paths.iterator(); ops.hasNext();)
        { sp = (SchemePath)ops.next();
          schemeFrame.schemePanel.insertPathToScheme(sp);
          Pool.put(SchemePath.typ, sp.getId(), sp);
        }
      }
      // ��������� ����� ���� � ��� � � �����
      for(Enumeration new_sps_enum = new_sps.elements(); new_sps_enum.hasMoreElements();)
      { sp = (SchemePath)new_sps_enum.nextElement();
        Pool.put(SchemePath.typ, sp.getId(), sp);
        schemeFrame.schemePanel.insertPathToScheme(sp); // ����� �� ���������� � scheme.path ��������, ���� ���
                                                        // � ��������, ��� ��� insertPathToScheme ����������� ���� ��
                                                        // ��� ����� ������������ ������, � �� ������ � ����� �������� ������
      }
      // ���������� ���� � ���, ��� ���� �� ����� ���������
      this.getInternalDispatcher().notify(new OperationEvent(this, 0, "scheme_updated_event"));
    }
    //--------------------------------------
    // �������� ����� �� [������������ �� ��] ������� �������������� ������� ��� ���������� ������ � ����� � �����
    else if(ae.getActionCommand().equals("solution_overwrite_event"))
    { Vector new_sps = optimizerContext.solution.paths; // ��� ���� ������ �������
      SchemePath sp; // ���� ���� �� �������
      // ������� ��� ���� (���� ��� ����) �� ����
      if(scheme.paths != null)
      { for(Enumeration ps = scheme.paths.elements(); ps.hasMoreElements();)
        { sp = (SchemePath)ps.nextElement();
          Pool.remove(SchemePath.typ, sp.getId());
        }
      }
      // ������� ��� ���� �� �����
      schemeFrame.schemePanel.removeAllPathsFromScheme();
      // ��������� ����� ���� � ��� � � �����
      if(new_sps != null)
      { for(Enumeration e_new_sps = new_sps.elements(); e_new_sps.hasMoreElements();)
        { sp = (SchemePath)e_new_sps.nextElement();
          Pool.put(SchemePath.typ, sp.getId(), sp);
          schemeFrame.schemePanel.insertPathToScheme(sp); // ����� �� ���������� � scheme.path ��������, ���� ��� � ��������, ��� ��� insertPathToScheme ����������� ���� �� ��� ����� ������������ ������, � �� ������ � ����� �������� ������
        }
      }
      else
      { System.err.println("solution_overwrite_event catched but solution.paths = null");
      }
      // ���������� ���� � ���, ��� ���� �� ����� ���������
      this.getInternalDispatcher().notify(new OperationEvent(this, 0, "scheme_updated_event"));
    }
    //--------------------------------------
    // �������� ����� ����� ��������
    else if(ae.getActionCommand().equals("scheme_updated_event"))
    { if(mapContext != null && mapFrame != null)
      {	 MapContext mc = mapContext;
         for(int i=0; i<scheme.paths.size(); i++)
         { SchemePath se = (SchemePath)scheme.paths.get(i);
           se.mtppe = null;
           Hashtable ht = Pool.getHash(MapTransmissionPathProtoElement.typ);
           if(ht != null)
           { for(Enumeration enum = ht.elements(); enum.hasMoreElements(); )
             { MapTransmissionPathProtoElement mtppe = (MapTransmissionPathProtoElement)enum.nextElement();
               if(mtppe.pathtype_ids.contains(se.type_id))
               { se.mtppe = mtppe;
                 break;
               }
             }
           }
           if(se.mtppe != null)
           { mapFrame.lnl().placeElement(se, se.mtppe);//������� ���� �� �����
           }
         }
         getInternalDispatcher().notify(new OperationEvent(this, 0, "mapchangeevent"));
         mapFrame.lnl().postDirtyEvent();
         mapFrame.lnl().postPaintEvent();
      }
    }
    //--------------------------------------
    // ������� "����������� �����" ( � �������� )
    else if(ae.getActionCommand().equals("print_scheme"))
    {	SchemePrintThread print_thread = new SchemePrintThread(schemeFrame.schemePanel, solutionFrame);
      print_thread.start();
    }
    //--------------------------------------
    //������� "������� �������� ����" (����������� �������� CloseAllCommand)
    else if(ae.getActionCommand().equals("close_all"))
    { for(int i = 0; i<desktopPane.getComponents().length; i++)
      { Component comp = desktopPane.getComponent(i);
        if(comp instanceof ElementsListFrame) //���� ������� ���������� �����
        { ((ElementsListFrame)comp).dispose();
          comp = null;
        }
      }
    }
    //--------------------------------------
    else if(ae.getActionCommand().equals("contextchange"))
    { ContextChangeEvent cce = (ContextChangeEvent )ae;
      System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
      if(cce.DOMAIN_SELECTED)
      {	setDomainSelected();
      }
      if(cce.SESSION_OPENED)
      {	SessionInterface ssi = (SessionInterface )cce.getSource();
        if(aContext.getSessionInterface().equals(ssi))
        { aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

          DataSourceInterface dataSource = aContext.getDataSourceInterface();
          new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
          new SchemeDataSourceImage(dataSource).LoadNetDirectory();//�������������� ������� � ������������� ���������
          new SchemeDataSourceImage(dataSource).LoadISMDirectory();// ���-�� �����
          new SchemeDataSourceImage(dataSource).LoadSchemeProto();
          new MapDataSourceImage(dataSource).LoadProtoElements();
          setSessionOpened();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
          statusBar.setText("user", aContext.getSessionInterface().getUser());
        }
      }
      if(cce.SESSION_CLOSED)
      {	SessionInterface ssi = (SessionInterface )cce.getSource();
        if(aContext.getSessionInterface().equals(ssi))
        {
          aContext.setDataSourceInterface(null);
          setSessionClosed();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("session", LangModel.String("statusNoSession"));
          statusBar.setText("user", LangModel.String("statusNoUser"));
        }
      }
      if(cce.SESSION_CHANGING)
      {  statusBar.setText("status", LangModel.String("statusSettingSession"));
      }
      if(cce.SESSION_CHANGED)	{}
      if(cce.CONNECTION_OPENED)
      {	ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {	setConnectionOpened();

          statusBar.setText("status", LangModel.String("statusReady"));
          statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
        }
      }
      if(cce.CONNECTION_CLOSED)
      {	ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {	statusBar.setText("status", LangModel.String("statusError"));
          statusBar.setText("server", LangModel.String("statusConnectionError"));

          statusBar.setText("status", LangModel.String("statusDisconnected"));
          statusBar.setText("server", LangModel.String("statusNoConnection"));

          setConnectionClosed();
        }
      }
      if(cce.CONNECTION_FAILED)
      {	ConnectionInterface cci = (ConnectionInterface)cce.getSource();
        if(aContext.getConnectionInterface().equals(cci))
        {	statusBar.setText("status", LangModel.String("statusError"));
          statusBar.setText("server", LangModel.String("statusConnectionError"));

          setConnectionFailed();
        }
      }
      if(cce.CONNECTION_CHANGING)
      {	statusBar.setText("status", LangModel.String("statusConnecting"));
      }
      if(cce.CONNECTION_CHANGED){}
    }
    //--------------------------------------
    // ��� ����, ����� ��������� ���� � ��������� ����������� �������� �����
    else if (ae.getActionCommand().equals("addschemeevent"))
    { Dimension dim = desktopPane.getSize();
      String scheme_id = (String)ae.getSource();
      Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
      scheme.unpack();
      SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
      panel.ignore_loading = true;
      SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
      frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
      frame.setTitle(scheme.getName());
      desktopPane.add(frame);
      frame.setSize(dim.width/2, dim.height/2);
      frame.setLocation(dim.width/4, dim.height/4);
      frame.setVisible(true);
      frame.toFront();
      panel.openScheme(scheme);
    }
    else if(ae.getActionCommand().equals("addschemeelementevent"))
    { String se_id = (String)ae.getSource();
      SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, se_id);
      se.unpack();
      SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
      panel.setGraphSize(new Dimension());
      SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
      frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
      frame.setTitle(se.getName());
      desktopPane.add(frame);
      Dimension dim = desktopPane.getSize();
      frame.setSize(dim.width/2, dim.height/2);
      frame.setLocation(dim.width/4, dim.height/4);
      frame.setVisible(true);
      frame.toFront();
      panel.openSchemeElement(se);
    }
    //--------------------------------------
    // ��� ����, ����� ��������� ���� � ��������� ����������� �������� �����
    else if(ae.getActionCommand().equals("mapaddschemeevent"))
    { MapMainFrame fr = (MapMainFrame)Pool.get("environment", "mapmainframe");
      if(fr!=null && (fr.getParent()!=null && fr.getParent().equals(desktopPane)) )
      { Dimension dim = desktopPane.getSize();
        String scheme_id = (String)ae.getSource();
        Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
        scheme.unpack();
        SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
        panel.ignore_loading = true;
        //ElementsEditorFrame frame = new ElementsEditorFrame(aContext, panel);
        SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
        frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(scheme.getName());
        desktopPane.add(frame);
        frame.setSize(dim.width/2, dim.height/2);
        frame.setLocation(dim.width/4, dim.height/4);
        frame.setVisible(true); frame.toFront();
        panel.openScheme(scheme);
      }
    }
    else if(ae.getActionCommand().equals("mapaddschemeelementevent"))
    { MapMainFrame fr = (MapMainFrame)Pool.get("environment", "mapmainframe");
      if(fr!=null && (fr.getParent()!=null && fr.getParent().equals(desktopPane)) )
      { String se_id = (String)ae.getSource();
        SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, se_id);
        se.unpack();
        SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
        panel.setGraphSize(new Dimension());
        SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
        frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(se.getName());
        desktopPane.add(frame);
        Dimension dim = desktopPane.getSize();
        frame.setSize(dim.width/2, dim.height/2);
        frame.setLocation(dim.width/4, dim.height/4);
        frame.setVisible(true); frame.toFront();
        panel.openSchemeElement(se);
      }
    }
    //--------------------------------------
	}
	//----------------------------------------------------------------------------------------------------------
	public void setConnectionOpened()
	{	ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	//----------------------------------------------------------------------------------------------------------
	public void setConnectionClosed()
	{	ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	//----------------------------------------------------------------------------------------------------------
	public void setConnectionFailed()
	{	ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	//----------------------------------------------------------------------------------------------------------
	public void setSessionOpened()// ���������� ��� �������� ������
	{	// �������� ���� �������
		Checker checker = new Checker( aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.enterOptimizationModul) )
		{
  return;
		}
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionClose");
		aModel.disable("menuSessionNew");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");
		aModel.enable("menuSessionDomain");
    aModel.enable("menuReport");    
		aModel.fireModelChanged("");

	    mainToolBar.open_scheme.setEnabled(true);
	    mainToolBar.open_map.setEnabled(true);
	    mainToolBar.new_session.setEnabled(false);
	
	    String domain_id = aContext.getSessionInterface().getDomainId();
	    if(domain_id!=null&&!domain_id.equals(""))
	    { setDomainSelected();
	    }
  }
//----------------------------------------------------------------------------------------------------------
  public void setSessionClosed()
  { // ��� �������� ����� , ��������� ��� ����
    new CloseAllCommand(internal_dispatcher, desktopPane, aContext, this).execute();

    ApplicationModel aModel = aContext.getApplicationModel();
    aModel.disable("menuSessionClose");
    aModel.enable("menuSessionNew");
    aModel.disable("menuSessionOptions");
    aModel.disable("menuSessionChangePassword");
    aModel.disable("menuSessionDomain");
    aModel.enable("menuView");
    aModel.enable("menuScheme");
    aModel.enable("menuOptimize");
    aModel.disable("menuMapOpen");
    aModel.disable("menuSchemeOpen");
    aModel.disable("menuSchemeSave");
    aModel.disable("menuSchemeSaveAs");
    aModel.disable("menuLoadSm");
    aModel.disable("menuClearScheme");
    aModel.disable("menuReport");    
    aModel.fireModelChanged("");

    mainToolBar.open_scheme.setEnabled(false);
    mainToolBar.open_map.setEnabled(false);
    mainToolBar.new_session.setEnabled(true);
  }
//--------------------------------------------------------------------------------------------------------------
  public void setDomainSelected()
  { ApplicationModel aModel = aContext.getApplicationModel();
    DataSourceInterface dataSource = aContext.getDataSourceInterface();
    // ���� ����� �� ���������, �� ������ ���������� �� ����� , ��� �� ����������
    String domain_id_new = aContext.getSessionInterface().getDomainId();
    aContext.getDispatcher().notify(new StatusMessageEvent("������������� ������..."));
    if( domain_id == null || !domain_id.equals(domain_id_new) )
    { // ��� ����� ������, ��������� ��� ����
      new CloseAllCommand(internal_dispatcher, desktopPane, aContext, this).execute();

      new SchemeDataSourceImage(dataSource).LoadSchemes();
      new ConfigDataSourceImage(dataSource).LoadISM(); //�������� ������������ ISM
      new MapDataSourceImage(dataSource).LoadMaps();
      new ConfigDataSourceImage(dataSource).LoadNet(); //�������� ������������ ����
      domain_id = domain_id_new;
      opened_scheme_num = 0;
    }
    aContext.getDispatcher().notify(new StatusMessageEvent("������ ���������"));
    aModel.fireModelChanged("");
    statusBar.setText( "domain", Pool.getName("domain",aContext.getSessionInterface().getDomainId()) );
    // ��� �������� ������ ���������� ��������� ������ � ������ , ������ � � �
    aModel.enable("menuView");
    if(opened_scheme_num==0) // ���� ����� ��� �� �������, �� ��� ������ �� �����
    { aModel.disable("menuViewMap");
      aModel.disable("menuViewScheme");
      aModel.disable("menuViewMapElProperties");
      aModel.disable("menuViewSchElProperties");
      aModel.disable("menuViewGraph");
      aModel.disable("menuViewKIS");
      aModel.disable("menuViewParams");
      aModel.disable("menuViewOptions");
      aModel.disable("menuViewSolution");
      aModel.disable("menuViewMode");
      aModel.disable("menuViewShowall");
    }
    aModel.enable("menuScheme");
    aModel.enable("menuMapOpen");
    aModel.enable("menuSchemeOpen");

    if(opened_scheme_num>0) // ���� ����� ��� ���� �������  �� ��������� � ���������
    { aModel.enable("menuSchemeSave");
      aModel.enable("menuSchemeSaveAs");
      //aModel.enable("menuLoadSm");
    }
    aModel.enable("menuOptimize");
    if(opened_scheme_num>0) // ���� ����� ��� ���� �������  �� ��������� � ��������������
    { aModel.enable("menuOptimizeCriteria");
      aModel.enable("menuOptimizeMode");
      aModel.enable("menuOptimizeStart");
    }
    aModel.disable("menuOptimizeStop");
    aModel.fireModelChanged("");
  }
  //--------------------------------------------------------------------------------------------------------------
	public Dispatcher getInternalDispatcher()
	{	return internal_dispatcher;
	}
	//--------------------------------------------------------------------------------------------------------------
	void this_componentShown(ComponentEvent e)
	{	init_module();
	}
	//--------------------------------------------------------------------------------------------------------------
	void this_windowClosing(WindowEvent e)
	{	internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}
	//--------------------------------------------------------------------------------------------------------------
	protected void processWindowEvent(WindowEvent e)
	{	if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{	Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{	internal_dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}
//==================================================================================================================
class OptimizeMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	OptimizeMDIMain adaptee;
	OptimizeMDIMain_this_componentAdapter(OptimizeMDIMain adaptee)
	{	this.adaptee = adaptee;
	}
	//--------------------------------------------------------------------------------------------------------------
	public void componentShown(ComponentEvent e)
	{	adaptee.this_componentShown(e);
	}
}
//==================================================================================================================
