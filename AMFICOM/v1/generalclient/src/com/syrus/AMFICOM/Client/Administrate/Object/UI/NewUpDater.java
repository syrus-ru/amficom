package com.syrus.AMFICOM.Client.Administrate.Object.UI;


import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;

public class NewUpDater{
  private ApplicationContext aContext = new ApplicationContext();
  private DataSourceInterface dsi;


  public NewUpDater(ApplicationContext aContext){
    this.aContext = aContext;
    this.dsi = this.aContext.getDataSourceInterface();
  }


//------------------------------------------------------------------------------
  private void updateObjectResources(ObjectResource or){

    or.updateLocalFromTransferable();
    AdminObjectResource aor = (AdminObjectResource)or;

    String []childTyps = getChildTyps(or);
    String []parentTyps= getParentTyps(or);

    for(int i=0; i<childTyps.length; i++){
      for(int j=0; j<parentTyps.length; j++){
        if(childTyps[i].equals(parentTyps[j])){ //Or can link to child and child can link to Or;
          Vector childIds = aor.getChildIds(childTyps[i]);
          Hashtable h = Pool.getHash(childTyps[i]);
          if(h == null)
            h = new Hashtable();
          for(Enumeration e = h.elements(); e.hasMoreElements();){
            AdminObjectResource child = (AdminObjectResource)e.nextElement();
            Vector parentIds = child.getChildIds(aor.getTyp());
            if(childIds.contains(child.getId()) && !parentIds.contains(aor.getId())){
              child.addChildId(aor.getTyp(), aor.getId());//And, it must be saved;
              saveObjectResource(child);
            }
            else if(!childIds.contains(child.getId()) && parentIds.contains(aor.getId())){
              child.removeChildId(aor.getTyp(), aor.getId());//And, it must be saved;
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
    Pool.put(or.getTyp(), or.getId(), or);


    if(or instanceof OperatorCategory){
      dsi.SaveCategory(or.getId());
    }
    else if(or instanceof OperatorProfile){
      OperatorProfile op = (OperatorProfile)or;
      User user = (User)Pool.get(User.typ, op.user_id);

      user.category_ids = new Vector();
      user.group_ids    = new Vector();
      for(int i=0; i<op.group_ids.size(); i++){
        user.group_ids.add(op.group_ids.get(i));
      }
      for(int i=0; i<op.category_ids.size(); i++){
        user.category_ids.add(op.category_ids.get(i));
      }
      user.setModificationTime(modified);
      user.setTransferableFromLocal();
      user.updateLocalFromTransferable();
      Pool.put(user.getTyp(), user.getId(), user);

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
      OperatorProfile op = (OperatorProfile)Pool.get(OperatorProfile.typ, user.object_id);

      op.group_ids = new Vector();
      op.category_ids = new Vector();

      for(int i=0; i<user.group_ids.size(); i++){
        op.group_ids.add(user.group_ids.get(i));
      }
      for(int i=0; i<user.category_ids.size(); i++){
        op.category_ids.add(user.category_ids.get(i));
      }
      op.setModificationTime(modified);
      op.setTransferableFromLocal();
      op.updateLocalFromTransferable();
      Pool.put(op.getTyp(), op.getId(), op);

      dsi.SaveUser(user.getId());
      dsi.SaveOperatorProfile(op.getId());
    }
    else if(or instanceof ObjectPermissionAttributes){
//      dsi.save
    }



  }


//------------------------------------------------------------------------------
  void updateObjectResources(ObjectResource or, boolean toBeDeleted){
    if(toBeDeleted){
      or.updateLocalFromTransferable();
      AdminObjectResource aor = (AdminObjectResource)or;

      String []parentTyps = getParentTyps(or);

      for(int j=0; j<parentTyps.length; j++)
      {
        Hashtable h = Pool.getHash(parentTyps[j]);
        if(h == null)
          h = new Hashtable();

        for(Enumeration e = h.elements(); e.hasMoreElements();){
          AdminObjectResource parent = (AdminObjectResource)e.nextElement();
          Vector chilsIds = parent.getChildIds(aor.getTyp());
          if(chilsIds.contains(aor.getId())){
            parent.removeChildId(aor.getTyp(), aor.getId());//And, it must be saved;
            parent.setTransferableFromLocal();
            Pool.put(parent.getTyp(), parent.getId(), parent);
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
      Hashtable h = Pool.getHash(typs[i]);
      if(h == null)
        h = new Hashtable();
      for(Enumeration e = h.elements(); e.hasMoreElements();){
        AdminObjectResource or = (AdminObjectResource)e.nextElement();
        if(or.getOwnerId().equals(owner)){
          or.setOwnerId("");
        }
      }
    }
  }


//----------------------------------------------------
  public String []getAllTyps(){
    String []s = {Domain.typ, User.typ, OperatorProfile.typ, CommandPermissionAttributes.typ, OperatorGroup.typ,
                  OperatorCategory.typ, Agent.typ, Client.typ, Server.typ,
                  ObjectPermissionAttributes.typ};
    return s;
  }


//------------------------------------------------------------------------------
  public String []getParentTyps(ObjectResource or){
    Vector v = new Vector();

    if(Domain.getChildTypes_().contains(or.getTyp()))
      v.add(Domain.typ);
    if(CommandPermissionAttributes.getChildTypes_().contains(or.getTyp()))
      v.add(CommandPermissionAttributes.typ);
    if(ObjectPermissionAttributes.getChildTypes_().contains(or.getTyp()))
      v.add(ObjectPermissionAttributes.typ);
    if(OperatorCategory.getChildTypes_().contains(or.getTyp()))
      v.add(OperatorCategory.typ);
    if(OperatorGroup.getChildTypes_().contains(or.getTyp()))
      v.add(OperatorGroup.typ);
    if(User.getChildTypes_().contains(or.getTyp()))
      v.add(User.typ);
    if(Agent.getChildTypes_().contains(or.getTyp()))
      v.add(Agent.typ);
    if(Client.getChildTypes_().contains(or.getTyp()))
      v.add(Client.typ);
    if(Server.getChildTypes_().contains(or.getTyp()))
      v.add(Server.typ);
    if(OperatorProfile.getChildTypes_().contains(or.getTyp()))
      v.add(OperatorProfile.typ);

    return (String [])v.toArray(new String[v.size()]);
  }


//------------------------------------------------------------------------------
  public String []getChildTyps(ObjectResource or){
    Vector v = new Vector();
    for(Enumeration e = or.getChildTypes(); e.hasMoreElements(); ){
      v.add(e.nextElement());
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
      domain.domain_ids.removeAllElements();
    }

    Hashtable h = Pool.getHash(Domain.typ);
    if(h == null)
      h = new Hashtable();

    Enumeration e = h.elements();

    Domain d;
    boolean save;
    for(; e.hasMoreElements(); ){
      d = (Domain)e.nextElement();
      if(!d.getId().equals(domain.getId())){
        save = false;
        if(domain.domain_id.equals(d.getId())){ // d must be father of the domain;
          if(!d.domain_ids.contains(domain.getId())){
            d.domain_ids.add(domain.getId());
            save = true;
          }
          if(d.domain_id.equals(domain.getId())){
            d.domain_id = "";
            save = true;
          }
        }
        if(domain.domain_ids.contains(d.getId())){ // d must be child of the domain;
          if(d.domain_ids.contains(domain.getId())){
            d.domain_ids.remove(domain.getId());
            save = true;
          }
          if(!d.domain_id.equals(domain.getId())){
            d.domain_id = domain.getId();
            save = true;
          }
        }
        if(d.domain_id.equals(domain.getId())){ // d probably is child of domain;
          if(!domain.domain_ids.contains(d.getId())){
            d.domain_id = "";
            save = true;
          }
        }
        if(d.domain_ids.contains(domain.getId())){ // d probably is father of domain;
          if(!domain.domain_id.equals(d.getId())){
            d.domain_ids.remove(domain.getId());
            save = true;
          }
        }
        for(int i=0; i<domain.domain_ids.size(); i++){
          if(d.domain_ids.contains(domain.domain_ids.get(i))){
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




  public static boolean contains(Hashtable h, String s){
    for(Enumeration e = h.elements(); e.hasMoreElements(); ){
      String str = (String)e.nextElement();
      if(str.equals(s))
        return true;
    }
    return false;
  }


  public static boolean contains(Vector v , String s){
    for(Enumeration e = v.elements(); e.hasMoreElements(); ){
      String str = (String)e.nextElement();
      if(str.equals(s))
        return true;
    }
    return false;
  }

  public static void remove(Vector v, String s){
    for(int i=0; i<v.size(); i++){
      String str = (String)v.get(i);
      if(str.equals(s)){
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