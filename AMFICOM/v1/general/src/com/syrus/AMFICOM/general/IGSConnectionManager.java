/*
 * $Id: IGSConnectionManager.java,v 1.2 2005/04/27 13:59:20 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 13:59:20 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface IGSConnectionManager {

	IdentifierGeneratorServer getIGSReference() throws CommunicationException;
}
