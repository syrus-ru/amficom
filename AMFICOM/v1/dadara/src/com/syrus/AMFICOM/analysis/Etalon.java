/*-
 * $Id: Etalon.java,v 1.2 2005/06/30 16:19:20 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
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
 * "������" ��� ���������, �.�. ���, � ��� ���� ����������.
 * ������� ��
 * <ol>
 * <li> double minTraceLevel - ������ ����������� ������;
 * <li> ModelTraceManager mtm - ��������� �/� � �������;
 * <li> EventAnchorer anc - ��������������� ��� �������� � �����, ����� ���� null
 * </ol>
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/06/30 16:19:20 $
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
	 * @param minTraceLevel (negative value)
	 */
	public void setMinTraceLevel(double minTraceLevel) {
		this.minTraceLevel = minTraceLevel;
	}

	/**
	 * @return (negative value)
	 */
	public double getMinTraceLevel() {
		return minTraceLevel;
	}

	/**
	 * @param mtm (not null)
	 */
	public void setMTM(ModelTraceManager mtm) {
		this.mtm = mtm;
	}

	/**
	 * @return MTM, not null
	 */
	public ModelTraceManager getMTM() {
		return mtm;
	}

	/**
	 * @param anc may be null
	 */
	public void setAnc(EventAnchorer anc) {
		this.anc = anc;
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
