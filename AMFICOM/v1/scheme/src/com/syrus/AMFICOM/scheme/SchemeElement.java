/*-
 * $Id: SchemeElement.java,v 1.7 2005/03/24 16:58:52 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.*;
import java.util.*;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/03/24 16:58:52 $
 * @module scheme_v1
 */
public final class SchemeElement extends AbstractSchemeElement implements
		SchemeCellContainer {
	private static final long serialVersionUID = 3618977875802797368L;

	protected Identifier equipmentId = null;

	protected Identifier equipmentTypeId = null;

	protected Identifier rtuId = null;

	/**
	 * Takes non-null value at pack time.
	 */
	protected Identifier schemeCellId = null;

	protected Identifier siteId = null;

	protected Identifier symbolId = null;

	protected String thisLabel = null;

	/**
	 * Takes non-null value at pack time.
	 */
	protected Identifier ugoCellId = null;

	/**
	 * @param id
	 */
	protected SchemeElement(Identifier id) {
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
	protected SchemeElement(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeElement createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemeElement createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeElement.changed = true;
			return schemeElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeElement createInstance(final SchemeProtoElement schemeProtoElement) {
		throw new UnsupportedOperationException();
	}

	public void addScheme(final Scheme innerScheme) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeElement(final SchemeElement schemeElement) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	public SchemePath alarmedPath() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPath(SchemePath alarmedPath) {
		throw new UnsupportedOperationException();
	}

	public PathElement alarmedPathElement() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPathElement(PathElement alarmedPathElement) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeElement schemeElement = (SchemeElement) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeElement;
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT;
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public Equipment getEquipment() {
		throw new UnsupportedOperationException();
	}

	public EquipmentType getEquipmentType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return <em>the first</em> <code>Scheme</code> inner to this
	 *         <code>SchemeElement</code>, or <code>null</code> if
	 *         none.
	 */
	public Scheme getScheme() {
		final Iterator schemeIterator = getSchemes().iterator();
		if (schemeIterator.hasNext())
			return (Scheme) schemeIterator.next();
		return null;
	}

	public Collection getSchemes() {
		throw new UnsupportedOperationException();
	}

	public KIS getKis() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		throw new UnsupportedOperationException();
	}

	public Collection getSchemeDevices() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeDevice[] getSchemeDevicesAsArray() {
		throw new UnsupportedOperationException();
	}

	public Collection getSchemeElements() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeElement[] getSchemeElementsAsArray() {
		throw new UnsupportedOperationException();
	}

	public Collection getSchemeLinks() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeLink[] getSchemeLinksAsArray() {
		throw new UnsupportedOperationException();
	}

	public SiteNode getSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeElement#label()
	 */
	public String label() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLabel
	 * @see SchemeElement#label(String)
	 */
	public void label(final String newLabel) {
		throw new UnsupportedOperationException();
	}

	public void removeScheme(final Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeDevice(final SchemeDevice schemeDevice) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeElement(final SchemeElement schemeElement) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentImpl
	 */
	public void setEquipment(Equipment newEquipmentImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentTypeImpl
	 */
	public void setEquipmentType(EquipmentType newEquipmentTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public void setScheme(final Scheme scheme) {
		setSchemes(scheme == null 
				? Collections.EMPTY_LIST
				: Collections.singletonList(scheme));
	}

	public void setSchemes(final Collection schemes) {
		/**
		 * @todo Check for circualr deps.
		 */
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuImpl
	 */
	public void setKis(KIS newRtuImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeDevices(final Collection schemeDevices) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeElements(final Collection schemeElements) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeLinks(final Collection schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public void setSiteNode(final SiteNode siteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
	}
}
