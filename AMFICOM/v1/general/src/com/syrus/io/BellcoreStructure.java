/*
 * $Id: BellcoreStructure.java,v 1.3 2004/10/29 09:39:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.io;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.3 $, $Date: 2004/10/29 09:39:31 $
 * @author $Author: max $
 * @module general_v1
 */
public class BellcoreStructure // extends ObjectResource
{
	static final int MAP = 1;
	static final int GENPARAMS = 2;
	static final int SUPPARAMS = 3;
	static final int FXDPARAMS = 4;
	static final int KEYEVENTS = 5;
	static final int LNKPARAMS = 6;
	static final int DATAPOINTS = 7;
	static final int SPECIAL = 8;
	static final int CKSUM = 9;

	// флаг, показывающий есть ли в файле определенное поле
	private boolean hasMap = false;
	private boolean hasGen = false;
	private boolean hasSup = false;
	private boolean hasFxd = false;
	private boolean hasKey = false;
	private boolean hasLnk = false;
	private boolean hasData = false;
	private boolean hasSpecial = false;
	private int blocks = 0;          // общее число полей
	int specials = 0; // число полей specials (может быть произвольным)


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

	void addField (int type)
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
	class Map
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
	class GenParams
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
	class SupParams
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
	class FxdParams
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
	class KeyEvents
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
	class LnkParams
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
	class DataPts
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
	class Cksum
	{
		public int CSM;            // Checksum

		public int getSize()
		{
			return 2;
		}
	}

	//----------- Special Field ------------//
	class Special
	{
		//public int size;           // Size Of Special Field
		public byte [] spec_data;  // Special Data Field

		public int getSize()
		{
			return this.spec_data.length;
		}
	}

	public String getOpticalModuleId()
	{
		return supParams.OMID;
	}

	public int getPulsewidth()
	{
		return fxdParams.PWU[0];
	}

	public int getAverages()
	{
		return fxdParams.NAV;
	}

	public double getBackscatter()
	{
		return -(double)fxdParams.BC / 10d;
	}

	public Date getDate()
	{
		return new Date(fxdParams.DTS * 1000);
	}

	public String getUnits()
	{
		return fxdParams.UD;
	}

	public int getWavelength()
	{
		return fxdParams.AW / 10;
	}

	public double getIOR()
	{
		return ((double)fxdParams.GI) / 100000d;
	}

	public double getResolution()
	{
		int n = this.dataPts.TNDP;
		return (this.fxdParams.AR - this.fxdParams.AO) * 3d / ((double)n * (double)this.fxdParams.GI/1000d);
	}

	public double getRange()
	{
		return (double)(fxdParams.AR - fxdParams.AO) * 3d / (double)fxdParams.GI * 1000;
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
