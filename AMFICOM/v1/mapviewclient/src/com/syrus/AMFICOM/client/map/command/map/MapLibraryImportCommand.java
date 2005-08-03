/*
 * $Id: MapLibraryImportCommand.java,v 1.2 2005/08/03 15:37:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ImportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

/**
 * Класс $RCSfile: MapLibraryImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/08/03 15:37:18 $
 * @module mapviewclient_v1
 */
public class MapLibraryImportCommand extends ImportCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapLibraryImportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		try {
			MapLibrary mapLibrary = null;
			System.out.println("Import map library");

			String fileName = ImportCommand
					.openFileForReading(MapPropertiesManager.getLastDirectory());
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
				mapLibrary = loadXML(fileName);
			}

			if(mapLibrary == null)
				return;

			StorableObjectPool.putStorableObject(mapLibrary);
			StorableObjectPool.flush(mapLibrary, LoginManager.getUserId(), true);

			Map map = this.mapFrame.getMapView().getMap();
			map.addMapLibrary(mapLibrary);
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(
						this, 
						MapEvent.LIBRARY_SET_CHANGED,
						map.getMapLibraries()));

			setResult(Command.RESULT_OK);
		} catch(DatabaseException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(IdentifierGenerationException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(XmlException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(IOException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(ApplicationException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}

	protected MapLibrary loadXML(String fileName)
			throws XmlException, IOException, ApplicationException {
		MapLibrary mapLibrary = null;

		File xmlfile = new File(fileName);

		// Create an instance of a type generated from schema to hold the XML.
		// Parse the instance into the type generated from the schema.
		com.syrus.amficom.map.xml.LibraryDocument doc = 
			com.syrus.amficom.map.xml.LibraryDocument.Factory.parse(xmlfile);

		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}

		Identifier userId = LoginManager.getUserId();

		com.syrus.amficom.map.xml.MapLibrary xmlLibrary = doc.getLibrary();
		
		mapLibrary = MapLibrary.createInstance(userId, xmlLibrary, new ClonedIdsPool());
		return mapLibrary;
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
