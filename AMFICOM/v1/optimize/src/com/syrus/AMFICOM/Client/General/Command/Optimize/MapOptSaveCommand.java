package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;

import com.syrus.AMFICOM.Client.General.Checker;
// команда "сохранить данное решение оптимизации"
public class MapOptSaveCommand extends VoidCommand
{	Dispatcher dispatcher;
	ApplicationContext aContext;
	OptimizeMDIMain parent;
	//-------------------------------------------------------------------------------------------------------------
	public MapOptSaveCommand(){}
	//-------------------------------------------------------------------------------------------------------------
	public MapOptSaveCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain parent)
	{	this.dispatcher = dispatcher;
		this.parent = parent;
		this.aContext = aContext;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
		{	setDispatcher((Dispatcher)value);
		}
		else if(field.equals("aContext"))
		{	setApplicationContext((ApplicationContext )value);
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void setApplicationContext(ApplicationContext aContext)
	{	this.aContext = aContext;
	}
	//-------------------------------------------------------------------------------------------------------------
	public Object clone()
	{	return new MapOptSaveCommand(dispatcher, aContext, parent);
	}
	//-------------------------------------------------------------------------------------------------------------
	public void execute()
	{	// проверка прав доступа
		Checker checker = new Checker( aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.saveResultOfOptimization) )
    {
  return;
    }

		NewJMapContextDialog dialog = new NewJMapContextDialog( parent, "—войства контекста", true, parent.ismMapContext);
		ISMMapContextPane mcp = (ISMMapContextPane )dialog.mainPanel;
		mcp.mapTextField.setEnabled(false);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
		{  frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{  frameSize.width = screenSize.width;
		}
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.setVisible(true);
		if ( dialog.ifAccept() )
		{  DataSourceInterface dataSource = aContext.getDataSourceInterface();
			 if(dataSource != null)
			 {  dataSource.SaveJMap(parent.ismMapContext.getId());
			 }
		}
	}
	//-------------------------------------------------------------------------------------------------------------
}