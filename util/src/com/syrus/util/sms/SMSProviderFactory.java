package com.syrus.util.sms;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 */
public final class SMSProviderFactory {
	public static SMSProvider getDefaultSMSProvider() {
		return SMSMailProvider.getInstance();
	}
}
