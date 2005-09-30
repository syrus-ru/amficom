/*-
 * $$Id: ExportCommand.java,v 1.12 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class ExportCommand extends AbstractCommand {

	protected static final String openFileForWriting(String path) {
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
		fileChooser.setDialogTitle(LangModelMap.getString(MapEditorResourceKeys.FILE_CHOOSER_SELECT_FILE_TO_SAVE));
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(MapEditorResourceKeys.EXTENSION_DOT_XML) || fileName.endsWith(MapEditorResourceKeys.EXTENSION_DOT_ESF)))
				fileName = fileName + MapEditorResourceKeys.EXTENSION_DOT_XML;
		}

		if(fileName == null)
			return null;
		if((new File(fileName)).exists()) {
			if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					LangModelMap.getString(MapEditorResourceKeys.FILE_CHOOSER_FILE_EXISTS_OVERWRITE),
					MapEditorResourceKeys.EMPTY_STRING,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE))
				return null;
		}

		return fileName;
	}

}
