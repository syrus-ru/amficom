/*
 * $Id: ThreshDX.java,v 1.1 2005/02/21 13:39:33 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/02/21 13:39:33 $
 * @module
 */
public class ThreshDX extends Thresh
{
	protected int[] dX;
	protected boolean isRise;

	protected ThreshDX()
	{
	}

	protected ThreshDX(int eventId, int xMin, int xMax, boolean isRise, int dx)
	{
		super(eventId, eventId, xMin, xMax);
		this.isRise = isRise;
		dX = new int[] { dx, 2 * dx, dx, 2 * dx };
	}

	protected void readSpecificFromDIS(DataInputStream dis) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dX[k] = dis.readInt();
	}
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException
	{
		for (int k = 0; k < 4; k++)
			dos.writeInt(dX[k]);
	}
}
