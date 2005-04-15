/*-
 * $Id: SchemePort.java,v 1.14 2005/04/15 19:22:55 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemePort_Transferable;

/**
 * #08 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/04/15 19:22:55 $
 * @module scheme_v1
 */
public final class SchemePort extends AbstractSchemePort {
	private static final long serialVersionUID = 3256436993469658930L;

	private SchemePortDatabase schemePortDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemePort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemePortDatabase = SchemeDatabaseContext.getSchemePortDatabase();
		try {
			this.schemePortDatabase.retrieve(this);
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
	SchemePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemePort(final SchemePort_Transferable transferable) throws CreateObjectException {
		try {
			this.schemePortDatabase = SchemeDatabaseContext.getSchemePortDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static SchemePort createInstance(final Identifier creatorId) throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemePort schemePort = new SchemePort(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SCHEME_PORT_ENTITY_CODE),
					created,
					created,
					creatorId,
					creatorId,
					0L);
			schemePort.changed = true;
			return schemePort;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Object clone() {
		final SchemePort schemePort = (SchemePort) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeLink();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getSort() == PortSort._PORT_SORT_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableThread getSchemeCableThread() {
		throw new UnsupportedOperationException();
	}

	public SchemeLink getSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort() == PortSort._PORT_SORT_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
