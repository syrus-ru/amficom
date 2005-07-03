package com.syrus.AMFICOM.Client.Administrate.Object;
import java.awt.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;

public class AdministrateTreeModel extends ObjectResourceTreeModel
{
  DataSourceInterface dsi;
  private static final String user_props = "user_props";
  private static final String permission_to_obj = "permission_to_obj";
  private static final String agent_server_client = "agent_server_client";
  private static final String root = "root";

//--------------------------------------------------------
  public AdministrateTreeModel(DataSourceInterface dsi)
  {
    this.dsi = dsi;
  }

//--------------------------------------------------------
  public ObjectResourceTreeNode getRoot()
  {
    return (new ObjectResourceTreeNode(root, "Объекты администрирования", true));
  }

//--------------------------------------------------------
  public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
  {
    if(node.getObject() instanceof User)
    {
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/whami.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(node.getObject() instanceof OperatorProfile)
    {
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/profiles.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(node.getObject() instanceof OperatorGroup)
    {
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/groups.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(node.getObject() instanceof Domain)
    {
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/domains.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else
    {
      return null;
    }
  }

//--------------------------------------------------------
  public Color getNodeTextColor(ObjectResourceTreeNode node)
  {
    return null;
  }


//--------------------------------------------------------
  public void nodeAfterSelected(ObjectResourceTreeNode node)
  {
  }

//--------------------------------------------------------
  public void nodeBeforeExpanded(ObjectResourceTreeNode node)
  {
  }

  public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
  {
    if(node.getObject() instanceof String)
    {
      String s = (String )node.getObject();
      if(s.equals(User.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.NO_PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
      else
      if(s.equals(OperatorProfile.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(OperatorGroup.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(OperatorCategory.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
      else
      if(
          s.equals(Checker.admin)  ||
          s.equals(Checker.model)  ||
          s.equals(Checker.ana)    ||
          s.equals(Checker.optim)  ||
          s.equals(Checker.observ) ||
          s.equals(Checker.config) ||
          s.equals(Checker.plan)   ||
          s.equals(Checker.predict) ||
          s.equals(CommandPermissionAttributes.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(Domain.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(Agent.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(Client.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
      else
      if(s.equals(Server.typ))
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);
    }
	else
    if(node.getObject() instanceof Domain)
        return new ObjectResourceCatalogActionModel(
            ObjectResourceCatalogActionModel.PANEL,
            ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
            ObjectResourceCatalogActionModel.SAVE_BUTTON,
            ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
            ObjectResourceCatalogActionModel.PROPS_BUTTON,
            ObjectResourceCatalogActionModel.CANCEL_BUTTON);

    return new ObjectResourceCatalogActionModel(
        ObjectResourceCatalogActionModel.NO_PANEL,
        ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
        ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
        ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
        ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
        ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
  }

  private Class getNodeClass(ObjectResourceTreeNode node)
  {
    if(node.getObject() instanceof User)
    {
      return User.class;
    }
    else if(node.getObject() instanceof OperatorCategory)
    {
      return OperatorCategory.class;
    }
    else if(node.getObject() instanceof OperatorProfile)
    {
      return OperatorProfile.class;
    }
    else if(node.getObject() instanceof OperatorGroup)
    {
      return OperatorGroup.class;
    }
    else if(node.getObject() instanceof CommandPermissionAttributes)
    {
      return CommandPermissionAttributes.class;
    }
    else if(node.getObject() instanceof Domain)
    {
      return DomainForAdmin.class;
    }
    else if(node.getObject() instanceof Agent)
    {
      return Agent.class;
    }
    else if(node.getObject() instanceof Client)
    {
      return Client.class;
    }
    else if(node.getObject() instanceof Server)
    {
      return Server.class;
    }
    else return null;
  }

//--------------------------------------------------------
  public Class getNodeChildClass(ObjectResourceTreeNode node)
  {
    if(node.getObject() instanceof ObjectResource)
    {
      if(//node.getParent() != null && (node.getParent().equals(OperatorCategory.typ || node.getParent().eqals(OperatorGroup.typ)) )&&
         (node.getObject() instanceof OperatorCategory || node.getObject() instanceof OperatorGroup))
        return User.class;

      return getNodeClass(node);
    }
    else if(node.getObject() instanceof String)
    {
      String s = (String)node.getObject();
      if(s.equals(User.typ))
      { //User;
        return User.class;
      }
      else if(s.equals(OperatorProfile.typ))
      { //Profile;
        return OperatorProfile.class;
      }
      else if(s.equals(OperatorCategory.typ))
      { //Category;
        return OperatorCategory.class;
      }
      else if(s.equals(OperatorGroup.typ))
      { //Group;
        return OperatorGroup.class;
      }
      else if(s.equals(Domain.typ))
      {
        return DomainForAdmin.class;
      }
      else if(s.equals(Agent.typ))
      {
        return Agent.class;
      }
      else if(s.equals(Server.typ))
      {
        return Server.class;
      }
      else if(s.equals(Client.typ))
      {
        return Client.class;
      }
      else if(s.equals(Checker.admin)  ||
              s.equals(Checker.model)  ||
              s.equals(Checker.ana)    ||
              s.equals(Checker.optim)  ||
              s.equals(Checker.observ) ||
              s.equals(Checker.config) ||
              s.equals(Checker.plan)   ||
              s.equals(Checker.predict))
      {
        return CommandPermissionAttributes.class;
      }
      else if(s.equals(CommandPermissionAttributes.typ))
      {
        return CommandPermissionAttributes.class;
      }
      else
        return null;
    }
    return null;
  }

//--------------------------------------------------------
  public List getChildNodes(ObjectResourceTreeNode node)
  {
    List vec = new ArrayList();
    ObjectResourceTreeNode ortn;

    if(node.getObject() instanceof String)
    {
      String s = (String)node.getObject();
      if(s.equals(root)) // Root of the tree
      {
        ortn = new ObjectResourceTreeNode(user_props, "Пользователи", true);
        vec.add(ortn);

        ortn = new ObjectResourceTreeNode(agent_server_client, "Архитектура ИСМ", true);
        vec.add(ortn);

        ortn = new ObjectResourceTreeNode(permission_to_obj, "Доступ к объектам", true);
        vec.add(ortn);
        registerSearchableNode(Domain.typ, ortn);
      }
      else if(s.equals(User.typ))// case of the User is selected
      {
        ortn = (ObjectResourceTreeNode)node.getParent();
        if(ortn!=null && ortn.getObject().equals(root))
        {
          this.setVectorFromPool(vec, User.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(), User.typ, vec);
        }
      }
      else if(s.equals(OperatorProfile.typ)) // case of the Profile is selected
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(user_props))
        {
          this.setVectorFromPool(vec, OperatorProfile.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      OperatorProfile.typ, vec);
        }
      }
      else if(s.equals(OperatorCategory.typ)) // case of the Category is selected
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(user_props))
        {
          this.setVectorFromPool(vec, OperatorCategory.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      OperatorCategory.typ, vec);
        }
      }
      else if(s.equals(OperatorGroup.typ)) // case of the Group is selected
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(user_props))
        {
          this.setVectorFromPool(vec, OperatorGroup.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      OperatorGroup.typ, vec);
        }
      }
      else if(s.equals("modul")) // Commands are selected
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null) // && ortn.getObject().equals(root))
        {
          ortn = new ObjectResourceTreeNode (Checker.admin, "Администрирование", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.model, "Моделирование", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.ana, "Анализ", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.optim, "Оптимизация", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.observ, "Наблюдение", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.config, "Конфигурирование", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.plan, "Планирование", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);

          ortn = new ObjectResourceTreeNode(Checker.predict, "Прогнозирование", true);
          vec.add(ortn);
          registerSearchableNode(CommandPermissionAttributes.typ, ortn);
        }
      }
      else if(s.equals(CommandPermissionAttributes.typ))
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      CommandPermissionAttributes.typ, vec);
        }
      }
      else if(s.equals(Checker.ana)) // Analysis
      {
        this.setCommandVectorFromPool(vec, Checker.ana);
      }
      else if(s.equals(Checker.model)) //Model
      {
        this.setCommandVectorFromPool(vec, Checker.model);
      }
      else if(s.equals(Checker.admin)) // Administrating
      {
        this.setCommandVectorFromPool(vec, Checker.admin);
      }
      else if(s.equals(Checker.plan)) // Planner
      {
        this.setCommandVectorFromPool(vec, Checker.plan);
      }
      else if(s.equals(Checker.optim)) // Optimizer
      {
        this.setCommandVectorFromPool(vec, Checker.optim);
      }
      else if(s.equals(Checker.observ)) // Observing
      {
        this.setCommandVectorFromPool(vec, Checker.observ);
      }
      else if(s.equals(Checker.config)) // Configuring
      {
        this.setCommandVectorFromPool(vec, Checker.config);
      }
      else if(s.equals(Checker.predict)) // Prediction
      {
        this.setCommandVectorFromPool(vec, Checker.predict);
      }
      else if(s.equals(permission_to_obj))
      {
        ortn = (ObjectResourceTreeNode)node.getParent();
        if(ortn != null && ortn.getObject().equals(root))
        {
          ortn = new ObjectResourceTreeNode(Domain.typ, "Домены", true);
          vec.add(ortn);
          registerSearchableNode(Domain.typ, ortn);

          ortn = new ObjectResourceTreeNode("modul", "Модули", true);
          vec.add(ortn);
        }
      }
      else if(s.equals(user_props))
      {
        ortn = (ObjectResourceTreeNode)node.getParent();
        if(ortn != null && ortn.getObject().equals(root))
        {
          ortn = new ObjectResourceTreeNode(OperatorProfile.typ, "Профили", true);
          vec.add(ortn);
          registerSearchableNode(OperatorProfile.typ, ortn);

          ortn = new ObjectResourceTreeNode(OperatorGroup.typ, "Группы", true);
          vec.add(ortn);
          registerSearchableNode(OperatorGroup.typ, ortn);

          ortn = new ObjectResourceTreeNode(OperatorCategory.typ, "Категории", true);
          vec.add(ortn);
          registerSearchableNode(OperatorCategory.typ, ortn);
        }
      }
      else if(s.equals(agent_server_client))
      {
        ortn = (ObjectResourceTreeNode)node.getParent();
        if(ortn != null && ortn.getObject().equals(root))
        {
          ortn = new ObjectResourceTreeNode(Client.typ, "Клиент", true);
          vec.add(ortn);
          registerSearchableNode(Client.typ, ortn);

          ortn = new ObjectResourceTreeNode(Server.typ, "Сервер", true);
          vec.add(ortn);
          registerSearchableNode(Server.typ, ortn);

          ortn = new ObjectResourceTreeNode(Agent.typ, "Агент", true);
          vec.add(ortn);
          registerSearchableNode(Agent.typ, ortn);

        }
      }
      else if(s.equals(Domain.typ))
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(permission_to_obj))
        {
          Map dSet = Pool.getMap(Domain.typ);
          if (dSet != null)
          {
            ImageIcon ii = getIcon(Domain.typ);
            for(Iterator it = dSet.values().iterator(); it.hasNext();)
            {
              Domain dom = (Domain)it.next();
              if(dom.domain_id == null ||
                 dom.domain_id.equals(""))
                vec.add(new ObjectResourceTreeNode(dom, dom.getName(), true, ii));
            }
          }
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      Domain.typ, vec);
        }
      }
      else if(s.equals(Agent.typ))
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(agent_server_client))
        {
          this.setVectorFromPool(vec, Agent.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      Agent.typ, vec);
        }
      }
      else if(s.equals(Client.typ))
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(agent_server_client))
        {
          this.setVectorFromPool(vec, Client.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      Client.typ, vec);
        }
      }
      else if(s.equals(Server.typ))
      {
        ortn = (ObjectResourceTreeNode )node.getParent();
        if(ortn!=null && ortn.getObject().equals(agent_server_client))
        {
          this.setVectorFromPool(vec, Server.typ);
        }
        else if(ortn != null && ortn.getObject() instanceof ObjectResource)
        {
          setVectorFromObjectResource((AdminObjectResource)ortn.getObject(),
                                      Server.typ, vec);
        }
      }
    }
    else if(node.getObject() instanceof ObjectResource)
    {

      AdminObjectResource or = (AdminObjectResource )node.getObject();
      {
        for(Iterator it = or.getChildTypes().iterator(); it.hasNext();)
        {
          String s = (String)it.next();

          if(s.equals(OperatorProfile.typ))
          {
              ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Профили", true);
              vec.add(tmp);
          }
          else if(s.equals(OperatorGroup.typ))
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Группы", true);
            vec.add(tmp);
          }
          else if(s.equals(OperatorCategory.typ))
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Категории", true);
            if(getChildNodes(tmp).size() == 0)
              tmp = new ObjectResourceTreeNode(s, "Категории", true, true);
            vec.add(tmp);
          }
          else if(s.equals(CommandPermissionAttributes.typ))
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Команды", true);
            vec.add(tmp);
          }
          else if(s.equals(User.typ))
          {
            if(node.getObject() instanceof OperatorCategory || node.getObject() instanceof OperatorGroup)
            {
              setVectorFromObjectResource(or, User.typ, vec);
            }
            else
            {
              ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Пользователи", true);
              vec.add(tmp);
            }
          }
          else if(s.equals(Domain.typ))
          {
            setVectorFromObjectResource(or, Domain.typ, vec);
          }
          else if(s.equals(Agent.typ))//////////////////////
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Агент", true);
            vec.add(tmp);
          }
          else if(s.equals(Client.typ))
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Клиент", true);
            vec.add(tmp);
          }
          else if(s.equals(Server.typ))
          {
            ObjectResourceTreeNode tmp = new ObjectResourceTreeNode(s, "Сервер", true);
            vec.add(tmp);
          }
        }
      }
    }

    return vec;
  }

  private void setVectorFromPool(List vec, String typ)
  {
    ImageIcon ii = getIcon(typ);

    ObjectResourceTreeNode ortn;
    Map map = Pool.getMap(typ);
    if (map != null)
    {
      for(ListIterator it = getSortedElements(map).listIterator(); it.hasNext();)
      {
        ObjectResource or = (ObjectResource)it.next();
        if(typ.equals(User.typ) || typ.equals(Agent.typ) || typ.equals(Client.typ) || typ.equals(Server.typ))
          ortn = new ObjectResourceTreeNode(or, or.getName(), true, ii, true);
        else
          ortn = new ObjectResourceTreeNode(or, or.getName(), true, ii);
        vec.add(ortn);
      }
    }
  }

  private void setCommandVectorFromPool(List vec, String filterGroup)
  {
    ObjectResourceTreeNode ortn;

    ImageIcon ii = getIcon(CommandPermissionAttributes.typ);

    Map map = Pool.getMap(CommandPermissionAttributes.typ);
    if (map != null)
    {
      for(ListIterator it = getSortedElements(map).listIterator(); it.hasNext();)
      {
        CommandPermissionAttributes cpa = (CommandPermissionAttributes)it.next();
        if(cpa.filterGroup.equals(filterGroup))
        {
          ortn = new ObjectResourceTreeNode(cpa, cpa.getName(), true, ii, true);
          vec.add(ortn);
        }
      }
    }
  }



  private void setVectorFromObjectResource(AdminObjectResource or, String typ, List vec)
  {
    or.updateLocalFromTransferable();
    ImageIcon ii = getIcon(typ);

    List sortedEls = getSortedElements(or.getChildren(typ));
    for (ListIterator it = sortedEls.listIterator(); it.hasNext();)
    {
      ObjectResource oRes = (ObjectResource)it.next();
      ObjectResourceTreeNode ortn;
      if(typ.equals(Domain.typ))
        ortn = new ObjectResourceTreeNode(oRes, oRes.getName(), true, ii);
      else
        ortn = new ObjectResourceTreeNode(oRes, oRes.getName(), true, ii,  true);
      vec.add(ortn);
    }
  }

  private List getSortedElements(Map h)
  {
    ObjectResourceSorter sorter = new ObjectResourceNameSorter();//  MonitoredElement.getDefaultSorter();
    sorter.setDataSet(h);
  
    return sorter.default_sort();
  }

  private List getSortedElements(Collection coll)
  {
    ObjectResourceSorter sorter = new ObjectResourceNameSorter();//  MonitoredElement.getDefaultSorter();
    sorter.setDataSet(coll);
  
    return sorter.default_sort();
  }

  private ImageIcon getIcon(String typ){
    if(typ.equals(User.typ)){
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/whami.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(typ.equals(OperatorProfile.typ)){
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/profiles.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(typ.equals(OperatorGroup.typ)){
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/groups.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(typ.equals(Domain.typ)){
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/domains.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
    else if(typ.equals(CommandPermissionAttributes.typ)){
      return new ImageIcon(Toolkit.getDefaultToolkit().
                           getImage("images/command.gif").
                           getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }

    else
    {
      return null;
    }

  }
}