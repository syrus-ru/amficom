package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

public class ViewParamCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	OptimizeMDIMain mdiMain;
	OpticalOptimizerContext optoptContext;

		//--------------------------------------------------------------------------------
	public ViewParamCommand(){}
		//--------------------------------------------------------------------------------
		public ViewParamCommand (Dispatcher         dispatcher,
														 JDesktopPane       desktop,
														 ApplicationContext aContext,
														 OpticalOptimizerContext   optoptContext,
														 OptimizeMDIMain    optimizeMDIMain)
	{	this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.optoptContext = optoptContext;
		this.mdiMain = optimizeMDIMain;
	}
		//--------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
		else if(field.equals("optimizerContext"))
			this.optoptContext = (OpticalOptimizerContext) value;
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
	public void setOptimizerContext(OpticalOptimizerContext optoptContext)
	{	this.optoptContext = optoptContext;
	}
		//--------------------------------------------------------------------------------
	public Object clone()
	{	return new ViewParamCommand(dispatcher, desktop, aContext, optoptContext, mdiMain);
	}
	//--------------------------------------------------------------------------------
	public void execute()
	{	 System.out.println("Starting Param frame");
		 // если уже создано, то второе не открываем
		 if( mdiMain.paramsFrame == null )
		 { mdiMain.paramsFrame = new OpticalOptimizationParamsFrame(dispatcher, optoptContext, mdiMain);
			 desktop.add(mdiMain.paramsFrame);
			 mdiMain.paramsFrame.setVisible(true);
		 }
		 else //иначе просто располагаем на своём месте заново и выводим поверх других
		 { mdiMain.paramsFrame.place();
			 mdiMain.paramsFrame.toFront();
		 }
	}
}