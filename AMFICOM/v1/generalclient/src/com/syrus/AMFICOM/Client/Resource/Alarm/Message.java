package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 13:24:31 $
 * @module generalclient_v1
 *
 * @see MessageModel
 * @see com.syrus.AMFICOM.Client.Object.UI.MessageDisplayModel
 * @deprecated
 */
public class Message extends StubResource
{
	public String id;
	public String text;
	public String eventId;

	public static final String typ = "message";

	Message_Transferable transferable;

	private final MessageModel messageModel = new MessageModel(this);

	public Message()
	{
	}

	/**
	 * @throws java.lang.IllegalArgumentException
	 */
	public Message(Message_Transferable transferable) throws IllegalArgumentException
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @throws java.lang.IllegalArgumentException
	 */
	public Message(String id, String text, String eventId) throws IllegalArgumentException
	{
		this.id = id;
		this.text = text;
		this.eventId = eventId;
		try
		{
			setTransferableFromLocal();
		}
		catch (NullPointerException npe)
		{
			transferable = new Message_Transferable();
			setTransferableFromLocal();
		}
	}

	/**
	 * Getter for property <code>alertingId</code>.
	 */
	public String getAlertingId()
	{
		return id;
	}

	/**
	 * Getter for property <code>eventId</code>.
	 */
	public String getEventId()
	{
		return eventId;
	}

	/**
	 * Getter for property <code>text</code>.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @throws java.lang.IllegalArgumentException
	 */
	public void setLocalFromTransferable() throws IllegalArgumentException
	{
		checkValidMessage_Transferable(transferable);
		id = transferable.id;
		text = transferable.text;
		eventId = transferable.event_id;
	}

	/**
	 * @throws java.lang.IllegalArgumentException
	 */
	public void setTransferableFromLocal() throws IllegalArgumentException
	{
		checkValidMessage(this);
		transferable.id = id;
		transferable.text = text;
		transferable.event_id = eventId;
	}

	public void updateLocalFromTransferable()
	{
	}

	/**
	 * Specified by {@link ObjectResource#getId() ObjectResource.getId()}
	 *
	 * @see #getAlertingId()
	 */
	public String getId()
	{
		return getAlertingId();
	}


	public String getDomainId()
	{
		return "sysdomain";
	}

	/**
	 * Specified by {@link ObjectResource#getName() ObjectResource.getName()}
	 *
	 * @see #getText()
	 */
	public String getName()
	{
		return getText();
	}

	public String getTyp()
	{
		return typ;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	/**
	 * Checks for validity the message sequence supplied.
	 *
	 * @param messageSeq the message sequence to be checked for validity.
	 * @throws java.lang.IllegalArgumentException if message sequence is
	 *         invalid.
	 */
	private static void checkValidMessageSeq(Message messageSeq[]) throws IllegalArgumentException
	{
		try
		{
			if (messageSeq.length == 0)
				throw new IllegalArgumentException("Message sequence cannot be empty.");
			for (int i = 0; i < messageSeq.length; i ++)
				checkValidMessage(messageSeq[i]);
		}
		catch (NullPointerException npe)
		{
			throw new IllegalArgumentException("Message sequence cannot be null.");
		}
	}

	/**
	 * Checks for validity the message supplied.
	 *
	 * @param message the message to be checked for validity.
	 * @throws java.lang.IllegalArgumentException if message is invalid.
	 */
	private static void checkValidMessage(Message message) throws IllegalArgumentException
	{
		try
		{
			String id = message.id;
			try
			{
				if (id.length() == 0)
					throw new IllegalArgumentException("Message id cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message id cannot be null.");
			}
			String eventId = message.eventId;
			try
			{
				if (eventId.length() == 0)
					throw new IllegalArgumentException("Message alarm id cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message alarm id cannot be null.");
			}
			String text = message.text;
			try
			{
				if (text.length() == 0)
					throw new IllegalArgumentException("Message text cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message text cannot be null.");
			}
		}
		catch (NullPointerException npe)
		{
			throw new IllegalArgumentException("Message cannot be null.");
		}
	}

	/**
	 * Checks for validity the message sequence supplied.
	 *
	 * @param messageSeq_Transferable the message sequence to be checked for
	 *        validity.
	 * @throws java.lang.IllegalArgumentException if message sequence is
	 *         invalid.
	 */
	private static void checkValidMessageSeq_Transferable(Message_Transferable messageSeq_Transferable[]) throws IllegalArgumentException
	{
		try
		{
			if (messageSeq_Transferable.length == 0)
				throw new IllegalArgumentException("Message sequence cannot be empty.");
			for (int i = 0; i < messageSeq_Transferable.length; i ++)
				checkValidMessage_Transferable(messageSeq_Transferable[i]);
		}
		catch (NullPointerException npe)
		{
			throw new IllegalArgumentException("Message sequence cannot be null.");
		}
	}

	/**
	 * Checks for validity the message supplied.
	 *
	 * @param message_Transferable the message to be checked for validity.
	 * @throws java.lang.IllegalArgumentException if message is invalid.
	 */
	private static void checkValidMessage_Transferable(Message_Transferable message_Transferable) throws IllegalArgumentException
	{
		try
		{
			String id = message_Transferable.id;
			try
			{
				if (id.length() == 0)
					throw new IllegalArgumentException("Message id cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message id cannot be null.");
			}
			String eventId = message_Transferable.event_id;
			try
			{
				if (eventId.length() == 0)
					throw new IllegalArgumentException("Message alarm id cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message alarm id cannot be null.");
			}
			String text = message_Transferable.text;
			try
			{
				if (text.length() == 0)
					throw new IllegalArgumentException("Message text cannot be empty.");
			}
			catch (NullPointerException npe)
			{
				throw new IllegalArgumentException("Message text cannot be null.");
			}
		}
		catch (NullPointerException npe)
		{
			throw new IllegalArgumentException("Message cannot be null.");
		}
	}

	public ObjectResourceModel getModel()
	{
		return messageModel;
	}
}
