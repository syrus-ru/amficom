package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import javax.swing.JPanel;

public abstract class PropertiesPanel extends JPanel
{
	static protected final int DEF_HEIGHT = 24;
	static protected final int DEF_WIDTH = 150;

	public PropertiesPanel()
	{
		super();
		this.setName(LangModel.String("labelTabbedProperties"));
	}

	public abstract ObjectResource getObjectResource();
	public abstract boolean setObjectResource(ObjectResource or);
	public abstract void setContext(ApplicationContext aContext);
	public abstract boolean modify();
	public abstract boolean create();
	public abstract boolean delete();
	public abstract boolean open();
	public abstract boolean save();
	public boolean cancel()
	{
		return false;
	}
}