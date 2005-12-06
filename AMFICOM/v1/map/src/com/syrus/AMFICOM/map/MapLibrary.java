/*-
 * $Id: MapLibrary.java,v 1.40 2005/12/06 09:43:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.UNSUPPORTED_CHILD_TYPE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlMapLibrary;
import com.syrus.AMFICOM.map.corba.IdlMapLibraryHelper;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkType;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSeq;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.40 $, $Date: 2005/12/06 09:43:34 $
 * @author $Author: bass $
 * @module map
 */
public final class MapLibrary extends StorableObject<MapLibrary>
		implements Namable, Library, XmlBeansTransferable<XmlMapLibrary> {
	private static final long	serialVersionUID	= -8616969914711251336L;

	private String name;
	private String codename;
	private String description;
	private Identifier parentMapLibraryId;

	public MapLibrary(final IdlMapLibrary mlt) throws CreateObjectException {
		try {
			this.fromTransferable(mlt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected MapLibrary(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String name,
			final String codename,
			final String description,
			final MapLibrary parentMapLibrary) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.parentMapLibraryId = Identifier.possiblyVoid(parentMapLibrary);
	}

	public static MapLibrary createInstance(final Identifier creatorId, final MapLibrary parentMapLibrary) throws CreateObjectException {
		MapLibrary newInstance = MapLibrary.createInstance(creatorId, "", "", "", parentMapLibrary);
		newInstance.codename = newInstance.id.toString();
		return newInstance;
	}

	public static MapLibrary createInstance(final Identifier creatorId, final String name, final MapLibrary parentMapLibrary) throws CreateObjectException {
		MapLibrary newInstance = MapLibrary.createInstance(creatorId, name, "", "",parentMapLibrary);
		newInstance.codename = newInstance.id.toString();
		return newInstance;
	}

	public static MapLibrary createInstance(final Identifier creatorId,
			final String name,
			final String codename,
			final String description,
			final MapLibrary parentMapLibrary) throws CreateObjectException {
		assert creatorId != null && name != null && codename != null && description != null : NON_NULL_EXPECTED;
			
		try {
			final MapLibrary mapLibrary = new MapLibrary(IdentifierPool.getGeneratedIdentifier(MAPLIBRARY_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					name,
					codename,
					description,
					parentMapLibrary);

			assert mapLibrary.isValid() : OBJECT_BADLY_INITIALIZED;

			mapLibrary.markAsChanged();

			return mapLibrary;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}
	
	protected void setName0(final String name) {
		this.name = name;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(final String codename) {
		this.setCodename0(codename);
		super.markAsChanged();
	}
	
	protected void setCodename0(final String codename) {
		this.codename = codename;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}
	
	protected void setDescription0(final String description) {
		this.description = description;
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMapLibrary mlt = (IdlMapLibrary) transferable;
		super.fromTransferable(mlt);

		this.name = mlt.name;
		this.codename = mlt.codename;
		this.description = mlt.description;

		this.parentMapLibraryId = new Identifier(mlt.parentMapLibraryId);
	}

	@Override
	public IdlMapLibrary getIdlTransferable(final ORB orb) {
		return IdlMapLibraryHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.codename,
				this.description,
				this.parentMapLibraryId.getIdlTransferable());
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String codename,
			final String description,
			final Identifier parentMapLibraryId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.parentMapLibraryId = parentMapLibraryId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		if (this.parentMapLibraryId != null && 
				!this.parentMapLibraryId.isVoid()) {
			final Set<Identifiable> dependencies = new HashSet<Identifiable>(1);
			dependencies.add(this.parentMapLibraryId);
			return Collections.unmodifiableSet(dependencies);
		}
		return Collections.emptySet();
	}

	public void addChild(final LibraryEntry libraryEntry) {
		assert libraryEntry != null : NON_NULL_EXPECTED;
		if (libraryEntry instanceof PhysicalLinkType
				|| libraryEntry instanceof SiteNodeType
				|| libraryEntry instanceof MapLibrary) {
			libraryEntry.setParent(this);
		} else {
			throw new UnsupportedOperationException(UNSUPPORTED_CHILD_TYPE);
		}
	}

	public void removeChild(final LibraryEntry libraryEntry) {
		if (libraryEntry instanceof PhysicalLinkType
				|| libraryEntry instanceof SiteNodeType
				|| libraryEntry instanceof MapLibrary) {
			libraryEntry.setParent(null);
			super.markAsChanged();
		}
	}

	public void setParent(final Library library) {
		this.setParent0(library);
		super.markAsChanged();
	}
	
	protected void setParent0(final Library library) {
		assert library instanceof MapLibrary : "must be instance of MapLibrary";
		assert library != this : CIRCULAR_DEPS_PROHIBITED;
		this.parentMapLibraryId = Identifier.possiblyVoid((MapLibrary) library);
	}

	public Set<SiteNodeType> getSiteNodeTypes() {
		StorableObjectCondition siteNodeTypeCondition = new LinkedIdsCondition(
				getId(),
				SITENODE_TYPE_CODE);
		try {
			return StorableObjectPool.getStorableObjectsByCondition(
					siteNodeTypeCondition,
					true);
		} catch(ApplicationException e) {
			Log.debugMessage(e, SEVERE);
			return Collections.emptySet();
		}
	}

	public Set<PhysicalLinkType> getPhysicalLinkTypes() {
		StorableObjectCondition physicalLinkTypeCondition = new LinkedIdsCondition(
				getId(),
				PHYSICALLINK_TYPE_CODE);
		try {
			return StorableObjectPool.getStorableObjectsByCondition(
					physicalLinkTypeCondition,
					true);
		} catch(ApplicationException e) {
			Log.debugMessage(e, SEVERE);
			return Collections.emptySet();
		}
	}

	public Set<LibraryEntry> getChildren() {
		Set<SiteNodeType> siteNodeTypes = getSiteNodeTypes();
		Set<PhysicalLinkType> physicalLinkTypes = getPhysicalLinkTypes();
		final Set<LibraryEntry> children = new HashSet<LibraryEntry>(
				physicalLinkTypes.size() + siteNodeTypes.size());
		children.addAll(physicalLinkTypes);
		children.addAll(siteNodeTypes);
		return children;
	}

	public MapLibrary getParent() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param mapLibrary
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlMapLibrary mapLibrary,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		mapLibrary.setName(this.name);
		mapLibrary.setCodename(this.codename);
		if(this.description != null && this.description.length() != 0) {
			mapLibrary.setDescription(this.description);
		}

		if (mapLibrary.isSetPhysicalLinkTypes()) {
			mapLibrary.unsetPhysicalLinkTypes();
		}
		final Set<PhysicalLinkType> physicalLinkTypes = this.getPhysicalLinkTypes();
		if (!physicalLinkTypes.isEmpty()) {
			final XmlPhysicalLinkTypeSeq physicalLinkTypeSeq = mapLibrary.addNewPhysicalLinkTypes();
			for (final PhysicalLinkType physicalLinkType : physicalLinkTypes) {
				physicalLinkType.getXmlTransferable(physicalLinkTypeSeq.addNewPhysicalLinkType(), importType, usePool);
			}
		}
		if (mapLibrary.isSetSiteNodeTypes()) {
			mapLibrary.unsetSiteNodeTypes();
		}
		final Set<SiteNodeType> siteNodeTypes = this.getSiteNodeTypes();
		if (!siteNodeTypes.isEmpty()) {
			final XmlSiteNodeTypeSeq siteNodeTypeSeq = mapLibrary.addNewSiteNodeTypes();
			for (final SiteNodeType siteNodeType : siteNodeTypes) {
				siteNodeType.getXmlTransferable(siteNodeTypeSeq.addNewSiteNodeType(), importType, usePool);
			}
		}
		mapLibrary.setImportType(importType);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private MapLibrary(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, MAPLIBRARY_CODE, created, creatorId);
		/**
		 * @todo should be moved to fromXmlTransferable(...) 
		 */
		this.parentMapLibraryId = VOID_IDENTIFIER;
	}

	public void fromXmlTransferable(final XmlMapLibrary xmlMapLibrary, final String importType) throws ApplicationException {
		this.name = xmlMapLibrary.getName();
		this.codename = xmlMapLibrary.getCodename();
		if(xmlMapLibrary.isSetDescription()) {
			this.description = xmlMapLibrary.getDescription();
		}
		else {
			this.description = "";
		}

		if(xmlMapLibrary.isSetPhysicalLinkTypes()) {
			for (final XmlPhysicalLinkType xmlPhysicalLinkType : xmlMapLibrary.getPhysicalLinkTypes().getPhysicalLinkTypeArray()) {
				this.addChild(PhysicalLinkType.createInstance(this.creatorId, importType, xmlPhysicalLinkType));
			}
		}

		if(xmlMapLibrary.isSetSiteNodeTypes()) {
			for (final XmlSiteNodeType xmlSiteNodeType : xmlMapLibrary.getSiteNodeTypes().getSiteNodeTypeArray()) {
				this.addChild(SiteNodeType.createInstance(this.creatorId, importType, xmlSiteNodeType));
			}
		}
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlMapLibrary
	 * @throws CreateObjectException
	 */
	public static MapLibrary createInstance(
			final Identifier creatorId, 
			final String importType,
			final XmlMapLibrary xmlMapLibrary)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlMapLibrary.getCodename();
			final Set<MapLibrary> mapLibraries = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, MAPLIBRARY_CODE, COLUMN_CODENAME),
					true);

			assert mapLibraries.size() <= 1;

			final XmlIdentifier xmlId = xmlMapLibrary.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			MapLibrary mapLibrary;
			if (mapLibraries.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					mapLibrary = new MapLibrary(xmlId,
							importType,
							created,
							creatorId);
				} else {
					mapLibrary = StorableObjectPool.getStorableObject(expectedId, true);
					if (mapLibrary == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						mapLibrary = new MapLibrary(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = mapLibrary.getCodename();
						if (!oldCodename.equals(newCodename)) {
							Log.debugMessage("WARNING: "
									+ expectedId + " will change its codename from ``"
									+ oldCodename + "'' to ``"
									+ newCodename + "''",
									WARNING);
						}
					}
				}
			} else {
				mapLibrary = mapLibraries.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					mapLibrary.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = mapLibrary.getId();
					if (!actualId.equals(expectedId)) {
						/*
						 * Arghhh, no match.
						 */
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + actualId
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						mapLibrary.insertXmlMapping(xmlId, importType);
					}
				}
			}
			mapLibrary.fromXmlTransferable(xmlMapLibrary, importType);
			assert mapLibrary.isValid() : OBJECT_BADLY_INITIALIZED;
			mapLibrary.markAsChanged();
			return mapLibrary;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	Identifier getParentMapLibraryId() {
		return this.parentMapLibraryId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MapLibraryWrapper getWrapper() {
		return MapLibraryWrapper.getInstance();
	}
}
