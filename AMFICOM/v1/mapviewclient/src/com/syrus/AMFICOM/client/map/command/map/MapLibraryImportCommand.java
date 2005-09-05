/*
 * $Id: MapLibraryImportCommand.java,v 1.11 2005/09/05 17:43:20 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.map;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

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
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.xml.MapLibraryDocument;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapLibraryImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/09/05 17:43:20 $
 * @module mapviewclient
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

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		try {
			MapLibrary mapLibrary = null;
			Log.debugMessage("Import map library", INFO);

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
		MapLibraryDocument doc = 
			MapLibraryDocument.Factory.parse(xmlfile);

		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}

		Identifier userId = LoginManager.getUserId();

		String user_dir = System.getProperty("user.dir");
		System.setProperty("user.dir",  xmlfile.getParent());

		XmlMapLibrary xmlLibrary = doc.getMapLibrary();
		
		mapLibrary = MapLibrary.createInstance(userId, xmlLibrary.getImportType(), xmlLibrary);

		StorableObjectPool.flush(mapLibrary, userId, true);

		//TODO siteNodeTypes and physicalLinkTypes should be saved when mapLibrary is saved
		for(SiteNodeType siteNodeType : mapLibrary.getSiteNodeTypes()) {
			StorableObjectPool.flush(siteNodeType, userId, true);
		}
		for(PhysicalLinkType physicalLinkType : mapLibrary.getPhysicalLinkTypes()) {
			StorableObjectPool.flush(physicalLinkType, userId, true);
		}
		
		System.setProperty("user.dir",  user_dir);

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
			Log.debugMessage("Invalid XML: ", WARNING);
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				Log.debugMessage(error.getMessage(), WARNING);
				Log.debugMessage(error.getObjectLocation().toString(), WARNING);
			}
		}
		return isXmlValid;
	}
}
