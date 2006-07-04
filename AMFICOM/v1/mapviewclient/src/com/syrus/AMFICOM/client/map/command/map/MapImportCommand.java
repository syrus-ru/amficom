/*-
 * $$Id: MapImportCommand.java,v 1.61 2006/07/04 14:41:09 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ImportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.xml.MapsDocument;
import com.syrus.AMFICOM.map.xml.XmlMap;
import com.syrus.AMFICOM.map.xml.XmlMapSeq;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.61 $, $Date: 2006/07/04 14:41:09 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
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

		if(this.mapFrame == null) {
			return;
		}

		Log.debugMessage("Importing map", INFO); //$NON-NLS-1$

		final String fileName = ImportCommand
				.openFileForReading(MapPropertiesManager.getLastDirectory());
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
		
		final String extension = ext;

//		new ProcessingDialog(new Runnable() {
//
//			public void run() {
				try {
					Map map = null;
					if(extension.equals(".xml")) { //$NON-NLS-1$
						// TODO add load by SyncroniousWorker  
						map = loadXML(fileName);
					}
	
					if(map == null)
						return;

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
					MapImportCommand.this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
					Log.errorMessage(e);
					setResult(Command.RESULT_NO);
				} catch(DatabaseException e) {
					Log.errorMessage(e);
					setResult(Command.RESULT_NO);
				} catch(XmlException e) {
					MapImportCommand.this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_XML_EXCEPTION)));
					Log.errorMessage(e);
					setResult(Command.RESULT_NO);
				} catch(IOException e) {
					Log.errorMessage(e);
					setResult(Command.RESULT_NO);
				}
//			}
//		}, I18N.getString(MapEditorResourceKeys.MESSAGE_INFORMATION_IMPORTING_PLS_WAIT));
	}

	protected Map loadXML(String fileName)
			throws CreateObjectException, XmlException, IOException {
		Map map = null;

		File xmlfile = new File(fileName);

		// Create an instance of a type generated from schema to hold the XML.
		// Parse the instance into the type generated from the schema.
		MapsDocument doc = 
			MapsDocument.Factory.parse(xmlfile);

		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML"); //$NON-NLS-1$
		}

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		// make sure default types loaded
//		LinkTypeController.getTopologicalLinkTypes();
//		NodeTypeController.getTopologicalNodeTypes();

		String user_dir = System.getProperty("user.dir"); //$NON-NLS-1$
		try {
			System.setProperty("user.dir",  xmlfile.getParent()); //$NON-NLS-1$

			XmlMapSeq xmlMaps = doc.getMaps();
			XmlMap[] xmlMapsArray = xmlMaps.getMapArray();
			for(int i = 0; i < xmlMapsArray.length; i++) {
				XmlMap xmlMap = xmlMapsArray[i];
				map = Map.createInstance(
						userId,
						domainId,
						xmlMap.getImportType(),
						xmlMap);
				map.setName(map.getName()
						+ " (" //$NON-NLS-1$
						+ I18N.getString(MapEditorResourceKeys.IMPORTED)
						+ " " //$NON-NLS-1$
						+ MapPropertiesManager.getDateFormat()
							.format(new Date(System.currentTimeMillis())) 
						+ " " //$NON-NLS-1$
						+ I18N.getString(MapEditorResourceKeys.OUT_OF_LOWERCASE)
						+ " \'" //$NON-NLS-1$
						+ xmlfile.getName() + "\')"); //$NON-NLS-1$
				
				map.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
				
				// only one map imported
				break;
			}
		} catch(CreateObjectException e) {
			throw e;
		} finally {
			System.setProperty("user.dir",  user_dir); //$NON-NLS-1$
		}
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
