/*-
 * $Id: CableChannelingItem.java,v 1.13 2005/04/21 15:02:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem_Transferable;

/**
 * #13 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/04/21 15:02:48 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject
		implements Describable {
	private static final long serialVersionUID = 3256437027796038705L;

	private String name;

	private String description;

	private double startSpare;

	private double endSpare;

	private int rowX;

	private int placeY;

	private int sequentialNumber;

	private Identifier physicalLinkId;

	private Identifier startSiteNodeId;

	private Identifier endSiteNodeId;

	private Identifier parentSchemeCableLinkId;

	private CableChannelingItemDatabase cableChannelingItemDatabase; 

	/**
	 * @param id
	 * @throws RetrieveObjectException 
	 * @throws ObjectNotFoundException 
	 */
	CableChannelingItem(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.cableChannelingItemDatabase = SchemeDatabaseContext.getCableChannelingItemDatabase();
		try {
			this.cableChannelingItemDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLink
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 */
	CableChannelingItem(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double startSpare, final double endSpare,
			final int rowX, final int placeY,
			final int sequentialNumber,
			final PhysicalLink physicalLink,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink) {
		super(id, created, modified, creatorId, modifierId, version);

		this.name = name;
		this.description = description;
		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = Identifier.possiblyVoid(physicalLink);
		this.startSiteNodeId = Identifier.possiblyVoid(startSiteNode);
		this.endSiteNodeId = Identifier.possiblyVoid(endSiteNode);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	CableChannelingItem(final CableChannelingItem_Transferable transferable) throws CreateObjectException {
		this.cableChannelingItemDatabase = SchemeDatabaseContext.getCableChannelingItemDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, int, int, int, PhysicalLink, SiteNode, SiteNode, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(
			final Identifier creatorId, final String name,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, 0, 0, 0, null, //$NON-NLS-1$
				startSiteNode, endSiteNode,
				parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLink
	 * @param startSiteNode
	 * @param endSiteNode
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static CableChannelingItem createInstance(
			final Identifier creatorId, final String name,
			final String description, final double startSpare,
			final double endSpare, final int rowX,
			final int placeY, final int sequentialNumber,
			final PhysicalLink physicalLink,
			final SiteNode startSiteNode,
			final SiteNode endSiteNode,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert startSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		assert endSiteNode != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, startSpare,
					endSpare, rowX, placeY,
					sequentialNumber, physicalLink,
					startSiteNode, endSiteNode,
					parentSchemeCableLink);
			cableChannelingItem.changed = true;
			return cableChannelingItem;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"CableChanelingItem.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
		}
	}

	public Object clone() {
		final CableChannelingItem cableChannelingItem = (CableChannelingItem) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return cableChannelingItem;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public SiteNode getEndSiteNode() {
		throw new UnsupportedOperationException();
	}

	public double getEndSpare() {
		return this.endSpare;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeCableLink getParentSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	public PhysicalLink getPhysicalLink() {
		throw new UnsupportedOperationException();
	}

	public int getPlaceY() {
		return this.placeY;
	}

	public int getRowX() {
		return this.rowX;
	}

	public int getSequentialNumber() {
		return this.sequentialNumber;
	}

	public SiteNode getStartSiteNode() {
		throw new UnsupportedOperationException();
	}

	public double getStartSpare() {
		return this.startSpare;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param startSpare
	 * @param endSpare
	 * @param rowX
	 * @param placeY
	 * @param sequentialNumber
	 * @param physicalLinkId
	 * @param startSiteNodeId
	 * @param endSiteNodeId
	 * @param parentSchemeCableLinkId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double startSpare, final double endSpare,
			final int rowX, final int placeY,
			final int sequentialNumber,
			final Identifier physicalLinkId,
			final Identifier startSiteNodeId,
			final Identifier endSiteNodeId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert physicalLinkId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert startSiteNodeId != null && !startSiteNodeId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert endSiteNodeId != null && !endSiteNodeId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert parentSchemeCableLinkId != null && !parentSchemeCableLinkId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = physicalLinkId;
		this.startSiteNodeId = startSiteNodeId;
		this.endSiteNodeId = endSiteNodeId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @param endSiteNode
	 */
	public void setEndSiteNode(final SiteNode endSiteNode) {
		throw new UnsupportedOperationException();
	}

	public void setEndSpare(final double endSpare) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		this.changed = true;
	}

	public void setParentSchemeCableLink(final SchemeCableLink parentSchemeCableLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param physicalLink
	 */
	public void setPhysicalLink(final PhysicalLink physicalLink) {
		throw new UnsupportedOperationException();
	}

	public void setPlaceY(final int placeY) {
		throw new UnsupportedOperationException();
	}

	public void setRowX(final int rowX) {
		throw new UnsupportedOperationException();
	}

	public void setSequentialNumber(final int sequentialNumber) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		throw new UnsupportedOperationException();
	}

	public void setStartSpare(final double startSpare) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
