/*
 * $Id: BellcoreWriter.java,v 1.4 2004/11/22 14:03:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.io;

import java.io.*;
import java.nio.*;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/22 14:03:38 $
 * @author $Author: stas $
 * @module general_v1
 */
public class BellcoreWriter
{
	byte[] data;
	ByteArrayOutputStream baos;
	IntelDataOutputStream idos;
	BellcoreStructure bs;

	public byte[] write (BellcoreStructure bs)
	{
		this.bs = bs;
		baos = new ByteArrayOutputStream();
		idos = new IntelDataOutputStream(baos);
//		int specials = bs.specials;
		write_map();

		int i = 1;
		int j = 0;
		while (i < bs.map.NB)
			{
				if (bs.map.B_id[i].equals("GenParams"))
					{ write_genParams(i++);}
				else if (bs.map.B_id[i].equals("SupParams"))
					{ write_supParams(i++);}
				else if (bs.map.B_id[i].equals("FxdParams"))
					{ write_fxdParams(i++);}
				else if (bs.map.B_id[i].equals("KeyEvents"))
					{ write_keyEvents(i++);}
				else if (bs.map.B_id[i].equals("LnkParams"))
					{ write_lnkParams(i++);}
				else if (bs.map.B_id[i].equals("DataPts") )
					{ write_dataPts(i++);}
				else if (bs.map.B_id[i].equals("Cksum"))
					{ write_cksum(i++);}
				else if (bs.specials > 0)
					{ write_special(j++, i++);}
				else
					System.out.println("Unknown block!");
			}

		data = new byte[baos.size()];
		data = baos.toByteArray();
		return data;
	}

	int write_map()
	{
		try
		{
			idos.writeIUnsignedShort(bs.map.MRN);
			idos.writeIInt(bs.map.MBS);
			idos.writeIShort(bs.map.NB);

			for (int i = 1; i < bs.map.NB; i++)
			{
				idos.writeIString(bs.map.B_id[i]);
				idos.writeIUnsignedShort(bs.map.B_rev[i]);
				idos.writeIInt(bs.map.B_size[i]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing Map"); return 0;}
		return 1;
	}

	int write_genParams(int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIChar(bs.genParams.LC.charAt(0));
			idos.writeIChar(bs.genParams.LC.charAt(1));
			idos.writeIString(bs.genParams.CID);
			idos.writeIString(bs.genParams.FID);
			idos.writeIShort(bs.genParams.NW);
			idos.writeIString(bs.genParams.OL);
			idos.writeIString(bs.genParams.TL);
			idos.writeIString(bs.genParams.CCD);
			idos.writeIChar(bs.genParams.CDF.charAt(0));
			idos.writeIChar(bs.genParams.CDF.charAt(1));
			idos.writeIInt(bs.genParams.UO);
			idos.writeIString(bs.genParams.OP);
			idos.writeIString(bs.genParams.CMT);
		}
		catch (IOException e)
			{System.out.println("IO Error writing GenParams"); return 0;}
		return 1;
	}

	int write_supParams(int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIString(bs.supParams.SN);
			idos.writeIString(bs.supParams.MFID);
			idos.writeIString(bs.supParams.OTDR);
			idos.writeIString(bs.supParams.OMID);
			idos.writeIString(bs.supParams.OMSN);
			idos.writeIString(bs.supParams.SR);
			idos.writeIString(bs.supParams.OT);
		}
		catch (IOException e)
			{System.out.println("IO Error writing SupParams"); return 0;}
		return 1;
	}

	int write_fxdParams(int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIUnsignedInt(bs.fxdParams.DTS);
			idos.writeIChar(bs.fxdParams.UD.charAt(0));
			idos.writeIChar(bs.fxdParams.UD.charAt(1));
			idos.writeIShort(bs.fxdParams.AW);
			idos.writeIInt(bs.fxdParams.AO);
			idos.writeIShort(bs.fxdParams.TPW);
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				idos.writeIShort(bs.fxdParams.PWU[i]);
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				idos.writeIInt(bs.fxdParams.DS[i]);
			for (int i = 0; i < bs.fxdParams.TPW; i++ )
				idos.writeIInt(bs.fxdParams.NPPW[i]);
			idos.writeIInt(bs.fxdParams.GI);
			idos.writeIShort(bs.fxdParams.BC);
			idos.writeIInt(bs.fxdParams.NAV);
			idos.writeIInt(bs.fxdParams.AR);
			idos.writeIInt(bs.fxdParams.FPO);
			idos.writeIUnsignedShort(bs.fxdParams.NF);
			idos.writeIUnsignedShort(bs.fxdParams.NFSF);
			idos.writeIUnsignedShort(bs.fxdParams.PO);
			idos.writeIUnsignedShort(bs.fxdParams.LT);
			idos.writeIUnsignedShort(bs.fxdParams.RT);
			idos.writeIUnsignedShort(bs.fxdParams.ET);
		}
		catch (IOException e)
			{System.out.println("IO Error writing FxdParams");return 0;}
		return 1;
	}

	int write_keyEvents (int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIShort(bs.keyEvents.TNKE);
			for (int i = 0; i < bs.keyEvents.TNKE; i++)
			{
				idos.writeIShort(bs.keyEvents.EN[i]);
				idos.writeIInt(bs.keyEvents.EPT[i]);
				idos.writeIShort(bs.keyEvents.ACI[i]);
				idos.writeIShort(bs.keyEvents.EL[i]);
				idos.writeIInt(bs.keyEvents.ER[i]);
				for (int j = 0; j < 6; j++)
					idos.writeIChar(bs.keyEvents.EC[i].charAt(j));
				idos.writeIChar(bs.keyEvents.LMT[i].charAt(0));
				idos.writeIChar(bs.keyEvents.LMT[i].charAt(1));
				idos.writeIString(bs.keyEvents.CMT[i]);
			}

			idos.writeIInt(bs.keyEvents.EEL);
			idos.writeIInt(bs.keyEvents.ELMP[0]);
			idos.writeIInt(bs.keyEvents.ELMP[1]);
			idos.writeIUnsignedShort(bs.keyEvents.ORL);
			idos.writeIInt(bs.keyEvents.RLMP[0]);
			idos.writeIInt(bs.keyEvents.RLMP[1]);
		}
		catch (IOException e)
			{System.out.println("IO Error writing KeyEvents");return 0;}
		return 1;
	}

	int write_lnkParams(int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIShort(bs.lnkParams.TNL);
			for (int i = 0; i < bs.lnkParams.TNL; i++)
			{
				idos.writeIShort(bs.lnkParams.LMN[i]);
				idos.writeIChar(bs.lnkParams.LMC[i].charAt(0));
				idos.writeIChar(bs.lnkParams.LMC[i].charAt(1));
				idos.writeIInt(bs.lnkParams.LML[i]);
				idos.writeShort(bs.lnkParams.REN[i]);
				idos.writeIInt(bs.lnkParams.GPA[i][0]);
				idos.writeIInt(bs.lnkParams.GPA[i][1]);
				idos.writeIShort(bs.lnkParams.FCI[i]);
				idos.writeIInt(bs.lnkParams.SMI[i]);
				idos.writeIInt(bs.lnkParams.SML[i]);
				idos.writeIChar(bs.lnkParams.USML[i].charAt(0));
				idos.writeIChar(bs.lnkParams.USML[i].charAt(1));
				idos.writeIShort(bs.lnkParams.MFDL[i]);
				idos.writeIString(bs.lnkParams.CMT[i]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing LnkParams");return 0;}
		return 1;
	}

	int write_dataPts(int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			idos.writeIInt(bs.dataPts.TNDP);
			idos.writeIShort(bs.dataPts.TSF);
			for (int i = 0; i < bs.dataPts.TSF; i++)
			{
				idos.writeIInt(bs.dataPts.TPS[i]);
				idos.writeIShort(bs.dataPts.SF[i]);
				for (int j = 0; j < bs.dataPts.TPS[i]; j++)
					idos.writeIUnsignedShort(bs.dataPts.DSF[i][j]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing DataPts");return 0;}
		return 1;
	}

	int write_special (int j, int n)
	{
		if (bs.map.B_size[n] == 0)
			return 0;
		try
		{
			for (int i = 0; i < bs.special[j].getSize(); i++ )
				idos.writeByte(bs.special[j].spec_data[i]);
		}
		catch (IOException e)
			{System.out.println("IO Error writing Special Data");return 0;}
		return 1;
	}

	int write_cksum (int n)
	{
	/*	ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());
		int sum = crc16(bb);
		try
		{
			idos.writeIUnsignedShort(sum);
		}
		catch (IOException e)
			{System.out.println("IO Error writing Checksum");return 0;}*/
		return 1;
	}

	private static int crc16(ByteBuffer bb)
	{
		int sum = 0;
		while (bb.hasRemaining())
		{
			if ( (sum & 1) != 0)
				sum = (sum >> 1) + 0x8000;
			else
				sum >>= 1;
			sum += bb.get() & 0xff;
			sum &= 0xffff;
		}
		return sum;
	}
}
