package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// команда "закрыть всё" - закрывает все окна, кроме главного
//========================================================================================================
public class SaveasPricelistCommand extends VoidCommand
{ private Dispatcher dispatcher;
  private ApplicationContext aContext;
  private JDesktopPane desktop;
  private OptimizeMDIMain mdiMain;
  //--------------------------------------------------------------------------------
  public SaveasPricelistCommand(){}
  //--------------------------------------------------------------------------------
  public SaveasPricelistCommand (  Dispatcher dispatcher, JDesktopPane desktop,
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
  { return new SaveasPricelistCommand(dispatcher, desktop, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  {	System.out.println("SaveasPricelistCommand.execute() starting ...");
    mdiMain.kisSelectFrame.menuFileSaveAsExecute();
    System.out.println("SaveasPricelistCommand.execute() - done");
  }
  //--------------------------------------------------------------------------------
}
//========================================================================================================
