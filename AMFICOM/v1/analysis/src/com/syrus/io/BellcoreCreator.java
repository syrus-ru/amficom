/*
 * $Id: BellcoreCreator.java,v 1.2 2005/03/01 14:40:10 saa Exp $
 * Very poor bellcore creator for development purposes
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/03/01 14:40:10 $
 * @module
 */
public class BellcoreCreator
{
	private BellcoreStructure bs = new BellcoreStructure();

	public BellcoreCreator(double []yarr)
	{
		int N = yarr.length;
		int[] y = new int[N];
		for (int i = 0; i < N; i++)
			y[i] = 65535 - (int )(yarr[i] * 1000);
		bs.dataPts = bs.new DataPts();
		bs.dataPts.TNDP = y.length;
		System.out.println("BellcoreCreator: TNDP set to " + bs.dataPts.TNDP);
		bs.dataPts.DSF = new int[][] { y };
		bs.dataPts.TSF = 1;
		bs.dataPts.TPS = new int[] { N };
		bs.dataPts.SF = new short[] { 1000 };
		bs.fxdParams = bs.new FxdParams();
		bs.fxdParams.TPW = 1;
		bs.fxdParams.PWU = new short[] { 0 };
		bs.fxdParams.DS = new int[] { 0 };
		bs.fxdParams.NPPW = new int[] { y.length };
		bs.fxdParams.AR = y.length * 1000 * bs.fxdParams.GI / 3;
		bs.supParams = bs.new SupParams();
		bs.map = bs.new Map();
		bs.map.NB = 4;
		bs.map.B_id = new String[] { "", "FxdParams", "SupParams", "DataPts" };
		bs.map.B_rev = new int[] {0, 0, 0, 0};
		bs.map.B_size = new int[] {
				1,
				bs.fxdParams.getSize(),
				bs.supParams.getSize(),
				bs.dataPts.getSize()
		};
	}

	public BellcoreStructure getBS()
	{
		return bs;
	}	
}
