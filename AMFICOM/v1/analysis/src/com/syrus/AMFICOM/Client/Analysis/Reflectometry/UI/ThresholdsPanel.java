package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class ThresholdsPanel extends ReflectogramEventsPanel
{
	public boolean paint_thresholds = true;
	public boolean paint_all_thresholds = false;
	public boolean edit_thresholds = true;

	protected ReflectogramEvent[] et_ep;

	protected int c_event = 0;
	protected int c_threshold = -1;
	protected boolean isRbutton = false;
	protected double max_et_y = 0;

	protected Color warningThresholdColor = new Color(255, 220, 0);
	protected Color alarmThresholdColor = new Color(255, 150, 60);

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
	{
	}

	public void updateThresholds(ReflectogramEvent[] ep)
	{
//		super.updateEvents(ep);

		if (ep != null)
		{/*
			if (this.thresholds != null && this.thresholds.length <= ep.length)
			{
				for (int i = 0; i < this.thresholds.length; i++)
				{
					Threshold t = this.thresholds[i].getThreshold();
					t.setReflectogramEvent(ep[i]);
					ep[i].setThreshold(t);
				}
			}
*/

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

		if (coord2index(currpos.x) > y.length)
			return;

//		if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThreshold().getThresholdValue(coord2index(currpos.x), Threshold.UP2))) < mouse_coupling)
		if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.UP2).refAmplitude(coord2index(currpos.x)))) < mouse_coupling)
		{
			if (et_ep[c_event].getType() == ReflectogramEvent.CONNECTOR)
			{
				c_threshold = Threshold.UP2;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
			else if (!isRbutton)
			{
				c_threshold = Threshold.UP2;
				setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			}
		}
		else //if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThreshold().getThresholdValue(coord2index(currpos.x), Threshold.DOWN2))) < mouse_coupling)
				if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.DOWN2).refAmplitude(coord2index(currpos.x)))) < mouse_coupling)
		{
			if (et_ep[c_event].getType() == ReflectogramEvent.CONNECTOR)
			{
				c_threshold = Threshold.DOWN2;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
				return;
			}
			else if (!isRbutton)
			{
				c_threshold = Threshold.DOWN2;
				setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		else //if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThreshold().getThresholdValue(coord2index(currpos.x), Threshold.UP1))) < mouse_coupling)
		if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.UP1).refAmplitude(coord2index(currpos.x)))) < mouse_coupling)
		{
			if (et_ep[c_event].getType() == ReflectogramEvent.CONNECTOR)
			{
				c_threshold = Threshold.UP1;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
				return;
			}
			else if (!isRbutton)
			{
				c_threshold = Threshold.UP1;
				setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		else //if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThreshold().getThresholdValue(coord2index(currpos.x), Threshold.DOWN1))) < mouse_coupling)
		if (Math.abs(currpos.y-value2coord(et_ep[c_event].getThresholdReflectogramEvent(Threshold.DOWN1).refAmplitude(coord2index(currpos.x)))) < mouse_coupling)
		{
			if (et_ep[c_event].getType() == ReflectogramEvent.CONNECTOR)
			{
				c_threshold = Threshold.DOWN1;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
				return;
			}
			else if (!isRbutton)
			{
				c_threshold = Threshold.DOWN1;
				setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				return;
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
			upd_currpos(e);

			if (!isRbutton)
				et_ep[c_event].getThreshold().changeThreshold((double)(tmppos.y - currpos.y)/scale_y, (double)(tmppos.x - currpos.x)/scale_x, 0, 0, c_threshold);
			if (isRbutton && (et_ep[c_event].getType() == ReflectogramEvent.CONNECTOR))
				et_ep[c_event].getThreshold().changeThreshold(0, 0, (double)(tmppos.x - currpos.x)/scale_x, (double)(tmppos.y - currpos.y)/scale_y, c_threshold);
			parent.repaint();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
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
			super.this_mouseReleased(e);
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

		for(int i=0; i<et_ep.length; i++)
		{

			g.setColor(warningThresholdColor);
			for(int j=Math.max(0, up1[i].begin - start); j<= Math.min (end, up1[i].end) - start && j<y.length; j++)
			{
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up1[i].refAmplitude(j+start) - top) * scale_y - 1),
				(int)((j+1)*scale_x+1), (int)((max_et_y - up1[i].refAmplitude(j+start+1) - top) * scale_y - 1));
			}
			for(int j=Math.max(0, down1[i].begin - start); j<= Math.min (end, down1[i].end) - start && j<y.length; j++)
			{
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down1[i].refAmplitude(j+start) - top) * scale_y - 1),
				(int)((j+1)*scale_x+1), (int)((max_et_y - down1[i].refAmplitude(j+start+1) - top) * scale_y - 1));
			}

			g.setColor(alarmThresholdColor);
			for(int j=Math.max(0, up2[i].begin - start); j<= Math.min (end, up2[i].end) - start && j<y.length; j++)
			{
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up2[i].refAmplitude(j+start) - top) * scale_y - 1),
				(int)((j+1)*scale_x+1), (int)((max_et_y - up2[i].refAmplitude(j+start+1) - top) * scale_y - 1));
			}
			for(int j=Math.max(0, down2[i].begin - start); j<= Math.min (end, down2[i].end) - start && j<y.length; j++)
			{
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down2[i].refAmplitude(j+start) - top) * scale_y - 1),
				(int)((j+1)*scale_x+1), (int)((max_et_y - down2[i].refAmplitude(j+start+1) - top) * scale_y - 1));
			}
		}


/*

		for(int i=0; i<et_ep.length; i++)
		{
			try
			{
				if(et_ep[i].threshold != null)
				{
					int st = Math.max (start, et_ep[i].begin);
					int en = Math.min (end, et_ep[i].end);
					ReflectogramEvent up1   = et_ep[i].getThresholdReflectogramEvent(0);
					ReflectogramEvent up2   = et_ep[i].getThresholdReflectogramEvent(1);
					ReflectogramEvent down1 = et_ep[i].getThresholdReflectogramEvent(2);
					ReflectogramEvent down2 = et_ep[i].getThresholdReflectogramEvent(3);

					if (et_ep[i].getType() == ReflectogramEvent.LINEAR)
					{
						g.setColor(warningThresholdColor);
						g.drawLine(index2coord(st), (int)((max_et_y - up1.refAmplitude(st) - top) * scale_y - 1),
											 index2coord(en), (int)((max_et_y - up1.refAmplitude(en) - top) * scale_y - 1));
						g.drawLine(index2coord(st), (int)((max_et_y - down1.refAmplitude(st) - top) * scale_y - 1),
											 index2coord(en), (int)((max_et_y - down1.refAmplitude(en) - top) * scale_y - 1));

						g.setColor(alarmThresholdColor);
						g.drawLine(index2coord(st), (int)((max_et_y - up2.refAmplitude(st) - top) * scale_y - 1),
											 index2coord(en), (int)((max_et_y - up2.refAmplitude(en) - top) * scale_y - 1));
						g.drawLine(index2coord(st), (int)((max_et_y - down2.refAmplitude(st) - top) * scale_y - 1),
											 index2coord(en), (int)((max_et_y - down2.refAmplitude(en) - top) * scale_y - 1));
					}
					else
					{
						for(int j=Math.max(0, et_ep[i].begin - (int)(start)); j<= Math.min (end, et_ep[i].end) - start; j++)// && j<y.length
						{
							g.setColor(warningThresholdColor);
							g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up1.refAmplitude(j+start) - top) * scale_y - 1),
												 (int)((j+1)*scale_x+1), (int)((max_et_y - up1.refAmplitude(j+start+1) - top) * scale_y - 1));
							g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down1.refAmplitude(j+start) - top) * scale_y - 1),
												 (int)((j+1)*scale_x+1), (int)((max_et_y - down1.refAmplitude(j+start+1) - top) * scale_y - 1));

							g.setColor(alarmThresholdColor);
							g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up2.refAmplitude(j+start) - top) * scale_y - 1),
												 (int)((j+1)*scale_x+1), (int)((max_et_y - up2.refAmplitude(j+start+1) - top) * scale_y - 1));
							g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down2.refAmplitude(j+start) - top) * scale_y - 1),
												 (int)((j+1)*scale_x+1), (int)((max_et_y - down2.refAmplitude(j+start+1) - top) * scale_y - 1));
						}
					}
				}
			}
			catch (Exception ex)
			{
			}
		}*/
	}

	void paint_threshold (Graphics g)
	{
		if (et_ep == null)
			return;
/*
		ReflectogramEvent []up1   = WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(et_ep, 0);
		ReflectogramEvent []up2   = WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(et_ep, 1);
		ReflectogramEvent []down1 = WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(et_ep, 2);
		ReflectogramEvent []down2 = WorkWithReflectoEventsArray.getThresholdReflectogramEventArray(et_ep, 3);

		if(et_ep[c_event].type == ReflectogramEvent.LINEAR)
		{
			g.setColor(warningThresholdColor);
					for(int j=Math.max(0, up1[c_event].begin - start); j<= Math.min (end, up1[c_event].end) - start && j<y.length; j++)
					{
							g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up1[c_event].refAmplitude(j+start) - top) * scale_y - 1),
							(int)((j+1)*scale_x+1), (int)((max_et_y - up1[c_event].refAmplitude(j+start+1) - top) * scale_y - 1));
					}
					for(int j=Math.max(0, down1[c_event].begin - start); j<= Math.min (end, down1[c_event].end) - start && j<y.length; j++)
					{
						g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down1[c_event].refAmplitude(j+start) - top) * scale_y - 1),
						(int)((j+1)*scale_x+1), (int)((max_et_y - down1[c_event].refAmplitude(j+start+1) - top) * scale_y - 1));
					}

			g.setColor(alarmThresholdColor);
					for(int j=Math.max(0, up2[c_event].begin - start); j<= Math.min (end, up2[c_event].end) - start && j<y.length; j++)
					{
						g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up2[c_event].refAmplitude(j+start) - top) * scale_y - 1),
										 (int)((j+1)*scale_x+1), (int)((max_et_y - up2[c_event].refAmplitude(j+start+1) - top) * scale_y - 1));
					}
					for(int j=Math.max(0, down2[c_event].begin - start); j<= Math.min (end, down2[c_event].end) - start && j<y.length; j++)
					{
						g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down2[c_event].refAmplitude(j+start) - top) * scale_y - 1),
						(int)((j+1)*scale_x+1), (int)((max_et_y - down2[c_event].refAmplitude(j+start+1) - top) * scale_y - 1));
					}
		}
		else
		{
			g.setColor(warningThresholdColor);

			int x1 = Math.max(0, up1[c_event].begin - start);
			int x2 = Math.min (end, up1[c_event].end - start);
			g.drawLine((int)(x1*scale_x+1), (int)((max_et_y - up1[c_event].refAmplitude(x1+start) - top) * scale_y - 1),
								 (int)(x2*scale_x+1), (int)((max_et_y - up1[c_event].refAmplitude(x2+start) - top) * scale_y - 1));

			x1 = Math.max(0, down1[c_event].begin - start);
			x2 = Math.min (end, down1[c_event].end - start);
			g.drawLine((int)(x1*scale_x+1), (int)((max_et_y - down1[c_event].refAmplitude(x1+start) - top) * scale_y - 1),
								 (int)(x2*scale_x+1), (int)((max_et_y - down1[c_event].refAmplitude(x2+start) - top) * scale_y - 1));



			g.setColor(alarmThresholdColor);

			x1 = Math.max(0, up2[c_event].begin - start);
			x2 = Math.min (end, up2[c_event].end - start);
			g.drawLine((int)(x1*scale_x+1), (int)((max_et_y - up2[c_event].refAmplitude(x1+start) - top) * scale_y - 1),
								 (int)(x2*scale_x+1), (int)((max_et_y - up2[c_event].refAmplitude(x2+start) - top) * scale_y - 1));

			x1 = Math.max(0, down2[c_event].begin - start);
			x2 = Math.min (end, down2[c_event].end - start);
			g.drawLine((int)(x1*scale_x+1), (int)((max_et_y - down2[c_event].refAmplitude(x1+start) - top) * scale_y - 1),
								 (int)(x2*scale_x+1), (int)((max_et_y - down2[c_event].refAmplitude(x2+start) - top) * scale_y - 1));
		}
*/


		int st = Math.max (start, et_ep[c_event].begin);
		int en = et_ep[c_event].end;

		ReflectogramEvent up1   = et_ep[c_event].getThresholdReflectogramEvent(0);
		ReflectogramEvent up2   = et_ep[c_event].getThresholdReflectogramEvent(1);
		ReflectogramEvent down1 = et_ep[c_event].getThresholdReflectogramEvent(2);
		ReflectogramEvent down2 = et_ep[c_event].getThresholdReflectogramEvent(3);


		if (et_ep[c_event].getType() == ReflectogramEvent.LINEAR)
		{
			g.setColor(warningThresholdColor);
			g.drawLine(index2coord(st), (int)((max_et_y - up1.refAmplitude(st) - top) * scale_y - 1),
								 index2coord(en), (int)((max_et_y - up1.refAmplitude(en) - top) * scale_y - 1));

			g.drawLine(index2coord(st), (int)((max_et_y - down1.refAmplitude(st) - top) * scale_y - 1),
								 index2coord(en), (int)((max_et_y - down1.refAmplitude(en) - top) * scale_y - 1));

			g.setColor(alarmThresholdColor);
			g.drawLine(index2coord(st), (int)((max_et_y - up2.refAmplitude(st) - top) * scale_y - 1),
								 index2coord(en), (int)((max_et_y - up2.refAmplitude(en) - top) * scale_y - 1));

			g.drawLine(index2coord(st), (int)((max_et_y - down2.refAmplitude(st) - top) * scale_y - 1),
								 index2coord(en), (int)((max_et_y - down2.refAmplitude(en) - top) * scale_y - 1));
		}
		else
		{
			for(int j=Math.max(0, et_ep[c_event].begin - (int)(start)); j<= Math.min (end, et_ep[c_event].end) - start; j++)// && j<y.length
			{
				g.setColor(warningThresholdColor);
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up1.refAmplitude(j+start) - top) * scale_y - 1),
									 (int)((j+1)*scale_x+1), (int)((max_et_y - up1.refAmplitude(j+start+1) - top) * scale_y - 1));

				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down1.refAmplitude(j+start) - top) * scale_y - 1),
									 (int)((j+1)*scale_x+1), (int)((max_et_y - down1.refAmplitude(j+start+1) - top) * scale_y - 1));

				g.setColor(alarmThresholdColor);
				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - up2.refAmplitude(j+start) - top) * scale_y - 1),
									 (int)((j+1)*scale_x+1), (int)((max_et_y - up2.refAmplitude(j+start+1) - top) * scale_y - 1));

				g.drawLine((int)(j*scale_x+1), (int)((max_et_y - down2.refAmplitude(j+start) - top) * scale_y - 1),
									 (int)((j+1)*scale_x+1), (int)((max_et_y - down2.refAmplitude(j+start+1) - top) * scale_y - 1));
			}
		}
	}

	private double getShift(ReflectogramEvent []etalon, double []data)
	{
		if(data == null || etalon == null || etalon.length<1)
			return 0.;

		double maxEtalon = -1000.;
		double maxData   = -1000.;

		for(int i=etalon[0].begin; i<=etalon[0].end && i<data.length; i++)
		{
			if(maxEtalon<etalon[0].refAmplitude(i))
				maxEtalon=etalon[0].refAmplitude(i);
			if(maxData<data[i])
				maxData = data[i];
		}

		return (maxData - maxEtalon);
	}
}