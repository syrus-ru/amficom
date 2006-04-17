package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

public class ScalableFrame extends SimpleResizableFrame {
	public ScalableFrame (ResizableLayeredPanel panel) {
		super (panel);
	}

	public ScalableFrame() {
		this (new ScalableLayeredPanel());
	}

	@Override
	public void setGraph (double[] y, double deltaX, boolean isReversedY, String id) {
		TraceEventsPanel p = new TraceEventsPanel(this.panel, y, deltaX, false);
		setGraph (p, isReversedY, id);
	}

	public void setGraph (ScaledGraphPanel p, boolean isReversedY, String id) {
		super.setGraph(p, isReversedY, id);
		p.select_by_mouse = true;
	}
}
