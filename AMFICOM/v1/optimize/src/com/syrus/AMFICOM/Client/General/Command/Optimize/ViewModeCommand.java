package com.syrus.AMFICOM.Client.General.Command.Optimize;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;

public class ViewModeCommand extends VoidCommand
{	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	OptimizeMDIMain mdiMain;
	//--------------------------------------------------------------------------------
	public ViewModeCommand(){}
	//--------------------------------------------------------------------------------
	public ViewModeCommand (   Dispatcher dispatcher, JDesktopPane       desktop,
                                   ApplicationContext aContext, OptimizeMDIMain    optimizeMDIMain )
	{	this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.mdiMain = optimizeMDIMain;

	}
	//--------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else if(field.equals("aContext"))
			setApplicationContext( (ApplicationContext) value);
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
	{	return new ViewModeCommand(dispatcher, desktop, aContext, mdiMain);
	}
	//--------------------------------------------------------------------------------
	public void execute()
	{	 System.out.println("Starting Mode (NodesOptimizePropertiesFrame) frame");

		//  Открыть фрейм со свойствами узлов
		if( mdiMain.nodesModeFrame == null ) //узлы
		{  mdiMain.nodesModeFrame = new NodesOptimizePropertiesFrame(aContext, mdiMain);
			 desktop.add(mdiMain.nodesModeFrame);//он сам себя не добавляет на десктоп, поэтому добавляем вручную
			 mdiMain.nodesModeFrame.setVisible(true);
		}
		else
		{  mdiMain.nodesModeFrame.place();
			 mdiMain.nodesModeFrame.toFront();
		}
		if( mdiMain.ribsModeFrame == null ) //рёбра
		{	 mdiMain.ribsModeFrame = new RibsOptimizePropertiesFrame(aContext, mdiMain);
			 desktop.add(mdiMain.ribsModeFrame);//он сам себя не добавляет на десктоп, поэтому добавляем вручную
			 mdiMain.ribsModeFrame.setVisible(true);
		}
		else
		{  mdiMain.ribsModeFrame.place();
			 mdiMain.ribsModeFrame.toFront();
		}
/*
		//----------------------------
		//  Открыть фрейм сo свойствами элементов карты
		ViewMapPropertiesCommand viewMapPropertiesCommand = new ViewMapPropertiesCommand(desktop, aContext);
		viewMapPropertiesCommand.execute();// он сам себя добавляет на десктоп, поэтому писать desktop.add(...) не надо

		Dimension dim3 = new Dimension(desktop.getWidth(), desktop.getHeight());
		viewMapPropertiesCommand.frame.setLocation(dim3.width * 4 / 5, 0);
		viewMapPropertiesCommand.frame.setSize(dim3.width / 5, dim3.height / 4);
*/
	}

}