/*
 * $Id: BellcoreReader.java,v 1.6 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright њ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.6 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class BellcoreReader extends DataReader
{
	BellcoreStructure bs;
	ByteArrayInputStream bais;
	IntelDataInputStream idis;

	private static final String IO_ERROR_MESSAGE = "I/O Error"; //$NON-NLS-1$

	public BellcoreStructure getData (byte[] raw_data)
	{
		if (raw_data.length < 50)
			 return null;
		this.bs = new BellcoreStructure();
		this.bais = new ByteArrayInputStream(raw_data);
		this.idis = new IntelDataInputStream(this.bais);

		this.bs.addField(1);
		read_map(); // читаем карту файла

		for (int i = 1; i < this.bs.map.NB; i++) // анализируем какие блоки присутствуют
																				// и читаем их
			{
				if (this.bs.map.B_id[i].equals("GenParams")) //$NON-NLS-1$
				{
					this.bs.addField(2); read_genParams(i);
				}
				else if (this.bs.map.B_id[i].equals("SupParams")) //$NON-NLS-1$
				{
					this.bs.addField(3); read_supParams(i);
				}
				else if (this.bs.map.B_id[i].equals("FxdParams")) //$NON-NLS-1$
				{
					this.bs.addField(4); read_fxdParams(i);
				}
				else if (this.bs.map.B_id[i].equals("KeyEvents")) //$NON-NLS-1$
				{
					this.bs.addField(5); read_keyEvents(i);
				}
				else if (this.bs.map.B_id[i].equals("LnkParams")) //$NON-NLS-1$
				{
					this.bs.addField(6); read_lnkParams(i);
				}
				else if (this.bs.map.B_id[i].equals("DataPts")) //$NON-NLS-1$
				{
					this.bs.addField(7); read_dataPts(i);
				}
				else if (this.bs.map.B_id[i].equals("Cksum")) //$NON-NLS-1$
				{
					read_ckSum(i);
				}
				else if (this.bs.map.B_id[i].equals("HPMiniSpecial")) //$NON-NLS-1$
				{
					this.bs.addField(BellcoreStructure.SPECIAL); read_HPMiniSpecial(i);
				}else
				{
					this.bs.addField(BellcoreStructure.SPECIAL); read_special(i);
				}
			}

		return this.bs;
	}

	int read_map()
	{
		try
		{
			this.bs.map.MRN = this.idis.readIUnsignedShort();
			this.bs.map.MBS = this.idis.readIInt();
			this.bs.map.NB = this.idis.readIShort();
			this.bs.map.B_id = new String[this.bs.map.NB + 32];
			this.bs.map.B_rev = new int[this.bs.map.NB + 32];
			this.bs.map.B_size = new int[this.bs.map.NB + 32];
			this.bs.special = new BellcoreStructure.Special[256];

			for (int i = 1; i < this.bs.map.NB; i++)
			{
				this.bs.map.B_id[i] = this.idis.readIString();
				this.bs.map.B_rev[i] = this.idis.readIUnsignedShort();
				this.bs.map.B_size[i] = this.idis.readIInt();
			}
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_genParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.genParams.LC = String.valueOf(this.idis.readIChar());
			this.bs.genParams.LC += this.idis.readIChar();
			this.bs.genParams.CID = this.idis.readIString();
			this.bs.genParams.FID = this.idis.readIString();
//    bs.genParams.FT = idis.readIShort();
			this.bs.genParams.NW = this.idis.readIShort();
			this.bs.genParams.OL = this.idis.readIString();
			this.bs.genParams.TL = this.idis.readIString();
			this.bs.genParams.CCD = this.idis.readIString();
			this.bs.genParams.CDF = String.valueOf(this.idis.readIChar());
			this.bs.genParams.CDF += this.idis.readIChar();
			this.bs.genParams.UO = this.idis.readIInt();
//		bs.genParams.UOD = idis.readIInt();
			this.bs.genParams.OP = this.idis.readIString();
			this.bs.genParams.CMT = this.idis.readIString();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_supParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.supParams.SN = this.idis.readIString();
			this.bs.supParams.MFID = this.idis.readIString();
			this.bs.supParams.OTDR = this.idis.readIString();
			this.bs.supParams.OMID = this.idis.readIString();
			this.bs.supParams.OMSN = this.idis.readIString();
			this.bs.supParams.SR = this.idis.readIString();
			this.bs.supParams.OT = this.idis.readIString();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_fxdParams(int n)
	{
	 if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.fxdParams.DTS = this.idis.readIUnsignedInt();
			this.bs.fxdParams.UD = String.valueOf(this.idis.readIChar());
			this.bs.fxdParams.UD += this.idis.readIChar();
			this.bs.fxdParams.AW = this.idis.readIShort();
			this.bs.fxdParams.AO = this.idis.readIInt();
			this.bs.fxdParams.TPW = this.idis.readIShort();
			this.bs.fxdParams.PWU = new short[this.bs.fxdParams.TPW];
			this.bs.fxdParams.DS = new int[this.bs.fxdParams.TPW];
			this.bs.fxdParams.NPPW = new int[this.bs.fxdParams.TPW];
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.bs.fxdParams.PWU[i] = this.idis.readIShort();
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.bs.fxdParams.DS[i] = this.idis.readIInt();
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.bs.fxdParams.NPPW[i] = this.idis.readIInt();
			this.bs.fxdParams.GI = this.idis.readIInt();
			this.bs.fxdParams.BC = this.idis.readIShort();
			this.bs.fxdParams.NAV = this.idis.readIInt();
			this.bs.fxdParams.AR = this.idis.readIInt();
			this.bs.fxdParams.FPO = this.idis.readIInt();
			this.bs.fxdParams.NF = this.idis.readIUnsignedShort();
			this.bs.fxdParams.NFSF = this.idis.readIUnsignedShort();
			this.bs.fxdParams.PO = this.idis.readIUnsignedShort();
			this.bs.fxdParams.LT = this.idis.readIUnsignedShort();
			this.bs.fxdParams.RT = this.idis.readIUnsignedShort();
			this.bs.fxdParams.ET = this.idis.readIUnsignedShort();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_keyEvents(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.keyEvents.TNKE = this.idis.readIShort();
			this.bs.keyEvents.EN = new short[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.EPT = new int[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.ACI = new short[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.EL = new short[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.ER = new int[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.EC = new String[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.LMT = new String[this.bs.keyEvents.TNKE];
			this.bs.keyEvents.CMT = new String[this.bs.keyEvents.TNKE];
			for (int i = 0; i < this.bs.keyEvents.TNKE; i++)
			{
				this.bs.keyEvents.EN[i] = this.idis.readIShort();
				this.bs.keyEvents.EPT[i] = this.idis.readIInt();
				this.bs.keyEvents.ACI[i] = this.idis.readIShort();
				this.bs.keyEvents.EL[i] = this.idis.readIShort();
				this.bs.keyEvents.ER[i] = this.idis.readIInt();
				this.bs.keyEvents.EC[i] = String.valueOf(this.idis.readIChar());
				for (int j = 1; j < 6; j++)
					this.bs.keyEvents.EC[i] += this.idis.readIChar();
				this.bs.keyEvents.LMT[i] = String.valueOf(this.idis.readIChar());
				this.bs.keyEvents.LMT[i] += this.idis.readIChar();
				this.bs.keyEvents.CMT[i] = this.idis.readIString();
			}
			this.bs.keyEvents.EEL = this.idis.readIInt();
			this.bs.keyEvents.ELMP[0] = this.idis.readIInt();
			this.bs.keyEvents.ELMP[1] = this.idis.readIInt();
			this.bs.keyEvents.ORL = this.idis.readIUnsignedShort();
			this.bs.keyEvents.RLMP[0] = this.idis.readIInt();
			this.bs.keyEvents.RLMP[1] = this.idis.readIInt();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_lnkParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.lnkParams.TNL = this.idis.readIShort();
			this.bs.lnkParams.LMN = new short[this.bs.lnkParams.TNL];
			this.bs.lnkParams.LMC = new String[this.bs.lnkParams.TNL];
			this.bs.lnkParams.LML = new int[this.bs.lnkParams.TNL];
			this.bs.lnkParams.REN = new short[this.bs.lnkParams.TNL];
			this.bs.lnkParams.GPA = new int[this.bs.lnkParams.TNL][2];
			this.bs.lnkParams.FCI = new short[this.bs.lnkParams.TNL];
			this.bs.lnkParams.SMI = new int[this.bs.lnkParams.TNL];
			this.bs.lnkParams.SML = new int[this.bs.lnkParams.TNL];
			this.bs.lnkParams.USML = new String[this.bs.lnkParams.TNL];
			this.bs.lnkParams.MFDL = new short[this.bs.lnkParams.TNL];
			this.bs.lnkParams.CMT = new String[this.bs.lnkParams.TNL];
			for (int i = 0; i < this.bs.lnkParams.TNL; i++)
			{
				this.bs.lnkParams.LMN[i] = this.idis.readIShort();
				this.bs.lnkParams.LMC[i] = String.valueOf(this.idis.readIChar());
				this.bs.lnkParams.LMC[i] += this.idis.readIChar();
				this.bs.lnkParams.LML[i] = this.idis.readIInt();
				this.bs.lnkParams.REN[i] = this.idis.readIShort();
				this.bs.lnkParams.GPA[i][0] = this.idis.readIInt();
				this.bs.lnkParams.GPA[i][1] = this.idis.readIInt();
				this.bs.lnkParams.FCI[i] = this.idis.readIShort();
				this.bs.lnkParams.SMI[i] = this.idis.readIInt();
				this.bs.lnkParams.SML[i] = this.idis.readIInt();
				this.bs.lnkParams.USML[i] = String.valueOf(this.idis.readIChar());
				this.bs.lnkParams.USML[i] += this.idis.readIChar();
				this.bs.lnkParams.MFDL[i] = this.idis.readIShort();
				this.bs.lnkParams.CMT[i] = this.idis.readIString();
			}
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_dataPts(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.bs.dataPts.TNDP = this.idis.readIInt();
			this.bs.dataPts.TSF = this.idis.readIShort();

			this.bs.dataPts.TPS = new int[this.bs.dataPts.TSF];
			this.bs.dataPts.SF = new short[this.bs.dataPts.TSF];
			this.bs.dataPts.DSF = new int[this.bs.dataPts.TSF][this.bs.dataPts.TNDP];

			for (int i = 0; i < this.bs.dataPts.TSF; i++)
			{
				this.bs.dataPts.TPS[i] = this.idis.readIInt();
				this.bs.dataPts.SF[i] = this.idis.readIShort();
				for (int j = 0; j < this.bs.dataPts.TPS[i]; j++)
				{
					this.bs.dataPts.DSF[i][j] = this.idis.readIUnsignedShort();
				}
			}
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	/**
	 * @todo Parameter <code>n</code> is never read. Method is used only
	 *       locally. Wouldn't it be wise to remove it?
	 */
	private int read_ckSum(final int n)
	{
		return 1;
	}

	int read_HPMiniSpecial(int n)
	{
		if (read_special(n) == 0)
			return 0;
		try
		{
			this.idis = new IntelDataInputStream(new ByteArrayInputStream(this.bs.special[this.bs.specials-1].spec_data));

			this.bs.fxdParams.UD = "mt"; //$NON-NLS-1$
			this.bs.fxdParams.TPW = 1;
			this.bs.fxdParams.PWU = new short[1];
			this.bs.fxdParams.DS = new int [1];
			this.bs.fxdParams.NPPW = new int [1];

			this.idis.skipBytes(0x22a);
			this.bs.fxdParams.PWU[0] = this.idis.readIShort();
			this.bs.genParams.NW = this.idis.readIShort();
			this.bs.fxdParams.AW = (short)(this.bs.genParams.NW * 10);

			this.idis.skipBytes(0x8);
			this.bs.fxdParams.GI = this.idis.readIInt();

			this.idis.skipBytes(0x4);
			double resolution = this.idis.readIInt()/1000000.0; // в m

			this.idis.skipBytes(0x1C);
			this.bs.fxdParams.DTS = this.idis.readIInt();

			this.bs.fxdParams.DS[0] = (int) (resolution * this.bs.fxdParams.GI / 3d * 10d/*points*/);
			this.bs.fxdParams.NPPW[0] = this.bs.dataPts.TNDP;
			this.bs.fxdParams.AR = (int) (resolution * this.bs.dataPts.TNDP * this.bs.fxdParams.GI / (3d * 1000/*meters*/) );

		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}

	int read_special(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
//			bs.special[bs.specials-1].getSize = bs.map.B_size[n];
			this.bs.special[this.bs.specials-1].spec_data = new byte[this.bs.map.B_size[n]];
			for (int i = 0; i < this.bs.map.B_size[n]; i++ )
				this.bs.special[this.bs.specials-1].spec_data[i] = this.idis.readByte();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}
}
