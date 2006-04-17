/*-
 * $Id: MultipleTracesFrame.java,v 1.1 2006/04/17 11:40:52 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.client.event.Dispatcher;

public class MultipleTracesFrame extends ScalableFrame implements PropertyChangeListener {
	protected Map<String,SimpleGraphPanel> traces = new HashMap<String,SimpleGraphPanel>();
	protected Dispatcher dispatcher;
	
	public MultipleTracesFrame(Dispatcher dispatcher, ResizableLayeredPanel panel) {
		super(panel);
		init_module(dispatcher);
	}
	
	private void init_module(Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
		this.dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(RefUpdateEvent.typ)) {
			RefUpdateEvent ev = (RefUpdateEvent)evt;
			if (ev.traceChanged()) {
				TraceResource tr = (TraceResource)evt.getNewValue();
				SimpleGraphPanel p = this.traces.get(tr.getId());
				if (p != null) {
					p.setShowAll(tr.isShown());
					super.panel.repaint();
				}
			}
		}
	}
	
	public void addGraph (SimpleGraphPanel p, String id) {
		if (this.panel instanceof ScalableLayeredPanel) {
			p.setColorModel (id);
			((ScalableLayeredPanel)this.panel).addGraphPanel(p);
			this.traces.put(id, p);
		} else {
			setGraph(p, false, id);
		}
	}

	public void removeGraph (String id) {
		if (id.equals("all")) {
			this.panel.removeAllGraphPanels();
			this.traces.clear();
		} else {
			SimpleGraphPanel p = this.traces.get(id);
			if (p != null) {
				this.panel.removeGraphPanel(p);
				this.panel.updScale2fit();
				this.traces.remove(id);
			}
		}
	}


	@Override
	public void setGraph (double[] y, double deltaX, boolean isReversedY, String id) {
		TraceEventsPanel p = new TraceEventsPanel(this.panel, y, deltaX, false);
		setGraph (p, isReversedY, id);
	}
	
	public void setGraph (ScaledGraphPanel p, boolean isReversedY, String id) {
		this.traces.clear();
		super.setGraph(p, isReversedY, id);
		p.select_by_mouse = true;
		this.traces.put(id, p);
	}
}
