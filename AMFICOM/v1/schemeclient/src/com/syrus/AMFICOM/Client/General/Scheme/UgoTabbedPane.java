package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class UgoTabbedPane extends JPanel implements OperationListener
{
	ApplicationContext aContext;
	protected UgoPanel panel;

	boolean ignore_loading = false;
	boolean insert_ugo = true;

	public UgoTabbedPane(ApplicationContext aContext)
	{
		this.aContext = aContext;
		init_panel();
		init_module();
	}

	protected void init_panel()
	{
		panel = new UgoPanel(aContext);
		setLayout(new BorderLayout());
		add(panel);
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
						"Схема \"" + panel.getGraph().getScheme().name() +
						"\" была изменена. Вы действительно хотите закрыть схему?",
						"Подтверждение",
						JOptionPane.YES_NO_OPTION);
			else if (panel.getGraph().getSchemeElement() != null)
				res = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						"Элемент \"" + panel.getGraph().getSchemeElement().name() +
						"\" был изменен. Вы действительно хотите закрыть схему?",
						"Подтверждение",
						JOptionPane.YES_NO_OPTION);

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
					if (obj instanceof SchemeProtoElement)
					{
						SchemeProtoElement proto = (SchemeProtoElement) obj;
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
				if (obj instanceof SchemeElement)
				{
					SchemeElement se = (SchemeElement)obj;
					removeSchemeElement(se);
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

		Map clones = graph.copyFromArchivedState(sch.ugoCellImpl().getData(), new java.awt.Point(0, 0));
		graph.selectionNotify();
	}

	public boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (sch.equals(graph.getScheme()))
		{
			GraphActions.clearGraph(graph);
			graph.setGraphChanged(false);
			return true;
		}
		return false;
	}

	public boolean removeSchemeElement(SchemeElement se)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (se.equals(graph.getSchemeElement()))
		{
			GraphActions.clearGraph(graph);
			graph.setGraphChanged(false);
			return true;
		}
		return false;
	}


}