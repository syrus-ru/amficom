/*-
 * $Id: CompositeEventList.java,v 1.7 2005/10/17 14:20:09 saa Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/10/17 14:20:09 $
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

	public CompositeEventList() {
	}

	public void dataUpdated() {
		updateRComp();
	}
	private void updateRComp() {
		SimpleReflectogramEventComparer comp = null;
		this.pNum = getPMTAE() != null ? getPMTAE().getNEvents() : 0;
		this.eNum = getEMTAE() != null ? getEMTAE().getNEvents() : 0; 
		if (getPMTAE() != null && getEMTAE() != null)
			comp = new SimpleReflectogramEventComparer(
					getPMTAE().getSimpleEvents(),
					getEMTAE().getSimpleEvents());
		this.p2c = new int[this.pNum];
		this.e2c = new int[this.eNum];
		this.c2p = new int[this.pNum + this.eNum]; // заведомо достаточная длина
		this.c2e = new int[this.pNum + this.eNum];
		this.c2pEx = new int[this.pNum + this.eNum];
		this.c2eEx = new int[this.pNum + this.eNum];
		int pi, ei, ci;
		for (pi = 0, ei = 0, ci = 0; pi < this.pNum || ei < this.eNum; ci++) {
			int e2p = comp != null && ei < this.eNum ?
					comp.getProbeIdByEtalonIdNonStrict(ei) : -1;
			int p2e = comp != null && pi < this.pNum ?
					comp.getEtalonIdByProbeIdNonStrict(pi) : -1;
			if (e2p > pi || ei == this.eNum) {
				this.p2c[pi] = ci;
				this.c2pEx[ci] = pi;
				this.c2eEx[ci] = p2e;
				this.c2p[ci] = pi;
				this.c2e[ci] = -1;
				pi++;
			} else if (e2p == pi && p2e == ei) {
				this.p2c[pi] = ci;
				this.e2c[ei] = ci;
				this.c2pEx[ci] = pi;
				this.c2eEx[ci] = ei;
				this.c2p[ci] = pi;
				this.c2e[ci] = ei;
				pi++;
				ei++;
			} else {
				this.e2c[ei] = ci;
				this.c2pEx[ci] = e2p;
				this.c2eEx[ci] = ei;
				this.c2p[ci] = -1;
				this.c2e[ci] = ei;
				ei++;
			}
			//System.out.println("c:p:e " + ci +":"+ pna[ci] +":"+ ena[ci]);
		}
		this.cNum = ci;
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
		return this.pNum;
	}

	public int getNEtalonEvents() {
		return this.eNum;
	}

	public int getNCompositeEvents() {
		return this.cNum;
	}

	public int getC2P(int c) {
		return c >= 0 ? this.c2p[c] : -1;
	}
	public int getC2E(int c) {
		return c >= 0 ? this.c2e[c] : -1;
	}
	public int getP2C(int p) {
		return p >= 0 ? this.p2c[p] : -1;
	}
	public int getE2C(int e) {
		return e >= 0 ? this.e2c[e] : -1;
	}

	public class Walker {
		private int n; // current composite number of event 

		public int getCompositeEvent() {
			return this.n;
		}
		public int getEvent1() {
			return this.n >= 0 ? CompositeEventList.this.c2p[this.n] : -1;
		}
		public int getEtalonEvent1() {
			return this.n >= 0 ? CompositeEventList.this.c2e[this.n] : -1;
		}
		public int getEvent2() {
			return this.n >= 0 ? CompositeEventList.this.c2pEx[this.n] : -1;
		}
		public int getEtalonEvent2() {
			return this.n >= 0 ? CompositeEventList.this.c2eEx[this.n] : -1;
		}

		public void toNextCompositeEvent() {
			this.n = inc(this.n, CompositeEventList.this.cNum);
		}
		public void toPrevCompositeEvent() {
			this.n = dec(this.n, CompositeEventList.this.cNum);
		}
		public void toNextEvent() {
			toEvent(inc(getEvent2(), CompositeEventList.this.pNum));
		}
		public void toPrevEvent() {
			toEvent(dec(getEvent2(), CompositeEventList.this.pNum));
		}
		public void toNextEtalonEvent() {
			toEtalonEvent(inc(getEtalonEvent2(), CompositeEventList.this.eNum));
		}
		public void toPrevEtalonEvent() {
			toEtalonEvent(dec(getEtalonEvent2(), CompositeEventList.this.eNum));
		}
		public void toEvent(int num) {
			this.n = num >= 0 ? CompositeEventList.this.p2c[num] : -1;
		}
		public void toEtalonEvent(int num) {
			this.n = num >= 0 ? CompositeEventList.this.e2c[num] : -1;
		}
		public void toCompositeEvent(int num) {
			this.n = num >= 0 ? num : -1;
		}
		public void fixNEvent() {
			if (this.n >= CompositeEventList.this.cNum)
				this.n = -1;
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
