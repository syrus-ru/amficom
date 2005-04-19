package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.*;

public class AddTraceFromDatabaseCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public AddTraceFromDatabaseCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new AddTraceFromDatabaseCommand(aContext);
	}

	public void execute()
	{

		try
		{
			Checker checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(Checker.loadReflectogrammFromDB))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		ReflectogrammLoadDialog dialog;
		JFrame parent = Environment.getActiveWindow();
		if(Heap.getRLDialogByKey(parent.getName()) != null)
		{
			dialog = Heap.getRLDialogByKey(parent.getName());
		}
		else
		{
			dialog = new ReflectogrammLoadDialog (aContext);
			Heap.setRLDialogByKey(parent.getName(), dialog);
		}
		
		if(dialog.showDialog() == JOptionPane.CANCEL_OPTION)
			return;
		
		Result result = dialog.getResult();
		if (result == null)
			return;

		BellcoreStructure bs = null;

		SetParameter[] parameters = result.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT))
			bs.title = ((Measurement)result.getAction()).getName();
		Heap.putSecondaryTraceByKey(bs.title, bs);
		Heap.setCurrentTrace(bs.title);
	}
}
