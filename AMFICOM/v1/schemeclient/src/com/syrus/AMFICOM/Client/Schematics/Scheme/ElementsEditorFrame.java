package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.ElementsPanel;

public class ElementsEditorFrame extends SchemeViewerFrame
{
	public ElementsEditorFrame(ApplicationContext aContext, ElementsPanel panel)
	{
		super(aContext, panel);
	}

	protected void closeFrame ()
	{
		((ElementsPanel)panel).scheme_elemement = null;
		//super.closeFrame();
	}

	public void doDefaultCloseAction()
	{
		ElementsPanel ep = (ElementsPanel)panel;
		if (ep.getGraph().isGraphChanged() && ep.scheme_elemement !=  null)
		{
			int res = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Компонент был изменен. Сохранить компонент перед закрытием?",
					"Подтверждение",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				ep.updateSchemeElement();
				aContext.getDispatcher().notify(new SchemeElementsEvent(
						this, ep.scheme_elemement, SchemeElementsEvent.SCHEME_CHANGED_EVENT));
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}
		super.doDefaultCloseAction();
	}

}

