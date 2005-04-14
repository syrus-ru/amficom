/*-
 * $Id: DataStreamableUtil.java,v 1.2 2005/04/14 16:21:29 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/14 16:21:29 $
 * @module
 */
public class DataStreamableUtil
{
	public static DataStreamable readDataStreamableFromBA(byte[] bar, DataStreamable.Reader reader)
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
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new byte[0]; //null // @todo: throw runtimeException instead
		}
	}
}
