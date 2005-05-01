package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThresholdHandle;

/**
 * Отображает пороги
 */
public class ThresholdsPanel extends ReflectogramEventsPanel
{
	protected boolean paint_thresholds = true;
	protected boolean edit_thresholds = true;

	private ModelTraceManager etalon;

	private int c_event = 0;
	private ModelTraceManager.ThresholdHandle c_TH = null;

	public ThresholdsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super (panel, dispatcher, y, deltaX);		
	}

	// we rely upon being up-to-date informed on MTMEtalon modification
	// Otherwise, our comunication with Heap.etalonEvent may not be accurate
	public void updateEtalon()
	{
		etalon = Heap.getMTMEtalon();

		if (etalon == null)
		{
			c_event = 0;
			c_TH = null;
			return;
		}

		if (c_event >= etalon.getMTAE().getNEvents())
			c_event = etalon.getMTAE().getNEvents() - 1;
	}

	// XXX: transient code (slow refactoring);
	// to be used in ThresholdsLayeredPanel.
	// Performance Note: creates an int[2] object
	// Design Note: uses TraceEventsPanel.events
	public int[] getStartAndEndOfCurrentEvent()
	{
		int num = Heap.getCurrentEvent();
		if (num < 0)
			return new int[] {0, events[events.length-1].last_point};
		if (num >= events.length)
			num = events.length - 1;
		int start1 = num > 0 ? events[num].first_point : 2;
		int end1 = events[num].last_point;
		return new int[] {start1, end1};
	}

	public void updateCurrentEvent()
	{
		// Design Note: uses TraceEventsPanel.events
		if (events == null)
			return;

		c_event = Heap.getCurrentEtalonEvent();

		if (etalon != null && c_event >= etalon.getMTAE().getNEvents())
			c_event = etalon.getMTAE().getNEvents() - 1;

		int num = Heap.getCurrentEvent();

		if (num >= 0) // XXX
		{
			start = events[num].first_point;
			end = events[num].last_point;
		}
	}
	
	protected void this_mousePressed(MouseEvent mev)
	{
		if (!edit_thresholds || etalon == null)
		{
			super.this_mousePressed(mev);
			return;
		}

		startpos = mev.getPoint();
		currpos = mev.getPoint();
		boolean isRbutton = SwingUtilities.isRightMouseButton(mev);

		boolean allThresholds = this.isToPaintAllThresholds(); // режим "все пороги"

		// пытаемся "ухватить" (drag) порог
		this.c_TH = this.etalon.getThresholdHandle(
			coord2indexF(this.currpos.x), // we need float value, without rounding
			coord2value(this.currpos.y),
			MOUSE_COUPLING / this.scaleX,
			MOUSE_COUPLING / this.scaleY,
			0.5,
			isRbutton ? 1 : 0,
			allThresholds ? -1 : c_event);

		if (this.c_TH != null) {

			switch (this.c_TH.getType()) {
				case ThresholdHandle.HORIZONTAL_LEFT_TYPE:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					break;
				case ThresholdHandle.HORIZONTAL_RIGHT_TYPE:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					break;
				case ThresholdHandle.VERTICAL_UP_TYPE:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
					break;
				case ThresholdHandle.VERTICAL_DOWN_TYPE:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
					break;

			} 
			// перемещаем мышь в точку захвата
			try
			{
				Robot r = new Robot();
				Point p = getLocationOnScreen();
				int x1 = index2coord(c_TH.getX());
				int y1 = value2coord(c_TH.getY());

				// не допускаем выхода на пределы окна
				currpos = new Point(x1, y1);
				limit_currpos();

				// перемещаем курсор
				r.mouseMove(currpos.x + p.x, currpos.y + p.y);
			}
			catch (AWTException ex)
			{
				System.out.println("ThresholdsPanel: Warning: MouseMove failed");
				ex.printStackTrace();
			}

			return;
		}

		super.this_mousePressed(mev);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (!edit_thresholds || etalon == null)
		{
			super.this_mouseDragged(e);
			return;
		}
		if (c_TH != null)
		{
			upd_currpos(e); // теперь tmppos - предыдущее положение, а  currpos - новое
			c_TH.moveBy(
				(currpos.x - tmppos.x) / getTrueScaleX(),
				(currpos.y - tmppos.y) / getTrueScaleY());

			parent.repaint();

			dispatcher.notify(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
		else
			// иначе выделяем область квадратиком
			super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (!edit_thresholds || etalon == null || c_TH == null)
		{
			super.this_mouseReleased(e);
		}
		else
		{
			if (c_TH != null)
			{
				c_TH.release();
				parent.repaint();
				dispatcher.notify(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				c_TH = null;
			}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	protected void this_mouseClicked(MouseEvent e)
	{
	    // если кликнули, но не на текущее событие, переходим к новому событию
	    int pos = coord2index(e.getPoint().x);
	    int evId = etalon != null ? etalon.getMTAE().getEventByCoord(pos) : -1;
		if (evId != -1 && evId != c_event)
		{
	    	Heap.setCurrentEtalonEvent(evId);
		    return;
		}
	}

	public void paint (Graphics g)
	{
		paint_scales(g);

		if (draw_events)
		{
			paint_reflectogram_events(g);
		}
		else
		{
			paint_trace(g);
		}
		if (draw_modeled)
		{
			paint_modeled_trace(g);
		}

		if (draw_alarms)
			paint_alarms(g);

		if (draw_min_trace_level && draw_events)
		{
			paint_noise_level(g);
			paint_min_trace_level(g);
		}
		else if (draw_noise_level && draw_events)
			paint_noise_level(g);

		paint_scale_digits(g);

		if (paint_thresholds)
		{
			if(isToPaintAllThresholds())
				paintAllThresholds(g);
			else
				paintOneThreshold(g);
		}
	}

	/**
	 * Paints one threshold or all thresholds.
	 * @param g graphics
	 * @param nEvent event number >= 0 to paint or -1 to paint all thresholds. 
	 */
	private void paintThresholdsEx(Graphics g, int nEvent)
	{
		if (etalon == null)
			return;

		for (int key = 0; key < 4; key++)
		{
			g.setColor(UIManager.getColor(Thresh.isKeyHard(key)
			     ? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
			     : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));
			// Note: нет draw_joint_of_two_model_curves
			ModelTrace thresholdMT = etalon.getThresholdMT(key);
			// Определяем диапазон отрисовки
			SimpleReflectogramEvent sre = null;
			if (nEvent >= 0)
			{
				sre = etalon.getEventRangeOnThresholdCurve(nEvent, key);
				if (sre == null)
					continue;
			}
			// If we draw thresholds for one event only, avoid drawing thresholds at the end point.
			// This is because sometimes (n/id event type) threshold curve can break.
			drawModelCurve(g, thresholdMT, sre, nEvent >= 0);
		}
	}

	private void paintOneThreshold(Graphics g)
	{
		if (c_event >= 0)
			paintThresholdsEx(g, c_event);
	}
	private void paintAllThresholds(Graphics g)
	{
		paintThresholdsEx(g, -1);
	}

	private boolean isToPaintAllThresholds()
	{
		return parent instanceof ThresholdsLayeredPanel
			&& ((ThresholdsLayeredPanel )parent).hasShowThresholdButtonSelected();
	}
}
