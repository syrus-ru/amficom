package com.syrus.AMFICOM.general;

public class UserDatabase extends StorableObjectDatabase {

	private User fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof User)
			return (User) storableObject;
		throw new IllegalDataException("UserDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
}
