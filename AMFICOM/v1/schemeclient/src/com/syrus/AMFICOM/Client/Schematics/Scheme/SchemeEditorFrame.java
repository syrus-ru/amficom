package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class SchemeEditorFrame extends SchemeViewerFrame
{
	public SchemeEditorFrame(ApplicationContext aContext, SchemePanel panel)
	{
		super(aContext, panel);
	}

	protected void closeFrame()
	{
		panel.scheme = new Scheme();
		super.closeFrame();
	}

	public void doDefaultCloseAction()
	{
		if (panel.scheme !=  null && panel.getGraph().isGraphChanged())
		{
			int res = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Схема была изменена. Сохранить изменения?",
					"Подтверждение",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.YES_OPTION)
			{
				new SchemeSaveCommand(aContext, (SchemePanel)panel, null);
				((SchemePanel)panel).getGraph().setGraphChanged(false);
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}
		super.doDefaultCloseAction();
	}

}
