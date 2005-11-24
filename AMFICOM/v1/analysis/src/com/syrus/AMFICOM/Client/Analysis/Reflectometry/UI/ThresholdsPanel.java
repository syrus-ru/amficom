package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager.Operation;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceRange;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceRangeImplMTRSubrange;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Thresh;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThresholdHandle;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * Отображает пороги
 */
public class ThresholdsPanel extends MapMarkersPanel
{
	protected boolean paint_thresholds = true;
	// возможность редактирования порогов определяется из прав на момент создания окна
	private boolean edit_thresholds = true &&
		PermissionManager.isPermitted(Operation.EDIT_ETALON);

	private ModelTraceManager.ThresholdHandle c_TH = null;

	private static class FPSCounter { // FIXME: debug only: FSPCounter
		long count = 0;
		long time = 0;
		public FPSCounter() {
			this.time = System.currentTimeMillis();
		}
		void inc() {
			this.count++;
			long ct = System.currentTimeMillis();
			final long ONE_SECOND = 1000;
			final long DT = 1000;
			if (ct < this.time + DT)
				return;
			double fps = count * 1.0 * ONE_SECOND / (ct - this.time);
			System.out.println("FPSCounter: " + fps + " fps");
			this.time = ct;
			this.count = 0;
		}
	}

	private FPSCounter fps = new FPSCounter();

	public ThresholdsPanel(TraceEventsLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super (panel, dispatcher, y, deltaX);		
	}

	// we rely upon being up-to-date informed on MTMEtalon modification
	// Otherwise, our comunication with Heap.etalonEvent may not be accurate
	public void updateEtalon()
	{
		if (Heap.getMTMEtalon() == null) {
			c_TH = null;
		}
	}

	// XXX: transient code (slow refactoring);
	// to be used in ThresholdsLayeredPanel.
	// Performance Note: creates an int[2] object
	// Design Note: uses TraceEventsPanel.events
	public int[] getStartAndEndOfCurrentEvent()
	{
		int num = Heap.getCurrentEvent2();
		if (num < 0)
			return new int[] {0, sevents[sevents.length-1].getEnd()};
		if (num >= sevents.length)
			num = sevents.length - 1;
		int start1 = num > 0 ? sevents[num].getBegin() : 2;
		int end1 = sevents[num].getEnd();
		return new int[] {start1, end1};
	}

	public void updateCurrentEvent()
	{
		// Design Note: uses TraceEventsPanel.events
		if (sevents == null)
			return;

		int num = Heap.getCurrentEvent2();

		if (num >= 0) // XXX
		{
			start = sevents[num].getBegin();
			end = sevents[num].getEnd();
		}
	}
	
	@Override
	protected void this_mousePressed(MouseEvent mev)
	{
		ModelTraceManager etalon = Heap.getMTMEtalon();
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
		this.c_TH = etalon.getThresholdHandle(
			coord2indexF(this.currpos.x), // we need float value, without rounding
			coord2value(this.currpos.y),
			MOUSE_COUPLING / this.scaleX,
			MOUSE_COUPLING / this.scaleY,
			0.5,
			isRbutton ? 1 : 0,
			allThresholds ? -1 : Heap.getCurrentEtalonEvent2(),
					true);

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
			} catch (AWTException ex)
			{
				System.out.println("ThresholdsPanel: Warning: MouseMove failed");
				ex.printStackTrace();
			}

			return;
		}

		super.this_mousePressed(mev);
	}

	@Override
	protected void this_mouseDragged(MouseEvent e)
	{
		if (!edit_thresholds || Heap.getMTMEtalon() == null)
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

			dispatcher.firePropertyChange(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		} else
			// иначе выделяем область квадратиком
			super.this_mouseDragged(e);
	}

	@Override
	protected void this_mouseReleased(MouseEvent e)
	{
		if (!edit_thresholds || Heap.getMTMEtalon() == null || c_TH == null)
		{
			super.this_mouseReleased(e);
		} else
		{
			if (c_TH != null)
			{
				c_TH.release();
				parent.repaint();
				dispatcher.firePropertyChange(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				c_TH = null;
			}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	protected void this_mouseClicked(MouseEvent e)
	{
		// для окна порогов переходим к другому событию,
		// для окна анализа - не переходим
		if (edit_thresholds) {
			// если кликнули, но не на текущее событие, переходим к новому событию
			int pos = coord2index(e.getPoint().x);
			ModelTraceManager etalon = Heap.getMTMEtalon();
			int evId = etalon != null ? etalon.getMTAE().getEventByCoord(pos) : -1;
			if (evId != -1 && evId != Heap.getCurrentEtalonEvent2())
			{
				Heap.setCurrentEtalonEvent(evId);
				return;
			}
		} else {
			super.this_mouseClicked(e);
		}
	}

	@Override
	public void paint (Graphics g)
	{
		paint_scales(g);

		if (showAll) {
			if (isDraw_events()) {
				paint_events(g);
			} else {
				paint_trace(g);
			}
			if (isDraw_modeled()) {
				paint_modeled_trace(g);
			}
			if (draw_alarms) {
				paint_alarms(g);
			}
			// XXX добавлено, чтобы отрисовывались маркеры в наблюдении
			// последствия в других модулях пока не известны, но не должны быть трагическими
			paint_markers(g);
		}

		if (isDraw_events()) {
			minTraceLevel.draw(g);
			if (draw_noise_level || minTraceLevel.isDrawed()) {
				paint_noise_level(g);
			}
		}

		paint_scale_digits(g);

		if (paint_thresholds) {
			if(isToPaintAllThresholds())
				paintAllThresholds(g, null);
			else
				paintOneThreshold(g, null);
		}

		if (true) { // @todo: add a boolean flag to control this painting
			paintTraceMismatch(g);
		}
	}

	/**
	 * Extends GraphRange to cover all thresholds curves.
	 * (see {@link GraphRange})
	 * @param r GraphRange to update
	 */
	public void updateGraphRangeByThresholds(GraphRange r) {
		if (paint_thresholds) {
			paintOneThreshold(null, r);
		}
	}

	/**
	 * Paints one threshold or all thresholds.
	 * @param g graphics, may be null if no painting is actually required
	 * @param r range to be updated to cover curves painted 
	 * @param nEvent event number >= 0 to paint or -1 to paint all thresholds.
	 */
	private void paintThresholdsEx(Graphics g, GraphRange r, int nEvent)
	{
		ModelTraceManager etalon = Heap.getMTMEtalon();
		if (etalon == null)
			return;

		for (int key = 0; key < 4; key++)
		{
			// определяем цвет
			if (g != null)
				g.setColor(UIManager.getColor(Thresh.isKeyHard(key)
						? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
						: AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));

			// определяем, какую кривую рисовать
			ModelTrace thresholdMT = etalon.getThresholdMT(key);

			// Определяем диапазон отрисовки
			if (nEvent >= 0) {
				SimpleReflectogramEvent sre =
					etalon.getEventRangeOnThresholdCurve(nEvent, key);
				if (sre == null)
					continue; // if no region, then do not draw at all
				// When we draw thresholds for one event only, avoid drawing thresholds at the end point.
				// This is because sometimes (n/id event type) threshold curve can break.
				ModelTraceRange subrange = new ModelTraceRangeImplMTRSubrange(
						thresholdMT, sre.getBegin(), sre.getEnd(), false);
				// последнее событие рисуем вместе с конечной точкой
				drawModelCurve(g, r, subrange,
						nEvent != etalon.getMTAE().getNEvents() - 1);
			} else {
				drawModelCurve(g, r, thresholdMT, false);
			}
		}
	}

	/**
	 * Paints secondary line for one threshold or all thresholds.
	 * @param g graphics, null if no actual plotting is required
	 * @param r GraphRange to update, null if no range update is required
	 * @param nEvent event number, must be >= 0.
	 */
	private void paintThresholdsSec(Graphics g, GraphRange r, int nEvent, boolean dashStroke)
	{
		ModelTraceManager etalon = Heap.getMTMEtalon();
		if (etalon == null)
			return;

		ModelTraceRange[] curves = etalon.getEventThresholdMTR(nEvent);
		
		if (g != null) { // draw actually
			if (dashStroke)
				((Graphics2D)g).setStroke(ScaledGraphPanel.DASHED_STROKE);
			for (int key = 0; key < 4; key++) {
				if (curves[key] == null)
					continue;
				// определяем цвет
				g.setColor(UIManager.getColor(Thresh.isKeyHard(key)
						? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
						: AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));
				// последнее событие рисуем вместе с конечной точкой
				drawModelCurve(g, r, curves[key],
						nEvent != etalon.getMTAE().getNEvents() - 1);
			}
			if (dashStroke)
				((Graphics2D)g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
			//this.fps.inc();
		} else { // update range only
			for (int key = 0; key < 4; key++) {
				if (curves[key] == null)
					continue;
				// последнее событие рисуем вместе с конечной точкой
				drawModelCurve(g, r, curves[key],
						nEvent != etalon.getMTAE().getNEvents() - 1);
			}
		}
	}

	private void paintOneThreshold(Graphics g, GraphRange r)
	{
		int cEvent = Heap.getCurrentEtalonEvent2();
		if (cEvent >= 0)
		{
			// Note: эти два метода иногда могут давать заметно несовпадающие кривые.
			// Note: пунктирная линия - paintThresholdsSec(..., true) - очень медленно прорисовывается
			paintThresholdsSec(g, r, cEvent, false);
			// Note: при рисовании пунктир и сплошная кривая могут совпадать неточно, что приводит к "мохнатости" линии
			//paintThresholdsEx(g, c_event);
		}
	}
	private void paintAllThresholds(Graphics g, GraphRange r)
	{
		paintThresholdsEx(g, r, -1);
	}

	private boolean isToPaintAllThresholds()
	{
		return parent instanceof ThresholdsLayeredPanel
			&& ((ThresholdsLayeredPanel )parent).hasShowThresholdButtonSelected();
	}

	/**
	 * Paints label of primary trace to etalon out-of-mask mismatch.
	 * Note, does not perform any other comparison.
	 * Note, does not care for Heap.etalonComparison object.
	 * @param g graphics to paint on.
	 */
	private void paintTraceMismatch(Graphics g) {
		// ищем выход из масок
		final ModelTraceAndEventsImpl mtae = Heap.getPrimaryTrace().getMTAE();
		if (mtae == null)
			return;
		final ModelTraceManager etalon = Heap.getMTMEtalon();
		if (etalon == null)
			return;

		final ModelTrace mt = mtae.getModelTrace();
		final ReflectogramMismatchImpl outofMaskPoint =
			ModelTraceComparer.getOutOfMaskPoint(mt, etalon);

		if (outofMaskPoint.getSeverity().equals(Severity.SEVERITY_NONE)) {
			return; // no mismatch: nothing to plot
		}

		// определяем физическые (x,y) координаты точки выхода
		int index = outofMaskPoint.getCoord();
		double value = mt.getY(index);

		// определяем экранные (x,y) координаты точки выхода
		int xCoord = index2coord(index);
		int yCoord = value2coord(value);

		// параметры значка маркера
		final int markerHeight = 10;
		final int markerHaifWidth = markerHeight * 2 / 3;
		final int markerMargin = markerHeight / 3;

		int textMargin = 0;
		int textHeight = 0;
		int textAscent = 0;
		int textDescent = 0;
		int textWidth = 0;

			// параметры надписи
		String textToDraw = null;
		if (outofMaskPoint.hasMismatch()) {
			textToDraw = "" + MathRef.round_4((outofMaskPoint.getMinMismatch()
					+ outofMaskPoint.getMaxMismatch()) / 2.0);
			final FontMetrics fontMetrics = getFontMetrics(getFont());
			textMargin = markerMargin;
			textAscent = fontMetrics.getAscent();
			textDescent = fontMetrics.getDescent();
			textHeight = textAscent + textDescent;
			textWidth = fontMetrics.stringWidth(textToDraw);
		}

		// определяем, с какой стороны рисовать
		boolean inverse = yCoord <
				markerMargin + markerHeight + textMargin + textHeight;
		int sign = inverse ? 1 : -1;

		// рисуем пометку в точке выхода
		int sy0 = yCoord + sign * markerMargin;
		int sy1 = sy0 + sign * markerHeight;
		int sx0 = xCoord;
		int sx1 = xCoord - markerHaifWidth;
		int sx2 = xCoord + markerHaifWidth;
		int syText = sy1 + sign * markerMargin
			+ sign * (inverse ? textAscent : textDescent);
		int sxText = xCoord - textWidth / 2;

		// set color; XXX: use ALARM color
		g.setColor(UIManager.getColor(
				AnalysisResourceKeys.COLOR_ALARM_THRESHOLD));

		g.drawLine(sx0, sy0, sx1, sy1);
		g.drawLine(sx0, sy0, sx2, sy1);
		g.drawLine(sx2, sy1, sx1, sy1);

		// делаем надпись
		if (textToDraw != null)
			g.drawString(textToDraw, sxText, syText);
	}
}
