package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.BellcoreStructure;

public class HistogrammFrame extends ScalableFrame implements OperationListener
{
	Dispatcher dispatcher;
	public HistogrammFrame(Dispatcher dispatcher)
	{
		super (new HistogrammLayeredPanel(dispatcher));

		init_module(dispatcher);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setTitle(LangModelAnalyse.getString("histogrammTitle"));
	}

	public Dispatcher getInternalDispatcher ()
	{
		return dispatcher;
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				addTrace (id);
				setVisible(true);
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					((ScalableLayeredPanel)panel).removeAllGraphPanels();
					setVisible (false);
				}
			}
		}

	}

	public void addTrace (String id)
	{
		if (id.equals("primarytrace") || id.equals("modeledtrace"))
		{
			HistogrammPanel p;

			BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
			if (bs == null)
				return;

			double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			p = new HistogrammPanel(panel, y, deltaX);
			p.setColorModel(id);
			((ScalableLayeredPanel)panel).addGraphPanel(p);
			panel.updScale2fit();

			setVisible(true);
		}
	}
}
