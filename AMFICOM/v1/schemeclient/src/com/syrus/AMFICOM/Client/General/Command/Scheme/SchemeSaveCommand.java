package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeSaveCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	ApplicationContext aContext;
	SchemeTabbedPane schemeTab;
	UgoTabbedPane ugoTab;

	public int ret_code = CANCEL;

	public SchemeSaveCommand(ApplicationContext aContext, SchemeTabbedPane schemeTab, UgoTabbedPane ugoTab)
	{
		this.aContext = aContext;
		this.schemeTab = schemeTab;
		this.ugoTab = ugoTab;
	}

	public Object clone()
	{
		return new SchemeSaveCommand(aContext, schemeTab, ugoTab);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeGraph graph = schemeTab.getPanel().getGraph();
		SchemeGraph ugograph = ugoTab.getPanel().getGraph();
		Scheme scheme = graph.getScheme();

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if (graph.getSchemeElement() != null) // сохраняем компонент
		{
			SchemeElement se = graph.getSchemeElement();
			schemeTab.getPanel().updateSchemeElement();
			UgoPanel[] p = schemeTab.getAllPanels();
			for (int i = 0; i < p.length; i++)
			{
				Scheme s = p[i].getGraph().getScheme();
				if (s != null)
				{
					if (s.getSchemeElement(se.getId()) != null)
					{
						schemeTab.setGraphChanged(p[i].getGraph(), true);
						JOptionPane.showMessageDialog(
								Environment.getActiveWindow(),
								"Элемент " + se.getName() + " успешно сохранен в схеме " + s.getName(),
								"Сообщение",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			schemeTab.setGraphChanged(false);
			return;
		}

		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			if (ugograph.getRoots().length == 0)
			{
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?", "Предупреждение", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
		}
		else if (scheme.serializable_ugo == null)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?", "Предупреждение", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
				return;
		}


//		if (scheme.getId().equals("") || scheme.getName().equals(""))
//		{
		SaveDialog sd;
		while (true)
		{
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = //sd.init(schemePanel.scheme.getName(), schemePanel.scheme.description, false);
					sd.init(scheme, scheme.getName(), false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
		ComponentSaveCommand.saveTypes(aContext.getDataSourceInterface(), false);

		scheme.name = sd.name;
		scheme.description = sd.description;
		scheme.scheme_type = sd.type;
		if (scheme.getId().equals(""))
			scheme.id = dataSource.GetUId(Scheme.typ);

		scheme.created = System.currentTimeMillis();
		scheme.created_by = dataSource.getSession().getUserId();
		scheme.modified_by = dataSource.getSession().getUserId();
		scheme.owner_id = dataSource.getSession().getUserId();
		scheme.domain_id = dataSource.getSession().getDomainId();

		Pool.put(Scheme.typ, scheme.getId(), scheme);
		//}

		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());
		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			scheme.serializable_ugo = ugograph.getArchiveableState(ugograph.getRoots());
			ugoTab.setGraphChanged(false);
		}
		else if (scheme.serializable_ugo == null)
			scheme.serializable_ugo = new SchemeGraph(new DefaultGraphModel(), new ApplicationContext()).getArchiveableState();

		boolean res = scheme.pack();

		if (!res)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
			return;
		}


		dataSource.SaveScheme(scheme.getId());
		schemeTab.setGraphChanged(false);

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));

		JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"Схема "+ scheme.getName() +" успешно сохранена",
						"Сообщение",
						JOptionPane.INFORMATION_MESSAGE);

//		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
//				TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));

		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ, TreeListSelectionEvent.REFRESH_EVENT));

		ret_code = OK;
	}
}

