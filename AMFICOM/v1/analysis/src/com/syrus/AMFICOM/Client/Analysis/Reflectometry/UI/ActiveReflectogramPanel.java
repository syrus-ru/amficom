package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * Интерактивная панель с рефлектограммой - может отслеживать и изменять
 * уровень шума
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/09/07 02:56:49 $
 * @module
 */
public class ActiveReflectogramPanel extends EnhancedReflectogramPanel {
	private static final long serialVersionUID = 7682534739688705176L;

	protected Dispatcher dispatcher;
	protected boolean moving_level = false;

	public ActiveReflectogramPanel(final TraceEventsLayeredPanel panel,
			final Dispatcher dispatcher,
			final double[] y,
			final double deltaX) {
		super(panel, y, deltaX);
		this.initModule(dispatcher);
	}

	void initModule(final Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
	}

	public void updateNoiseLevel() {
		final RefAnalysis ana = Heap.getRefAnalysisPrimary();
		if (ana != null) {
			final TraceEvent ev = ana.overallStats;
			if (ev != null) {
				this.noise_level = ev.overallStatsNoiseLevel98Pct();
			}
		}
	}

	@Override
	protected void this_mousePressed(final MouseEvent e) {
		this.startpos = e.getPoint();
		this.currpos = e.getPoint();

		if (this.draw_min_trace_level && hasMinTraceLevel()) {
			if (super.coord2index(this.currpos.x) > this.y.length) {
				return;
			}

			if (Math.abs(this.currpos.y - super.getMinTraceLevelCoord()) < MOUSE_COUPLING) {
				this.moving_level = true;
				this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		super.this_mousePressed(e);
	}

	@Override
	protected void this_mouseReleased(final MouseEvent e) {
		if (this.moving_level) {
			this.moving_level = false;
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

	@Override
	protected void this_mouseDragged(final MouseEvent e) {
		if (this.moving_level) {
			this.upd_currpos(e);

			final double pos = coord2value(this.currpos.y);
			Heap.setMinTraceLevel(pos);
			this.parent.repaint();
			this.dispatcher.firePropertyChange(new RefUpdateEvent(this, RefUpdateEvent.MIN_TRACE_LEVEL_CHANGED_EVENT));
			return;
		}
		super.this_mouseDragged(e);
	}
}
