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

public class ServerDatabase extends StorableObjectDatabase {
	private Server fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Server)
			return (Server) storableObject;
		throw new IllegalDataException("ServerDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}
