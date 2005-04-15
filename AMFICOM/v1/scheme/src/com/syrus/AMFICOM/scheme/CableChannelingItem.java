/*-
 * $Id: CableChannelingItem.java,v 1.11 2005/04/15 19:22:55 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/04/15 19:22:55 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject
		implements Describable {
	private static final long serialVersionUID = 3256437027796038705L;

	private String description;

	private Identifier endSiteNodeId;

	private double endSpare;

	private String name;

	private Identifier parentSchemeCableLinkId;

	private Identifier physicalLinkId;

	private int placeY;

	private int rowX;

	private int sequentialNumber;

	private Identifier startSiteNodeId;

	private double startSpare;

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
	 */
	CableChannelingItem(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	CableChannelingItem(final CableChannelingItem_Transferable transferable) throws CreateObjectException {
		this.cableChannelingItemDatabase = SchemeDatabaseContext.getCableChannelingItemDatabase();
		fromTransferable(transferable);
	}

	public static CableChannelingItem createInstance(final Identifier creatorId) throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE),
					created,
					created,
					creatorId,
					creatorId,
					0L);
			cableChannelingItem.changed = true;
			return cableChannelingItem;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
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
	 * @see StorableObject#getDependencies()
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
	 * @see Namable#getName()
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
	 * @see TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
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
	 * @see Namable#setName(String)
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
	 * @see StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
