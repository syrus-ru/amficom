package com.syrus.AMFICOM.Client.General.Command.Optimize;
// команда вызывается при изменении режима тестирования волокон (обычное\встречное  тестирование)

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

//========================================================================================================
public class SetOptimizeModeCommand extends VoidCommand
{
  private Dispatcher dispatcher;
  private ApplicationContext aContext;
  private JDesktopPane desktop;
  private OptimizeMDIMain mdiMain;
  private int new_mode;
  //--------------------------------------------------------------------------------
  public SetOptimizeModeCommand(){}
  //--------------------------------------------------------------------------------
  public SetOptimizeModeCommand(  int new_mode, Dispatcher dispatcher, JDesktopPane desktop,
                                  ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain )
  {	 this.dispatcher = dispatcher;
     this.desktop = desktop;
     this.aContext = aContext;
     this.mdiMain = optimizeMDIMain;
     this.new_mode = new_mode;
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
  { return new SetOptimizeModeCommand(new_mode, dispatcher, desktop, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  { mdiMain.optimizerContext.optimize_mode = new_mode;
    ApplicationModel aModel = aContext.getApplicationModel();
    if(new_mode == 0) // односторонняя оптимизация
    { aModel.setSelected("menuOptimizeModeUnidirect", true);
      aModel.setSelected("menuOptimizeModeBidirect", false);
      aModel.fireModelChanged("");
    }
    if(new_mode == 1) // двусторонняя оптимизация
    { aModel.setSelected("menuOptimizeModeBidirect", true);
      aModel.setSelected("menuOptimizeModeUnidirect", false);
      aModel.fireModelChanged("");
    }
  }
  //--------------------------------------------------------------------------------
}
//========================================================================================================
