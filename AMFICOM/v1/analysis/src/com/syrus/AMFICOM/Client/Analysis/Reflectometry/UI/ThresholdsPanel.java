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
import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * ���������� ������
 */
public class ThresholdsPanel extends ReflectogramEventsPanel
{
	protected boolean paint_thresholds = true;
	protected boolean edit_thresholds = true;

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

	public ThresholdsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
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

		boolean allThresholds = this.isToPaintAllThresholds(); // ����� "��� ������"

		// �������� "��������" (drag) �����
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
			// ���������� ���� � ����� �������
			try
			{
				Robot r = new Robot();
				Point p = getLocationOnScreen();
				int x1 = index2coord(c_TH.getX());
				int y1 = value2coord(c_TH.getY());

				// �� ��������� ������ �� ������� ����
				currpos = new Point(x1, y1);
				limit_currpos();

				// ���������� ������
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

	protected void this_mouseDragged(MouseEvent e)
	{
		if (!edit_thresholds || Heap.getMTMEtalon() == null)
		{
			super.this_mouseDragged(e);
			return;
		}
		if (c_TH != null)
		{
			upd_currpos(e); // ������ tmppos - ���������� ���������, �  currpos - �����
			c_TH.moveBy(
				(currpos.x - tmppos.x) / getTrueScaleX(),
				(currpos.y - tmppos.y) / getTrueScaleY());

			parent.repaint();

			dispatcher.firePropertyChange(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		} else
			// ����� �������� ������� �����������
			super.this_mouseDragged(e);
	}

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

	protected void this_mouseClicked(MouseEvent e)
	{
        // ��� ���� ������� ��������� � ������� �������,
        // ��� ���� ������� - �� ���������
        if (edit_thresholds) {
    	    // ���� ��������, �� �� �� ������� �������, ��������� � ������ �������
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

	public void paint (Graphics g)
	{
		paint_scales(g);

		if (draw_events)
		{
			paint_reflectogram_events(g);
		} else
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
		} else if (draw_noise_level && draw_events)
			paint_noise_level(g);

		paint_scale_digits(g);

		if (paint_thresholds) {
			if(isToPaintAllThresholds())
				paintAllThresholds(g, null);
			else
				paintOneThreshold(g, null);
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
            // ���������� ����
            if (g != null)
                g.setColor(UIManager.getColor(Thresh.isKeyHard(key)
                        ? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
                        : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));

            // ����������, ����� ������ ��������
			ModelTrace thresholdMT = etalon.getThresholdMT(key);

            // ���������� �������� ���������
			if (nEvent >= 0) {
                SimpleReflectogramEvent sre =
                    etalon.getEventRangeOnThresholdCurve(nEvent, key);
				if (sre == null)
					continue; // if no region, then do not draw at all
                // When we draw thresholds for one event only, avoid drawing thresholds at the end point.
                // This is because sometimes (n/id event type) threshold curve can break.
                ModelTraceRange subrange = new ModelTraceRangeImplMTRSubrange(
                        thresholdMT, sre.getBegin(), sre.getEnd(), false);
            	// ��������� ������� ������ ������ � �������� ������
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
                // ���������� ����
                g.setColor(UIManager.getColor(Thresh.isKeyHard(key)
                        ? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
                        : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));
            	// ��������� ������� ������ ������ � �������� ������
                drawModelCurve(g, r, curves[key],
                		nEvent != etalon.getMTAE().getNEvents() - 1);
            }
            if (dashStroke)
                ((Graphics2D)g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
            //this.fps.inc();
        } else { // update range only
            for (int key = 0; key < 4; key++) {
            	// ��������� ������� ������ ������ � �������� ������
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
            // Note: ��� ��� ������ ������ ����� ������ ������� ������������� ������.
            // Note: ���������� ����� - paintThresholdsSec(..., true) - ����� �������� ���������������
            paintThresholdsSec(g, r, cEvent, false);
            // Note: ��� ��������� ������� � �������� ������ ����� ��������� �������, ��� �������� � "����������" �����
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
}
