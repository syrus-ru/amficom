/*-
 * $$Id: ImportCommand.java,v 1.13 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command;

import java.io.File;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class ImportCommand extends AbstractCommand {

	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				MapEditorResourceKeys.EXTENSION_ESF,
				I18N.getString(MapEditorResourceKeys.FILE_CHOOSER_EXPORT_SAVE_FILE));
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				MapEditorResourceKeys.EXTENSION_XML,
				I18N.getString(MapEditorResourceKeys.FILE_CHOOSER_EXPORT_SAVE_FILE));
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle(I18N.getString(MapEditorResourceKeys.FILE_CHOOSER_SELECT_FILE_TO_OPEN));
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
