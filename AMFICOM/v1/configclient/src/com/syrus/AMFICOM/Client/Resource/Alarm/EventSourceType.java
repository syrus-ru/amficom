package com.syrus.AMFICOM.Client.Resource.Alarm;

import java.util.Vector;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class EventSourceType extends ObjectResource
{
	/**
	 * Value: {@value}.
	 */
	public static final String ID_CLIENT_SOURCE = "clientsource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_EQUIPMENT_SOURCE = "equipmentsource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_ISM_SOURCE = "ismsource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_KIS_SOURCE = "KISsource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_NET_SOURCE = "netsource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_SERVER_SOURCE = "serversource";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_USER_SOURCE = "usersource";

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CLIENT_SOURCE = ID_CLIENT_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EQUIPMENT_SOURCE = ID_EQUIPMENT_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ISM_SOURCE = ID_ISM_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_KIS_SOURCE = ID_KIS_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_NET_SOURCE = ID_NET_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SERVER_SOURCE = ID_SERVER_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_USER_SOURCE = ID_USER_SOURCE;

	EventSourceType_Transferable transferable;

    public String id = "";
    public String name = "";

	public Vector rules = new Vector();

	public static final String typ = "eventsourcetype";

	public EventSourceType()
	{
		transferable = new EventSourceType_Transferable();
	}

	public EventSourceType(EventSourceType_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	EventSourceType(
			String id,
		    String name)
	{
		this.id = id;
	    this.name = name;

		transferable = new EventSourceType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
	    this.name = transferable.name;

		for(int i = 0; i < transferable.rules.length; i++)
			rules.add(new Rule(transferable.rules[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
	    transferable.name = name;

		transferable.rules = new Rule_Transferable[rules.size()];
		for(int i = 0; i < rules.size(); i++)
		{
			Rule rule = (Rule )rules.get(i);
			transferable.rules[i] = (Rule_Transferable )rule.getTransferable();
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
	
	public String getTyp()
	{
		return typ;
	}
	
	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
		return transferable;
	}
}