package com.syrus.AMFICOM.Client.General.Filter;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import javax.swing.JPanel;

public class FilterPanel extends JPanel
{
	ApplicationContext aContext;
	
	public FilterPanel()
	{
		super();
		this.setName(LangModel.String("labelTabbedProperties"));
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception
	{
		this.setLayout(new XYLayout());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public FilterExpression getExpression(String col_id, String col_name) { return null; }
	public void setExpression(FilterExpression expr){}

	//public abstract FilterExpression getExpression();
	//public abstract void setExpression(FilterExpression expr);

}