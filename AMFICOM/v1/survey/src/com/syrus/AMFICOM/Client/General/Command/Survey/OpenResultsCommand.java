package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Survey.Result.*;

public class OpenResultsCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public ResultFrame frame;

	public OpenResultsCommand()
	{
		 // nothing
	}

	public OpenResultsCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext)value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.reflectogrammObserving))
		{
			return;
		}

//		DataSourceInterface dataSource = aC.getDataSourceInterface();
//		if(dataSource == null);
//		//			return;
		System.out.println("Starting Results frame");

		//		dataSource.GetResults();

		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof ResultFrame)
				frame = (ResultFrame)comp;
		}
		if (frame == null)
		{
			frame = new ResultFrame(aContext);
			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 3 / 5, 0);
			frame.setSize(dim.width * 2 / 5, dim.height / 4);
		}

		frame.show();
		frame.toFront();

	/*	Enumeration enum = Pool.getHash("result").elements();
		Result result = (Result)enum.nextElement();
		while (enum.hasMoreElements())
			result = (Result)enum.nextElement();
		//frame.setResult(result);
		frame.showActiveResult();*/
	}

}

