/*
 * $Id: User.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.administration.corba.UserSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
 * @author $Author: arseniy $
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

		this.userDatabase = AdministrationDatabaseContext.userDatabase;
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

		this.userDatabase = AdministrationDatabaseContext.userDatabase;
	}

	protected User(Identifier id,
							 Identifier creatorId,
							 String login,
							 int sort,
							 String name,
							 String description) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;

		super.currentVersion = super.getNextVersion();

		this.userDatabase = AdministrationDatabaseContext.userDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.userDatabase != null)
				this.userDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static User getInstance(User_Transferable ut) throws CreateObjectException {
//		User user = new User(ut);
//		
//		user.userDatabase = AdministrationDatabaseContext.userDatabase;
//		try {
//			if (user.userDatabase != null)
//				user.userDatabase.insert(user);
//		}
//		catch (IllegalDataException ide) {
//			throw new CreateObjectException(ide.getMessage(), ide);
//		}
//		
//		return user;
//	}

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
		super.currentVersion = super.getNextVersion();
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
			return new User(IdentifierPool.getGeneratedIdentifier(ObjectEntities.USER_ENTITY_CODE),
								creatorId,
								login,
								sort.value(),
								name,
								description);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("User.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String login,
																						int sort,
																						String name,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	public List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
