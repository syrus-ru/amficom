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
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/10/04 15:00:31 $
 * @module
 */
public class ActiveReflectogramPanel extends EnhancedReflectogramPanel {
	private static final long serialVersionUID = 7682534739688705176L;

	protected Dispatcher dispatcher;

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

		// пытаемся начать перемещение какого-нибудь LevelLine
		if (this.minTraceLevel.startMoving(this.currpos)
				|| this.eotDetectionLevel.startMoving(this.currpos)) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			return;
		}
		super.this_mousePressed(e);
	}

	@Override
	protected void this_mouseReleased(final MouseEvent e) {
		if (this.minTraceLevel.isMoving()) {
			this.upd_currpos(e);
			this.minTraceLevel.endMoving(this.currpos);
		} else if (this.eotDetectionLevel.isMoving()) {
			this.upd_currpos(e);
			this.eotDetectionLevel.endMoving(this.currpos);
		} else {
			super.this_mouseReleased(e);
			return;
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.parent.repaint();
	}

	@Override
	protected void this_mouseDragged(final MouseEvent e) {
		if (this.minTraceLevel.isMoving()) {
			this.upd_currpos(e);
			this.minTraceLevel.continueMoving(this.currpos);
		} else if (this.eotDetectionLevel.isMoving()) {
			this.upd_currpos(e);
			this.eotDetectionLevel.continueMoving(this.currpos);
		} else {
			super.this_mouseDragged(e);
			return;
		}
		this.parent.repaint();
		this.dispatcher.firePropertyChange(new RefUpdateEvent(this, RefUpdateEvent.MIN_TRACE_LEVEL_CHANGED_EVENT));
		return;
	}
}
