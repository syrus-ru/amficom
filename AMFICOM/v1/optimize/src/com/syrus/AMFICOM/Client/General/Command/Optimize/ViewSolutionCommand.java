package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// команда на просмотр подроьного вида решени€ оптимизации
public class ViewSolutionCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	private OptimizeMDIMain mdiMain;
	//-------------------------------------------------------------------------------------------------------------
	public ViewSolutionCommand(){}
	//-------------------------------------------------------------------------------------------------------------
	public ViewSolutionCommand(   Dispatcher             dispatcher,
																JDesktopPane           desktop,
																ApplicationContext     aContext,
																OptimizeMDIMain        mdiMain  )
	{	this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.mdiMain = mdiMain;
	}
	//-------------------------------------------------------------------------------------------------------------
	// функци€ необходима дл€ установки некоторых параметров, которые в момент создани€ экземпл€ра этого класса ещЄ могут быть неизвестны (создаютс€ позже)
	public void setParameter(String field, Object value)
	{	if(field.equals("dispatcher"))
			setDispatcher( (Dispatcher)value );
		else
		if(field.equals("aContext"))
			setApplicationContext( (ApplicationContext)value );
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
	{	return new ViewSolutionCommand(dispatcher, desktop, aContext, mdiMain);
	}
	//-------------------------------------------------------------------------------------------------------------
	public void execute()
	{		System.out.println("Starting Solution frame");
			// окно отображени€ подробных путей в виде таблицы
			if(mdiMain.solutionFrame == null)
			{  mdiMain.solutionFrame = new ViewSolutionFrame(mdiMain);
				 desktop.add(mdiMain.solutionFrame);
				 mdiMain.solutionFrame.setVisible(true);
			}
		 else //если уже открыто, то просто располагаем на своЄм месте заново
		 {	mdiMain.solutionFrame.place();
				mdiMain.solutionFrame.toFront();
		 }
	}
	//-------------------------------------------------------------------------------------------------------------
}