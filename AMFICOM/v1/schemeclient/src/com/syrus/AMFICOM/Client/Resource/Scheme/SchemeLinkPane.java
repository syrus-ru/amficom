package com.syrus.AMFICOM.Client.Resource.Scheme;

import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Link;

public class SchemeLinkPane extends LinkPane
{
	SchemeLink sl;

	public SchemeLinkPane()
	{
		super();
	}

	public SchemeLinkPane(SchemeLink l)
	{
		this();
		setObjectResource(l);
	}

	public ObjectResource getObjectResource()
	{
		return sl;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.sl = (SchemeLink )or;
		Link link = (Link)Pool.get(Link.typ, sl.link_id);
		super.setObjectResource(link);

		if(link != null)
			return true;
		return false;
	}

/*  public boolean modify()
	{
		if (gPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveLink(link.getId());
				return true;
		}
		else
		{
			new MessageBox(LangModelConfig.getString("err_incorrect_data_input")).show();
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = link.id;
		aContext.getDataSourceInterface().RemoveLinks(s);

		return true;
	}

	public boolean create()
	{
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}*/
}
