package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.Analysis.UI.ArchiveTreeModel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.*;

public class ViewSurveyNavigatorCommand extends ViewNavigatorCommand
{
//	ApplicationContext aContext;

	public ViewSurveyNavigatorCommand(Dispatcher dispatcher, JDesktopPane desktop, String title, ApplicationContext aContext)
	{
		super(dispatcher, desktop, title);
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewSurveyNavigatorCommand(dispatcher, desktop, title, aContext);
	}

	public void execute()
	{
		try
		{
			Identifier domainId = new Identifier(aContext.getSessionInterface().getAccessIdentifier().domain_id);
			Domain domain = (Domain )ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			setParameter("treemodel", new ArchiveTreeModel(domain));
	
			super.execute();
	
			Dimension dim = desktop.getSize();
			frame.setLocation(0, dim.height / 4);
			frame.setSize(dim.width / 5, 3 * dim.height / 4);
	
			new OpenResultsCommand(dispatcher, desktop, aContext).execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
