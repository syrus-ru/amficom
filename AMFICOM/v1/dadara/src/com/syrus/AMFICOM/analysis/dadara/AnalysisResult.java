/*-
 * $Id: AnalysisResult.java,v 1.4 2005/09/30 12:56:22 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.io.SignatureMismatchException;

/**
 * ���������� ����������� ������� � ���������:
 * <ul>
 * <li> ���������� �������:
 *   <ul>
 *   <li> dataLength - ����� ���������� ��������������;
 *   <li> traceLength - ����� ������� ������� �/� (�� ����� � ����);
 *   <li> MTAE - ModelTraceAndEventsImpl - �/�, �������, deltaX
 *     (�������������, ����� �� � ��������� ������ ������� ����� �������).
 *   </ul>
 * <li> ����������� ���������:
 *   <ul>
 *   <li> anchorer (����� ���� null) - {@link EventAnchorer}.
 *   </ul>
 *   ������ ���������� ��������� ���� �� �����������.
 * </ul>
 * ���� ����������� ������� (����) ���������������,
 * ���� ����������� ��������� ������������� � ���������� null.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.4 $, $Date: 2005/09/30 12:56:22 $
 * @module dadara
 */
public class AnalysisResult implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	// ���������� �������
	private int dataLength; // ����� ���������� ��������������
	private int traceLength; // ����� ������� ������� �/� (�� ����� � ����)
	private ModelTraceAndEventsImpl mtae; // �/�, ������� � deltaX

	// ���������� ��������� (��� ���� ����� ���� null)
	private EventAnchorer anchorer = null;

	private static final long SIGNATURE = 7990616050929170500L;

	public AnalysisResult(int dataLength, int traceLength,
			ModelTraceAndEventsImpl mtae) {
		this.dataLength = dataLength;
		this.traceLength = traceLength;
		this.mtae = mtae;
		this.anchorer = null;
	}
	protected AnalysisResult() {
		// for dis reading
	}

	public int getDataLength() {
		return dataLength;
	}
	public ModelTraceAndEventsImpl getMTAE() {
		return mtae;
	}
	public int getTraceLength() {
		return traceLength;
	}
	public EventAnchorer getAnchorer() {
		return anchorer;
	}
	public void setAnchorer(EventAnchorer anchorer) {
		this.anchorer = anchorer;
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeLong(SIGNATURE);
		dos.writeInt(dataLength);
		dos.writeInt(traceLength);
		mtae.writeToDOS(dos);
		dos.writeBoolean(anchorer != null);
		if (anchorer != null) {
			anchorer.writeToDOS(dos);
		}
	}

	protected void readFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		dataLength = dis.readInt();
		traceLength = dis.readInt();
		mtae = (ModelTraceAndEventsImpl)
				ModelTraceAndEventsImpl.getReader().readFromDIS(dis);
		if (dis.readBoolean()) {
			anchorer = (EventAnchorer) EventAnchorer.getDSReader().readFromDIS(dis);
		} else {
			anchorer = null;
		}
	}

	public static DataStreamable.Reader getDSReader() {
		// singleton
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					AnalysisResult ar = new AnalysisResult();
					ar.readFromDIS(dis);
					return ar;
				}
			};
		}
		return dsReader;
	}
	public byte[] toByteArray() {
		return DataStreamableUtil.writeDataStreamableToBA(this);
	}
}
