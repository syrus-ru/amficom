package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.GraphActions;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

import com.syrus.AMFICOM.Client.Schematics.Elements.SchemePropsPanel;

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
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустую схему", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		SaveDialog sd;
		while (true)
		{
			sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение схемы");
			int ret = sd.init(schemePanel.scheme, schemePanel.scheme.getName(), false);

			if (ret == 0)
				return;

			if (!MyUtil.validName(sd.name))
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название схемы.", "Ошибка", JOptionPane.OK_OPTION);
			else
				break;
		}

		Scheme scheme = (Scheme)schemePanel.scheme.clone(aContext.getDataSourceInterface());
		scheme.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
		scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());

		scheme.name = sd.name;
		scheme.description = sd.description;
		scheme.created = System.currentTimeMillis();
		scheme.created_by = dataSource.getSession().getUserId();
		scheme.modified_by = dataSource.getSession().getUserId();
		scheme.owner_id = dataSource.getSession().getUserId();
		scheme.domain_id = dataSource.getSession().getDomainId();

		Pool.put(Scheme.typ, scheme.getId(), scheme);

		boolean res = scheme.pack();

		if (!res)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка сохранения схемы " +
																		scheme.getName(), "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		dataSource.SaveScheme(scheme.getId());

		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Схема "+ scheme.getName() + " успешно сохранена",
				"Сообщение",
				JOptionPane.INFORMATION_MESSAGE);

		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
				TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));

		Pool.removeHash("clonedids");
	}
}

