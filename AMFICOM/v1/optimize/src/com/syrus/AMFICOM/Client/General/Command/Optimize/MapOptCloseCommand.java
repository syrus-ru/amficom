package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

//====================================================================================================
public class MapOptCloseCommand extends VoidCommand
{
  Dispatcher dispatcher;
  ApplicationContext aContext;
  OptimizeMDIMain parent;
  //--------------------------------------------------------------
  public MapOptCloseCommand(){}
  //--------------------------------------------------------------
  public MapOptCloseCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain parent)
  { this.dispatcher = dispatcher;
    this.parent = parent;
    this.aContext = aContext;
  }
  //--------------------------------------------------------------
  public void setParameter(String field, Object value)
  { if(field.equals("dispatcher"))
    { setDispatcher((Dispatcher)value);
    }
    else if(field.equals("aContext"))
    { setApplicationContext((ApplicationContext )value);
    }
  }
  //--------------------------------------------------------------
  public void setDispatcher(Dispatcher dispatcher)
  { this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  { this.aContext = aContext;
  }
  //--------------------------------------------------------------
  public Object clone(){return new MapOptCloseCommand(dispatcher, aContext, parent);}
  //--------------------------------------------------------------
  public void execute()
  { //parent.ismMapContext = null; - ismMapContext больше не используется (depricated)
    if(parent.mapFrame != null)
    {  dispatcher.notify(new OperationEvent(this, 0, "map_close"));
    }
    parent.mapContext = null;
    parent.map_is_opened = false;
  }
  //--------------------------------------------------------------
}
//====================================================================================================