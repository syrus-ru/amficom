package com.syrus.io;

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

	// экземпл€ры классов, представл€ющих собой пол€ данных в формате bellcore
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
	public String test_setup_id = "";
	public String monitored_element_id = "";

	public void addField (int type)
	{
		switch (type)
		{
			case MAP:  // required
					 if (!hasMap) { map = new Map(); hasMap = true; blocks++; }
					 break;
			case GENPARAMS:  // required
					 if (!hasGen) { genParams = new GenParams(); hasGen = true; blocks++; }
					 break;
			case SUPPARAMS:  // required
					 if (!hasSup) { supParams = new SupParams(); hasSup = true; blocks++; }
					 break;
			case FXDPARAMS:  // required
					 if (!hasFxd) { fxdParams = new FxdParams(); hasFxd = true; blocks++; }
					 break;
			case KEYEVENTS:
					 if (!hasKey) { keyEvents = new KeyEvents(); hasKey = true; blocks++; }
					 break;
			case LNKPARAMS:
					 if (!hasLnk) { lnkParams = new LnkParams(); hasLnk = true; blocks++; }
					 break;
			case DATAPOINTS:
					 if (!hasData) { dataPts = new DataPts(); hasData = true; blocks++; }
					 break;
			case SPECIAL:
					 special[specials++] = new Special(); hasSpecial = true; blocks++;
					 break;
			case CKSUM: // required
					 if (!hasMap) { cksum = new Cksum();  blocks++; }
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
			for (int i = 1; i < NB; i++)
				size = size + B_id[i].length() + 7;
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
			return (10 + 7 + CID.length() + FID.length() + OL.length() + TL.length()// + 6
						 + CCD.length() + OP.length() + CMT.length());
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
			return (7 + SN.length() + MFID.length() + OTDR.length() + OMID.length()
						 + OMSN.length() + SR.length() + OT.length());
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
			return (44 + TPW * 10);
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
			for (int i = 0; i < TNKE; i++)
					size = size + 22 + CMT[i].length() + 1;
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
			for (int i = 0; i < TNL; i++)
					size = size + 32 + CMT[i].length() + 1;
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
			int size = 6 + TSF * 6 + TNDP * 2;
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
			return spec_data.length;
		}
	}
}
