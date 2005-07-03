package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

public class SurveyMapViewOpenCommand extends MapEditorOpenViewCommand
{
	public SurveyMapViewOpenCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super(desktop, aContext);
	}

	public void execute()
	{
		try
		{
			super.execute();

			if (super.getResult() == Command.RESULT_OK)
			{
				MapFrame frame = super.getMapFrame();
				frame.getModel().getCommand("mapModeViewNodes").execute();
				frame.getModel().getCommand("mapModePath").execute();

				Dimension dim = super.desktop.getSize();

				super.getPropertiesFrame().setLocation(
						0, 
						dim.height * 2 / 8);
				super.getPropertiesFrame().setSize(
						dim.width / 5, 
						dim.height * 3 / 8);

				super.getElementsFrame().setLocation(
						0, 
						dim.height * 5 / 8);
				super.getElementsFrame().setSize(
						dim.width * 1 / 5, 
						dim.height * 3 / 8);
			}
		}
		catch (RuntimeException ex)
		{
			ex.printStackTrace();
			setResult(Command.RESULT_NO);
//			JOptionPane.showMessageDialog(desktop, "Не могу найти файл лицензии ofx.properties", "Ошибка", JOptionPane.OK_OPTION);
		}
	}
}

