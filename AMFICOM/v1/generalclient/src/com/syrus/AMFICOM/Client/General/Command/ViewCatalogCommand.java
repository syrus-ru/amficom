package com.syrus.AMFICOM.Client.General.Command;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

public class ViewCatalogCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;
	ObjectResourceDisplayModel dModel;
	Class orclass;

	public ViewCatalogCommand()
	{
	}

	public ViewCatalogCommand(JDesktopPane desktop, ApplicationContext aContext, ObjectResourceDisplayModel dModel, Class orclass)
	{
		this.desktop = desktop;
		this.aContext = aContext;
		this.dModel = dModel;
		this.orclass = orclass;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("desktop"))
			setDesktop((JDesktopPane)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
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
		return new ViewCatalogCommand(desktop, aContext, dModel, orclass);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		String parameter = "";

		try
		{
			java.lang.reflect.Field field = orclass.getField("typ");
//			parameter = (String )field.getName();
		}
		catch(Exception e)
		{
			System.out.println("?????");
			parameter = "";
		}

		ObjectResourceCatalogFrame frame = new ObjectResourceCatalogFrame(LangModel.String("titleCatalog"), aContext);
		frame.setDisplayModel(dModel);
		frame.setContents(new DataSet(Pool.getHash(parameter)));
		frame.setObjectResourceClass(orclass);

//		frame.setBounds(0, 0, 660, 600);
		desktop.add(frame);

		frame.show();
	}
}
