package com.syrus.util;

import java.io.*;
import java.util.*;

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

  public ByteArray(int a) throws IOException {
    this(toByteArray(a));
  }

  public ByteArray(long l) throws IOException {
    this(toByteArray(l));
  }

  public ByteArray(double d) throws IOException {
    this(toByteArray(d));
  }

  public ByteArray(double[] dar) throws IOException {
    this(toByteArray(dar));
  }

  public ByteArray(String s) throws IOException {
    this(toByteArray(s));
  }

	public ByteArray(String[] sar) throws IOException {
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
			System.out.println("Exception while converting byte array to int array: " + e.getMessage() + ", length of byte array == " + this.bar.length);
		}
		Integer[] ii = new Integer[linkedlist.size()];
		ii = (Integer[])linkedlist.toArray(ii);
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
	  	System.out.println("Exception while converting byte array to double array: " + e.getMessage() + ", length of byte array == " + this.bar.length);
      e.printStackTrace();
 		}
  	Double[] d = new Double[linkedlist.size()];
	  d = (Double[])linkedlist.toArray(d);
 		double[] dar = new double[d.length];
  	for(int i = 0; i < d.length; i++)
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
		} catch (Exception e) {
			System.out.println("Exception while converting byte array to string array: " + e.getMessage() + ", length of byte array == " + this.bar.length);
			e.printStackTrace();
		}
		return (String[]) c.toArray(new String[c.size()]);
	}

  public void concat(ByteArray bArr) {
    byte[] bar1 = bArr.getBytes();
    int len = this.bar.length + bar1.length;
    byte[] barn = new byte[len];
    for(int i = 0; i < this.bar.length; i++)
      barn[i] = this.bar[i];
    for(int i = this.bar.length; i < len; i++)
      barn[i] = bar1[i - this.bar.length];
    this.bar = barn;
  }

  public ByteArray getInverse() {
    byte[] bar1 = new byte[this.bar.length];
    for (int i = 0; i < bar1.length; i++)
      bar1[i] = this.bar[bar1.length - 1 - i];
    return new ByteArray(bar1);
  }

  public static byte[] toByteArray(int a) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(a);
    return baos.toByteArray();
  }

  public static byte[] toByteArray(long l) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeLong(l);
    return baos.toByteArray();
  }

  public static byte[] toByteArray(double d) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeDouble(d);
    return baos.toByteArray();
  }

  public static byte[] toByteArray(double[] dar) throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    for (int i = 0; i < dar.length; i++)
      dos.writeDouble(dar[i]);
    return baos.toByteArray();
  }

  public static byte[] toByteArray(String s) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(s);
//    dos.write((byte)\u0000);
    return baos.toByteArray();
  }

	public static byte[] toByteArray(String[] sar) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
		for (int i = 0; i < sar.length; i++)
			dos.writeUTF(sar[i]);
//    dos.write((byte)\u0000);
    return baos.toByteArray();
  }
}
