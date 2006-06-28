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
		} catch(Exception e)
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
		if (this.panel instanceof ScalableLayeredPanel)
		{
			p.setColorModel (id);
			((ScalableLayeredPanel)this.panel).addGraphPanel(p);
			this.panels.put(id, p);
		}
	}

	public void removeGraph (String id)
	{
		if (id.equals("all"))
		{
			((ScalableLayeredPanel)this.panel).removeAllGraphPanels();
			this.panels = new HashMap();
		} else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)this.panels.get(id);
			if (p != null)
			{
				((ScalableLayeredPanel)this.panel).removeGraphPanel(p);
				((ScalableLayeredPanel)this.panel).updScale2fit();
				this.panels.remove(id);
			}
		}
	}

	@Override
	public void setGraph (double[] y, double deltaX, boolean isReversedY, String id)
	{
		TraceEventsPanel p = new TraceEventsPanel(this.panel, y, deltaX, false);
		setGraph (p, isReversedY, id);
	}

	public void setGraph (ScaledGraphPanel p, boolean isReversedY, String id)
	{
		this.panels = new HashMap();
		this.panels.put(id, p);
		super.setGraph(p, isReversedY, id);
		p.select_by_mouse = true;
	}
}
