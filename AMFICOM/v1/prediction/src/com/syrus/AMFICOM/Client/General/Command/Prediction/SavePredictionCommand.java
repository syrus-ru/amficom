package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.ActionParameterType;
import com.syrus.AMFICOM.Client.Resource.Result.Modeling;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Test.ModelingType;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.ByteArray;

public class SavePredictionCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	String traceid;
	Checker checker;

	public SavePredictionCommand()
	{
	}

	public SavePredictionCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SavePredictionCommand(dispatcher, aContext);
	}

	public void execute()
	{
		traceid = (String)Pool.get("predictedModel", "id");
		if(traceid == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if(bs == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		ReflectoEventStatistics refStat = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
		if(refStat == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		if(Pool.get("predictionTime", bs.title) == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if(Pool.get("theModels", "savedModels")==null)
		{
			Pool.put("theModels", "savedModels", new Vector());
		}

		Vector savedModels = (Vector)Pool.get("theModels", "savedModels");
		if(savedModels.contains(bs.title))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Выбранная модель предсказания уже была сохранена.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		else
		{
			savedModels.add(bs.title);
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
//		String s = (String)JOptionPane.showInputDialog(null, "Название модели:", "", JOptionPane.OK_CANCEL_OPTION, null, new Object[] {bs.title}, bs.title);
//    String s = JOptionPane.showInputDialog(null, "Название модели:", bs.title, JOptionPane.OK_CANCEL_OPTION);

//    if (s == null || s.equals(""))
//			return;


		Modeling m = new Modeling(dataSource.GetUId(Modeling.typ));

		m.setName(bs.title);

		m.setTypeId("optprognosis");
		m.setDomainId(dataSource.getSession().getDomainId());

		long predictionTime = ((Long)Pool.get("predictionTime", bs.title)).longValue();


//		String path_id = (String)Pool.get("activecontext", "activepathid");

		m.setSchemePathId(refStat.getPathID());
		m.setUserId(aContext.getSessionInterface().getUserId());
		m.setModified(new Date().getTime());
		m.setTransferableFromLocal();

		ModelingType mt = (ModelingType)Pool.get(ModelingType.typ, m.getTypeId());
		ActionParameterType apt;

		apt = (ActionParameterType )mt.getSortedArguments().get("time_start");
		try
		{
			Parameter resparam3 = new Parameter(
					dataSource.GetUId(Parameter.typ),
					apt.getId(),
					new ByteArray(refStat.getLowerTime()).getBytes(),
					"time_start",
					"long");
			resparam3.setTransferableFromLocal();
			m.addArgument(resparam3);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		apt = (ActionParameterType )mt.getSortedArguments().get("time_end");
		try
		{
			Parameter resparam4 = new Parameter(
					dataSource.GetUId(Parameter.typ),
					apt.getId(),
					new ByteArray(refStat.getUpperTime()).getBytes(),
					"time_end",
					"long");
			resparam4.setTransferableFromLocal();
			m.addArgument(resparam4);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		m.setTransferableFromLocal();
		Pool.put(Modeling.typ, m.getId(), m);

		Result r = new Result(m.getId(), "modeling", "", aContext.getSessionInterface().getUserId(), dataSource.GetUId("result"));
		apt = (ActionParameterType )mt.getSortedParameters().get("reflectogramm");
		Parameter resparam1 = new Parameter(
				dataSource.GetUId(Parameter.typ),
				apt.getId(),
				new BellcoreWriter().write(bs),
				"reflectogramm",
				"reflectogramm");
		resparam1.setTransferableFromLocal();
		r.addParameter(resparam1);

		apt = (ActionParameterType )mt.getSortedParameters().get("time");


		try
		{
			Parameter resparam2 = new Parameter(
					dataSource.GetUId(Parameter.typ),
					apt.getId(),
					new ByteArray(predictionTime).getBytes(),
					"time",
					"long");
			resparam2.setTransferableFromLocal();
			r.addParameter(resparam2);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		Pool.put("result", r.getId(), r);

		dataSource.SaveModeling(m.getId(), r.getId());

		JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Модель сохранена под именем <"+bs.title+">", "Модель сохранена", JOptionPane.INFORMATION_MESSAGE);
		return;
	}
}
