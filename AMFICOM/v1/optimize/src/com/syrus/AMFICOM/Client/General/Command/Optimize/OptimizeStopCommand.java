// класс выполнения команды "Стоп" из меню команд фрейма оптимизации сети
package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class OptimizeStopCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private OpticalOptimizerContext optimizerContext;
  private OptimizeMDIMain mdiMain;
	//---------------------------------------------------------------------------------------
	public OptimizeStopCommand(){}
	//---------------------------------------------------------------------------------------
	public OptimizeStopCommand(Dispatcher dispatcher, OpticalOptimizerContext optimizerContext, OptimizeMDIMain mdiMain)
	{  this.dispatcher = dispatcher;
		 this.optimizerContext = optimizerContext;
     this.mdiMain = mdiMain;
	}
	//---------------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{
		if(field.equals("optimizerContext"))
			this.optimizerContext = (OpticalOptimizerContext)value;
	}
	//---------------------------------------------------------------------------------------
	public Object clone()
	{  return new OptimizeStopCommand(dispatcher, optimizerContext, mdiMain);
	}
	//---------------------------------------------------------------------------------------
	public void execute()
	{  System.out.println("Stopping Dll...");
		 // проверка, был ли создан контекст при открытии карты
		 if (optimizerContext == null)
     { System.err.println("OptimizeStopCommand.execute(): optimizerContext = null. Nothing to stop");
  return;
     }
		 optimizerContext.Stop();
     mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("оптимизация приостановлена"));
		 dispatcher.notify(new OperationEvent (this, 0, "stopevent"));

     System.out.println("Stopping Dll - done");
	}
 //---------------------------------------------------------------------------------------
}