/*
 * $Id: MapLibraryExportCommand.java,v 1.1 2005/08/02 07:23:05 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ExportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapLibraryTableController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.MapLibrary;

/**
 * Класс $RCSfile: MapLibraryExportCommand.java,v $ используется для закрытия карты при
 * сохранении на экране самого окна карты. При этом в азголовке окна
 * отображается информация о том, что активной карты нет, и карта центрируется
 * по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/08/02 07:23:05 $
 * @module mapviewclient_v1
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

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		MapLibraryTableController mapLibraryTableController = MapLibraryTableController.getInstance();

		MapLibrary mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("MapLibrary"),
				MapViewController.getMapLibraries(),
				mapLibraryTableController,
				mapLibraryTableController.getKeysArray(),
				true);

		if(mapLibrary == null) {
			return;
		}

		String fileName = ExportCommand.openFileForWriting(MapPropertiesManager
				.getLastDirectory());
		if(fileName == null)
			return;
		File file = new File(fileName);
		MapPropertiesManager.setLastDirectory(file.getParent());

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf("."));

		if(ext == null) {
			ext = ".xml";
		}

		if(ext.equals(".xml")) {
			saveXML(mapLibrary, fileName);
		}
		setResult(Command.RESULT_OK);
	}

	protected void saveXML(MapLibrary mapLibrary, String fileName) {

		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);

		com.syrus.amficom.map.xml.LibraryDocument doc = 
			com.syrus.amficom.map.xml.LibraryDocument.Factory.newInstance(xmlOptions);

		com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = doc.addNewLibrary();
		xmlMapLibrary.set(mapLibrary.getXMLTransferable());
		
		// Validate the new XML
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			File f = new File(fileName);

			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nXML Instance Document saved at : "
					+ f.getPath());
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
			System.out.println("Invalid XML: ");
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				System.out.println(error.getMessage());
				System.out.println(error.getObjectLocation());
			}
		}
		return isXmlValid;
	}
}
