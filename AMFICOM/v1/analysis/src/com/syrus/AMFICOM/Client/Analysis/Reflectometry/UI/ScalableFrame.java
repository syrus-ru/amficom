package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

public class ScalableFrame extends SimpleResizableFrame
{
	protected Map panels = new HashMap();

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
	{ // empty
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
			panels = new HashMap();
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

	public void setGraph (double[] y, double deltaX, boolean isReversedY, String id)
	{
		TraceEventsPanel p = new TraceEventsPanel((ScalableLayeredPanel)panel, y, deltaX);
		setGraph (p, isReversedY, id);
	}

	public void setGraph (ScaledGraphPanel p, boolean isReversedY, String id)
	{
		panels = new HashMap();
		panels.put(id, p);
		super.setGraph(p, isReversedY, id);
		p.select_by_mouse = true;
	}
}