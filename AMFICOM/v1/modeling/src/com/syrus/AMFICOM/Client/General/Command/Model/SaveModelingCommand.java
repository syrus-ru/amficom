package com.syrus.AMFICOM.Client.General.Command.Model;

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

import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

public class SaveModelingCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	String traceid;
	Checker checker;

	public SaveModelingCommand()
	{
	}

	public SaveModelingCommand(Dispatcher dispatcher, ApplicationContext aContext, String id)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
		this.traceid = id;
	}

	public Object clone()
	{
		return new SaveModelingCommand(dispatcher, aContext, traceid);
	}

	public void execute()
	{
		this.checker = new Checker(this.aContext.getDataSourceInterface());
		if(!checker.checkCommand(checker.saveReflectoModeling))
			return;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if(bs == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		String modelingId = dataSource.GetUId(Modeling.typ);


		String inicialName = "Модель маршрута <"+bs.monitored_element_id+">, id: "+modelingId;

		if(Pool.get("names", "savedNames") == null)
		{
			Pool.put("names", "savedNames", new Vector());
		}

		Vector v = (Vector)Pool.get("names", "savedNames");
		if(v.contains(inicialName))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Модель под именем <"+inicialName+">"+" Уже была сохранена", "Ошибка", JOptionPane.OK_OPTION);
		}
		else
		{
			v.add(inicialName);
		}


		String s = inicialName;
		Modeling m = new Modeling(modelingId);
		m.name = s;
		m.type_id = "dadara";

		String path_id = (String)Pool.get("activecontext", "activepathid");

		m.scheme_path_id = path_id;
		m.user_id = aContext.getSessionInterface().getUserId();
		m.domain_id = aContext.getSessionInterface().getDomainId();
		Pool.put("modeling", m.getId(), m);

		ModelingType mt = (ModelingType)Pool.get(ModelingType.typ, m.type_id);
		Result r = new Result(m.getId(), "modeling", "", aContext.getSessionInterface().getUserId(), dataSource.GetUId("result"));
		ActionParameterType apt;
		apt = (ActionParameterType )mt.sorted_parameters.get("reflectogramm");
		Pool.put(apt.typ, apt.id, apt);

//    GlobalParameterType gpt = new GlobalParameterType();
		Parameter resparam1 = new Parameter(
				dataSource.GetUId(Parameter.typ),
				apt.getId(),
				new BellcoreWriter().write(bs),
				"reflectogramm",
				"reflectogramm");
		resparam1.setTransferableFromLocal();
		r.addParameter(resparam1);
		r.setTransferableFromLocal();
		m.setTransferableFromLocal();




		Pool.put("result", r.id, r);

		dataSource.SaveModeling(m.getId(), r.getId());

		JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Модель сохранена под именем <"+inicialName+">", "Модель сохранена", JOptionPane.INFORMATION_MESSAGE);
		return;
	}
}