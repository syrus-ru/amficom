package com.syrus.AMFICOM.server;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public final class ServerTrafficReporter
{
	static public SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	static FileOutputStream fos;
	static PrintWriter pstr;

	static boolean firstin;
	static boolean firstout;

	static int incount;
	static int outcount;

	public static void startLog()
	{
		openLog();
		try
		{
			pstr.println("");
			pstr.println("-------------------------------------------------------");
			pstr.println("started " + sdf.format(new Date(System.currentTimeMillis())));
			pstr.println("-------------------------------------------------------");
			pstr.println("");
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT WRITE TRAFFIC LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
		closeLog();
	}

	public static void openLog()
	{

//	static
//  	{
		try
		{
//			System.out.println("OPEN TRAFFIC LOG FILE ");
//			oracle.aurora.rdbms.security.PolicyTableManager.grant("AMFICOM","SYS:java.io.FilePermission","d:\\traffic.log","write");
			fos = new FileOutputStream("d:\\traffic.log", true);
			pstr = new PrintWriter(fos);
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT CREATE TRAFFIC LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private ServerTrafficReporter()
	{
	}

	static public void fnReport(String fnname)
	{
		openLog();
		firstin = true;
		firstout = true;
		incount = 0;
		outcount = 0;
		try
		{
			pstr.println("-------------------------------------------------------");
			pstr.println("at " + sdf.format(new Date(System.currentTimeMillis())) + " call for " + fnname + ":");
		}
		catch(Exception e)
		{
			System.out.println("-------------------------------------------------------");
			System.out.println("at " + sdf.format(new Date(System.currentTimeMillis())) + " call for " + fnname + ":");
		}
		closeLog();
	}

	static public void fnInReport(String paramname, Object obj)
	{
		int count = 0;
		fnInReport(paramname, count);
	}

	static public void fnStrReport(String text)
	{
		try
		{
			pstr.println("		" + text);
		}
		catch(Exception e)
		{
		}
	}

	static public void fnInReport(String paramname, int count)
	{
		openLog();
		incount += count;
		try
		{
			if(firstin)
				pstr.println("	in parameters:");
			pstr.println("		" + paramname + " --- " + count);
		}
		catch(Exception e)
		{
			if(firstin)
				System.out.println("	in parameters:");
			System.out.println("		" + paramname + " --- " + count);
		}
		if(firstin)
			firstin = false;
		closeLog();
	}

	static public void fnOutReport(String paramname, Object obj)
	{
		int count = 0;
		fnOutReport(paramname, count);
	}

	static public void fnOutReport(String paramname, int count)
	{
		openLog();
		outcount += count;
		try
		{
			if(!firstin)
				pstr.println("	in total			" + incount);
			if(firstout)
				pstr.println("	out parameters:");
			pstr.println("		" + paramname + " --- " + count);
		}
		catch(Exception e)
		{
			if(!firstin)
				System.out.println("	in total			" + incount);
			if(firstout)
				System.out.println("	out parameters:");
			System.out.println("		" + paramname + " --- " + count);
		}
		if(!firstin)
			firstin = true;
		if(firstout)
			firstout = false;
		closeLog();
	}

	static public void fnReportEnd()
	{
		openLog();
		try
		{
			if(!firstout)
				pstr.println("	out total			" + outcount);
		}
		catch(Exception e)
		{
			if(!firstout)
				System.out.println("	out total			" + outcount);
		}
		if(!firstout)
			firstout = true;
		try
		{
			pstr.flush();
			fos.flush();
		}
		catch(Exception ex)
		{
			System.out.println("flush traffic log: " + ex.getMessage());
		}
		closeLog();
	}

	static public void stopLog()
	{
		openLog();
		try
		{
			pstr.println("");
			pstr.println("-------------------------------------------------------");
			pstr.println("closed " + sdf.format(new Date(System.currentTimeMillis())));
			pstr.println("-------------------------------------------------------");
			pstr.println("");
		}
		catch(Exception e)
		{
			System.out.println("COULD NOT WRITE TRAFFIC LOG FILE: " + e.getMessage());
		}
		closeLog();
	}

	static public void closeLog()
	{
		try
		{
//			System.out.println("CLOSE TRAFFIC LOG FILE ");
			pstr.close();
			fos.close();
		}
		catch(Exception ex)
		{
			System.out.println("COULD NOT CLOSE TRAFFIC LOG FILE: " + ex.getMessage());
		}
	}

}

