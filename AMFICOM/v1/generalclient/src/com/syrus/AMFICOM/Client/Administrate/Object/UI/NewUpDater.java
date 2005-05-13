/*
 * $Id: NewUpDater.java,v 1.7 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

import java.util.*;
import javax.swing.JOptionPane;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
public class NewUpDater {
	private ApplicationContext aContext = new ApplicationContext();

	private DataSourceInterface dsi;

  public NewUpDater(ApplicationContext aContext){
    this.aContext = aContext;
    this.dsi = this.aContext.getDataSource();
  }


//------------------------------------------------------------------------------
  private void updateObjectResources(StorableObject or){

    or.updateLocalFromTransferable();
    AdminObjectResource aor = (AdminObjectResource)or;

    String []childTyps = getChildTyps(aor);
    String []parentTyps= getParentTyps(or);

    for(int i=0; i<childTyps.length; i++){
      for(int j=0; j<parentTyps.length; j++){
        if(childTyps[i].equals(parentTyps[j])){ //Or can link to child and child can link to Or;
          List childIds = aor.getChildIds(childTyps[i]);
          Map h = Pool.getMap(childTyps[i]);
          if(h == null)
            h = new HashMap();
            
          for(Iterator it = h.values().iterator(); it.hasNext();)
          {
            AdminObjectResource child = (AdminObjectResource)it.next();
            List parentIds = child.getChildIds(aor.getClass().getName());
            if(childIds.contains(child.getId()) && !parentIds.contains(aor.getId()))
            {
              child.addChildId(aor.getClass().getName(), aor.getId());//And, it must be saved;
              saveObjectResource(child);
            }
            else if(!childIds.contains(child.getId()) && parentIds.contains(aor.getId()))
            {
              child.removeChildId(aor.getClass().getName(), aor.getId());//And, it must be saved;
              saveObjectResource(child);
            }
          }
        }
      }
    }
  }

  public void saveObjectResource(AdminObjectResource or){
    Date date = new Date();
    long modified = date.getTime();

    or.setModificationTime(modified);
    or.setTransferableFromLocal();
    or.updateLocalFromTransferable();
    Pool.put(or.getClass().getName(), or.getId(), or);


    if(or instanceof OperatorCategory){
      dsi.SaveCategory(or.getId());
    }
    else if(or instanceof OperatorProfile){
      OperatorProfile op = (OperatorProfile)or;
      User user = (User)Pool.get(User.class.getName(), op.user_id);

      user.category_ids = new ArrayList();
      user.group_ids    = new ArrayList();
      for(int i=0; i<op.group_ids.size(); i++){
        user.group_ids.add(op.group_ids.get(i));
      }
      for(int i=0; i<op.category_ids.size(); i++){
        user.category_ids.add(op.category_ids.get(i));
      }
      user.setModificationTime(modified);
      user.setTransferableFromLocal();
      user.updateLocalFromTransferable();
      Pool.put(user.getClass().getName(), user.getId(), user);

      dsi.SaveUser(user.getId());
      dsi.SaveOperatorProfile(op.getId());
    }
    else if(or instanceof OperatorGroup){
      dsi.SaveGroup(or.getId());
    }
    else if(or instanceof CommandPermissionAttributes){
      dsi.SaveExec(or.getId());
    }
    else if(or instanceof Agent){
      dsi.SaveAgent(or.getId());
    }
    else if(or instanceof Client){
      dsi.SaveClient(or.getId());
    }
    else if(or instanceof Server){
      dsi.SaveServer(or.getId());
    }
    else if(or instanceof Domain){
      dsi.SaveDomain(or.getId());
    }
    else if(or instanceof User){
      User user = (User)or;
      OperatorProfile op = (OperatorProfile)Pool.get(OperatorProfile.class.getName(), user.object_id);

      op.group_ids = new ArrayList();
      op.category_ids = new ArrayList();

      for(int i=0; i<user.group_ids.size(); i++){
        op.group_ids.add(user.group_ids.get(i));
      }
      for(int i=0; i<user.category_ids.size(); i++){
        op.category_ids.add(user.category_ids.get(i));
      }
      op.setModificationTime(modified);
      op.setTransferableFromLocal();
      op.updateLocalFromTransferable();
      Pool.put(op.getClass().getName(), op.getId(), op);

      dsi.SaveUser(user.getId());
      dsi.SaveOperatorProfile(op.getId());
    }
    else if(or instanceof ObjectPermissionAttributes){
//      dsi.save
    }



  }


//------------------------------------------------------------------------------
  void updateObjectResources(StorableObject or, boolean toBeDeleted){
    if(toBeDeleted){
      or.updateLocalFromTransferable();
      AdminObjectResource aor = (AdminObjectResource)or;

      String []parentTyps = getParentTyps(or);

      for(int j=0; j<parentTyps.length; j++)
      {
        Map h = Pool.getMap(parentTyps[j]);
        if(h == null)
          h = new HashMap();

        for(Iterator it = h.values().iterator(); it.hasNext();)
        {
          AdminObjectResource parent = (AdminObjectResource)it.next();
          List chilsIds = parent.getChildIds(aor.getClass().getName());
          if(chilsIds.contains(aor.getId())){
            parent.removeChildId(aor.getClass().getName(), aor.getId());//And, it must be saved;
            parent.setTransferableFromLocal();
            Pool.put(parent.getClass().getName(), parent.getId(), parent);
            saveObjectResource(parent);
          }
        }
      }

      String owner = null;
      if(or instanceof User || or instanceof OperatorProfile)
      {
        if(or instanceof User)
          owner = ((User)or).getId();
        else if(or instanceof OperatorProfile)
          owner = ((OperatorProfile)or).user_id;
        if(owner != null)
          removeDeletedOwnerIds(owner);
      }
    }
    else
    {
      updateObjectResources(or);
    }
  }

//----------------------------------------------------
  private void removeDeletedOwnerIds(String owner){
    String []typs = getAllTyps();

    for(int i=0; i<typs.length; i++)
    {
      Map h = Pool.getMap(typs[i]);
      if(h == null)
        h = new HashMap();
      for(Iterator it = h.values().iterator(); it.hasNext();)
      {
        AdminObjectResource or = (AdminObjectResource)it.next();
        if(or.getOwnerId().equals(owner)){
          or.setOwnerId("");
        }
      }
    }
  }


//----------------------------------------------------
  public String []getAllTyps(){
    String []s = {Domain.class.getName(), User.class.getName(), OperatorProfile.class.getName(), CommandPermissionAttributes.class.getName(), OperatorGroup.class.getName(),
                  OperatorCategory.class.getName(), Agent.class.getName(), Client.class.getName(), Server.class.getName(),
                  ObjectPermissionAttributes.class.getName()};
    return s;
  }


//------------------------------------------------------------------------------
  public String []getParentTyps(StorableObject or){
    List v = new ArrayList();

    if(Domain.getChildTypes_().contains(or.getClass().getName()))
      v.add(Domain.class.getName());
    if(CommandPermissionAttributes.getChildTypes_().contains(or.getClass().getName()))
      v.add(CommandPermissionAttributes.class.getName());
    if(ObjectPermissionAttributes.getChildTypes_().contains(or.getClass().getName()))
      v.add(ObjectPermissionAttributes.class.getName());
    if(OperatorCategory.getChildTypes_().contains(or.getClass().getName()))
      v.add(OperatorCategory.class.getName());
    if(OperatorGroup.getChildTypes_().contains(or.getClass().getName()))
      v.add(OperatorGroup.class.getName());
    if(User.getChildTypes_().contains(or.getClass().getName()))
      v.add(User.class.getName());
    if(Agent.getChildTypes_().contains(or.getClass().getName()))
      v.add(Agent.class.getName());
    if(Client.getChildTypes_().contains(or.getClass().getName()))
      v.add(Client.class.getName());
    if(Server.getChildTypes_().contains(or.getClass().getName()))
      v.add(Server.class.getName());
    if(OperatorProfile.getChildTypes_().contains(or.getClass().getName()))
      v.add(OperatorProfile.class.getName());

    return (String [])v.toArray(new String[v.size()]);
  }


//------------------------------------------------------------------------------
  public String []getChildTyps(AdminObjectResource or){
    List v = new ArrayList();
    for(Iterator it = or.getChildTypes().iterator(); it.hasNext(); )
    {
      v.add(it.next());
    }
    String []childTyps = (String [])v.toArray(new String[v.size()]);

    return childTyps;
  }



  public void updateDomain(Domain domain, boolean toBeDeleted){
    // checking of the domain itself;
    if(domain.domain_ids.contains(domain.getId())){
      domain.domain_ids.remove(domain.getId());
    }
    if(domain.domain_ids.contains(domain.domain_id)){
      domain.domain_ids.remove(domain.domain_id);
    }
    if(domain.domain_id.equals(domain.getId())){
      domain.domain_id = "";
    }// end of checking;

    if(toBeDeleted){
      domain.domain_id = "";
      domain.domain_ids.clear();
    }

    Map h = Pool.getMap(Domain.class.getName());
    if(h == null)
      h = new HashMap();

    Domain d;
    boolean save;
    for(Iterator it = h.values().iterator(); it.hasNext(); )
    {
      d = (Domain)it.next();
      if(!d.getId().equals(domain.getId()))
      {
        save = false;
        if(domain.domain_id.equals(d.getId()))
        { // d must be father of the domain;
          if(!d.domain_ids.contains(domain.getId()))
          {
            d.domain_ids.add(domain.getId());
            save = true;
          }
          if(d.domain_id.equals(domain.getId()))
          {
            d.domain_id = "";
            save = true;
          }
        }
        if(domain.domain_ids.contains(d.getId()))
        { // d must be child of the domain;
          if(d.domain_ids.contains(domain.getId()))
          {
            d.domain_ids.remove(domain.getId());
            save = true;
          }
          if(!d.domain_id.equals(domain.getId()))
          {
            d.domain_id = domain.getId();
            save = true;
          }
        }
        if(d.domain_id.equals(domain.getId())){ // d probably is child of domain;
          if(!domain.domain_ids.contains(d.getId()))
          {
            d.domain_id = "";
            save = true;
          }
        }
        if(d.domain_ids.contains(domain.getId()))
        { // d probably is father of domain;
          if(!domain.domain_id.equals(d.getId()))
          {
            d.domain_ids.remove(domain.getId());
            save = true;
          }
        }
        for(int i=0; i<domain.domain_ids.size(); i++)
        {
          if(d.domain_ids.contains(domain.domain_ids.get(i)))
          {
            d.domain_ids.remove(domain.domain_ids.get(i));
            save = true;
          }
        }

        if(save){
          saveObjectResource(d);
        }
      }
    }
  }

  public static boolean contains(Map h, String s)
  {
    for(Iterator it = h.values().iterator(); it.hasNext(); )
    {
      String str = (String) it.next();
      if(str.equals(s))
        return true;
    }
    return false;
  }


  public static boolean contains(List v , String s)
  {
    for(Iterator it = v.listIterator(); it.hasNext(); )
    {
      String str = (String)it.next();
      if(str.equals(s))
        return true;
    }
    return false;
  }

  public static void remove(List v, String s)
  {
    for(Iterator it = v.listIterator(); it.hasNext(); )
    {
      String str = (String)it.next();
      if(str.equals(s))
      {
        v.remove(str);
        return;
      }
    }
    return;
  }


  public static boolean checkName(AdminObjectResource aor)
  {
    String name = aor.getName();
    if(name == null)
    {
      String error = "Не задано имя объекта. Объект не сохранен.";
      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
      return false;
    }

    for(int i=0; i<name.length(); i++)
    {
      if(name.charAt(i) != ' ')
      {
        return true;
      }
    }

    String error = "Не задано имя объекта. Объект не сохранен.";
    JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
    return false;
  }

}
