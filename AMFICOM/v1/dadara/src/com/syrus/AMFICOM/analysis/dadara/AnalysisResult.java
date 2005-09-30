/*-
 * $Id: AnalysisResult.java,v 1.4 2005/09/30 12:56:22 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
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
 * Аггрегатор результатов анализа и сравнения:
 * <ul>
 * <li> результаты анализа:
 *   <ul>
 *   <li> dataLength - длина полученной рефлектограммы;
 *   <li> traceLength - длина рабочей области р/г (до ухода в ноль);
 *   <li> MTAE - ModelTraceAndEventsImpl - а/к, события, deltaX
 *     (следовательно, здесь же и дистанция начала события конца волокна).
 *   </ul>
 * <li> результатов сравнения:
 *   <ul>
 *   <li> anchorer (может быть null) - {@link EventAnchorer}.
 *   </ul>
 *   Другие результаты сравнения пока не реализованы.
 * </ul>
 * Поля результатов анализа (пока) немодицифируемы,
 * поля результатов сравнения модифицируемы и изначально null.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.4 $, $Date: 2005/09/30 12:56:22 $
 * @module dadara
 */
public class AnalysisResult implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	// результаты анализа
	private int dataLength; // длина полученной рефлектограммы
	private int traceLength; // длина рабочей области р/г (до ухода в ноль)
	private ModelTraceAndEventsImpl mtae; // а/к, события и deltaX

	// результаты сравнения (все поля могут быть null)
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
