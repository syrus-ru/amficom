package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class Rule extends ObjectResource
{
	Rule_Transferable transferable;

    public String event_type_id = "";
    public String source_type_id = "";
    public String logic_text = "";
    public String source_id = "";
	boolean is_generated = true;

	public static final String typ = "eventrule";

	public Rule()
	{
		transferable = new Rule_Transferable();
	}

	public Rule(Rule_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	public Rule(
			String event_type_id,
			String source_type_id,
			String logic_text)
	{
		this.event_type_id = event_type_id;
	    this.source_type_id = source_type_id;
	    this.logic_text = logic_text;

		transferable = new Rule_Transferable();
	}

	public void setLocalFromTransferable()
	{
		this.event_type_id = transferable.event_type_id;
	    this.source_type_id = transferable.source_type_id;
	    this.logic_text = transferable.logic_text;
	    this.source_id = transferable.source_id;
		this.is_generated = transferable.is_generated;
	}

	public void setTransferableFromLocal()
	{
		transferable.event_type_id = event_type_id;
	    transferable.source_type_id = source_type_id;
	    transferable.logic_text = logic_text;
	    transferable.source_id = source_id;
		transferable.is_generated = is_generated;
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getId()
	{
		return "";
	}

	public String getName()
	{
		return logic_text;
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
		setTransferableFromLocal();
		return transferable;
	}

	public String getAlarmType()
	{
		int ind = logic_text.indexOf("GENERATE_ALARM");
		if(ind != -1)
		{
			String s1 = logic_text.substring(
					ind + 
					new String("GENERATE_ALARM").length() + 
					2);
			int ind2 = s1.indexOf("\"");
			String s2 = s1.substring(0, ind2);
			return s2;
		}
		return "";
	}
}