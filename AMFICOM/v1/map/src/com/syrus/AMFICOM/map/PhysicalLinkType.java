/*-
 * $Id: PhysicalLinkType.java,v 1.66 2005/08/08 11:35:11 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypeHelper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;

/**
 * Тип линии топологической схемы. Существует несколько предустановленных
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #DEFAULT_TUNNEL}, {@link #DEFAULT_COLLECTOR}, {@link #DEFAULT_INDOOR},
 * {@link #DEFAULT_SUBMARINE}, {@link #DEFAULT_OVERHEAD}, {@link #DEFAULT_UNBOUND}
 * @author $Author: arseniy $
 * @version $Revision: 1.66 $, $Date: 2005/08/08 11:35:11 $
 * @module map
 * @todo add 'topological' to constructor
 * @todo make 'topological' persistent
 * @todo make 'sort' persistent (update database scheme as well)
 * @todo make 'mapLibrary' persistent
 * @todo make 'bindingDimension' persistent
 */
public final class PhysicalLinkType extends StorableObjectType 
		implements Characterizable, Namable, LibraryEntry, XMLBeansTransferable {

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

	private PhysicalLinkTypeSort sort;

	private String name;

	/**
	 * Размерность тоннеля. Для тоннеля обозначает размерность матрицы труб в
	 * разрезе, для участка коллектора - число полок и мест на полках
	 *
	 * @todo добавить сохранение в БД (make persistent)
	 */
	private IntDimension bindingDimension;

	private MapLibrary mapLibrary;
	
	private boolean topological;

	PhysicalLinkType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.PHYSICALLINK_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLinkType(final IdlPhysicalLinkType pltt) throws CreateObjectException {
		try {
			this.fromTransferable(pltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PhysicalLinkType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension,
			final boolean topological,
			final MapLibrary mapLibrary) {
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
		this.topological = topological;
		if (bindingDimension == null) {
			this.bindingDimension = new IntDimension(0, 0);
		}
		else {
			this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());
		}
		this.mapLibrary = mapLibrary;
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension,
			final boolean topological,
			final MapLibrary mapLibrary) throws CreateObjectException {

		if (creatorId == null 
				|| codename == null 
				|| name == null 
				|| description == null 
				|| bindingDimension == null 
				|| mapLibrary == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final PhysicalLinkType physicalLinkType = new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICALLINK_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					sort,
					codename,
					name,
					description,
					bindingDimension,
					topological,
					mapLibrary);

			assert physicalLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			physicalLinkType.markAsChanged();

			return physicalLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}
	
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPhysicalLinkType pltt = (IdlPhysicalLinkType) transferable;
		super.fromTransferable(pltt, pltt.codename, pltt.description);
		
		this.name = pltt.name;

		this.sort = pltt.sort;
		this.bindingDimension = new IntDimension(pltt.dimensionX, pltt.dimensionY);
		this.topological = pltt.topological;
		this.mapLibrary = StorableObjectPool.getStorableObject(new Identifier(pltt.mapLibraryId), true);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.mapLibrary);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPhysicalLinkType getTransferable(final ORB orb) {
		return IdlPhysicalLinkTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.sort,
				this.codename,
				this.name,
				this.description,
				this.bindingDimension.getWidth(),
				this.bindingDimension.getHeight(),
				this.topological,
				this.mapLibrary.getId().getTransferable());
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	protected void setDescription0(final String description) {
		this.description = description;
	}

	@Override
	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(final String name) {
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final int width,
			final int height,
			final boolean topological,
			final Identifier mapLibraryId) throws IllegalDataException {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.bindingDimension = new IntDimension(width, height);
		assert name != null: ErrorMessages.NON_NULL_EXPECTED;
		this.name = name;

		this.sort = sort;
		this.topological = topological;
		
		try {
			this.mapLibrary = StorableObjectPool.getStorableObject(mapLibraryId, true);
		} catch (ApplicationException e) {
			throw new IllegalDataException(e);
		}
	}

	@Override
	public void setCodename(final String codename) {
		super.setCodename(codename);
	}

	protected void setBindingDimension0(final IntDimension bindingDimension) {
		this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());
	}

	public void setBindingDimension(final IntDimension bindingDimension) {
		this.setBindingDimension0(bindingDimension);
		super.markAsChanged();
	}

	public IntDimension getBindingDimension() {
		return new IntDimension(this.bindingDimension);
	}

	@Override
	protected void setCodename0(final String codename) {
		super.setCodename0(codename);
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	public PhysicalLinkTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final PhysicalLinkTypeSort sort) {
		this.sort = sort;
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = com.syrus.amficom.map.xml.PhysicalLinkType.Factory.newInstance();
		this.fillXMLTransferable(xmlPhysicalLinkType);
		return xmlPhysicalLinkType;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType) xmlObject;

		final com.syrus.amficom.general.xml.UID uid = xmlPhysicalLinkType.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlPhysicalLinkType.setName(this.name);
		xmlPhysicalLinkType.setDescription(this.description);
		xmlPhysicalLinkType.setSort(com.syrus.amficom.map.xml.PhysicalLinkTypeSort.Enum.forInt(this.sort.value()));

		xmlPhysicalLinkType.setDimensionX(BigInteger.valueOf(this.getBindingDimension().getWidth()));
		xmlPhysicalLinkType.setDimensionY(BigInteger.valueOf(this.getBindingDimension().getHeight()));
	}

	PhysicalLinkType(final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.PHYSICALLINK_CODE, xmlPhysicalLinkType.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.fromXMLTransferable(xmlPhysicalLinkType, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType) xmlObject;

		this.name = xmlPhysicalLinkType.getName();
		this.description = xmlPhysicalLinkType.getDescription();
		this.bindingDimension = new IntDimension(xmlPhysicalLinkType.getDimensionX().intValue(),
				xmlPhysicalLinkType.getDimensionY().intValue());
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType) xmlObject;

		try {
			final PhysicalLinkType physicalLinkType = new PhysicalLinkType(creatorId,
					StorableObjectVersion.createInitial(),
					xmlPhysicalLinkType.getSort().toString(),
					xmlPhysicalLinkType.getDescription(),
					xmlPhysicalLinkType,
					clonedIdsPool);
			assert physicalLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			physicalLinkType.markAsChanged();
			return physicalLinkType;
		} catch (Exception e) {
			System.out.println(xmlPhysicalLinkType);
			throw new CreateObjectException("PhysicalLinkType.createInstance |  ", e);
		}
	}

	public void setParent(Library library) {
		this.mapLibrary = (MapLibrary)library;
	}

	public Library getParent() {
		return this.mapLibrary;
	}

	public MapLibrary getMapLibrary() {
		return this.mapLibrary;
	}

	public void setMapLibrary(MapLibrary mapLibrary) {
		this.mapLibrary = mapLibrary;
	}
}
