package com.syrus.AMFICOM.Client.Model;

import java.awt.*;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class Model
{
	ApplicationContext aContext = new ApplicationContext();

	public Model(ModelApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_MODEL))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new ModelMDIMain(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/model_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		new Model(new DefaultModelApplicationModelFactory());
	}
}

