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

		//  ������� ����� �� ���������� �����
		if( mdiMain.nodesModeFrame == null ) //����
		{  mdiMain.nodesModeFrame = new NodesOptimizePropertiesFrame(aContext, mdiMain);
			 desktop.add(mdiMain.nodesModeFrame);//�� ��� ���� �� ��������� �� �������, ������� ��������� �������
			 mdiMain.nodesModeFrame.setVisible(true);
		}
		else
		{  mdiMain.nodesModeFrame.place();
			 mdiMain.nodesModeFrame.toFront();
		}
		if( mdiMain.ribsModeFrame == null ) //����
		{	 mdiMain.ribsModeFrame = new RibsOptimizePropertiesFrame(aContext, mdiMain);
			 desktop.add(mdiMain.ribsModeFrame);//�� ��� ���� �� ��������� �� �������, ������� ��������� �������
			 mdiMain.ribsModeFrame.setVisible(true);
		}
		else
		{  mdiMain.ribsModeFrame.place();
			 mdiMain.ribsModeFrame.toFront();
		}
/*
		//----------------------------
		//  ������� ����� �o ���������� ��������� �����
		ViewMapPropertiesCommand viewMapPropertiesCommand = new ViewMapPropertiesCommand(desktop, aContext);
		viewMapPropertiesCommand.execute();// �� ��� ���� ��������� �� �������, ������� ������ desktop.add(...) �� ����

		Dimension dim3 = new Dimension(desktop.getWidth(), desktop.getHeight());
		viewMapPropertiesCommand.frame.setLocation(dim3.width * 4 / 5, 0);
		viewMapPropertiesCommand.frame.setSize(dim3.width / 5, dim3.height / 4);
*/
	}

}