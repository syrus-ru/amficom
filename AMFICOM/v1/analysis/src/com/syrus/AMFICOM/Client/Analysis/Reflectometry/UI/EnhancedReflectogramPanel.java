/*-
 * $Id: EnhancedReflectogramPanel.java,v 1.9 2005/10/06 15:53:58 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
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
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;

/**
 * Отрисовывает рефлектограмму и кучу всяких сопутствующих вещей
 * @author $Author: saa $
 * @version $Revision: 1.9 $, $Date: 2005/10/06 15:53:58 $
 * @module
 */
public class EnhancedReflectogramPanel extends ReflectogramPanel {
	/**
	 * Описывает горизонтальную линию-уровень.
	 * Обеспечивает необходимую рассылку сообщений при выполнении setValue().
	 * Не обеспечивает подписку на изменение значения (клиент должен подписаться сам).
	 */
	protected abstract class LevelLine {
		private boolean drawed = false;
		protected boolean moving = false;
		private Color levelColor;

		/**
		 * Создает линию в состоянии "не отображается"
		 * @param levelColor цвет отрисовки линии
		 */
		public LevelLine(Color levelColor) {
			this.levelColor = levelColor;
		}

		/**
		 * @return разрешено ли сейчас отображение этой линии.
		 * Линия будет отображаться только если
		 * {@link #isDrawed()} && {@link #isDefined()}
		 */
		public final boolean isDrawed() { return this.drawed; }
		/**
		 * Установить флаг, отображать ли линию.
		 * Клиент должен сам обеспечить перерисовку.
		 */
		public final void setDrawed(boolean flag) { this.drawed = flag; }
		/**
		 * @return Находится ли ли сейчас эта линия в состоянии перемещения.
		 */
		public final boolean isMoving() { return this.moving; }

		/**
		 * Описание модели
		 * @return Должен вернуть, определено ли сейчас состояние этой линии.
		 */
		public abstract boolean isDefined();
		/**
		 * Описание модели. Этот метод не будет вызываться, если
		 * {@link #isDefined()} возвращает false.
		 * @return Должен вернуть уровень линии в физических единицах (дБ).
		 */
		public abstract double getLevel();
		/**
		 * Описание модели.
		 * @return Можно ли линию (вообще) двигать.
		 */
		public abstract boolean isMovable();
		/**
		 * Метод не будет вызываься, если {@link #isMovable()} возвращает false
		 * @param level Установить новое значение уровня (физ. ед. - дБ).
		 */
		protected abstract void setLevel(double level);

		/**
		 * Отрисовывается только если {@link #isDefined} && {@link #isDrawed}
		 * @param g
		 */
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
				if (Math.abs(pos.y - value2coord(getLevel()))
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

	public LevelLine eotDetectionLevel =  new LevelLine(
			UIManager.getColor(AnalysisResourceKeys.COLOR_END)) {
		@Override
		public boolean isMovable() {
			return true;
		}
		@Override
		public boolean isDefined() {
			return true;
		}
		@Override
		public double getLevel() {
			// ограничиваем уровень областью самой р/г
			double level = Heap.getMinuitAnalysisParams().getLevelEot();
			return level >= minY ? level : minY;
		}
		@Override
		protected void setLevel(double level) {
			try {
				Heap.getMinuitAnalysisParams().setLevelEot(level, true);
				Heap.notifyAnalysisParametersUpdated();
			} catch (InvalidAnalysisParametersException e) {
				// ignore
			}
		}
	};

	public boolean draw_noise_level = false;
	protected double noise_level = 28; // ???!
	protected ReflectogramMismatchImpl[] alarms;

	public EnhancedReflectogramPanel(TraceEventsLayeredPanel panel,
			double[] y, double deltaX) {
		super(panel, y, deltaX);
	}

	public void updateAlarms (ReflectogramMismatchImpl[] alarms1) {
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
		if (isDraw_events()) {
			minTraceLevel.draw(g);
			eotDetectionLevel.draw(g);
			if (draw_noise_level || minTraceLevel.isDrawed()) {
				paint_noise_level(g);
			}
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
