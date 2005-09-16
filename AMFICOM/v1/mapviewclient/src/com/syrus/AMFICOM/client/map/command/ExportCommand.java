/*
 * $Id: ExportCommand.java,v 1.9 2005/09/16 15:45:54 krupenn Exp $
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
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * 
 * @version $Revision: 1.9 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class ExportCommand extends AbstractCommand {

	protected static final String openFileForWriting(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				"esf", //$NON-NLS-1$
				LangModelMap.getString("FileChooser.ExportSaveFile")); //$NON-NLS-1$
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml", //$NON-NLS-1$
				LangModelMap.getString("FileChooser.ExportSaveFile")); //$NON-NLS-1$
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle(LangModelMap.getString("FileChooser.SelectFileToSave")); //$NON-NLS-1$
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf"))) //$NON-NLS-1$ //$NON-NLS-2$
				fileName = fileName + ".xml"; //$NON-NLS-1$
		}

		if(fileName == null)
			return null;
		if((new File(fileName)).exists()) {
			if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					LangModelMap.getString("FileChooser.FileExists.Overwrite"), //$NON-NLS-1$
					"", //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE))
				return null;
		}

		return fileName;
	}

}
