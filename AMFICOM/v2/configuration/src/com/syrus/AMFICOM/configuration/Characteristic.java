package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;

public class Characteristic extends StorableObject  {
	private Identifier type_id;
	private String name;
	private String description;
	private String value;

	private StorableObject_Database characteristicDatabase;

	public Characteristic(Identifier id) throws RetrieveObjectException {
		super(id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
}