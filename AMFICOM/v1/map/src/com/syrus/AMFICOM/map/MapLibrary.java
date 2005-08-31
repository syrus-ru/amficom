/*-
 * $Id: MapLibrary.java,v 1.20 2005/08/31 13:25:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.UNSUPPORTED_CHILD_TYPE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlObject;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.corba.IdlMapLibrary;
import com.syrus.AMFICOM.map.corba.IdlMapLibraryHelper;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkType;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSeq;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.20 $, $Date: 2005/08/31 13:25:08 $
 * @author $Author: bass $
 * @module map
 */
public class MapLibrary extends StorableObject implements Identifiable, Namable, Library, XmlBeansTransferable<XmlMapLibrary> {
	private static final long	serialVersionUID	= -8616969914711251336L;

	private String name;
	private String codename;
	private String description;
	private Identifier parentMapLibraryId;

	MapLibrary(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(MAPLIBRARY_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

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
					StorableObjectVersion.createInitial(),
					name,
					codename,
					description,
					parentMapLibrary);

			assert mapLibrary.isValid() : OBJECT_STATE_ILLEGAL;

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
	public IdlMapLibrary getTransferable(final ORB orb) {
		return IdlMapLibraryHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.codename,
				this.description,
				this.parentMapLibraryId.getTransferable());
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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
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
			Log.debugException(e, Level.SEVERE);
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
			Log.debugException(e, Level.SEVERE);
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

	public void setParent(final Item parent) {
		this.setParent0(parent);
		super.markAsChanged();
	}
	
	protected void setParent0(final Item parent) {
		assert parent instanceof MapLibrary : "must be instance of MapLibrary";
		assert parent != this : CIRCULAR_DEPS_PROHIBITED;
		this.parentMapLibraryId = Identifier.possiblyVoid((MapLibrary) parent);
	}

	public MapLibrary getParent() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public XmlMapLibrary getXmlTransferable() {
		final XmlMapLibrary xmlMapLibrary = XmlMapLibrary.Factory.newInstance();
		xmlMapLibrary.setCodename(this.codename);
		xmlMapLibrary.setName(this.name);
		xmlMapLibrary.setDescription(this.description);
		
		final XmlPhysicalLinkTypeSeq xmlPhysicalLinkTypes = xmlMapLibrary.addNewPhysicalLinkTypes();
		final Collection<XmlObject> xmlPhysicalLinkTypesArray = new LinkedList<XmlObject>();
		for (final PhysicalLinkType physicalLinkType : this.getPhysicalLinkTypes()) {
			xmlPhysicalLinkTypesArray.add(physicalLinkType.getXmlTransferable());
		}
		xmlPhysicalLinkTypes.setPhysicalLinkTypeArray(xmlPhysicalLinkTypesArray.toArray(new XmlPhysicalLinkType[xmlPhysicalLinkTypesArray.size()]));
		
		final XmlSiteNodeTypeSeq xmlSitenodetypes = xmlMapLibrary.addNewSiteNodeTypes();
		final Collection<XmlObject> xmlSiteNodeTypesArray = new LinkedList<XmlObject>();
		for (final SiteNodeType siteNodeType : this.getSiteNodeTypes()) {
			xmlSiteNodeTypesArray.add(siteNodeType.getXmlTransferable());
		}
		xmlSitenodetypes.setSiteNodeTypeArray(xmlSiteNodeTypesArray.toArray(new XmlSiteNodeType[xmlSiteNodeTypesArray.size()]));

		xmlMapLibrary.setImportType("amficom");

		return xmlMapLibrary;
	}

	MapLibrary(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlMapLibrary xmlMapLibrary,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(IdentifierPool.getGeneratedIdentifier(MAPLIBRARY_CODE),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.fromXmlTransferable(xmlMapLibrary, clonedIdsPool, importType);
		this.parentMapLibraryId = Identifier.VOID_IDENTIFIER;

	}

	public void fromXmlTransferable(final XmlMapLibrary xmlMapLibrary, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.name = xmlMapLibrary.getName();
		this.description = xmlMapLibrary.getDescription();
		this.codename = xmlMapLibrary.getCodename();

		for (final XmlPhysicalLinkType xmlPhysicalLinkType : xmlMapLibrary.getPhysicalLinkTypes().getPhysicalLinkTypeArray()) {
			this.addChild(PhysicalLinkType.createInstance(this.creatorId, importType, xmlPhysicalLinkType, clonedIdsPool));
		}

		for (final XmlSiteNodeType xmlSiteNodeType : xmlMapLibrary.getSiteNodeTypes().getSiteNodeTypeArray()) {
			this.addChild(SiteNodeType.createInstance(this.creatorId, importType, xmlSiteNodeType, clonedIdsPool));
		}
	}

	public static MapLibrary createInstance(
			final Identifier creatorId, 
			final String importType,
			final XmlMapLibrary xmlMapLibrary, 
			final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		try {
			final XmlIdentifier xmlId = xmlMapLibrary.getId();
			Identifier existingIdentifier = Identifier.fromXmlTransferable(xmlId, importType);
			MapLibrary mapLibrary = null;
			if(existingIdentifier != null) {
				mapLibrary = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(mapLibrary != null) {
					clonedIdsPool.setExistingId(xmlId, existingIdentifier);
					mapLibrary.fromXmlTransferable(xmlMapLibrary, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, xmlId);
				}
			}
			if(mapLibrary == null) {
				mapLibrary = new MapLibrary(creatorId, StorableObjectVersion.createInitial(), xmlMapLibrary, clonedIdsPool, importType);
				ImportUidMapDatabase.insert(importType, xmlId, mapLibrary.id);
			}
			assert mapLibrary.isValid() : OBJECT_STATE_ILLEGAL;
			mapLibrary.markAsChanged();
			return mapLibrary;
		} catch (Exception e) {
			throw new CreateObjectException("MapLibrary.createInstance |  ", e);
		}
	}

	Identifier getParentMapLibraryId() {
		return this.parentMapLibraryId;
	}	
}
