package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.io.IOException;
import java.util.Hashtable;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;

import com.syrus.AMFICOM.Client.Analysis.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.ByteArray;

public class ThresholdsFrame extends SimpleResizableFrame implements OperationListener
{
	private Dispatcher dispatcher;
	Hashtable traces = new Hashtable();

	public ThresholdsFrame(Dispatcher dispatcher)
	{
		super (new ThresholdsLayeredPanel(dispatcher));

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
		setTitle(LangModelAnalyse.String("thresholdsTitle"));
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
				removeTrace(id);
				if (id.equals("all"))
					setVisible (false);
			}
			if (rce.OPEN_ETALON)
			{
				String et_id = (String)rce.getSource();
				addEtalon(et_id);
			}
			if (rce.CLOSE_ETALON)
			{
				String et_id = (String)rce.getSource();
				removeEtalon(et_id);
			}
		}
	}

	void addEtalon(String id)
	{
		if (traces.get(id) != null)
			return;
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs != null)
			addTrace (id);
		else
		{
			int n = 0;
			double delta_x = 0;

			TestArgumentSet metas = (TestArgumentSet)Pool.get(TestArgumentSet.typ, id);
			double len = 0;
			try
			{
				for (int i = 0; i <metas.arguments.size(); i++)
				{
					Parameter p = (Parameter)metas.arguments.get(i);
					if (p.codename.equals("ref_res"))
					{
						delta_x = new ByteArray(p.value).toDouble();
					}
					if (p.codename.equals("ref_trclen"))
					{
						len = new ByteArray(p.value).toDouble();
					}
				}
				if (delta_x == 0 || len == 0)
					return;
				n = (int)(len * 1000 / delta_x);

				SimpleGraphPanel oldpanel = (SimpleGraphPanel)traces.get(id);
				if (oldpanel != null)
				{
					((ScalableLayeredPanel)panel).removeGraphPanel(oldpanel);
					traces.remove(id);
				}

				SimpleGraphPanel epPanel;
				ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);
				if (ep != null)
				{
					double[] y = new double[n];
					for (int i = 0; i < ep.length; i++)
					{
						for (int j = ep[i].begin; j <= ep[i].end && j < n; j++)
							y[j] = ep[i].refAmpl(j)[0];
					}
					epPanel = new SimpleGraphPanel(y, delta_x);
					epPanel.setColorModel("etalon");
					((ScalableLayeredPanel)panel).addGraphPanel(epPanel);
					panel.updScale2fit();
					((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
					traces.put(id, epPanel);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	void removeEtalon(String et_id)
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(et_id);
		panel.removeGraphPanel(epPanel);
	}

	void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);

		double delta_x = bs.getDeltaX();
		double[] y = bs.getTraceData();

		if (id.equals("primarytrace") || id.equals("modeledtrace"))
		{
			p = new ThresholdsPanel((ThresholdsLayeredPanel)panel, dispatcher, y, delta_x);
			ReflectogramEvent[] ep = ((ReflectogramEvent[])Pool.get("eventparams", id));
			((ThresholdsPanel)p).updateEvents(ep);
			((ThresholdsPanel)p).updEvents(id);
			((ThresholdsPanel)p).updateNoiseLevel();
			((ThresholdsPanel)p).draw_min_trace_level = true;
		}
		else
		{
			p = new SimpleGraphPanel(y, delta_x);
		}
		((ThresholdsLayeredPanel)panel).addGraphPanel(p);
		panel.updScale2fit();
		((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
		p.setColorModel(id);
		traces.put(id, p);

		setVisible(true);
	}

	void removeTrace (String id)
	{
		if (id.equals("all"))
		{
			((ThresholdsLayeredPanel)panel).removeAllGraphPanels();
			traces = new Hashtable();
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
			if (p != null)
			{
				panel.removeGraphPanel(p);
				traces.remove(id);
				((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
			}
		}
	}
}
