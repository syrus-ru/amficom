package com.syrus.AMFICOM.Client.General.Command.Optimize;


import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

import com.syrus.AMFICOM.Client.General.Checker;
//===============================================================================================================
public class MapOptOpenCommand extends VoidCommand
{ Dispatcher dispatcher;
  ApplicationContext aContext;
  OptimizeMDIMain mdiMain;
  //-----------------------------------------------------------------
  public MapOptOpenCommand(){}
  //-----------------------------------------------------------------
  public MapOptOpenCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain parent)
  {	this.dispatcher = dispatcher;
    this.mdiMain = parent;
    this.aContext = aContext;
  }
  //-----------------------------------------------------------------
  public void setParameter(String field, Object value)
  {	if(field.equals("dispatcher"))
      setDispatcher((Dispatcher)value);
    else
    if(field.equals("aContext"))
      setApplicationContext((ApplicationContext )value);
  }
  //-----------------------------------------------------------------
  public void setDispatcher(Dispatcher dispatcher)
  {	this.dispatcher = dispatcher;
  }
  //-----------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  {	this.aContext = aContext;
  }
  //-----------------------------------------------------------------
  public Object clone()
  {	return new MapOptOpenCommand(this.dispatcher, this.aContext,this.mdiMain);
  }
  //-----------------------------------------------------------------
  public void execute()
  {	// проверка прав доступа
    Checker checker = new Checker( this.aContext.getDataSourceInterface() );
    if( !checker.checkCommand(Checker.openMapToBeOptimized) )
    {
  return;
    }

    MapOpenCommand com = new MapOpenCommand(null, null, this.aContext);
    com.execute();
    if( com.retCode == 1 )
    { // закрыть всё, что было открыто, если открытие новой карты подтверждено
      new CloseAllCommand(this.dispatcher, this.mdiMain.desktopPane, this.aContext, this.mdiMain).execute();

      this.mdiMain.mapContext = (MapContext )Pool.get("mapcontext", com.retobj_id);
      this.dispatcher.notify(new OperationEvent(this, 0, "mapopened"));
      this.mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent(""));
      // при открытии карты подгружаем и схему ( при открытии схемы будут открыты все )
      Scheme scheme = (Scheme )Pool.get("scheme", this.mdiMain.mapContext.scheme_id);
      new SchemeOpenCommand(this.dispatcher, this.aContext, this.mdiMain).execute(scheme);  
    }
  }
  //-----------------------------------------------------------------
}
