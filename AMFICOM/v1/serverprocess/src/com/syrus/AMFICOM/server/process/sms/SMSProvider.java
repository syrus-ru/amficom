/*
 * $Id: SMSProvider.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.server.process.sms;

import com.syrus.AMFICOM.corba.portable.common.MessageDeliveryFailedException;

/**
 * @verson $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
public interface SMSProvider {
	/**
	 * @param from the "from" field of the message
	 * @param destination the phone number of the person message is to be 
	 *        delivered to.
	 * @param message actual text of the message, 160 symbols if message
	 *        contains only US-ASCII symbols, 70 symbols otherwise.
	 * @throws MessageDeliveryFailedException
	 */
	void sendMessage(String from, String destination, String message) throws MessageDeliveryFailedException;
}
