/*-
 * $Id: EventIterator.java,v 1.2 2005/05/02 16:57:00 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;

/**
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/05/02 16:57:00 $
 * @module
 */
public class EventIterator {
    private int[] pn = null;
    private int[] en = null;
    private int len = 0;
    private int n = -1;
    private boolean strictMode;

    /**
     * @param strictMode false, if you allow one A event being in many
     * iterations paired to different B events and vica versa.
     * true, if you want find every A and every B event only one time
     * during any kind of iteration.
     * @todo: false strictMode is not now properly working with
     * to(Prev|Next)(|Etalon)Event, its ok only for to*CompositeEvent
     */
    public EventIterator(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public int getEvent() {
        return n >= 0 ? pn[n] : -1;
    }
    public int getEtalonEvent() {
        return n >= 0 ? en[n] : -1;
    }
    public void toNextCompositeEvent() {
        n++;
        if (n >= len)
            n = len > 0 ? 0 : -1;
    }
    public void toPrevCompositeEvent() {
        if (n <= 0)
            n = len;
        n--;
    }
    public void toNextEvent() {
        for (int i = 0; i < len; i++) {
            toNextCompositeEvent();
            if (getEvent() >= 0)
                return;
        }
    }
    public void toPrevEvent() {
        for (int i = 0; i < len; i++) {
            toPrevCompositeEvent();
            if (getEvent() >= 0)
                return;
        }
    }
    public void toNextEtalonEvent() {
        for (int i = 0; i < len; i++) {
            toNextCompositeEvent();
            if (getEtalonEvent() >= 0)
                return;
        }
    }
    public void toPrevEtalonEvent() {
        for (int i = 0; i < len; i++) {
            toPrevCompositeEvent();
            if (getEtalonEvent() >= 0)
                return;
        }
    }
    public void toEvent(int num) {
        for (n = 0; n < len; n++) {
            if (pn[n] == num)
                return;
        }
        n = -1;
    }
    public void toEtalonEvent(int num) {
        for (n = 0; n < len; n++) {
            if (en[n] == num)
                return;
        }
        n = -1;
    }

    public void dataUpdated() {
        updateRComp();
        fixNEvent();
    }

    private ModelTraceAndEvents getPMTAE() {
        return Heap.getMTAEPrimary();
    }
    private ModelTraceAndEvents getEMTAE() {
        return Heap.getMTMEtalon() == null
                ? null
                : Heap.getMTMEtalon().getMTAE();
    }
    private void updateRComp() {
        SimpleReflectogramEventComparer comp = null;
        int pl = getPMTAE() != null ? getPMTAE().getNEvents() : 0;
        int el = getEMTAE() != null ? getEMTAE().getNEvents() : 0; 
        if (getPMTAE() != null && getEMTAE() != null)
            comp = new SimpleReflectogramEventComparer(
                    getPMTAE().getSimpleEvents(),
                    getEMTAE().getSimpleEvents(),
                    strictMode);
        int[] pna = new int[pl + el];
        int[] ena = new int[pl + el];
        int pi, ei, ci;
        for (pi = 0, ei = 0, ci = 0; pi < pl || ei < el; ci++) {
            int e2p = comp != null && ei < el ?
                    comp.getProbeIdByEtalonId(ei) : -1;
            int p2e = comp != null && pi < pl ?
                    comp.getEtalonIdByProbeId(pi) : -1;
            if (e2p > pi || ei == el) {
                pna[ci] = pi++;
                ena[ci] = strictMode ? -1 : p2e;
            } else if (e2p == pi && p2e == ei) {
                pna[ci] = pi++;
                ena[ci] = ei++;
            } else {
                pna[ci] = strictMode ? -1 : e2p;
                ena[ci] = ei++;
            }
            //System.out.println("c:p:e " + ci +":"+ pna[ci] +":"+ ena[ci]);
        }
        pn = pna;
        en = ena;
        len = ci;
        //System.out.println("pl " + pl + " el " + el + " len " + len);
    }
    private void fixNEvent() {
        if (n >= len)
            n = -1;
    }
}