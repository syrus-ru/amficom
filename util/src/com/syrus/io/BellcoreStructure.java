/*
 * $Id: BellcoreStructure.java,v 1.6 2004/10/18 14:01:56 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.io;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.6 $, $Date: 2004/10/18 14:01:56 $
 * @author $Author: stas $
 * @module util
 */
public class BellcoreStructure // extends ObjectResource
{
	public static final int MAP = 1;
	public static final int GENPARAMS = 2;
	public static final int SUPPARAMS = 3;
	public static final int FXDPARAMS = 4;
	public static final int KEYEVENTS = 5;
	public static final int LNKPARAMS = 6;
	public static final int DATAPOINTS = 7;
	public static final int SPECIAL = 8;
	public static final int CKSUM = 9;

	// флаг, показывающий есть ли в файле определенное поле
	public boolean hasMap = false;
	public boolean hasGen = false;
	public boolean hasSup = false;
	public boolean hasFxd = false;
	public boolean hasKey = false;
	public boolean hasLnk = false;
	public boolean hasData = false;
	public boolean hasSpecial = false;
	public int specials = 0; // число полей specials (может быть произвольным)
	int blocks = 0;          // общее число полей

	// экземпляры классов, представляющих собой поля данных в формате bellcore
	public Map map;
	public GenParams genParams;
	public SupParams supParams;
	public FxdParams fxdParams;
	public KeyEvents keyEvents;
	public LnkParams lnkParams;
	public DataPts dataPts;
	public Cksum cksum;
	public Special[] special;

	public String title = "";
	public String schemePathId;
	public Identifier measurementId;
	public Identifier monitoredElementId;

	public void addField (int type)
	{
		switch (type)
		{
			case MAP:  // required
					 if (!this.hasMap) { this.map = new Map(); this.hasMap = true; this.blocks++; }
					 break;
			case GENPARAMS:  // required
					 if (!this.hasGen) { this.genParams = new GenParams(); this.hasGen = true; this.blocks++; }
					 break;
			case SUPPARAMS:  // required
					 if (!this.hasSup) { this.supParams = new SupParams(); this.hasSup = true; this.blocks++; }
					 break;
			case FXDPARAMS:  // required
					 if (!this.hasFxd) { this.fxdParams = new FxdParams(); this.hasFxd = true; this.blocks++; }
					 break;
			case KEYEVENTS:
					 if (!this.hasKey) { this.keyEvents = new KeyEvents(); this.hasKey = true; this.blocks++; }
					 break;
			case LNKPARAMS:
					 if (!this.hasLnk) { this.lnkParams = new LnkParams(); this.hasLnk = true; this.blocks++; }
					 break;
			case DATAPOINTS:
					 if (!this.hasData) { this.dataPts = new DataPts(); this.hasData = true; this.blocks++; }
					 break;
			case SPECIAL:
					 this.special[this.specials++] = new Special(); this.hasSpecial = true; this.blocks++;
					 break;
			case CKSUM: // required
					 if (!this.hasMap) { this.cksum = new Cksum();  this.blocks++; }
		}
	}

	//------------------ Map -------------------//
	public class Map
	{
		public int MRN = 0;          // Map Revision Number
		public int MBS = 0;          // Map Block Size (in bytes)
		public short NB = 1;         // Number of Blocks (including Map)
														 // Block Info:
		public String[] B_id;    // Block ID
		public int[] B_rev;      // Block Revision Number
		public int[] B_size;     // Block Size (in bytes)

		public int getSize()
		{
			int size = 8;
			for (int i = 1; i < this.NB; i++)
				size = size + this.B_id[i].length() + 7;
			return size;
		}
	}

	//----------- General Parameters ------------//
	public class GenParams
	{
		public String LC = "EN";       // Language Code (2 bytes)
		public String CID = " ";       // Cable ID
		public String FID = " ";       // Fiber ID
		public short FT = 652;         // Fiber Type
		public short NW = 1310;        // Nominal Wavelength
		public String OL = " ";        // Originating Location
		public String TL = " ";        // Terminating Location
		public String CCD = " ";       // Cable Code
		public String CDF = "BC";      // Current Data Flag (2 bytes)
		public int UO = 0;             // User Offset
		public int UOD = 0;            // User Offset Distance
		public String OP = " ";        // Operator
		public String CMT = " ";       // Comment

		public int getSize()
		{
			return (10 + 7 + this.CID.length() + this.FID.length() + this.OL.length() + this.TL.length()// + 6
						 + this.CCD.length() + this.OP.length() + this.CMT.length());
		}
	}

	//----------- Supplier Parameters ------------//
	public class SupParams
	{
		public String SN = " ";          // Supplier Name
		public String MFID = " ";        // OTDR Mainframe ID
		public String OTDR = " ";        // OTDR Mainframe SerNum
		public String OMID = " ";        // Optical Module ID
		public String OMSN = " ";        // Optical Module SerNum
		public String SR = " ";          // Software Revision
		public String OT = " ";          // Other

		public int getSize()
		{
			return (7 + this.SN.length() + this.MFID.length() + this.OTDR.length() + this.OMID.length()
						 + this.OMSN.length() + this.SR.length() + this.OT.length());
		}
	}

	//----------- Fixed Parameters ------------//
	public class FxdParams
	{
		public long DTS = 0;           // Date/Time Stamp (in ms)
		public String UD = "km";          // Units of Distanse (2 bytes)
		public short AW = 13100;           // Actual Wavelength
		public int AO = 0;             // Acquision Offset
		public int AOD = 0;            // Acquision Offset Distance
		public short TPW = 0;          // Total Number of Pulse Width Used
		public short[] PWU;        // Pulse Width Used
		public int[] DS;           // Data Spacing
		public int[] NPPW;         // Number of Data Points for Each Pulse Width
		public int GI = 146800;             // Group Index
		public short BC = 800;           // Backscatter Coefficient
		public int NAV;            // Number of Averages
		public int AT;             // Averaging Time --- ??
		public int AR;             // Acquision Range
		public int ARD;            // Acquision Range Distance
		public int FPO = 0;            // Front Panel Offset
		public int NF = 40000;             // Noise Floor Level
		public int NFSF = 1000;           // Noise Floor Scale Factor
		public int PO = 0;             // Power Offset First Point
		public int LT = 200;             // Loss Threshold
		public int RT = 40000;             // Reflectance Threshold
		public int ET = 3000;             // End-of-Fiber Threshold
		public String TT = "ST";          // Trace Type -- ??
		public int WC[] = new int[4]; // Window Coordinates

		public int getSize()
		{
			return (44 + this.TPW * 10);
		}
	}

	//----------- Key Events ------------//
	public class KeyEvents
	{
		public short TNKE;         // Number of Key Events
		public short[] EN;         // Event Number
		public int[] EPT;          // Event Propagation Time
		public short[] ACI;        // Attenuation Coefficient Lead-in-Fiber
		public short[] EL;         // Event Loss
		public int[] ER;           // Event Reflectance
		public String[] EC;        // Event Code (6 bytes)
		public String[] LMT;       // Loss Measurement Event (2bytes)
		public String[] CMT;       // Comment
		public int EEL = 0;            // End-to-End Loss
		public int ELMP[] = new int[2]; // End-toEnd Marker Positions
		public int ORL;            // Optical Return Loss
		public int RLMP[] = new int[2]; // Optical Return Loss Marker Positions

		public int getSize()
		{
			int size = 26;
			for (int i = 0; i < this.TNKE; i++)
					size = size + 22 + this.CMT[i].length() + 1;
			return size;
		}
	}

	//----------- Link Parameters ------------//
	public class LnkParams
	{
		public short TNL;          // Total Number of Landmarks
		public short[] LMN;        // Landmark Number
		public String[] LMC;       // Landmark Code (2 bytes)
		public int[] LML;          // Landmark Location
		public short[] REN;        // Related Event Number
		public int[][] GPA;        // GPS Info - longitude, latitude (2 ints)
		public short[] FCI;        // Fiber Correction Factor Lead-in-Fiber
		public int[] SMI;          // Stealth Marker Entering Landmark
		public int[] SML;          // Stealth Marker Leaving Landmark
		public String[] USML;      // Units of Stealth Marker Leaving Landmark (2 bytes)
		public short[] MFDL;       // Mode Field Diameter Leaving Landmark
		public String[] CMT;       // Comment

		public int getSize()
		{
			int size = 2;
			for (int i = 0; i < this.TNL; i++)
					size = size + 32 + this.CMT[i].length() + 1;
			return size;
		}
	}

	//----------- Data Points ------------//
	public class DataPts
	{
		public int TNDP;           // Number of Data Points
		public short TSF;          // Total number Scale Factor Used
		public int TPS[];          // Total Data Points Using Scale Factor i
		public short SF[];         // Scale Factor i
		public int DSF[][];        // Data

		public int getSize()
		{
			int size = 6 + this.TSF * 6 + this.TNDP * 2;
			//for (int i = 0; i < TSF; i++)
				//	size += TPS[i] * 2;
			return size;
		}
	}

	//----------- Checksum ------------//
	public class Cksum
	{
		public int CSM;            // Checksum

		public int getSize()
		{
			return 2;
		}
	}

	//----------- Special Field ------------//
	public class Special
	{
		//public int size;           // Size Of Special Field
		public byte [] spec_data;  // Special Data Field

		public int getSize()
		{
			return this.spec_data.length;
		}
	}

	public double getDeltaX()
	{
		int n = this.dataPts.TNDP;
		return (this.fxdParams.AR - this.fxdParams.AO) * 3d / ((double)n * (double)this.fxdParams.GI/1000d);
	}

	public double[] getTraceData()
	{
		int n = this.dataPts.TNDP;
		double[] y = new double[n];

		for (int i = 0; i < this.dataPts.TPS[0]; i++)
			y[i] = (65535 - this.dataPts.DSF[0][i])/1000d;

		correctReflectogramm(y);
		return y;
	}

	private void correctReflectogramm(double []data)
	{
		int begin = 300;
		if(begin > data.length / 2)
			begin = data.length / 2;

		double min = data[begin];

		for(int i = begin; i < data.length; i++)
			if(data[i] < min)
				min = data[i];

		if (min != 0)
			for(int i = 0; i < data.length; i++)
				data[i] = data[i] - min;

		for(int i = 0; i <= begin; i++)
			if(data[i] < 0.)
				data[i] = 0.;

		if(data[0] > 0.001)
			data[0] = 0.;

		if(data[1] < 0.001)
			data[1] = data[2] / 2.;
	}
}
