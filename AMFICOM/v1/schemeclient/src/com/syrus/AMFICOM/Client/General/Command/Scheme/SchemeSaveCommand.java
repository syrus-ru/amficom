package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Schematics.General.GraphActions;
import com.syrus.AMFICOM.Client.Schematics.General.SchemeGraph;
import com.syrus.AMFICOM.Client.Schematics.General.SchemePanel;
import com.syrus.AMFICOM.Client.Schematics.General.UgoPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class SchemeSaveCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	ApplicationContext aContext;
	SchemePanel schemePanel;
	UgoPanel ugoPanel;

	public int ret_code = CANCEL;

	public SchemeSaveCommand(ApplicationContext aContext, SchemePanel schemePanel, UgoPanel ugoPanel)
	{
		this.aContext = aContext;
		this.schemePanel = schemePanel;
		this.ugoPanel = ugoPanel;
	}

	public Object clone()
	{
		return new SchemeSaveCommand(aContext, schemePanel, ugoPanel);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeGraph graph = schemePanel.getGraph();

		Scheme scheme = schemePanel.scheme;

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if (ugoPanel != null && ugoPanel.getGraph().getRoots().length == 0)
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

			if (!MyUtil.validName(sd.name))
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

		if (ugoPanel != null)
		{
			scheme.serializable_ugo = ugoPanel.getGraph().getArchiveableState(ugoPanel.getGraph().getRoots());
			GraphActions.setResizable(ugoPanel.getGraph(), ugoPanel.getGraph().getAll(), false);
		}
		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());
		boolean res = scheme.pack();

		if (!res)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		Hashtable h = schemePanel.schemes_to_save;
		h.put(scheme.getId(), scheme);

		for (Enumeration e = h.elements(); e.hasMoreElements();)
		{
			Scheme s = (Scheme)e.nextElement();
			dataSource.SaveScheme(s.getId());
		}
		schemePanel.schemes_to_save = new Hashtable();
		graph.setGraphChanged(false);
		if (ugoPanel != null)
			ugoPanel.getGraph().setGraphChanged(false);

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme, SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));

		JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						"Схема "+ scheme.getName() +" успешно сохранена",
						"Сообщение",
						JOptionPane.INFORMATION_MESSAGE);

//		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
//				TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));

		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ, TreeListSelectionEvent.REFRESH_EVENT));

		Pool.remove("serialized", "serialized");
		ret_code = OK;
	}
}

