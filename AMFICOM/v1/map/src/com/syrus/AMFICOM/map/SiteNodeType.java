/**
 * $Id: SiteNodeType.java,v 1.13 2005/01/27 14:43:37 krupenn Exp $
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Тип сетевого узла топологической схемы. Существует несколько 
 * предустановленных  типов сетевых узлов, которые определяются полем 
 * {@link #codename}, соответствующим какому-либо значению {@link #WELL}, 
 * {@link #PIQUET}, {@link #ATS}, {@link #BUILDING}, {@link #UNBOUND}, 
 * {@link #CABLE_INLET}, {@link #TOWER}
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */
public class SiteNodeType extends StorableObjectType implements Characterized {

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
	private static final long	serialVersionUID	= 3690481316080464696L;

	private List					characteristics;

	private Identifier				imageId;

	private String					name;

	private boolean					topological;

	private StorableObjectDatabase	siteNodeTypeDatabase;

	public SiteNodeType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeTypeDatabase = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			this.siteNodeTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
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
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNodeType(final Identifier id,
			final Identifier creatorId,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.siteNodeTypeDatabase = MapDatabaseContext.getSiteNodeTypeDatabase();
	}

	public void insert() throws CreateObjectException {
		this.siteNodeTypeDatabase = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			if (this.siteNodeTypeDatabase != null)
				this.siteNodeTypeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static SiteNodeType createInstance(
			final Identifier creatorId,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean isTopological) 
		throws CreateObjectException {
		
		if (creatorId == null || codename == null || name == null 
				|| description == null || imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new SiteNodeType(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE),
				creatorId,
				codename,
				name,
				description,
				imageId,
				isTopological);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("SiteNodeType.createInstance | cannot generate identifier ", e);
		}
	}

	public List getCharacteristics() {
		return  Collections.unmodifiableList(this.characteristics);
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

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.imageId);
		dependencies.addAll(this.characteristics);
		return dependencies;
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
		return new SiteNodeType_Transferable(super.getHeaderTransferable(), this.codename, this.name, this.description,
												(Identifier_Transferable) this.imageId.getTransferable(),
												this.topological, charIds);
	}

	public boolean isTopological() {
		return this.topological;
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
	
	public void setDescription(final String description) {
		super.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(final String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  String codename,
											  String name,
											  String description,
											  Identifier imageId,
											  boolean topological) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					codename,
					description);
			this.name = name;
			this.imageId = imageId;
			this.topological = topological;		
	}
}
