package com.syrus.AMFICOM.Client.General.Command.Admin;

import java.awt.*;

import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;

public class OpenObjectFrameCommand extends VoidCommand// implements OperationListener
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

  public void execute()
  {
    Environment.the_dispatcher.notify(new StatusMessageEvent("Открытие окна представления объектов"));

    DataSourceInterface dataSource = aContext.getDataSourceInterface();
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
			LangModelAdmin.String("titleObjectsCatalog"),
            aContext);

    frame.setDisplayModel(dModel);
    frame.setContents(new DataSet(Pool.getHash(parameter)));
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

    Environment.the_dispatcher.notify(new StatusMessageEvent(" "));
  }
}

