/*
 * $Id: User.java,v 1.8 2005/04/01 10:31:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/01 10:31:51 $
 * @author $Author: bass $
 * @module administration_v1
 */

public class User extends StorableObject {
	private static final long serialVersionUID = 7173419705878464356L;
	
	private String login;
	private int sort;
	private String name;
	private String description;

	private StorableObjectDatabase userDatabase;

	public User(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.userDatabase = AdministrationDatabaseContext.getUserDatabase();
		try {
			this.userDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public User(User_Transferable ut) {
		super(ut.header);
		this.login = ut.login;
		this.sort = ut.sort.value();
		this.name = new String(ut.name);
		this.description = new String(ut.description);

		this.userDatabase = AdministrationDatabaseContext.getUserDatabase();
	}

	protected User(Identifier id,
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

		this.userDatabase = AdministrationDatabaseContext.getUserDatabase();
	}

	public Object getTransferable() {
		return new User_Transferable(super.getHeaderTransferable(),
									 new String(this.login),
									 UserSort.from_int(this.sort),
									 new String(this.name),
									 new String(this.description));
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
	public static User createInstance(Identifier creatorId,
									  String login,
									  UserSort sort,
									  String name,
									  String description) throws CreateObjectException {
		if (creatorId == null || login == null || name == null || 
				description == null || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			User user = new User(IdentifierPool.getGeneratedIdentifier(ObjectEntities.USER_ENTITY_CODE),
							creatorId,
							0L,
							login,
							sort.value(),
							name,
							description);
			user.changed = true;
			return user;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("User.createInstance | cannot generate identifier ", e);
		}
	}

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
