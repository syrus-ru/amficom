package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

import com.syrus.AMFICOM.Client.General.Checker;
//===============================================================================================================
public class MapOptOpenCommand extends VoidCommand
{
  Dispatcher dispatcher;
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
  {	return new MapOptOpenCommand(dispatcher, aContext, mdiMain);
  }
  //-----------------------------------------------------------------
  public void execute()
  {	// проверка прав доступа
    Checker checker = new Checker( aContext.getDataSourceInterface() );
    if( !checker.checkCommand(checker.openMapToBeOptimized) )
    {
  return;
    }

    MapOpenCommand com = new MapOpenCommand(null, null, aContext);
    com.execute();
    if( com.retCode == 1 )
    { //закрыть всё, что было открыто, если открытие новой карты подтверждено
      new CloseAllCommand(dispatcher, mdiMain.desktopPane, aContext, mdiMain).execute();

      mdiMain.mapContext = (MapContext )Pool.get("mapcontext", com.retobj_id);
      dispatcher.notify(new OperationEvent(this, 0, "mapopened"));
      mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent(""));

      Scheme scheme = (Scheme )Pool.get("scheme", mdiMain.mapContext.scheme_id);//при открытии карты подгружаем и схему
      new SchemeOpenCommand(dispatcher, aContext, mdiMain).execute(scheme);

      // при открытии карты  открываем всё окна
      // new ViewShowallCommand(dispatcher, mdiMain.desktopPane, aContext, mdiMain).execute();
    }
  }
  //-----------------------------------------------------------------
}
