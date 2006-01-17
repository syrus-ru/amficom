/*-
 * $Id: AnalysisResult.java,v 1.8 2006/01/17 12:17:26 saa Exp $
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
 * Агрегатор результатов анализа и данных о привязке
 * <ul>
 * <li> результаты анализа:
 *   <ul>
 *   <li> dataLength - длина полученной рефлектограммы;
 *   <li> traceLength - длина рабочей области р/г (до ухода в ноль);
 *   <li> MTAE - ModelTraceAndEventsImpl - а/к, события, deltaX
 *     (следовательно, здесь же и дистанция начала события конца волокна).
 *   </ul>
 * <li> привязка (в результате сравнения):
 *   <ul>
 *   <li> anchorer (может быть null) - {@link EventAnchorer}.
 *   </ul>
 *   Другие результаты сравнения здесь не используются.
 * </ul>
 * <p>Поля результатов анализа немодицифируемы, not null;
 * поле привязки модифицируемо и может быть null.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.8 $, $Date: 2006/01/17 12:17:26 $
 * @module dadara
 */
public class AnalysisResult implements DataStreamable {
	private static DataStreamable.Reader dsReader = null;

	// результаты анализа
	private int dataLength; // длина полученной рефлектограммы
	private int traceLength; // длина рабочей области р/г (до ухода в шумы)
	private ModelTraceAndEventsImpl mtae; // а/к, события и deltaX (здесь же и длина р/г до EOT)

	// результаты сравнения (может быть null)
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

	public AnalysisResult(AnalysisResult that) {
		this.dataLength = that.dataLength;
		this.traceLength = that.traceLength;
		this.mtae = new ModelTraceAndEventsImpl(that.mtae);
		this.anchorer = that.anchorer == null ? null : new EventAnchorer(that.anchorer);
	}

	public int getDataLength() {
		return this.dataLength;
	}
	public ModelTraceAndEventsImpl getMTAE() {
		return this.mtae;
	}
	public int getTraceLength() {
		return this.traceLength;
	}
	public EventAnchorer getAnchorer() {
		return this.anchorer;
	}
	public void setAnchorer(EventAnchorer anchorer) {
		this.anchorer = anchorer;
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.dataLength);
		dos.writeInt(this.traceLength);
		this.mtae.writeToDOS(dos);
		dos.writeBoolean(this.anchorer != null);
		if (this.anchorer != null) {
			this.anchorer.writeToDOS(dos);
		}
	}

	protected void readFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		this.dataLength = dis.readInt();
		this.traceLength = dis.readInt();
		this.mtae = (ModelTraceAndEventsImpl)
				ModelTraceAndEventsImpl.getReader().readFromDIS(dis);
		if (dis.readBoolean()) {
			this.anchorer = (EventAnchorer) EventAnchorer.getDSReader().readFromDIS(dis);
		} else {
			this.anchorer = null;
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
