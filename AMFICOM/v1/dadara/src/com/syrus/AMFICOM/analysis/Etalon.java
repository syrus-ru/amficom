/*-
 * $Id: Etalon.java,v 1.3 2005/07/12 08:09:44 saa Exp $
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
import com.syrus.AMFICOM.analysis.dadara.SignatureMismatchException;

/**
 * "Эталон" для сравнения, т.е. все, с чем надо сравнивать.
 * Состоит из
 * <ol>
 * <li> double minTraceLevel - Уровня обнаружения обрыва;
 * <li> ModelTraceManager mtm - Эталонной а/к и событий;
 * <li> EventAnchorer anc - Идентификаторов для привязки к схеме, может быть null
 * </ol>
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/07/12 08:09:44 $
 * @module
 */
public class Etalon implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	private double minTraceLevel; // defined
	private ModelTraceManager mtm; // not null
	private EventAnchorer anc; // maybe null, maybe not

	public Etalon(ModelTraceManager mtm, double minTraceLevel, EventAnchorer anc) {
		this.mtm = mtm;
		this.minTraceLevel = minTraceLevel;
		this.anc = anc;
	}

	protected Etalon(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		minTraceLevel = dis.readDouble();
		mtm = (ModelTraceManager) ModelTraceManager.getReader().readFromDIS(dis);
		if (dis.readBoolean()) {
			anc = (EventAnchorer) EventAnchorer.getDSReader().readFromDIS(dis);
		} else {
			anc = null;
		}
	}

	/**
	 * @return (negative value)
	 */
	public double getMinTraceLevel() {
		return minTraceLevel;
	}

	/**
	 * @return MTM, not null
	 */
	public ModelTraceManager getMTM() {
		return mtm;
	}

	/**
	 * @return EventAnchorer (may be null)
	 */
	public EventAnchorer getAnc() {
		return anc;
	}
	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(minTraceLevel);
		mtm.writeToDOS(dos);
		dos.writeBoolean(anc != null);
		if (anc != null) {
			anc.writeToDOS(dos);
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
