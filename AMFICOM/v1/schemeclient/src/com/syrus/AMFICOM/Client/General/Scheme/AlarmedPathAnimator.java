package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Color;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;

import com.jgraph.graph.DefaultEdge;

public class AlarmedPathAnimator// extends Thread implements Runnable
{
	boolean alarmState = false;
	int timeInterval = 1000;

	SchemePanel panel;
	public ApplicationContext aContext;
	String alarmed_link_id;

	DefaultEdge[] edges;
	Object[] objects;

	public AlarmedPathAnimator(ApplicationContext aContext,  SchemePanel panel, String alarmed_link_id)
	{
		this.panel = panel;
		this.aContext = aContext;
		this.alarmed_link_id = alarmed_link_id;

		Object[] cells = findAlarmedObjects(panel, alarmed_link_id);
		System.out.println("found " + cells.length + " cells to repaint");

		ArrayList _edges = new ArrayList();
		ArrayList _objects = new ArrayList();
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultEdge)
				_edges.add(cells[i]);
			else
				_objects.add(cells[i]);
		}

		edges = (DefaultEdge[])_edges.toArray(new DefaultEdge[_edges.size()]);
		objects = _objects.toArray(new Object[_objects.size()]);
	}

/*	public void run()
	{
		Object[] cells = findAlarmedObjects(panel, alarmed_link_id);
		System.out.println("found " + cells.length + " cells to repaint");

		ArrayList _edges = new ArrayList();
		ArrayList _objects = new ArrayList();
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultEdge)
				_edges.add(cells[i]);
			else
				_objects.add(cells[i]);
		}

		DefaultEdge[] edges = (DefaultEdge[])_edges.toArray(new DefaultEdge[_edges.size()]);
		Object[] objects = _objects.toArray(new Object[_objects.size()]);

		flag = true;
		while (flag)
		{
			try
			{
				if (objects.length == 0 && edges.length != 0)
				{
					CellView[] views = panel.getGraph().getGraphLayoutCache().getMapping(edges);
					for (int i = 0; i < views.length; i++)
					{
						((LinkView)views[i]).alarmed = true;
						((LinkView)views[i]).trigger = true;
					}
				}
				Component c1 = panel.getParent();
				c1.paint(c1.getGraphics());

				sleep( timeInterval );

				if (objects.length == 0 && edges.length != 0)
				{
					CellView[] views = panel.getGraph().getGraphLayoutCache().getMapping(edges);
					for (int i = 0; i < views.length; i++)
					{
						((LinkView)views[i]).trigger = false;
					}
				}
				c1.paint(c1.getGraphics());

				sleep( timeInterval );
			}
			catch (Exception e)
			{
				System.out.println("AnimateThread found: " + e);
			}
		}

//		System.out.println("stoppin thread " + hashCode());
	}*/

	public void mark()
	{
		if (objects.length == 0 && edges.length != 0)
			GraphActions.setEdgeColor(panel.getGraph(), edges, Color.red);
		if (objects.length != 0)
			GraphActions.setObjectsForeColor(panel.getGraph(), objects, Color.red);
		panel.repaint();
	}

	public void unmark()
	{
		if (objects.length == 0 && edges.length != 0)
			GraphActions.setEdgeColor(panel.getGraph(), edges, Color.black);
		if (objects.length != 0)
			GraphActions.setObjectsForeColor(panel.getGraph(), objects, Color.black);
		panel.repaint();
	}

	Object[] findAlarmedObjects(SchemePanel panel, String alarmed_link_id)
	{
		Object[] cells = panel.getGraph().getAll();
		ArrayList edges_to_paint = new ArrayList();

		if (!alarmed_link_id.equals(""))
		{
			;//at first find if link at this scheme
			for (int i = 0; i < cells.length; i++)
			{
				;
				if (cells[i] instanceof DefaultLink)
				{
					SchemeLink sl = ((DefaultLink)cells[i]).getSchemeLink();
					if (sl.link_id.equals(alarmed_link_id))
					{
						edges_to_paint.add(cells[i]);
						sl.alarmed = true;

						//LinkView lv = (LinkView)panel.getGraph().getGraphLayoutCache().getMapping(cells[i], false);
						//lv.alarmed = true;

						return edges_to_paint.toArray();
					}
				}
				else if (cells[i] instanceof DefaultCableLink)
				{
					SchemeCableLink sl = ((DefaultCableLink)cells[i]).getSchemeCableLink();
					if (sl.cable_link_id.equals(alarmed_link_id))
					{
						edges_to_paint.add(cells[i]);
						sl.alarmed = true;

						//LinkView lv = (LinkView)panel.getGraph().getGraphLayoutCache().getMapping(cells[i], false);
						//lv.alarmed = true;

						return edges_to_paint.toArray();
					}
				}
			}
			//if nothing is found check witch element contains this link
			for (int i = 0; i < cells.length; i++)
			{
				;
				if (cells[i] instanceof DeviceGroup)
				{
					SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
					if (se.scheme_id.equals(""))
					{
						for (Iterator it = se.getAllElementsLinks(); it.hasNext();)
						{
							SchemeLink sl = (SchemeLink)it.next();
							if (sl.link_id.equals(alarmed_link_id))
							{
								for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements();)
									edges_to_paint.add(e.nextElement());
								se.alarmed = true;
								se.alarmed_link_id = alarmed_link_id;
								return edges_to_paint.toArray();
							}
						}
					}
					else
					{
						Scheme scheme = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
						if (scheme.isSchemeContainsLink(alarmed_link_id) ||
								scheme.isSchemeContainsCableLink(alarmed_link_id))
						{
							for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements();)
								edges_to_paint.add(e.nextElement());
							se.alarmed = true;
							se.alarmed_link_id = alarmed_link_id;
							return edges_to_paint.toArray();
						}
					}
				}
			}

		}
		return edges_to_paint.toArray();
	}

	/*
	 Object[] findAlarmedObjects(SchemeGraph graph)
 {
	 Object[] cells = graph.getAll();
	 ArrayList edges_to_paint = new ArrayList();
	 for (int i = 0; i < cells.length; i++)
	 {
		 String sp_id = "";
		 if (cells[i] instanceof DefaultLink)
			 sp_id = ((DefaultLink)cells[i]).scheme_path_id;
		 else if (cells[i] instanceof DefaultCableLink)
			 sp_id = ((DefaultCableLink)cells[i]).scheme_path_id;

		 if (!sp_id.equals(""))
		 {
			 SchemePath sp = (SchemePath)Pool.get(SchemePath.typ, sp_id);
			 ElementAttribute ea = (ElementAttribute)sp.attributes.get("alarmed");
			 if(ea.value.equals("true"))
				 edges_to_paint.add(cells[i]);
		 }
	 }
		return edges_to_paint.toArray();
	*/
}

