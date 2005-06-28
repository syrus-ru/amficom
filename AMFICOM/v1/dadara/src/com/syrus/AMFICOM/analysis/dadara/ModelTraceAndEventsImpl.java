/*-
 * $Id: ModelTraceAndEventsImpl.java,v 1.15 2005/06/28 15:06:10 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;


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

/**
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2005/06/28 15:06:10 $
 * @module
 */
public class ModelTraceAndEventsImpl
implements ReliabilityModelTraceAndEvents, DataStreamable {
	protected static final long SIGNATURE_EVENTS = 3353520050119193102L;
	protected static final double CINFO_DEVIATION_PREC = 1e-4;

	protected ReliabilitySimpleReflectogramEventImpl[] rse; // not null
    //private ComplexReflectogramEvent[] ce; // auto-generated, not null
	private ModelFunction mf; // not null
	private int traceLength;
	private double deltaX;
	protected ModelTrace mt; // will just contain mt
    protected ComplexInfo cinfo;

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
        this.setTraceLength(calcTraceLength());
        mt = new ModelTraceImplMF(this.getMF(), this.getTraceLength());
    }

	public ModelTraceAndEventsImpl(ReliabilitySimpleReflectogramEventImpl[] rse,
			ModelFunction mf, double[] y, double deltaX)
	{
        this(rse, mf, deltaX);
        cinfo = new ComplexInfo(y); // use all our internal fields initialized by this moment
	}

    public static ModelTraceAndEventsImpl replaceRSE(
            ModelTraceAndEventsImpl that,
            ReliabilitySimpleReflectogramEventImpl[] rse,
            double[] y)
    {
        return new ModelTraceAndEventsImpl(rse, that.mf, y, that.deltaX);
    }
            

	public double getDeltaX()
	{
		return deltaX;
	}
	public ModelTrace getModelTrace()
	{
		return mt;
	}

    /**
     * —обирает из y[] информацию необходимую дл€ DetailedEvent,
     * которой нет ни в rse[], ни в mf.
     * Ёту информацию можно будет компактно запомнить в поток,
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
            return rse[nEvent].getEventType() ==
                SimpleReflectogramEvent.DEADZONE;
        }
        protected boolean eventNeedsMaxDev(int nEvent) {
            return
               rse[nEvent].getEventType() == SimpleReflectogramEvent.LINEAR
            || rse[nEvent].getEventType() == SimpleReflectogramEvent.NOTIDENTIFIED
            || rse[nEvent].getEventType() == SimpleReflectogramEvent.GAIN // FIXME: gain/loss do not need these pars
            || rse[nEvent].getEventType() == SimpleReflectogramEvent.LOSS;
        }
        protected int getEdz(int i) {
            return edz[i];
        }
        protected int getAdz(int i) {
            return adz[i];
        }
        protected double getMaxDev(int i) {
            return maxDevI[i] * CINFO_DEVIATION_PREC;
        }
        protected double getRmsDev(int i) {
            return rmsDevI[i] * CINFO_DEVIATION_PREC;
        }

        public double getYTop() {
            return yTop;
        }
        private void allocateArrays() {
            edz = new int[rse.length];
            adz = new int[rse.length];
            maxDevI = new int[rse.length];
            rmsDevI = new int[rse.length];
        }

        public ComplexInfo(double[] y) {
            allocateArrays();
            yTop = ReflectogramMath.getArrayMax(y);
            for (int i = 0; i < rse.length; i++)
            {
                edz[i] = 0;
                adz[i] = 0;
                rmsDevI[i] = 0;
                maxDevI[i] = 0;
                if (eventNeedsEdzAdzPo(i)) {
                    double po = ReflectogramMath.getPo(rse, i, mt);
                    int[] res = ReflectogramMath.getEdzAdz(po, rse[i], mt);
                    edz[i] = res[0];
                    adz[i] = res[1];
                }
                if (eventNeedsMaxDev(i)) {
                	// округл€ем вверх
                    maxDevI[i] = (int)Math.ceil(
                    		ReflectogramMath.getMaxDev(y, rse[i], mt)
                    			/ CINFO_DEVIATION_PREC);
                    rmsDevI[i] = (int)Math.ceil(
                    		ReflectogramMath.getRmsDev(y, rse[i], mt)
                    			/ CINFO_DEVIATION_PREC);
                }
            }
        }

        protected ComplexInfo(DataInputStream dis) throws IOException {
            yTop = dis.readDouble();
            allocateArrays();
            for (int i = 0; i < rse.length; i++) {
                edz[i] = 0;
                adz[i] = 0;
                rmsDevI[i] = 0;
                maxDevI[i] = 0;
                if (eventNeedsEdzAdzPo(i)) {
                    edz[i] = dis.readInt();
                    adz[i] = dis.readInt();
                }
                if (eventNeedsMaxDev(i)) {
                    maxDevI[i] = dis.readInt();
                    rmsDevI[i] = dis.readInt();
                }
            }
        }

        public void writeToDOS(DataOutputStream dos) throws IOException {
            dos.writeDouble(yTop);
            for (int i = 0; i < rse.length; i++) {
                if (eventNeedsEdzAdzPo(i)) {
                	dos.writeInt(edz[i]);
                	dos.writeInt(adz[i]);
                }
                if (eventNeedsMaxDev(i)) {
                	dos.writeInt(maxDevI[i]);
                	dos.writeInt(rmsDevI[i]);
                }
            }
        }
    }

    private int eventLength(int i) {
        return rse[i].getEnd() - rse[i].getBegin();
    }
    private double linearTangent(int i) {
        int begin = rse[i].getBegin();
        int end = rse[i].getEnd();
        return (mt.getY(begin) - mt.getY(end)) / (end - begin);
    }

    private double getAddToMLoss(int i, boolean useLeft, boolean useRight) {
        // берем средний (из одного или двух) наклон смежных линейных
        // событий, которые по длине больше текущего событи€
        int linCount = 0;
        double linAtt = 0;
        if (useLeft && i > 0
                && eventLength(i - 1) > eventLength(i))
        {
            linCount++;
            linAtt += linearTangent(i - 1);
        }
        if (useRight && i < rse.length - 1
                && eventLength(i + 1) > eventLength(i))
        {
            linCount++;
            linAtt += linearTangent(i + 1);
        }
        if (linCount > 0)
            linAtt /= linCount;
        return linAtt * eventLength(i);
    }

    private DetailedEvent getDetailedEvent(int i) {
        SimpleReflectogramEvent ev = rse[i];
        double y0 = mt.getY(ev.getBegin());
        double y1 = mt.getY(ev.getEnd());
        switch(ev.getEventType()) {
        case SimpleReflectogramEvent.LINEAR:
            return new LinearDetailedEvent(ev,
                    y0 - cinfo.getYTop(),
                    y1 - cinfo.getYTop(),
                    cinfo.getRmsDev(i),
                    cinfo.getMaxDev(i));
        case SimpleReflectogramEvent.GAIN:
            // fall through
        case SimpleReflectogramEvent.LOSS:
            return new SpliceDetailedEvent(ev,
                    y0 - cinfo.getYTop(),
                    y1 - cinfo.getYTop(),
                    y0 - y1 - getAddToMLoss(i, true, true));
        case SimpleReflectogramEvent.NOTIDENTIFIED:
            return new NotIdentifiedDetailedEvent(ev,
                    y0 - cinfo.getYTop(),
                    y1 - cinfo.getYTop(),
                    ReflectogramMath.getYMin(ev, mt) - cinfo.getYTop(),
                    ReflectogramMath.getYMax(ev, mt) - cinfo.getYTop(),
                    cinfo.getMaxDev(i),
                    y0 - y1);
        case SimpleReflectogramEvent.DEADZONE:
            return new DeadZoneDetailedEvent(ev,
                    ReflectogramMath.getPo(rse, i, mt) - cinfo.getYTop(),
                    y1 - cinfo.getYTop(),
                    cinfo.getEdz(i),
                    cinfo.getAdz(i));
        case SimpleReflectogramEvent.ENDOFTRACE:
            return new EndOfTraceDetailedEvent(ev,
                    y0 - cinfo.getYTop(),
                    ReflectogramMath.getYMax(ev, mt) - cinfo.getYTop());
        case SimpleReflectogramEvent.CONNECTOR:
            return new ConnectorDetailedEvent(ev,
                    y0 - cinfo.getYTop(),
                    y1 - cinfo.getYTop(),
                    ReflectogramMath.getYMax(ev, mt) - cinfo.getYTop(),
                    y0 - y1 - getAddToMLoss(i, true, false));
        default:
            // FIXME: error processing: this seem to may occur even when
            // receiving invalid eventType from server.
            // I guess InternalError is not a good behaviour for invalid eventType
            // received from server.
            throw new InternalError("Unexpected eventType");
        }
    }

    public DetailedEvent[] getDetailedEvents() {
        // @todo: add caching (maybe event a constructor-time pre-computation)
        DetailedEvent[] ret = new DetailedEvent[rse.length];
        for (int i = 0; i < rse.length; i++)
            ret[i] = getDetailedEvent(i);
        return ret;
    }

    /**
     * protected because hopes that caller will not modify the array returned
     * @return internal array of reliability events.
     */
    protected ReliabilitySimpleReflectogramEventImpl[] getRSE()
    {
        return rse;
    }
	protected ModelFunction getMF()
	{
		return mf;
	}
	protected int getTraceLength()
	{
		return traceLength;
	}

	private int calcTraceLength()
	{
		if (getRSE().length == 0)
			return 0;
		else
			return getRSE()[getRSE().length - 1].getEnd() + 1;
	}
	private void setTraceLength(int traceLength)
	{
		this.traceLength = traceLength;
	}

    /**
     * согласно контракту {@link ReliabilityModelTraceAndEvents}
     * возвращает массив {@link ReliabilitySimpleReflectogramEvent}
     */
	public SimpleReflectogramEvent[] getSimpleEvents()
	{
        // Copy an array and all its references to protect se array.
        // Array elements are unmodifiable, so no need to clone them.
        return (ReliabilitySimpleReflectogramEvent[] )getRSE().clone();
	}

//	public ComplexReflectogramEvent[] getComplexEvents()
//	{
//		return ComplexReflectogramEvent.createEvents(getRSE(), mt);
//	}

	/**
	 * ¬озвращает номер событи€, соответствующего данному
	 * иксу. ≈сли x попадает на границу двух событий,
	 * то выбор левого или правого зависит от реализации.
	 * ≈сли x не попадает ни на одно событие,
	 * то возвращает -1.
	 * <p> ќтносительно медленный метод и не очень удобный. 
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
	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return getRSE()[nEvent];
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		int pos1 = dos.size();
		dos.writeLong(SIGNATURE_EVENTS);
		getMF().writeToDOS(dos);
		dos.writeDouble(getDeltaX());
		int pos2 = dos.size();
		ReliabilitySimpleReflectogramEventImpl.writeArrayToDOS(rse, dos);
		int pos3 = dos.size();
        cinfo.writeToDOS(dos);
		int pos4 = dos.size();
		System.out.println("MTAEI: writeToDOS:"
				+ " MT " + (pos2-pos1)     // 62-68% of total
				+ ", rse " + (pos3-pos2)   // 14-16% of total
				+ ", cinfo " + (pos4-pos3) // 18-22% of total
				+ ", total " + (pos4-pos1));
	}

	public static DataStreamable.Reader getReader()
	{
		if (dsReader == null)
			dsReader = new DataStreamable.Reader() {
            public DataStreamable readFromDIS(DataInputStream dis)
            throws IOException, SignatureMismatchException
            {
                long signature = dis.readLong();
                if (signature != SIGNATURE_EVENTS)
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
