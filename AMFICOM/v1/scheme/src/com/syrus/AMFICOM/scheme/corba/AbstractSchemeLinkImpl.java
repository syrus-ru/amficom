/*
 * $Id: AbstractSchemeLinkImpl.java,v 1.7 2004/12/17 15:58:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import java.util.*;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/12/17 15:58:58 $
 * @module schemecommon_v1
 */
final class AbstractSchemeLinkImpl extends AbstractSchemeLink {
	private static final long serialVersionUID = 1419449466646507243L;

	public AbstractLinkType_Transferable abstractLinkType() {
		throw new UnsupportedOperationException();
	}

	public void abstractLinkType(AbstractLinkType_Transferable abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	public AbstractLinkType abstractLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	public void abstractLinkTypeImpl(AbstractLinkType abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	public List characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	public void characteristicsImpl(List characteristics) {
		throw new UnsupportedOperationException();
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public Date getCreated() {
		return createdImpl();
	}

	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		return modifiedImpl();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public Link_Transferable link() {
		throw new UnsupportedOperationException();
	}

	public void link(Link_Transferable link) {
		throw new UnsupportedOperationException();
	}

	public Link linkImpl() {
		throw new UnsupportedOperationException();
	}

	public void linkImpl(Link link) {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
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

	public AbstractSchemePort sourceAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public void sourceAbstractSchemePort(AbstractSchemePort sourceAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort targetAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public void targetAbstractSchemePort(AbstractSchemePort targetAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}
}
