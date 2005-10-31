/*-
 * $Id: ModelTraceAndEventsImpl.java,v 1.29 2005/10/31 12:30:19 bass Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;


import static java.util.logging.Level.FINEST;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.io.SignatureMismatchException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.29 $, $Date: 2005/10/31 12:30:19 $
 * @module
 */
public class ModelTraceAndEventsImpl
implements ReliabilityModelTraceAndEvents, DataStreamable {
	protected static final long SIGNATURE_MTAE =  6205542050930132000L;
	protected static final short SIGNATURE_CINFO_SHORT = 11500;
	protected static final double CINFO_DEVIATION_PREC = 1e-4;

	protected ReliabilitySimpleReflectogramEventImpl[] rse; // not null
	//private ComplexReflectogramEvent[] ce; // auto-generated, not null
	private ModelFunction mf; // not null
	private int traceLength;
	private double deltaX;
	protected ModelTrace mt; // will just contain mt
	protected ComplexInfo cinfo;

	private DetailedEvent[] detailedEventsCache = null; 

	private static DataStreamable.Reader dsReader = null; // DIS reader singleton-style object

	// private only: cinfo should be initialized later
	protected ModelTraceAndEventsImpl(
			ReliabilitySimpleReflectogramEventImpl[] rse,
			ModelFunction mf,
			double deltaX)
	{
		this.rse = rse;
		this.mf = mf;
		this.deltaX = deltaX;
		this.traceLength = calcTraceLength();
		this.mt = new ModelTraceImplMF(this.getMF(), this.getTraceLength());
	}

	public ModelTraceAndEventsImpl(ReliabilitySimpleReflectogramEventImpl[] rse,
			ModelFunction mf, double[] y, double deltaX) {
		this(rse, mf, deltaX);
		this.cinfo = new ComplexInfo(y); // use all our internal fields initialized by this moment
	}

	public ModelTraceAndEventsImpl(ModelTraceAndEventsImpl that) {
		// полагаемся на то, что объекты RSE неизменны
		this(that.getRSE().clone(), that.getMF().copy(), that.deltaX);
		this.cinfo = new ComplexInfo(that.cinfo);
	}

	public static ModelTraceAndEventsImpl replaceRSE(
			ModelTraceAndEventsImpl that,
			ReliabilitySimpleReflectogramEventImpl[] rse,
			double[] y) {
		return new ModelTraceAndEventsImpl(rse, that.mf, y, that.deltaX);
	}
			

	public double getDeltaX()
	{
		return this.deltaX;
	}
	public ModelTrace getModelTrace()
	{
		return this.mt;
	}

	/**
	 * Собирает из y[] информацию необходимую для DetailedEvent,
	 * которой нет ни в rse[], ни в mf.
	 * Эту информацию можно будет компактно запомнить в поток,
	 * и/или построить по ней и rse[], mf, DetailedEvent
	 * @author saa
	 * @module
	 */
	protected class ComplexInfo implements DataStreamable {
		private double yTop;
		private int[] edz;
		private int[] adz;
		private int[] rmsDevI;
		private int[] maxDevI;

		protected boolean eventNeedsEdzAdzPo(int nEvent) {
			return ModelTraceAndEventsImpl.this.rse[nEvent].getEventType() ==
				SimpleReflectogramEvent.DEADZONE;
		}
		protected boolean eventNeedsMaxDev(int nEvent) {
			return
			   ModelTraceAndEventsImpl.this.rse[nEvent].getEventType() == SimpleReflectogramEvent.LINEAR
			|| ModelTraceAndEventsImpl.this.rse[nEvent].getEventType() == SimpleReflectogramEvent.NOTIDENTIFIED;
		}

		protected int getEdz(int i) {
			return this.edz[i];
		}
		protected int getAdz(int i) {
			return this.adz[i];
		}
		protected double getMaxDev(int i) {
			return this.maxDevI[i] * CINFO_DEVIATION_PREC;
		}
		protected double getRmsDev(int i) {
			return this.rmsDevI[i] * CINFO_DEVIATION_PREC;
		}

		public double getYTop() {
			return this.yTop;
		}
		private void allocateArrays() {
			this.edz = new int[ModelTraceAndEventsImpl.this.rse.length];
			this.adz = new int[ModelTraceAndEventsImpl.this.rse.length];
			this.maxDevI = new int[ModelTraceAndEventsImpl.this.rse.length];
			this.rmsDevI = new int[ModelTraceAndEventsImpl.this.rse.length];
		}

		private ModelTraceAndEventsImpl getOwner() {
			return ModelTraceAndEventsImpl.this;
		}

		// используется при копировании MTAEI
		public ComplexInfo(ComplexInfo that) {
			assert ModelTraceAndEventsImpl.this.rse.length == that.getOwner().rse.length;
			this.yTop = that.yTop;
			this.edz = that.edz.clone();
			this.adz = that.adz.clone();
			this.rmsDevI = that.rmsDevI;
			this.maxDevI = that.maxDevI;
		}

		public ComplexInfo(double[] y) {
			allocateArrays();
			this.yTop = ReflectogramMath.getArrayMax(y);
			for (int i = 0; i < ModelTraceAndEventsImpl.this.rse.length; i++)
			{
				this.edz[i] = 0;
				this.adz[i] = 0;
				this.rmsDevI[i] = 0;
				this.maxDevI[i] = 0;
				if (eventNeedsEdzAdzPo(i)) {
					double po = ReflectogramMath.getPo(ModelTraceAndEventsImpl.this.rse, i, ModelTraceAndEventsImpl.this.mt);
					int[] res = ReflectogramMath.getEdzAdz(po, ModelTraceAndEventsImpl.this.rse[i], ModelTraceAndEventsImpl.this.mt);
					this.edz[i] = res[0];
					this.adz[i] = res[1];
				}
				if (eventNeedsMaxDev(i)) {
					// округляем вверх
					this.maxDevI[i] = (int)Math.ceil(
							ReflectogramMath.getMaxDev(y, ModelTraceAndEventsImpl.this.rse[i], ModelTraceAndEventsImpl.this.mt)
								/ CINFO_DEVIATION_PREC);
					this.rmsDevI[i] = (int)Math.ceil(
							ReflectogramMath.getRmsDev(y, ModelTraceAndEventsImpl.this.rse[i], ModelTraceAndEventsImpl.this.mt)
								/ CINFO_DEVIATION_PREC);
				}
			}
		}

		protected ComplexInfo(DataInputStream dis)
		throws IOException, SignatureMismatchException {
			if (dis.readShort() != SIGNATURE_CINFO_SHORT) {
				throw new SignatureMismatchException();
			}
			this.yTop = dis.readDouble();
			allocateArrays();
			for (int i = 0; i < ModelTraceAndEventsImpl.this.rse.length; i++) {
				this.edz[i] = 0;
				this.adz[i] = 0;
				this.rmsDevI[i] = 0;
				this.maxDevI[i] = 0;
				if (eventNeedsEdzAdzPo(i)) {
					this.edz[i] = dis.readInt();
					this.adz[i] = dis.readInt();
				}
				if (eventNeedsMaxDev(i)) {
					this.maxDevI[i] = dis.readInt();
					this.rmsDevI[i] = dis.readInt();
				}
			}
		}

		public void writeToDOS(DataOutputStream dos) throws IOException {
			dos.writeShort(SIGNATURE_CINFO_SHORT);
			dos.writeDouble(this.yTop);
			for (int i = 0; i < ModelTraceAndEventsImpl.this.rse.length; i++) {
				if (eventNeedsEdzAdzPo(i)) {
					dos.writeInt(this.edz[i]);
					dos.writeInt(this.adz[i]);
				}
				if (eventNeedsMaxDev(i)) {
					dos.writeInt(this.maxDevI[i]);
					dos.writeInt(this.rmsDevI[i]);
				}
			}
		}
	}

	private int eventLength(int i) {
		return this.rse[i].getEnd() - this.rse[i].getBegin();
	}
	private double linearTangent(int i) {
		int begin = this.rse[i].getBegin();
		int end = this.rse[i].getEnd();
		return (this.mt.getY(begin) - this.mt.getY(end)) / (end - begin);
	}

	private boolean isEventLinear(int i) {
		return this.rse[i].getEventType() == SimpleReflectogramEvent.LINEAR;
	}

	private double getAddToMLoss(int i, boolean useLeft, boolean useRight) {
		// берем средний (из одного или двух) наклон смежных линейных
		// событий, которые по длине больше текущего события
		int linCount = 0;
		double linAtt = 0;
		if (useLeft && i > 0 && isEventLinear(i - 1)
				&& eventLength(i - 1) > eventLength(i))
		{
			linCount++;
			linAtt += linearTangent(i - 1);
		}
		if (useRight && i < this.rse.length - 1 && isEventLinear(i)
				&& eventLength(i + 1) > eventLength(i))
		{
			linCount++;
			linAtt += linearTangent(i + 1);
		}
		if (linCount > 0)
			linAtt /= linCount;
		return linAtt * eventLength(i);
	}

	/**
	 * обеспечивает кэширование с lazy-инициализацией
	 */
	public DetailedEvent getDetailedEvent(int i) {
		if (this.detailedEventsCache == null)
			this.detailedEventsCache = new DetailedEvent[this.rse.length];
		if (this.detailedEventsCache[i] == null)
			this.detailedEventsCache[i] = makeDetailedEvent(i);
		return this.detailedEventsCache[i];
	}

	/**
	 * обеспечивает кэширование с lazy-инициализацией
	 */
	public DetailedEvent[] getDetailedEvents() {
		DetailedEvent[] ret = new DetailedEvent[this.rse.length];
		for (int i = 0; i < this.rse.length; i++)
			ret[i] = getDetailedEvent(i);
		return ret;
	}

	private DetailedEvent makeDetailedEvent(int i) {
		SimpleReflectogramEvent ev = this.rse[i];
		double y0 = this.mt.getY(ev.getBegin());
		double y1 = this.mt.getY(ev.getEnd());
		// если слева лин. событие хотя бы из 5 точек,
		// то альтернативным y0 будет экстраполированное значение по точке
		// непосредственно слева началом события;
		// иначе альтернативное y0 совпадает с y0
		double y0alt = i > 0 && isEventLinear(i - 1)
					&& this.rse[i - 1].getEnd() - this.rse[i - 1].getBegin() > 5
				? this.mt.getY(ev.getBegin() - 1) - linearTangent(i - 1) * 1.0
				: y0;
		switch(ev.getEventType()) {
		case SimpleReflectogramEvent.LINEAR:
			return new LinearDetailedEvent(ev,
					y0 - this.cinfo.getYTop(),
					y1 - this.cinfo.getYTop(),
					this.cinfo.getRmsDev(i),
					this.cinfo.getMaxDev(i),
					ReflectogramMath.getRmsLoss(ev, this.mt));
		case SimpleReflectogramEvent.GAIN:
			// fall through
		case SimpleReflectogramEvent.LOSS:
			return new SpliceDetailedEvent(ev,
					y0 - this.cinfo.getYTop(),
					y1 - this.cinfo.getYTop(),
					y0 - y1 - getAddToMLoss(i, true, true));
		case SimpleReflectogramEvent.NOTIDENTIFIED:
			return new NotIdentifiedDetailedEvent(ev,
					y0 - this.cinfo.getYTop(),
					y1 - this.cinfo.getYTop(),
					ReflectogramMath.getYMin(ev, this.mt) - this.cinfo.getYTop(),
					ReflectogramMath.getYMax(ev, this.mt) - this.cinfo.getYTop(),
					this.cinfo.getMaxDev(i),
					y0 - y1);
		case SimpleReflectogramEvent.DEADZONE:
			return new DeadZoneDetailedEvent(ev,
					ReflectogramMath.getPo(this.rse, i, this.mt) - this.cinfo.getYTop(),
					y1 - this.cinfo.getYTop(),
					this.cinfo.getEdz(i),
					this.cinfo.getAdz(i));
		case SimpleReflectogramEvent.ENDOFTRACE:
			return new EndOfTraceDetailedEvent(ev,
					y0alt - this.cinfo.getYTop(),
					ReflectogramMath.getYMax(ev, this.mt) - this.cinfo.getYTop());
		case SimpleReflectogramEvent.CONNECTOR:
			return new ConnectorDetailedEvent(ev,
					y0alt - this.cinfo.getYTop(),
					y1 - this.cinfo.getYTop(),
					ReflectogramMath.getYMax(ev, this.mt) - this.cinfo.getYTop(),
					y0alt - y1 - getAddToMLoss(i, true, false));
		default:
			// FIXME: error processing: this seem to may occur even when
			// receiving invalid eventType from server.
			// I guess InternalError is not a good behaviour for invalid eventType
			// received from server.
			throw new InternalError("Unexpected eventType");
		}
	}

	/**
	 * protected because hopes that caller will not modify the array returned
	 * @return internal array of reliability events.
	 */
	protected ReliabilitySimpleReflectogramEventImpl[] getRSE()
	{
		return this.rse;
	}
	protected ModelFunction getMF()
	{
		return this.mf;
	}
	protected int getTraceLength()
	{
		return this.traceLength;
	}

	private int calcTraceLength()
	{
		if (getRSE().length == 0)
			return 0;
		return getRSE()[getRSE().length - 1].getEnd() + 1;
	}

	/**
	 * согласно контракту {@link ReliabilityModelTraceAndEvents}
	 * возвращает массив {@link ReliabilitySimpleReflectogramEvent}
	 */
	public ReliabilitySimpleReflectogramEvent[] getSimpleEvents()
	{
		// Copy an array and all its references to protect se array.
		// Array elements are unmodifiable, so no need to clone them.
		return getRSE().clone();
	}

//	public ComplexReflectogramEvent[] getComplexEvents()
//	{
//		return ComplexReflectogramEvent.createEvents(getRSE(), mt);
//	}

	/**
	 * Возвращает номер события, соответствующего данному
	 * иксу. Если x попадает на границу двух событий,
	 * то выбор левого или правого зависит от реализации.
	 * Если x не попадает ни на одно событие,
	 * то возвращает -1.
	 * <p> Относительно медленный метод и не очень удобный. 
	 */
	public int getEventByCoord(int x)
	{
		int ret = -1;
		for (int i = 0; i < getRSE().length; i++)
		{
			if (x >= getRSE()[i].getBegin() && x <= getRSE()[i].getEnd())
				ret = i;
		}
		return ret;
	}

	public int getNEvents()
	{
		return getRSE().length;
	}

	/**
	 * согласно контракту {@link ReliabilityModelTraceAndEvents}
	 * возвращает {@link ReliabilitySimpleReflectogramEvent}
	 */
	public ReliabilitySimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return getRSE()[nEvent];
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		dos.writeLong(SIGNATURE_MTAE);
		int pos1 = dos.size();
		this.mf.writeToDOS(dos);
		dos.writeDouble(getDeltaX());
		int pos2 = dos.size();
		ReliabilitySimpleReflectogramEventImpl.writeArrayToDOS(this.rse, dos);
		int pos3 = dos.size();
		this.cinfo.writeToDOS(dos);
		int pos4 = dos.size();
		Log.debugMessage("MTAEI: writeToDOS:"
				+ " MT " + (pos2-pos1)     // 66-72% of total
				+ ", rse " + (pos3-pos2)   // 15-17% of total
				+ ", cinfo " + (pos4-pos3) // 13-17% of total
				+ ", total " + (pos4-pos1),
				FINEST);
	}

	public static DataStreamable.Reader getReader()
	{
		if (dsReader == null)
			dsReader = new DataStreamable.Reader() {
			public DataStreamable readFromDIS(DataInputStream dis)
			throws IOException, SignatureMismatchException
			{
				long signature = dis.readLong();
				if (signature != SIGNATURE_MTAE)
					throw new SignatureMismatchException();
				ModelFunction mf = ModelFunction.createFromDIS(dis);
				double deltaX = dis.readDouble();
				ReliabilitySimpleReflectogramEventImpl[] se =
					ReliabilitySimpleReflectogramEventImpl.readArrayFromDIS(dis);
				ModelTraceAndEventsImpl mtae =
					new ModelTraceAndEventsImpl(se, mf, deltaX);
				mtae.cinfo = mtae.new ComplexInfo(dis);
				return mtae;
			}
		};
		return dsReader;
	}
}
