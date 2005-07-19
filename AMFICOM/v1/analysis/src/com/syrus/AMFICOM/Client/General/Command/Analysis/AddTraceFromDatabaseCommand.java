package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class AddTraceFromDatabaseCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public AddTraceFromDatabaseCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new AddTraceFromDatabaseCommand(this.aContext);
	}

	@Override
	public void execute()
	{
		ReflectogrammLoadDialog dialog;
		JFrame parent = Environment.getActiveWindow();
		if(Heap.getRLDialogByKey(parent.getName()) != null)
		{
			dialog = Heap.getRLDialogByKey(parent.getName());
		} else
		{
			dialog = new ReflectogrammLoadDialog (this.aContext);
			Heap.setRLDialogByKey(parent.getName(), dialog);
		}
		
		if(dialog.showDialog() == JOptionPane.CANCEL_OPTION)
			return;
		
		Result result1 = dialog.getResult();
		if (result1 == null)
			return;

		BellcoreStructure bs = null;

		Parameter[] parameters = result1.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodename.REFLECTOGRAMMA.stringValue()))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		if (result1.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT))
			bs.title = ((Measurement)result1.getAction()).getName();
		Heap.putSecondaryTraceByKey(bs.title, bs);
		Heap.setCurrentTrace(bs.title);
	}
}
