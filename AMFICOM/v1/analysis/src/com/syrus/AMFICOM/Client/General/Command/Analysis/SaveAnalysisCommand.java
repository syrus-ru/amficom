package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.ActionParameterType;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;

import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.io.ByteArrayCollector;

public class SaveAnalysisCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	String traceid;

	public SaveAnalysisCommand()
	{
	}

	public SaveAnalysisCommand(Dispatcher dispatcher, ApplicationContext aContext, String id)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
		this.traceid = id;
	}

	public Object clone()
	{
		return new SaveAnalysisCommand(dispatcher, aContext, traceid);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
//		dataSource.LoadISM();

	//	MEDialog dialog = new MEDialog();
	//	dialog.setModal(true);
	//	dialog.show();

		//if(dialog.ret_code == 0)
		//	return;
		//String me = dialog.me;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs.monitored_element_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noMonitoredElementError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}
		if (bs.test_setup_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noTestSetupError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		RefAnalysis refanalysis = (RefAnalysis)Pool.get("refanalysis", traceid);
		ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", traceid);
		TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);

		String s = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.String("newname"),
				LangModelAnalyse.String("analysis"),
				JOptionPane.OK_CANCEL_OPTION);
		if (s == null || s.equals(""))
			return;

		Analysis a = new Analysis(dataSource.GetUId("analysis"));
		a.name = s;
		//SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
		//a.name = sdf.format(new Date(System.currentTimeMillis()));
		a.type_id = "dadara";
		a.criteria_set_id = ts.criteria_set_id;
		a.result_ids = new String[0];
		AnalysisType atype = (AnalysisType)Pool.get(AnalysisType.typ, a.type_id);

		byte[] value = new byte[0];

		ActionParameterType apt = (ActionParameterType )atype.sorted_arguments.get("reflectogramm");
		Parameter arg1 = new Parameter();
		arg1.id = dataSource.GetUId(Parameter.typ);
		arg1.parameter_type_id = "reflectogramm";
		arg1.type_id = apt.getId();
		arg1.value = new BellcoreWriter().write(bs);
		arg1.codename = "reflectogramm";
		a.addArgument(arg1);

		a.user_id = aContext.getSessionInterface().getUserId();
		a.monitored_element_id = bs.monitored_element_id;

		a.setTransferableFromLocal();

		Pool.put("analysis", a.getId(), a);

		Result r = new Result(a.getId(), "analysis", "", aContext.getSessionInterface().getUserId(), dataSource.GetUId("result"));

		ByteArrayCollector bac = new ByteArrayCollector();
		for (int i = 0; i < refanalysis.events.length; i++)
		{
			byte [] b = refanalysis.events[i].toByteArray();
			bac.add(b);
		}
		apt = (ActionParameterType )atype.sorted_parameters.get("traceevents");

		Parameter resparam1 = new Parameter();
		resparam1.id = dataSource.GetUId(Parameter.typ);
		resparam1.parameter_type_id = "traceeventarray";
		resparam1.type_id = apt.getId();
		resparam1.value = bac.encode();
		resparam1.codename = "traceevents";

		bac = new ByteArrayCollector();
		for (int i = 0; i < refanalysis.concavities.length; i++)
			bac.add(refanalysis.concavities[i].toByteArray());

		apt = (ActionParameterType )atype.sorted_parameters.get("dadara_event_array");

		Parameter resparam2 = new Parameter();
		resparam2.id = dataSource.GetUId(Parameter.typ);
		resparam2.parameter_type_id = apt.parameter_type_id;
		resparam2.type_id = apt.getId();
		resparam2.value = ReflectogramEvent.toByteArray(ep);
		resparam2.codename = "dadara_event_array";

/*
		apt = (ActionParameterType )atype.sorted_parameters.get("concavities");

		Parameter resparam2 = new Parameter();
		resparam2.id = dataSource.GetUId(Parameter.typ);
		resparam2.parameter_type_id = "traceeventarray";
		resparam2.type_id = apt.getId();
		resparam2.value = bac.encode();
		resparam2.codename = "concavities";
*/
		r.addParameter(resparam1);
		r.addParameter(resparam2);
		r.setTransferableFromLocal();
		Pool.put("result", r.id, r);

		dataSource.SaveAnalysis(a.getId(), r.getId());
	}
}