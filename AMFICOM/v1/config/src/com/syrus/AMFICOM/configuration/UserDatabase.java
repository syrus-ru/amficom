package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;

public class UserDatabase extends StorableObjectDatabase {

	private User fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof User)
			return (User) storableObject;
		throw new IllegalDataException("UserDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
}
