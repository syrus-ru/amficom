package com.syrus.AMFICOM.Client.Schematics.Scheme;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class PrimarySchemeEditorFrame extends //SchemeViewerFrame
		SchemeEditorFrame
{
	public PrimarySchemeEditorFrame(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		super(aContext, pane);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
//			if (see.OPEN_PRIMARY_SCHEME)
//			{
//				Scheme sch = (Scheme)see.obj;
//				if (sch.getName().equals(""))
//				{
//					if (panel instanceof SchemePanel)
//						setTitle(LangModelSchematics.getString("schemeMainTitle"));
//					else
//						setTitle(LangModelSchematics.getString("elementsUGOTitle"));
//				}
//				else
//					setTitle(sch.getName());
//			}
			if (see.SCHEME_CHANGED)
			{
				pane.setGraphChanged(true);
			}
		}
		super.operationPerformed(ae);
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

