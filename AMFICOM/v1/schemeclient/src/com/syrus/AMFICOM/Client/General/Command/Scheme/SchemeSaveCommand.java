package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import javax.swing.JOptionPane;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.*;

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
					if (SchemeUtils.isSchemeContainsElement(s, se))
					{
						schemeTab.setGraphChanged(p[i].getGraph(), true);
						JOptionPane.showMessageDialog(
								Environment.getActiveWindow(),
								"Элемент " + se.name() + " успешно сохранен в схеме " + s.name(),
								"Сообщение",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			schemeTab.setGraphChanged(false);
			return;
		}

		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
			new PathSaveCommand(aContext, schemeTab).execute();
		if (SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
			return;

		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			if (ugograph.getRoots().length == 0)
			{
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Схему нельзя будет включить в другую схему,\nт.к. не создано условное графическое обозначение схемы.\nПродолжить сохранение?", "Предупреждение", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
					return;
			}
		}
		else if (scheme.ugoCell() == null)
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
					sd.init(scheme, scheme.name(), false);
			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}
//		ComponentSaveCommand.saveTypes(aContext.getDataSourceInterface(), false);

		scheme.name(sd.name);
		scheme.description(sd.description);
		scheme.type(sd.type);
//		scheme.created = System.currentTimeMillis();
		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			scheme.domainImpl(domain);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		scheme.schemeCellImpl().setData((List)graph.getArchiveableState(graph.getRoots()));
		if (graph.getScheme().equals(ugograph.getScheme()))
		{
			scheme.ugoCellImpl().setData((List)ugograph.getArchiveableState(ugograph.getRoots()));
			ugoTab.setGraphChanged(false);
		}
		else
//		if (scheme.ugoCell() == null)
		{
			scheme.ugoCellImpl().setData((List)new SchemeGraph(new DefaultGraphModel(), new ApplicationContext()).getArchiveableState());
		}

//		if (!res)
//		{
//			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
//																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
//			return;
//		}


		try {
			SchemeStorableObjectPool.putStorableObject(scheme);

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Схема " + scheme.name() + " успешно сохранена",
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);

			aContext.getDispatcher().notify(new TreeListSelectionEvent("",
					TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		ret_code = OK;
	}
}

