/*
 * $Id: ExportCommand.java,v 1.4 2005/02/07 16:10:17 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/02/07 16:10:17 $
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
			pw.println ("@[" + field);
			for(Iterator it = list.iterator(); it.hasNext();)
			{
				pw.println (it.next().toString());
			}
			pw.println ("@]" + field);
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
		fileChooser.setDialogTitle("Выберите файл для записи");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
		{
			fileName = fileChooser.getSelectedFile().getPath();
			if (!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				fileName = fileName + ".xml";
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
