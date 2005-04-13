/*-
 * $Id: Scheme.java,v 1.10 2005/04/13 19:07:46 arseniy Exp $
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TransferableObject;
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
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/04/13 19:07:46 $
 * @module scheme_v1
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
	 * @deprecated Use {@link #createInstance(Identifier, Identifier, String, String)}
	 *             instead.
	 */
	public static Scheme createInstance() {
		throw new UnsupportedOperationException();
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

	public void addSchemeCableLink(final SchemeCableLink schemeCableLink) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeElement(final SchemeElement schemeElement) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		throw new UnsupportedOperationException();
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
	 * @see Namable#getName()
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
	 * @deprecated
	 */
	public SchemeCableLink[] getSchemeCableLinksAsArray() {
		final Set schemeCableLinks = getSchemeCableLinks();
		return (SchemeCableLink[]) schemeCableLinks.toArray(new SchemeCableLink[schemeCableLinks.size()]);
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

	/**
	 * @deprecated
	 */
	public SchemeElement[] getSchemeElementsAsArray() {
		final Set schemeElements = getSchemeElements();
		return (SchemeElement[]) schemeElements.toArray(new SchemeElement[schemeElements.size()]);
	}

	public SchemeKind getSchemeKind() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeLinks() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeLink[] getSchemeLinksAsArray() {
		final Set schemeLinks = getSchemeLinks();
		return (SchemeLink[]) schemeLinks.toArray(new SchemeLink[schemeLinks.size()]);
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
	 * @see TransferableObject#getTransferable()
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

	public void removeSchemeCableLink(final SchemeCableLink schemeCableLink) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeElement(final SchemeElement schemeElement) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		throw new UnsupportedOperationException();
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
		if (description.equals(this.description))
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
	 * @see StorableObject#fromTransferable(IDLEntity)
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
