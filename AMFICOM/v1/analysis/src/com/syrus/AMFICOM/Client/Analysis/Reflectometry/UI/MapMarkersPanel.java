package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.General.Event.*;

public class MapMarkersPanel extends ThresholdsPanel
{
	protected String monitored_element_id = null;
	protected String map_path_id = null;

	protected boolean show_markers = true;
	protected boolean creating_marker = false;

	protected ArrayList markers = new ArrayList();
	protected Marker active_marker;
	protected boolean move_marker = false;
	private static float marker_w = 1f; // width of marker in pixels
	public static Stroke MARKER_STROKE = new BasicStroke(marker_w);

	public boolean useXORMode = true;
	protected boolean paintMarkerXOR = false;

	public MapMarkersPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double delta_x)
	{
		super (panel, dispatcher, y, delta_x);

		edit_thresholds = false;

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

	public void setMonitoredElementId (String monitored_element_id)
	{
		this.monitored_element_id = monitored_element_id;
	}

	public void setMapPathId (String map_path_id)
	{
		this.map_path_id = map_path_id;
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (coord2index(currpos.x) > y.length)
			return;

		if (creating_marker && parent instanceof MapMarkersLayeredPanel)
		{
			Marker m = createMarker ("", "", coord2index(currpos.x) * delta_x);
			((MapMarkersLayeredPanel)parent).setButtons();

			MapNavigateEvent mne = new MapNavigateEvent (this,
					MapNavigateEvent.DATA_MARKER_CREATED_EVENT,
					m.id, m.pos * delta_x, map_path_id, monitored_element_id);
			if(true)
			{
//				mne.spd = new SchemePathDecompositor();
//				mne.spd = new OpticalLength();
//				double d = WorkWithReflectoArray.getDistanceTillLastSplash(y, delta_x, 1);
//				mne.spd.setMeasurement (new LengthParameters (ep, delta_x, "", d));
			}
			dispatcher.notify(mne);
			dispatcher.notify(new MapNavigateEvent (this,
											MapNavigateEvent.DATA_MARKER_SELECTED_EVENT,
													m.id, m.pos * delta_x, map_path_id, monitored_element_id));

			return;
		}

		if (show_markers)
		{
			Iterator it = markers.iterator();
			while(it.hasNext())
			{
				Marker m = (Marker)it.next();
				if(Math.abs(index2coord(m.pos)-currpos.x) < mouse_coupling)
				{
					if(m.canMove())
					{
						move_marker = true;
						setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
					}
					active_marker = m;
					dispatcher.notify (new MapNavigateEvent (this, MapNavigateEvent.DATA_MARKER_SELECTED_EVENT,
														 m.id, m.pos * delta_x, map_path_id, monitored_element_id));
				}
			}
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (creating_marker)
			return;

		// если двигаем курсор
		if(move_marker)
		{
			upd_currpos(e);

			moveMarker (active_marker, coord2index(currpos.x));
			move_notify();
			parent.repaint();
			return;
		}

		super.this_mouseDragged(e);
	}

	void move_notify()
	{
		dispatcher.notify (new MapNavigateEvent (this, MapNavigateEvent.DATA_MARKER_MOVED_EVENT,
												 active_marker.id, active_marker.pos * delta_x, map_path_id, monitored_element_id));
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		// если создавали маркеры - ничего не делаем
		if (creating_marker)
		{
			creating_marker = false;
			parent.repaint();
			return;
		}

		// если рисовали маркеры - ничего не делаем
		if(move_marker)
		{
			move_marker = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}

		super.this_mouseReleased(e);
	}

	protected void updColorModel()
	{
		super.updColorModel();
	}
/*
	public void updateAlarms (ReflectogramAlarm[] alarms)
	{
		if (this.alarms != null)
		{
			for (int i = 0; i < this.alarms.length; i++)
			{
				dispatcher.notify (new MapNavigateEvent (this,
				MapNavigateEvent.DATA_ALARMMARKER_DELETED_EVENT,
				"amarker" + String.valueOf(this.alarms[i].alarmPointCoord),
				this.alarms[i].alarmPointCoord * delta_x,
				map_path_id,
				monitored_element_id));
			}
		}

		super.updateAlarms(alarms);

		if (alarms != null)
		{
			for (int i = 0; i < alarms.length; i++)
			{
				dispatcher.notify ( new MapNavigateEvent (this,
				MapNavigateEvent.DATA_ALARMMARKER_CREATED_EVENT,
				"amarker" + String.valueOf(alarms[i].alarmPointCoord),
				alarms[i].alarmPointCoord * delta_x,
				map_path_id,
				monitored_element_id));;
			}
		}
	}
*/
	public AlarmMarker get_alarm_marker()
	{
		Iterator it = markers.iterator();
		while(it.hasNext())
		{
			try
			{
				AlarmMarker m = (AlarmMarker )it.next();
				return m;
			}
			catch(Exception ex)
			{
			}
		}
		return null;
	}

	public void paint (Graphics g)
	{
		super.paint(g);

		paint_markers(g);
	}

	void paint_markers (Graphics g)
	{
		Iterator it = markers.iterator();
		while(it.hasNext())
		{
			Marker m = (Marker)it.next();
			if ( (m.pos > start) && (m.pos < end))
				paint_marker(g, m);
		}
	}

	protected void paint_marker (Graphics g, Marker m)
	{
		int jh =  getHeight();
		g.setColor(m.getColor());
		((Graphics2D) g).setStroke(MARKER_STROKE);
		g.drawLine(index2coord(m.pos), 0, index2coord(m.pos), jh);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString(m.name, index2coord(m.pos)+2+(int)marker_w,10);
	}

	protected void setPaintMode(boolean useXOR)
	{
		if (useXOR)
		{
			paintMarkerXOR = true;
			parent.repaint();
		}
	}

	protected void removePaintMode(boolean useXOR)
	{
		paintMarkerXOR = false;
		parent.repaint();
	}

	public void removeAllMarkers()
	{
		if (markers != null && !markers.isEmpty())
		{
			for (int i = 0; i < markers.size(); i++)
			{
				Marker m = (Marker)markers.get(i);
				MapNavigateEvent mne;
				if(m instanceof AlarmMarker)
				{
					mne = new MapNavigateEvent(
						this,
						MapNavigateEvent.DATA_ALARMMARKER_DELETED_EVENT,
						m.id,
						alarms[i].alarmPointCoord * delta_x,
						map_path_id,
						monitored_element_id);
				}
				else
				{
					mne = new MapNavigateEvent(
						this,
						MapNavigateEvent.DATA_MARKER_DELETED_EVENT,
						m.id,
						m.pos * delta_x,
						map_path_id,
						monitored_element_id);
				}
				mne.descriptor = "refevent";
				dispatcher.notify(mne);
				deleteMarker(m);
			}
		}
	}

	public Marker createMarker (String name, String id, double position)
	{
		Marker m = new Marker(name, (int)(position / delta_x));
		if (!id.equals(""))
			m.id = id;
		markers.add(m);
		active_marker = m;
		return m;
	}

	public Marker createAlarmMarker (String name, String id, double position)
	{
		Marker m = new AlarmMarker(name, (int)(position / delta_x));
		if (!id.equals(""))
			m.id = id;
		markers.add(m);
		active_marker = m;
		return m;
	}

	public Marker activateMarker (String id)
	{
		Iterator it = markers.iterator();
		while(it.hasNext())
		{
			Marker m = (Marker)it.next();
			if (m.id.equals(id))
			{
				active_marker = m;
				return m;
			}
		}
		return null;
	}

	public void moveMarker (String id, double new_position)
	{
		Iterator it = markers.iterator();
		while(it.hasNext())
		{
			Marker m = (Marker)it.next();
			if (m.id.equals(id))
			{
				moveMarker (m, (int)(new_position / delta_x));
				return;
			}
		}
	}

	public int moveMarker (Marker m, int new_position)
	{
		int st = m.pos;
		m.move(new_position);
		m.checkBounds(0, y.length-1);
		int moved = m.pos - st;

		return moved;
	}

	public void scrollToMarkerVisible(Marker m)
	{
		if (m.pos < end && m.pos > start)
			return;

		int delta = (start - m.pos) + (end - start) / 2;
		if (end - delta > y.length)
			delta = end - y.length;
		if (start - delta < 0)
			delta = start;

		int st = start - delta;
		int en = end - delta;
		((ScalableLayeredPanel)parent).updScale2fit(st, en);
	}

	public Marker deleteMarker (String id)
	{
		Iterator it = markers.iterator();
		while(it.hasNext())
		{
			Marker m = (Marker)it.next();
			if (m.id.equals(id))
			{
				deleteMarker (m);
				return m;
			}
		}
		return null;
	}

	public Marker deleteActiveMarker()
	{
		Marker m = deleteMarker(active_marker);
		MapNavigateEvent mne;
		if(m instanceof AlarmMarker)
		{
			mne = new MapNavigateEvent(
				this,
				MapNavigateEvent.DATA_ALARMMARKER_DELETED_EVENT,
				m.id,
				m.pos * delta_x,
				map_path_id,
				monitored_element_id);
		}
		else
		{
			mne = new MapNavigateEvent (
				this,
				MapNavigateEvent.DATA_MARKER_DELETED_EVENT,
				m.id,
				m.pos * delta_x,
				map_path_id,
				monitored_element_id);
		}
		dispatcher.notify(mne);
		return m;
	}

	public Marker deleteMarker (Marker m)
	{
		markers.remove(m);

		if (m.equals(active_marker))
		{

			if (!markers.isEmpty())
				active_marker = (Marker)markers.iterator().next();
			else
				active_marker = null;
		}
		return m;
	}
}