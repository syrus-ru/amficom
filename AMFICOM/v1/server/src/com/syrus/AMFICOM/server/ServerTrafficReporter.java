package com.syrus.AMFICOM.server;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public final class ServerTrafficReporter
{
	static public SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	FileOutputStream fos;
	PrintWriter pstr;

	boolean firstin;
	boolean firstout;

	int incount;
	int outcount;
	
	String fname = "d:\\traffic.log";
	
	public ServerTrafficReporter(String fn)
	{
		fname = fn;
		startLog();
	}
	
	public void startLog()
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

	public void openLog()
	{

		try
		{
//			System.out.println("OPEN TRAFFIC LOG FILE ");
//			oracle.aurora.rdbms.security.PolicyTableManager.grant("AMFICOM","SYS:java.io.FilePermission",fname,"write");
			fos = new FileOutputStream(fname, true);
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

	public void fnReport(String fnname)
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

	public void fnInReport(String paramname, Object obj)
	{
		int count = 0;
		fnInReport(paramname, count);
	}

	public void fnStrReport(String text)
	{
		openLog();
		try
		{
			pstr.println("		" + text);
		}
		catch(Exception e)
		{
			System.out.println("		" + text);
		}
		closeLog();
	}

	public void fnInReport(String paramname, int count)
	{
		openLog();
		incount += count;
		try
		{
			if(firstin)
				pstr.println("	in parameters:");
			pstr.println("		" + paramname + " " + count);
		}
		catch(Exception e)
		{
			if(firstin)
				System.out.println("	in parameters:");
			System.out.println("		" + paramname + " " + count);
		}
		if(firstin)
			firstin = false;
		closeLog();
	}

	public void fnOutReport(String paramname, Object obj)
	{
		int count = 0;
		fnOutReport(paramname, count);
	}

	public void fnOutReport(String paramname, int count)
	{
		openLog();
		outcount += count;
		try
		{
			if(!firstin)
				pstr.println("	in total			" + incount);
			if(firstout)
				pstr.println("	out parameters:");
			pstr.println("		" + paramname + " " + count);
		}
		catch(Exception e)
		{
			if(!firstin)
				System.out.println("	in total			" + incount);
			if(firstout)
				System.out.println("	out parameters:");
			System.out.println("		" + paramname + " " + count);
		}
		if(!firstin)
			firstin = true;
		if(firstout)
			firstout = false;
		closeLog();
	}

	public void fnReportEnd()
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

	public void stopLog()
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

	public void closeLog()
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

