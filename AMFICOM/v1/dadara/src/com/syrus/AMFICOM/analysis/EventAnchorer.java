/*-
 * $Id: EventAnchorer.java,v 1.1 2005/06/24 15:42:32 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.SignatureMismatchException;

public class EventAnchorer implements DataStreamable {
	// DIS reader singleton object
	private static DataStreamable.Reader dsReader = null;

	private SOAnchor[] anchorArray;

	EventAnchorer(int len) {
        this.anchorArray = new SOAnchor[len];
        for (int i = 0; i < this.anchorArray.length; i++) {
        	anchorArray[i] = SOAnchor.VOID_ANCHOR;
        }
	}

	public SOAnchor getEventAnchor(int nEvent) {
		return anchorArray[nEvent];
	}

	public void setEventAnchor(int nEvent, SOAnchor anchor) {
		anchorArray[nEvent] = anchor;
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeInt(anchorArray.length);
		for (int i = 0; i < anchorArray.length; i++) {
			anchorArray[i].writeToDOS(dos);
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
	throws IOException {
		int len = dis.readInt();
        SOAnchor[] anchors = new SOAnchor[len];
        for (int i = 0; i < anchors.length; i++) {
            anchors[i] = new SOAnchor(dis);
        }
	}
}
