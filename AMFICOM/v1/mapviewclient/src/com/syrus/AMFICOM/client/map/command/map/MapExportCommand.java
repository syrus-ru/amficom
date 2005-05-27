/*
 * $Id: MapExportCommand.java,v 1.15 2005/05/27 15:14:55 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Command.ExportCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;

/**
 * Класс $RCSfile: MapExportCommand.java,v $ используется для закрытия карты при
 * сохранении на экране самого окна карты. При этом в азголовке окна
 * отображается информация о том, что активной карты нет, и карта центрируется
 * по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapExportCommand extends ExportCommand {
	public static final String MAP_TYPE = "map";

	public static final String MARK_TYPE = "mapmarkelement";

	public static final String SITE_TYPE = "mapsiteelement";

	public static final String NODE_TYPE = "mapnodeelement";

	public static final String NODELINK_TYPE = "mapnodelinkelement";

	public static final String COLLECTOR_TYPE = "mappipepathelement";

	public static final String LINK_TYPE = "maplinkelement";

	private static java.util.Map typesMap = new HashMap();

	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	static {
		typesMap.put(ObjectEntities.MAP_ENTITY, MAP_TYPE);
		typesMap.put(ObjectEntities.MARK_ENTITY, MARK_TYPE);
		typesMap.put(ObjectEntities.SITE_NODE_ENTITY, SITE_TYPE);
		typesMap.put(ObjectEntities.TOPOLOGICAL_NODE_ENTITY, NODE_TYPE);
		typesMap.put(ObjectEntities.NODE_LINK_ENTITY, NODELINK_TYPE);
		typesMap.put(ObjectEntities.COLLECTOR_ENTITY, COLLECTOR_TYPE);
		typesMap.put(ObjectEntities.PHYSICAL_LINK_ENTITY, LINK_TYPE);
	}

	public MapExportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		System.out.println("Closing map");

		Map map = this.mapFrame.getMap();

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
			saveXML(map, fileName);
		}
		else
			if(ext.equals(".esf")) {
				saveESF(map, fileName);
			}
		// mapFrame.setTitle(LangModelMap.getString("Map"));
		setResult(Command.RESULT_OK);
	}

	Collection getAllElements(Map map) {
		Collection allElements = new LinkedList();

		allElements.addAll(map.getMarks());
		allElements.addAll(map.getTopologicalNodes());
		allElements.addAll(map.getSiteNodes());

		allElements.addAll(map.getPhysicalLinks());
		allElements.addAll(map.getNodeLinks());
		allElements.addAll(map.getCollectors());

		return allElements;
	}

	protected void saveESF(Map map, String fileName) {
		java.util.Map exportColumns = null;

		super.open(fileName);

		super.startObject(MAP_TYPE);
		exportColumns = map.getExportMap();
		for(Iterator it = exportColumns.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			super.put(key, exportColumns.get(key));
		}
		super.endObject();

		for(Iterator it = getAllElements(map).iterator(); it.hasNext();) {
			MapElement me = (MapElement )it.next();
			String entityCodeString = ObjectEntities.codeToString(me.getId()
					.getMajor());
			super.startObject((String )typesMap.get(entityCodeString));
			exportColumns = me.getExportMap();
			for(Iterator it2 = exportColumns.keySet().iterator(); it2.hasNext();) {
				Object key = it2.next();
				super.put(key, exportColumns.get(key));
			}
			super.endObject();
		}

		super.close();
	}

	protected void saveXML(Map map, String fileName) {

		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);

		com.syrus.amficom.map.xml.MapsDocument doc = 
			com.syrus.amficom.map.xml.MapsDocument.Factory.newInstance(xmlOptions);

		com.syrus.amficom.map.xml.Maps xmlMaps = doc.addNewMaps();

		xmlMaps.setMapArray(new com.syrus.amficom.map.xml.Map[] {(com.syrus.amficom.map.xml.Map)map.getXMLTransferable()});
		
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
