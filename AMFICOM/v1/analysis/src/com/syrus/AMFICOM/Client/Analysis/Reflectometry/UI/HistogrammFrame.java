package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.io.BellcoreStructure;

public class HistogrammFrame extends ScalableFrame
implements OperationListener, bsHashChangeListener
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
		Heap.addBsHashListener(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
	}

	public void addTrace (String id)
	{
		if (id.equals(RefUpdateEvent.PRIMARY_TRACE) || id.equals("modeledtrace"))
		{
			HistogrammPanel p;

			BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
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

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
			addTrace (key);
			setVisible(true);
	}

	public void bsHashRemoved(String key)
	{
	}

	public void bsHashRemovedAll()
	{
		((ScalableLayeredPanel)panel).removeAllGraphPanels();
		setVisible (false);
	}
}
