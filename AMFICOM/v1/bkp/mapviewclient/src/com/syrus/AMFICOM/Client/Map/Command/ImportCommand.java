/*
 * $Id: ImportCommand.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
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
 * Класс $RCSfile: ImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class ImportCommand extends VoidCommand
{
	private FileInputStream fis;
	private IntelStreamReader isr;

	private Map clonedIds = new HashMap();
	
	private DataSourceInterface dsi;
	
	public ImportCommand(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	protected final String getClonedId(String typ, String id)
	{
		String clonedId = (String )clonedIds.get(id);
		if(clonedId == null)
			clonedId = cloneId(typ, id);
		return clonedId;
	}
	
	private final String cloneId(String typ, String id)
	{
		String clonedId;
		if(dsi == null)
			clonedId = "new" + id;
		else
			clonedId = dsi.GetUId(typ);
		clonedIds.put(id, clonedId);
		return clonedId;
	}

	protected String[][] readObject()
	{
		List objectColumnList = new LinkedList();
		String[] type = readType();
		if(type == null)
			return null;
		objectColumnList.add(type);
		while(true)
		{
			String[] s = getString();
			if(s == null)
				break;
			if(s[0].length() == 0)
				break;
			if(s[0].indexOf("@@") != -1)
				return null;
			if(s[0].indexOf("@") != 0)
				return null;
			s[0] = s[0].substring(1, s[0].length());

			objectColumnList.add(s);
		}
		String[][] retObj = new String[objectColumnList.size()][];
		int i = 0;
		for(Iterator it = objectColumnList.iterator(); it.hasNext();)
		{
			String[] s = (String[] )it.next();
			retObj[i++] = s;
		}
		return retObj;
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

//	protected static String[] readString = new String[2];

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

	public void setDsi(DataSourceInterface dsi)
	{
		this.dsi = dsi;
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
