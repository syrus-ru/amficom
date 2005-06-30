/*-
 * $Id: SOAnchor.java,v 1.3 2005/06/30 15:42:36 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.dadara.DataStreamable;
import com.syrus.AMFICOM.analysis.dadara.SignatureMismatchException;

/**
 * ����������� ������������� ��� �������� � StorableObject,
 * �� ��������� �� StorableObject Framework,
 * ��������� ��� ���������� � �������� dadara.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.3 $, $Date: 2005/06/30 15:42:36 $
 * @module
 */
public class SOAnchor implements DataStreamable {
	private static DataStreamable.Reader reader = null;
	private static final long voidCode = 0x7FFF000000000000L; // FIXME: 'untied' value for SOAnchor
	public static final SOAnchor VOID_ANCHOR = new SOAnchor(voidCode);

	private long value;

	public SOAnchor(long value) {
		this.value = value;
	}
	public SOAnchor(DataInputStream dis) throws IOException {
		this(dis.readLong());
	}

	public long getValue() {
		return value;
	}
	public void writeToDOS(DataOutputStream dos) throws IOException {
		dos.writeLong(value);
	}
	public static DataStreamable.Reader getDSReader () {
		if (reader == null) {
			reader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
				throws IOException, SignatureMismatchException {
					return new SOAnchor(dis);
				}
			};
		}
		return reader;
	}
	public String toString() {
		return "[" + value + "]";
	}
}
