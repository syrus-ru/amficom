/*-
 * $Id: DataStreamableUtil.java,v 1.3 2005/04/30 10:04:07 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/04/30 10:04:07 $
 * @module
 */
public class DataStreamableUtil
{
	public static DataStreamable readDataStreamableFromBA(byte[] bar,
            DataStreamable.Reader reader)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			return reader.readFromDIS(dis);
		}
		catch (IOException e)
		{
			// FIXME: IOException - what to do?
			// we should not catch exceptions here?
			System.out.println("IOException caught, wanna die: " + e);
			e.printStackTrace();
			return null;
		}
		catch (SignatureMismatchException e)
		{
            // FIXME: SignatureMismatchException - what to do?
			System.out.println("SignatureMismatchException caught, wanna die: " + e);
			e.printStackTrace();
			return null;
            //return new ModelTraceManager(new ReflectogramEvent[0]); // ???
		}
	}
	public static byte[] writeDataStreamableToBA(DataStreamable obj)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			obj.writeToDOS(dos);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException e)
		{
            throw new InternalError("Unexpected exception: " + e.getMessage());
		}
	}
}
