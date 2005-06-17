/*-
 * $Id: SiteNodeType.java,v 1.38 2005/06/17 12:40:40 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

/**
 * Тип сетевого узла топологической схемы. Существует несколько
 * предустановленных  типов сетевых узлов, которые определяются полем
 * {@link #codename}, соответствующим какому-либо значению {@link #DEFAULT_WELL},
 * {@link #DEFAULT_PIQUET}, {@link #DEFAULT_ATS}, {@link #DEFAULT_BUILDING}, {@link #DEFAULT_UNBOUND},
 * {@link #DEFAULT_CABLE_INLET}, {@link #DEFAULT_TOWER}
 * @author $Author: bass $
 * @version $Revision: 1.38 $, $Date: 2005/06/17 12:40:40 $
 * @module map_v1
 * @todo make 'sort' persistent (update database scheme as well)
 */
public class SiteNodeType extends StorableObjectType implements Characterizable, Namable {

	public static final String DEFAULT_WELL = "well";
	public static final String DEFAULT_PIQUET = "piquet";
	public static final String DEFAULT_ATS = "ats";
	public static final String DEFAULT_BUILDING = "building";
	public static final String DEFAULT_UNBOUND = "unbound";
	public static final String DEFAULT_CABLE_INLET = "cableinlet";
	public static final String DEFAULT_TOWER = "tower";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690481316080464696L;

	private Set characteristics;

	private Identifier imageId;
	private String name;
	private boolean topological;

	private transient SiteNodeTypeSort sort;

	SiteNodeType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		final SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITENODE_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	SiteNodeType(final SiteNodeType_Transferable sntt) throws CreateObjectException {
		try {
			this.fromTransferable(sntt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	SiteNodeType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final SiteNodeTypeSort sort,
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
		this.sort = sort;

		this.characteristics = new HashSet();
	}

	public static SiteNodeType createInstance(final Identifier creatorId,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean isTopological) throws CreateObjectException {

		if (creatorId == null || codename == null || name == null || description == null || imageId == null || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			SiteNodeType siteNodeType = new SiteNodeType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_TYPE_CODE),
					creatorId,
					0L,
					sort,
					codename,
					name,
					description,
					imageId,
					isTopological);

			assert siteNodeType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			siteNodeType.markAsChanged();

			return siteNodeType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		SiteNodeType_Transferable sntt = (SiteNodeType_Transferable) transferable;
		super.fromTransferable(sntt.header, sntt.codename, sntt.description);

		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;

		//@todo retreive from transferable!
		this.sort = SiteNodeTypeSort.fromString(sntt.codename);

		Set ids = Identifier.fromTransferables(sntt.characteristicIds);
		this.characteristics = StorableObjectPool.getStorableObjects(ids, true);
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
		super.markAsChanged();
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;

		//@todo retreive from transferable!
		this.sort = SiteNodeTypeSort.fromString(codename);
	}

	public void setCodename(final String codename) {
		super.setCodename(codename);
		//@todo retreive from transferable!
		this.sort = SiteNodeTypeSort.fromString(codename);
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(final Characteristic characteristic) {
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
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

	public SiteNodeTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final SiteNodeTypeSort sort) {
		this.sort = sort;
	}
}
