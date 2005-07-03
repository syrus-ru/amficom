package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// открыть окно для задания цен оборудования
//==================================================================================================
public class ViewKISCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	ApplicationContext aContext;
	JDesktopPane desktop;
	public KISFrame frame;
	//---------------------------------------------------------
	public ViewKISCommand(){}
	//---------------------------------------------------------
	public ViewKISCommand(Dispatcher dispatcher,
												JDesktopPane desktop,
												ApplicationContext aContext,
												OptimizeMDIMain mdiMain)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.mdiMain = mdiMain;
	}
	//---------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}
	//---------------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
	}
	//---------------------------------------------------------
	public void setApplicationContext(ApplicationContext aContext)
	{	this.aContext = aContext;
	}
	//---------------------------------------------------------
	public Object clone()
	{	return new ViewKISCommand(dispatcher, desktop, aContext, mdiMain);
	}
	//---------------------------------------------------------
	public void execute()
	{	// это окно единственное, которое открывается не по команде из меню, а автоматически при открытии карты\схемы
		if(mdiMain.kisSelectFrame != null )
		{  mdiMain.kisSelectFrame.place();
			 mdiMain.kisSelectFrame.toFront();
		}

		else { System.out.println("mdiMain.kisSelectFrame = null");}
	}
	//---------------------------------------------------------
}
//==================================================================================================
