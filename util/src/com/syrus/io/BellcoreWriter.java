/*
 * $Id: BellcoreWriter.java,v 1.5 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;
import java.nio.*;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class BellcoreWriter
{
	byte[] data;
	ByteArrayOutputStream baos;
	IntelDataOutputStream idos;
	BellcoreStructure bs;

	public byte[] write (BellcoreStructure bs1)
	{
		this.bs = bs1;
		this.baos = new ByteArrayOutputStream();
		this.idos = new IntelDataOutputStream(this.baos);
//		int specials = bs.specials;
		write_map();

		int i = 1;
		int j = 0;
		while (i < bs1.map.NB)
			{
				if (bs1.map.B_id[i].equals("GenParams")) //$NON-NLS-1$
					{ write_genParams(i++);}
				else if (bs1.map.B_id[i].equals("SupParams")) //$NON-NLS-1$
					{ write_supParams(i++);}
				else if (bs1.map.B_id[i].equals("FxdParams")) //$NON-NLS-1$
					{ write_fxdParams(i++);}
				else if (bs1.map.B_id[i].equals("KeyEvents")) //$NON-NLS-1$
					{ write_keyEvents(i++);}
				else if (bs1.map.B_id[i].equals("LnkParams")) //$NON-NLS-1$
					{ write_lnkParams(i++);}
				else if (bs1.map.B_id[i].equals("DataPts") ) //$NON-NLS-1$
					{ write_dataPts(i++);}
				else if (bs1.map.B_id[i].equals("Cksum")) //$NON-NLS-1$
					{ write_cksum(i++);}
				else if (bs1.specials > 0)
					{ write_special(j++, i++);}
				else
					System.out.println("Unknown block!"); //$NON-NLS-1$
			}

		this.data = new byte[this.baos.size()];
		this.data = this.baos.toByteArray();
		return this.data;
	}

	int write_map()
	{
		try
		{
			this.idos.writeIUnsignedShort(this.bs.map.MRN);
			this.idos.writeIInt(this.bs.map.MBS);
			this.idos.writeIShort(this.bs.map.NB);

			for (int i = 1; i < this.bs.map.NB; i++)
			{
				this.idos.writeIString(this.bs.map.B_id[i]);
				this.idos.writeIUnsignedShort(this.bs.map.B_rev[i]);
				this.idos.writeIInt(this.bs.map.B_size[i]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing Map"); return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_genParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIChar(this.bs.genParams.LC.charAt(0));
			this.idos.writeIChar(this.bs.genParams.LC.charAt(1));
			this.idos.writeIString(this.bs.genParams.CID);
			this.idos.writeIString(this.bs.genParams.FID);
			this.idos.writeIShort(this.bs.genParams.NW);
			this.idos.writeIString(this.bs.genParams.OL);
			this.idos.writeIString(this.bs.genParams.TL);
			this.idos.writeIString(this.bs.genParams.CCD);
			this.idos.writeIChar(this.bs.genParams.CDF.charAt(0));
			this.idos.writeIChar(this.bs.genParams.CDF.charAt(1));
			this.idos.writeIInt(this.bs.genParams.UO);
			this.idos.writeIString(this.bs.genParams.OP);
			this.idos.writeIString(this.bs.genParams.CMT);
		}
		catch (IOException e)
			{System.out.println("IO Error writing GenParams"); return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_supParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIString(this.bs.supParams.SN);
			this.idos.writeIString(this.bs.supParams.MFID);
			this.idos.writeIString(this.bs.supParams.OTDR);
			this.idos.writeIString(this.bs.supParams.OMID);
			this.idos.writeIString(this.bs.supParams.OMSN);
			this.idos.writeIString(this.bs.supParams.SR);
			this.idos.writeIString(this.bs.supParams.OT);
		}
		catch (IOException e)
			{System.out.println("IO Error writing SupParams"); return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_fxdParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIUnsignedInt(this.bs.fxdParams.DTS);
			this.idos.writeIChar(this.bs.fxdParams.UD.charAt(0));
			this.idos.writeIChar(this.bs.fxdParams.UD.charAt(1));
			this.idos.writeIShort(this.bs.fxdParams.AW);
			this.idos.writeIInt(this.bs.fxdParams.AO);
			this.idos.writeIShort(this.bs.fxdParams.TPW);
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.idos.writeIShort(this.bs.fxdParams.PWU[i]);
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.idos.writeIInt(this.bs.fxdParams.DS[i]);
			for (int i = 0; i < this.bs.fxdParams.TPW; i++ )
				this.idos.writeIInt(this.bs.fxdParams.NPPW[i]);
			this.idos.writeIInt(this.bs.fxdParams.GI);
			this.idos.writeIShort(this.bs.fxdParams.BC);
			this.idos.writeIInt(this.bs.fxdParams.NAV);
			this.idos.writeIInt(this.bs.fxdParams.AR);
			this.idos.writeIInt(this.bs.fxdParams.FPO);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.NF);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.NFSF);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.PO);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.LT);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.RT);
			this.idos.writeIUnsignedShort(this.bs.fxdParams.ET);
		}
		catch (IOException e)
			{System.out.println("IO Error writing FxdParams");return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_keyEvents (int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIShort(this.bs.keyEvents.TNKE);
			for (int i = 0; i < this.bs.keyEvents.TNKE; i++)
			{
				this.idos.writeIShort(this.bs.keyEvents.EN[i]);
				this.idos.writeIInt(this.bs.keyEvents.EPT[i]);
				this.idos.writeIShort(this.bs.keyEvents.ACI[i]);
				this.idos.writeIShort(this.bs.keyEvents.EL[i]);
				this.idos.writeIInt(this.bs.keyEvents.ER[i]);
				for (int j = 0; j < 6; j++)
					this.idos.writeIChar(this.bs.keyEvents.EC[i].charAt(j));
				this.idos.writeIChar(this.bs.keyEvents.LMT[i].charAt(0));
				this.idos.writeIChar(this.bs.keyEvents.LMT[i].charAt(1));
				this.idos.writeIString(this.bs.keyEvents.CMT[i]);
			}

			this.idos.writeIInt(this.bs.keyEvents.EEL);
			this.idos.writeIInt(this.bs.keyEvents.ELMP[0]);
			this.idos.writeIInt(this.bs.keyEvents.ELMP[1]);
			this.idos.writeIUnsignedShort(this.bs.keyEvents.ORL);
			this.idos.writeIInt(this.bs.keyEvents.RLMP[0]);
			this.idos.writeIInt(this.bs.keyEvents.RLMP[1]);
		}
		catch (IOException e)
			{System.out.println("IO Error writing KeyEvents");return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_lnkParams(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIShort(this.bs.lnkParams.TNL);
			for (int i = 0; i < this.bs.lnkParams.TNL; i++)
			{
				this.idos.writeIShort(this.bs.lnkParams.LMN[i]);
				this.idos.writeIChar(this.bs.lnkParams.LMC[i].charAt(0));
				this.idos.writeIChar(this.bs.lnkParams.LMC[i].charAt(1));
				this.idos.writeIInt(this.bs.lnkParams.LML[i]);
				this.idos.writeShort(this.bs.lnkParams.REN[i]);
				this.idos.writeIInt(this.bs.lnkParams.GPA[i][0]);
				this.idos.writeIInt(this.bs.lnkParams.GPA[i][1]);
				this.idos.writeIShort(this.bs.lnkParams.FCI[i]);
				this.idos.writeIInt(this.bs.lnkParams.SMI[i]);
				this.idos.writeIInt(this.bs.lnkParams.SML[i]);
				this.idos.writeIChar(this.bs.lnkParams.USML[i].charAt(0));
				this.idos.writeIChar(this.bs.lnkParams.USML[i].charAt(1));
				this.idos.writeIShort(this.bs.lnkParams.MFDL[i]);
				this.idos.writeIString(this.bs.lnkParams.CMT[i]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing LnkParams");return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_dataPts(int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			this.idos.writeIInt(this.bs.dataPts.TNDP);
			this.idos.writeIShort(this.bs.dataPts.TSF);
			for (int i = 0; i < this.bs.dataPts.TSF; i++)
			{
				this.idos.writeIInt(this.bs.dataPts.TPS[i]);
				this.idos.writeIShort(this.bs.dataPts.SF[i]);
				for (int j = 0; j < this.bs.dataPts.TPS[i]; j++)
					this.idos.writeIUnsignedShort(this.bs.dataPts.DSF[i][j]);
			}
		}
		catch (IOException e)
			{System.out.println("IO Error writing DataPts");return 0;} //$NON-NLS-1$
		return 1;
	}

	int write_special (int j, int n)
	{
		if (this.bs.map.B_size[n] == 0)
			return 0;
		try
		{
			for (int i = 0; i < this.bs.special[j].getSize(); i++ )
				this.idos.writeByte(this.bs.special[j].spec_data[i]);
		}
		catch (IOException e)
			{System.out.println("IO Error writing Special Data");return 0;} //$NON-NLS-1$
		return 1;
	}

	/**
	 * @todo Parameter never read.
	 */
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

	/**
	 * @todo Method never read locally.
	 */
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
