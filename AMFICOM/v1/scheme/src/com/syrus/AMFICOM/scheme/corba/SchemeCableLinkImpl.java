/*
 * $Id: SchemeCableLinkImpl.java,v 1.3 2004/11/24 13:05:02 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/24 13:05:02 $
 * @module schemecommon_v1
 */
public final class SchemeCableLinkImpl extends SchemeCableLink implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeCableLinkImpl() {
	}

	SchemeCableLinkImpl(final Identifier id) {
		this.thisId = id;
	}

	/**
	 * @see #abstractLinkTypeImpl()
	 */
	public AbstractLinkType_Transferable abstractLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkTypeImpl(AbstractLinkType) 
	 */
	public void abstractLinkType(final AbstractLinkType_Transferable abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkType()
	 */
	public AbstractLinkType abstractLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkType(AbstractLinkType_Transferable)
	 */
	public void abstractLinkTypeImpl(final AbstractLinkType abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
		throw new UnsupportedOperationException();
	}

	public CableChannelingItem[] cableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkTypeImpl()
	 */
	public LinkType_Transferable cableLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkTypeImpl(LinkType)
	 */
	public void cableLinkType(final LinkType_Transferable cableLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkType() 
	 */
	public LinkType cableLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkType(LinkType_Transferable)
	 */
	public void cableLinkTypeImpl(final LinkType cableLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristicsImpl()
	 */
	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristicsImpl(Characteristic[])
	 */
	public void characteristics(final Characteristic_Transferable characteristics[]) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristics()
	 */
	public Characteristic[] characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristics(Characteristic_Transferable[])
	 */
	public void characteristicsImpl(final Characteristic characteristics[]) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink cloneInstance() {
		try {
			return (SchemeCableLink) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #linkImpl()
	 */
	public Link_Transferable link() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #link()
	 */
	public Link linkImpl() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public double opticalLength() {
		throw new UnsupportedOperationException();
	}

	public void opticalLength(double opticalLength) {
		throw new UnsupportedOperationException();
	}

	public double physicalLength() {
		throw new UnsupportedOperationException();
	}

	public void physicalLength(double physicalLength) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread[] schemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort sourceAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort sourceSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort targetAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort targetSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
