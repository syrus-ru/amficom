/*-
 * $Id: Scheme.java,v 1.12 2005/04/14 11:15:52 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;
import com.syrus.AMFICOM.scheme.corba.Scheme_Transferable;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #03 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/14 11:15:52 $
 * @module scheme_v1
 * @todo Possibly join (add|remove)Scheme(Element|Link|CableLink).
 */
public final class Scheme extends AbstractCloneableDomainMember implements Describable, SchemeCellContainer {
	private static final long serialVersionUID = 3257289136389173298L;

	private String name;

	private String description;

	private String label;

	private int width;

	private int height;

	private SchemeKind schemeKind;

	private Identifier mapId;

	private Identifier symbolId;

	private Identifier ugoCellId;

	private Identifier schemeCellId;

	private Identifier currentSchemeMonitoringSolutionId;

	private Identifier parentSchemeElementId;

	private SchemeDatabase schemeDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	Scheme(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeDatabase = SchemeDatabaseContext.getSchemeDatabase();
		try {
			this.schemeDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param description can be null.
	 */
	Scheme(final Identifier id, final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final Identifier domainId, final String name,
			final String description) {
		super(id, created, modified, creatorId, modifierId, version,
				domainId);
		assert name != null;
		this.name = name;
		this.description = description;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	Scheme(final Scheme_Transferable transferable) throws CreateObjectException {
		try {
			this.schemeDatabase = SchemeDatabaseContext.getSchemeDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param description can be null.
	 */
	public static Scheme createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description)
			throws CreateObjectException {
		try {
			final Date created = new Date();
			final Scheme scheme = new Scheme(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, domainId, name, description);
			scheme.changed = true;
			return scheme;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"Scheme.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param schemeCableLink cannot be <code>null</code>.
	 */
	public void addSchemeCableLink(final SchemeCableLink schemeCableLink) {
		assert schemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeCableLink.setParentScheme(this);
	}

	/**
	 * @param schemeElement cannot be <code>null</code>.
	 */
	public void addSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeElement.setParentScheme(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeLink.setParentScheme(this);
	}

	/**
	 * @param schemeOptimizeInfo cannot be <code>null</code>.
	 */
	public void addSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeOptimizeInfo.setParentScheme(this);
	}

	public Object clone() {
		final Scheme scheme = (Scheme) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return scheme;
	}

	public SchemeMonitoringSolution getCurrentSchemeMonitoringSolution() {
		throw new UnsupportedOperationException();
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

	public int getHeight() {
		throw new UnsupportedOperationException();
	}

	public String getLabel() {
		throw new UnsupportedOperationException();
	}

	public Map getMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeCableLinks() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeElements() {
		throw new UnsupportedOperationException();
	}

	public SchemeKind getSchemeKind() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeLinks() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeOptimizeInfos() {
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
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		throw new UnsupportedOperationException();
	}

	public int getWidth() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The <code>SchemeCableLink</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeCableLink
	 */
	public void removeSchemeCableLink(final SchemeCableLink schemeCableLink) {
		assert schemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeCableLinks().contains(schemeCableLink): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableLink.setParentScheme(null);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 */
	public void removeSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeElements().contains(schemeElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentScheme(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this <code>Scheme</code>,
	 * or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeLinks().contains(schemeLink): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentScheme(null);
	}

	/**
	 * The <code>SchemeOptimizeInfo</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeOptimizeInfo
	 */
	public void removeSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeOptimizeInfos().contains(schemeOptimizeInfo): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfo.setParentScheme(null);
	}

	public void setCurrentSchemeMonitoringSolution(final SchemeMonitoringSolution currentSchemeMonitoringSolution) {
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

	public void setHeight(final int height) {
		throw new UnsupportedOperationException();
	}

	public void setLabel(final String label) {
		throw new UnsupportedOperationException();
	}

	public void setMap(final Map map) {
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

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCableLinks(final Set schemeCableLinks) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeElements(final Set schemeElements) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeKind(final SchemeKind schemeKind) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeLinks(final Set schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeOptimizeInfos(final Set schemeOptimizeInfos) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public void setWidth(final int width) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param description can be null.
	 */
	protected synchronized void setAttributes(final Date created,
			  final Date modified,
			  final Identifier creatorId,
			  final Identifier modifierId,
			  final long version,
			  final Identifier domainId,
			  final String name,
			  final String description) {
		assert name != null;
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
	}

	/**
	 * @param transferable
	 * @throws ApplicationException 
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final Scheme_Transferable scheme = (Scheme_Transferable) transferable;
		super.fromTransferable(scheme.header, new Identifier(scheme.domainId));
		this.name = scheme.name;
		this.description = scheme.description;
		this.label = scheme.label;
		this.width = scheme.width;
		this.height = scheme.height;
		this.schemeKind = scheme.schemeKind;
		this.mapId = new Identifier(scheme.mapId);
		this.symbolId = new Identifier(scheme.symbolId);
		this.ugoCellId = new Identifier(scheme.ugoCellId);
		this.schemeCellId = new Identifier(scheme.schemeCellId);
		this.currentSchemeMonitoringSolutionId = new Identifier(scheme.currentSchemeMonitoringSolutionId);
		this.parentSchemeElementId = new Identifier(scheme.parentSchemeElementId);
	}
}
