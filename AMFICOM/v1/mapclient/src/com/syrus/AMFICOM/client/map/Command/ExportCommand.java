/*
 * $Id: ExportCommand.java,v 1.1.2.1 2004/09/24 10:55:35 krupenn Exp $
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Класс $RCSfile: ExportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.1.2.1 $, $Date: 2004/09/24 10:55:35 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class ExportCommand extends VoidCommand
{
	protected FileOutputStream fos;
	protected OutputStreamWriter osw;
	protected PrintWriter pw;

	protected DataSourceInterface dsi;
	
	public ExportCommand(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	protected void startObject(String type)
	{
		pw.println ("@@" + type);
	}

	protected void put(String field, String value)
	{
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
	
	protected String openFileForWriting(String path)
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
	
}
