/*-
 * $Id: SOAnchorImpl.java,v 1.1 2005/10/06 14:36:05 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * ����������� ������������� ��� �������� � StorableObject,
 * �� ��������� �� StorableObject Framework,
 * ��������� ��� ���������� � �������� dadara.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/10/06 14:36:05 $
 * @module
 */
public class SOAnchorImpl implements SOAnchor {
	private static final long voidCode = 0x7FFF000000000000L; // FIXME: 'untied' value for SOAnchor
	public static final SOAnchorImpl VOID_ANCHOR = new SOAnchorImpl(voidCode);
	private static final byte SIGNATURE_BYTE_0 = 0; // format version number

	private long value;

	public SOAnchorImpl(long value) {
		this.value = value;
	}
	public static SOAnchorImpl createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readByte() != SIGNATURE_BYTE_0) {
			throw new SignatureMismatchException();
		}
		return new SOAnchorImpl(dis.readLong());
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
