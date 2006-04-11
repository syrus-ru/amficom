package com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScalableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TimeDependanceLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TimeDependencePanel;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.PredictionResourceKeys;

public class TimeDependenceFrame extends ScalableFrame implements BsHashChangeListener {
	public TimeDependenceFrame(Dispatcher dispatcher)
	{
		super(new TimeDependanceLayeredPanel(dispatcher));
		Heap.addBsHashListener(this);
		
		setTitle(I18N.getString(PredictionResourceKeys.FRAME_TIME_DEPENDANCE));
	}

	public void bsHashAdded(String key) {
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			setTrace(key);
			setVisible(true);
		}
	}
	
	public void setTrace (String id) {
		double[] y = new double[] {1, 30, 400};
		TimeDependencePanel p = new TimeDependencePanel(this.panel, y, 3);
		super.setGraph(p, true, id);
		p.updEvents(id);
		this.panel.setGraphPanel(p);
		this.panel.setInversedY(false);
	}

	public void bsHashRemoved(String key) {
		// nothing
	}

	public void bsHashRemovedAll() {
		setVisible(false);
	}
}