package com.syrus.io;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SystemLogWriter
{
	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	static FileOutputStream fos;
	static PrintStream pstr;
	static PrintWriter pwr;

	static
	{
		try
		{
			fos = new FileOutputStream(".\\client.log", true);
//			pwr = new PrintWriter(fos);
			pstr = new PrintStream(fos);
			System.setOut(pstr);
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT CREATE CLIENT LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
		try
		{
			System.out.println("");
			System.out.println("-------------------------------------------------------");
			System.out.println("started " + sdf.format(new Date(System.currentTimeMillis())));
			System.out.println("-------------------------------------------------------");
			System.out.println("");
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT WRITE CLIENT LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private SystemLogWriter()
	{
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
			System.out.println("");
			System.out.println("-------------------------------------------------------");
			System.out.println("closed " + sdf.format(new Date(System.currentTimeMillis())));
			System.out.println("-------------------------------------------------------");
			System.out.println("");
			System.out.close();
			try
			{
				fos.close();
			}
			catch(IOException ex)
			{
				;
			}
		}
		catch(Exception e)
		{
			;
		}
	}
}
