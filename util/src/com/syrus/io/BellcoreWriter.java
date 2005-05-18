/*
 * $Id: BellcoreWriter.java,v 1.8 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;
import java.nio.*;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */
public class BellcoreWriter {
	byte[] data;
	ByteArrayOutputStream baos;
	IntelDataOutputStream idos;
	BellcoreStructure bs;

	public byte[] write(BellcoreStructure bs1) {
		this.bs = bs1;
		this.baos = new ByteArrayOutputStream();
		this.idos = new IntelDataOutputStream(this.baos);
		// int specials = bs.specials;
		this.writeMap();

		int i = 1;
		int j = 0;
		while (i < bs1.map.nb) {
			if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_GENPARAMS)) {
				this.writeGenParams(i++);
			}
			else
				if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_SUPPARAMS)) {
					this.writeSupParams(i++);
				}
				else
					if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_FXDPARAMS)) {
						this.writeFxdParams(i++);
					}
					else
						if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_KEYEVENTS)) {
							this.writeKeyEvents(i++);
						}
						else
							if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_LNKPARAMS)) {
								this.writeLnkParams(i++);
							}
							else
								if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_DATAPTS)) {
									this.writeDataPts(i++);
								}
								else
									if (bs1.map.bId[i].equals(BellcoreStructure.FIELD_NAME_CKSUM)) {
										this.writeCksum(i++);
									}
									else
										if (bs1.specials > 0) {
											this.writeSpecial(j++, i++);
										}
										else
											System.out.println("Unknown block!");
		}

		this.data = new byte[this.baos.size()];
		this.data = this.baos.toByteArray();
		return this.data;
	}

	private int writeMap() {
		try {
			this.idos.writeIUnsignedShort(this.bs.map.mrn);
			this.idos.writeIInt(this.bs.map.mbs);
			this.idos.writeIShort(this.bs.map.nb);

			for (int i = 1; i < this.bs.map.nb; i++) {
				this.idos.writeIString(this.bs.map.bId[i]);
				this.idos.writeIUnsignedShort(this.bs.map.bRev[i]);
				this.idos.writeIInt(this.bs.map.bSize[i]);
			}
		}
		catch (IOException e) {
			System.out.println("IO Error writing Map");
			return 0;
		}
		return 1;
	}

	private int writeGenParams(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIChar(this.bs.genParams.lc.charAt(0));
			this.idos.writeIChar(this.bs.genParams.lc.charAt(1));
			this.idos.writeIString(this.bs.genParams.cid);
			this.idos.writeIString(this.bs.genParams.fid);
			this.idos.writeIShort(this.bs.genParams.nw);
			this.idos.writeIString(this.bs.genParams.ol);
			this.idos.writeIString(this.bs.genParams.tl);
			this.idos.writeIString(this.bs.genParams.ccd);
			this.idos.writeIChar(this.bs.genParams.cdf.charAt(0));
			this.idos.writeIChar(this.bs.genParams.cdf.charAt(1));
			this.idos.writeIInt(this.bs.genParams.uo);
			this.idos.writeIString(this.bs.genParams.op);
			this.idos.writeIString(this.bs.genParams.cmt);
		}
		catch (IOException e) {
			System.out.println("IO Error writing GenParams");
			return 0;
		}
		return 1;
	}

	private int writeSupParams(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIString(this.bs.supParams.sn);
			this.idos.writeIString(this.bs.supParams.mfid);
			this.idos.writeIString(this.bs.supParams.otdr);
			this.idos.writeIString(this.bs.supParams.omid);
			this.idos.writeIString(this.bs.supParams.omsn);
			this.idos.writeIString(this.bs.supParams.sr);
			this.idos.writeIString(this.bs.supParams.ot);
		}
		catch (IOException e) {
			System.out.println("IO Error writing SupParams");
			return 0;
		}
		return 1;
	}

	private int writeFxdParams(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIUnsignedInt(this.bs.fxdParams.dts);
			this.idos.writeIChar(this.bs.fxdParams.ud.charAt(0));
			this.idos.writeIChar(this.bs.fxdParams.ud.charAt(1));
			this.idos.writeIShort(this.bs.fxdParams.aw);
			this.idos.writeIInt(this.bs.fxdParams.ao);
			this.idos.writeIShort(this.bs.fxdParams.tpw);
			for (int i = 0; i < this.bs.fxdParams.tpw; i++)
				this.idos.writeIShort(this.bs.fxdParams.pwu[i]);
			for (int i = 0; i < this.bs.fxdParams.tpw; i++)
				this.idos.writeIInt(this.bs.fxdParams.ds[i]);
			for (int i = 0; i < this.bs.fxdParams.tpw; i++)
				this.idos.writeIInt(this.bs.fxdParams.nppw[i]);
			this.idos.writeIInt(this.bs.fxdParams.gi);
			this.idos.writeIShort(this.bs.fxdParams.bc);
			this.idos.writeIInt(this.bs.fxdParams.nav);
			this.idos.writeIInt(this.bs.fxdParams.ar);
			this.idos.writeIInt(this.bs.fxdParams.fpo);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.nf);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.nfsf);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.po);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.lt);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.rt);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.et);
		}
		catch (IOException e) {
			System.out.println("IO Error writing FxdParams");
			return 0;
		}
		return 1;
	}

	private int writeKeyEvents(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIShort(this.bs.keyEvents.tnke);
			for (int i = 0; i < this.bs.keyEvents.tnke; i++) {
				this.idos.writeIShort(this.bs.keyEvents.en[i]);
				this.idos.writeIInt(this.bs.keyEvents.ept[i]);
				this.idos.writeIShort(this.bs.keyEvents.aci[i]);
				this.idos.writeIShort(this.bs.keyEvents.el[i]);
				this.idos.writeIInt(this.bs.keyEvents.er[i]);
				for (int j = 0; j < 6; j++)
					this.idos.writeIChar(this.bs.keyEvents.ec[i].charAt(j));
				this.idos.writeIChar(this.bs.keyEvents.lmt[i].charAt(0));
				this.idos.writeIChar(this.bs.keyEvents.lmt[i].charAt(1));
				this.idos.writeIString(this.bs.keyEvents.cmt[i]);
			}

			this.idos.writeIInt(this.bs.keyEvents.eel);
			this.idos.writeIInt(this.bs.keyEvents.elmp[0]);
			this.idos.writeIInt(this.bs.keyEvents.elmp[1]);
			this.idos.writeIUnsignedShort(this.bs.keyEvents.orl);
			this.idos.writeIInt(this.bs.keyEvents.rlmp[0]);
			this.idos.writeIInt(this.bs.keyEvents.rlmp[1]);
		}
		catch (IOException e) {
			System.out.println("IO Error writing KeyEvents");
			return 0;
		}
		return 1;
	}

	private int writeLnkParams(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIShort(this.bs.lnkParams.tnl);
			for (int i = 0; i < this.bs.lnkParams.tnl; i++) {
				this.idos.writeIShort(this.bs.lnkParams.lmn[i]);
				this.idos.writeIChar(this.bs.lnkParams.lmc[i].charAt(0));
				this.idos.writeIChar(this.bs.lnkParams.lmc[i].charAt(1));
				this.idos.writeIInt(this.bs.lnkParams.lml[i]);
				this.idos.writeShort(this.bs.lnkParams.ren[i]);
				this.idos.writeIInt(this.bs.lnkParams.gpa[i][0]);
				this.idos.writeIInt(this.bs.lnkParams.gpa[i][1]);
				this.idos.writeIShort(this.bs.lnkParams.fci[i]);
				this.idos.writeIInt(this.bs.lnkParams.smi[i]);
				this.idos.writeIInt(this.bs.lnkParams.sml[i]);
				this.idos.writeIChar(this.bs.lnkParams.usml[i].charAt(0));
				this.idos.writeIChar(this.bs.lnkParams.usml[i].charAt(1));
				this.idos.writeIShort(this.bs.lnkParams.mfdl[i]);
				this.idos.writeIString(this.bs.lnkParams.cmt[i]);
			}
		}
		catch (IOException e) {
			System.out.println("IO Error writing LnkParams");
			return 0;
		}
		return 1;
	}

	private int writeDataPts(int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			this.idos.writeIInt(this.bs.dataPts.tndp);
			this.idos.writeIShort(this.bs.dataPts.tsf);
			for (int i = 0; i < this.bs.dataPts.tsf; i++) {
				this.idos.writeIInt(this.bs.dataPts.tps[i]);
				this.idos.writeIShort(this.bs.dataPts.sf[i]);
				for (int j = 0; j < this.bs.dataPts.tps[i]; j++)
					this.idos.writeIUnsignedShort(this.bs.dataPts.dsf[i][j]);
			}
		}
		catch (IOException e) {
			System.out.println("IO Error writing DataPts");
			return 0;
		}
		return 1;
	}

	private int writeSpecial(int j, int n) {
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try {
			for (int i = 0; i < this.bs.special[j].getSize(); i++)
				this.idos.writeByte(this.bs.special[j].specData[i]);
		}
		catch (IOException e) {
			System.out.println("IO Error writing Special Data");
			return 0;
		}
		return 1;
	}

	/**
	 * @todo Parameter never read.
	 */
	private int writeCksum(int n) {
//		ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());
//		int sum = crc16(bb);
//		try {
//			idos.writeIUnsignedShort(sum);
//		}
//		catch (IOException e) {
//			System.err.println("IO Error writing Checksum");
//			return 0;
//		}
		return 1;
	}

	/**
	 * @todo Method never read locally.
	 */
	private static int crc16(ByteBuffer bb) {
		int sum = 0;
		while (bb.hasRemaining()) {
			if ((sum & 1) != 0)
				sum = (sum >> 1) + 0x8000;
			else
				sum >>= 1;
			sum += bb.get() & 0xff;
			sum &= 0xffff;
		}
		return sum;
	}
}
