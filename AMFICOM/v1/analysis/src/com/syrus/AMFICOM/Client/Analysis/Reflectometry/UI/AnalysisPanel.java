package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;

public class AnalysisPanel extends MapMarkersPanel
{
	public boolean show_markers = true;

	protected boolean loss_analysis = true;
	protected boolean reflection_analysis = false;

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
	protected Color markerColorXOR;

	public AnalysisPanel(AnalysisLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super (panel, dispatcher, y, deltaX);

		this.start_y = MathRef.getLinearStartPoint(y);

		this.markerA = new Marker("A", (int)(y.length*.2));
//		markerA.id = "A";
		lines[0] = new AnaLine();
		lines[1] = new AnaLine();
		lines[0].point[0] = markerA.pos - (int)(y.length*.05);
		lines[0].point[1] = markerA.pos-8-(int)ana_line_w;
		lines[1].point[0] = markerA.pos+8+(int)ana_line_w;
		lines[1].point[1] = markerA.pos + (int)(y.length*.05);
		lines[0].factor = MathRef.calcLSA(y, lines[0].point[0], lines[0].point[1]);
		lines[1].factor = MathRef.calcLSA(y, lines[1].point[0], lines[1].point[1]);

		this.markerB = new Marker ("B", (int)(y.length * .8));
//		markerB.id = "B";
		
		this.markerColor = UIManager.getColor(AnalysisResourceKeys.COLOR_MARKER);
		this.markerA.setColor(markerColor);
		this.markerB.setColor(markerColor);
		
		this.markerColorXOR = new Color(this.markerColor.getRGB() ^ UIManager.getColor(ResourceKeys.COLOR_GRAPHICS_BACKGROUND).getRGB());

		MARKER_STROKE = new BasicStroke(marker_w);
		this.useXORMode = false;
	}

	public void updEvents(String id)
	{
		super.updEvents(id);
		if (activeEvent >= sevents.length)
			activeEvent = sevents.length - 1;
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

		// ���� ���������� ���������� ��������� ��� ���������� ������
		// ���� loss => ��������� ����� � ������� 4,3,1,2
		// ���� ref => ��������� ����� � ������� 3,1,2
		if (show_markers)
		{
			if(reflection_analysis || loss_analysis)
			{
				moving_point = 0;
				if (loss_analysis)
				{
					if(Math.abs(currpos.x-index2coord(lines[1].point[1])) < MOUSE_COUPLING &&
						 Math.abs(currpos.y-lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[1])) < MOUSE_COUPLING)
					{
						moving_point = 4;
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
					if(Math.abs(currpos.x-index2coord(lines[1].point[0])) < MOUSE_COUPLING &&
						 Math.abs(currpos.y-lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[0])) < MOUSE_COUPLING)
					{
						moving_point = 3;
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
				if (reflection_analysis)
				{
					if(Math.abs(currpos.x-index2coord(lines[1].point[0])) < MOUSE_COUPLING &&
						 Math.abs(currpos.y-(int)((maxY - y[lines[1].point[0]] - top)*scaleY)) < MOUSE_COUPLING)
					{
						moving_point = 3;
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
				for(int i=0; i<=1; i++)
				{
					if(Math.abs(currpos.x-index2coord(lines[0].point[i])) < MOUSE_COUPLING &&
						 Math.abs(currpos.y-lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[i])) < MOUSE_COUPLING)
					{
						moving_point = i + 1;
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						setPaintMode(useXORMode);
						return;
					}
				}
			}
			// ���� ������ � ������
			if (Math.abs(index2coord(markerA.pos)-currpos.x) < MOUSE_COUPLING)
			{
				moving_marker = markerA;
				setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				updMarker (markerA);
				setPaintMode(useXORMode);
				return;
			}
			if (Math.abs(index2coord(markerB.pos)-currpos.x) < MOUSE_COUPLING)
			{
				moving_marker = markerB;
				setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				updMarker (markerB);
				setPaintMode(useXORMode);
				return;
			}
		}
		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		// ���� ������� ������
		if(moving_marker != null)
		{
			upd_currpos(e);
			int pos = coord2index(currpos.x);

			Graphics g = getGraphics();
			if (paintMarkerXOR)
			{
				g.setXORMode(this.markerColorXOR);
				paint_marker(g, moving_marker);
				if (moving_marker.equals(markerA))
				{
					if(loss_analysis)
						paint_loss_ana(g);
					if(reflection_analysis)
						paint_reflection_ana(g);
				}
			}
			int prev_pos = moving_marker.pos;
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
			{
				//repaint old marker place
				int x = index2coord(prev_pos - 1) - 1;
				int x2 = index2coord(prev_pos + 1) + 1;
				parent.repaint(x, 0, x2 - x, parent.getHeight());
				//repaint marker label
				int width = g.getFontMetrics().stringWidth(moving_marker.name) + 5;
				int height = g.getFontMetrics().getHeight() + 2;
				parent.repaint(x, 10, x2 - x + width, height);

				//repaint new marker place
				int n_x = index2coord(moving_marker.pos - 1) - 1;
				int n_x2 = index2coord(moving_marker.pos + 1) + 1;
				parent.repaint(x, 0, n_x2 - n_x, parent.getHeight());
				parent.repaint(x, 10, n_x2 - n_x + width, height);

				if (moving_marker.equals(markerA))
				{
					if (loss_analysis)
					{
						int delta_left = currpos.x - index2coord(lines[0].point[0]) + 5;
						x = Math.min(x, n_x) - delta_left;
						int delta_right = index2coord(lines[1].point[1]) - currpos.x + 5;
						x2 = Math.max(x2, n_x2) + delta_right;

						int y1 = lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[0]);
						int y2 = lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[1]);
						int y3 = lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[0]);
						int y4 = lindraw(lines[1].factor[0], lines[1].factor[1], lines[1].point[1]);

						parent.repaint(x, Math.min(Math.min(y1, y2), Math.min(y3, y4)),
													 x2 - x, Math.max(Math.max(y1, y2), Math.max(y3, y4)));
					}
					else if (reflection_analysis)
					{
						int delta_left = currpos.x - index2coord(lines[0].point[0]) + 5;
						x = Math.min(x, n_x) - delta_left;
						int delta_right = index2coord(lines[1].point[1]) - currpos.x + 5;
						x2 = Math.max(x2, n_x2) + delta_right;

						int y1 = lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[0]);
						int y2 = lindraw(lines[0].factor[0], lines[0].factor[1], lines[0].point[1]);
						int y3 = (int)((maxY - y[lines[1].point[0]] - top)*scaleY);

						parent.repaint(x, Math.min(Math.min(y1, y2), y3),
													 x2 - x, Math.max(Math.max(y1, y2), y3));
					}
				}
			}

            // We must update numerical info in windows, so we send messages
            // But, we get working very slow if Histogram is in 'tied to markers' mode.
            // @todo: Histogram should update only on mouse release, not on every drag (?)
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

		//���� ���������� ������ ������ ��� ������ ���������
		else if(loss_analysis || reflection_analysis)
		{
			if (moving_point != 0) // ���� ������� ����� ������
			{
				upd_currpos(e);

				Graphics g = getGraphics();
				if (paintMarkerXOR)
				{
					g.setXORMode(this.markerColorXOR);
					if (loss_analysis)
						paint_loss_ana(g);
					else
						paint_reflection_ana(g);
				}
				move_ana_point (moving_point, coord2index(currpos.x));
				if (paintMarkerXOR)
				{
					g.setXORMode(this.markerColorXOR);
					if (loss_analysis)
						paint_loss_ana(g);
					else
						paint_reflection_ana(g);
				}
				else
				{
					//parent.repaint();
					if (loss_analysis)
					{
						int x = Math.min(index2coord(lines[0].point[0]), tmppos.x) - 5;
						int x2 = Math.max(index2coord(lines[1].point[1]), tmppos.x) + 8;
						parent.repaint(x, 0, x2 - x, parent.getHeight());
					}
					if (reflection_analysis)
					{
						int x = Math.min(index2coord(lines[0].point[0]), tmppos.x) - 5;
						int x2 = Math.max(index2coord(lines[1].point[0]), tmppos.x) + 8;
						parent.repaint(x, 0, x2 - x, parent.getHeight());
					}
					return;
				}
			}
		}//else if

		super.this_mouseDragged(e);
		// ����� �������� ������� �����������
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// ���� �������� ������� - ������ �� ������
		if(moving_marker != null)
		{
			moving_marker = null;
			removePaintMode(useXORMode);

			activeEvent = marker_to_event();
			if (_activeEvent != activeEvent)
			{
				_activeEvent = activeEvent;
				moved_here = true;
				Heap.setCurrentEvent(activeEvent);
				moved_here = false;
				return;
			}
			return;
		}

		if (show_markers)
		{
			// ���� ������� �������� - ������ �� ������
			if (moving_point != 0)
			{
				moving_point = 0;
				removePaintMode(useXORMode);
				return;
			}

			// ���� ��� ������ ���� < 2 �������� - ������� ���� ������ � ��� ����� ���� � ������ � ��� ������
			if (coord2index(currpos.x) < y.length && !moving_level)
			{
				if (((Math.abs(currpos.x - startpos.x)) < 2) && ((Math.abs(currpos.y - startpos.y)) < 2 ))
				{
					if (SwingUtilities.isRightMouseButton(e))
					{
						moveMarker (markerB, coord2index(currpos.x));
						updAnalysisMarkerInfo();
						activeEvent = marker_to_event();
						if (_activeEvent != activeEvent)
						{
							_activeEvent = activeEvent;
							moved_here = true;
							Heap.setCurrentEvent(activeEvent);
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

		// ���� ��� ������� ��-� � ����� �� ������ < 5 �������� - ������ �� ������
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
				mInfo.a_attfactor = -(1000d * lines[0].factor[0] / deltaX);
				mInfo.a_cumulative_loss = start_y - y[mInfo.a_pos];
			}
			else
				mInfo.a_type = MarkersInfo.NOANALYSIS;

			mInfo.a_pos = m.pos;
			mInfo.a_pos_m = m.pos * deltaX;
		}
		else if (m.equals(markerB)) // update marker B
		{
			mInfo.b_activeEvent = marker_to_event();
			mInfo.b_pos = m.pos;
			mInfo.b_pos_m = m.pos * deltaX;
		}
	}

	int marker_to_event()
	{
		if (sevents == null)
			return -1;
		for(int j=0; j<sevents.length; j++)
			if(markerB.pos > sevents[j].getBegin() && markerB.pos <= sevents[j].getEnd())
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
			lines[0].factor = MathRef.calcLSA(y, lines[0].point[0], lines[0].point[1]);
			lines[1].move(moved);
			lines[1].checkBounds(m.pos + 1, y.length-1, 1);
			lines[1].factor = MathRef.calcLSA(y, lines[1].point[0], lines[1].point[1]);
		}

		updMarker(m);
		return moved;
	}

	void move_marker_to_ev (int event)
	{
		if (sevents == null)
			return;
		if (event < 0 || event >= sevents.length)
				return;
		if (!moved_here)
		{
			_activeEvent = activeEvent;
			activeEvent = event;
			if (markerB.pos < sevents[event].getBegin() || markerB.pos >= sevents[event].getEnd())
				moveMarker(markerB, (sevents[event].getBegin()));
		}
		parent.repaint();
	}

	void move_ana_point(int movingPoint1, int newPos)
	{
		int i = (movingPoint1 - 1) / 2;
		int j = (movingPoint1 - 1) - 2 * i;

		lines[i].move(j, newPos);
		lines[0].checkBounds(0, markerA.pos - 1, 1);
		lines[1].checkBounds(markerA.pos + 1, y.length-1, 1);
		lines[i].factor = MathRef.calcLSA(y, lines[i].point[0], lines[i].point[1]);

		updMarker(markerA);
		updAnalysisMarkerInfo();
	}

	void updAnalysisMarkerInfo()
	{
		int l = Math.min(mInfo.a_pos, mInfo.b_pos);
		int r = Math.max(mInfo.a_pos, mInfo.b_pos);
		mInfo.a_b_distance = r - l;
		mInfo.a_b_distance_m = mInfo.a_b_distance * deltaX;
		mInfo.a_b_loss = y[l] - y[r];
		mInfo.a_b_attenuation = 1000d * (mInfo.a_b_loss) / mInfo.a_b_distance_m;
		double lsa[] = MathRef.calcLSA(y, l, r);
		mInfo.lsa_attenuation = -1000d * lsa[0] / deltaX;
		mInfo.a_b_orl = MathRef.calcORL(y[l], y[r]);
		moved_here = true;
		dispatcher.notify(new RefUpdateEvent(mInfo, RefUpdateEvent.MARKER_MOVED_EVENT));
		moved_here = false;
	}

	protected void updColorModel()
	{
		super.updColorModel();
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
			g.setXORMode(this.markerColorXOR);
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
			g.drawOval(index2coord(lines[1].point[0])-4, (int)((maxY - y[lines[1].point[0]] - top)*scaleY)-5, 8, 8);

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	int lindraw(double a, double b, int x)
	{
		return (int)((maxY - a*x-b - top)*scaleY - 1);
	}
}

class AnaLine
{
	int[] point = new int[2]; // ���������� ������ ������
	double[] factor = new double[2]; // ������������ ������ y=ax+b
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
