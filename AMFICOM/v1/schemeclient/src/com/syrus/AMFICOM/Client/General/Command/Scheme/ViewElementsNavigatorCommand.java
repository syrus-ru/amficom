package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.ViewNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Schematics.UI.ElementsTreeModel;

public class ViewElementsNavigatorCommand extends ViewNavigatorCommand
{
	String parameter;
	JDesktopPane desktop;
	ApplicationContext aContext;

	public ViewElementsNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext, String parameter)
	{
		super(aContext.getDispatcher(), desktop, "Компоненты сети");

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
		return new ViewElementsNavigatorCommand(desktop, aContext, parameter);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		setParameter("treemodel", new ElementsTreeModel(dataSource));
		super.setApplicationContext(aContext);
		super.execute();

		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(1000 / 5, 2 * 700 / 5);
		frame.setVisible(true);
	}
}
