/**
 * $Id: Map.java,v 1.23 2005/01/14 10:26:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;


/**
 * Класс используется для хранения и информации по канализационной
 * прокладке кабелей и положению узлов и других топологических объектов
 * 
 * 
 * 
 * @version $Revision: 1.23 $, $Date: 2005/01/14 10:26:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public abstract class Map
{

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
