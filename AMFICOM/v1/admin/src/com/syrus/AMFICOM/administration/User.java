/*
 * $Id: User.java,v 1.24 2005/06/02 14:26:54 arseniy Exp $
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

import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/06/02 14:26:54 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class User extends StorableObject {
	private static final long serialVersionUID = 7173419705878464356L;
	
	private String login;
	private int sort;
	private String name;
	private String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public User(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		UserDatabase database = (UserDatabase) DatabaseContext.getDatabase(ObjectEntities.USER_ENTITY_CODE);
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
	User(User_Transferable ut) {
		this.fromTransferable(ut);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	User(Identifier id,
					 Identifier creatorId,
					 long version,
					 String login,
					 int sort,
					 String name,
					 String description) {
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
	protected void fromTransferable(IDLEntity transferable) {
		User_Transferable ut = (User_Transferable) transferable;
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
		return new User_Transferable(super.getHeaderTransferable(),
									 this.login,
									 UserSort.from_int(this.sort),
									 this.name,
									 this.description);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
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

	public UserSort getSort() {
		return UserSort.from_int(this.sort);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
		super.changed = true;
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
	public static User createInstance(Identifier creatorId, String login, UserSort sort, String name, String description)
			throws CreateObjectException {
		try {
			Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(ObjectEntities.USER_ENTITY_CODE);
			User user = new User(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					0L,
					login,
					sort.value(),
					name,
					description);

			assert user.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			user.changed = true;
			try {
				StorableObjectPool.putStorableObject(user);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

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
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												String login,
												int sort,
												String name,
												String description) {
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
	
	public void setLogin(String login) {
		this.login = login;
		super.changed = true;
	}
	
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	
	public void setSort(UserSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}
}
