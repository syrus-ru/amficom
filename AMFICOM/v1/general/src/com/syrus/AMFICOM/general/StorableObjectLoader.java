package com.syrus.AMFICOM.general;

public interface StorableObjectLoader {

	StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException;

}
