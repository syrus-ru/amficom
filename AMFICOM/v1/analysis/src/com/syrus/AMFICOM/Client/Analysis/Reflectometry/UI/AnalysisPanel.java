package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;

import com.syrus.AMFICOM.analysis.dadara.MathRef;

public class AnalysisPanel extends MapMarkersPanel
{
	public boolean show_markers = true;
	public boolean loss_analysis = true;
	public boolean reflection_analysis = false;

	protected Marker markerA;
	protected Marker markerB;
	protected AnaLine[] lines = new AnaLine[2];
	protected MarkersInfo mInfo = new MarkersInfo();
	protected Marker moving_marker;
	private boolean moved_here = true;

	private static float marker_w = 1.6f; // width of marker in pixels
	private static float ana_line_w = 1.6f; // width of analysis lines in pixels
	public static Stroke ANA_LINE_STROKE = new BasicStroke(ana_line_w);

	private int moving_point = 0;
	protected double start_y;

	private int _activeEvent = -1;
	private int activeEvent = -1;

	protected Color markerColor;

	public AnalysisPanel(AnalysisLayeredPanel panel, Dispatcher dispatcher, double y[], double delta_x)
	{
		super (panel, dispatcher, y, delta_x);

		start_y = MathRef.getLinearStartPoint(y);

		markerA = new Marker("A", (int)(y.length*.2));
		markerA.id = "A";
		lines[0] = new AnaLine();
		lines[1] = new AnaLine();
		lines[0].point[0] = markerA.pos - (int)(y.length*.05);
		lines[0].point[1] = markerA.pos-8-(int)ana_line_w;
		lines[1].point[0] = markerA.pos+8+(int)ana_line_w;
		lines[1].point[1] = markerA.pos + (int)(y.length*.05);
		lines[0].factor = MathRef.LSA(y, lines[0].point[0], lines[0].point[1]);
		lines[1].factor = MathRef.LSA(y, lines[1].point[0], lines[1].point[1]);

		markerB = new Marker ("B", (int)(y.length * .8));
		markerB.id = "B";

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
		MARKER_STROKE = new BasicStroke(marker_w);
		useXORMode = false;
	}

	public void updEvents(String id)
	{
		super.updEvents(id);
		if (activeEvent >= events.length)
			activeEvent = events.length - 1;
	}

	public void updMarkers()
	{
		if (show_markers)
		{
			updMarker(markerA);
			updMarker(markerB);
		}
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (coord2index(currpos.x) > y.length)
			return;

		// если производим вычисление отражения или вычисление потерь
		// если loss => проверяем точки в порядке 4,3,1,2
		// если ref => проверяем точки в порядке 3,1,2
		if (show_markers)
		{
			if(reflection_analysis || loss_analysis)
			{
				moving_point = 0;
				if (loss_analysis)
				{
					if(Math.abs(currpos.x-index2coord(lines[1].point[1])) < mouse_coupling &&
						 Math.abs(currpos.y-lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[1])) < mouse_coupling)
					{
						moving_point = 4;
						setCursor(new Cursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
					if(Math.abs(currpos.x-index2coord(lines[1].point[0])) < mouse_coupling &&
						 Math.abs(currpos.y-lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[0])) < mouse_coupling)
					{
						moving_point = 3;
						setCursor(new Cursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
				if (reflection_analysis)
				{
					if(Math.abs(currpos.x-index2coord(lines[1].point[0])) < mouse_coupling &&
						 Math.abs(currpos.y-(int)((max_y - y[lines[1].point[0]] - top)*scale_y)) < mouse_coupling)
					{
						moving_point = 3;
						setCursor(new Cursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
				for(int i=0; i<=1; i++)
				{
					if(Math.abs(currpos.x-index2coord(lines[0].point[i])) < mouse_coupling &&
						 Math.abs(currpos.y-lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[i])) < mouse_coupling)
					{
						moving_point = i + 1;
						setCursor(new Cursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
			}
			// если ткнули в маркер
			if (Math.abs(index2coord(markerA.pos)-currpos.x) < mouse_coupling)
			{
				moving_marker = markerA;
				setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				updMarker (markerA);
				setPaintMode(useXORMode);
				return;
			}
			if (Math.abs(index2coord(markerB.pos)-currpos.x) < mouse_coupling)
			{
				moving_marker = markerB;
				setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				updMarker (markerB);
				setPaintMode(useXORMode);
				return;
			}
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		// если двигаем курсор
		if(moving_marker != null)
		{
			upd_currpos(e);
			int pos = coord2index(currpos.x);

			Graphics g = getGraphics();
			if (paintMarkerXOR)
			{
				g.setXORMode(new Color(markerColor.getRGB() ^ SystemColor.window.getRGB()));
				paint_marker(g, moving_marker);
				if (moving_marker.equals(markerA))
				{
					if(loss_analysis)
						paint_loss_ana(g);
					if(reflection_analysis)
						paint_reflection_ana(g);
				}
			}
			moveMarker (moving_marker, pos);

			if (paintMarkerXOR)
			{
				paint_marker(g, moving_marker);
				if (moving_marker.equals(markerA))
				{
					if(loss_analysis)
						paint_loss_ana(g);
					if(reflection_analysis)
						paint_reflection_ana(g);
				}
			}
			else
				parent.repaint();

			updAnalysisMarkerInfo();
/*		if (markers_pair_moving)
			{
				if (active_marker == 0)
					move_marker (1, pos);
				if (active_marker == 1)
					move_marker (0, pos);
			}*/
			return;
		}

		//если производим анализ потерь или анализ отражения
		else if(loss_analysis || reflection_analysis)
		{
			if (moving_point != 0) // если изменям длину прямых
			{
				upd_currpos(e);

				Graphics g = getGraphics();
				if (paintMarkerXOR)
				{
					g.setXORMode(new Color(markerColor.getRGB() ^ SystemColor.window.getRGB()));
					if (loss_analysis)
						paint_loss_ana(g);
					else
						paint_reflection_ana(g);
				}
				move_ana_point (moving_point, coord2index(currpos.x));
				if (paintMarkerXOR)
				{
					g.setXORMode(new Color(markerColor.getRGB() ^ SystemColor.window.getRGB()));
					if (loss_analysis)
						paint_loss_ana(g);
					else
						paint_reflection_ana(g);
				}
				else
					parent.repaint();

				return;
			}
		}//else if

		super.this_mouseDragged(e);
		// иначе выделяем область квадратиком
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		// если рисовали маркеры - ничего не делаем
		if(moving_marker != null)
		{
			moving_marker = null;
			removePaintMode(useXORMode);

			activeEvent = marker_to_event();
			if (_activeEvent != activeEvent)
			{
				_activeEvent = activeEvent;
				moved_here = true;
				dispatcher.notify(new RefUpdateEvent(String.valueOf(activeEvent), RefUpdateEvent.EVENT_SELECTED_EVENT));
				moved_here = false;
				return;
			}
			return;
		}

		if (show_markers)
		{
			// если двигали кружочки - ничего не делаем
			if (moving_point != 0)
			{
				moving_point = 0;
				removePaintMode(useXORMode);
				return;
			}

			// если был сделан клик < 2 пикселов - двигаем туда маркер А для левой мыши и маркер Б для правой
			if (coord2index(currpos.x) < y.length && !moving_level)
			{
				if (((Math.abs(currpos.x - startpos.x)) < 2) && ((Math.abs(currpos.y - startpos.y)) < 2 ))
				{
					if (SwingUtilities.isRightMouseButton(e))
					{
						moveMarker (markerB, coord2index(currpos.x));
						updAnalysisMarkerInfo();
//					if (markers_pair_moving)
//							move_marker_B (db);
						activeEvent = marker_to_event();
						if (_activeEvent != activeEvent)
						{
							_activeEvent = activeEvent;
							moved_here = true;
							dispatcher.notify(new RefUpdateEvent(String.valueOf(activeEvent), RefUpdateEvent.EVENT_SELECTED_EVENT));
							moved_here = false;
							return;
						}
					}
					if (SwingUtilities.isLeftMouseButton(e))
					{
						int pos = coord2index(currpos.x);
						moveMarker (markerA, pos);
						updAnalysisMarkerInfo();
					}
					parent.repaint();
					return;
				}
			}
		}

		// если был выделен пр-к с одной из сторон < 5 пикселов - ничего не делаем
		if (((Math.abs(currpos.x - startpos.x)) < 5) || ((Math.abs(currpos.y - startpos.y)) < 5 ))
		{
		}

		super.this_mouseReleased(e);
	}

	void updMarker(Marker m)
	{
		if (m.equals(markerA)) // update marker A
		{
			if (loss_analysis)
			{
				mInfo.a_type = MarkersInfo.NONREFLECTIVE;
				mInfo.a_loss = ( (lines[0].factor[0]*m.pos + lines[0].factor[1]) - (lines[1].factor[0]*m.pos + lines[1].factor[1]) );
			}
			else if (reflection_analysis)
			{
				mInfo.a_type = MarkersInfo.REFLECTIVE;
				mInfo.a_reflectance = ( y[lines[1].point[0]] - (lines[0].factor[0]*m.pos + lines[0].factor[1]) );
			}
			if (loss_analysis || reflection_analysis)
			{
				mInfo.a_attfactor = -(1000d * lines[0].factor[0] / delta_x);
				mInfo.a_cumulative_loss = start_y - y[mInfo.a_pos];
			}
			else
				mInfo.a_type = MarkersInfo.NOANALYSIS;

			mInfo.a_pos = m.pos;
			mInfo.a_pos_m = m.pos * delta_x;
		}
		else if (m.equals(markerB)) // update marker B
		{
			mInfo.b_activeEvent = marker_to_event();
			mInfo.b_pos = m.pos;
			mInfo.b_pos_m = m.pos * delta_x;
		}
	}

	int marker_to_event()
	{
		if (events == null)
			return -1;
		for(int j=0; j<events.length; j++)
			if(markerB.pos > events[j].first_point && markerB.pos <= events[j].last_point)
				return j;
		return -1;
	}

	public int moveMarker (Marker m, int new_position)
	{
		if (m == null)
			return 0;

		int st = m.pos;
		m.move(new_position);
		m.checkBounds(0, y.length-1);
		int moved = m.pos - st;

		if (m.equals(markerA))
		{
			lines[0].move(moved);
			lines[0].checkBounds(0, m.pos - 1, 1);
			lines[0].factor = MathRef.LSA(y, lines[0].point[0], lines[0].point[1]);
			lines[1].move(moved);
			lines[1].checkBounds(m.pos + 1, y.length-1, 1);
			lines[1].factor = MathRef.LSA(y, lines[1].point[0], lines[1].point[1]);
		}

		updMarker(m);
		return moved;
	}

	void move_marker_to_ev (int event)
	{
		if (events == null)
			return;
		if (!moved_here)
		{
			_activeEvent = activeEvent;
			activeEvent = event;
			moveMarker(markerB, (events[event].first_point + events[event].last_point)/2);
		}
//		scrollToMarkerVisible(markerB);
		parent.repaint();
	}

	void move_ana_point(int moving_point, int new_pos)
	{
		int i = (moving_point - 1) / 2;
		int j = (moving_point - 1) - 2 * i;

		lines[i].move(j, new_pos);
		lines[0].checkBounds(0, markerA.pos - 1, 1);
		lines[1].checkBounds(markerA.pos + 1, y.length-1, 1);
		lines[i].factor = MathRef.LSA(y, lines[i].point[0], lines[i].point[1]);

		updMarker(markerA);
		updAnalysisMarkerInfo();
	}

	void updAnalysisMarkerInfo()
	{
		int l = Math.min(mInfo.a_pos, mInfo.b_pos);
		int r = Math.max(mInfo.a_pos, mInfo.b_pos);
		mInfo.a_b_distance = r - l;
		mInfo.a_b_distance_m = mInfo.a_b_distance * delta_x;
		mInfo.a_b_loss = y[l] - y[r];
		mInfo.a_b_attenuation = 1000d * (mInfo.a_b_loss) / mInfo.a_b_distance_m;
		double lsa[] = MathRef.LSA(y, l, r);
		mInfo.lsa_attenuation = -1000d * lsa[0] / delta_x;
		mInfo.a_b_orl = MathRef.ORL(y, l, r);
		moved_here = true;
		dispatcher.notify(new RefUpdateEvent(mInfo, RefUpdateEvent.MARKER_MOVED_EVENT));
		moved_here = false;
	}

	protected void updColorModel()
	{
		super.updColorModel();

		markerColor = ColorManager.getColor("analysisMarkerColor");
		markerA.setColor(markerColor);
		markerB.setColor(markerColor);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		if (show_markers)
		{
			paint_analysis_markers(g);
			if(loss_analysis)
				paint_loss_ana(g);
			if(reflection_analysis)
				paint_reflection_ana(g);
		}
	}

	protected void paint_analysis_markers (Graphics g)
	{
		if (paintMarkerXOR)
			g.setXORMode(new Color(markerColor.getRGB() ^ SystemColor.window.getRGB()));
		else
			g.setColor(markerColor);

		if ( (markerA.pos > start) && (markerA.pos < end))
			paint_marker(g, markerA);
		if ( (markerB.pos > start) && (markerB.pos < end))
			paint_marker(g, markerB);
	}

	protected void paint_loss_ana(Graphics g)
	{
		((Graphics2D) g).setStroke(ANA_LINE_STROKE);
		for (int i = 0; i < 2; i++)
		{
			if ((lines[i].point[1] > start) && (lines[i].point[0] < end))
			{
				int x1 = Math.max(start, lines[i].point[0]);
				int x2 = Math.min(lines[i].point[1], end);

				g.drawLine (index2coord(x1), lindraw(lines[i].factor[0], lines[i].factor[1], x1),
										index2coord(x2), lindraw(lines[i].factor[0], lines[i].factor[1], x2));

				g.drawOval(index2coord(lines[i].point[0])-4, lindraw(lines[i].factor[0], lines[i].factor[1], lines[i].point[0])-4, 8, 8);
				g.drawOval(index2coord(lines[i].point[1])-4, lindraw(lines[i].factor[0], lines[i].factor[1], lines[i].point[1])-4, 8, 8);
			}
		}
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_reflection_ana(Graphics g)
	{
		((Graphics2D) g).setStroke(ANA_LINE_STROKE);

		if ((lines[0].point[1] > start) && (lines[0].point[0] < end))
		{
			int x1 = Math.max(start, lines[0].point[0]);
			int x2 = Math.min(lines[0].point[1], end);

			g.drawLine(index2coord(x1), lindraw(lines[0].factor[0], lines[0].factor[1], x1),
								 index2coord(x2), lindraw(lines[0].factor[0], lines[0].factor[1], x2));

			g.drawOval(index2coord(lines[0].point[0])-4, lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[0]) - 4, 8, 8);
			g.drawOval(index2coord(lines[0].point[1])-4, lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[1]) - 4, 8, 8);
		}

		if ((lines[1].point[0] > start) && (lines[1].point[0] < end))
			g.drawOval(index2coord(lines[1].point[0])-4, (int)((max_y - y[lines[1].point[0]] - top)*scale_y)-5, 8, 8);

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	int lindraw(double a, double b, int x)
	{
		return (int)((max_y - a*x-b - top)*scale_y - 1);
	}
}

class AnaLine
{
	int[] point = new int[2]; // координаты концов прямой
	double[] factor = new double[2]; // коэффициенты прямой y=ax+b
	private int last_moved = 0;

	void move (int dx)
	{
		point[0] += dx;
		point[1] += dx;
	}

	void move (int n, int new_pos)
	{
		point[n] = new_pos;
		last_moved = n;
	}

	void checkBounds(int min, int max, int min_dist)
	{
		if (point[0] < min)
			move (0, min);
		if (point[1] < min)
			move (1, min + 1);
		if (point[0] > max)
			move (0, max - 1);
		if (point[1] > max)
			move (1, max);

		if (point[1] - point[0] < min_dist)
		{
			if  (last_moved == 0)
			{
				point[1] = Math.min(point[0] + min_dist, max);
				point[0] = Math.max(point[1] - min_dist, min);
			}
			else
			{
				point[0] = Math.max(point[1] - min_dist, min);
				point[1] = Math.min(point[0] + min_dist, max);
			}
		}

		if (point[0] < min)
			move (0, min);
		if (point[1] < min)
			move (1, min + 1);
		if (point[0] > max)
			move (0, max - 1);
		if (point[1] > max)
			move (1, max);
	}
}
class MarkersInfo
{
	static final int NOANALYSIS = 0;
	static final int REFLECTIVE = 1;
	static final int NONREFLECTIVE = 2;

	int a_type;
	int a_pos;
	double a_pos_m;
	double a_loss;
	double a_reflectance;
	double a_attfactor;
	double a_cumulative_loss;

	int b_pos;
	double b_pos_m;
	int b_activeEvent;

	int a_b_distance;
	double a_b_distance_m;
	double a_b_loss;
	double a_b_attenuation = 11.;
	double lsa_attenuation = 22.;
	double a_b_orl = 33.;
}