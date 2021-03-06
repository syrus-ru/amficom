/*-
 * $Id: StorableObjectType.java,v 1.40 2006/04/20 12:35:18 arseniy Exp $
 *
 * Copyright ? 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * @version $Revision: 1.40 $, $Date: 2006/04/20 12:35:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public abstract class StorableObjectType extends StorableObject {

	/**
	 * ??????? ???. ????????? ????? ???????? ????? ????????.
	 */
	protected String codename;

	/**
	 * ????????.
	 */
	protected String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public StorableObjectType(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creator_id,
			final Identifier modifier_id,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, created, modified, creator_id, modifier_id, version);
		this.codename = codename;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected StorableObjectType(/*IdlStorableObjectType*/) {
		// super();
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	protected StorableObjectType(final XmlIdentifier id,
			final String importType,
			final short entityCode,
			final Date created,
			final Identifier creatorId) throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param transferable
	 * @param codename1
	 * @param description1
	 */
	protected final void fromIdlTransferable(final IdlStorableObject transferable,
			final String codename1,
			final String description1) {
		super.fromIdlTransferable(transferable);
		this.codename = codename1;
		this.description = description1;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.codename != null && this.codename.length() != 0;
	}
	
	public String getCodename() {
		return this.codename;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setCodename0(final String codename) {
		this.codename = codename;
	}

	public void setCodename(final String codename) {
		this.setCodename0(codename);
		super.markAsChanged();
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		assert codename != null && codename.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		this.codename = codename;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		this.description = description;
	}

	public static <T extends StorableObjectType> Map<String, T> createCodenamesMap(final Set<T> storableObjectTypes) {
		final Map<String, T> codenamesMap = new HashMap<String, T>();
		for (final T storableObjectType : storableObjectTypes) {
			codenamesMap.put(storableObjectType.getCodename(), storableObjectType);
		}
		return codenamesMap;
	}
}
