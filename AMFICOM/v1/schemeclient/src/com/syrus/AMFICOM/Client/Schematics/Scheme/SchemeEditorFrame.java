package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.scheme.corba.Scheme;

public class SchemeEditorFrame extends SchemeViewerFrame
{
	public SchemeEditorFrame(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		super(aContext, pane);
	}

	protected void closeFrame()
	{
//		pane.getPanel().getGraph().setScheme(null);
		super.closeFrame();
	}

	public void doDefaultCloseAction()
	{
		pane.removeAllPanels();
		/*
		SchemeGraph graph = pane.getPanel().getGraph();
		if (graph.getScheme() !=  null && graph.isGraphChanged())
		{
			int res = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Схема была изменена. Сохранить изменения?",
					"Подтверждение",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.YES_OPTION)
			{
				new SchemeSaveCommand(aContext, (SchemeTabbedPane)pane, null);
				graph.setGraphChanged(false);
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}
		super.doDefaultCloseAction();
	*/
	}
}
