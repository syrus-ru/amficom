/*-
 * $Id: Etalon.java,v 1.6 2005/09/30 12:56:21 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.io.SignatureMismatchException;

/**
 * "Эталон" для сравнения, т.е. все, с чем надо сравнивать.
 * Состоит из
 * <ol>
 * <li> double minTraceLevel - Уровня обнаружения обрыва;
 * <li> ModelTraceManager mtm - Эталонной а/к и событий;
 * <li> EventAnchorer anc - Идентификаторов для привязки к схеме, может быть null
 * </ol>
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/09/30 12:56:21 $
 * @module
 */
public class Etalon implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	private double minTraceLevel; // defined
	private ModelTraceManager mtm; // not null
	private EventAnchorer anc; // maybe null, maybe not

	private static final long SIGNATURE = 6685629050929163900L;

	public Etalon(ModelTraceManager mtm, double minTraceLevel, EventAnchorer anc) {
		this.mtm = mtm;
		this.minTraceLevel = minTraceLevel;
		this.anc = anc;
	}

	protected Etalon(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		this.minTraceLevel = dis.readDouble();
		this.mtm = (ModelTraceManager) ModelTraceManager.getReader().readFromDIS(dis);
		if (dis.readBoolean()) {
			this.anc = (EventAnchorer) EventAnchorer.getDSReader().readFromDIS(dis);
		} else {
			this.anc = null;
		}
	}

	/**
	 * @return (negative value)
	 */
	public double getMinTraceLevel() {
		return this.minTraceLevel;
	}

	/**
	 * @return MTM, not null
	 */
	public ModelTraceManager getMTM() {
		return this.mtm;
	}

	/**
	 * @return EventAnchorer (may be null)
	 */
	public EventAnchorer getAnc() {
		return this.anc;
	}
	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeLong(SIGNATURE);
		dos.writeDouble(this.minTraceLevel);
		this.mtm.writeToDOS(dos);
		dos.writeBoolean(this.anc != null);
		if (this.anc != null) {
			this.anc.writeToDOS(dos);
		}
	}

	public static DataStreamable.Reader getDSReader() {
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					return new Etalon(dis);
				}
			};
		}
		return dsReader;
	}
}
