package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Test.*;
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
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;
/*
		new SurveyDataSourceImage(dataSource).GetTests();
		dataSource.GetAnalysis();
		dataSource.GetModelings();
		dataSource.GetRequests();
		dataSource.GetEvaluations();
*/
		setParameter("treemodel", new ArchiveTreeModel(dataSource));
//		setParameter("treemodel", new ObserverTreeModel(dataSource));
//		super.aContext = this.aContext;

		super.execute();

		Dimension dim = desktop.getSize();
		frame.setLocation(0, dim.height / 4);
		frame.setSize(dim.width / 5, 3 * dim.height / 4);

		new OpenResultsCommand(dispatcher, desktop, aContext, new DefaultResultApplicationModelFactory()).execute();
//		new OpenTestsCommand(dispatcher, desktop, aContext, "test").execute();
	}
}
