/*
 * $Id: User.java,v 1.1 2004/08/09 11:54:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
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
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:54:14 $
 * @author $Author: arseniy $
 * @module general_v1
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
}
