package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.InitialAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.DomainCondition;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import com.syrus.io.*;

public class LoadModelingCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	private Checker checker;

	public LoadModelingCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new LoadModelingCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.loadReflectogrammFromDB))
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
		if(Pool.get("dialog", parent.getName()) != null)
		{
			dialog = (ReflectogrammLoadDialog)Pool.get("dialog", parent.getName());
		}
		else
		{
			dialog = new ReflectogrammLoadDialog (aContext);
			Pool.put("dialog", parent.getName(), dialog);
		}

		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (!(dialog.getResult() instanceof Result))
			return;

		Result res = (Result)dialog.getResult();

		if(res == null)
		{
			String error = "Ошибка при загрузке смоделированной рефлектограммы.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = null;

		SetParameter[] parameters =  res.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		if (res.getSort().equals(ResultSort.RESULT_SORT_MODELING))
		{
			Modeling m = (Modeling)res.getAction();
			bs.title = m.getName();

			try {
				MonitoredElement me = (MonitoredElement)MeasurementStorableObjectPool.
						getStorableObject(m.getMonitoredElementId(), true);

				if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
					List tpathIds = me.getMonitoredDomainMemberIds();
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.SCHEME_PATH_ENTITY_CODE);
					List paths = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = paths.iterator(); it.hasNext(); ) {
						SchemePath sp = (SchemePath)it.next();
						if (tpathIds.contains(sp.pathImpl().getId()))
						{
							Pool.put("activecontext", "activepathid", sp.id());
							break;
						}
					}
				}
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		else
			bs.title = res.getMeasurement().getName();
		Pool.put("bellcorestructure", "primarytrace", bs);

		new InitialAnalysisCommand().execute();

		dispatcher.notify(new RefChangeEvent("primarytrace", RefChangeEvent.CLOSE_EVENT));
		dispatcher.notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
	}
}

