package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// ������� �� �������� ���������� ���� ������� �����������
public class ViewSolutionCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	private OptimizeMDIMain mdiMain;
	//-------------------------------------------------------------------------------------------------------------
	public ViewSolutionCommand(){}
	//-------------------------------------------------------------------------------------------------------------
	public ViewSolutionCommand(   Dispatcher             dispatcher,
																JDesktopPane           desktop,
																ApplicationContext     aContext,
																OptimizeMDIMain        mdiMain  )
	{	this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.mdiMain = mdiMain;
	}
	//-------------------------------------------------------------------------------------------------------------
	// ������� ���������� ��� ��������� ��������� ����������, ������� � ������ �������� ���������� ����� ������ ��� ����� ���� ���������� (��������� �����)
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher( (Dispatcher)value );
		else
		if(field.equals("aContext"))
			setApplicationContext( (ApplicationContext)value );
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
	{	return new ViewSolutionCommand(dispatcher, desktop, aContext, mdiMain);
	}
	//-------------------------------------------------------------------------------------------------------------
	public void execute()
	{		System.out.println("Starting Solution frame");
			// ���� ����������� ��������� ����� � ���� �������
			if(mdiMain.solutionFrame == null)
			{  mdiMain.solutionFrame = new ViewSolutionFrame(mdiMain);
				 desktop.add(mdiMain.solutionFrame);
				 mdiMain.solutionFrame.setVisible(true);
			}
		 else //���� ��� �������, �� ������ ����������� �� ���� ����� ������
		 {	mdiMain.solutionFrame.place();
				mdiMain.solutionFrame.toFront();
		 }
	}
	//-------------------------------------------------------------------------------------------------------------
}