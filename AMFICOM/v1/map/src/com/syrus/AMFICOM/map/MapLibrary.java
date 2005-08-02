/**
 * $Id: MapLibrary.java,v 1.4 2005/08/02 18:02:50 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.map;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.corba.IdlMapLibrary;
import com.syrus.AMFICOM.map.corba.IdlMapLibraryHelper;


/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 18:02:50 $
 * @author $Author: arseniy $
 * @module map
 */
public class MapLibrary extends StorableObject implements Identifiable, Namable, Library, XMLBeansTransferable {
	private static final long	serialVersionUID	= -8616969914711251336L;

	private String name;
	private String codename;
	private String description;
	private MapLibrary parentMapLibrary;

	private transient Set<PhysicalLinkType> physicalLinkTypes = new HashSet<PhysicalLinkType>();
	private transient Set<SiteNodeType> siteNodeTypes = new HashSet<SiteNodeType>();
	private transient Set<MapLibrary> libraries = new HashSet<MapLibrary>();

	MapLibrary(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.MAPLIBRARY_CODE).retrieve(this);
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
			MapLibrary parent) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.parentMapLibrary = parent;
	}

	public static MapLibrary createInstance(final Identifier creatorId, final MapLibrary parent) throws CreateObjectException {
		return MapLibrary.createInstance(creatorId, "", "", "", parent);
	}

	public static MapLibrary createInstance(final Identifier creatorId,
			final String name,
			final String codename,
			final String description,
			final MapLibrary parent) throws CreateObjectException {
		if (creatorId == null || name == null || codename == null || description == null || parent == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final MapLibrary mapLibrary = new MapLibrary(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAPLIBRARY_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					codename,
					description,
					parent);

			assert mapLibrary.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			mapLibrary.markAsChanged();

			return mapLibrary;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMapLibrary mlt = (IdlMapLibrary) transferable;
		super.fromTransferable(mlt);

		this.name = mlt.name;
		this.codename = mlt.codename;
		this.description = mlt.description;

		this.parentMapLibrary = StorableObjectPool.getStorableObject(new Identifier(mlt.parentMapLibraryId), true);
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
				this.parentMapLibrary.getId().getTransferable());
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String codename,
			final String description,
			final MapLibrary parent) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.parentMapLibrary = parent;
	}

	public void addChild(final LibraryEntry libraryEntry) {
		assert libraryEntry != null : ErrorMessages.NON_NULL_EXPECTED;
		if (libraryEntry instanceof PhysicalLinkType) {
			this.physicalLinkTypes.add((PhysicalLinkType) libraryEntry);
		} else if (libraryEntry instanceof SiteNodeType) {
			this.siteNodeTypes.add((SiteNodeType) libraryEntry);
		} else if (libraryEntry instanceof MapLibrary) {
			this.libraries.add((MapLibrary) libraryEntry);
		} else {
			throw new UnsupportedOperationException(ErrorMessages.UNSUPPORTED_CHILD_TYPE);
		}
	}

	public void removeChild(final LibraryEntry libraryEntry) {
		if (libraryEntry instanceof PhysicalLinkType) {
			this.physicalLinkTypes.remove(libraryEntry);
		} else if (libraryEntry instanceof SiteNodeType) {
			this.siteNodeTypes.remove(libraryEntry);
		} else if (libraryEntry instanceof MapLibrary) {
			this.libraries.remove(libraryEntry);
		}
	}

	public void setParent(final Library library) {
		assert library != this : ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		this.parentMapLibrary = (MapLibrary) library;
	}

	public String getName() {
		return this.name;
	}

	public Set<LibraryEntry> getChildren() {
		final Set<LibraryEntry> children = new HashSet<LibraryEntry>(this.libraries.size()
				+ this.physicalLinkTypes.size()
				+ this.siteNodeTypes.size());
		children.addAll(this.libraries);
		children.addAll(this.physicalLinkTypes);
		children.addAll(this.siteNodeTypes);
		return children;
	}

	public void setParent(final Item parent) {
		assert parent != this : ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		this.parentMapLibrary = (MapLibrary) parent;
	}

	public MapLibrary getParent() {
		return this.parentMapLibrary;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = com.syrus.amficom.map.xml.MapLibrary.Factory.newInstance();
		fillXMLTransferable(xmlMapLibrary);
		return xmlMapLibrary;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary) xmlObject;

		final com.syrus.amficom.map.xml.PhysicalLinkTypes xmlPhysicallinktypes = xmlMapLibrary.addNewPhysicallinktypes();
		final Collection<XmlObject> xmlPhysicalLinkTypesArray = new LinkedList<XmlObject>();
		for (final PhysicalLinkType physicalLinkType : this.physicalLinkTypes) {
			xmlPhysicalLinkTypesArray.add(physicalLinkType.getXMLTransferable());
		}
		xmlPhysicallinktypes.setPhysicallinktypeArray(xmlPhysicalLinkTypesArray.toArray(new com.syrus.amficom.map.xml.PhysicalLinkType[xmlPhysicalLinkTypesArray.size()]));

		final com.syrus.amficom.map.xml.SiteNodeTypes xmlSitenodetypes = xmlMapLibrary.addNewSitenodetypes();
		final Collection<XmlObject> xmlSiteNodeTypesArray = new LinkedList<XmlObject>();
		for (final SiteNodeType siteNodeType : this.siteNodeTypes) {
			xmlSiteNodeTypesArray.add(siteNodeType.getXMLTransferable());
		}
		xmlSitenodetypes.setSitenodetypeArray(xmlSiteNodeTypesArray.toArray(new com.syrus.amficom.map.xml.SiteNodeType[xmlSiteNodeTypesArray.size()]));
	}

	MapLibrary(final Identifier creatorId,
			final StorableObjectVersion version,
			final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException, ApplicationException {

		/**
		 * @todo: correct uid
		 */
		super(clonedIdsPool.getClonedId(ObjectEntities.PHYSICALLINK_CODE, "?"/*xmlMapLibrary.getUid().getStringValue()*/),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.fromXMLTransferable(xmlMapLibrary, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary) xmlObject;

		final com.syrus.amficom.map.xml.PhysicalLinkType[] xmlPhysicalLinkTypesArray = xmlMapLibrary.getPhysicallinktypes().getPhysicallinktypeArray();
		this.physicalLinkTypes = new HashSet<PhysicalLinkType>(xmlPhysicalLinkTypesArray.length);
		for (int i = 0; i < xmlPhysicalLinkTypesArray.length; i++) {
			final com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = xmlPhysicalLinkTypesArray[i];
			this.addChild(PhysicalLinkType.createInstance(this.creatorId, xmlPhysicalLinkType, clonedIdsPool));
		}

		final com.syrus.amficom.map.xml.SiteNodeType[] xmlSiteNodeTypesArray = xmlMapLibrary.getSitenodetypes().getSitenodetypeArray();
		this.siteNodeTypes = new HashSet<SiteNodeType>(xmlSiteNodeTypesArray.length);
		for (int i = 0; i < xmlSiteNodeTypesArray.length; i++) {
			final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = xmlSiteNodeTypesArray[i];
			this.addChild(SiteNodeType.createInstance(this.creatorId, xmlSiteNodeType, clonedIdsPool));
		}
	}

	public static MapLibrary createInstance(final Identifier creatorId, final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary) xmlObject;

		try {
			final MapLibrary mapLibrary = new MapLibrary(creatorId, StorableObjectVersion.createInitial(), xmlMapLibrary, clonedIdsPool);
			assert mapLibrary.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			mapLibrary.markAsChanged();
			return mapLibrary;
		} catch (Exception e) {
			throw new CreateObjectException("MapLibrary.createInstance |  ", e);
		}
	}
	
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentMapLibrary);
		return dependencies;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(final String codename) {
		this.codename = codename;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}
