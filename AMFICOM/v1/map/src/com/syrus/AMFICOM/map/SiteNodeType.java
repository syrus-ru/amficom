/**
 * $Id: SiteNodeType.java,v 1.18 2005/03/24 14:23:03 arseniy Exp $
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
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

/**
 * Тип сетевого узла топологической схемы. Существует несколько 
 * предустановленных  типов сетевых узлов, которые определяются полем 
 * {@link #codename}, соответствующим какому-либо значению {@link #WELL}, 
 * {@link #PIQUET}, {@link #ATS}, {@link #BUILDING}, {@link #UNBOUND}, 
 * {@link #CABLE_INLET}, {@link #TOWER}
 * @author $Author: arseniy $
 * @version $Revision: 1.18 $, $Date: 2005/03/24 14:23:03 $
 * @module map_v1
 */
public class SiteNodeType extends StorableObjectType implements Characterizable {

	public static final String WELL = "well";
	public static final String PIQUET = "piquet";
	public static final String ATS = "ats";
	public static final String BUILDING = "building";
	public static final String UNBOUND = "unbound";
	public static final String CABLE_INLET = "cableinlet";
	public static final String TOWER = "tower";

	public static final String WELL_IMAGE = "images/well.gif";
	public static final String PIQUET_IMAGE = "images/piquet.gif";
	public static final String ATS_IMAGE = "images/ats.gif";
	public static final String BUILDING_IMAGE = "images/building.gif";
	public static final String UNBOUND_IMAGE = "images/unbound.gif";
	public static final String CABLE_INLET_IMAGE = "images/cableinlet.gif";
	public static final String TOWER_IMAGE = "images/tower.gif";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690481316080464696L;

	private Collection characteristics;

	private Identifier imageId;
	private String name;
	private boolean topological;

	private StorableObjectDatabase siteNodeTypeDatabase;

	public SiteNodeType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeTypeDatabase = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			this.siteNodeTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNodeType(SiteNodeType_Transferable sntt) throws CreateObjectException {
		super(sntt.header, sntt.codename, sntt.description);
		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;

		try {
			this.characteristics = new ArrayList(sntt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(sntt.characteristicIds.length);
			for (int i = 0; i < sntt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(sntt.characteristicIds[i]));

			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNodeType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;

		this.characteristics = new LinkedList();

		this.siteNodeTypeDatabase = MapDatabaseContext.getSiteNodeTypeDatabase();
	}

	public static SiteNodeType createInstance(final Identifier creatorId,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean isTopological) throws CreateObjectException {

		if (creatorId == null || codename == null || name == null || description == null || imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			SiteNodeType siteNodeType = new SiteNodeType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					name,
					description,
					imageId,
					isTopological);
			siteNodeType.changed = true;
			return siteNodeType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("SiteNodeType.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		return Collections.singletonList(this.imageId);
	}

	public String getDescription() {
		return this.description;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public String getName() {
		return this.name;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		return new SiteNodeType_Transferable(super.getHeaderTransferable(),
				this.codename,
				this.name,
				this.description,
				(Identifier_Transferable) this.imageId.getTransferable(),
				this.topological,
				charIds);
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setDescription(final String description) {
		super.description = description;
		this.changed = true;
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		this.changed = true;
	}

	public void setName(final String name) {
		this.name = name;
		this.changed = true;
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		this.changed = true;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String codename,
			String name,
			String description,
			Identifier imageId,
			boolean topological) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;
	}

	public Collection getCharacteristics() {
		return Collections.unmodifiableCollection(this.characteristics);
	}

	public void addCharacteristic(Characteristic characteristic) {
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(final Collection characteristics) {
		this.setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SITE_NODE_TYPE;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(final Collection characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
}
