package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.corba.*;
import com.syrus.io.*;
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

		if(Pool.get("theModels", "savedModels") == null)
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

//		String s = (String)JOptionPane.showInputDialog(null, "Название модели:", "", JOptionPane.OK_CANCEL_OPTION, null, new Object[] {bs.title}, bs.title);
//    String s = JOptionPane.showInputDialog(null, "Название модели:", bs.title, JOptionPane.OK_CANCEL_OPTION);
//    if (s == null || s.equals(""))
//			return;

		Identifier modelingId = IdentifierPool.generateId(ObjectEntities.MODELING_ENTITY_CODE);
		Identifier userId = new Identifier(aContext.getSessionInterface().getUserId());
		List meIds = new ArrayList(1);
		meIds.add(bs.monitoredElementId);

		try {
			SetParameter[] parameters = new SetParameter[3];

			ParameterType ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.PREDICTION_TIME);
			Long predictionTime = ((Long)Pool.get("predictionTime", bs.title));
			parameters[0] = new SetParameter(
					IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE),
					ptype,
					ByteArray.toByteArray(predictionTime.longValue()));

			ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.PREDICTION_DATA_FROM);
			parameters[1] = new SetParameter(
					IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE),
					ptype,
					ByteArray.toByteArray(refStat.getLowerTime()));

			ptype = AnalysisUtil.getParameterType(userId,	ParameterTypeCodenames.PREDICTION_DATA_TO);
			parameters[2] = new SetParameter(
					IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE),
					ptype,
					ByteArray.toByteArray(refStat.getUpperTime()));

			Set argumentSet = Set.createInstance(
					IdentifierPool.generateId(ObjectEntities.SET_ENTITY_CODE),
					userId,
					SetSort.SET_SORT_ANALYSIS_CRITERIA,
					"",
					parameters,
					meIds);

			String name = bs.title;
			Modeling m = Modeling.createInstance(
					modelingId,
					userId,
					bs.schemePathId,
					bs.monitoredElementId,
					name,
					argumentSet,
					ModelingSort.MODELINGSORT_PREDICTION);

			parameters = new SetParameter[1];
			ptype = AnalysisUtil.getParameterType(userId,
					ParameterTypeCodenames.REFLECTOGRAMMA);
			parameters[0] = new SetParameter(
					IdentifierPool.generateId(ObjectEntities.SETPARAMETER_ENTITY_CODE),
					ptype,
					new BellcoreWriter().write(bs));

			m.createResult(
					IdentifierPool.generateId(ObjectEntities.RESULT_ENTITY_CODE),
					userId,
					parameters);

			MeasurementStorableObjectPool.putStorableObject(m);
			MeasurementStorableObjectPool.flush(true);

//		m.setTypeId("optprognosis");

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Модель сохранена под именем " + bs.title,
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch (ApplicationException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка сохранения прогнозируемой рефлектограммы на сервере",
					"Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка сохранения параметров прогнозирования",
					"Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
