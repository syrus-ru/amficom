package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// команда "закрыть всё" - закрывает все окна, кроме главного
//========================================================================================================
public class CloseAllCommand extends VoidCommand
{ private Dispatcher dispatcher;
  private ApplicationContext aContext;
  private JDesktopPane desktop;
  private OptimizeMDIMain mdiMain;
  //--------------------------------------------------------------------------------
  public CloseAllCommand(){}
  //--------------------------------------------------------------------------------
  public CloseAllCommand (  Dispatcher dispatcher, JDesktopPane desktop,
                            ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain )
  {	 this.dispatcher = dispatcher;
     this.desktop = desktop;
     this.aContext = aContext;
     this.mdiMain = optimizeMDIMain;
  }
  //--------------------------------------------------------------------------------
  public void setParameter(String field, Object value)
  {	 if(field.equals("dispatcher"))
     { setDispatcher((Dispatcher)value);
     }
     else if(field.equals("aContext"))
     { setApplicationContext((ApplicationContext)value);
     }
  }
  //--------------------------------------------------------------------------------
  public void setDispatcher(Dispatcher dispatcher)
  { this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  { this.aContext = aContext;
  }
  //--------------------------------------------------------------------------------
  public Object clone()
  { return new CloseAllCommand(dispatcher, desktop, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  {	System.out.println("CloseAllCommand.execute() starting ...");

    // если схема была открыта, то надо закрыть
    dispatcher.notify(new SchemeElementsEvent(this, mdiMain.scheme, SchemeElementsEvent.CLOSE_SCHEME_EVENT));
    new MapOptCloseCommand(dispatcher, aContext, mdiMain).execute();// закрываем карту

    closeAllInternalFrames();// закрыть все окна, которые были открыты при открытии схемы
    disableSomeMenuItems( aContext.getApplicationModel() ); //возвращаем меню в начальное состояние (т.е. почти все пункты неактивны)

    dispatcher.notify(new OperationEvent(this, 0, "close_all"));// на это реагируют, например, кнопки
    mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("схема не загружена"));
    System.out.println("CloseAllCommand.execute() - done");
  }
  //--------------------------------------------------------------------------------
  // закрыть все окна, которые были открыты при открыти схемы
  private void closeAllInternalFrames()
  { if(mdiMain.kisSelectFrame != null) // окно, содержащее информацию о характеристиках и ценах оборудования
    { mdiMain.kisSelectFrame.dispose();
      mdiMain.kisSelectFrame = null; // dispose() только освбождает ресурсы, но окно продолжает существовать
    }
    if(mdiMain.iterHistFrame != null) // окно графика хода оптимизации
    { mdiMain.iterHistFrame.dispose();
      mdiMain.iterHistFrame = null;
    }
    if(mdiMain.paramsFrame != null) // окно задания параметров оптимизации
    { mdiMain.paramsFrame.dispose();
      mdiMain.paramsFrame = null;
    }
    if(mdiMain.solutionFrame != null) // окно подробной нитки маршрута одного из решений
    { mdiMain.solutionFrame.dispose();
      mdiMain.solutionFrame = null;
    }
    if(mdiMain.nodesModeFrame != null) // окно задания режимов узлов ( fixed , active )
    { mdiMain.nodesModeFrame.dispose();
      mdiMain.nodesModeFrame = null;
    }
    if(mdiMain.ribsModeFrame != null) // окно задания режимов рёбер ( active )
    { mdiMain.ribsModeFrame.dispose();
      mdiMain.ribsModeFrame = null;
    }
    if(mdiMain.schemeFrame != null) // окно отображения схемы
    { mdiMain.schemeFrame.dispose();
      mdiMain.schemeFrame = null;
    }
    if(mdiMain.mapFrame != null) // окно отображения карты
    { //mdiMain.mapFrame.dispose();
      mdiMain.mapFrame = null;
    }
  }
  //--------------------------------------------------------------------------------
  // сделать неактивными некоторые пункты меню (кнопки делаются неактивными по событию close_all)
  private void disableSomeMenuItems( ApplicationModel aModel )
  {
//      aModel.setAllItemsEnabled(false);
//      aModel.enable("menuSession");
//      aModel.enable("menuSessionNew");
//      aModel.enable("menuSessionConnection");
      aModel.enable("menuExit");
      aModel.enable("menuView");
      aModel.enable("menuHelp");
      aModel.enable("menuHelpAbout");

      aModel.enable("menuScheme"); // меню "Схема"
      aModel.disable("menuMapOpen");
      aModel.disable("menuSchemeOpen");
      aModel.disable("menuSchemeSave");
      aModel.disable("menuSchemeSaveAs");
      aModel.disable("menuLoadSm");
      aModel.disable("menuClearScheme");
      aModel.disable("menuSchemeClose");
      aModel.enable("menuView"); // меню "вид"
      aModel.disable("menuViewMap");
      aModel.disable("menuViewScheme");
      aModel.disable("menuViewMapElProperties");
      aModel.disable("menuViewSchElProperties");
      aModel.disable("menuViewSolution");
      aModel.disable("menuViewKIS");
       aModel.disable("menuViewMode");
      aModel.disable("menuViewParams");
      aModel.disable("menuViewGraph");
      aModel.disable("menuViewShowall");
      aModel.enable("menuOptimize"); // меню "оптимизация"
      aModel.disable("menuOptimizeStart");
      aModel.disable("menuOptimizeCriteria");
      aModel.disable("menuOptimizeMode");
      aModel.disable("menuOptimizeStop");
      aModel.disable("menuReportCreate");

      aModel.fireModelChanged("");
  }
  //-----------------------------------------------------------------------------------------
}
//========================================================================================================