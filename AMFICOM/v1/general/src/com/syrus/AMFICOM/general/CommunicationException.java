/*
 * $Id: CommunicationException.java,v 1.2 2004/08/06 13:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/06 13:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class CommunicationException extends Exception {
	private static final long serialVersionUID = 2340932236057527353L;

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(String message, Exception e) {
		super(message, e);
	}
}
