package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.awt.event.*;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;

import java.util.Map;
import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;


public class DomainDomainPanel extends GeneralPanel
{

  JLabel jLabel1 = new JLabel();

  OrListBox internalDomainsList = new OrListBox();
  OrListBox otherDomainsList = new OrListBox();

  Domain domain;
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();
//  JList profileGroupsTotalList = new JList();
  JLabel jLabel2 = new JLabel();
  JButton jButtonAdd = new JButton();
  JButton jButtonRemove = new JButton();
  OrComboBox fatherDomain = new OrComboBox();
  JLabel jLabel3 = new JLabel();
  private JPanel mainPanel1 = new JPanel();
  private JPanel jPanel1 = new JPanel();
  private JPanel mainPanel2 = new JPanel();
  private JPanel jPanel3 = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JSplitPane splitPane = new JSplitPane();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JPanel jPanel2 = new JPanel();
  private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  private BorderLayout borderLayout3 = new BorderLayout();

  public DomainDomainPanel()
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

  public DomainDomainPanel(OperatorProfile profile)
  {
    this();
    setObjectResource(profile);
  }

  private void jbInit() throws Exception
  {
    setName("Домены");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

    this.setLayout(borderLayout1);

    jLabel1.setText("Подключенные домены");

//		Color disabledColor = jLabel1.getBackground();

    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(250, 300));
    jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane2.setPreferredSize(new Dimension(259, 350));
    jLabel2.setText("Прочие домены");
    jButtonAdd.setText("Подключить");
    jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAdd_actionPerformed(e);
      }
    });
    jButtonRemove.setText("Отключить ");
    jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonRemove_actionPerformed(e);
      }
    });
    jLabel3.setText("В составе домена");
    fatherDomain.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fatherDomain_actionPerformed(e);
      }
    });
    mainPanel1.setLayout(borderLayout3);

    jPanel1.setMinimumSize(new Dimension(93, 31));
    jPanel1.setPreferredSize(new Dimension(208, 31));
    mainPanel2.setLayout(borderLayout2);
    jPanel3.setMinimumSize(new Dimension(99, 31));
    jPanel3.setPreferredSize(new Dimension(99, 31));
    mainPanel1.setPreferredSize(new Dimension(260, 186));
    mainPanel2.setPreferredSize(new Dimension(260, 386));
    jPanel2.setLayout(verticalFlowLayout2);
    splitPane.setDividerSize(3);
    mainPanel1.add(jPanel2, BorderLayout.NORTH);
    mainPanel1.add(jScrollPane1, BorderLayout.CENTER);
    mainPanel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jButtonRemove, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(fatherDomain, null);
    jPanel2.add(jLabel1, null);
    splitPane.add(mainPanel2, JSplitPane.RIGHT);
    jScrollPane1.getViewport().add(internalDomainsList, null);
    mainPanel2.add(jLabel2, BorderLayout.NORTH);
    mainPanel2.add(jScrollPane2, BorderLayout.CENTER);
    mainPanel2.add(jPanel3, BorderLayout.SOUTH);
    jPanel3.add(jButtonAdd, null);
    jScrollPane2.getViewport().add(otherDomainsList, null);
    this.add(splitPane, BorderLayout.CENTER);
    splitPane.add(mainPanel1, JSplitPane.LEFT);

    splitPane.setResizeWeight(.5);
  }

  public ObjectResource getObjectResource()
  {
    return domain;
  }

//--------------------------------------------------------
/*  public boolean setObjectResource(ObjectResource or)
  {

    this.domain = (Domain)or;
    if(domain == null)
      return false;

    this.fatherDomain.removeAllItems();
    this.internalDomainsList.removeAll();
    this.otherDomainsList.removeAll();

    this.fatherDomain.setTyp(Domain.typ);
    this.fatherDomain.remove(this.domain);
    this.fatherDomain.setSelectedTyp(Domain.typ, domain.domain_id);

    this.internalDomainsList.
        setContents(this.domain.getChildren(Domain.typ));

    this.otherDomainsList.setContents(getOtherDomains());
    return true;
  }*/


  public void setObjectResource(ObjectResource or)
  {

    this.domain = (Domain)or;
    if(domain == null)
      return;

    this.fatherDomain.removeAllItems();
    this.internalDomainsList.removeAll();
    this.otherDomainsList.removeAll();

    this.fatherDomain.setTyp(Domain.typ);
    this.fatherDomain.remove(this.domain);
    this.fatherDomain.setSelectedTyp(Domain.typ, domain.domain_id);
    ObjectResourceSorter sorter = new ObjectResourceNameSorter();//MonitoredElement.getDefaultSorter();
    sorter.setDataSet(this.domain.getChildren(Domain.typ));
    internalDomainsList.setContents(sorter.default_sort());

    sorter = new ObjectResourceNameSorter();//MonitoredElement.getDefaultSorter();
    sorter.setDataSet(getOtherDomains());
    otherDomainsList.setContents(sorter.default_sort());
  }

//--------------------------------------------------------
  private Collection getOtherDomains()
  {
    List exeptOtherIds = this.internalDomainsList.getVectorIDfromList();
    exeptOtherIds.add(this.domain.id);
    exeptOtherIds.add(this.fatherDomain.getSelectedId());

    Map other = new HashMap();
    Map tmp_h = Pool.getMap(Domain.typ);

    if(tmp_h == null)
      tmp_h = new HashMap();

    for(Iterator it = tmp_h.values().iterator(); it.hasNext();)
    {
      ObjectResource o = (ObjectResource)it.next();
      if(!exeptOtherIds.contains(o.getId()))
        other.put(o.getId(), o);
    }
    return other.values();
  }

//--------------------------------------------------------
  public boolean modify()
  {
    this.domain.domain_id = this.fatherDomain.getSelectedId();
    this.domain.domain_ids =
        internalDomainsList.getVectorIDfromList();
    return true;
  }

//--------------------------------------------------------
  void jButtonAdd_actionPerformed(ActionEvent e)
  {
    Domain o = (Domain)
               (this.otherDomainsList.getSelectedObjectResource());
    if(o == null)
      return;
//    System.out.println("Adding of the "+o.getName());
    this.internalDomainsList.add(o);
    this.otherDomainsList.remove(o);
  }

//--------------------------------------------------------
  void jButtonRemove_actionPerformed(ActionEvent e)
  {
    Domain o = (Domain)
               (this.internalDomainsList.getSelectedObjectResource());
    if(o == null)
      return;
//    System.out.println("Removing of the "+o.getName());
    this.otherDomainsList.add(o);
    this.internalDomainsList.remove(o);
  }

//--------------------------------------------------------
  public void distributeData(String fatherID)
  {
    if(fatherID == null)
      fatherID = "";
    Domain fatherDomain = (Domain)Pool.get(Domain.typ, fatherID);

    List internalIds = this.internalDomainsList.getVectorIDfromList();

    if(internalIds.contains(fatherID))
    {
      this.internalDomainsList.remove(fatherDomain);
    }

    this.otherDomainsList.removeAll();
    this.otherDomainsList.setContents(getOtherDomains());
  }

//--------------------------------------------------------
  void fatherDomain_actionPerformed(ActionEvent e)
  {
    String selId = fatherDomain.getSelectedId();
//    System.out.println("Selected id is "+selId);
    this.distributeData(fatherDomain.getSelectedId());
  }

}
