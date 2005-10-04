/*-
 * $Id: EnhancedReflectogramPanel.java,v 1.7 2005/10/04 09:22:53 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;

/**
 * ������������ �������������� � ���� ������ ������������� �����
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/10/04 09:22:53 $
 * @module
 */
public class EnhancedReflectogramPanel extends ReflectogramPanel {
	/**
	 * ��������� �������������� �����-�������.
	 */
	protected abstract class LevelLine {
		private boolean drawed = false;
		protected boolean moving = false;
		private Color levelColor;

		/**
		 * ������� ����� � ��������� "�� ������������"
		 * @param levelColor ���� ��������� �����
		 */
		public LevelLine(Color levelColor) {
			this.levelColor = levelColor;
		}

		/**
		 * @return ��������� �� ������ ����������� ���� �����.
		 * ����� ����� ������������ ������ ����
		 * {@link #isDrawed()} && {@link #isDefined()}
		 */
		public final boolean isDrawed() { return this.drawed; }
		/**
		 * ���������� ����, ���������� �� �����.
		 * ������ ������ ��� ���������� �����������.
		 */
		public final void setDrawed(boolean flag) { this.drawed = flag; }
		/**
		 * @return ��������� �� �� ������ ��� ����� � ��������� �����������.
		 */
		public final boolean isMoving() { return this.moving; }

		/**
		 * �������� ������
		 * @return ������ �������, ���������� �� ������ ��������� ���� �����.
		 */
		public abstract boolean isDefined();
		/**
		 * �������� ������. ���� ����� �� ����� ����������, ����
		 * {@link #isDefined()} ���������� false.
		 * @return ������ ������� ������� ����� � ���������� �������� (��).
		 */
		public abstract double getLevel();
		/**
		 * �������� ������.
		 * @return ����� �� ����� (������) �������.
		 */
		public abstract boolean isMovable();
		/**
		 * ����� �� ����� ���������, ���� {@link #isMovable()} ���������� false
		 * @param level ���������� ����� �������� ������ (���. ��. - ��).
		 */
		protected abstract void setLevel(double level);

		public final void draw(Graphics g) {
			if (!isDefined() || !isDrawed())
				return;
			int jw = getWidth();
			((Graphics2D) g).setStroke(SELECTION_STROKE);
			g.setColor(this.levelColor);
			int coordY = value2coord(getLevel());
			g.drawLine(0, coordY, jw, coordY);
			((Graphics2D) g).setStroke(DEFAULT_STROKE);
		}

		public final boolean startMoving(Point pos) {
			this.moving = false;
			if (isDefined() && isDrawed() && isMovable()) {
				if (Math.abs(pos.y - value2coord(Heap.getMinTraceLevel()))
						< MOUSE_COUPLING) {
					this.moving = true;
				}
			}
			return this.moving;
		}
		public final void continueMoving(Point pos) {
			if (this.moving) {
				setLevel(coord2value(pos.y));
			}
		}

		public final void endMoving(Point pos) {
			if (this.moving) {
				setLevel(coord2value(pos.y));
			}
			this.moving = false;
		}
	}

	public boolean draw_alarms = false;
	public LevelLine minTraceLevel =  new LevelLine(
			UIManager.getColor(AnalysisResourceKeys.COLOR_MIN_TRACE_LEVEL)) {
		@Override
		public boolean isMovable() {
			return true;
		}
		@Override
		public boolean isDefined() {
			return Heap.hasMinTraceLevel();
		}
		@Override
		public double getLevel() {
			return Heap.getMinTraceLevel();
		}
		@Override
		protected void setLevel(double level) {
			Heap.setMinTraceLevel(level);
		}
	};

	public boolean draw_noise_level = false;
	protected double noise_level = 28; // ???!
	protected ReflectogramMismatch[] alarms;

	public EnhancedReflectogramPanel(TraceEventsLayeredPanel panel,
			double[] y, double deltaX) {
		super(panel, y, deltaX);
	}

	public void updateAlarms (ReflectogramMismatch[] alarms1) {
		this.alarms = alarms1;
		if (alarms1 != null) {
			draw_alarms = true;
		} else {
			draw_alarms = false;
		}
	}

	@Override
	protected void paint_specific(Graphics g) {
		super.paint_specific(g);
		if (showAll) {
			if (draw_alarms)
				paint_alarms(g);
		}
//		if (draw_min_trace_level && isDraw_events()) {
//			paint_noise_level(g);
//			paint_min_trace_level(g);
//		} else if (draw_noise_level && isDraw_events()) {
//			paint_noise_level(g);
//		}
		if (minTraceLevel.isDrawed() && isDraw_events()) {
			paint_noise_level(g);
			minTraceLevel.draw(g);
		} else if (draw_noise_level && isDraw_events()) {
			paint_noise_level(g);
		}
	}

	protected void paint_noise_level(Graphics g) {
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE).darker());
		int h = (int)((noise_level - top) * scaleY - 1);
		g.drawLine(0, h, jw, h);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString(LangModelAnalyse.getString("Noise level"), jw - 87, h - 1);
	}

	protected void paint_alarms(Graphics g) {
		if (alarms == null)
			return;

		g.setColor(Color.red);
		for (int j = 0; j < alarms.length; j++) {
			if ((alarms[j].getCoord() <= end) && (alarms[j].getCoord() >= start)) {
				for (int i = Math.max(0, alarms[j].getCoord() - start); i < Math.min (end, alarms[j].getEndCoord()) - start; i++) {
					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY - 1),
					(int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY - 1));
				}
			}
		}
	}
}
