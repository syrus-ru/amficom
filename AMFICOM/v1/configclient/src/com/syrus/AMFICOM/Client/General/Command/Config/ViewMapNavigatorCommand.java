package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ViewMapNavigatorCommand extends ViewNavigatorCommand
{
	String parameter;
	JDesktopPane desktop;
	ApplicationContext aContext;
	
	public ViewMapNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext, String parameter)
	{
		super(aContext.getDispatcher(), desktop, "Навигатор объектов");

		this.desktop = desktop;
		this.aContext = aContext;
		this.parameter = parameter;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("desktop"))
			setDesktop((JDesktopPane)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
		else
			super.setParameter(field, value);
	}

	public void setDesktop(JDesktopPane desktop)
	{
		this.desktop = desktop;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewMapNavigatorCommand(desktop, aContext, parameter);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		setParameter("treemodel", new MapTreeModel(dataSource));
		super.setApplicationContext(aContext);
		super.execute();

		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(dim.width * 4 / 5, dim.height / 2);
		frame.setSize(dim.width / 5, dim.height / 2);
	}
}
