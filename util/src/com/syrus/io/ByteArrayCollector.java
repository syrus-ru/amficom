/*
 * $Id: ByteArrayCollector.java,v 1.4 2005/03/16 16:29:26 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class ByteArrayCollector {
	ByteArrayOutputStream baos;
	ByteArrayInputStream bais;
	DataOutputStream dos;
	DataInputStream dis;

	public ByteArrayCollector() {
		this.baos = new ByteArrayOutputStream();
		this.dos = new DataOutputStream(this.baos);
	}

	public void add(byte[] b) {
		try {
			this.dos.writeInt(b.length);
			this.dos.write(b);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public byte[] encode() {
		try {
			this.dos.flush();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		return this.baos.toByteArray();
	}

	public byte[][] decode(byte[] b) {
		this.bais = new ByteArrayInputStream(b);
		this.dis = new DataInputStream(this.bais);

		int size = 0;

		try {
			while (this.dis.available() != 0) {
				int s = this.dis.readInt();
				this.dis.skipBytes(s);
				size++;
			}
		}
		catch (EOFException eof) {
			// empty
			eof.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		byte[][] data = new byte[size][];
		try {
			this.dis.reset();
			for (int i = 0; i < size; i++) {
				int s = this.dis.readInt();
				data[i] = new byte[s];
				this.dis.read(data[i]);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
}
