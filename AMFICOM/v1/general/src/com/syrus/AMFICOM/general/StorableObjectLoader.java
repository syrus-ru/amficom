package com.syrus.AMFICOM.general;

public interface StorableObjectLoader {

	StorableObject load(Identifier objectId) throws DatabaseException, CommunicationException;

}
