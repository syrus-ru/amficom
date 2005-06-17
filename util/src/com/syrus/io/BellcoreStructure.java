/*
 * $Id: BellcoreStructure.java,v 1.16 2005/06/17 07:12:06 saa Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.util.Date;

/**
 * @version $Revision: 1.16 $, $Date: 2005/06/17 07:12:06 $
 * @author $Author: saa $
 * @module util
 */
public class BellcoreStructure {
	protected static final String FIELD_NAME_MAP = "Map";
	protected static final String FIELD_NAME_GENPARAMS = "GenParams";
	protected static final String FIELD_NAME_SUPPARAMS = "SupParams";
	protected static final String FIELD_NAME_FXDPARAMS = "FxdParams";
	protected static final String FIELD_NAME_KEYEVENTS = "KeyEvents";
	protected static final String FIELD_NAME_LNKPARAMS = "LnkParams";
	protected static final String FIELD_NAME_DATAPTS = "DataPts";
	protected static final String FIELD_NAME_CKSUM = "Cksum";
	protected static final String FIELD_NAME_HP_MINI_SPECIAL = "HPMiniSpecial";

	static final int MAP = 1;
	static final int GENPARAMS = 2;
	static final int SUPPARAMS = 3;
	static final int FXDPARAMS = 4;
	static final int KEYEVENTS = 5;
	static final int LNKPARAMS = 6;
	static final int DATAPOINTS = 7;
	static final int SPECIAL = 8;
	static final int CKSUM = 9;

	//флаг, показывающий есть ли в файле определенное поле
	private boolean hasMap = false;
	private boolean hasGen = false;
	private boolean hasSup = false;
	private boolean hasFxd = false;
	private boolean hasKey = false;
	private boolean hasLnk = false;
	private boolean hasData = false;
	/**
	 * @todo Field is never read locally.
	 */
	private boolean hasSpecial = false;
	private int blocks = 0; // общее число полей
	int specials = 0; // число полей specials (может быть произвольным)

	// экземпляры классов, представляющих собой поля данных в формате bellcore
	Map map;
	GenParams genParams;
	SupParams supParams;
	FxdParams fxdParams;
	KeyEvents keyEvents;
	LnkParams lnkParams;
	DataPts dataPts;
	Cksum cksum;
	Special[] special;

	public String title = "";
	public String schemePathId;
	public String measurementId;
	public String monitoredElementId;

	void addField(int type) {
		switch (type) {
			case MAP: // required
				if (!this.hasMap) {
					this.map = new Map();
					this.hasMap = true;
					this.blocks++;
				}
				break;
			case GENPARAMS: // required
				if (!this.hasGen) {
					this.genParams = new GenParams();
					this.hasGen = true;
					this.blocks++;
				}
				break;
			case SUPPARAMS: // required
				if (!this.hasSup) {
					this.supParams = new SupParams();
					this.hasSup = true;
					this.blocks++;
				}
				break;
			case FXDPARAMS: // required
				if (!this.hasFxd) {
					this.fxdParams = new FxdParams();
					this.hasFxd = true;
					this.blocks++;
				}
				break;
			case KEYEVENTS:
				if (!this.hasKey) {
					this.keyEvents = new KeyEvents();
					this.hasKey = true;
					this.blocks++;
				}
				break;
			case LNKPARAMS:
				if (!this.hasLnk) {
					this.lnkParams = new LnkParams();
					this.hasLnk = true;
					this.blocks++;
				}
				break;
			case DATAPOINTS:
				if (!this.hasData) {
					this.dataPts = new DataPts();
					this.hasData = true;
					this.blocks++;
				}
				break;
			case SPECIAL:
				this.special[this.specials++] = new Special();
				this.hasSpecial = true;
				this.blocks++;
				break;
			case CKSUM: // required
				if (!this.hasMap) {
					this.cksum = new Cksum();
					this.blocks++;
				}
		}
	}

	// ------------------ Map -------------------//
	class Map {
		public int mrn = 0; // Map Revision Number
		public int mbs = 0; // Map Block Size (in bytes)
		public short nb = 1; // Number of Blocks (including Map)
		// Block Info:
		public String[] bId; // Block ID
		public int[] bRev; // Block Revision Number
		public int[] bSize; // Block Size (in bytes)

		public int getSize() {
			int size = 8;
			for (int i = 1; i < this.nb; i++)
				size = size + this.bId[i].length() + 7;
			return size;
		}
	}

	// ----------- General Parameters ------------//
	class GenParams {
		public String lc = "EN"; // Language Code (2 bytes)
		public String cid = " "; // Cable ID
		public String fid = " "; // Fiber ID
		public short ft = 652; // Fiber Type
		public short nw = 1310; // Nominal Wavelength
		public String ol = " "; // Originating Location
		public String tl = " "; // Terminating Location
		public String ccd = " "; // Cable Code
		public String cdf = "BC"; // Current Data Flag (2 bytes)
		public int uo = 0; // User Offset
		public int uod = 0; // User Offset Distance
		public String op = " "; // Operator
		public String cmt = " "; // Comment

		public int getSize() {
			return (10
					+ 7
					+ this.cid.length()
					+ this.fid.length()
					+ this.ol.length()
					+ this.tl.length()
					//+ 6
					+ this.ccd.length()
					+ this.op.length() + this.cmt.length());
		}
	}

	// ----------- Supplier Parameters ------------//
	class SupParams {
		public String sn = " "; // Supplier Name
		public String mfid = " "; // OTDR Mainframe ID
		public String otdr = " "; // OTDR Mainframe SerNum
		public String omid = " "; // Optical Module ID
		public String omsn = " "; // Optical Module SerNum
		public String sr = " "; // Software Revision
		public String ot = " "; // Other

		public int getSize() {
			return (7
					+ this.sn.length()
					+ this.mfid.length()
					+ this.otdr.length()
					+ this.omid.length()
					+ this.omsn.length()
					+ this.sr.length() + this.ot.length());
		}
	}

	// ----------- Fixed Parameters ------------//
	class FxdParams {
		public long dts = 0; // Date/Time Stamp (in ms)
		public String ud = "km"; // Units of Distanse (2 bytes)
		public short aw = 13100; // Actual Wavelength
		public int ao = 0; // Acquision Offset
		public int aod = 0; // Acquision Offset Distance
		public short tpw = 0; // Total Number of Pulse Width Used
		public short[] pwu; // Pulse Width Used
		public int[] ds; // Data Spacing
		public int[] nppw; // Number of Data Points for Each Pulse Width
		public int gi = 146800; // Group Index
		public short bc = 800; // Backscatter Coefficient
		public int nav; // Number of Averages
		public int at; // Averaging Time --- ??
		public int ar; // Acquision Range
		public int ard; // Acquision Range Distance
		public int fpo = 0; // Front Panel Offset
		public int nf = 40000; // Noise Floor Level
		public int nfsf = 1000; // Noise Floor Scale Factor
		public int po = 0; // Power Offset First Point
		public int lt = 200; // Loss Threshold
		public int rt = 40000; // Reflectance Threshold
		public int et = 3000; // End-of-Fiber Threshold
		public String tt = "ST"; // Trace Type -- ??
		public int wc[] = new int[4]; // Window Coordinates

		public int getSize() {
			return (44 + this.tpw * 10);
		}
	}

	// ----------- Key Events ------------//
	class KeyEvents {
		public short tnke; // Number of Key Events
		public short[] en; // Event Number
		public int[] ept; // Event Propagation Time
		public short[] aci; // Attenuation Coefficient Lead-in-Fiber
		public short[] el; // Event Loss
		public int[] er; // Event Reflectance
		public String[] ec; // Event Code (6 bytes)
		public String[] lmt; // Loss Measurement Event (2bytes)
		public String[] cmt; // Comment
		public int eel = 0; // End-to-End Loss
		public int elmp[] = new int[2]; // End-toEnd Marker Positions
		public int orl; // Optical Return Loss
		public int rlmp[] = new int[2]; // Optical Return Loss Marker Positions

		public int getSize() {
			int size = 26;
			for (int i = 0; i < this.tnke; i++)
				size = size + 22 + this.cmt[i].length() + 1;
			return size;
		}
	}

	// ----------- Link Parameters ------------//
	class LnkParams {
		public short tnl; // Total Number of Landmarks
		public short[] lmn; // Landmark Number
		public String[] lmc; // Landmark Code (2 bytes)
		public int[] lml; // Landmark Location
		public short[] ren; // Related Event Number
		public int[][] gpa; // GPS Info - longitude, latitude (2 ints)
		public short[] fci; // Fiber Correction Factor Lead-in-Fiber
		public int[] smi; // Stealth Marker Entering Landmark
		public int[] sml; // Stealth Marker Leaving Landmark
		public String[] usml; // Units of Stealth Marker Leaving Landmark (2 bytes)
		public short[] mfdl; // Mode Field Diameter Leaving Landmark
		public String[] cmt; // Comment

		public int getSize() {
			int size = 2;
			for (int i = 0; i < this.tnl; i++)
				size = size + 32 + this.cmt[i].length() + 1;
			return size;
		}
	}

	// ----------- Data Points ------------//
	class DataPts {
		public int tndp; // Number of Data Points
		public short tsf; // Total number Scale Factor Used
		public int tps[]; // Total Data Points Using Scale Factor i
		public short sf[]; // Scale Factor i
		public int dsf[][]; // Data

		public int getSize() {
			int size = 6 + this.tsf * 6 + this.tndp * 2;
			// for (int i = 0; i < tsf; i++)
			// size += tps[i] * 2;
			return size;
		}
	}

	// ----------- Checksum ------------//
	class Cksum {
		public int csm; // Checksum

		public int getSize() {
			return 2;
		}
	}

	// ----------- Special Field ------------//
	class Special {
		// public int size; // Size Of Special Field
		public byte[] specData; // Special Data Field

		public int getSize() {
			return this.specData.length;
		}
	}

	public String getOpticalModuleId() {
		return this.supParams.omid;
	}

	public int getPulsewidth() {
		return this.fxdParams.pwu[0];
	}

	public int getAverages() {
		return this.fxdParams.nav;
	}

	public double getBackscatter() {
		return -(double) this.fxdParams.bc / 10d;
	}

	public Date getDate() {
		return new Date(this.fxdParams.dts * 1000);
	}

	public String getUnits() {
		return this.fxdParams.ud;
	}

	public int getWavelength() {
		return this.fxdParams.aw / 10;
	}

	public double getIOR() {
		return this.fxdParams.gi / 100000d;
	}

	/**
	 * Gives resolution (sample spacing) measured in meters.
	 * Generally, gives relative accuracy better than 1e-5.
	 * @return Data resolution, m.
	 */
	public double getResolution() {
		// if BS contains info on data spacing, return that
		if (this.fxdParams.tpw > 0)
		{
			double res = this.fxdParams.ds[0] * 1e-14 * 3e8
				/ (this.fxdParams.gi * 1e-5);
			if (res > 0)
				return res;
		}
		// otherwise, guess it based on total length
		int n = this.dataPts.tndp;
		double res = (this.fxdParams.ar - this.fxdParams.ao) * 3d
			/ ((double) n * (double) this.fxdParams.gi / 1000d);
		return res;
	}

	/**
	 * @return trace range, km
	 */
	public double getRange() {
		return (this.fxdParams.ar - this.fxdParams.ao) * 3d / this.fxdParams.gi;
	}

	public double[] getTraceData() {
		int n = this.dataPts.tndp;
		double[] y = new double[n];

		for (int i = 0; i < this.dataPts.tps[0]; i++)
			y[i] = (65535 - this.dataPts.dsf[0][i]) / 1000d;

		correctReflectogramm1(y);
		shiftTraceByAbsMax(y);
		return y;
	}

	// корректируем мин. значения и первые две точки рефлектограммы
	private void correctReflectogramm1(double[] data) {
		if (data.length < 3)
			return;

		int begin = 300;
		if (begin > data.length / 2)
			begin = data.length / 2;

		double min = data[begin];

		for (int i = begin; i < data.length; i++)
			if (data[i] < min)
				min = data[i];

		if (min != 0)
			for (int i = 0; i < data.length; i++)
				data[i] = data[i] - min;

		for (int i = 0; i <= begin; i++)
			if (data[i] < 0.)
				data[i] = 0.;

		if (data[0] > 0.001)
			data[0] = 0.;

		if (data[1] < 0.001)
			data[1] = data[2] / 2.;
	}

	/**
	 * Выравниваем р/г по абс. максимуму (т.к. считаем его уровнем мертвой зоны).
	 * Это необходимо для нынешнего (june 2005) анализа - по крайней мере для
	 * построения эталона. Код анализа, отображающий р/г на экран,
	 * сам проводит выравнивание, которое, с введением настоящей процедуры
	 * становится излишним.
	 * @todo Привести выравнивание в порядок, одним из трех способов:
	 * <ol>
	 * <li>оставить только настоящее выравнивание, убрав все остальное
	 * выравнивание из анализа как излишнее
	 * (самый прямолинейный и надежный вариант),
	 * <li>сделать унифицированное выравнивание в анализе (фактически,
	 * сделать надстойку над BellcoreStructure и перенести туда
	 * настоящую процедуру),
	 * <li>сделать выравнивание "по месту" - зависящую от того, где она
	 * требуется. Это наиболее гибкий, но наиболее сложный и наименее трудно
	 * контролируемый вариант - однако он может понадобиться, если
	 * выравнивание по мертвой зоне окажется неадекватным - скажем, в случае
	 * отсутствия мертвой зоны.
	 * </ol>
	 */
	private void shiftTraceByAbsMax(double[] data) {
		if (data.length == 0) {
			return;
		}
		// определяем абс. макс.
		double vMax = data[0];
		for (int i = 1; i < data.length; i++) {
			if (vMax < data[i]) {
				vMax = data[i];
			}
		}
		// System.err.println("correctReflectogramm2: vMax = " + vMax);
		// смещаем
		for (int i = 0; i < data.length; i++) {
			data[i] -= vMax;
		}
	}
}
