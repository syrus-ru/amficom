/*
 * $Id: ImportCommand.java,v 1.11 2005/09/25 16:08:01 krupenn Exp $
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
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * 
 * @version $Revision: 1.11 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class ImportCommand extends AbstractCommand {

	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				MapEditorResourceKeys.EXTENSION_ESF,
				LangModelMap.getString(MapEditorResourceKeys.FILE_CHOOSER_EXPORT_SAVE_FILE));
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				MapEditorResourceKeys.EXTENSION_XML,
				LangModelMap.getString(MapEditorResourceKeys.FILE_CHOOSER_EXPORT_SAVE_FILE));
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle(LangModelMap.getString(MapEditorResourceKeys.FILE_CHOOSER_SELECT_FILE_TO_OPEN));
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(MapEditorResourceKeys.EXTENSION_DOT_XML) || fileName.endsWith(MapEditorResourceKeys.EXTENSION_DOT_ESF)))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}

}
