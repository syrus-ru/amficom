/*
 * $Id: ImportCommand.java,v 1.10 2005/09/16 15:45:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command;

import java.io.File;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * 
 * @version $Revision: 1.10 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class ImportCommand extends AbstractCommand {

	protected static final String openFileForReading(String path) {
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
		fileChooser.setDialogTitle(LangModelMap.getString("FileChooser.SelectFileToOpen")); //$NON-NLS-1$
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf"))) //$NON-NLS-1$ //$NON-NLS-2$
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}

}
