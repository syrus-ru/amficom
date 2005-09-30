/*-
 * $Id: SOAnchor.java,v 1.7 2005/09/30 12:56:21 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * Константный идентификатор для привязки к StorableObject,
 * не зависящий от StorableObject Framework,
 * пригодный для сохранения в объектах dadara.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.7 $, $Date: 2005/09/30 12:56:21 $
 * @module
 */
public class SOAnchor {
	private static final long voidCode = 0x7FFF000000000000L; // FIXME: 'untied' value for SOAnchor
	public static final SOAnchor VOID_ANCHOR = new SOAnchor(voidCode);
	private static final byte SIGNATURE_BYTE_0 = 0; // format version number

	private long value;

	public SOAnchor(long value) {
		this.value = value;
	}
	public static SOAnchor createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readByte() != SIGNATURE_BYTE_0) {
			throw new SignatureMismatchException();
		}
		return new SOAnchor(dis.readLong());
	}

	public long getValue() {
		return this.value;
	}
	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeByte(SIGNATURE_BYTE_0);
		dos.writeLong(this.value);
	}
	@Override
	public String toString() {
		return "[" + this.value + "]";
	}
}
