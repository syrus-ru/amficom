package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.InitialAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

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
			String error = "������ ��� �������� ��������������� ��������������.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "������", JOptionPane.OK_OPTION);
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
					Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
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
			bs.title = ((Measurement )res.getAction()).getName();
		Pool.put("bellcorestructure", "primarytrace", bs);

		new InitialAnalysisCommand().execute();

		dispatcher.notify(new RefChangeEvent("primarytrace", RefChangeEvent.CLOSE_EVENT));
		dispatcher.notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
	}
}

