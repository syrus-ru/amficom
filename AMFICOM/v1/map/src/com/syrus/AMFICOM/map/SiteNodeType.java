/*-
 * $Id: SiteNodeType.java,v 1.27 2005/04/14 16:00:33 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

/**
 * Тип сетевого узла топологической схемы. Существует несколько 
 * предустановленных  типов сетевых узлов, которые определяются полем 
 * {@link #codename}, соответствующим какому-либо значению {@link #WELL}, 
 * {@link #PIQUET}, {@link #ATS}, {@link #BUILDING}, {@link #UNBOUND}, 
 * {@link #CABLE_INLET}, {@link #TOWER}
 * @author $Author: bass $
 * @version $Revision: 1.27 $, $Date: 2005/04/14 16:00:33 $
 * @module map_v1
 */
public class SiteNodeType extends StorableObjectType implements Characterizable, Namable {

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

	private Set characteristics;

	private Identifier imageId;
	private String name;
	private boolean topological;

	SiteNodeType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	SiteNodeType(SiteNodeType_Transferable sntt) throws CreateObjectException {
		try {
			this.fromTransferable(sntt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	SiteNodeType(final Identifier id,
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

		this.characteristics = new HashSet();
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

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		SiteNodeType_Transferable sntt = (SiteNodeType_Transferable) transferable;
		super.fromTransferable(sntt.header, sntt.codename, sntt.description);

		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;

		Set ids = Identifier.fromTransferables(sntt.characteristicIds);
		this.characteristics = GeneralStorableObjectPool.getStorableObjects(ids, true);
	}

	public Set getDependencies() {
		return Collections.singleton(this.imageId);
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

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);
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

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
}
