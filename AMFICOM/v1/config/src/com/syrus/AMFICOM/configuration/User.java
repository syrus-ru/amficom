/*
 * $Id: User.java,v 1.11 2004/11/04 09:05:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.corba.UserSort;

/**
 * @version $Revision: 1.11 $, $Date: 2004/11/04 09:05:13 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class User extends StorableObject {
	private String login;
	private int sort;
	private String name;
	private String description;

	private StorableObjectDatabase userDatabase;

	public User(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.userDatabase = ConfigurationDatabaseContext.userDatabase;
		try {
			this.userDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public User(User_Transferable ut) throws CreateObjectException {
		super(new Identifier(ut.id),
					new Date(ut.created),
					new Date(ut.modified),
					new Identifier(ut.creator_id),
					new Identifier(ut.modifier_id));
		this.login = ut.login;
		this.sort = ut.sort.value();
		this.name = new String(ut.name);
		this.description = new String(ut.description);		
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
		
		this.userDatabase = ConfigurationDatabaseContext.userDatabase;
	}
	
	public static User getInstance(User_Transferable ut) throws CreateObjectException {
		User user = new User(ut);
		
		user.userDatabase = ConfigurationDatabaseContext.userDatabase;
		try {
			if (user.userDatabase != null)
				user.userDatabase.insert(user);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return user;
	}

	public Object getTransferable() {
		return new User_Transferable((Identifier_Transferable)super.id.getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creatorId.getTransferable(),
																 (Identifier_Transferable)super.modifierId.getTransferable(),
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

	public static User createInstance(Identifier id,
																		Identifier creatorId,
																		String login,
																		UserSort sort,
																		String name,
																		String description) {
		return new User(id,
										creatorId,
										login,
										sort.value(),
										name,
										description);
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
