/*
 * $Id: DomainPaneAdmin.java,v 1.7 2004/09/27 13:14:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 13:14:56 $
 * @module generalclient_v1
 */
public class DomainPaneAdmin extends JPanel implements ObjectResourcePropertiesPane
{
  public ApplicationContext aContext = new ApplicationContext();
  NewUpDater updater;

  User user;
  Domain domain;
  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane jtp = new JTabbedPane();
  DomainDomainPanel ddp = new DomainDomainPanel();
  DomainGeneralPanel dgp = new DomainGeneralPanel();
//  DomainPermissionAttributesPanel dpap = new DomainPermissionAttributesPanel();
  ObjectPermissionAttributesPanel opap = new ObjectPermissionAttributesPanel();

  public DomainPaneAdmin()
  {
    super();
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public DomainPaneAdmin(Domain domain)
  {
    this();
    setObjectResource(domain);
  }

  private void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(500, 500));
    this.setLayout(borderLayout1);
    this.add(jtp, BorderLayout.CENTER);

    opap.enableExecutingEditing(false);

    jtp.add(dgp.getName(), dgp);
    jtp.add(ddp.getName(), ddp);
    jtp.add(opap.getName(), opap);

  }

  public ObjectResource getObjectResource()
  {
    return domain;
  }

  public void setObjectResource(ObjectResource or)
  {
    ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
                                   Pool.get("ObjectFrame", "AdministrateObjectFrame");
    if(f!=null)
    {
      f.setTitle("Домены");
    }

    if(!Checker.checkCommand(user, Checker.readDomainInfo))
    {
      this.showTheWindow(false);
      setData(or);
      return;
    }
    this.showTheWindow(true);
    setData(or);
  }

  private void setData(ObjectResource or)
  {
    this.domain = (Domain)or;
    if(domain == null)
      return;

    if(domain.opa.id.equals("") || domain.opa.id == null)
    {
      domain.opa.id = aContext.getDataSource().GetUId(ObjectPermissionAttributes.typ);
    }
    domain.updateLocalFromTransferable();
    ddp.setObjectResource(or);
    dgp.setObjectResource(or);
    opap.setPermissionObjectResource(domain);
    opap.setObjectResource(domain.opa);
  }

  public boolean modify()
  {
    if(!Checker.checkCommand(user, Checker.modifyDomain))
    {
      setData(domain);
      return false;
    }
    this.showTheWindow(true);

    Date d = new Date();
    ddp.modify();
    dgp.modify();
    opap.modify();

//    this.domain.modified = d.getTime();
//    this.domain.modified_by = user.id;
//    this.domain.opa.modified = domain.modified;
//    this.domain.opa.modified_by = domain.modified_by;
//    this.domain.opa.created = domain.created;
//    this.domain.opa.created_by = domain.created_by;
//    this.domain.opa.owner_id = domain.owner_id;

    if(!NewUpDater.checkName(domain))
      return false;

    updater.updateDomain(domain, false);
    Pool.put(Domain.typ, domain.id, domain);

    this.aContext.getDataSource().SaveDomain(domain.id);

    setData(domain);

    return true;
  }

  public boolean save()
  {
    return modify();
  }

  public boolean create()
  {
//    if(true)
//    {
//      String error = "Домен не может быть создан в модуле администрирования.";
//      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
//    }
    if(user == null)
      return false;

    if(!Checker.checkCommand(user, Checker.addDomain))
    {
      this.showTheWindow(false);
      return false;
    }
    this.showTheWindow(true);
    DataSourceInterface dataSource = aContext.getDataSource();
    domain = new Domain();

    domain.id = dataSource.GetUId(Domain.typ);
    domain.opa.id = dataSource.GetUId(ObjectPermissionAttributes.typ);

    domain.name = domain.id;
    domain.codename = domain.id;
    Date d = new Date();

    domain.created = d.getTime();
    domain.modified = d.getTime();

    domain.created_by = user.id;
    domain.modified_by = user.id;
    domain.owner_id = user.id;

    domain.opa.created = domain.created;
    domain.opa.modified = domain.modified;
    domain.opa.owner_id = domain.owner_id;
    domain.opa.created_by = domain.created_by;
    domain.opa.modified_by = domain.modified_by;
    domain.opa.name = "Атрибуты домена " + domain.name;
    domain.opa.codename = Domain.typ + "" + domain.id;

    Pool.put(Domain.typ, domain.id, domain);

    setData(domain);
    this.aContext.getDataSource().SaveDomain(domain.id);

    return true;
  }

  public boolean delete()
  {
//    if(true)
//    {
//      String error = "Домен не может быть удален в модуле администрирования.";
//      JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
//    }
    if(Checker.checkCommand(user, Checker.removeDomain))
    {
      domain.domain_id = "";
      domain.domain_ids = new ArrayList();
      updater.updateDomain(domain, true);
      String[] s = new String[1];
      s[0] = domain.id;
      Pool.put(Domain.typ, domain.id, domain);
      this.aContext.getDataSource().RemoveDomain(s);
      Pool.remove(domain);
      return true;
    }
    return false;
  }

  public boolean open()
  {
    return true;
  }

  public boolean cancel()
  {
    this.setData(this.domain);
    return true;
  }


  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.user = (User)Pool.get(User.typ,
                               aContext.getSessionInterface().getUserId());
    opap.setContext(aContext);
//    this.dispatcher = this.aContext.getDispatcher();

//    this.dispatcher.register(this, Domain.typ+"updated");

    this.updater = new NewUpDater(this.aContext);
  }



  void showTheWindow(boolean key)
  {
    this.jtp.setVisible(key);
    repaint();
  }
}
