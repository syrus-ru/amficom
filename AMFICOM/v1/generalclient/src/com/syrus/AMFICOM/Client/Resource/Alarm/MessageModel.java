package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 * 
 * @see Message
 * @see com.syrus.AMFICOM.Client.Object.UI.MessageDisplayModel
 */
public final class MessageModel extends ObjectResourceModel
{
//	private static final ResourceBundle messageModelResourceBundle = ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelListResourceBundle");
	private static final ResourceBundle messageModelResourceBundle = ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle");

	public static final String ALERTING_ID = "alertingId";
	public static final String EVENT_ID = "eventId";
	public static final String TEXT = "text";

	public static final String ALERTING_ID_TITLE = messageModelResourceBundle.getString("Alerting_Id");
	public static final String EVENT_ID_TITLE = messageModelResourceBundle.getString("Event_Id");
	public static final String TEXT_TITLE = messageModelResourceBundle.getString("Text");

	private Message message;
	
	private MessageModel()
	{
	}

	public MessageModel(Message message)
	{
		this.message = message;
	}

	public String getColumnValue(String col_id)
	{
		String returnValue;
		if (col_id.equals(ALERTING_ID))
			returnValue = message.getId();
		else if (col_id.equals(EVENT_ID))
			returnValue = message.getEventId();
		else if (col_id.equals(TEXT))
			returnValue = message.getText();
		else
			returnValue = null;
		if (returnValue == null)
			returnValue = "";
		return returnValue;
	}
}
