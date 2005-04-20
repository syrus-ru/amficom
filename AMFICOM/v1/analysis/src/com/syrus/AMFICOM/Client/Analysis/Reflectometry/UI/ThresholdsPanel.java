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
	//public boolean paint_all_thresholds = false;
	public boolean paint_thresholds = true;

	protected boolean edit_thresholds = true;

	protected ModelTraceManager et_mtm;

	protected int c_event = 0;
	protected ModelTraceManager.ThresholdHandle c_TH = null;
	protected boolean isRbutton = false;

	public ThresholdsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super (panel, dispatcher, y, deltaX);		
	}

	public void updateThresholds(ModelTraceManager mtm)
	{
//		updateTrace(mtm); // XXX: на что влияет? нужно ли?

		if (mtm == null)
			return;

		this.et_mtm = mtm;

		if (c_event >= mtm.getMTAE().getNEvents())
			c_event = mtm.getMTAE().getNEvents() - 1;
	}

	// XXX: transient code (slow refactoring);
	// to be used in ThresholdsLayeredPanel.
	// Note: creates an int[2] object
	public int[] getStartAndEndOfCurrentEvent()
	{
		if (c_event >= events.length)
			c_event = events.length - 1;
		int start1 = events[c_event].first_point;
		if (c_event == 0)
			start1 = 2;
		int end1 = events[c_event].last_point;
		return new int[] {start1, end1};
	}

	public void showEvent (int num)
	{
		if (events == null)
			return;
		if (num == -1)
			return;

		if (et_mtm != null && c_event >= et_mtm.getMTAE().getNEvents())
			c_event = et_mtm.getMTAE().getNEvents() - 1;
		else
			c_event = num;

		start = events[num].first_point;
		end = events[num].last_point;
	}
	
	protected void this_mousePressed(MouseEvent mev)
	{
		if (!edit_thresholds || et_mtm == null)
		{
			super.this_mousePressed(mev);
			return;
		}

		startpos = mev.getPoint();
		currpos = mev.getPoint();
		isRbutton = SwingUtilities.isRightMouseButton(mev);

		boolean allThresholds = this.isToPaintAllThresholds(); // режим "все пороги"
		boolean isOutside = false; // true если клик вне x-пределов текущего события

		if (!allThresholds) // определяем isOutside
		{
			// проверяем, попадает ли X мыши в область текущего события
			if (this.c_event >= 0)
			{
				SimpleReflectogramEvent simpleEvent = this.et_mtm.getMTAE().getSimpleEvent(this.c_event);
				double currposF = coord2indexF(this.currpos.x);
				double mouseCouplingF = MOUSE_COUPLING / this.scaleX;
				if (!(currposF >= simpleEvent.getBegin() - mouseCouplingF
						&& currposF <= simpleEvent.getEnd() + mouseCouplingF ))
					isOutside = true;
			}
			else
				isOutside = true; // текущее событие не выбрано
		}

		// если не isOutside, то пытаемся "ухватить" (drag) порог
		this.c_TH = isOutside
			? null
			: this.et_mtm.getThresholdHandle(
			coord2indexF(this.currpos.x), // we need float value, without rounding
			coord2value(this.currpos.y),
			MOUSE_COUPLING / this.scaleX,
			MOUSE_COUPLING / this.scaleY,
			0.5,
			isRbutton ? 1 : 0);

		// если режим !allThresholds, игнорируем хватания за посторонние пороги
		// @todo: это тоже не самая лучшая проверка.
		if (c_TH != null && !allThresholds)
		{
			if (c_event < 0 || !c_TH.isRelevantToNEvent(c_event))
				c_TH = null;
		}

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
		if (!edit_thresholds || et_mtm == null)
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
		if (!edit_thresholds || et_mtm == null || c_TH == null)
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
	    int evId = et_mtm != null ? et_mtm.getMTAE().getEventByCoord(pos) : -1;
		if (evId != -1 && evId != c_event)
		{
	    	Heap.setCurrentEvent(evId);
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
				paint_all_thresholds(g);
			else
				paint_one_threshold(g);
		}
	}

	/**
	 * Paints one threshold or all thresholds.
	 * @param g graphics
	 * @param nEvent event number >= 0 to paint or -1 to paintall thresholds. 
	 */
	private void paintThresholdsEx(Graphics g, int nEvent)
	{
		if (et_mtm == null)
			return;

		for (int key = 0; key < 4; key++)
		{
			g.setColor(UIManager.getColor(Thresh.IS_KEY_HARD[key]
			     ? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
			     : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));
			// XXX: нет draw_joint_of_two_model_curves
			ModelTrace thresholdMT = et_mtm.getThresholdMT(key);
			// FIXME: debug code
			SimpleReflectogramEvent sre = null;
			if (nEvent >= 0)
			{
				sre = et_mtm.getEventRangeOnThresholdCurve(nEvent, key);
				if (sre == null)
					continue;
			}
			drawModelCurve(g, thresholdMT, sre, false);
		}
	}

	private void paint_one_threshold(Graphics g)
	{
		if (c_event >= 0)
			paintThresholdsEx(g, c_event);
	}
	private void paint_all_thresholds(Graphics g)
	{
		paintThresholdsEx(g, -1);
	}

	/*
	void paint_all_thresholds(Graphics g)
	{
		if(et_ep == null)
			return;

		ReflectogramEvent []up1   = ReflectogramMath.getThreshold(et_ep, 0);
		ReflectogramEvent []up2   = ReflectogramMath.getThreshold(et_ep, 1);
		ReflectogramEvent []down1 = ReflectogramMath.getThreshold(et_ep, 2);
		ReflectogramEvent []down2 = ReflectogramMath.getThreshold(et_ep, 3);
		
		ReflectogramEvent[][] p_curves = { up1, down1, up2, down2 };
		Color[] p_colors = {warningThresholdColor, warningThresholdColor, alarmThresholdColor, alarmThresholdColor };
		
		for (int key = 0; key < 4; key++)
		{
			g.setColor(p_colors[key]);
			for(int j=0; j<et_ep.length; j++)
			{
				((Graphics2D) g).setStroke(DASHED_STROKE);
				if (j != 0)
					draw_joint_of_two_model_curves(g, p_curves[key][j-1], p_curves[key][j]);
				((Graphics2D) g).setStroke(j == c_event ? DEFAULT_STROKE : DASHED_STROKE);
				draw_one_model_curve(g, p_curves[key][j]);
			}
		}
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	void paint_threshold (Graphics g)
	{
		if (et_ep == null)
			return;

		ReflectogramEvent up1   = et_ep[c_event].getThresholdReflectogramEvent(0);
		ReflectogramEvent up2   = et_ep[c_event].getThresholdReflectogramEvent(1);
		ReflectogramEvent down1 = et_ep[c_event].getThresholdReflectogramEvent(2);
		ReflectogramEvent down2 = et_ep[c_event].getThresholdReflectogramEvent(3);

		g.setColor(warningThresholdColor);
		draw_one_model_curve(g, up1);
		draw_one_model_curve(g, down1);
		g.setColor(alarmThresholdColor);
		draw_one_model_curve(g, up2);
		draw_one_model_curve(g, down2);
	}
	*/

	/*
	private double getShift(ReflectogramEvent []etalon, double []data)
	{
		if(data == null || etalon == null || etalon.length<1)
			return 0.;

		double maxEtalon = -1000.;
		double maxData   = -1000.;

		for(int i=etalon[0].getBegin(); i<=etalon[0].getEnd() && i<data.length; i++)
		{
			if(maxEtalon<etalon[0].refAmplitude(i))
				maxEtalon=etalon[0].refAmplitude(i);
			if(maxData<data[i])
				maxData = data[i];
		}

		return (maxData - maxEtalon);
	}
	*/

	private boolean isToPaintAllThresholds()
	{
		return parent instanceof ThresholdsLayeredPanel
			&& ((ThresholdsLayeredPanel )parent).hasShowThresholdButtonSelected();
	}
}
