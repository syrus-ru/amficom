package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.io.BellcoreStructure;

public class HistogrammFrame extends ScalableFrame
implements BsHashChangeListener, AnalysisParametersListener
{
	Dispatcher dispatcher;
	public HistogrammFrame(Dispatcher dispatcher)
	{
		super (new HistogrammLayeredPanel(dispatcher));

		init_module(dispatcher);
		try
		{
			jbInit();
		} catch(Exception e)
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
		Heap.addBsHashListener(this);
        Heap.addAnalysisParametersListener(this);
	}

	public void addTrace (String id)
	{
		if (id.equals(Heap.PRIMARY_TRACE_KEY) || id.equals(Heap.MODELED_TRACE_KEY))
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

    public void analysisParametersUpdated() {
        JLayeredPane slp = ((ScalableLayeredPanel)panel).jLayeredPane;
        for(int i=0; i<slp.getComponentCount(); i++)
        {
            JPanel panel1 = (JPanel)slp.getComponent(i);
            if (panel1 instanceof HistogrammPanel)
            {
                ((HistogrammPanel)panel1).updAnalysisParameters();
            }
        }
    }
}
