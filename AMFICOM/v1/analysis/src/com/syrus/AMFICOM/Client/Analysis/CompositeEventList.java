/*-
 * $Id: CompositeEventList.java,v 1.2 2005/05/03 12:41:11 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/03 12:41:11 $
 * @module
 */
public class CompositeEventList {
    protected int pNum = 0; // number of primary events
    protected int eNum = 0; // number of etaon events
    protected int cNum = 0; // number of composite events
    protected int[] p2c = null; // primary to composite
    protected int[] e2c = null; // etalon to composite
    protected int[] c2p = null; // composite number to primary number (m b -1)
    protected int[] c2e = null; // composite number to etalon number (m b -1)
    protected int[] c2pEx = null; // non-strict mapping
    protected int[] c2eEx = null; // non-strict mapping

    /**
     * @param strictMode false, if you allow one A event being in many
     * iterations paired to different B events and vica versa.
     * true, if you want find every A and every B event only one time
     * during any kind of iteration.
     */
    public CompositeEventList(boolean strictMode) {
    }

    public void dataUpdated() {
        updateRComp();
    }
    private void updateRComp() {
        SimpleReflectogramEventComparer comp = null;
        pNum = getPMTAE() != null ? getPMTAE().getNEvents() : 0;
        eNum = getEMTAE() != null ? getEMTAE().getNEvents() : 0; 
        if (getPMTAE() != null && getEMTAE() != null)
            comp = new SimpleReflectogramEventComparer(
                    getPMTAE().getSimpleEvents(),
                    getEMTAE().getSimpleEvents(),
                    false);
        p2c = new int[pNum];
        e2c = new int[eNum];
        c2p = new int[pNum + eNum]; // заведомо достаточная длина
        c2e = new int[pNum + eNum];
        c2pEx = new int[pNum + eNum];
        c2eEx = new int[pNum + eNum];
        int pi, ei, ci;
        for (pi = 0, ei = 0, ci = 0; pi < pNum || ei < eNum; ci++) {
            int e2p = comp != null && ei < eNum ?
                    comp.getProbeIdByEtalonId(ei) : -1;
            int p2e = comp != null && pi < pNum ?
                    comp.getEtalonIdByProbeId(pi) : -1;
            if (e2p > pi || ei == eNum) {
                p2c[pi] = ci;
                c2pEx[ci] = pi;
                c2eEx[ci] = p2e;
                c2p[ci] = pi;
                c2e[ci] = -1;
                pi++;
            } else if (e2p == pi && p2e == ei) {
                p2c[pi] = ci;
                e2c[ei] = ci;
                c2pEx[ci] = pi;
                c2eEx[ci] = ei;
                c2p[ci] = pi;
                c2e[ci] = ei;
                pi++;
                ei++;
            } else {
                e2c[ei] = ci;
                c2pEx[ci] = e2p;
                c2eEx[ci] = ei;
                c2p[ci] = -1;
                c2e[ci] = ei;
                ei++;
            }
            //System.out.println("c:p:e " + ci +":"+ pna[ci] +":"+ ena[ci]);
        }
        cNum = ci;
        //System.out.println("pl " + pl + " el " + el + " len " + len);
    }
    private ModelTraceAndEvents getPMTAE() {
        return Heap.getMTAEPrimary();
    }
    private ModelTraceAndEvents getEMTAE() {
        return Heap.getMTMEtalon() == null
                ? null
                : Heap.getMTMEtalon().getMTAE();
    }

    public int getNEvents() {
        return pNum;
    }

    public int getNEtalonEvents() {
        return eNum;
    }

    public int getNCompositeEvents() {
        return cNum;
    }

    public int getC2P(int c) {
        return c >= 0 ? c2p[c] : -1;
    }
    public int getC2E(int c) {
        return c >= 0 ? c2e[c] : -1;
    }
    public int getP2C(int p) {
        return p >= 0 ? p2c[p] : -1;
    }
    public int getE2C(int e) {
        return e >= 0 ? e2c[e] : -1;
    }

    public class Walker {
        private int n; // current composite number of event 

        public int getCompositeEvent() {
            return n;
        }
        public int getEvent1() {
            return n >= 0 ? c2p[n] : -1;
        }
        public int getEtalonEvent1() {
            return n >= 0 ? c2e[n] : -1;
        }
        public int getEvent2() {
            return n >= 0 ? c2pEx[n] : -1;
        }
        public int getEtalonEvent2() {
            return n >= 0 ? c2eEx[n] : -1;
        }

        public void toNextCompositeEvent() {
            n = inc(n, cNum);
        }
        public void toPrevCompositeEvent() {
            n = dec(n, cNum);
        }
        public void toNextEvent() {
            toEvent(inc(getEvent2(), pNum));
        }
        public void toPrevEvent() {
            toEvent(dec(getEvent2(), pNum));
        }
        public void toNextEtalonEvent() {
            toEtalonEvent(inc(getEtalonEvent2(), eNum));
        }
        public void toPrevEtalonEvent() {
            toEtalonEvent(dec(getEtalonEvent2(), eNum));
        }
        public void toEvent(int num) {
            n = num >= 0 ? p2c[num] : -1;
        }
        public void toEtalonEvent(int num) {
            n = num >= 0 ? e2c[num] : -1;
        }
        public void toCompositeEvent(int num) {
            n = num >= 0 ? num : -1;
        }
        public void fixNEvent() {
            if (n >= cNum)
                n = -1;
        }
    }
    protected static int inc(int i, int len) {
        i++;
        if (i >= len)
            i = len > 0 ? 0 : -1;
        return i;
    }
    protected static int dec(int i, int len) {
        if (i <= 0)
            i = len;
        i--;
        return i;
    }
}