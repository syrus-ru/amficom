package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

public class ComponentNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph cell_graph;
	SchemeGraph ugo_graph;

	public ComponentNewCommand(ApplicationContext aContext, SchemeGraph cell_graph, SchemeGraph ugo_graph)
	{
		this.aContext = aContext;
		this.cell_graph = cell_graph;
		this.ugo_graph = ugo_graph;
	}

	public Object clone()
	{
		return new ComponentNewCommand(aContext, cell_graph, ugo_graph);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		if (cell_graph.getAll().length != 0)
		{
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Создать новый компонент?", "Новый компонент", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;
		}
		cell_graph.removeAll();
		ugo_graph.removeAll();
		cell_graph.selectionNotify();
	}
}

