package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.Config.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

public class SurveyNewMapViewCommand extends MapViewOpenCommand
{
	public SurveyNewMapViewCommand()
	{
		// nothing
	}

	public SurveyNewMapViewCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		super(dispatcher, desktop, aContext, factory);
	}

	public Object clone()
	{
		return new SurveyNewMapViewCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		try
		{
			super.execute();

			if (opened)
			{
				MapMainFrame frame = (MapMainFrame )Pool.get("environment", "mapmainframe");
				frame.getModel().getCommand("mapModeViewNodes").execute();
				frame.getModel().getCommand("mapModePath").execute();
			
				Dimension dim = desktop.getSize();

				ViewMapPropertiesCommand c1 = new ViewMapPropertiesCommand(desktop, aContext);
				c1.execute();
				c1.frame.setLocation(0, dim.height * 2 / 8);
				c1.frame.setSize(dim.width / 5, dim.height * 3 / 8);

				c1.frame.setObjectResource((ObjectResource )Pool.get(MapContext.typ, mc_id));

				ViewMapElementsCommand c2 = new ViewMapElementsCommand(desktop, aContext);
				c2.execute();
				c2.frame.setLocation(0, dim.height * 5 / 8);
				c2.frame.setSize(dim.width * 1 / 5, dim.height * 3 / 8);

				c2.frame.setMapContext((MapContext )Pool.get(MapContext.typ, mc_id));
			}
		}
		catch (RuntimeException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(desktop, "Не могу найти файл лицензии ofx.properties", "Ошибка", JOptionPane.OK_OPTION);
		}
	}
}