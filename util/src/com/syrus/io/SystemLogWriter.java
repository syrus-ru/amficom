/*
 * $Id: SystemLogWriter.java,v 1.3 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SystemLogWriter
{
	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss"); //$NON-NLS-1$
	static FileOutputStream fos;
	static PrintStream pstr;
	static PrintWriter pwr;

	static
	{
		try
		{
			fos = new FileOutputStream(".\\client.log", true); //$NON-NLS-1$
//			pwr = new PrintWriter(fos);
			pstr = new PrintStream(fos);
			System.setOut(pstr);
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT CREATE CLIENT LOG FILE: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
		try
		{
			System.out.println(""); //$NON-NLS-1$
			System.out.println("-------------------------------------------------------"); //$NON-NLS-1$
			System.out.println("started " + sdf.format(new Date(System.currentTimeMillis()))); //$NON-NLS-1$
			System.out.println("-------------------------------------------------------"); //$NON-NLS-1$
			System.out.println(""); //$NON-NLS-1$
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT WRITE CLIENT LOG FILE: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	private SystemLogWriter()
	{
		assert false;
	}

	private static boolean initiated = false;
	
	public static void initialize()
	{
		if(initiated)
			return;

		initiated = true;
	}

	public static void closeLog()
	{
		try
		{
			System.out.println(""); //$NON-NLS-1$
			System.out.println("-------------------------------------------------------"); //$NON-NLS-1$
			System.out.println("closed " + sdf.format(new Date(System.currentTimeMillis()))); //$NON-NLS-1$
			System.out.println("-------------------------------------------------------"); //$NON-NLS-1$
			System.out.println(""); //$NON-NLS-1$
			System.out.close();
			try
			{
				fos.close();
			}
			catch(IOException ex)
			{
				// empty
			}
		}
		catch(Exception e)
		{
			// empty
		}
	}
}
