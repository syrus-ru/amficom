package com.syrus.AMFICOM.general;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.syrus.io.IntelDataInputStream;

public class BellcoreReader extends DataReader
{
	BellcoreStructure bs;
	ByteArrayInputStream bais;
	IntelDataInputStream idis;

	private static final String IO_ERROR_MESSAGE = "I/O Error";

	public BellcoreStructure getData (byte[] raw_data)
	{
		if (raw_data.length < 50)
			 return null;
		bs = new BellcoreStructure();
		bais = new ByteArrayInputStream(raw_data);
		idis = new IntelDataInputStream(bais);

		bs.addField(1);
		read_map(); // читаем карту файла

		for (int i = 1; i < bs.map.NB; i++) // анализируем какие блоки присутствуют
																				// и читаем их
			{
				if (bs.map.B_id[i].equals("GenParams"))
				{
					bs.addField(2); read_genParams(i);
				}
				else if (bs.map.B_id[i].equals("SupParams"))
				{
					bs.addField(3); read_supParams(i);
				}
				else if (bs.map.B_id[i].equals("FxdParams"))
				{
					bs.addField(4); read_fxdParams(i);
				}
				else if (bs.map.B_id[i].equals("KeyEvents"))
				{
					bs.addField(5); read_keyEvents(i);
				}
				else if (bs.map.B_id[i].equals("LnkParams"))
				{
					bs.addField(6); read_lnkParams(i);
				}
				else if (bs.map.B_id[i].equals("DataPts"))
				{
					bs.addField(7); read_dataPts(i);
				}
				else if (bs.map.B_id[i].equals("Cksum"))
				{
					read_ckSum(i);
				}
				else if (bs.map.B_id[i].equals("HPMiniSpecial"))
				{
					bs.addField(BellcoreStructure.SPECIAL); read_HPMiniSpecial(i);
				}else
				{
					bs.addField(BellcoreStructure.SPECIAL); read_special(i);
				}
			}

		return bs;
	}

	int read_map()
	{
		try
		{
			bs.map.MRN = idis.readIUnsignedShort();
			bs.map.MBS = idis.readIInt();
			bs.map.NB = idis.readIShort();
			bs.map.B_id = new String[bs.map.NB + 32];
			bs.map.B_rev = new int[bs.map.NB + 32];
			bs.map.B_size = new int[bs.map.NB + 32];
			bs.special = new BellcoreStructure.Special[256];

			for (int i = 1; i < bs.map.NB; i++)
			{
				bs.map.B_id[i] = idis.readIString();
				bs.map.B_rev[i] = idis.readIUnsignedShort();
				bs.map.B_size[i] = idis.readIInt();
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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.genParams.LC = String.valueOf(idis.readIChar());
			bs.genParams.LC += idis.readIChar();
			bs.genParams.CID = idis.readIString();
			bs.genParams.FID = idis.readIString();
//    bs.genParams.FT = idis.readIShort();
			bs.genParams.NW = idis.readIShort();
			bs.genParams.OL = idis.readIString();
			bs.genParams.TL = idis.readIString();
			bs.genParams.CCD = idis.readIString();
			bs.genParams.CDF = String.valueOf(idis.readIChar());
			bs.genParams.CDF += idis.readIChar();
			bs.genParams.UO = idis.readIInt();
//		bs.genParams.UOD = idis.readIInt();
			bs.genParams.OP = idis.readIString();
			bs.genParams.CMT = idis.readIString();
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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.supParams.SN = idis.readIString();
			bs.supParams.MFID = idis.readIString();
			bs.supParams.OTDR = idis.readIString();
			bs.supParams.OMID = idis.readIString();
			bs.supParams.OMSN = idis.readIString();
			bs.supParams.SR = idis.readIString();
			bs.supParams.OT = idis.readIString();
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
	 if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.fxdParams.DTS = idis.readIUnsignedInt();
			bs.fxdParams.UD = String.valueOf(idis.readIChar());
			bs.fxdParams.UD += idis.readIChar();
			bs.fxdParams.AW = idis.readIShort();
			bs.fxdParams.AO = idis.readIInt();
			bs.fxdParams.TPW = idis.readIShort();
			bs.fxdParams.PWU = new short[bs.fxdParams.TPW];
			bs.fxdParams.DS = new int[bs.fxdParams.TPW];
			bs.fxdParams.NPPW = new int[bs.fxdParams.TPW];
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				bs.fxdParams.PWU[i] = idis.readIShort();
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				bs.fxdParams.DS[i] = idis.readIInt();
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				bs.fxdParams.NPPW[i] = idis.readIInt();
			bs.fxdParams.GI = idis.readIInt();
			bs.fxdParams.BC = idis.readIShort();
			bs.fxdParams.NAV = idis.readIInt();
			bs.fxdParams.AR = idis.readIInt();
			bs.fxdParams.FPO = idis.readIInt();
			bs.fxdParams.NF = idis.readIUnsignedShort();
			bs.fxdParams.NFSF = idis.readIUnsignedShort();
			bs.fxdParams.PO = idis.readIUnsignedShort();
			bs.fxdParams.LT = idis.readIUnsignedShort();
			bs.fxdParams.RT = idis.readIUnsignedShort();
			bs.fxdParams.ET = idis.readIUnsignedShort();
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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.keyEvents.TNKE = idis.readIShort();
			bs.keyEvents.EN = new short[bs.keyEvents.TNKE];
			bs.keyEvents.EPT = new int[bs.keyEvents.TNKE];
			bs.keyEvents.ACI = new short[bs.keyEvents.TNKE];
			bs.keyEvents.EL = new short[bs.keyEvents.TNKE];
			bs.keyEvents.ER = new int[bs.keyEvents.TNKE];
			bs.keyEvents.EC = new String[bs.keyEvents.TNKE];
			bs.keyEvents.LMT = new String[bs.keyEvents.TNKE];
			bs.keyEvents.CMT = new String[bs.keyEvents.TNKE];
			for (int i = 0; i < bs.keyEvents.TNKE; i++)
			{
				bs.keyEvents.EN[i] = idis.readIShort();
				bs.keyEvents.EPT[i] = idis.readIInt();
				bs.keyEvents.ACI[i] = idis.readIShort();
				bs.keyEvents.EL[i] = idis.readIShort();
				bs.keyEvents.ER[i] = idis.readIInt();
				bs.keyEvents.EC[i] = String.valueOf(idis.readIChar());
				for (int j = 1; j < 6; j++)
					bs.keyEvents.EC[i] += idis.readIChar();
				bs.keyEvents.LMT[i] = String.valueOf(idis.readIChar());
				bs.keyEvents.LMT[i] += idis.readIChar();
				bs.keyEvents.CMT[i] = idis.readIString();
			}
			bs.keyEvents.EEL = idis.readIInt();
			bs.keyEvents.ELMP[0] = idis.readIInt();
			bs.keyEvents.ELMP[1] = idis.readIInt();
			bs.keyEvents.ORL = idis.readIUnsignedShort();
			bs.keyEvents.RLMP[0] = idis.readIInt();
			bs.keyEvents.RLMP[1] = idis.readIInt();
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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.lnkParams.TNL = idis.readIShort();
			bs.lnkParams.LMN = new short[bs.lnkParams.TNL];
			bs.lnkParams.LMC = new String[bs.lnkParams.TNL];
			bs.lnkParams.LML = new int[bs.lnkParams.TNL];
			bs.lnkParams.REN = new short[bs.lnkParams.TNL];
			bs.lnkParams.GPA = new int[bs.lnkParams.TNL][2];
			bs.lnkParams.FCI = new short[bs.lnkParams.TNL];
			bs.lnkParams.SMI = new int[bs.lnkParams.TNL];
			bs.lnkParams.SML = new int[bs.lnkParams.TNL];
			bs.lnkParams.USML = new String[bs.lnkParams.TNL];
			bs.lnkParams.MFDL = new short[bs.lnkParams.TNL];
			bs.lnkParams.CMT = new String[bs.lnkParams.TNL];
			for (int i = 0; i < bs.lnkParams.TNL; i++)
			{
				bs.lnkParams.LMN[i] = idis.readIShort();
				bs.lnkParams.LMC[i] = String.valueOf(idis.readIChar());
				bs.lnkParams.LMC[i] += idis.readIChar();
				bs.lnkParams.LML[i] = idis.readIInt();
				bs.lnkParams.REN[i] = idis.readIShort();
				bs.lnkParams.GPA[i][0] = idis.readIInt();
				bs.lnkParams.GPA[i][1] = idis.readIInt();
				bs.lnkParams.FCI[i] = idis.readIShort();
				bs.lnkParams.SMI[i] = idis.readIInt();
				bs.lnkParams.SML[i] = idis.readIInt();
				bs.lnkParams.USML[i] = String.valueOf(idis.readIChar());
				bs.lnkParams.USML[i] += idis.readIChar();
				bs.lnkParams.MFDL[i] = idis.readIShort();
				bs.lnkParams.CMT[i] = idis.readIString();
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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			bs.dataPts.TNDP = idis.readIInt();
			bs.dataPts.TSF = idis.readIShort();

			bs.dataPts.TPS = new int[bs.dataPts.TSF];
			bs.dataPts.SF = new short[bs.dataPts.TSF];
			bs.dataPts.DSF = new int[bs.dataPts.TSF][bs.dataPts.TNDP];

			for (int i = 0; i < bs.dataPts.TSF; i++)
			{
				bs.dataPts.TPS[i] = idis.readIInt();
				bs.dataPts.SF[i] = idis.readIShort();
				for (int j = 0; j < bs.dataPts.TPS[i]; j++)
				{
					bs.dataPts.DSF[i][j] = idis.readIUnsignedShort();
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

	int read_ckSum(int n)
	{
		return 1;
	}

	int read_HPMiniSpecial(int n)
	{
		if (read_special(n) == 0)
			return 0;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bs.special[bs.specials-1].spec_data);
			idis = new IntelDataInputStream(bais);

			bs.fxdParams.UD = "mt";
			bs.fxdParams.TPW = 1;
			bs.fxdParams.PWU = new short[1];
			bs.fxdParams.DS = new int [1];
			bs.fxdParams.NPPW = new int [1];

			idis.skipBytes(0x22a);
			bs.fxdParams.PWU[0] = idis.readIShort();
			bs.genParams.NW = idis.readIShort();
			bs.fxdParams.AW = (short)(bs.genParams.NW * 10);

			idis.skipBytes(0x8);
			bs.fxdParams.GI = idis.readIInt();

			idis.skipBytes(0x4);
			double resolution = (double)idis.readIInt()/1000000.0; // в m

			idis.skipBytes(0x1C);
			bs.fxdParams.DTS = idis.readIInt();

			bs.fxdParams.DS[0] = (int) (resolution * bs.fxdParams.GI / 3d * 10d/*points*/);
			bs.fxdParams.NPPW[0] = bs.dataPts.TNDP;
			bs.fxdParams.AR = (int) (resolution * bs.dataPts.TNDP * bs.fxdParams.GI / (3d * 1000/*meters*/) );

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
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
//			bs.special[bs.specials-1].getSize = bs.map.B_size[n];
			bs.special[bs.specials-1].spec_data = new byte[bs.map.B_size[n]];
			for (int i = 0; i < bs.map.B_size[n]; i++ )
				bs.special[bs.specials-1].spec_data[i] = idis.readByte();
		}
		catch (IOException ioe)
		{
			System.err.println(IO_ERROR_MESSAGE);
			return 0;
		}
		return 1;
	}
}
