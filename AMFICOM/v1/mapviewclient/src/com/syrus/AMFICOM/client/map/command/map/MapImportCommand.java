/*
 * $Id: MapImportCommand.java,v 1.39 2005/08/18 14:08:53 krupenn Exp $
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
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ImportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.39 $, $Date: 2005/08/18 14:08:53 $
 * @module mapviewclient
 */
public class MapImportCommand extends ImportCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapImportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		Log.debugMessage("Importing map", Level.INFO);

		final String fileName = ImportCommand
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
		
		final String extension = ext;

		CommonUIUtilities.invokeAsynchronously(new Runnable() {

			@SuppressWarnings("synthetic-access")
			public void run() {
				try {
					Map map = null;
					MapImportCommand.this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));
					if(extension.equals(".xml")) {
						map = loadXML(fileName);
					}
					MapImportCommand.this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
		
					if(map == null)
						return;
		
					StorableObjectPool.putStorableObject(map);
		
					MapView mv = MapImportCommand.this.mapFrame.getMapView();
					MapImportCommand.this.mapFrame.getMapViewer().getLogicalNetLayer()
							.getMapViewController().removeSchemes();
					mv.setMap(map);
					MapImportCommand.this.mapFrame.setMapView(mv);
		
					Dispatcher disp = MapImportCommand.this.mapFrame.getContext().getDispatcher();
					if(disp != null) {
						disp.firePropertyChange(new MapEvent(
								this,
								MapEvent.MAP_VIEW_CHANGED,
								mv));
						disp.firePropertyChange(new MapEvent(
								this,
								MapEvent.MAP_VIEW_SELECTED,
								mv));
					}
		
					setResult(Command.RESULT_OK);
				} catch(MapException e) {
					MapImportCommand.this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Ошибка соединения с картографическими данными"));
					e.printStackTrace();
					setResult(Command.RESULT_NO);
				} catch(DatabaseException e) {
					e.printStackTrace();
					setResult(Command.RESULT_NO);
				} catch(IllegalObjectEntityException e) {
					e.printStackTrace();
					setResult(Command.RESULT_NO);
				} catch(XmlException e) {
					e.printStackTrace();
					setResult(Command.RESULT_NO);
				} catch(IOException e) {
					e.printStackTrace();
					setResult(Command.RESULT_NO);
				}
			}
		}, LangModelMap.getString("Message.Information.ImportingPlsWait"));	
	}

	protected Map loadXML(String fileName)
			throws CreateObjectException, XmlException, IOException {
		Map map = null;

		File xmlfile = new File(fileName);

		// Create an instance of a type generated from schema to hold the XML.
		// Parse the instance into the type generated from the schema.
		com.syrus.amficom.map.xml.MapsDocument doc = 
			com.syrus.amficom.map.xml.MapsDocument.Factory.parse(xmlfile);

		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		// make sure default types loaded
		LinkTypeController.getTopologicalLinkTypes();
		NodeTypeController.getTopologicalNodeTypes();

		String user_dir = System.getProperty("user.dir");
		System.setProperty("user.dir",  xmlfile.getParent());

		com.syrus.amficom.map.xml.Maps xmlMaps = doc.getMaps();
		com.syrus.amficom.map.xml.Map[] xmlMapsArray = xmlMaps.getMapArray();
		for(int i = 0; i < xmlMapsArray.length; i++) {
			com.syrus.amficom.map.xml.Map xmlMap = xmlMapsArray[i];
			map = Map.createInstance(
					userId,
					domainId,
					xmlMap.getImporttype(),
					xmlMap,
					new ClonedIdsPool());
			map.setName(map.getName()
					+ "(imported "
					+ MapPropertiesManager.getDateFormat()
						.format(new Date(System.currentTimeMillis())) 
					+ " from \'"
					+ xmlfile.getName() + "\')");
			
			map.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
			
			// only one map imported
			break;
		}
		System.setProperty("user.dir",  user_dir);
		return map;
	}

	public boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			Log.debugMessage("Invalid XML: ", Level.WARNING);
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				Log.debugMessage(error.getMessage(), Level.WARNING);
				Log.debugMessage(error.getObjectLocation().toString(), Level.WARNING);
			}
		}
		return isXmlValid;
	}
}
