/*
 * $Id: MapImportCommand.java,v 1.36 2005/08/12 10:45:20 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
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
 * ����� $RCSfile: MapImportCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.36 $, $Date: 2005/08/12 10:45:20 $
 * @module mapviewclient
 */
public class MapImportCommand extends ImportCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * ���� �����
	 */
	MapFrame mapFrame;

	public MapImportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		try {
			Map map = null;
			Log.debugMessage("Importing map", Level.INFO);

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
				map = loadXML(fileName);
			}

			if(map == null)
				return;

			StorableObjectPool.putStorableObject(map);

			MapView mv = this.mapFrame.getMapView();
			this.mapFrame.getMapViewer().getLogicalNetLayer()
					.getMapViewController().removeSchemes();
			mv.setMap(map);
			this.mapFrame.setMapView(mv);

			Dispatcher disp = this.mapFrame.getContext().getDispatcher();
			if(disp != null) {
				disp.firePropertyChange(new MapEvent(
						mv,
						MapEvent.MAP_VIEW_CHANGED));
				disp.firePropertyChange(new MapEvent(
						mv,
						MapEvent.MAP_VIEW_SELECTED));
			}

			setResult(Command.RESULT_OK);
		} catch(MapException e) {
			this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "������ ���������� � ����������������� �������"));
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

		com.syrus.amficom.map.xml.Maps xmlMaps = doc.getMaps();
		com.syrus.amficom.map.xml.Map[] xmlMapsArray = xmlMaps.getMapArray();
		for(int i = 0; i < xmlMapsArray.length; i++) {
			com.syrus.amficom.map.xml.Map xmlMap = xmlMapsArray[i];
			map = Map.createInstance(
					userId,
					domainId,
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
