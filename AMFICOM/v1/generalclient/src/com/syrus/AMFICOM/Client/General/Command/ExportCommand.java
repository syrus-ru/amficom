/*
 * $Id: ExportCommand.java,v 1.2 2005/01/13 15:13:35 krupenn Exp $
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.Iterator;

/**
 * Класс $RCSfile: ExportCommand.java,v $ 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/13 15:13:35 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class ExportCommand extends VoidCommand
{
	private FileOutputStream fos;
	private OutputStreamWriter osw;
	private PrintWriter pw;

	protected void startObject(String type)
	{
		pw.println ("@@" + type);
	}

	protected void put(Object field, Object value)
	{
		if(value instanceof List)
		{
			List list = (List )value;
			pw.println ("@" + field);
			for(Iterator it = list.iterator(); it.hasNext();)
			{
				pw.println (it.next().toString());
			}
			pw.println ("@\\" + field);
		}
		else
			pw.println ("@" + field + " " + value);
	}
	
	protected void endObject()
	{
		pw.println();
	}
	
	protected void breakLine()
	{
		pw.println();
	}
	
	protected void open(String fileName)
	{
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка записи");
		}
		try
		{
			fos = new FileOutputStream (fileName);
			osw = new OutputStreamWriter(fos, "UTF-16");
			pw = new PrintWriter(osw, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void close()
	{
		try
		{
			pw.close();
			osw.close();
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected static final String openFileForWriting(String path)
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
				fileName = fileName + ".esf";
		}

		if (fileName == null)
			return null;
		if ((new File(fileName)).exists())
		{
			if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Файл существует. Перезаписать?",
					"",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE))
				return null;
		}

		return fileName;
	}
	
}
