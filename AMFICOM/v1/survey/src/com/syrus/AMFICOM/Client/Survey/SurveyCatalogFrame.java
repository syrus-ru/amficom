package com.syrus.AMFICOM.Client.Survey;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;

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
			if(obj instanceof Analysis)
			{
				Analysis a = (Analysis )obj;
				Pool.put("activecontext", "useractionselected", "analysis_selected");
				Pool.put("activecontext", "selected_id", a.getId());
				System.out.println("Analysis " + a.getId());
			}
			else
			if(obj instanceof Modeling)
			{
				Modeling m = (Modeling )obj;
				Pool.put("activecontext", "useractionselected", "modeling_selected");
				Pool.put("activecontext", "selected_id", m.getId());
				System.out.println("Modeling " + m.getId());
			}
			else
			if(obj instanceof Evaluation)
			{
				Evaluation ev = (Evaluation )obj;
				Pool.put("activecontext", "useractionselected", "evaluation_selected");
				Pool.put("activecontext", "selected_id", ev.getId());
				System.out.println("Evaluation " + ev.getId());
			}
			else
			if(obj instanceof Test)
			{
				Test t = (Test )obj;
				Pool.put("activecontext", "useractionselected", "test_selected");
				Pool.put("activecontext", "selected_id", t.getId());
				System.out.println("Test " + t.getId());
			}
			else
			if(obj instanceof Result)
			{
				Result r = (Result )obj;
				Pool.put("activecontext", "useractionselected", "result_selected");
				Pool.put("activecontext", "selected_id", r.getId());
				System.out.println("Result " + r.getId());
			}
			else
			{
//				super.valueChanged(e);
				return;
			}

			if(dispatcher != null)
			{
				dispatcher.notify(new OperationEvent(this, 0, "activecontextevent"));
				System.out.println("notify " + dispatcher);
			}
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