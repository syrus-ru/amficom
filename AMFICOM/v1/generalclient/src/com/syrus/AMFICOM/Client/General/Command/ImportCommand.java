/*
 * $Id: ImportCommand.java,v 1.8 2005/03/16 13:40:57 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.io.IntelStreamReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * ����� $RCSfile: ImportCommand.java,v $ 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2005/03/16 13:40:57 $
 * @module map_v2
 * @author $Author: bass $
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
		public Map exportColumns;
		
		public ImportObject(String type, java.util.Map exportColumns)
		{
			this.type = type;
			this.exportColumns = new HashMap(exportColumns);
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

	private Map objectColumnList = new HashMap();

	protected ImportObject readObject()
	{
		objectColumnList.clear();

		String[] type = readType();
		if(type == null)
			return null;
		while(true)
		{
			String[] s = getString();
			Object key;
			Object value = null;
			if(s == null)
				break;
			if(s[0].length() == 0)
				break;
			if(s[0].indexOf("@@") != -1)
				return null;
			if(s[0].indexOf("@") != 0)
				return null;

			s[0] = s[0].substring(1, s[0].length());
			key = s[0];

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
				key = s[0];
				value = arg;
			}
			else
				value = s[1];

			if(value != null)
				objectColumnList.put(key, value);
		}
		return new ImportObject(type[0], objectColumnList);
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
					"������ ������");
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

		ChoosableFileFilter esfFilter =
			new ChoosableFileFilter(
			"esf",
			"Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter =
			new ChoosableFileFilter(
			"xml",
			"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle("�������� ���� ��� ������");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
		{
			fileName = fileChooser.getSelectedFile().getPath();
			if (!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				return null;
		}

		if (fileName == null)
			return null;

		if (!(new File(fileName)).exists())
			return null;

		return fileName;
	}

}
