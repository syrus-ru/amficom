package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Hashtable;

public class ScalableFrame extends SimpleResizableFrame
{
	protected Hashtable panels = new Hashtable();

	public ScalableFrame (ResizableLayeredPanel panel)
	{
		super (panel);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public ScalableFrame()
	{
		this (new ScalableLayeredPanel());
	}

	private void jbInit() throws Exception
	{
	}

	public void addGraph (SimpleGraphPanel p, String id)
	{
		if (panel instanceof ScalableLayeredPanel)
		{
			p.setColorModel (id);
			((ScalableLayeredPanel)panel).addGraphPanel(p);
			panels.put(id, p);
		}
	}

	public void removeGraph (String id)
	{
		if (id.equals("all"))
		{
			((ScalableLayeredPanel)panel).removeAllGraphPanels();
			panels = new Hashtable();
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)panels.get(id);
			if (p != null)
			{
				((ScalableLayeredPanel)panel).removeGraphPanel(p);
				((ScalableLayeredPanel)panel).updScale2fit();
				panels.remove(id);
			}
		}
	}

	public void setGraph (double[] y, double delta_x, boolean is_reversed_y, String id)
	{
		TraceEventsPanel p = new TraceEventsPanel((ScalableLayeredPanel)panel, y, delta_x);
		setGraph (p, is_reversed_y, id);
	}

	public void setGraph (ScaledGraphPanel p, boolean is_reversed_y, String id)
	{
		panels = new Hashtable();
		panels.put(id, p);
		super.setGraph(p, is_reversed_y, id);
		p.select_by_mouse = true;
	}
}