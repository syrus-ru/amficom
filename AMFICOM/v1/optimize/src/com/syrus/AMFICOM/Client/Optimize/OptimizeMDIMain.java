// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.General.Command.Optimize.ClosePricelistCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.LoadPricelistCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.LoadSolutionCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.OptOpenMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.OptimizeStartCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.OptimizeStopCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SavePricelistCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SaveSolutionAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SaveasPricelistCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SchemeOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SchemePathsClearCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SchemeSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.SetOptimizeModeCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewGraphCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewKISCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewModeCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewParamCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewSchElPropFrameCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewSchemeCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewShowallCommand;
import com.syrus.AMFICOM.Client.General.Command.Optimize.ViewSolutionCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Optimize.UI.OptimizeMainToolBar;
import com.syrus.AMFICOM.Client.Optimize.UI.SchemePrintThread;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.command.editor.ViewAdditionalPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewGeneralPropertiesCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.HelpAboutCommand;
import com.syrus.AMFICOM.client_.scheme.SchemeEditorMainFrame;
import com.syrus.AMFICOM.client_.scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class OptimizeMDIMain extends AbstractMainFrame implements PropertyChangeListener
{

  // XXX wtf ?
//  public MapContext mapContext;
  //public ISMMapContext ismMapContext;//  ismMapContext больше не используется, но в коде с ним почему-то ещё идёт работа

  // <Vit>
  public OpticalOptimizerContext optimizerContext;
  public KISselectionFrame kisSelectFrame;           // объект (окно), содержащй информацию о характеристиках и ценах оборудования
  public IterationsHistoryFrame iterHistFrame;       // окно графика хода оптимизации
  public OpticalOptimizationParamsFrame paramsFrame; // окно задания параметров оптимизации
  public ViewSolutionFrame solutionFrame;            // окно подробной нитки маршрута одного из решений
  public NodesOptimizePropertiesFrame nodesModeFrame;// окно задания режимов узлов ( fixed , active )
  public RibsOptimizePropertiesFrame ribsModeFrame;  // окно задания режимов рёбер ( active )
  public ViewSchemeFrame schemeFrame;                // окно отображения схемы
  public Scheme scheme;
  private UIDefaults frames; // создание в init_module()
//XXX wtf ?
//  public MapMainFrame mapFrame;                      // окно отображения схемы
  private int opened_scheme_num = 0; // количество открытых схем
  public boolean map_is_opened = false; // индикатор открытия карты
//  public OptimizeMainToolBar mainToolBar; // панелька с кнопками, дублирующими главное меню
  // </Vit>

    //-------------------------------------------------------------------------------------------------------------
    public OptimizeMDIMain(ApplicationContext aContext){ 
    	
    	super(aContext, LangModelOptimize.String("AppTitle"), new OptimizeMenuBar(aContext
				.getApplicationModel()), new OptimizeMainToolBar());
    	
    	this.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/design.gif"));

      Environment.addWindow(this);
      setContext(aContext);
    }
    //-------------------------------------------------------------------------------------------------------------
    public OptimizeMDIMain()
    { this(new ApplicationContext());
    }
    //-------------------------------------------------------------------------------------------------------------
    public void init_module()
    { this.frames = new UIDefaults();
      this.frames.put(SchemeViewerFrame.NAME, new UIDefaults.LazyValue() {
		
      public Object createValue(UIDefaults table) {
			Log.debugMessage(".createValue | EDITOR_FRAME", Level.FINEST);
			SchemeViewerFrame editorFrame = new SchemeViewerFrame(SchemeEditorMainFrame.this.aContext, 
																	SchemeEditorMainFrame.this.schemeTab);
			editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
			SchemeEditorMainFrame.this.desktopPane.add(editorFrame);
			return editorFrame;
		}
      });
    
      ApplicationModel aModel = aContext.getApplicationModel();

      this.dispatcher.addPropertyChangeListener( "mapopened", this);
      this.dispatcher.addPropertyChangeListener( "map_close", this);// мой аналог mapcloseevent
      this.dispatcher.addPropertyChangeListener( "mapcloseevent", this);
      this.dispatcher.addPropertyChangeListener( "scheme_is_opened", this);
      this.dispatcher.addPropertyChangeListener( "showallevent", this);
      this.dispatcher.addPropertyChangeListener( "close_all", this);

      this.dispatcher.addPropertyChangeListener( "startevent", this);
      this.dispatcher.addPropertyChangeListener( "stopevent", this);
      this.dispatcher.addPropertyChangeListener( "solution_updated_event", this);
      this.dispatcher.addPropertyChangeListener( "solution_overwrite_event", this);
      this.dispatcher.addPropertyChangeListener( "scheme_updated_event", this);

      this.dispatcher.addPropertyChangeListener( "print_scheme", this);

      this.dispatcher.addPropertyChangeListener( "mapjframeshownevent", this);

      this.dispatcher.addPropertyChangeListener( "addschemeevent", this);
      this.dispatcher.addPropertyChangeListener( "addschemeelementevent", this);

      Dispatcher eDispatcher = Environment.getDispatcher();
      eDispatcher.addPropertyChangeListener( "mapaddschemeevent", this);
      eDispatcher.addPropertyChangeListener( "mapaddschemeelementevent", this);

      // открыть топологическую схему для оптимизации
      aModel.setCommand("menuMapOpen", new OptOpenMapViewCommand(this.desktopPane, aContext));
      // открыть физическую схему сети ( возможно, с одним из ранее записанных решений оптимизированного мониторинга )
      aModel.setCommand("menuSchemeOpen", new SchemeOpenCommand(this.dispatcher, aContext, this) );
      // сохранить схему со всей информацией, которую на ней изменили ( записали оптимизированые пути и атрибуты узлов )
      aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(this.dispatcher, aContext, this));
      //aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand_2(this.dispatcher, aContext, this));
      aModel.setCommand("menuSchemeSaveAs", new SaveSolutionAsCommand(this.dispatcher, aContext, this)); //  то ли надо передавать в ф-ю ?
      // загрузить новое решение и переписать существующие пути на схеме вновь загруженными поверх ( ВСЕ старые стереть )
      aModel.setCommand("menuLoadSm", new LoadSolutionCommand(this.dispatcher, aContext, this)); // то ли надо передавать в ф-ю ?
      //очисить схему от всех путей, которые в ней могут находиться
      aModel.setCommand("menuClearScheme", new SchemePathsClearCommand(this.dispatcher, aContext, this));
      aModel.setCommand("menuViewScheme", new ViewSchemeCommand(this.dispatcher, desktopPane, aContext));
      //открытие свойств карты и схемы
      aModel.setCommand("menuViewMapElProperties", new ViewGeneralPropertiesCommand(desktopPane, aContext));
      aModel.setCommand("menuViewMapAdProperties", new ViewAdditionalPropertiesCommand(desktopPane, aContext));
      aModel.setCommand("menuViewSchElProperties", new ViewSchElPropFrameCommand(this.dispatcher, aContext, this));
      aModel.setCommand("menuViewGraph", new ViewGraphCommand(this.dispatcher, desktopPane, aContext, optimizerContext, this));
      aModel.setCommand("menuViewSolution", new ViewSolutionCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuViewKIS", new ViewKISCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuViewParams", new ViewParamCommand(this.dispatcher, desktopPane, aContext, optimizerContext, this));
      aModel.setCommand("menuViewMode", new ViewModeCommand(this.dispatcher, desktopPane, aContext, this));
      // показать все основные окна (на всякий случай посылает event "showallevent")
      aModel.setCommand("menuViewShowall", new ViewShowallCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeStart", new OptimizeStartCommand(this.dispatcher, this) );
      aModel.setCommand("menuOptimizeStop", new OptimizeStopCommand(this.dispatcher, optimizerContext, this) );
      aModel.setCommand("menuOptimizeCriteriaPriceLoad", new LoadPricelistCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceSave", new SavePricelistCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceSaveas", new SaveasPricelistCommand(this.dispatcher, desktopPane, aContext, this));
      aModel.setCommand("menuOptimizeCriteriaPriceClose", new ClosePricelistCommand(this.dispatcher, desktopPane, aContext, this));
      // изменение режима оптимизации ( встречное\односторонее )
      aModel.setCommand("menuOptimizeModeUnidirect", new SetOptimizeModeCommand(  0, this.dispatcher, desktopPane, aContext, this ));
      aModel.setCommand("menuOptimizeModeBidirect", new SetOptimizeModeCommand(  1, this.dispatcher, desktopPane, aContext, this ));

      aModel.add("menuHelpAbout", new HelpAboutCommand(this));//, new OptimizeMDIMain_AboutBoxPanel1()));

      aModel.setAllItemsEnabled(false);

      aModel.setEnabled("menuScheme", true); // меню "Схема"
      // aModel.setEnabled("menuSchemeOpen", false);
      // aModel.setEnabled("menuSchemeSave", false);
      // aModel.setEnabled("menuSchemeSaveAs", false);
      // aModel.setEnabled("menuSchemeClose", false);
      aModel.setEnabled("menuView", true); // меню "вид"
      //  aModel.setEnabled("menuViewMap", false);
      //  aModel.setEnabled("menuViewScheme", false);
      //  aModel.setEnabled("menumenuViewMapElProperties", false);
      //  aModel.setEnabled("menumenuViewSchElProperties", false);
      aModel.setEnabled("menuOptimize", true); // меню "оптимизация"
      //  aModel.setEnabled("menuOptimizeStart", false);
      //  aModel.setEnabled("menuOptimizeStop", false);
      //  aModel.setEnabled("menuViewSolution", false);
      //  aModel.setEnabled("menuViewKIS", false);
      //  aModel.setEnabled("menuViewMode", false);
      //  aModel.setEnabled("menuViewParams", false);
      //  aModel.setEnabled("menuViewShowall", false);
      //  aModel.setEnabled("menuViewMap", false);
      //  aModel.setEnabled("menuViewMapElProperties", false);
      //  aModel.setEnabled("menuViewSchElProperties", false);
      //  aModel.setEnable("menuViewScheme", true);
      //  aModel.setEnable("menuViewParams", true);
      //  aModel.setEnable("menuViewOptions", true);
      //  aModel.setEnable("menuViewMode", true);
      //  aModel.setEnable("menuViewSolution", true);
      //  aModel.setEnable("menuViewGraph", true);
      //  aModel.setEnable("menuViewShowall", true);

      //  aModel.setEnable("menuMapOpen", true);
      //  aModel.setEnable("menuSchemeOpen", true);
      //  aModel.setEnable("menuSchemeSave", true);
      //  aModel.setEnable("menuSchemeClose", true);
      aModel.fireModelChanged("");
    }

    //--------------------------------------------------------------------------------------------------------------
    // добавлено в соответствии с письмом Андрея от 15 апреля 2003 года
    public void dispose()
    { this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, "mapcloseevent", null, null));
      super.dispose();
    }
    //----------------------------------------------------------------------------------------------------------
  @Override
  	public void setSessionClosed() { 
		super.setSessionClosed();
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuScheme", true);
		aModel.setEnabled("menuOptimize", true);
		aModel.setEnabled("menuMapOpen", false);
		aModel.setEnabled("menuSchemeOpen", false);
		aModel.setEnabled("menuSchemeSave", false);
		aModel.setEnabled("menuSchemeSaveAs", false);
		aModel.setEnabled("menuLoadSm", false);
		aModel.setEnabled("menuClearScheme", false);
		aModel.fireModelChanged("");
// XXX: узнать, как теперь правильно дизейблить кнопочки 
//    mainToolBar.open_scheme.setEnabled(false);
//    mainToolBar.open_map.setEnabled(false);
//    mainToolBar.new_session.setEnabled(true);
  	}
//--------------------------------------------------------------------------------------------------------------
  @Override
  public void setDomainSelected(){ 
	super.setDomainSelected();
	ApplicationModel aModel = aContext.getApplicationModel();
    {  opened_scheme_num = 0;
    }
    aModel.fireModelChanged("");
    // при открытии домена становится доступной работа с картой , схемой и т д
    aModel.setEnabled("menuView", true);
    if(opened_scheme_num==0) // если схема ещё не открыта, то эти пункты не нужны
    { aModel.setEnabled("menuViewMap", false);
      aModel.setEnabled("menuViewScheme", false);
      aModel.setEnabled("menuViewMapElProperties", false);
      aModel.setEnabled("menuViewSchElProperties", false);
      aModel.setEnabled("menuViewGraph", false);
      aModel.setEnabled("menuViewKIS", false);
      aModel.setEnabled("menuViewParams", false);
      aModel.setEnabled("menuViewOptions", false);
      aModel.setEnabled("menuViewSolution", false);
      aModel.setEnabled("menuViewMode", false);
      aModel.setEnabled("menuViewShowall", false);
    }
    aModel.setEnabled("menuScheme", true);
    aModel.setEnabled("menuMapOpen", true);
    aModel.setEnabled("menuSchemeOpen", true);

    if(opened_scheme_num>0) // если схема уже была открыта  то разрешаем её сохранять
    { aModel.setEnabled("menuSchemeSave", true);
      aModel.setEnabled("menuSchemeSaveAs", true);
      //aModel.setEnabled("menuLoadSm", true);
    }
    aModel.setEnabled("menuOptimize", true);
    if(opened_scheme_num>0) // если схема уже была открыта  то разрешаем её оптимизировать
    { aModel.setEnabled("menuOptimizeCriteria", true);
      aModel.setEnabled("menuOptimizeMode", true);
      aModel.setEnabled("menuOptimizeStart", true);
    }
    aModel.setEnabled("menuOptimizeStop", false);
    aModel.fireModelChanged("");
  }
  //--------------------------------------------------------------------------------------------------------------
	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		ApplicationModel aModel = aContext.getApplicationModel();
		final String propertyName = evt.getPropertyName();
	    //---------------------
	     if(propertyName.equals("mapopened"))// событие: открыта новая карта
	     {
//	 все нижезакоментированные действия автоматически выполняются при открытии схемы
//	 так как при открытии карты автомматически открывается и схема
//	       //как только открывается новая карта , сразу производятся все действия для подготовки процесса оптимизации
//	       optimizerContext = new OpticalOptimizerContext(this);
//	       // при открытии карты надо переприсвоить optimizerContext в модели всех команд, которые к ней относятся;
//	       aModel.getCommand("menuOptimizeStart").setParameter("optimizerContext", optimizerContext);
//	       aModel.getCommand("menuOptimizeStop").setParameter("optimizerContext", optimizerContext);
//	       aModel.getCommand("menuViewGraph").setParameter("optimizerContext", optimizerContext);
//	       aModel.getCommand("menuViewSolution").setParameter("optimizerContext", optimizerContext);
//	       aModel.getCommand("menuViewParams").setParameter("optimizerContext", optimizerContext);
//	       aModel.getCommand("menuViewShowall").setParameter("optimizerContext", optimizerContext);
//	       //System.out.println("Open map " + mapContext.id);
	//
//	       //задаём цены на устройства при открытии новой карты: получаем таблицу идентификаторов для задания цены
//	       // ht2 хранит идентификаторы устройств в другом виде, нежели ht1. Именно такой формат нужен для окна задания цены
//	       Hashtable ht2 = (Hashtable)Pool.getHash(EquipmentType.typ);
//	       // окно задания цены на RTU и коммутаторы (при открытии окна карты передвинем)
//	       kisSelectFrame = new KISselectionFrame( optimizerContext, ht2, this);
//	       this.desktopPane.add(kisSelectFrame, null);
//	       kisSelectFrame.setVisible(true);

	       aModel.setEnabled("menuScheme", true);
	       aModel.setEnabled("menuMapOpen", true);
	       aModel.setEnabled("menuSchemeOpen", true);
	       aModel.setEnabled("menuSchemeSave", true);
	       aModel.setEnabled("menuSchemeSaveAs", true);
	       aModel.setEnabled("menuLoadSm", true);
	       aModel.setEnabled("menuClearScheme", true);
	       aModel.setEnabled("menuSchemeClose", true);
	       aModel.setEnabled("menuView", true);
	       aModel.setEnabled("menuViewMap", true);
	       aModel.setEnabled("menuViewScheme", true);
	       aModel.setEnabled("menuViewMapElProperties", true);
	       aModel.setEnabled("menuViewSchElProperties", true);
	       aModel.setEnabled("menuViewKIS", true);
	       aModel.setEnabled("menuViewGraph", true);
	       aModel.setEnabled("menuViewSolution", true);
	       aModel.setEnabled("menuViewParams", true);
	       aModel.setEnabled("menuViewMode", true);
	       aModel.setEnabled("menuViewShowall", true);
	       aModel.setEnabled("menuOptimize", true);
	       aModel.setEnabled("menuOptimizeCriteria", true);
	       aModel.setEnabled("menuOptimizeMode", true);
	       aModel.setEnabled("menuOptimizeModeUnidirect", true);
	       aModel.setEnabled("menuOptimizeModeBidirect", true);
	       aModel.setEnabled("menuOptimizeCriteriaPrice", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceLoad", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceSave", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceSaveas", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceClose", true);
	       aModel.setEnabled("menuOptimizeStart", true);
	       aModel.setEnabled("menuOptimizeStop", false);
	       aModel.fireModelChanged("");

	       opened_scheme_num++; // счётчик открытых схем
	       map_is_opened = true;
	    }
	    //--------------------------------------
	    else if(propertyName.equals("scheme_is_opened"))// событие: открыта новая схема
	    {	 
	       //как только открывается новая схема , сразу производятся все действия для подготовки процесса оптимизации
	       optimizerContext = new OpticalOptimizerContext(this);
	       // при открытии карты надо переприсвоить optimizerContext в модели всех команд, которые к ней относятся;
	       aModel.getCommand("menuOptimizeStart").setParameter("optimizerContext", optimizerContext);
	       aModel.getCommand("menuOptimizeStop").setParameter("optimizerContext", optimizerContext);
	       aModel.getCommand("menuViewGraph").setParameter("optimizerContext", optimizerContext);
	       aModel.getCommand("menuViewSolution").setParameter("optimizerContext", optimizerContext);
	       aModel.getCommand("menuViewParams").setParameter("optimizerContext", optimizerContext);
	       aModel.getCommand("menuViewShowall").setParameter("optimizerContext", optimizerContext);
	       //задаём цены на устройства при открытии новой карты: получаем таблицу идентификаторов для задания цены
	       // ht2 хранит идентификаторы устройств в другом виде, нежели ht1. Именно такой формат нужен для окна задания цены
	       Hashtable ht2 = (Hashtable)Pool.getHash(EquipmentType.typ);
	       // окно задания цены на RTU и коммутаторы (при открытии окна карты передвинем)
	       kisSelectFrame = new KISselectionFrame( optimizerContext, ht2, this);
	       this.desktopPane.add(kisSelectFrame, null);
	       kisSelectFrame.setVisible(false);// пока ещё не должно быть видно (на случай показа предупреждения)
	       //создаём окно для карты
	       schemeFrame = new ViewSchemeFrame(this);
	       desktopPane.add(schemeFrame);
	       schemeFrame.setVisible(false);// до выполнения команды "показать схему" окно невидимо
	       // если схема уже содержит пути, то запоминаем их
	       optimizerContext.original_paths = new ArrayList(); // создаём в любом случае, если он будет пустой, это будет признаком того, что в схеме изначально не было путей
	       optimizerContext.originally_lconnected_nodes = new ArrayList();
	       if(scheme.paths.size() != 0)
	       { System.out.println("event scheme_is_opened: Loaded scheme already contains scheme path(s).");
	         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Схема уже содержит пути тестирования.", "Внимание!", javax.swing.JOptionPane.WARNING_MESSAGE );
	         SchemePath sp;
	         System.out.println("Loading initial paths and links ...");
	         for(Iterator iter = scheme.paths.iterator(); iter.hasNext(); )
        	 {  SchemePath sp = (SchemePath)iter.next();
	           // запоминаем начальные пути тестирования
	           optimizerContext.original_paths.add(sp);
	           // прописываем originally_lconnected_nodes (пробегаем по пути, ищем линки и отмечаем соединяемые ими узлы)
           	for( Iterator pes = sp.links.iterator(); pes.hasNext(); )
	           { PathElement pe = (PathElement)pes.next();
	             if(!pe.is_cable)//если это не кабель, а линк
	             { SchemeLink sl = (SchemeLink)Pool.get( SchemeLink.typ, pe.link_id);
	               String se1_id = ((SchemeElement)scheme.getSchemeElementByPort(sl.source_port_id)).id;
	               String se2_id = ((SchemeElement)scheme.getSchemeElementByPort(sl.target_port_id)).id;
	               String sl_id = sl.id;
	               // добавляем запись о том, что два элемента уже соединены линком
	               optimizerContext.originally_lconnected_nodes.add(se1_id);
	               optimizerContext.originally_lconnected_nodes.add(se2_id);
	               optimizerContext.originally_lconnected_nodes.add(sl_id);
	             }
	           }
	         }
	         System.out.println("Loading initial paths and links - done");
	       }
	       kisSelectFrame.setVisible(true); // после возможного показа предупредления можно сделать видимым

	       aModel.setEnabled("menuScheme", true);
	       aModel.setEnabled("menuMapOpen", true);
	       aModel.setEnabled("menuSchemeOpen", true);
	       aModel.setEnabled("menuSchemeSave", true);
	       aModel.setEnabled("menuSchemeSaveAs", true);
	       aModel.setEnabled("menuLoadSm", true);
	       aModel.setEnabled("menuClearScheme", true);
	       aModel.setEnabled("menuSchemeClose", true);
	       aModel.setEnabled("menuView", true);
	       aModel.setEnabled("menuViewMap", false);
	       aModel.setEnabled("menuViewScheme", true);
	       aModel.setEnabled("menuViewMapElProperties", false);
	       aModel.setEnabled("menuViewSchElProperties", true);
	       aModel.setEnabled("menuViewKIS", true);
	       aModel.setEnabled("menuViewGraph", true);
	       aModel.setEnabled("menuViewSolution", true);
	       aModel.setEnabled("menuViewParams", true);
	       aModel.setEnabled("menuViewMode", true);
	       aModel.setEnabled("menuViewShowall", true);
	       aModel.setEnabled("menuOptimize", true);
	       aModel.setEnabled("menuOptimizeCriteria", true);
	       aModel.setEnabled("menuOptimizeCriteriaPrice", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceLoad", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceSave", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceSaveas", true);
	       aModel.setEnabled("menuOptimizeCriteriaPriceClose", true);
	       aModel.setEnabled("menuOptimizeMode", true);
	       aModel.setSelected("menuOptimizeModeUnidirect", true);// по умолчанию стоит "одностороннее" тестирование
	       aModel.setSelected("menuOptimizeModeBidirect", false);
	       aModel.setEnabled("menuOptimizeModeUnidirect", true);
	       aModel.setEnabled("menuOptimizeModeBidirect", true);
	       aModel.setEnabled("menuOptimizeStart", true);
	       aModel.setEnabled("menuOptimizeStop", false);
	       aModel.fireModelChanged("");

	       opened_scheme_num++; // счётчик открытых схем
	     }
	    //--------------------------------------
	    else if(propertyName.equals("mapjframeshownevent"))// сообщение о том, что открылось окно сети на карте jframe
	    {	 JInternalFrame frame = (JInternalFrame )ae.getSource();
	       System.out.println("Map j frame shown " + frame.getTitle());
	       //сдвигаем  окно задания цены, чтобы оно не перекрывалось картой (рисуется справа, под окном параметров)
	       if(kisSelectFrame != null)// если окно было открыто, то передвигаем его (окно не открывается , если мы загружаем уже оптимизированную схему сети)
	       { kisSelectFrame.place();
	       }
	    }
	    //--------------------------------------
	    else if(propertyName.equals("mapframeshownevent"))//сообщение о том, что открылось окно карты frame (jframe != frame)
	    { 	 System.out.println("Map frame shown ");
	    }
	    //--------------------------------------
	    else if(propertyName.equals("mapjframeopenevent"))//сообщение о том, что открылось окно вывода одного из ранее сохранённых решений
	    {  System.out.println("Topology solution opened");
	    }
	    //--------------------------------------
	    else if(propertyName.equals("map_close") || propertyName.equals("mapcloseevent"))
	    { System.out.println("Map is closing... ");
	      for(int i = 0; i<desktopPane.getComponents().length; i++)
	      { Component comp = desktopPane.getComponent(i);
	        if(comp instanceof MapMainFrame)
	        { ( (MapMainFrame)comp).setVisible(false);
	          ( (MapMainFrame)comp).setMapContext(null);// освобождаем карту
	          ( (MapMainFrame)comp).setContext(null);  // отписываемся от событий
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
	    // команда "начать\продолжить оптимизацию"
	    else if(propertyName.equals("startevent"))
	    { 	aModel.setEnabled("menuOptimizeCriteria", false);// запретить загружать новые цены
	        //aModel.setEnabled("menuOptimizeMode", false);
	        aModel.setEnabled("menuOptimizeStart", false);
	        aModel.setEnabled("menuOptimizeStop", true);
	        aModel.setEnabled("menuMapOpen", false);    // после начала оптимизации открывать новую схему нельзя
	        aModel.setEnabled("menuSchemeOpen", false); // после начала оптимизации открывать новую схему нельзя
	        aModel.setEnabled("menuLoadSm", false);     // после начала оптимизации загружать решение нельзя
	        aModel.fireModelChanged("");
	        //если уже начали оптимизацию, то цены и длину волны менять нельзя
	        if(kisSelectFrame != null)
	        {  kisSelectFrame.jComboBox_wave.setEnabled(false);
	           kisSelectFrame.jTextField2.setEditable(false);//чтоб не меняли руками
	           kisSelectFrame.jTextField4.setEditable(false);//чтоб не меняли руками
	        }
	    }
	    //--------------------------------------
	    // команда "приостановить оптимизацию"
	    else if(propertyName.equals("stopevent"))
	    { 	aModel.setEnabled("menuOptimizeStart", true);
	        aModel.setEnabled("menuOptimizeStop", false);
	        aModel.fireModelChanged("");
	    }
	    //--------------------------------------
	    // обновить схему по новому решению с сохранением исходных путей
	    else if(propertyName.equals("solution_updated_event"))
	    { Vector new_sps = optimizerContext.solution.paths; // все пути нового решения
	      SchemePath sp; // один путь из решения
	      // удаляем все пути из пула
      for(Iterator ps = scheme.paths.iterator(); ps.hasNext();)
      { sp = (SchemePath)ps.next();
        Pool.remove(SchemePath.typ, sp.getId());
      }
	      // удаляем все пути из схемы
	      schemeFrame.schemePanel.removeAllPathsFromScheme();
	      // восстанавливаем исходные пути на схеме и в пуле
	      if(optimizerContext.original_paths != null) // если изначально на схеме были пути
      { for(Iterator ops = optimizerContext.original_paths.iterator(); ops.hasNext();)
        { sp = (SchemePath)ops.next();
          schemeFrame.schemePanel.insertPathToScheme(sp);
          Pool.put(SchemePath.typ, sp.getId(), sp);
        }
      }
	      // добавляем новые пути в пул и в схему
	      for(Enumeration new_sps_enum = new_sps.elements(); new_sps_enum.hasMoreElements();)
	      { sp = (SchemePath)new_sps_enum.nextElement();
	        Pool.put(SchemePath.typ, sp.getId(), sp);
	        schemeFrame.schemePanel.insertPathToScheme(sp); // важно не обращаться к scheme.path напрямую, хотя это
	                                                        // и возможно, так как insertPathToScheme прописывает путь во
	                                                        // все схемы нижележащего уровня, а не только в схему верхнего уровня
	      }
	      // уведомляем всех о том, что пути на схеме обновлены
	      this.getInternalDispatcher().notify(new OperationEvent(this, 0, "scheme_updated_event"));
	    }
	    //--------------------------------------
	    // обновить схему по [загруженному из БД] решению предварительно очистив все предыдущие записи о путях в схеме
	    else if(propertyName.equals("solution_overwrite_event"))
	    { Vector new_sps = optimizerContext.solution.paths; // все пути нового решения
      SchemePath sp; // один путь из решения
      // удаляем все пути (если они есть) из пула
      if(scheme.paths != null)
      { for(Iterator ps = scheme.paths.iterator(); ps.hasNext();)
        { sp = (SchemePath)ps.next();
          Pool.remove(SchemePath.typ, sp.getId());
        }
      }
      // удаляем все пути из схемы
      schemeFrame.schemePanel.removeAllPathsFromScheme();
      // добавляем новые пути в пул и в схему
      if(new_sps != null)
      { for(Enumeration e_new_sps = new_sps.elements(); e_new_sps.hasMoreElements();)
        { sp = (SchemePath)e_new_sps.nextElement();
          Pool.put(SchemePath.typ, sp.getId(), sp);
          schemeFrame.schemePanel.insertPathToScheme(sp); // важно не обращаться к scheme.path напрямую, хотя это и возможно, так как insertPathToScheme прописывает путь во все схемы нижележащего уровня, а не только в схему верхнего уровня
        }
      }
      else
      { System.err.println("solution_overwrite_event catched but solution.paths = null");
      }
      // уведомляем всех о том, что пути на схеме обновлены
      this.getInternalDispatcher().notify(new OperationEvent(this, 0, "scheme_updated_event"));
    }
	    //--------------------------------------
	    // обновить карту новым решением
	    else if(propertyName.equals("scheme_updated_event"))
	    { if(mapContext != null && mapFrame != null)
	      {	 MapContext mc = mapContext;
	         for(int i=0; i<scheme.paths.size(); i++)
	         { SchemePath se = (SchemePath)scheme.paths.get(i);
	           se.mtppe = null;
	           Hashtable ht = Pool.getHash(MapTransmissionPathProtoElement.typ);
	           if(ht != null)
	           { for(Enumeration enum1 = ht.elements(); enum1.hasMoreElements(); )
	             { MapTransmissionPathProtoElement mtppe = (MapTransmissionPathProtoElement)enum1.nextElement();
	               if(mtppe.pathtype_ids.contains(se.type_id))
	               { se.mtppe = mtppe;
	                 break;
	               }
	             }
	           }
	           if(se.mtppe != null)
	           { mapFrame.lnl().placeElement(se, se.mtppe);//нанесли путь на карту
	           }
	         }
	         getInternalDispatcher().notify(new OperationEvent(this, 0, "mapchangeevent"));
	         mapFrame.lnl().postDirtyEvent();
	         mapFrame.lnl().postPaintEvent();
	      }
	    }
	    //--------------------------------------
	    // команда "распечатать схему" ( с решением )
	    else if(propertyName.equals("print_scheme"))
	    {	SchemePrintThread print_thread = new SchemePrintThread(schemeFrame.schemePanel, solutionFrame);
	      print_thread.start();
	    }
	    //--------------------------------------
	    //событие "закрыть основные окна" (рассылается командой CloseAllCommand)
	    else if(propertyName.equals("close_all"))
	    { for(int i = 0; i<desktopPane.getComponents().length; i++)
	      { Component comp = desktopPane.getComponent(i);
	        if(comp instanceof ElementsListFrame) //окно свойств физической схемы
	        { ((ElementsListFrame)comp).dispose();
	          comp = null;
	        }
	      }
	    }
	    //--------------------------------------
	    // для того, чтобы открывать окна с подробным избражением элемента схемы
	    else if (propertyName.equals("addschemeevent"))
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
	    else if(propertyName.equals("addschemeelementevent"))
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
	    // для того, чтобы открывать окна с подробным избражением элемента карты
	    else if(propertyName.equals("mapaddschemeevent"))
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
	    else if(propertyName.equals("mapaddschemeelementevent"))
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
	//--------------------------------------------------------------------------------------------------------------
}
