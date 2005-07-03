/*
 * $Id: OpenAllWindowsCommand.java,v 1.2 2004/09/27 16:29:07 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Admin;

import com.syrus.AMFICOM.Client.General.Command.Admin.OpenWhoAmIFrameCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import javax.swing.JDesktopPane;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/27 16:29:07 $
 * @module admin_v1
 */
public class OpenAllWindowsCommand extends VoidCommand
{
  JDesktopPane desktop;
  ApplicationContext aContext;
  String parameter;
  ObjectResourceDisplayModel dModel;
  Class orclass;

  Dispatcher dispatcher;
  String rootTitle;
  String root_name;
  String[] root_objects;
  String[] filters;


  public OpenAllWindowsCommand(JDesktopPane desktop, ApplicationContext aContext,
                               String parameter, ObjectResourceDisplayModel dModel,
                               Class orclass,

                               Dispatcher dispatcher, String rootTitle,
                               String root_name, String[] root_objects,
                               String[] filters)
  {
    this.desktop = desktop;
    this.aContext = aContext;
    this.parameter = parameter;
    this.dModel = dModel;
    this.orclass = orclass;

    this.dispatcher = dispatcher;
    this.root_name = root_name;
    this.rootTitle = rootTitle;
    this.root_objects = root_objects;
    this.filters = filters;
  }


  public void setDesktop(JDesktopPane desktop)
  {
    this.desktop = desktop;
  }

  public void setApplicationContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
  }

  public Object clone()
  {
    return new OpenAllWindowsCommand(desktop, aContext, parameter,
                                     dModel, orclass,
                                     dispatcher, rootTitle,
                                     root_name, root_objects,
                                     filters);
  }

	public void execute() {
		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Инициализация интерфейса"));

		(new ViewObjectNavigatorCommand(dispatcher, desktop, rootTitle, aContext )).execute();
		(new OpenObjectFrameCommand(desktop, aContext, parameter, dModel, orclass)).execute();
		(new OpenWhoAmIFrameCommand(desktop, aContext)).execute();

		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "  "));
	}
}
