/*
 * $Id: BellcoreReader.java,v 1.11 2005/06/20 14:24:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/20 14:24:40 $
 * @author $Author: bass $
 * @module util
 */
public final class BellcoreReader extends DataReader {
	BellcoreStructure bs;
	ByteArrayInputStream bais;
	IntelDataInputStream idis;

	private static final String IO_ERROR_MESSAGE = "I/O Error";

	@Override
	public BellcoreStructure getData(byte[] raw_data) {
		if (raw_data.length < 50)
			return null;
		this.bs = new BellcoreStructure();
		this.bais = new ByteArrayInputStream(raw_data);
		this.idis = new IntelDataInputStream(this.bais);

		this.bs.addField(1);
		this.readMap(); // читаем карту файла

		// анализируем какие блоки присутствуют и читаем их
		for (int i = 1; i < this.bs.map.nb; i++) {
			if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_GENPARAMS)) {
				this.bs.addField(2);
				this.readGenParams(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_SUPPARAMS)) {
				this.bs.addField(3);
				this.readSupParams(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_FXDPARAMS)) {
				this.bs.addField(4);
				this.readFxdParams(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_KEYEVENTS)) {
				this.bs.addField(5);
				this.readKeyEvents(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_LNKPARAMS)) {
				this.bs.addField(6);
				this.readLnkParams(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_DATAPTS)) {
				this.bs.addField(7);
				this.readDataPts(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_CKSUM)) {
				readCksum(i);
			} else if (this.bs.map.bId[i].equals(BellcoreStructure.FIELD_NAME_HP_MINI_SPECIAL)) {
				this.bs.addField(BellcoreStructure.SPECIAL);
				this.readHPMiniSpecial(i);
			} else {
				this.bs.addField(BellcoreStructure.SPECIAL);
				this.readSpecial(i);
			}
		}

		return this.bs;
	}

	private int readMap()
	{
		try
		{
			this.bs.map.mrn = this.idis.readIUnsignedShort();
			this.bs.map.mbs = this.idis.readIInt();
			this.bs.map.nb = this.idis.readIShort();
			this.bs.map.bId = new String[this.bs.map.nb + 32];
			this.bs.map.bRev = new int[this.bs.map.nb + 32];
			this.bs.map.bSize = new int[this.bs.map.nb + 32];
			this.bs.special = new BellcoreStructure.Special[256];

			for (int i = 1; i < this.bs.map.nb; i++)
			{
				this.bs.map.bId[i] = this.idis.readIString();
				this.bs.map.bRev[i] = this.idis.readIUnsignedShort();
				this.bs.map.bSize[i] = this.idis.readIInt();
			}
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readGenParams(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.genParams.lc = String.valueOf(this.idis.readIChar());
			this.bs.genParams.lc += this.idis.readIChar();
			this.bs.genParams.cid = this.idis.readIString();
			this.bs.genParams.fid = this.idis.readIString();
//    bs.genParams.FT = idis.readIShort();
			this.bs.genParams.nw = this.idis.readIShort();
			this.bs.genParams.ol = this.idis.readIString();
			this.bs.genParams.tl = this.idis.readIString();
			this.bs.genParams.ccd = this.idis.readIString();
			this.bs.genParams.cdf = String.valueOf(this.idis.readIChar());
			this.bs.genParams.cdf += this.idis.readIChar();
			this.bs.genParams.uo = this.idis.readIInt();
//		bs.genParams.UOD = idis.readIInt();
			this.bs.genParams.op = this.idis.readIString();
			this.bs.genParams.cmt = this.idis.readIString();
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readSupParams(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.supParams.sn = this.idis.readIString();
			this.bs.supParams.mfid = this.idis.readIString();
			this.bs.supParams.otdr = this.idis.readIString();
			this.bs.supParams.omid = this.idis.readIString();
			this.bs.supParams.omsn = this.idis.readIString();
			this.bs.supParams.sr = this.idis.readIString();
			this.bs.supParams.ot = this.idis.readIString();
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readFxdParams(int n)
	{
	 if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.fxdParams.dts = this.idis.readIUnsignedInt();
			this.bs.fxdParams.ud = String.valueOf(this.idis.readIChar());
			this.bs.fxdParams.ud += this.idis.readIChar();
			this.bs.fxdParams.aw = this.idis.readIShort();
			this.bs.fxdParams.ao = this.idis.readIInt();
			this.bs.fxdParams.tpw = this.idis.readIShort();
			this.bs.fxdParams.pwu = new short[this.bs.fxdParams.tpw];
			this.bs.fxdParams.ds = new int[this.bs.fxdParams.tpw];
			this.bs.fxdParams.nppw = new int[this.bs.fxdParams.tpw];
			for (int i = 0; i < this.bs.fxdParams.tpw; i++ )
				this.bs.fxdParams.pwu[i] = this.idis.readIShort();
			for (int i = 0; i < this.bs.fxdParams.tpw; i++ )
				this.bs.fxdParams.ds[i] = this.idis.readIInt();
			for (int i = 0; i < this.bs.fxdParams.tpw; i++ )
				this.bs.fxdParams.nppw[i] = this.idis.readIInt();
			this.bs.fxdParams.gi = this.idis.readIInt();
			this.bs.fxdParams.bc = this.idis.readIShort();
			this.bs.fxdParams.nav = this.idis.readIInt();
			this.bs.fxdParams.ar = this.idis.readIInt();
			this.bs.fxdParams.fpo = this.idis.readIInt();
			this.bs.fxdParams.nf = this.idis.readIUnsignedShort();
			this.bs.fxdParams.nfsf = this.idis.readIUnsignedShort();
			this.bs.fxdParams.po = this.idis.readIUnsignedShort();
			this.bs.fxdParams.lt = this.idis.readIUnsignedShort();
			this.bs.fxdParams.rt = this.idis.readIUnsignedShort();
			this.bs.fxdParams.et = this.idis.readIUnsignedShort();
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readKeyEvents(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.keyEvents.tnke = this.idis.readIShort();
			this.bs.keyEvents.en = new short[this.bs.keyEvents.tnke];
			this.bs.keyEvents.ept = new int[this.bs.keyEvents.tnke];
			this.bs.keyEvents.aci = new short[this.bs.keyEvents.tnke];
			this.bs.keyEvents.el = new short[this.bs.keyEvents.tnke];
			this.bs.keyEvents.er = new int[this.bs.keyEvents.tnke];
			this.bs.keyEvents.ec = new String[this.bs.keyEvents.tnke];
			this.bs.keyEvents.lmt = new String[this.bs.keyEvents.tnke];
			this.bs.keyEvents.cmt = new String[this.bs.keyEvents.tnke];
			for (int i = 0; i < this.bs.keyEvents.tnke; i++)
			{
				this.bs.keyEvents.en[i] = this.idis.readIShort();
				this.bs.keyEvents.ept[i] = this.idis.readIInt();
				this.bs.keyEvents.aci[i] = this.idis.readIShort();
				this.bs.keyEvents.el[i] = this.idis.readIShort();
				this.bs.keyEvents.er[i] = this.idis.readIInt();
				this.bs.keyEvents.ec[i] = String.valueOf(this.idis.readIChar());
				for (int j = 1; j < 6; j++)
					this.bs.keyEvents.ec[i] += this.idis.readIChar();
				this.bs.keyEvents.lmt[i] = String.valueOf(this.idis.readIChar());
				this.bs.keyEvents.lmt[i] += this.idis.readIChar();
				this.bs.keyEvents.cmt[i] = this.idis.readIString();
			}
			this.bs.keyEvents.eel = this.idis.readIInt();
			this.bs.keyEvents.elmp[0] = this.idis.readIInt();
			this.bs.keyEvents.elmp[1] = this.idis.readIInt();
			this.bs.keyEvents.orl = this.idis.readIUnsignedShort();
			this.bs.keyEvents.rlmp[0] = this.idis.readIInt();
			this.bs.keyEvents.rlmp[1] = this.idis.readIInt();
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readLnkParams(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.lnkParams.tnl = this.idis.readIShort();
			this.bs.lnkParams.lmn = new short[this.bs.lnkParams.tnl];
			this.bs.lnkParams.lmc = new String[this.bs.lnkParams.tnl];
			this.bs.lnkParams.lml = new int[this.bs.lnkParams.tnl];
			this.bs.lnkParams.ren = new short[this.bs.lnkParams.tnl];
			this.bs.lnkParams.gpa = new int[this.bs.lnkParams.tnl][2];
			this.bs.lnkParams.fci = new short[this.bs.lnkParams.tnl];
			this.bs.lnkParams.smi = new int[this.bs.lnkParams.tnl];
			this.bs.lnkParams.sml = new int[this.bs.lnkParams.tnl];
			this.bs.lnkParams.usml = new String[this.bs.lnkParams.tnl];
			this.bs.lnkParams.mfdl = new short[this.bs.lnkParams.tnl];
			this.bs.lnkParams.cmt = new String[this.bs.lnkParams.tnl];
			for (int i = 0; i < this.bs.lnkParams.tnl; i++)
			{
				this.bs.lnkParams.lmn[i] = this.idis.readIShort();
				this.bs.lnkParams.lmc[i] = String.valueOf(this.idis.readIChar());
				this.bs.lnkParams.lmc[i] += this.idis.readIChar();
				this.bs.lnkParams.lml[i] = this.idis.readIInt();
				this.bs.lnkParams.ren[i] = this.idis.readIShort();
				this.bs.lnkParams.gpa[i][0] = this.idis.readIInt();
				this.bs.lnkParams.gpa[i][1] = this.idis.readIInt();
				this.bs.lnkParams.fci[i] = this.idis.readIShort();
				this.bs.lnkParams.smi[i] = this.idis.readIInt();
				this.bs.lnkParams.sml[i] = this.idis.readIInt();
				this.bs.lnkParams.usml[i] = String.valueOf(this.idis.readIChar());
				this.bs.lnkParams.usml[i] += this.idis.readIChar();
				this.bs.lnkParams.mfdl[i] = this.idis.readIShort();
				this.bs.lnkParams.cmt[i] = this.idis.readIString();
			}
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readDataPts(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
			this.bs.dataPts.tndp = this.idis.readIInt();
			this.bs.dataPts.tsf = this.idis.readIShort();

			this.bs.dataPts.tps = new int[this.bs.dataPts.tsf];
			this.bs.dataPts.sf = new short[this.bs.dataPts.tsf];
			this.bs.dataPts.dsf = new int[this.bs.dataPts.tsf][this.bs.dataPts.tndp];

			for (int i = 0; i < this.bs.dataPts.tsf; i++)
			{
				this.bs.dataPts.tps[i] = this.idis.readIInt();
				this.bs.dataPts.sf[i] = this.idis.readIShort();
				for (int j = 0; j < this.bs.dataPts.tps[i]; j++)
				{
					this.bs.dataPts.dsf[i][j] = this.idis.readIUnsignedShort();
				}
			}
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	/**
	 * @todo Parameter <code>n</code> is never read. Method is used only
	 *       locally. Wouldn't it be wise to remove it?
	 */
	private int readCksum(@SuppressWarnings("unusedArgument") final int n) {
		return 1;
	}

	private int readHPMiniSpecial(int n)
	{
		if (readSpecial(n) == 0)
			return 0;
		try
		{
			this.idis = new IntelDataInputStream(new ByteArrayInputStream(this.bs.special[this.bs.specials-1].specData));

			this.bs.fxdParams.ud = "mt";
			this.bs.fxdParams.tpw = 1;
			this.bs.fxdParams.pwu = new short[1];
			this.bs.fxdParams.ds = new int [1];
			this.bs.fxdParams.nppw = new int [1];

			this.idis.skipBytes(0x22a);
			this.bs.fxdParams.pwu[0] = this.idis.readIShort();
			this.bs.genParams.nw = this.idis.readIShort();
			this.bs.fxdParams.aw = (short)(this.bs.genParams.nw * 10);

			this.idis.skipBytes(0x8);
			this.bs.fxdParams.gi = this.idis.readIInt();

			this.idis.skipBytes(0x4);
			double resolution = this.idis.readIInt()/1000000.0; // Б m

			this.idis.skipBytes(0x1C);
			this.bs.fxdParams.dts = this.idis.readIInt();

			this.bs.fxdParams.ds[0] = (int) (resolution * this.bs.fxdParams.gi / 3d * 10d/*points*/);
			this.bs.fxdParams.nppw[0] = this.bs.dataPts.tndp;
			this.bs.fxdParams.ar = (int) (resolution * this.bs.dataPts.tndp * this.bs.fxdParams.gi / (3d * 1000/*meters*/) );

		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	private int readSpecial(int n)
	{
		if (this.bs.map.bSize[n] == 0)
			return 0;
		try
		{
//			bs.special[bs.specials-1].getSize = bs.map.B_size[n];
			this.bs.special[this.bs.specials-1].specData = new byte[this.bs.map.bSize[n]];
			for (int i = 0; i < this.bs.map.bSize[n]; i++ )
				this.bs.special[this.bs.specials-1].specData[i] = this.idis.readByte();
		} catch (IOException ioe) {
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}
}
