/*
 * $Id: ByteArray.java,v 1.10 2005/03/17 10:12:50 bass Exp $
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
import java.util.Collection;
import java.util.LinkedList;

public class ByteArray {
  private byte[] bar = null;

	public ByteArray() {
		this.bar = new byte[0];
	}

	public ByteArray(byte[] bar) {
		this.bar = bar;
	}

	public ByteArray(byte b) {
		this.bar = new byte[1];
		this.bar[0] = b;
	}

  public ByteArray(int a) {
		this(toByteArray(a));
	}

	public ByteArray(long l) {
		this(toByteArray(l));
	}

	public ByteArray(double d) {
		this(toByteArray(d));
	}

	public ByteArray(double[] dar) {
		this(toByteArray(dar));
	}

	public ByteArray(String s) {
		this(toByteArray(s));
	}

	public ByteArray(String[] sar) {
		this(toByteArray(sar));
	}

	public byte[] getBytes() {
		return this.bar;
	}

	public int getLength() {
		return this.bar.length;
	}

	public int toInt() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		return dis.readInt();
	}

	public int[] toIntArray() {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		LinkedList linkedlist = new LinkedList();
		try {
			while (bais.available() > 0)
				linkedlist.add(new Integer(dis.readInt()));
		}
		catch (Exception e) {
			System.err.println("Exception while converting byte array to int array: " + e.getMessage() //$NON-NLS-1$
					+ ", length of byte array == " + this.bar.length); //$NON-NLS-1$
		}
		Integer[] ii = new Integer[linkedlist.size()];
		ii = (Integer[]) linkedlist.toArray(ii);
		int[] iar = new int[ii.length];
		for (int i = 0; i < ii.length; i++)
			iar[i] = ii[i].intValue();
		return iar;
	}

	public long toLong() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		return dis.readLong();
	}

	public double toDouble() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		return dis.readDouble();
	}

  public double[] toDoubleArray() {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		LinkedList linkedlist = new LinkedList();
		try {
			while (bais.available() > 0)
				linkedlist.add(new Double(dis.readDouble()));
		}
		catch (Exception e) {
			System.err.println("Exception while converting byte array to double array: " + e.getMessage() //$NON-NLS-1$
					+ ", length of byte array == " + this.bar.length); //$NON-NLS-1$
			e.printStackTrace();
		}
		Double[] d = new Double[linkedlist.size()];
		d = (Double[]) linkedlist.toArray(d);
		double[] dar = new double[d.length];
		for (int i = 0; i < d.length; i++)
			dar[i] = d[i].doubleValue();
		return dar;
	}

  public String toUTFString() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		return dis.readUTF();
	}

	public String[] toUTFStringArray() {
		ByteArrayInputStream bais = new ByteArrayInputStream(this.bar);
		DataInputStream dis = new DataInputStream(bais);
		Collection c = new LinkedList();
		try {
			while (bais.available() > 0)
				c.add(dis.readUTF());
		}
		catch (Exception ioe) {
			System.err.println("Exception while converting byte array to string array: " + ioe.getMessage() //$NON-NLS-1$
					+ ", length of byte array == " + this.bar.length); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return (String[]) c.toArray(new String[c.size()]);
	}

  public void concat(ByteArray bArr) {
		byte[] bar1 = bArr.getBytes();
		int len = this.bar.length + bar1.length;
		byte[] barn = new byte[len];
		for (int i = 0; i < this.bar.length; i++)
			barn[i] = this.bar[i];
		for (int i = this.bar.length; i < len; i++)
			barn[i] = bar1[i - this.bar.length];
		this.bar = barn;
	}

  public ByteArray getInverse() {
		byte[] bar1 = new byte[this.bar.length];
		for (int i = 0; i < bar1.length; i++)
			bar1[i] = this.bar[bar1.length - 1 - i];
		return new ByteArray(bar1);
	}

	public static byte[] toByteArray(int a) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(a);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting int to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(long l) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeLong(l);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting long to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

  public static byte[] toByteArray(double d) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeDouble(d);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting double to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

  public static byte[] toByteArray(double[] dar) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < dar.length; i++)
				dos.writeDouble(dar[i]);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting double array to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

  public static byte[] toByteArray(String s) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF(s);
			// dos.write((byte)\u0000);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting string to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(String[] sar) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < sar.length; i++)
				dos.writeUTF(sar[i]);
			//dos.write((byte)\u0000);
		}
		catch (IOException ioe) {
			// IOExceptions are never thrown in ByteArrayOutputStream.write()
			System.err.println("Exception while converting string array to byte array -- " + ioe.getMessage()); //$NON-NLS-1$
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}
}
