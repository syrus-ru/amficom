/*
 * $Id: ExportCommand.java,v 1.5 2005/06/06 12:20:29 krupenn Exp $
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;

/**
 * 
 * @version $Revision: 1.5 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public abstract class ExportCommand extends AbstractCommand {
	private FileOutputStream fos;

	private OutputStreamWriter osw;

	private PrintWriter pw;

	protected void startObject(String type) {
		this.pw.println("@@" + type);
	}

	protected void put(Object field, Object value) {
		if(value instanceof Collection) {
			Collection objects = (Collection )value;
			this.pw.println("@[" + field);
			for(Iterator it = objects.iterator(); it.hasNext();) {
				this.pw.println(it.next().toString());
			}
			this.pw.println("@]" + field);
		}
		else
			this.pw.println("@" + field + " " + value);
	}

	protected void endObject() {
		this.pw.println();
	}

	protected void breakLine() {
		this.pw.println();
	}

	protected void open(String fileName) {
		if(fileName == null) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка записи");
		}
		try {
			this.fos = new FileOutputStream(fileName);
			this.osw = new OutputStreamWriter(this.fos, "UTF-16");
			this.pw = new PrintWriter(this.osw, true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void close() {
		try {
			this.pw.close();
			this.osw.close();
			this.fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected static final String openFileForWriting(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				"esf",
				"Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle("Выберите файл для записи");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				fileName = fileName + ".xml";
		}

		if(fileName == null)
			return null;
		if((new File(fileName)).exists()) {
			if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
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
