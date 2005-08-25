/*-
 * $Id: SiteNodeType.java,v 1.71 2005/08/25 16:40:19 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static java.util.logging.Level.SEVERE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUIDMapDatabase;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypeHelper;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.util.Log;

/**
 * Тип сетевого узла топологической схемы. Существует несколько
 * предустановленных  типов сетевых узлов, которые определяются полем
 * {@link #codename}, соответствующим какому-либо значению {@link #DEFAULT_WELL},
 * {@link #DEFAULT_PIQUET}, {@link #DEFAULT_ATS}, {@link #DEFAULT_BUILDING}, {@link #DEFAULT_UNBOUND},
 * {@link #DEFAULT_CABLE_INLET}, {@link #DEFAULT_TOWER}
 * @author $Author: krupenn $
 * @version $Revision: 1.71 $, $Date: 2005/08/25 16:40:19 $
 * @module map
 */
public final class SiteNodeType extends StorableObjectType 
		implements Characterizable, Namable, LibraryEntry, XMLBeansTransferable {

	public static final String DEFAULT_WELL = "defaultwell";
	public static final String DEFAULT_PIQUET = "defaultpiquet";
	public static final String DEFAULT_ATS = "defaultats";
	public static final String DEFAULT_BUILDING = "defaultbuilding";
	public static final String DEFAULT_UNBOUND = "unbound";
	public static final String DEFAULT_CABLE_INLET = "defaultcableinlet";
	public static final String DEFAULT_TOWER = "defaulttower";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690481316080464696L;

	private Identifier imageId;
	private String name;
	private boolean topological;

	private SiteNodeTypeSort sort;

	private Identifier mapLibraryId;

	private transient CharacterizableDelegate characterizableDelegate;

	SiteNodeType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.SITENODE_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNodeType(final IdlSiteNodeType sntt) throws CreateObjectException {
		try {
			this.fromTransferable(sntt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	SiteNodeType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
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
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;
		this.sort = sort;
		this.mapLibraryId = mapLibraryId;
	}

	public static SiteNodeType createInstance(final Identifier creatorId,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological,
			final Identifier mapLibraryId) throws CreateObjectException {

		assert creatorId != null 
				&& codename != null 
				&& name != null 
				&& description != null 
				&& imageId != null 
				&& sort != null
				&& mapLibraryId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !mapLibraryId.isVoid() : ErrorMessages.NON_VOID_EXPECTED;

		try {
			final SiteNodeType siteNodeType = new SiteNodeType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					sort,
					codename,
					name,
					description,
					imageId,
					topological,
					mapLibraryId);

			assert siteNodeType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			siteNodeType.markAsChanged();

			return siteNodeType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlSiteNodeType sntt = (IdlSiteNodeType) transferable;
		super.fromTransferable(sntt, sntt.codename, sntt.description);

		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;
		this.sort = sntt.sort;
		this.mapLibraryId = new Identifier(sntt.mapLibraryId);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.imageId);
		dependencies.add(this.mapLibraryId);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return dependencies;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSiteNodeType getTransferable(final ORB orb) {
		return IdlSiteNodeTypeHelper.init(orb,
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
				this.imageId.getTransferable(),
				this.topological,
				this.mapLibraryId.getTransferable());
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}
	
	protected void setName0(final String name) {
		this.name = name;
	}

	public void setTopological(final boolean topological) {
		this.setTopological0(topological);
		super.markAsChanged();
	}
	
	protected void setTopological0(final boolean topological) {
		this.topological = topological;
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological,
			final Identifier mapLibraryId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;
		this.sort = sort;
		this.mapLibraryId = mapLibraryId;
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	public SiteNodeTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final SiteNodeTypeSort sort) {
		this.setSort0(sort);
		super.markAsChanged();
	}
	
	protected void setSort0(final SiteNodeTypeSort sort) {
		this.sort = sort;
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = com.syrus.amficom.map.xml.SiteNodeType.Factory.newInstance();
		this.fillXMLTransferable(xmlSiteNodeType);
		return xmlSiteNodeType;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = (com.syrus.amficom.map.xml.SiteNodeType) xmlObject;

		final com.syrus.amficom.general.xml.UID uid = xmlSiteNodeType.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlSiteNodeType.setName(this.name);
		xmlSiteNodeType.setDescription(this.description);
		xmlSiteNodeType.setSort(com.syrus.amficom.map.xml.SiteNodeTypeSort.Enum.forInt(this.sort.value()));
		xmlSiteNodeType.setTopological(this.isTopological());

		String imageCodeName = "";
		try {
			final AbstractImageResource ir = StorableObjectPool.getStorableObject(this.getImageId(), false);
			if (ir instanceof FileImageResource) {
				imageCodeName = ((FileImageResource) ir).getCodename();
			}
			else if (ir instanceof BitmapImageResource) {
				imageCodeName = ((BitmapImageResource) ir).getCodename();
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		xmlSiteNodeType.setImage(imageCodeName);
	}

	SiteNodeType(final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.SITENODE_TYPE_CODE, xmlSiteNodeType.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.fromXMLTransferable(xmlSiteNodeType, clonedIdsPool, importType);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = (com.syrus.amficom.map.xml.SiteNodeType )xmlObject;

		this.name = xmlSiteNodeType.getName();
		this.description = xmlSiteNodeType.getDescription();
		this.sort = SiteNodeTypeSort.from_int(xmlSiteNodeType.getSort().intValue());
		this.topological = xmlSiteNodeType.getTopological();

		final String imageCodeName = xmlSiteNodeType.getImage();
		Identifier loadedImageId = getImageId(this.modifierId, imageCodeName);

		if (loadedImageId == null) {
			throw new CreateObjectException("ImageResource \'" + imageCodeName + "\' not found");
		}
		
		this.imageId = loadedImageId;
	}

	public static SiteNodeType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = (com.syrus.amficom.map.xml.SiteNodeType) xmlObject;

		try {
			String uid = xmlSiteNodeType.getUid().getStringValue();
			Identifier existingIdentifier = ImportUIDMapDatabase.retrieve(importType, uid);
			SiteNodeType siteNodeType = null;
			if(existingIdentifier != null) {
				siteNodeType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(siteNodeType != null) {
					siteNodeType.fromXMLTransferable(xmlObject, clonedIdsPool, importType);
				}
				else{
					ImportUIDMapDatabase.delete(importType, uid);
				}
			}
			if(siteNodeType == null) {
				siteNodeType = new SiteNodeType(creatorId,
						StorableObjectVersion.createInitial(),
						xmlSiteNodeType.getCodename(),
						xmlSiteNodeType.getDescription(),
						xmlSiteNodeType,
						clonedIdsPool, 
						importType);
				ImportUIDMapDatabase.insert(importType, uid, siteNodeType.id);
			}
			assert siteNodeType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			siteNodeType.markAsChanged();
			return siteNodeType;
		} catch (Exception e) {
			throw new CreateObjectException("SiteNodeType.createInstance |  ", e);
		}
	}
	
	public Library getParent() {
		return getMapLibrary();
	}

	public void setParent(Library library) {
		this.setParent0(library);
		super.markAsChanged();
	}
	
	protected void setParent0(Library library) {
		assert library instanceof MapLibrary : "must be instance of MapLibrary";
		this.setMapLibrary0((MapLibrary)library);
	}

	public MapLibrary getMapLibrary() {
		try {
			return StorableObjectPool.getStorableObject(this.getMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}
	
	Identifier getMapLibraryId() {
		return this.mapLibraryId;
	}

	public void setMapLibrary(MapLibrary mapLibrary) {
		this.setMapLibrary0(mapLibrary);
		this.markAsChanged();
	}
	
	protected void setMapLibrary0(MapLibrary mapLibrary) {
		this.mapLibraryId = mapLibrary.getId();
	}
	
	/**
	 * Получить пиктограмму по кодовому имени. Если пиктограмма не существует, 
	 * на создается.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return Идентификатор пиктограммы ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 * @throws ApplicationException 
	 */
	static Identifier getImageId(
			final Identifier userId, 
			final String codename)
			throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGERESOURCE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<AbstractBitmapImageResource> bitMaps = 
			StorableObjectPool.getStorableObjectsByCondition(condition, true);

		if(!bitMaps.isEmpty()) {
			return bitMaps.iterator().next().getId();
		}

		// interprete codename as a file name
		// and create new bitmap image resource
		try {
			File file = new File(codename);
			FileInputStream in = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			in.read(data);
			in.close();

			BitmapImageResource bitmapImageResource = BitmapImageResource.createInstance(
					userId,
					codename,
					data);
			StorableObjectPool.flush(bitmapImageResource, userId, true);
			return bitmapImageResource.getId();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
