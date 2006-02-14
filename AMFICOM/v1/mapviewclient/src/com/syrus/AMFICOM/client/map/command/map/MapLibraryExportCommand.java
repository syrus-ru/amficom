/*-
 * $$Id: MapLibraryExportCommand.java,v 1.22 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ExportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapLibraryTableController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.xml.MapLibraryDocument;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;

/**
 * Класс $RCSfile: MapLibraryExportCommand.java,v $ используется для закрытия карты при
 * сохранении на экране самого окна карты. При этом в азголовке окна
 * отображается информация о том, что активной карты нет, и карта центрируется
 * по умолчанию
 * 
 * @version $Revision: 1.22 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapLibraryExportCommand extends ExportCommand {

	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapLibraryExportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null) {
			return;
		}

		MapLibraryTableController mapLibraryTableController = MapLibraryTableController.getInstance();

		Collection allLibraries;
		try {
			StorableObjectCondition condition = new EquivalentCondition(
					MAPLIBRARY_CODE);
			allLibraries = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch(CommunicationException e) {
			e.printStackTrace();
			return;
		} catch(DatabaseException e) {
			e.printStackTrace();
			return;
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		MapLibrary mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_LIBRARY),
				allLibraries,
				mapLibraryTableController,
				mapLibraryTableController.getKeysArray(),
				null,
				false);

		if(mapLibrary == null) {
			return;
		}

		String fileName = ExportCommand.openFileForWriting(MapPropertiesManager
				.getLastDirectory());
		if(fileName == null) {
			return;
		}
		File file = new File(fileName);
		MapPropertiesManager.setLastDirectory(file.getParent());

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf(".")); //$NON-NLS-1$

		if(ext == null) {
			ext = ".xml"; //$NON-NLS-1$
		}

		if(ext.equals(".xml")) { //$NON-NLS-1$
			saveXML(mapLibrary, fileName);
		}
		setResult(Command.RESULT_OK);
	}

	protected void saveXML(MapLibrary mapLibrary, String fileName) {
		try {
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
			java.util.Map<String, String> prefixes = new HashMap<String, String>();
			prefixes.put("http://syrus.com/AMFICOM/map/xml", "map"); //$NON-NLS-1$ //$NON-NLS-2$
			prefixes.put("http://syrus.com/AMFICOM/general/xml", "general"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlOptions.setSaveSuggestedPrefixes(prefixes);
			xmlOptions.setSaveAggressiveNamespaces();
	
			MapLibraryDocument doc = 
				MapLibraryDocument.Factory.newInstance(xmlOptions);

			mapLibrary.getXmlTransferable(doc.addNewMapLibrary(), "amficom", false); //$NON-NLS-1$

			// Validate the new XML
			if (validateXml(doc)) {
				File f = new File(fileName);
	
				try {
					// Writing the XML Instance to a file.
					doc.save(f, xmlOptions);
				} catch(IOException e) {
					e.printStackTrace();
				}
				Log.debugMessage("\nXML Instance Document saved at : " //$NON-NLS-1$
						+ f.getPath(), INFO);
			}
		} catch (final XmlConversionException xce) {
			Log.errorMessage(xce);
		}
	}

	public boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			Log.debugMessage("Invalid XML: ", WARNING); //$NON-NLS-1$
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				Log.debugMessage(error.getMessage(), WARNING);
				Log.debugMessage(error.getObjectLocation().toString(), WARNING);
			}
		}
		return isXmlValid;
	}
}
