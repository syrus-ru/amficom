/*
 * $Id: MessageImpl.java,v 1.1 2004/06/22 12:27:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.alarm;

import com.syrus.AMFICOM.corba.portable.common.*;
import java.util.Date;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public class MessageImpl extends Message {
	{
		alertingId = new IdentifierImpl("");
		messageTypeId = new IdentifierImpl("");
		eventId = new IdentifierImpl("");
		eventDate = 0L;
		eventSourceName = "";
		eventSourceDescription = "";
		transmissionPathName = "";
		transmissionPathDescription = "";
		text = "";
	}

	public MessageImpl() {
	}
	
	public Identifier getAlertingId() {
		return alertingId;
	}

	public void setAlertingId(Identifier alertingId) {
		if (alertingId == null)
			throw new NullPointerException();
		this.alertingId = alertingId;
	}

	public Identifier getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(Identifier messageTypeId) {
		if (messageTypeId == null)
			throw new NullPointerException();
		this.messageTypeId = messageTypeId;
	}

	public Identifier getEventId() {
		return eventId;
	}

	public void setEventId(Identifier eventId) {
		if (eventId == null)
			throw new NullPointerException();
		this.eventId = eventId;
	}

	public long getEventDate() {
		return eventDate;
	}

	public void setEventDate(long eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventDate(Date eventDate) {
		try {
			this.eventDate = eventDate.getTime();
		} catch (NullPointerException npe) {
			this.eventDate = 0L;
		}
	}

	public String getEventSourceName() {
		return eventSourceName;
	}

	public void setEventSourceName(String eventSourceName) {
		if (eventSourceName == null)
			eventSourceName = "";
		this.eventSourceName = eventSourceName;
	}

	public String getEventSourceDescription() {
		return eventSourceDescription;
	}

	public void setEventSourceDescription(String eventSourceDescription) {
		if (eventSourceDescription == null)
			eventSourceDescription = "";
		this.eventSourceDescription = eventSourceDescription;
	}

	public String getTransmissionPathName() {
		return transmissionPathName;
	}

	public void setTransmissionPathName(String transmissionPathName) {
		if (transmissionPathName == null)
			transmissionPathName = "";
		this.transmissionPathName = transmissionPathName;
	}

	public String getTransmissionPathDescription() {
		return transmissionPathDescription;
	}

	public void setTransmissionPathDescription(String transmissionPathDescription) {
		if (transmissionPathDescription == null)
			transmissionPathDescription = "";
		this.transmissionPathDescription = transmissionPathDescription;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null)
			text = "";
		this.text = text;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof MessageImpl) {
			MessageImpl anotherMessageImpl = (MessageImpl) obj;
			return (anotherMessageImpl.alertingId.equals(alertingId)
				&& anotherMessageImpl.messageTypeId.equals(messageTypeId)
				&& anotherMessageImpl.eventId.equals(eventId)
				&& (anotherMessageImpl.eventDate == eventDate)
				&& anotherMessageImpl.eventSourceName.equals(eventSourceName)
				&& anotherMessageImpl.eventSourceDescription.equals(eventSourceDescription)
				&& anotherMessageImpl.transmissionPathName.equals(transmissionPathName)
				&& anotherMessageImpl.transmissionPathDescription.equals(transmissionPathDescription)
				&& anotherMessageImpl.text.equals(text));
		}
		return false;
	}

	public int hashCode() {
		int result = 17;
		result *= 37;
		result += alertingId.hashCode();
		result *= 37;
		result += messageTypeId.hashCode();
		result *= 37;
		result += eventId.hashCode();
		result *= 37;
		result += (int) (eventDate ^ (eventDate >>> 32));
		result *= 37;
		result += eventSourceName.hashCode();
		result *= 37;
		result += eventSourceDescription.hashCode();
		result *= 37;
		result += transmissionPathName.hashCode();
		result *= 37;
		result += transmissionPathDescription.hashCode();
		result *= 37;
		result += text.hashCode();
		return result;
	}

	protected String paramString() {
		String returnValue = getClass().getName();
		returnValue += "; alertingId = \"" + alertingId + '"';
		returnValue += "; messageTypeId = \"" + messageTypeId + '"';
		returnValue += "; eventId = \"" + eventId + '"';
		returnValue += "; eventDate = \"" + (new Date(eventDate)) + '"';
		returnValue += "; eventSourceName = \"" + eventSourceName + '"';
		returnValue += "; eventSourceDescription = \"" + eventSourceDescription + '"';
		returnValue += "; transmissionPathName = \"" + transmissionPathName + '"';
		returnValue += "; transmissionPathDescription = \"" + transmissionPathDescription + '"';
		returnValue += "; text = \"" + text + '"';
		return returnValue;
	}

	public String toString() {
		return paramString();
	}
}
