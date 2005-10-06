/*-
 * $Id: DetailedEvent.java,v 1.6 2005/10/06 13:34:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.dadara.DataStreamable;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.io.SignatureMismatchException;

/**
 * Представляет максимальный набор информации по событию, кроме информации
 * о достоверности. Это начало, конец, тип и специфическая информация.
 * 
 * Все абс. уровни производных классов отсчитываются от максимума,
 * и имеют отрицательные значения.
 * Все отн. величины имеют знаки, соответствующие их названию. 
 * {@link #begin}, {@link #end} {@link #eventType} - см.
 * описание {@link SimpleReflectogramEvent}
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public abstract class DetailedEvent
implements SimpleReflectogramEvent, DataStreamable {
	private static DataStreamable.Reader dsReader = null;
	private static final short SIGNATURE_SHORT = 21375;

	protected int begin;
	protected int end;
	protected int eventType;

	protected DetailedEvent(SimpleReflectogramEvent sre) {
		this.begin = sre.getBegin();
		this.end = sre.getEnd();
		this.eventType = sre.getEventType();
	}
	protected DetailedEvent() {
		// empty, for dis reading
	}

	public int getBegin() {
		return this.begin;
	}
	public int getEnd() {
		return this.end;
	}
	public int getEventType() {
		return this.eventType;
	}

	protected abstract void writeSpecificToDOS(DataOutputStream dos)
	throws IOException;

	protected abstract void readSpecificFromDIS(DataInputStream dis)
	throws IOException;//, SignatureMismatchException;

	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeShort(SIGNATURE_SHORT);
		dos.writeInt(this.begin);
		dos.writeInt(this.end);
		dos.writeInt(this.eventType);
		writeSpecificToDOS(dos);
	}

	protected static DetailedEvent factory(int eventType)
	throws SignatureMismatchException {
		switch(eventType) {
		case SimpleReflectogramEvent.LINEAR: return new LinearDetailedEvent();
		case SimpleReflectogramEvent.GAIN: return new SpliceDetailedEvent();
		case SimpleReflectogramEvent.LOSS: return new SpliceDetailedEvent();
		case SimpleReflectogramEvent.DEADZONE: return new DeadZoneDetailedEvent();
		case SimpleReflectogramEvent.ENDOFTRACE: return new EndOfTraceDetailedEvent();
		case SimpleReflectogramEvent.CONNECTOR: return new ConnectorDetailedEvent();
		case SimpleReflectogramEvent.NOTIDENTIFIED: return new NotIdentifiedDetailedEvent();
		default: throw new SignatureMismatchException("unknown eventType");
		}
	}

	public static DataStreamable.Reader getReader() {
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					if (dis.readShort() != SIGNATURE_SHORT) {
						throw new SignatureMismatchException();
					}
					int begin = dis.readInt();
					int end = dis.readInt();
					int eventType = dis.readInt();
					DetailedEvent ev = factory(eventType);
					ev.begin = begin;
					ev.end = end;
					ev.eventType = eventType;
					ev.readSpecificFromDIS(dis);
					return ev;
				}
			};
		}
		return dsReader;
	}}
