package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.Arrays;

import java.awt.Dimension;
import javax.swing.BorderFactory;

import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.scheme.PathElementController;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import oracle.jdeveloper.layout.XYConstraints;

public class TransmissionPathFibrePanel extends GeneralPanel
{
	protected SchemePath path;

	private ObjectResourceTable linksTable = new ObjectResourceTable(PathElementController.getInstance());

	protected TransmissionPathFibrePanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected TransmissionPathFibrePanel(SchemePath path)
	{
		this();
		setObject(path);
	}

	private void jbInit() throws Exception
	{
		setName("Список волокон");

		this.setPreferredSize(new Dimension(510, 410));
		this.setMaximumSize(new Dimension(510, 410));
		this.setMinimumSize(new Dimension(510, 410));

		linksTable.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(linksTable,    new XYConstraints(2, 35, 455, 120));
	}

	public Object getObject()
	{
		return path;
	}

	public void setObject(Object or)
	{
		path = (SchemePath)or;

		ObjectResourceTableModel model = (ObjectResourceTableModel)linksTable.getModel();
		model.setContents(Arrays.asList(path.links()));
	}

	public boolean modify()
	{
		return true;
	}
}