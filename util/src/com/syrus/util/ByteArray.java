/*
 * $Id: ByteArray.java,v 1.16 2006/03/01 10:38:29 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.16 $, $Date: 2006/03/01 10:38:29 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class ByteArray {
	private byte[] bar = null;

	public ByteArray() {
		this.bar = new byte[0];
	}

	public ByteArray(final byte[] bar) {
		this.bar = bar;
	}

	public ByteArray(final byte b) {
		this.bar = new byte[1];
		this.bar[0] = b;
	}

	public ByteArray(final int a) {
		this(toByteArray(a));
	}

	public ByteArray(final long l) {
		this(toByteArray(l));
	}

	public ByteArray(final Date date) {
		this(toByteArray(date));
	}

	public ByteArray(final double d) {
		this(toByteArray(d));
	}

	public ByteArray(final double[] dar) {
		this(toByteArray(dar));
	}

	public ByteArray(final boolean b) {
		this(toByteArray(b));
	}

	public ByteArray(final String s) {
		this(toByteArray(s));
	}

	public ByteArray(final String[] sar) {
		this(toByteArray(sar));
	}

	public byte[] getBytes() {
		return this.bar;
	}

	public int getLength() {
		return this.bar.length;
	}

	public int toInt() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		return dis.readInt();
	}

	public int[] toIntArray() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		final List<Integer> list = new LinkedList<Integer>();
		try {
			while (bais.available() > 0) {
				list.add(new Integer(dis.readInt()));
			}
		} catch (Exception e) {
			System.err.println("Exception while converting byte array to int array: " + e.getMessage()
					+ ", length of byte array == " + this.bar.length);
		}
		final Integer[] ii = list.toArray(new Integer[list.size()]);
		final int[] iar = new int[ii.length];
		for (int i = 0; i < ii.length; i++) {
			iar[i] = ii[i].intValue();
		}
		return iar;
	}

	public long toLong() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		return dis.readLong();
	}

	public Date toDate() throws IOException {
		return new Date(this.toLong());
	}

	public double toDouble() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		return dis.readDouble();
	}

	public double[] toDoubleArray() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		final List<Double> list = new LinkedList<Double>();
		try {
			while (bais.available() > 0) {
				list.add(new Double(dis.readDouble()));
			}
		} catch (Exception e) {
			System.err.println("Exception while converting byte array to double array: " + e.getMessage()
					+ ", length of byte array == " + this.bar.length);
			e.printStackTrace();
		}
		final Double[] dd = list.toArray(new Double[list.size()]);
		final double[] dar = new double[dd.length];
		for (int i = 0; i < dd.length; i++) {
			dar[i] = dd[i].doubleValue();
		}
		return dar;
	}

	public boolean toBoolean() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		return dis.readBoolean();
	}

	public String toUTFString() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		return dis.readUTF();
	}

	public String[] toUTFStringArray() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		final DataInputStream dis = new DataInputStream(bais);
		final List<String> list = new LinkedList<String>();
		try {
			while (bais.available() > 0) {
				list.add(dis.readUTF());
			}
		} catch (Exception ioe) {
			System.err.println("Exception while converting byte array to string array: " + ioe.getMessage()
					+ ", length of byte array == " + this.bar.length);
			ioe.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}

	public void concat(final ByteArray bArr) {
		final byte[] bar1 = bArr.getBytes();
		final int len = this.bar.length + bar1.length;
		final byte[] barn = new byte[len];
		for (int i = 0; i < this.bar.length; i++) {
			barn[i] = this.bar[i];
		}
		for (int i = this.bar.length; i < len; i++) {
			barn[i] = bar1[i - this.bar.length];
		}
		this.bar = barn;
	}

	public ByteArray getInverse() {
		final byte[] bar1 = new byte[this.bar.length];
		for (int i = 0; i < bar1.length; i++) {
			bar1[i] = this.bar[bar1.length - 1 - i];
		}
		return new ByteArray(bar1);
	}

	public static byte[] toByteArray(final int a) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(a);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting int to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final long l) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeLong(l);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting long to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final Date date) {
		return toByteArray(date.getTime());
	}

  public static byte[] toByteArray(final double d) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeDouble(d);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting double to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final double[] dar) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < dar.length; i++) {
				dos.writeDouble(dar[i]);
			}
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting double array to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final boolean b) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeBoolean(b);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting boolean to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final String s) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF(s);
			// dos.write((byte)\u0000);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting string to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(final String[] sar) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < sar.length; i++) {
				dos.writeUTF(sar[i]);
			}
			// dos.write((byte)\u0000);
		} catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting string array to byte array -- " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}
}
