package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class SchemeCopyCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel schemePanel;
	UgoPanel ugoPanel;

	public SchemeCopyCommand(ApplicationContext aContext, SchemePanel schemePanel, UgoPanel ugoPanel)
	{
		this.aContext = aContext;
		this.schemePanel = schemePanel;
		this.ugoPanel = ugoPanel;
	}

	public Object clone()
	{
		return new SchemeCopyCommand(aContext, schemePanel, ugoPanel);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		SchemeGraph graph = schemePanel.getGraph();
		SchemeGraph ugo_graph = ugoPanel.getGraph();

		if (graph.getRoots().length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "���������� ��������� ������ �����", "������", JOptionPane.OK_OPTION);
			return;
		}

		SaveDialog sd;
		Scheme scheme = schemePanel.getGraph().getScheme();
		while (true)
		{

			sd = new SaveDialog(aContext, aContext.getDispatcher(), "���������� �����");
			int ret = sd.init(scheme, scheme.getName(), false);

			if (ret == 0)
				return;

			if (!MiscUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "������������ �������� �����.", "������", JOptionPane.OK_OPTION);
			else
				break;
		}

		scheme = (Scheme)scheme.clone(aContext.getDataSourceInterface());
		scheme.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());

		scheme.name = sd.name;
		scheme.description = sd.description;
		scheme.created = System.currentTimeMillis();
		scheme.createdBy = dataSource.getSession().getUserId();
		scheme.modifiedBy = dataSource.getSession().getUserId();
		scheme.ownerId = dataSource.getSession().getUserId();
		scheme.domainId = dataSource.getSession().getDomainId();

		Pool.put(Scheme.typ, scheme.getId(), scheme);

		boolean res = scheme.pack();

		if (!res)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "������ ���������� ����� " +
																		scheme.getName(), "������", JOptionPane.OK_OPTION);
			return;
		}

		dataSource.SaveScheme(scheme.getId());

		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"����� "+ scheme.getName() + " ������� ���������",
				"���������",
				JOptionPane.INFORMATION_MESSAGE);

		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
				TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));

		Pool.removeMap("clonedids");
	}
}

