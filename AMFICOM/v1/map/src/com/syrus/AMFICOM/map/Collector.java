/**
 * $Id: Collector.java,v 1.19 2005/01/27 14:43:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */
public class Collector 
	extends StorableObject 
	implements Characterized, MapElement {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4049922679379212598L;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_LINKS = "links";	

	/** 
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;
	
	private String					name;
	private String					description;

	private List					physicalLinks;
	private List					characteristics;

	private StorableObjectDatabase	collectorDatabase;

	protected transient Map map;

	protected transient boolean selected = false;

	protected transient boolean removed = false;

	protected transient boolean alarmState = false;

	public Collector(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinks = new LinkedList();
		this.characteristics = new LinkedList();

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		try {
			this.collectorDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Collector(Collector_Transferable mt) throws CreateObjectException {
		super(mt.header);
		this.name = mt.name;
		this.description = mt.description;

		try {
			this.physicalLinks = new ArrayList(mt.physicalLinkIds.length);
			ArrayList physicalLinkIds = new ArrayList(mt.physicalLinkIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				physicalLinkIds.add(new Identifier(mt.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));

			this.characteristics = new ArrayList(mt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Collector(final Identifier id,
					final Identifier creatorId,
					final String name,
					final String description) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;		

		this.physicalLinks = new LinkedList();
		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
	}

	
	public void insert() throws CreateObjectException {
		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		try {
			if (this.collectorDatabase != null)
				this.collectorDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static Collector createInstance(
			Identifier creatorId,
			Map map,
			String name,
			String description)
		throws CreateObjectException {

		if (creatorId == null || map == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new Collector(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.COLLECTOR_ENTITY_CODE),
				creatorId,
				name,
				description);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Collector.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.physicalLinks);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] physicalLinkIds = new Identifier_Transferable[this.physicalLinks.size()];
		for (Iterator iterator = this.physicalLinks.iterator(); iterator.hasNext();)
			physicalLinkIds[i++] = (Identifier_Transferable) ((PhysicalLink) iterator.next()).getId().getTransferable();
		
		i= 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new Collector_Transferable(super.getHeaderTransferable(),
								this.name,
								this.description,
								physicalLinkIds,
								charIds);
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}	

	public void addCharacteristic(Characteristic characteristic)
	{
		this.characteristics.add(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCharacteristic(Characteristic characteristic)
	{
		this.characteristics.remove(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public String getDescription() {
		return this.description;
	}
	
	protected void setDescription0(String description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.setDescription0(description);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.setName0(name);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getPhysicalLinks() {
		return  Collections.unmodifiableList(this.physicalLinks);
	}
	
	protected void setPhysicalLinks0(List physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
	}
	
	public void setPhysicalLinks(List physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.currentVersion = super.getNextVersion();		
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;					
	}

	/**
	 * Убрать линию из состава коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void removePhysicalLink(PhysicalLink link)
	{
		this.physicalLinks.remove(link);
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Добавить линию в состав коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void addPhysicalLink(PhysicalLink link)
	{
		this.physicalLinks.add(link);
		super.currentVersion = super.getNextVersion();
	}

	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		double x = 0.0;
		double y = 0.0;
		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();){
			PhysicalLink mle = (PhysicalLink )it.next();
			DoublePoint an = mle.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		if (count > 0){			
			x /= count;
			y /= count;
			point.setLocation(x, y);
		}
		
		return point;
	}

	/**
	 * Возвращает суммарную топологическую длинну всех линий в составе 
	 * коллектора в метрах.
	 * @return суммарная длина
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getPhysicalLinks().iterator();
		while( e.hasNext())
		{
			PhysicalLink mle = (PhysicalLink )e.next();
			length = length + mle.getLengthLt();
		}
		return length;
	}

	public boolean isRemoved()
	{
		return this.removed;
	}
	
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	public Map getMap()
	{
		return this.map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized(exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			List physicalLinkIds = new ArrayList(getPhysicalLinks().size());
			for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();)	{
				PhysicalLink link = (PhysicalLink )it.next();
				physicalLinkIds.add(link.getId());
			}
			exportMap.put(COLUMN_LINKS, physicalLinkIds);
			return Collections.unmodifiableMap(exportMap);
		}		
	}

	public static Collector createInstance(Identifier creatorId,
	                                       java.util.Map exportMap)
	                           		throws CreateObjectException {
		Identifier id = (Identifier) exportMap.get(COLUMN_ID);
		String name = (String) exportMap.get(COLUMN_NAME);
		String description = (String) exportMap.get(COLUMN_DESCRIPTION);
		List physicalLinkIds = (List) exportMap.get(COLUMN_LINKS);
		
		if (id == null || creatorId == null || name == null || description == null || physicalLinkIds == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Collector collector = new Collector(id, creatorId, name, description);
			for (Iterator it = physicalLinkIds.iterator(); it.hasNext();) {
				Identifier physicalLinkId = (Identifier) it.next();
				PhysicalLink physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(physicalLinkId,
					false);
				collector.addPhysicalLink(physicalLink);
			}
			return collector;
		} catch (ApplicationException e) {
			throw new CreateObjectException("Collector.createInstance |  ", e);
		}
	}
}
