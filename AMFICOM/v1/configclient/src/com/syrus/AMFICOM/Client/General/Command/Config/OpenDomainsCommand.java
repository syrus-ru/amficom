package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class OpenDomainsCommand extends ViewNavigatorCommand
{
	String parameter;
	JDesktopPane desktop;
	ApplicationContext aContext;
	
	public OpenDomainsCommand(JDesktopPane desktop, ApplicationContext aContext, String parameter)
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
		return new OpenDomainsCommand(desktop, aContext, parameter);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		setParameter("treemodel", new ConfigTreeModel(dataSource));
		super.setApplicationContext(aContext);
		super.execute();
		frame.setTitle("Конфигурирование");

		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 3 / 10, dim.height);

		ObjectResourceCatalogFrame frame2;

		frame2 = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof ObjectResourceCatalogFrame)
			{
				frame2 = (ObjectResourceCatalogFrame )comp;
				break;
			}
		}
		if (frame2 == null)
		{
			frame2 = new ObjectResourceCatalogFrame("Домены", aContext);
			desktop.add(frame2);
			frame2.setLocation(dim.width * 3 / 10, 0);
			frame2.setSize(dim.width * 7 / 10, dim.height);
		}

		frame2.setContents(new DataSet(Pool.getHash("domain")));
//		frame2.setDisplayModel(new StubDisplayModel(new String[] { "id", "name" },new String[] { "Идентификатор", "Название" }));
		frame2.setObjectResourceClass(Domain.class);
		frame2.panel.setListState();

		ObjectResourceCatalogActionModel orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);

		frame2.setActionModel(orcam);

		frame2.show();

		aContext.getDispatcher().notify(new TreeListSelectionEvent("domain", TreeListSelectionEvent.SELECT_EVENT, true));
	}
}