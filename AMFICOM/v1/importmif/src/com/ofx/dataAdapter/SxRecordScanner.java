// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 

package com.ofx.dataAdapter;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import java.io.*;
import java.util.Vector;

// Referenced classes of package com.ofx.dataAdapter:
//			SxDelimitedRecordDefinition, SxDelimitedRecordScanner, SxDataAdapter, SxImportException, 
//			SxRecordDefinition, SxDataRecord

abstract class SxRecordScanner
	implements SxDataAdapter
{

	static SxRecordScanner create(InputStream inputstream, SxRecordDefinition sxrecorddefinition)
	{
		if(sxrecorddefinition instanceof SxDelimitedRecordDefinition)
			return new SxDelimitedRecordScanner(inputstream, (SxDelimitedRecordDefinition)sxrecorddefinition);
		else
			return null;
	}

	protected SxRecordScanner(InputStream inputstream, SxRecordDefinition sxrecorddefinition)
	{
		lineCount = 0;
		sawEOF = false;
		setInputStream(inputstream);
		currentLine = null;
		currentRecord = null;
		recordDefinition = sxrecorddefinition;
		automaticConversion = true;
		selectedFieldDefinitions = sxrecorddefinition.getFields();
	}

	public void release()
	{
		try
		{
			if(inputStream != null)
			{
				inputStream.close();
				inputStream = null;
			}
			recordDefinition = null;
		}
		catch(IOException ioexception) { }
	}

	protected boolean getAutomaticConversion()
	{
		return automaticConversion;
	}

	protected void setAutomaticConversion(boolean flag)
	{
		automaticConversion = flag;
	}

	public String getCurrentLine()
	{
		return currentLine;
	}

	protected void setCurrentLine(String s)
	{
		currentLine = s;
	}

	public int getLineNumber()
	{
		return lineCount;
	}

	public SxDataRecord getCurrentRecord()
	{
		return currentRecord;
	}

	protected void setCurrentRecord(SxDataRecord sxdatarecord)
	{
		currentRecord = sxdatarecord;
	}

	protected BufferedReader getInputStream()
	{
		return inputStream;
	}

	void setInputStream(InputStream inputstream)
	{
		inputStream = new BufferedReader(new InputStreamReader(inputstream), 0x186a0);
	}

	public SxRecordDefinition getRecordDefinition()
	{
		return recordDefinition;
	}

	public void setRecordDefinition(SxRecordDefinition sxrecorddefinition)
	{
		recordDefinition = sxrecorddefinition;
		setSelectedFields(sxrecorddefinition.getFields());
	}

	public boolean isAtEnd()
	{
		return sawEOF;
	}

	public void setSawEOF(boolean flag)
	{
		sawEOF = flag;
	}

	public void reset()
	{
		try
		{
			getInputStream().reset();
		}
		catch(IOException ioexception)
		{
			SxEnvironment.log().println("SxRecordScanner.reset() exception:" + ioexception);
		}
		setCurrentRecord(null);
		setCurrentLine("");
	}

	protected Vector getSelectedFields()
	{
		return selectedFieldDefinitions;
	}

	protected void setSelectedFields(Vector vector)
	{
		selectedFieldDefinitions = vector;
	}

	protected String scanLine()
	{
		if(scanned)
		{
			return null;
		}
		currentLine = null;
		try
		{
			do
			{
				currentLine = getInputStream().readLine();
				lineCount++;
			} while(currentLine != null && currentLine.length() == 0);
			if(currentLine == null)
				sawEOF = true;
		}
		catch(EOFException eofexception)
		{
			sawEOF = true;
		}
		catch(IOException ioexception)
		{
			ioexception.printStackTrace();
		}
		scanned = true;
		return currentLine;
	}

	protected String readLine()
	{
		if(scanned)
		{
			scanned = false;
			return currentLine;
		}
		currentLine = null;
		try
		{
			do
			{
				currentLine = getInputStream().readLine();
				lineCount++;
			} while(currentLine != null && currentLine.length() == 0);
			if(currentLine == null)
				sawEOF = true;
		}
		catch(EOFException eofexception)
		{
			sawEOF = true;
		}
		catch(IOException ioexception)
		{
			ioexception.printStackTrace();
		}
		return currentLine;
	}

	public abstract SxDataRecord readRecord()
		throws SxImportException;

	public abstract SxDataRecord readRecord(SxRecordDefinition sxrecorddefinition)
		throws SxImportException;

	public abstract SxDataRecord parseRecord(SxRecordDefinition sxrecorddefinition)
		throws SxImportException;

	public void skipToEnd()
	{
		while(readLine() != null) ;
	}

	private BufferedReader inputStream;
	private int lineCount;
	private SxDataRecord currentRecord;
	private String currentLine;
	private boolean automaticConversion;
	private SxRecordDefinition recordDefinition;
	private Vector selectedFieldDefinitions;
	private boolean sawEOF;

	private boolean scanned = false;
}
