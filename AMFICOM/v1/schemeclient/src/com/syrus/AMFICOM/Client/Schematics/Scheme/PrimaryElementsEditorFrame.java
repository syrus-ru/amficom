package com.syrus.AMFICOM.Client.Schematics.Scheme;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.ElementsTabbedPane;

public class PrimaryElementsEditorFrame extends //SchemeViewerFrame
		SchemeViewerFrame
{
	public PrimaryElementsEditorFrame(ApplicationContext aContext, ElementsTabbedPane pane)
	{
		super(aContext, pane);
	}

	protected void closeFrame()
	{
	}

	/*

	protected void closeFrame()
	{
		if (panel.scheme !=  null && panel.getGraph().changed)
		{
			int res = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Схема была изменена. Сохранить схему перед закрытием?",
					"Подтверждение",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				Scheme scheme = panel.updateScheme();
				aContext.getDataSourceInterface().SaveScheme(scheme.getId());
				panel.getGraph().changed = false;
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}

		if (panel instanceof SchemePanel)
			setTitle(LangModelSchematics.String("schemeMainTitle"));
		else
			setTitle(LangModelSchematics.String("elementsUGOTitle"));
		panel.scheme = null;
		panel.getGraph().removeAll();
		panel.getGraph().changed = false;
		doDefaultCloseAction();
	}*/
}


