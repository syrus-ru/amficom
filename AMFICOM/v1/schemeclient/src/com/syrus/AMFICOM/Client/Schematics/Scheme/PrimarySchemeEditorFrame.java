package com.syrus.AMFICOM.Client.Schematics.Scheme;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.General.SchemePanel;
import com.syrus.AMFICOM.Client.Schematics.General.UgoPanel;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

public class PrimarySchemeEditorFrame extends //SchemeViewerFrame
		SchemeEditorFrame
{
	public PrimarySchemeEditorFrame(ApplicationContext aContext, SchemePanel panel)
	{
		super(aContext, panel);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.OPEN_PRIMARY_SCHEME)
			{
				Scheme sch = (Scheme)see.obj;
				if (sch.getName().equals(""))
				{
					if (panel instanceof SchemePanel)
						setTitle(LangModelSchematics.String("schemeMainTitle"));
					else
						setTitle(LangModelSchematics.String("elementsUGOTitle"));
				}
				else
					setTitle(sch.getName());
			}
			if (see.SCHEME_CHANGED)
			{
				panel.getGraph().setGraphChanged(true);
			}
		}
		super.operationPerformed(ae);
	}

	protected void closeFrame()
	{
		panel.scheme = new Scheme();
		if (panel instanceof SchemePanel)
			setTitle(LangModelSchematics.String("schemeMainTitle"));
		else
			setTitle(LangModelSchematics.String("elementsUGOTitle"));
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

