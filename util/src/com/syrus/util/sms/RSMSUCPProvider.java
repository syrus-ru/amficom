/*
 * $Id: RSMSUCPProvider.java,v 1.1 2004/05/06 11:48:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.sms;

import com.syrus.AMFICOM.corba.portable.common.MessageDeliveryFailedException;

/**
 * @verson $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 */
final class RSMSUCPProvider implements SMSProvider {
	public void sendMessage(String from, String destination, String message) throws MessageDeliveryFailedException {
		throw new UnsupportedOperationException();
	}
}
