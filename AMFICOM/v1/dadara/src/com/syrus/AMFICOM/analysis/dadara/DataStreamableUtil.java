/*-
 * $Id: DataStreamableUtil.java,v 1.5 2005/05/01 06:12:58 saa Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/05/01 06:12:58 $
 * @module
 */
public class DataStreamableUtil
{
    private DataStreamableUtil() {
        // non-instantiable
    }

	public static DataStreamable readDataStreamableFromBA(byte[] bar,
            DataStreamable.Reader reader) throws DataFormatException
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			return reader.readFromDIS(dis);
		}
		catch (IOException e)
		{
            throw new DataFormatException(e.toString());
		}
		// do not catch (SignatureMismatchException e)
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
