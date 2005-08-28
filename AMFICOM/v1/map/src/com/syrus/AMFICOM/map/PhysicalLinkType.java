/*-
 * $Id: PhysicalLinkType.java,v 1.77 2005/08/28 19:17:54 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUIDMapDatabase;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypeHelper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkType;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * Тип линии топологической схемы. Существует несколько предустановленных
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #DEFAULT_TUNNEL}, {@link #DEFAULT_COLLECTOR}, {@link #DEFAULT_INDOOR},
 * {@link #DEFAULT_SUBMARINE}, {@link #DEFAULT_OVERHEAD}, {@link #DEFAULT_UNBOUND}
 * @author $Author: bass $
 * @version $Revision: 1.77 $, $Date: 2005/08/28 19:17:54 $
 * @module map
 */
public final class PhysicalLinkType extends StorableObjectType 
		implements Characterizable, Namable, LibraryEntry, XmlBeansTransferable<XmlPhysicalLinkType> {

	/** тоннель */
	public static final String DEFAULT_TUNNEL = "defaulttunnel";
	/** участок коллектора */
	public static final String DEFAULT_COLLECTOR = "defaultcollector";
	/** внутренняя проводка */
	public static final String DEFAULT_INDOOR = "defaultindoor";
	/** подводная линия */
	public static final String DEFAULT_SUBMARINE = "defaultsubmarine";
	/** навесная линия */
	public static final String DEFAULT_OVERHEAD = "defaultoverhead";
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
	 */
	private IntDimension bindingDimension;

	private Identifier mapLibraryId;
	
	private boolean topological;

	private transient CharacterizableDelegate characterizableDelegate;

	PhysicalLinkType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(PHYSICALLINK_TYPE_CODE).retrieve(this);
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
			final Identifier mapLibraryId) {
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
		this.mapLibraryId = mapLibraryId;
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension,
			final boolean topological,
			final Identifier mapLibraryId) throws CreateObjectException {

		assert creatorId != null 
				&& codename != null 
				&& name != null 
				&& description != null 
				&& bindingDimension != null 
				&& mapLibraryId != null : NON_NULL_EXPECTED;
		assert !mapLibraryId.isVoid() : NON_VOID_EXPECTED;
		
		try {
			final PhysicalLinkType physicalLinkType = new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(PHYSICALLINK_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					sort,
					codename,
					name,
					description,
					bindingDimension,
					topological,
					mapLibraryId);

			assert physicalLinkType.isValid() : OBJECT_STATE_ILLEGAL;

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
		this.mapLibraryId = new Identifier(pltt.mapLibraryId);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.mapLibraryId);
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
				this.mapLibraryId.getTransferable());
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setTopological(final boolean topological) {
		this.setTopological0(topological);
		super.markAsChanged();
	}
	
	protected void setTopological0(final boolean topological) {
		this.topological = topological;
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
			final Identifier mapLibraryId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.bindingDimension = new IntDimension(width, height);
		assert name != null: NON_NULL_EXPECTED;
		this.name = name;

		this.sort = sort;
		this.topological = topological;
		
		this.mapLibraryId = mapLibraryId;
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

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	public PhysicalLinkTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final PhysicalLinkTypeSort sort) {
		this.setSort0(sort);
		super.markAsChanged();
	}
	
	protected void setSort0(final PhysicalLinkTypeSort sort) {
		this.sort = sort;
	}

	public XmlPhysicalLinkType getXmlTransferable() {
		final XmlPhysicalLinkType xmlPhysicalLinkType = XmlPhysicalLinkType.Factory.newInstance();
		final XmlIdentifier uid = xmlPhysicalLinkType.addNewId();
		uid.setStringValue(this.id.toString());
		xmlPhysicalLinkType.setName(this.name);
		xmlPhysicalLinkType.setDescription(this.description);
		xmlPhysicalLinkType.setSort(XmlPhysicalLinkTypeSort.Enum.forInt(this.sort.value()));
		
		xmlPhysicalLinkType.setDimensionX(BigInteger.valueOf(this.getBindingDimension().getWidth()));
		xmlPhysicalLinkType.setDimensionY(BigInteger.valueOf(this.getBindingDimension().getHeight()));
		return xmlPhysicalLinkType;
	}

	PhysicalLinkType(final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final XmlPhysicalLinkType xmlPhysicalLinkType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(PHYSICALLINK_TYPE_CODE, xmlPhysicalLinkType.getId().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.fromXmlTransferable(xmlPhysicalLinkType, clonedIdsPool, importType);
	}

	public void fromXmlTransferable(final XmlPhysicalLinkType xmlPhysicalLinkType, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.name = xmlPhysicalLinkType.getName();
		this.description = xmlPhysicalLinkType.getDescription();
		this.bindingDimension = new IntDimension(xmlPhysicalLinkType.getDimensionX().intValue(),
				xmlPhysicalLinkType.getDimensionY().intValue());
	}

	public static PhysicalLinkType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPhysicalLinkType xmlPhysicalLinkType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			String uid = xmlPhysicalLinkType.getId().getStringValue();
			Identifier existingIdentifier = ImportUIDMapDatabase.retrieve(importType, uid);
			PhysicalLinkType physicalLinkType = null;
			if(existingIdentifier != null) {
				physicalLinkType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(physicalLinkType != null) {
					clonedIdsPool.setExistingId(uid, existingIdentifier);
					physicalLinkType.fromXmlTransferable(xmlPhysicalLinkType, clonedIdsPool, importType);
				}
				else{
					ImportUIDMapDatabase.delete(importType, uid);
				}
			}
			if(physicalLinkType == null) {
				physicalLinkType = new PhysicalLinkType(creatorId,
						StorableObjectVersion.createInitial(),
						xmlPhysicalLinkType.getCodename(),
						xmlPhysicalLinkType.getDescription(),
						xmlPhysicalLinkType,
						clonedIdsPool, 
						importType);
				ImportUIDMapDatabase.insert(importType, uid, physicalLinkType.id);
			}
			assert physicalLinkType.isValid() : OBJECT_STATE_ILLEGAL;
			physicalLinkType.markAsChanged();
			return physicalLinkType;
		} catch (Exception e) {
			System.out.println(xmlPhysicalLinkType);
			throw new CreateObjectException("PhysicalLinkType.createInstance |  ", e);
		}
	}
	
	Identifier getMapLibraryId() {
		return this.mapLibraryId;
	}

	public void setParent(Library library) {
		setParent0(library);
		super.markAsChanged();
	}
	
	protected void setParent0(Library library) {
		MapLibrary mapLibrary = (MapLibrary)library;
		setMapLibrary0(mapLibrary);
	}

	public Library getParent() {
		return getMapLibrary();
	}

	public MapLibrary getMapLibrary() {
		try {
			return StorableObjectPool.getStorableObject(this.getMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public void setMapLibrary(MapLibrary mapLibrary) {
		this.setMapLibrary0(mapLibrary);
		super.markAsChanged();
	}
	
	protected void setMapLibrary0(MapLibrary mapLibrary) {
		this.mapLibraryId = mapLibrary.getId();
	}
}
