/*
 * $Id: MapImportCommand.java,v 1.29 2005/06/17 11:01:08 bass Exp $
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
import java.util.Iterator;
import java.util.List;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ImportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.TopologicalNodeWrapper;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: bass $
 * @version $Revision: 1.29 $, $Date: 2005/06/17 11:01:08 $
 * @module mapviewclient_v1
 */
public class MapImportCommand extends ImportCommand {
	public static final String MAP_TYPE = "map";

	public static final String MARK_TYPE = "mapmarkelement";

	public static final String SITE_TYPE = "mapsiteelement";

	public static final String NODE_TYPE = "mapnodeelement";

	public static final String NODELINK_TYPE = "mapnodelinkelement";

	public static final String COLLECTOR_TYPE = "mappipepathelement";

	public static final String LINK_TYPE = "maplinkelement";

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

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

		try {
			Map map = null;
			System.out.println("Import map");

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
			else
				if(ext.equals(".esf")) {
					map = loadESF(fileName);
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
		} catch(MapConnectionException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(MapDataException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
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
			break;
		}
		return map;
	}

	protected Map loadESF(String fileName)
			throws DatabaseException, IllegalObjectEntityException,
			IdentifierGenerationException {
		Map map;
		MapElement me;
		ImportCommand.ImportObject importObject;
		String type;
		java.util.Map exportColumns;

		// make sure default types loaded
		LinkTypeController.getTopologicalLinkTypes();
		NodeTypeController.getTopologicalNodeTypes();

		super.open(fileName);

		importObject = super.readObject();
		if(importObject == null)
			return null;
		type = importObject.type;
		exportColumns = importObject.exportColumns;

		if(!type.equals(MAP_TYPE))
			return null;

		correctCrossLinks(type, exportColumns);

		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		map = Map.createInstance(userId, domainId, exportColumns);

		while(true) {
			importObject = super.readObject();
			if(importObject == null)
				break;
			type = importObject.type;
			exportColumns = importObject.exportColumns;

			correctCrossLinks(type, exportColumns);

			if(type.equals(MARK_TYPE)) {
				me = Mark.createInstance(userId, exportColumns);
				map.addNode((Mark )me);
			}
			else
				if(type.equals(NODELINK_TYPE)) {
					me = NodeLink.createInstance(userId, exportColumns);
					map.addNodeLink((NodeLink )me);
				}
				else
					if(type.equals(LINK_TYPE)) {
						me = PhysicalLink.createInstance(userId, exportColumns);
						map.addPhysicalLink((PhysicalLink )me);
					}
					else
						if(type.equals(NODE_TYPE)) {
							me = TopologicalNode.createInstance(
									userId,
									exportColumns);
							map.addNode((TopologicalNode )me);
						}
						else
							if(type.equals(COLLECTOR_TYPE)) {
								me = Collector.createInstance(
										userId,
										exportColumns);
								map.addCollector((Collector )me);
							}
							else
								if(type.equals(SITE_TYPE)) {
									me = SiteNode.createInstance(
											userId,
											exportColumns);
									map.addNode((SiteNode )me);
								}
								else
									return null;

			StorableObjectPool.putStorableObject((StorableObject )me);
		}

		super.close();

		return map;
	}

	private void correctCrossLinks(String type, java.util.Map exportColumns)
			throws IdentifierGenerationException {
		Object field;
		Object value;

		for(Iterator it = exportColumns.keySet().iterator(); it.hasNext();) {
			field = it.next();
			value = exportColumns.get(field);

			if(type.equals(MAP_TYPE)) {
				if(field.equals(Map.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.MAP_CODE,
							(String )value);
			}
			else
			if(type.equals(MARK_TYPE)) {
				if(field.equals(Mark.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.MARK_CODE,
							(String )value);
				else
				if(field.equals(Mark.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(
							ObjectEntities.PHYSICALLINK_CODE,
							(String )value);
			}
			else
			if(type.equals(NODELINK_TYPE)) {
				if(field.equals(NodeLink.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.NODELINK_CODE,
							(String )value);
				else
					if(field.equals(NodeLink.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(
							ObjectEntities.PHYSICALLINK_CODE,
							(String )value);
				else
				if(field.equals(NodeLink.COLUMN_START_NODE_ID))
					value = super.getClonedId(
							ObjectEntities.SITENODE_CODE,
							(String )value);
				else
				if(field.equals(NodeLink.COLUMN_END_NODE_ID))
					value = super.getClonedId(
							ObjectEntities.SITENODE_CODE,
							(String )value);
			}
			else
			if(type.equals(LINK_TYPE)) {
				if(field.equals(PhysicalLink.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.PHYSICALLINK_CODE,
							(String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_START_NODE_ID))
					value = super.getClonedId(
							ObjectEntities.SITENODE_CODE,
							(String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_END_NODE_ID))
					value = super.getClonedId(
							ObjectEntities.SITENODE_CODE,
							(String )value);
				else
				if(field.equals(PhysicalLink.COLUMN_NODE_LINKS)) {
					List list = (List )value;
					List newList = new ArrayList(list
							.size());
					for(Iterator it2 = list.iterator(); it2
							.hasNext();) {
						String id = (String )it2.next();
						newList.add(super.getClonedId(
								ObjectEntities.NODELINK_CODE,
								id));
					}
					value = newList;
				}
			}
			else
			if(type.equals(NODE_TYPE)) {
				if(field.equals(StorableObjectWrapper.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.TOPOLOGICALNODE_CODE,
							(String )value);
				else
				if(field.equals(TopologicalNodeWrapper.COLUMN_PHYSICAL_LINK_ID))
					value = super.getClonedId(
							ObjectEntities.PHYSICALLINK_CODE,
							(String )value);
			}
			else
			if(type.equals(COLLECTOR_TYPE)) {
				if(field.equals(Collector.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.COLLECTOR_CODE,
							(String )value);
				if(field.equals(Collector.COLUMN_LINKS)) {
					List list = (List )value;
					List newList = new ArrayList(list
							.size());
					for(Iterator it2 = list.iterator(); it2
							.hasNext();) {
						String id = (String )it2.next();
						newList.add(super.getClonedId(
								ObjectEntities.PHYSICALLINK_CODE,
								id));
					}
					value = newList;
				}
			}
			else
			if(type.equals(SITE_TYPE)) {
				if(field.equals(SiteNode.COLUMN_ID))
					value = super.getClonedId(
							ObjectEntities.SITENODE_CODE,
							(String )value);
			}

			exportColumns.put(field, value);
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
