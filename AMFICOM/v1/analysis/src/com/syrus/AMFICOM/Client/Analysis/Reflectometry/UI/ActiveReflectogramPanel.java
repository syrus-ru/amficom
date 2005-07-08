package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * Интерактивная панель с рефлектограммой - может отслеживать и изменять
 * уровень шума
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/07/08 12:36:08 $
 * @module
 */
public class ActiveReflectogramPanel extends EnhancedReflectogramPanel
{
	protected Dispatcher dispatcher;
	protected boolean moving_level = false;

	public ActiveReflectogramPanel(TraceEventsLayeredPanel panel, Dispatcher dispatcher, double[] y, double deltaX)
	{
		super (panel, y, deltaX);
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
	}

	public void updateNoiseLevel()
	{
		RefAnalysis ana = Heap.getRefAnalysisPrimary();
		if (ana != null)
		{
			TraceEvent ev = ana.overallStats;
			if (ev != null)
				noise_level = ev.overallStatsNoiseLevel98Pct();
		}

		// FIXME: способ выбора minTraceLevel (оба не годятся -- saa)
		//updateMinTraceLevel(noise_level - 3); // по +3 дБ от ур. шума?
		ClientAnalysisManager.setDefaultMinTraceLevel(); // между мин. фит. кр. и абс. мин. р/г? 
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (draw_min_trace_level)
		{
			if (coord2index(currpos.x) > y.length)
				return;

			if(Math.abs(currpos.y - getMinTraceLevelCoord()) < MOUSE_COUPLING)
			{
				moving_level = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		super.this_mousePressed(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (moving_level)
		{
			moving_level = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (moving_level)
		{
			upd_currpos(e);

			double pos = coord2value(currpos.y);
			Heap.setMinTraceLevel(pos);
			parent.repaint();
			dispatcher.firePropertyChange(new RefUpdateEvent(this, RefUpdateEvent.MIN_TRACE_LEVEL_CHANGED_EVENT));
			return;
		}
		super.this_mouseDragged(e);
	}
}
