package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.analysis.dadara.*;

public class ThresholdsPanel extends ReflectogramEventsPanel
{
	public boolean paint_all_thresholds = false;
	public boolean paint_thresholds = true;

	protected boolean edit_thresholds = true;

	protected ReflectogramEvent[] et_ep;

	protected int c_event = 0;
	protected int c_threshold = -1;
	protected boolean isRbutton = false;
	protected double max_et_y = 0;
	
	// XXX: read colors from file?
	protected static Color warningThresholdColor = new Color(255, 220, 0);
	protected static Color alarmThresholdColor = new Color(255, 150, 60);

	public ThresholdsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double delta_x)
	{
		super (panel, dispatcher, y, delta_x);
		
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{ // empty
	}

	public void updateThresholds(ReflectogramEvent[] ep)
	{
//		super.updateEvents(ep);

		if (ep != null)
		{
			//WorkWithReflectoEventsArray.shiftDataToEtalon(ep, super.ep);
			this.et_ep = ep;

			max_et_y = ReflectogramMath.getMaximum(ep);//max_y;//

			if (c_event >= ep.length)
				c_event = ep.length - 1;
		}
	}

	public void showEvent (int num)
	{
		if (events == null)
			return;
		if (num == -1)
			return;

		if (et_ep != null && c_event >= et_ep.length)
			c_event = et_ep.length - 1;
		else
			c_event = num;

		start = events[num].first_point;
		end = events[num].last_point;
	}

	protected void this_mousePressed(MouseEvent e)
	{
		if (!edit_thresholds || et_ep == null)
		{
			super.this_mousePressed(e);
			return;
		}

		startpos = e.getPoint();
		currpos = e.getPoint();
		if (SwingUtilities.isRightMouseButton(e))
			isRbutton = true;
		else
			isRbutton = false;
		
		// определяем позицию, на котороую попали
		int index = coord2index(currpos.x);

		// если это текущее событие - пытаемся "ухватить" (drag) порог
		if (index >= et_ep[c_event].getBegin() && index <= et_ep[c_event].getEnd())
		{
			
			int new_threshold = -1;
	
			if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.UP2).refAmplitude(index))) < mouse_coupling)
				new_threshold = Threshold.UP2;
			else
			if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.DOWN2).refAmplitude(index))) < mouse_coupling)
				new_threshold = Threshold.DOWN2;
			else
			if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.UP1).refAmplitude(index))) < mouse_coupling)
				new_threshold = Threshold.UP1;
			else
			if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.DOWN1).refAmplitude(index))) < mouse_coupling)
				new_threshold = Threshold.DOWN1;
			
			if (new_threshold != -1)
			{
				if (et_ep[c_event].getThresholdType() == ReflectogramEvent.CONNECTOR)
				{
					c_threshold = new_threshold;
					setCursor(new Cursor(Cursor.MOVE_CURSOR));
					return;
				}
				else if (!isRbutton)
				{
					c_threshold = new_threshold;
					setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
					return;
				}
			}
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (!edit_thresholds || et_ep == null)
		{
			super.this_mouseDragged(e);
			return;
		}
		if (c_threshold != -1)
		{
			//long t0 = System.currentTimeMillis();
			upd_currpos(e);

			//long t1 = System.currentTimeMillis();

			if (!isRbutton)
				et_ep[c_event].getThreshold().changeThresholdBy(
						(double) (tmppos.y - currpos.y) / scale_y,
						(double) (tmppos.x - currpos.x) / scale_x,
						0, 0,
						et_ep[c_event], c_threshold);
			if (isRbutton
					&& (et_ep[c_event].getThresholdType() == ReflectogramEvent.CONNECTOR))
				et_ep[c_event].getThreshold().changeThresholdBy(
						0, 0,
						(double) (tmppos.x - currpos.x) / scale_x,
						(double) (tmppos.y - currpos.y) / scale_y,
						et_ep[c_event], c_threshold);

			//long t2 = System.currentTimeMillis();

			parent.repaint();

			//long t3 = System.currentTimeMillis();

			dispatcher.notify(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));

			//long t4 = System.currentTimeMillis();
			//System.out.println("t0 " + t0 + " dt1 " + (t1-t0) + " dt2 " + (t2-t1) + " dt3 " + (t3-t2) + " dt4 " + (t4-t3));
		}
		else
			// иначе выделяем область квадратиком
			super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (!edit_thresholds || et_ep == null)
		{
			super.this_mouseReleased(e);
			return;
		}
		if (c_threshold != -1)
		{
			c_threshold = -1;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		else
		{
			super.this_mouseReleased(e);
		}
	}
	
	protected void this_mouseClicked(MouseEvent e)
	{
	    // если кликнули, но не на текущее событие, переходим к новому событию
	    int evId = -1;
	    ReflectogramEvent[] events = et_ep;
		if (events != null)
		{
		    int pos = coord2index(e.getPoint().x);
			for(int j=0; j<events.length; j++)
				if(pos > events[j].getBegin() && pos <= events[j].getEnd())
					evId = j;
		}
		
		if (evId != -1 && evId != c_event)
		{
		    dispatcher.notify(
		            new RefUpdateEvent(String.valueOf(evId), RefUpdateEvent.EVENT_SELECTED_EVENT)
		    );
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
			if(paint_all_thresholds)
				paint_all_thresholds(g);
			else
				paint_threshold(g);
		}
	}
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
}