package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;
import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.*;

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
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}
		if (bs.test_setup_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestSetupError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		RefAnalysis refanalysis = (RefAnalysis)Pool.get("refanalysis", traceid);
		ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", traceid);
		TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);

		String s = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("analysis"),
				JOptionPane.OK_CANCEL_OPTION);
		if (s == null || s.equals(""))
			return;

		Analysis a = new Analysis(dataSource.GetUId("analysis"));
		a.setName(s);
		//SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
		//a.name = sdf.format(new Date(System.currentTimeMillis()));
		a.setTypeId(AnalysisUtil.DADARA);
		a.setCriteriaSetId(ts.getCriteriaSetId());
		a.setResultIds(new String[0]);
		AnalysisType atype = (AnalysisType)Pool.get(AnalysisType.typ, a.getTypeId());

		byte[] value = new byte[0];

		ActionParameterType apt = (ActionParameterType )atype.getSortedArguments().get(AnalysisUtil.REFLECTOGRAMM);
		Parameter arg1 = new Parameter();
		arg1.setId(dataSource.GetUId(Parameter.typ));
		arg1.setParameterTypeId(AnalysisUtil.REFLECTOGRAMM);
		arg1.setTypeId(apt.getId());
		arg1.setValue(new BellcoreWriter().write(bs));
		arg1.setCodename(AnalysisUtil.REFLECTOGRAMM);
		a.addArgument(arg1);

		a.setUserId(aContext.getSessionInterface().getUserId());
		a.setMonitoredElementId(bs.monitored_element_id);

		a.setTransferableFromLocal();

		Pool.put("analysis", a.getId(), a);

		Result r = new Result(a.getId(), "analysis", "", aContext.getSessionInterface().getUserId(), dataSource.GetUId("result"));

		ByteArrayCollector bac = new ByteArrayCollector();
		for (int i = 0; i < refanalysis.events.length; i++)
		{
			byte [] b = refanalysis.events[i].toByteArray();
			bac.add(b);
		}
		apt = (ActionParameterType )atype.getSortedParameters().get("traceevents");

		Parameter resparam1 = new Parameter();
		resparam1.setId(dataSource.GetUId(Parameter.typ));
		resparam1.setParameterTypeId("traceeventarray");
		resparam1.setTypeId(apt.getId());
		resparam1.setValue(bac.encode());
		resparam1.setCodename("traceevents");

		bac = new ByteArrayCollector();
		for (int i = 0; i < refanalysis.concavities.length; i++)
			bac.add(refanalysis.concavities[i].toByteArray());

		apt = (ActionParameterType )atype.getSortedParameters().get("dadara_event_array");

		Parameter resparam2 = new Parameter();
		resparam2.setId(dataSource.GetUId(Parameter.typ));
		resparam2.setParameterTypeId(apt.getParameterTypeId());
		resparam2.setTypeId(apt.getId());
		resparam2.setValue(ReflectogramEvent.toByteArray(ep));
		resparam2.setCodename("dadara_event_array");

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
		Pool.put("result", r.getId(), r);

		dataSource.SaveAnalysis(a.getId(), r.getId());
	}
}