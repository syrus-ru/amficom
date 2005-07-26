/*-
 * $Id: SiteNodeType.java,v 1.51 2005/07/26 12:07:03 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypeHelper;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * Тип сетевого узла топологической схемы. Существует несколько
 * предустановленных  типов сетевых узлов, которые определяются полем
 * {@link #codename}, соответствующим какому-либо значению {@link #DEFAULT_WELL},
 * {@link #DEFAULT_PIQUET}, {@link #DEFAULT_ATS}, {@link #DEFAULT_BUILDING}, {@link #DEFAULT_UNBOUND},
 * {@link #DEFAULT_CABLE_INLET}, {@link #DEFAULT_TOWER}
 * @author $Author: arseniy $
 * @version $Revision: 1.51 $, $Date: 2005/07/26 12:07:03 $
 * @module map_v1
 * @todo make 'sort' persistent (update database scheme as well)
 */
public final class SiteNodeType extends StorableObjectType implements Characterizable, Namable, XMLBeansTransferable {

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
			final SiteNodeType siteNodeType = new SiteNodeType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlSiteNodeType sntt = (IdlSiteNodeType) transferable;
		super.fromTransferable(sntt, sntt.codename, sntt.description);

		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;

		//@todo retreive from transferable!
		this.sort = SiteNodeTypeSort.fromString(sntt.codename);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.singleton((Identifiable) this.imageId);
	}

	@Override
	public String getDescription() {
		return this.description;
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
				this.codename,
				this.name,
				this.description,
				this.imageId.getTransferable(),
				this.topological);
	}

	public boolean isTopological() {
		return this.topological;
	}

	@Override
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
			final StorableObjectVersion version,
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

	@Override
	public void setCodename(final String codename) {
		super.setCodename(codename);
		//@todo retreive from transferable!
		this.sort = SiteNodeTypeSort.fromString(codename);
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	public SiteNodeTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final SiteNodeTypeSort sort) {
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
		xmlSiteNodeType.setSort(com.syrus.amficom.map.xml.SiteNodeTypeSort.Enum.forString(this.sort.value()));
		xmlSiteNodeType.setTopological(this.isTopological());

		String imageCodeName = "";
		try {
			final AbstractImageResource ir = (AbstractImageResource) StorableObjectPool.getStorableObject(this.getImageId(), false);
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
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.SITENODE_CODE, xmlSiteNodeType.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.fromXMLTransferable(xmlSiteNodeType, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = (com.syrus.amficom.map.xml.SiteNodeType )xmlObject;

		this.name = xmlSiteNodeType.getName();
		this.description = xmlSiteNodeType.getDescription();
		this.sort = SiteNodeTypeSort.fromString(xmlSiteNodeType.getSort().toString());
		this.topological = xmlSiteNodeType.getTopological();

		final String imageCodeName = xmlSiteNodeType.getImage();
		Identifier loadedImageId = null;
		final StorableObjectCondition condition = new TypicalCondition(
				String.valueOf(ImageResourceSort._FILE),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGERESOURCE_CODE,
				ImageResourceWrapper.COLUMN_SORT);
		final Set bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

		for(final Iterator it = bitMaps.iterator(); it.hasNext();) {
			final FileImageResource ir = (FileImageResource )it.next();
			if(ir.getCodename().equals(imageCodeName)) {
				loadedImageId = ir.getId();
				break;
			}
		}

		if (loadedImageId == null) {
			throw new CreateObjectException("ImageResource \'" + loadedImageId + "\' not found");
		}
		
		this.imageId = loadedImageId;
	}

	public static SiteNodeType createInstance(final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		final com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = (com.syrus.amficom.map.xml.SiteNodeType) xmlObject;

		try {
			final SiteNodeType siteNode = new SiteNodeType(creatorId,
					StorableObjectVersion.createInitial(),
					xmlSiteNodeType.getSort().toString(),
					xmlSiteNodeType.getDescription(),
					xmlSiteNodeType,
					clonedIdsPool);
			assert siteNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			siteNode.markAsChanged();
			return siteNode;
		} catch (Exception e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}

	
}
