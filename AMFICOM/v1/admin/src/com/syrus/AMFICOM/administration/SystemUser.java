/*
 * $Id: SystemUser.java,v 1.2 2005/06/17 11:01:06 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.SystemUser_Transferable;
import com.syrus.AMFICOM.administration.corba.SystemUser_TransferablePackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/17 11:01:06 $
 * @author $Author: bass $
 * @module administration_v1
 */

public final class SystemUser extends StorableObject {
	private static final long serialVersionUID = 7173419705878464356L;
	
	private String login;
	private int sort;
	private String name;
	private String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public SystemUser(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		SystemUserDatabase database = (SystemUserDatabase) DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	SystemUser(final SystemUser_Transferable ut) {
		this.fromTransferable(ut);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	SystemUser(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String login,
			final int sort,
			final String name,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(final IDLEntity transferable) {
		SystemUser_Transferable ut = (SystemUser_Transferable) transferable;
		try {
			super.fromTransferable(ut.header);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.login = ut.login;
		this.sort = ut.sort.value();
		this.name = ut.name;
		this.description = ut.description;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new SystemUser_Transferable(super.getHeaderTransferable(),
				this.login,
				SystemUserSort.from_int(this.sort),
				this.name,
				this.description);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected boolean isValid() {
		return super.isValid()
				&& this.login != null && this.login.length() != 0
				&& this.name != null && this.name.length() != 0
				&& this.description != null;
	}
	
	public String getLogin() {
		return this.login;
	}

	public SystemUserSort getSort() {
		return SystemUserSort.from_int(this.sort);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * client constructor
	 * @param creatorId
	 * @param login
	 * @param sort
	 * @param name
	 * @param description
	 * @throws CreateObjectException
	 */
	public static SystemUser createInstance(final Identifier creatorId,
			final String login,
			final SystemUserSort sort,
			final String name,
			final String description) throws CreateObjectException {
		try {
			Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
			SystemUser user = new SystemUser(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					0L,
					login,
					sort.value(),
					name,
					description);

			assert user.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			user.markAsChanged();

			return user;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String login,
			final int sort,
			final String name,
			final String description) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {		
		return Collections.EMPTY_SET;
	}
	
	public void setLogin(final String login) {
		this.login = login;
		super.markAsChanged();
	}
	
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	
	public void setSort(final SystemUserSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}
}
