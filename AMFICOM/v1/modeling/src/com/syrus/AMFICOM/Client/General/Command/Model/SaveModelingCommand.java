package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.SetSort;
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

	public static ModelingType getModelingType(String codename)
	{
		StorableObjectCondition aTypeCondition = new StringFieldCondition(
				codename,
				ObjectEntities.MODELINGTYPE_ENTITY_CODE,
				StringFieldSort.STRINGSORT_BASE);

		try
		{
			List aTypes =
				MeasurementStorableObjectPool.getStorableObjectsByCondition(aTypeCondition, true);
			for (Iterator it = aTypes.iterator(); it.hasNext();)
			{
				ModelingType type = (ModelingType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching ModelingType.");
			ex.printStackTrace();
		}

		return null;
	}

	public void execute()
	{
		this.checker = new Checker(aContext.getDataSource());
		if(!checker.checkCommand(checker.saveReflectoModeling))
			return;

		BellcoreStructure bs = (BellcoreStructure )Pool.get("bellcorestructure", traceid);
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
					getModelingType(ModelingType.CODENAME_DADARA),
					new Identifier(bs.monitoredElementId),
					name,
					argumentSet);

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