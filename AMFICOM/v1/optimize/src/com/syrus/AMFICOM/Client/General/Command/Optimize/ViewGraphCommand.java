// �������� ������� ���� ������� �����������
package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// ������� "����������� ������ ���� �������� �����������"
public class ViewGraphCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	private OpticalOptimizerContext optimizerContext;

	ApplicationContext aContext;
	JDesktopPane desktop;
	//--------------------------------------------------------------------------
	public ViewGraphCommand(){}
	//--------------------------------------------------------------------------
	public ViewGraphCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext,
														OpticalOptimizerContext optimizerContext, OptimizeMDIMain optimizeMDIMain)
	{ this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.optimizerContext = optimizerContext;
		this.mdiMain = optimizeMDIMain;
	}
	//--------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher( (Dispatcher)value );
		else if(field.equals("aContext"))
			setApplicationContext( (ApplicationContext)value );
		else if(field.equals("optimizerContext"))
			optimizerContext = (OpticalOptimizerContext)value;
	}
	//--------------------------------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
	}
	//--------------------------------------------------------------------------
	public void setApplicationContext(ApplicationContext aContext)
	{	this.aContext = aContext;
	}
	//--------------------------------------------------------------------------
	public Object clone()
	{	return new ViewGraphCommand(dispatcher, desktop, aContext, optimizerContext, mdiMain);
	}
	//--------------------------------------------------------------------------
	public void execute()
	{ if(optimizerContext == null)
	return;
		if(mdiMain.iterHistFrame == null)
		{	System.out.println("Starting iteration history frame");
			mdiMain.iterHistFrame = new IterationsHistoryFrame(dispatcher, optimizerContext, mdiMain);
			desktop.add(mdiMain.iterHistFrame);
			mdiMain.iterHistFrame.setVisible(true);
		}
		else //���� ��� �������, �� ������ ����������� �� ���� ����� ������
		{ mdiMain.iterHistFrame.place();
			mdiMain.iterHistFrame.toFront();
		}

		// ��������, ���� ���� ���������� �� ���� ������ �����, ������� ���������� ���������
		if(mdiMain.nodesModeFrame != null)
		{  //mdiMain.nodesModeFrame.place();// ������� �� ������������ ����������� ������� !!
		}


	}
	//--------------------------------------------------------------------------
}