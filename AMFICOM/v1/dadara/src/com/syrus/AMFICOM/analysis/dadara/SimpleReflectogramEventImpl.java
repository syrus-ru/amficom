/*
 * $Id: SimpleReflectogramEventImpl.java,v 1.15 2005/10/24 10:13:36 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * Just am implementation of SimpleReflectogramEvent
 * with protected-visibility streaming support.
 * <p>
 * Object state can modifiable only within protected visibility
 * (via protected method {@link #readArrayBaseFromDIS}), so that
 * children can ensure unmodifiable behaviour.
 * <p>
 * <b>NB:</b> Support two *different* ways of streaming:
 * <ul>
 * <li> one-event (see {@link #writeBaseToDOS}, no compression, no signatures)
 * <li> array-of-events ({@link #writeArrayBaseToDOS}, uses both signature
 *   and compression).
 * </ul>
 * <p>
 * Today, both ways are coded within protected visibility because
 * there is no need of streaming this (super-)class itself by now.
 * 
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2005/10/24 10:13:36 $
 * @module
 */
public class SimpleReflectogramEventImpl
implements SimpleReflectogramEvent {
	private static final short SIGNATURE_SHORT_ARRAY = 12500;

	private int begin;
	private int end;
	private int eventType;

	protected SimpleReflectogramEventImpl()
	{ // for native use and dis reading
	}

	public SimpleReflectogramEventImpl(int begin, int end, int eventType)
	{
		this.begin = begin;
		this.end = end;
		this.eventType = eventType;
	}

	/**
	 * copy-constructor
	 */
	public SimpleReflectogramEventImpl(SimpleReflectogramEvent that) {
		this.begin = that.getBegin();
		this.end = that.getEnd();
		this.eventType = that.getEventType();
	}

	public int getBegin()
	{
		return this.begin;
	}

	public int getEnd()
	{
		return this.end;
	}

	public int getEventType()
	{
		return this.eventType;
	}

	/**
	 * writes to stream base object state
	 */
	private void writeBaseToDOS(DataOutputStream dos)
	throws IOException
	{
		dos.writeInt(this.begin);
		dos.writeInt(this.end);
		dos.writeInt(this.eventType);
	}

	/**
	 * reads from stream base object state
	 */
	private void readBaseFromDIS(DataInputStream dis)
	throws IOException
	{
		this.begin = dis.readInt();
		this.end = dis.readInt();
		this.eventType = dis.readInt();
	}

	/**
	 * writes to stream base state of objects of an array
	 * (assumes the invoker will store array length prior to this call).
	 * Uses compression.
	 */
	protected static void writeArrayBaseToDOS(
			SimpleReflectogramEventImpl[] se,
			DataOutputStream dos) throws IOException {
		dos.writeShort(SIGNATURE_SHORT_ARRAY);
		for (int i = 0; i < se.length; i++) {
			if (i > 0 && se[i].begin == se[i - 1].end
					&& (se[i].eventType & ~0xff) == 0) {
				int delta = se[i].end - se[i].begin;
				if (delta < 0 || delta > 0xffff) {
					dos.writeByte(0x1);
					dos.writeInt(se[i].end);
				} else if (delta > 0xff) {
					dos.writeByte(0x2);
					dos.writeShort(delta);
				} else { // delta < 0xff, delta >= 0
					dos.writeByte(0x3);
					dos.writeByte(delta);
				}
				dos.writeByte(se[i].eventType);
			} else {
				dos.writeByte(0x0);
				se[i].writeBaseToDOS(dos);
			}
		}
	}

	/**
	 * reads from stream base state of objects of a pre-allocated array.
	 * Uses decompression.
	 * @throws SignatureMismatchException
	 * @throws IOException
	 */
	protected static void readArrayBaseFromDIS(
			SimpleReflectogramEventImpl[] se,
			DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readShort() != SIGNATURE_SHORT_ARRAY) {
			throw new SignatureMismatchException();
		}
		for (int i = 0; i < se.length; i++) {
			int type = dis.readByte();
			if (type == 0) {
				se[i].readBaseFromDIS(dis);
			} else {
				se[i].begin = se[i - 1].end;
				if (type == 0x1) {
					se[i].end = dis.readInt();
				} else if (type == 0x2) {
					se[i].end = se[i].begin + (dis.readShort() & 0xffff);
				} else if (type == 0x3) {
					se[i].end = se[i].begin + (dis.readByte() & 0xff);
				} else {
					throw new SignatureMismatchException("Unexpected code");
				}
				se[i].eventType = dis.readByte() & 0xff;
			}
		}
	}
}
