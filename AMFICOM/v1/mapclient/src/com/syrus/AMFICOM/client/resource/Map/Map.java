/**
 * $Id: Map.java,v 1.21 2004/12/22 16:17:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.map.MapElement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import java.io.Serializable;

/**
 * Класс используется для хранения и информации по канализационной
 * прокладке кабелей и положению узлов и других топологических объектов
 * 
 * 
 * 
 * @version $Revision: 1.21 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class Map extends com.syrus.AMFICOM.map.Map implements Serializable
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_MODIFIED_BY = "modified_by";
	
	/** 
	 * массив параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	public static String[][] exportColumns = null;
	
	/**
	 * Используется для создания нового контекста пользователем
	 */
	public Map()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("map"));
//		Environment.log(
//				Environment.LOG_LEVEL_FINER, 
//				"constructor call", 
//				getClass().getName(), 
//				"Map()");
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapPanel";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

/*
	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[3][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		
		return exportColumns;
	}

	public void setColumn(String field, String value)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setColumn(" + field + ", "+ value + ")");
		if(field.equals(COLUMN_ID))
			this.setId(value);
		else
		if(field.equals(COLUMN_NAME))
			this.setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			this.setDescription(value);
	}
*/
	/**
	 * Клонирование объекта - оспользуется при сохранении контекста карты
	 * под новым именем. При этом для сохранения ссылок на другие клонируемые
	 * (или клонированные) объекты используется хранилище 
	 * 		Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id
	 * в котором по ключу старого Id хранится Id нового клонированного объекта
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"clone(" + dataSource + ")");

		String clonedId = (String )Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);

		if (clonedId != null)
			return Pool.get(Map.typ, clonedId);

		Map mc = (Map )super.clone();

		mc.createdBy = mc.userId;
		mc.description = description;
		mc.domainId = domainId;
		mc.id = dataSource.GetUId(Map.typ);
		mc.modified = mc.created;
		mc.modifiedBy = mc.userId;
		mc.name = name + "(copy)";
		mc.userId = dataSource.getSession().getUserId();

		Pool.put(Map.typ, mc.getId(), mc);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mc.getId());

		mc.nodeLinks = new LinkedList();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
			mc.nodeLinks.add(((MapElement )it.next()).clone(dataSource));
			
		mc.nodes = new LinkedList();
		for(Iterator it = nodes.iterator(); it.hasNext();)
			mc.nodes.add(((MapElement)it.next()).clone(dataSource));
			
		mc.physicalLinks = new LinkedList();
		for(Iterator it = physicalLinks.iterator(); it.hasNext();)
			mc.physicalLinks.add((((MapElement)it.next()).clone(dataSource)));

		mc.collectors = new LinkedList();
		for(Iterator it = collectors.iterator(); it.hasNext();)
			mc.collectors.add(((MapElement)it.next()).clone(dataSource));
			
		mc.markIds = new LinkedList();
		for(Iterator it = markIds.iterator(); it.hasNext();)
			mc.markIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.nodelinkIds = new LinkedList();
		for(Iterator it = nodelinkIds.iterator(); it.hasNext();)
			mc.nodelinkIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.nodeIds = new LinkedList();
		for(Iterator it = nodeIds.iterator(); it.hasNext();)
			mc.nodeIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.siteIds = new LinkedList();
		for(Iterator it = siteIds.iterator(); it.hasNext();)
			mc.siteIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.linkIds = new LinkedList();
		for(Iterator it = linkIds.iterator(); it.hasNext();)
			mc.linkIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		mc.collectorIds = new LinkedList();
		for(Iterator it = collectorIds.iterator(); it.hasNext();)
			mc.collectorIds.add(Pool.get(
					MapPropertiesManager.MAP_CLONED_IDS, 
					(String )it.next()));
			
		return mc;
	}
*/	
}
