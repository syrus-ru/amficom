/*
 * $Id: ThreshDY.java,v 1.2 2005/03/09 10:49:50 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/03/09 10:49:50 $
 * @module
 */
public class ThreshDY extends Thresh
{
	private static final double VALUE_GRID = 0.001;
	private boolean typeL; // 0: dA, 1: dL
	private double[] values; // dA or dL values

	protected ThreshDY()
	{
	}

	protected ThreshDY(int eventId, boolean typeL, int xMin, int xMax)
	{
		super(eventId, eventId, xMin, xMax);
		this.typeL = typeL;
		//this.values = new double[] { 0.1, 0.2, -0.1, -0.2 }; // defaults -- XXX
		this.values = new double[] { 0, 0, -0, -0 }; // FIXME: debug
	}
	
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		values = new double[4];
		for (int k = 0; k < 4; k++)
			values[k] = dis.readDouble();
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeDouble(values[k]);
	}

	protected double getDY(int n)
	{
		return values[n];
	}
	protected boolean getTypeL()
	{
		return typeL;
	}
	protected void setDY(int n, double val)
	{
		if (VALUE_GRID > 0)
			val = Math.rint(val / VALUE_GRID) * VALUE_GRID;
		values[n] = val;
	}
}
