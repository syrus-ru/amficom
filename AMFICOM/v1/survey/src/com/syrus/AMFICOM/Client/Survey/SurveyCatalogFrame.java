package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.measurement.Result;

public class SurveyCatalogFrame extends ObjectResourceCatalogFrame
	implements OperationListener
{
	Dispatcher dispatcher;

	public SurveyCatalogFrame(String title, ApplicationContext aContext)
	{
		super(title, aContext);
		this.dispatcher = aContext.getDispatcher();

		dispatcher.register(this, TreeListSelectionEvent.typ);
		dispatcher.register(this, "treeselectionevent");
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeListSelectionEvent.typ) ||
			oe.getActionCommand().equals("treeselectionevent") )
		{
			Object obj = oe.getSource();
			if (obj instanceof Result) {
				Result r = (Result)obj;
				Pool.put("activecontext", "useractionselected", "result_selected");
				Pool.put("activecontext", "selected_id", r.getId());
			}
/*
			if(obj instanceof Modeling)
			{
				Modeling m = (Modeling)obj;
				Pool.put("activecontext", "useractionselected", "modeling_selected");
				Pool.put("activecontext", "selected_id", m.getId());
//				System.out.println("Modeling " + m.getId());
			}
			else if(obj instanceof Test)
			{
				Test t = (Test )obj;
				Pool.put("activecontext", "useractionselected", "test_selected");
				Pool.put("activecontext", "selected_id", t.getId());
//				System.out.println("Test " + t.getId());
			}
			else if(obj instanceof Measurement)
			{
				Measurement m = (Measurement)obj;
				Pool.put("activecontext", "useractionselected", "measurement_selected");
				Pool.put("activecontext", "selected_id", m.getId());
//				System.out.println("Result " + r.getId());
			}
			else if (obj instanceof MeasurementSetup)
			{
				MeasurementSetup ms = (MeasurementSetup)obj;
				Pool.put("activecontext", "useractionselected", "measurementsetup_selected");
				Pool.put("activecontext", "selected_id", ms.getId());
			}
			else if(obj instanceof MonitoredElement)
			{
				MonitoredElement me = (MonitoredElement)obj;
				Pool.put("activecontext", "useractionselected", "monitoredelement_selected");
				Pool.put("activecontext", "selected_id", me.getId());
			}*/
			else
				return;

			if(dispatcher != null)
				dispatcher.notify(new OperationEvent(this, 0, "activecontextevent"));
		}
		else
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent )oe;

			Class cl = tdse.getDataClass();

			String title;
			try
			{
				java.lang.reflect.Field typField = cl.getField("");
				title = (String )typField.get(cl);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("Не найдено поле typ класса " + cl.getName());
				title = "";
			}
			catch(Exception e)
			{
				System.out.println("Не найден объект ObjectResource - " + e.getMessage());
				title = "";
			}

			setTitle(LangModel.getString("node" + title));
		}
	}
}

