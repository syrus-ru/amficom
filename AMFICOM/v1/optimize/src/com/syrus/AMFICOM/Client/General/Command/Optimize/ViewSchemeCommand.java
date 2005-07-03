package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Config.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// ������� ���� "������� ���� ��������� �����"
//----------------------------------------------------------------------------------------------------------------
public class ViewSchemeCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;
	OptimizeMDIMain mdiMain;
	//-------------------------------------------------------------------------------------------------------------
	public ViewSchemeCommand(){}
	//-------------------------------------------------------------------------------------------------------------
	public ViewSchemeCommand( Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory, OptimizeMDIMain mdiMain )
	{	this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
		this.mdiMain = mdiMain;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setApplicationContext(ApplicationContext aContext)
	{	this.aContext = aContext;
	}
	//-------------------------------------------------------------------------------------------------------------
	public Object clone()
	{	return new ViewSchemeCommand(dispatcher, desktop, aContext, factory, mdiMain);
	}
	//-------------------------------------------------------------------------------------------------------------
	public void execute()
	{
		if(mdiMain == null)
		{ System.err.println("ViewSchemeCommand().execute(): mdiMain = null. aborting ..." );
	return;
		}
		//���� ���� ��������� ����� ��� �� �������, �� �������������� ��� ����� ������
		if(mdiMain.schemeFrame == null) // ���� ��� �� �������, �� ������
		{  mdiMain.schemeFrame = new ViewSchemeFrame(mdiMain);
       desktop.add(mdiMain.schemeFrame);
		}
		//������������� ������ ���� ��������� �����
		mdiMain.schemeFrame.place();
		mdiMain.schemeFrame.setVisible(true);
		mdiMain.schemeFrame.toFront();
		dispatcher.notify(new OperationEvent (this, 0, "view_scheme_command_event"));
    // ������������� ��������� ���� �� ���������� �������� �����
    new ViewSchElPropFrameCommand(dispatcher, aContext, mdiMain).execute();
	}
	//-------------------------------------------------------------------------------------------------------------
}
