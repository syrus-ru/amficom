/*-
 * $Id: EventAnchorer.java,v 1.7 2005/10/06 14:32:34 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.dadara.DataStreamable;
import com.syrus.io.SignatureMismatchException;

public class EventAnchorer implements DataStreamable {
	private static final long SIGNATURE = 2567555050929170200L;

	// DIS reader singleton object
	private static DataStreamable.Reader dsReader = null;

	private SOAnchorImpl[] anchorArray;

	public EventAnchorer(int len) {
		this.anchorArray = new SOAnchorImpl[len];
		for (int i = 0; i < this.anchorArray.length; i++) {
			this.anchorArray[i] = SOAnchorImpl.VOID_ANCHOR;
		}
	}

	public SOAnchorImpl getEventAnchor(int nEvent) {
		return this.anchorArray[nEvent];
	}

	public void setEventAnchor(int nEvent, SOAnchorImpl anchor) {
		this.anchorArray[nEvent] = anchor;
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.anchorArray.length);
		for (int i = 0; i < this.anchorArray.length; i++) {
			this.anchorArray[i].writeToDOS(dos);
		}
	}

	public static DataStreamable.Reader getDSReader() {
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					return new EventAnchorer(dis);
				}
			};
		}
		return dsReader;
	}

	protected EventAnchorer(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		int len = dis.readInt();
		// FIXME
		SOAnchor[] anchors = new SOAnchor[len];
		for (int i = 0; i < anchors.length; i++) {
			anchors[i] = SOAnchorImpl.createFromDIS(dis);
		}
	}
}
