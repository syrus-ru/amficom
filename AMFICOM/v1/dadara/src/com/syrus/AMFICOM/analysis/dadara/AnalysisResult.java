/*-
 * $Id: AnalysisResult.java,v 1.1 2005/06/21 09:31:25 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * ���������� ����������� �������
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/06/21 09:31:25 $
 * @module dadara
 */
public class AnalysisResult implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	private int dataLength; // ����� ���������� ��������������
	private int traceLength; // ����� ������� ������� �/� (�� ����� � ����)
	private ModelTraceAndEventsImpl mtae; // �/�, ������� � deltaX

	public AnalysisResult(int dataLength, int traceLength,
			ModelTraceAndEventsImpl mtae) {
		this.dataLength = dataLength;
		this.traceLength = traceLength;
		this.mtae = mtae;
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

	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeInt(dataLength);
		dos.writeInt(traceLength);
		mtae.writeToDOS(dos);
	}
	public static DataStreamable.Reader getDSReader() {
		// singleton
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					int dataLength = dis.readInt();
					int traceLength = dis.readInt();
					ModelTraceAndEventsImpl mtae = (ModelTraceAndEventsImpl)
						ModelTraceAndEventsImpl.getReader().readFromDIS(dis);
					return new AnalysisResult(dataLength, traceLength, mtae);
				}
			};
		}
		return dsReader;
	}
	public byte[] toByteArray() {
		return DataStreamableUtil.writeDataStreamableToBA(this);
	}
}
