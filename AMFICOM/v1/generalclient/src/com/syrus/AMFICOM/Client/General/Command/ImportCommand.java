/*
 * $Id: ImportCommand.java,v 1.4 2005/01/14 09:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.io.IntelStreamReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Класс $RCSfile: ImportCommand.java,v $ 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/14 09:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class ImportCommand extends VoidCommand
{
	private FileInputStream fis;
	private IntelStreamReader isr;

	private Map clonedIds = new HashMap();

	protected class ImportObject
	{
		public String type;
		public Object[][] exportColumns;
		
		public ImportObject(String type, Object[][] exportColumns)
		{
			this.type = type;
			this.exportColumns = exportColumns;
		}
	}

	public ImportCommand()
	{
	}
	
	protected final Identifier getClonedId(short entityCode, String id)
		throws IllegalObjectEntityException
	{
		Identifier clonedId = (Identifier )clonedIds.get(id);
		if(clonedId == null)
			clonedId = cloneId(entityCode, id);
		return clonedId;
	}
	
	private final Identifier cloneId(short entityCode, String id)
		throws IllegalObjectEntityException
	{
		Identifier clonedId;
		clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		clonedIds.put(id, clonedId);
		return clonedId;
	}

	protected ImportObject readObject()
	{
		List objectColumnList = new LinkedList();
		String[] type = readType();
		if(type == null)
			return null;
		while(true)
		{
			String[] s = getString();
			Object[] o = new Object[2];
			if(s == null)
				break;
			if(s[0].length() == 0)
				break;
			if(s[0].indexOf("@@") != -1)
				return null;
			if(s[0].indexOf("@") != 0)
				return null;

			s[0] = s[0].substring(1, s[0].length());
			o[0] = s[0];

			if(s[0].indexOf("[") == 0)
			{
				s[0] = s[0].substring(1, s[0].length());
				List arg = new LinkedList();
				while(true)
				{
					String[] s2 = getString();
					if(s2 == null)
						return null;
					if(s2[0].length() == 0)
						return null;
					if(s2[0].indexOf("@]") != -1)
					{
						if(s2[0].indexOf(s[0]) == -1)
							return null;
						break;
					}
					if(s2[0].indexOf("@") != -1)
						return null;
					arg.add(s2[0]);
				}
				o[1] = arg;
			}
			else
				o[1] = s[1];

			objectColumnList.add(o);
		}
		Object[][] retObj = new Object[objectColumnList.size()][];
		int i = 0;
		for(Iterator it = objectColumnList.iterator(); it.hasNext();)
		{
			Object[] s = (Object[] )it.next();
			retObj[i++] = s;
		}
		return new ImportObject(type[0], retObj);
	}
	
	private String[] readType()
	{
		String[] s = getString();
		if(s == null)
			return null;
		if(s[0].indexOf("@@") == -1)
			return null;
		s[0] = s[0].substring(2, s[0].length());
		
		return s;
	}

	private String[] getString()
	{
		String[] readString = new String[2];
		try 
		{
			if(!isr.ready())
				return null;
			String s = isr.readASCIIString();
			if(s.length() == 0)
			{
				readString[0] = "";
				readString[1] = "";
			}
			else
			{
				int n = s.indexOf(" ");
				if (n == -1)
				{
					readString[0] = s;
					readString[1] = "";
				}
				else
				{
					readString[0] = s.substring(0, n);
					readString[1] = s.substring(n + 1, s.length());
				}
			}
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			
		} 
		finally 
		{
		}
		
		return readString;
	}

	protected void open(String fileName)
	{
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка чтения");
		}
		try
		{
			fis = new FileInputStream(fileName);
			isr = new IntelStreamReader(fis, "UTF-16");
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
	}
	
	protected void close()
	{
		try
		{
			isr.close();
			fis.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected static final String openFileForReading(String path)
	{
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter filter =
			new ChoosableFileFilter(
			"esf",
			"Export Save File");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setApproveButtonText("Ok");
		fileChooser.setCurrentDirectory(new File(path));

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
		{
			fileName = fileChooser.getSelectedFile().getPath();
			if (!fileName.endsWith(".esf"))
				return null;
		}

		if (fileName == null)
			return null;

		if (!(new File(fileName)).exists())
			return null;

		return fileName;
	}

}
