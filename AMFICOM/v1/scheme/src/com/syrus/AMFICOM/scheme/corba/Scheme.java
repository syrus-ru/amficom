/*
 * $Id: Scheme.java,v 1.4 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class Scheme extends AbstractCloneableDomainMember implements Namable,
		Describable, SchemeCellContainer {
	protected String description = null;

	protected Identifier mapId = null;

	protected String name = null;

	protected Identifier schemeCableLinkIds[] = null;

	/**
	 * Takes non-null value at pack time.
	 */
	protected Identifier schemeCellId = null;

	protected Identifier schemeElementIds[] = null;

	protected Identifier schemeLinkIds[] = null;

	protected Identifier schemeMonitoringSolutionId = null;

	protected Identifier symbolId = null;

	protected int thisHeight = 0;

	protected String thisLabel = null;

	protected Type thisType = null;

	protected int thisWidth = 0;

	/**
	 * Takes non-null value at pack time.
	 */
	protected Identifier ugoCellId = null;

	Scheme(final Identifier id) {
		super(id);
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

	public Object clone() {
		final Scheme scheme = (Scheme) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return scheme;
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public Map getMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#getSymbol()
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

	public int height() {
		throw new UnsupportedOperationException();
	}

	public void height(int height) {
		throw new UnsupportedOperationException();
	}

	public String label() {
		throw new UnsupportedOperationException();
	}

	public void label(String label) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink[] schemeCableLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeCableLinks(SchemeCableLink[] schemeCableLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement[] schemeElements() {
		throw new UnsupportedOperationException();
	}

	public void schemeElements(SchemeElement[] schemeElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] schemeLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeLinks(SchemeLink[] schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution schemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	public void schemeMonitoringSolution(
			SchemeMonitoringSolution schemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	public void setMap(final Map map) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(SchemeImageResource schemeCellImpl) {
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
	public void setUgoCell(SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public Type type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newType
	 * @see com.syrus.AMFICOM.scheme.corba.Scheme#type(com.syrus.AMFICOM.scheme.corba.SchemePackage.Type)
	 */
	public void type(Type newType) {
		throw new UnsupportedOperationException();
	}

	public int width() {
		throw new UnsupportedOperationException();
	}

	public void width(int width) {
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
}
