/*
 * $Id: OpenObjectFrameCommand.java,v 1.3 2004/09/27 16:34:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Admin;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAdmin;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import java.awt.*;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 16:34:18 $
 * @module admin_v1
 */
public class OpenObjectFrameCommand extends VoidCommand
{
  String parameter;
  JDesktopPane desktop;
  ApplicationContext aContext;
  ObjectResourceDisplayModel dModel;
  Class orclass;
  ObjectResourceCatalogFrame frame;

  public OpenObjectFrameCommand()
  {
  }

  public OpenObjectFrameCommand(
		JDesktopPane desktop,
		ApplicationContext aContext,
		  String parameter,
		ObjectResourceDisplayModel dModel,
		  Class orclass)
  {
	 this.desktop = desktop;
	 this.aContext = aContext;
	 this.parameter = parameter;
	 this.dModel = dModel;
	 this.orclass = orclass;
  }

  public void setParameter(String field, Object value)
  {
	 if(field.equals("desktop"))
		setDesktop((JDesktopPane)value);
	 else
		if(field.equals("aContext"))
		  setApplicationContext((ApplicationContext )value);
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
	 return new OpenObjectFrameCommand(desktop, aContext, parameter, dModel, orclass);
  }

	public void execute() {
		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Открытие окна представления объектов"));

	 DataSourceInterface dataSource = aContext.getDataSource();
	 if(dataSource == null)
		return;
	 frame =
		  (ObjectResourceCatalogFrame)
		  Pool.get("ObjectFrame", "AdministrateObjectFrame");
	 if(frame != null)
	 {
		Pool.remove(frame);
		frame.dispose();
	 }

	 new ObjectDataSourceImage(dataSource).GetObjects();

	 frame = new ObjectResourceCatalogFrame(
			LangModelAdmin.getString("titleObjectsCatalog"),
				aContext);

	 frame.setDisplayModel(dModel);
	 frame.setContents(Pool.getList(parameter));
	 frame.setObjectResourceClass(orclass);

	 Dimension d = desktop.getSize();

	 int y = (int )d.getHeight();
	 int x = (int )d.getWidth();
	 int pointX = (int )(x * 0.3);
	 int pointY = 0;
	 int sizeX = (int )(x * 0.7);
	 int sizeY = (int )y;

	 frame.setBounds(pointX, pointY, sizeX, sizeY);
	 desktop.add(frame);
	 frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage
												 ("¤")));

	ObjectResourceCatalogActionModel orcam = new ObjectResourceCatalogActionModel(
			ObjectResourceCatalogActionModel.NO_PANEL,
			ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
			ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
			ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
			ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
			ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);

	 if(parameter.equals(Domain.typ))
	 {
		frame.setTitle("Домены");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(CommandPermissionAttributes.typ))
	 {
		frame.setTitle("Доступ к модулям");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(OperatorCategory.typ))
	 {
		frame.setTitle("Категории");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	 }
	 else if(parameter.equals(OperatorGroup.typ))
	 {
		frame.setTitle("Группы");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(OperatorProfile.typ))
	 {
		frame.setTitle("Профили");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(User.typ))
	 {
		frame.setTitle("Пользователи");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	 }
	 else if(parameter.equals(Agent.typ))
	 {
		frame.setTitle("Агенты");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(Server.typ))
	 {
		frame.setTitle("Сервера");
	  orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals(Client.typ))
	 {
		frame.setTitle("Клиенты");
		orcam = new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }
	 else if(parameter.equals("modul"))
	 {
		frame.setTitle("Доступ к модулям");
		orcam = new ObjectResourceCatalogActionModel(
			 ObjectResourceCatalogActionModel.PANEL,
			 ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
			 ObjectResourceCatalogActionModel.SAVE_BUTTON,
			 ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
			 ObjectResourceCatalogActionModel.PROPS_BUTTON,
			 ObjectResourceCatalogActionModel.CANCEL_BUTTON);
	 }

	 frame.setActionModel(orcam);


	 frame.setVisible(true);

	 aContext.getDispatcher().notify(new TreeListSelectionEvent(parameter, TreeListSelectionEvent.SELECT_EVENT, true));

	 Pool.put("ObjectFrame", "AdministrateObjectFrame", frame);

		Environment.getDispatcher().notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, " "));
	}
}
