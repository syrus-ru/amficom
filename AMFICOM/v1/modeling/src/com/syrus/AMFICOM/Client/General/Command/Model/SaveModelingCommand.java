package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.corba.*;
import com.syrus.io.*;

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
		this.checker = new Checker(aContext.getDataSource());
		if(!checker.checkCommand(checker.saveReflectoModeling))
			return;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if(bs == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при сохранении модели.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);

		String name = "Модель маршрута <" + bs.monitoredElementId + ">";
		List meIds = new ArrayList(1);
		meIds.add(bs.monitoredElementId);

		SetParameter[] parameters = new SetParameter[0];
/*		ParameterType ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.REFLECTOGRAMMA);
		parameters[0] = new SetParameter(
				IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE),
				ptype,
				new BellcoreWriter().write(bs));*/

		try {
			Set argumentSet = Set.createInstance(
					userId,
					SetSort.SET_SORT_ANALYSIS_CRITERIA,
					"",
					parameters,
					meIds);

			Modeling m = Modeling.createInstance(
					userId,
					new Identifier(bs.schemePathId),
					new Identifier(bs.monitoredElementId),
					name,
					argumentSet,
					ModelingSort.MODELINGSORT_MODELING);

			parameters = new SetParameter[1];
			ParameterType ptype = AnalysisUtil.getParameterType(userId,
					ParameterTypeCodenames.REFLECTOGRAMMA);
			parameters[0] = SetParameter.createInstance(
					ptype,
					new BellcoreWriter().write(bs));

			m.createResult(
					userId,
					parameters);

			MeasurementStorableObjectPool.putStorableObject(m);
			MeasurementStorableObjectPool.flush(true);

//		m.setTypeId("dadara");
//		Pool.put("modeling", m.getId().getIdentifierString(), m);

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Модель сохранена под именем :" + name,
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch (ApplicationException ex) {
		}
	}
}