/*
 * $Id: CableChannelingItem.java,v 1.3 2005/03/17 18:17:27 bass Exp $ Copyright ¿
 * 2004 Syrus Systems. Dept. of Science & Technology. Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.map.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/17 18:17:27 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject
		implements Describable {
	private static final long serialVersionUID = 3256437027796038705L;

	protected Identifier endSiteNodeId = null;

	protected Identifier physicalLinkId = null;

	protected Identifier startSiteNodeId = null;

	private String description = null;

	protected double thisEndSpare = 0;

	private String name = null;

	protected int thisPlaceY = 0;

	protected int thisRowX = 0;

	protected int thisSequentialNumber = 0;

	protected double thisStartSpare = 0;

	/**
	 * @param id
	 */
	protected CableChannelingItem(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected CableChannelingItem(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public SiteNode getEndSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param endSiteNode
	 */
	public void setEndSiteNode(final SiteNode endSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#endSpare()
	 */
	public double endSpare() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#endSpare(double)
	 */
	public void endSpare(double endSpare) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public PhysicalLink getPhysicalLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param physicalLink
	 */
	public void setPhysicalLink(final PhysicalLink physicalLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#placeY()
	 */
	public int placeY() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#placeY(int)
	 */
	public void placeY(int placeY) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#rowX()
	 */
	public int rowX() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#rowX(int)
	 */
	public void rowX(int rowX) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#sequentialNumber()
	 */
	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#sequentialNumber(int)
	 */
	public void sequentialNumber(int sequentialNumber) {
		throw new UnsupportedOperationException();
	}

	public SiteNode getStartSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#startSpare()
	 */
	public double startSpare() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#startSpare(double)
	 */
	public void startSpare(double startSpare) {
		throw new UnsupportedOperationException();
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
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static CableChannelingItem createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			cableChannelingItem.changed = true;
			return cableChannelingItem;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"CableChanelingItem.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static CableChannelingItem createInstance() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}
}
