package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

public class ViewShowallCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	OptimizeMDIMain mdiMain;
	//--------------------------------------------------------------------------------
	public ViewShowallCommand(){}
	//--------------------------------------------------------------------------------
        public ViewShowallCommand (  Dispatcher dispatcher,
                                     JDesktopPane desktop,
                                     ApplicationContext aContext,
                                     OptimizeMDIMain    optimizeMDIMain )
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
		 { setApplicationContext((ApplicationContext )value);
     }
	}
	//--------------------------------------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
	}
	//--------------------------------------------------------------------------------
	public void setApplicationContext(ApplicationContext aContext)
	{	this.aContext = aContext;
	}
	//--------------------------------------------------------------------------------
	public Object clone()
	{	return new ViewShowallCommand(dispatcher, desktop, aContext, mdiMain);
	}
	//--------------------------------------------------------------------------------
	public void execute()
	{	 System.out.println("ViewShowallCommand.execute() - starting ...");
		 // эмулируем выбор соответствующих пунктов меню
		 // if(mdiMain.kisSelectFrame!=null ) {mdiMain.kisSelectFrame.place();} else {System.out.println("mdiMain.kisSelectFrame=null");}
		 new ViewKISCommand(dispatcher, desktop, aContext, mdiMain).execute();
     new ViewParamCommand(dispatcher, desktop, aContext,  mdiMain.optimizerContext, mdiMain).execute();
		 new ViewSolutionCommand(dispatcher, desktop, aContext, mdiMain).execute();
		 new ViewGraphCommand(dispatcher, desktop, aContext, mdiMain.optimizerContext, mdiMain).execute();
		 new ViewModeCommand(dispatcher, desktop, aContext, mdiMain).execute();
		 new ViewSchemeCommand(dispatcher, desktop, aContext, new MapOptimizeApplicationModelFactory(), mdiMain).execute();
		 new ViewMapCommand(dispatcher, desktop, aContext, new MapOptimizeApplicationModelFactory(), mdiMain ).execute();

		 dispatcher.notify(new OperationEvent(this, 0, "showallevent"));// используем для выравнивания всех окон с учётом того, что они все должны разместиться
		 System.out.println("ViewShowallCommand.execute() - done");
	}
	//--------------------------------------------------------------------------------
}
