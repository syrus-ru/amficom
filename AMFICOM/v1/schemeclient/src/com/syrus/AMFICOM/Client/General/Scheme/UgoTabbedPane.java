package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class UgoTabbedPane extends JPanel implements OperationListener
{
	ApplicationContext aContext;
	protected UgoPanel panel;

	boolean ignore_loading = false;
	boolean insert_ugo = true;

	public UgoTabbedPane(ApplicationContext aContext)
	{
		this.aContext = aContext;
		panel = new UgoPanel(aContext);

		setLayout(new BorderLayout());
		add(panel);

		init_module();
	}

	public void init_module()
	{
		aContext.getDispatcher().register(this, SchemeElementsEvent.type);
	}

	public UgoPanel getPanel()
	{
		return panel;
	}

	public boolean removePanel(UgoPanel panel)
	{
		if (panel.getGraph().isGraphChanged())
		{
			int res = JOptionPane.CANCEL_OPTION;
			if (panel.getGraph().getScheme() != null)
				res = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						"Схема была изменена. Вы действительно хотите закрыть схему?",
						"Подтверждение",
						JOptionPane.YES_NO_CANCEL_OPTION);
			else if (panel.getGraph().getSchemeElement() != null)
				res = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						"Компонент был изменен. Вы действительно хотите закрыть схему?",
						"Подтверждение",
						JOptionPane.YES_NO_CANCEL_OPTION);

			if (res != JOptionPane.OK_OPTION)
			{
//				SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, this, null);
//				ssc.execute();
//				if (ssc.ret_code == SchemeSaveCommand.CANCEL)
					return false;
			}
		}
		return true;
	}

	public void removeAllPanels()
	{
		remove(panel);
	}

	public void setGraphChanged(boolean b)
	{
		getPanel().getGraph().setGraphChanged(b);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeGraph graph = getPanel().getGraph();
			SchemeElementsEvent see = (SchemeElementsEvent) ae;
			if (see.OPEN_PRIMARY_SCHEME)
			{
				if (!ignore_loading)
				{
					Scheme scheme = (Scheme) see.obj;
//					GraphActions.clearGraph(graph);
					openScheme(scheme);
				}
			}
			if (see.OPEN_ELEMENT)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof ProtoElement)
					{
						ProtoElement proto = (ProtoElement) obj;
						getPanel().setProtoCell(proto, null);
					}
					graph.skip_notify = false;
				}
			}
			if (see.OPEN_SCHEME)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof Scheme)
					{
						Scheme scheme = (Scheme) obj;
						getPanel().setSchemeCell(scheme);
					}
					graph.skip_notify = false;
				}
			}
			if (see.CLOSE_SCHEME)
			{
				graph.skip_notify = true;
				Object obj = see.obj;
				if (obj instanceof Scheme)
				{
					Scheme scheme = (Scheme) obj;
					removeScheme(scheme);
//					graph.setGraphChanged(false);
				}
				graph.skip_notify = false;
			}
		}
	}

	public void openScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		graph.setScheme(sch);
		GraphActions.clearGraph(graph);
		sch.unpack();

		Map clones = graph.copyFromArchivedState(sch.serializable_ugo, new java.awt.Point(0, 0));
		graph.selectionNotify();
	}

	public boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch.getId().equals(graph.getScheme().getId()))
		{
			GraphActions.clearGraph(graph);
			graph.setGraphChanged(false);
			return true;
		}
		return false;
	}

}