/*
 * $Id: MapExportCommand.java,v 1.12 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExportCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.XMLMapObjectLoader;

/**
 * Класс $RCSfile: MapExportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class MapExportCommand extends ExportCommand
{
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
	
	static
	{
		typesMap.put(ObjectEntities.MAP_ENTITY, MAP_TYPE);
		typesMap.put(ObjectEntities.MARK_ENTITY, MARK_TYPE);
		typesMap.put(ObjectEntities.SITE_NODE_ENTITY, SITE_TYPE);
		typesMap.put(ObjectEntities.TOPOLOGICAL_NODE_ENTITY, NODE_TYPE);
		typesMap.put(ObjectEntities.NODE_LINK_ENTITY, NODELINK_TYPE);
		typesMap.put(ObjectEntities.COLLECTOR_ENTITY, COLLECTOR_TYPE);
		typesMap.put(ObjectEntities.PHYSICAL_LINK_ENTITY, LINK_TYPE);
	}

	public MapExportCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null)
			return;

        System.out.println("Closing map");

		Map map = this.mapFrame.getMap();
		
		String fileName = ExportCommand.openFileForWriting(MapPropertiesManager.getLastDirectory());
		if(fileName == null)
			return;
		File file = new File(fileName);
		MapPropertiesManager.setLastDirectory(file.getParent());
		
		String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));

		if(ext == null)
		{
			ext = ".xml";
		}
		
		if(ext.equals(".xml"))
		{
			saveXML(map, file);
		}
		else
		if(ext.equals(".esf"))
		{
			saveESF(map, fileName);
		}
//        mapFrame.setTitle(LangModelMap.getString("Map"));
		setResult(Command.RESULT_OK);
	}

	protected void saveXML(Map map, File file)
	{
		XMLMapObjectLoader xmlLoader = new XMLMapObjectLoader(file);
		
		try
		{
			xmlLoader.saveMap(map, true);
		}
		catch (VersionCollisionException e)
		{
			e.printStackTrace();
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}

	protected void saveESF(Map map, String fileName)
	{
		java.util.Map exportColumns = null;

		super.open(fileName);
		
		super.startObject(MAP_TYPE);
		exportColumns = map.getExportMap();
		for(Iterator it = exportColumns.keySet().iterator(); it.hasNext();)
		{
			Object key = it.next();
			super.put(key, exportColumns.get(key));
		}
		super.endObject();

		for(Iterator it = map.getAllElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			String entityCodeString = ObjectEntities.codeToString(me.getId().getMajor());
			super.startObject((String )typesMap.get(entityCodeString));
			exportColumns = me.getExportMap();
			for(Iterator it2 = exportColumns.keySet().iterator(); it2.hasNext();)
			{
				Object key = it2.next();
				super.put(key, exportColumns.get(key));
			}
			super.endObject();
		}
		
		super.close();
	}
}
