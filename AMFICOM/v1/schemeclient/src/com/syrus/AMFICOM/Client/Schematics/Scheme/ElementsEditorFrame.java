package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;

public class ElementsEditorFrame extends SchemeViewerFrame
{
	public ElementsEditorFrame(ApplicationContext aContext, UgoTabbedPane pane)
	{
		super(aContext, pane);
	}

	protected void closeFrame ()
	{
		pane.getPanel().getGraph().setSchemeElement(null);
		//super.closeFrame();
	}

	public void doDefaultCloseAction()
	{
		ElementsPanel ep = (ElementsPanel)pane.getPanel();
		if (ep.getGraph().isGraphChanged() && ep.getGraph().getSchemeElement() !=  null)
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
						this, ep.getGraph().getSchemeElement(), SchemeElementsEvent.SCHEME_CHANGED_EVENT));
			}
			else if (res == JOptionPane.CANCEL_OPTION)
				return;
		}
		super.doDefaultCloseAction();
	}

}

