package com.syrus.AMFICOM.Client.General.Command.Optimize;

import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// команда "закрыть всё" - закрывает все окна, кроме главного
//========================================================================================================
public class LoadPricelistCommand extends VoidCommand
{ private Dispatcher dispatcher;
  private ApplicationContext aContext;
  private JDesktopPane desktop;
  private OptimizeMDIMain mdiMain;
  //--------------------------------------------------------------------------------
  public LoadPricelistCommand(){}
  //--------------------------------------------------------------------------------
  public LoadPricelistCommand (  Dispatcher dispatcher, JDesktopPane desktop,
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
  { return new LoadPricelistCommand(dispatcher, desktop, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  {	System.out.println("LoadPricelistCommand.execute() starting ...");
    mdiMain.kisSelectFrame.menuFileOpenExecute();
    System.out.println("LoadPricelistCommand.execute() - done");
  }
  //--------------------------------------------------------------------------------
}
//========================================================================================================
