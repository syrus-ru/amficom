/*
 * $Id: BellcoreCreator.java,v 1.3 2005/03/17 08:49:04 saa Exp $
 * Very poor bellcore creator for development purposes
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/03/17 08:49:04 $
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
		bs.dataPts.tndp = y.length;
		System.out.println("BellcoreCreator: TNDP set to " + bs.dataPts.tndp);
		bs.dataPts.dsf = new int[][] { y };
		bs.dataPts.tsf = 1;
		bs.dataPts.tps = new int[] { N };
		bs.dataPts.sf = new short[] { 1000 };
		bs.fxdParams = bs.new FxdParams();
		bs.fxdParams.tpw = 1;
		bs.fxdParams.pwu = new short[] { 0 };
		bs.fxdParams.ds = new int[] { 0 };
		bs.fxdParams.nppw = new int[] { y.length };
		bs.fxdParams.ar = y.length * 1000 * bs.fxdParams.gi / 3;
		bs.supParams = bs.new SupParams();
		bs.map = bs.new Map();
		bs.map.nb = 4;
		bs.map.bId = new String[] { "", "FxdParams", "SupParams", "DataPts" };
		bs.map.bRev = new int[] {0, 0, 0, 0};
		bs.map.bSize = new int[] {
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
