package com.syrus.AMFICOM.Client.Schematics.UI;

import com.syrus.AMFICOM.Client.Configure.UI.CableLinkPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

public class SchemeCableLinkPane extends CableLinkPane
{
	SchemeCableLink scl;

	public SchemeCableLinkPane()
	{
		super();
	}

	public SchemeCableLinkPane(SchemeCableLink l)
	{
		this();
		setObjectResource(l);
	}

	public ObjectResource getObjectResource()
	{
		return scl;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.scl = (SchemeCableLink )or;
		CableLink link = (CableLink)Pool.get(CableLink.typ, scl.cable_link_id);
		super.setObjectResource(link);
	}

/*
	public boolean modify()
	{
		if (gPanel.modify() &&
				fPanel.modify() &&
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
			dataSource.SaveCableLink(link.getId());
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
		aContext.getDataSourceInterface().RemoveCableLinks(s);

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
