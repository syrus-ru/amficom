package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.util.*;

public class OpenCatalogCommand extends ViewNavigatorCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;
	String typ;
	Class cl;
	String cmd;

	public OpenCatalogCommand(JDesktopPane desktop, ApplicationContext aContext, String typ, Class cl, String cmd)
	{
		super(aContext.getDispatcher(), desktop, "Навигатор объектов");

		this.desktop = desktop;
		this.aContext = aContext;

		this.typ = typ;
		this.cl = cl;
		this.cmd = cmd;
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
		return new OpenCatalogCommand(desktop, aContext, typ, cl, cmd);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(aContext.getSessionInterface().getUserId(), cmd))
		{
			return;
		}

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
		frame.setTitle("Конфигурирование");

		ObjectResourceCatalogFrame frame2 = null;

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
			frame2 = new ObjectResourceCatalogFrame("Оборудование", aContext);
			frame2.setLocation(dim.width * 3 / 10, 0);
			frame2.setSize(dim.width * 7 / 10, dim.height);
			desktop.add(frame2);
		}
		Map dSet = Pool.getHash(typ);
		ObjectResourceFilter filter = new ObjectResourceDomainFilter(dataSource.getSession().getDomainId());
		dSet = filter.filter(dSet);

		frame2.setContents(new DataSet(dSet.values().iterator()));
		frame2.setDisplayModel(new StubDisplayModel(new String[] { "id", "name" },new String[] { "Идентификатор", "Название" }));
		frame2.setObjectResourceClass(cl);
		frame2.panel.setButtonPanelVisible(false);
		frame2.panel.setListState();

		ObjectResourceCatalogActionModel orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);

		if(typ.equals(Domain.typ))
		{
			orcam = new ObjectResourceCatalogActionModel(
					ObjectResourceCatalogActionModel.PANEL,
					ObjectResourceCatalogActionModel.ADD_BUTTON,
					ObjectResourceCatalogActionModel.SAVE_BUTTON,
					ObjectResourceCatalogActionModel.REMOVE_BUTTON,
					ObjectResourceCatalogActionModel.PROPS_BUTTON,
					ObjectResourceCatalogActionModel.CANCEL_BUTTON);
		}
		else
		if(typ.equals(Equipment.typ))
		{
			orcam = new ObjectResourceCatalogActionModel(
					ObjectResourceCatalogActionModel.PANEL,
					ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
					ObjectResourceCatalogActionModel.SAVE_BUTTON,
					ObjectResourceCatalogActionModel.REMOVE_BUTTON,
					ObjectResourceCatalogActionModel.PROPS_BUTTON,
					ObjectResourceCatalogActionModel.CANCEL_BUTTON);
		}
		else
		if(typ.equals(KIS.typ))
		{
			orcam = new ObjectResourceCatalogActionModel(
					ObjectResourceCatalogActionModel.PANEL,
					ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
					ObjectResourceCatalogActionModel.SAVE_BUTTON,
					ObjectResourceCatalogActionModel.REMOVE_BUTTON,
					ObjectResourceCatalogActionModel.PROPS_BUTTON,
					ObjectResourceCatalogActionModel.CANCEL_BUTTON);
		}

		frame2.setActionModel(orcam);

		frame2.show();

		aContext.getDispatcher().notify(new TreeListSelectionEvent(typ, TreeListSelectionEvent.SELECT_EVENT, true, true));
	}
}
