/*-
 * $Id: PhysicalLinkType.java,v 1.39 2005/05/26 15:31:16 bass Exp $
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
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;

/**
 * Тип линии топологической схемы. Существует несколько предустановленных
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #DEFAULT_TUNNEL}, {@link #DEFAULT_COLLECTOR}, {@link #DEFAULT_INDOOR},
 * {@link #DEFAULT_SUBMARINE}, {@link #DEFAULT_OVERHEAD}, {@link #DEFAULT_UNBOUND}
 * @author $Author: bass $
 * @version $Revision: 1.39 $, $Date: 2005/05/26 15:31:16 $
 * @module map_v1
 * @todo add 'topological' to constructor
 * @todo make 'topological' persistent
 * @todo make 'sort' transient (update database scheme as well)
 */
public class PhysicalLinkType extends StorableObjectType implements Characterizable, Namable {

	/** тоннель */
	public static final String DEFAULT_TUNNEL = "tunnel";
	/** участок коллектора */
	public static final String DEFAULT_COLLECTOR = "collector";
	/** внутренняя проводка */
	public static final String DEFAULT_INDOOR = "indoor";
	/** подводная линия */
	public static final String DEFAULT_SUBMARINE = "submarine";
	/** навесная линия */
	public static final String DEFAULT_OVERHEAD = "overhead";
	/** непривязанный кабель */
	public static final String DEFAULT_UNBOUND = "cable";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690191057812271924L;

	private transient PhysicalLinkTypeSort sort;
	
	private Set characteristics;

	private String name;

	/**
	 * Размерность тоннеля. Для тоннеля обозначает размерность матрицы труб в
	 * разрезе, для участка коллектора - число полок и мест на полках
	 *
	 * @todo добавить сохранение в БД
	 */
	private IntDimension 			bindingDimension;

	private transient boolean topological = true;

	PhysicalLinkType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	PhysicalLinkType(PhysicalLinkType_Transferable pltt) throws CreateObjectException {
		try {
			this.fromTransferable(pltt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PhysicalLinkType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.sort = sort;
		this.name = name;
		if (bindingDimension == null)
			this.bindingDimension = new IntDimension(0, 0);
		else
			this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());

		this.characteristics = new HashSet();
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) throws CreateObjectException {

		if (creatorId == null || codename == null || name == null || description == null || bindingDimension == null || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLinkType physicalLinkType = new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE),
					creatorId,
					0L,
					sort,
					codename,
					name,
					description,
					bindingDimension);
			physicalLinkType.changed = true;
			return physicalLinkType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		PhysicalLinkType_Transferable pltt = (PhysicalLinkType_Transferable) transferable;
		super.fromTransferable(pltt.header, pltt.codename, pltt.description);

		this.name = pltt.name;

		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(pltt.codename);

		Set ids = Identifier.fromTransferables(pltt.characteristicIds);
		this.characteristics = StorableObjectPool.getStorableObjects(ids, true);
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);
		return new PhysicalLinkType_Transferable(super.getHeaderTransferable(),
				this.codename,
				this.name,
				this.description,
				this.bindingDimension.getWidth(),
				this.bindingDimension.getHeight(),
				charIds);
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.setDescription0(description);
		this.changed = true;
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		this.changed = true;
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
	}

	synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String codename,
			String name,
			String description,
			int width,
			int height) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.bindingDimension = new IntDimension(width, height);
		this.name = name;

		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(codename);
	}

	public void setCodename(String codename) {
		super.setCodename(codename);
		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(codename);
	}

	protected void setBindingDimension0(IntDimension bindingDimension) {
		this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());
	}

	public void setBindingDimension(IntDimension bindingDimension) {
		this.setBindingDimension0(bindingDimension);
		this.changed = true;
	}

	public IntDimension getBindingDimension() {
		return new IntDimension(this.bindingDimension);
	}

	protected void setCodename0(String codename) {
		super.setCodename0(codename);
	}

	public Set getCharacteristics() {
		return  Collections.unmodifiableSet(this.characteristics);
	}
	
	public void addCharacteristic(Characteristic characteristic){
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic characteristic)
	{
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
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public PhysicalLinkTypeSort getSort() {
		return this.sort;
	}

	public void setSort(PhysicalLinkTypeSort sort) {
		this.sort = sort;
	}
}
