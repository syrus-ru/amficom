package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class TwoListsPanel extends GeneralPanel
{
  public String childTyp = "";

  public JLabel name1 = new JLabel();
  public JLabel name2 = new JLabel();

  OrListBox list1 = new OrListBox();
  OrListBox list2 = new OrListBox();

  ObjectResource or;

  JScrollPane scrollPane1 = new JScrollPane();
  JScrollPane scrollPane2 = new JScrollPane();

  JButton buttonAdd = new JButton();
  JButton buttonRemove = new JButton();

  JPanel mainPanel1 = new JPanel();
  JPanel mainPanel2 = new JPanel();

  JPanel panelAdd = new JPanel();
  JPanel panelRemove = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JSplitPane jSplitPane1 = new JSplitPane();

  public TwoListsPanel()
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

  public TwoListsPanel(String firstListName, String secondListName, String childTyp)
  {
    this();
    name1.setText(firstListName);
    name2.setText(secondListName);
    this.childTyp = childTyp;
  }


  public TwoListsPanel(ObjectResource or, String firstListName, String secondListName, String childTyp)
  {
    this();
    name1.setText(firstListName);
    name2.setText(secondListName);

    this.childTyp = childTyp;

    this.setObjectResource(or);
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout3);

    name1.setText("name1");
    name2.setText("name2");

//		Color disabledColor = jLabel1.getBackground();

    scrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
//    scrollPane1.setPreferredSize(new Dimension(194, 360));
    scrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
//    scrollPane2.setPreferredSize(new Dimension(198, 360));
    buttonAdd.setText("Подключить");
    buttonAdd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonAdd_actionPerformed(e);
      }
    });
//    mainPanel1.setPreferredSize(new Dimension(250, 200));

    mainPanel1.setLayout(borderLayout1);

    buttonRemove.setText("Отключить");
    buttonRemove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonRemove_actionPerformed(e);
      }
    });
    mainPanel2.setLayout(borderLayout2);
//    mainPanel2.setPreferredSize(new Dimension(250, 200));
    jSplitPane1.setDividerSize(6);
    mainPanel1.add(name1, BorderLayout.NORTH);
    mainPanel1.add(scrollPane1, BorderLayout.CENTER);
    mainPanel1.add(panelRemove, BorderLayout.SOUTH);
    panelRemove.add(buttonRemove, null);
    jSplitPane1.add(mainPanel2, JSplitPane.RIGHT);
    scrollPane1.getViewport().add(list1, null);



    mainPanel2.add(name2, BorderLayout.NORTH);
    mainPanel2.add(scrollPane2, BorderLayout.CENTER);
    mainPanel2.add(panelAdd, BorderLayout.SOUTH);
    panelAdd.add(buttonAdd, null);
    scrollPane2.getViewport().add(list2, null);
    this.add(jSplitPane1, BorderLayout.CENTER);
    jSplitPane1.add(mainPanel1, JSplitPane.LEFT);


    panelAdd.setPreferredSize(new Dimension(198, 37));
    panelRemove.setPreferredSize(new Dimension(198, 37));

    name1.setPreferredSize(new Dimension(198, 17));
    name2.setPreferredSize(new Dimension(198, 17));

    jSplitPane1.setResizeWeight(.5);
  }

  public ObjectResource getObjectResource()
  {
    return or;
  }

/*
  public boolean setObjectResource(ObjectResource or)
  {
    this.or = or;


    list1.removeAll();
    list1.setContents(or.getChildren(childTyp));

    Hashtable h = new Hashtable();

    Hashtable tmp_h = Pool.getHash(childTyp);
    if(tmp_h == null)
      tmp_h = new Hashtable();
    for(Enumeration e = tmp_h.elements(); e.hasMoreElements();)
    {
      ObjectResource o = (ObjectResource)e.nextElement();
      h.put(o.getId(), o);
    }

    for(Enumeration e = or.getChildren(childTyp);
        e.hasMoreElements();)
    {
      h.remove(((ObjectResource)e.nextElement()).getId());
    }
    list2.removeAll();
    list2.setContents(h);
    return true;
  }*/



  public boolean setObjectResource(ObjectResource or)
  {
    this.or = or;

    {
      list1.removeAll();
      DataSet dSet = new DataSet(or.getChildren(childTyp));
      ObjectResourceSorter sorter = new ObjectResourceNameSorter();//  MonitoredElement.getDefaultSorter();
      sorter.setDataSet(dSet);
      dSet = sorter.default_sort();
      list1.setContents(dSet.elements());
    }



    Hashtable h = new Hashtable();

    Hashtable tmp_h = Pool.getHash(childTyp);
    if(tmp_h == null)
      tmp_h = new Hashtable();
    for(Enumeration e = tmp_h.elements(); e.hasMoreElements();)
    {
      ObjectResource o = (ObjectResource)e.nextElement();
      h.put(o.getId(), o);
    }

    for(Enumeration e = or.getChildren(childTyp);
        e.hasMoreElements();)
    {
      h.remove(((ObjectResource)e.nextElement()).getId());
    }
    list2.removeAll();

    {
      DataSet dSet = new DataSet(h);
      ObjectResourceSorter sorter = new ObjectResourceNameSorter();//MonitoredElement.getDefaultSorter();
      sorter.setDataSet(dSet);
      dSet = sorter.default_sort();
      list2.setContents(dSet.elements());
    }
    return true;
  }


/*
  public boolean modify()
  {
//    cpa.group_ids = this.cpaGroups.getVectorIDfromList();
    return true;
  }

  void buttonAdd_actionPerformed(ActionEvent e)
  {
    ObjectResource o = (ObjectResource)
                      (this.list2.getSelectedObjectResource());
    if(o == null)
      return;
    this.list1.add(o);
    this.list2.remove(o);
  }

  void buttonRemove_actionPerformed(ActionEvent e)
  {
    ObjectResource o = (ObjectResource)
                      (this.list1.getSelectedObjectResource());
    if(o == null)
      return;
    this.list2.add(o);
    this.list1.remove(o);
  }
  */



  public boolean modify(Vector modifiedVector)
  {
    modifiedVector.removeAllElements();
    Vector v  = this.list1.getVectorIDfromList();

    for(int i=0; i<v.size(); i++)
    {
      modifiedVector.add(v.get(i));
    }
    return true;
  }

  void buttonAdd_actionPerformed(ActionEvent e)
  {
    list1.getSelectedObjectResource();
    Object []o = list2.getSelectedValues();
    for(int i=0; i<o.length; i++)
    {
      list1.add((ObjectResource)o[i]);
      list2.remove((ObjectResource)o[i]);
    }
  }

  void buttonRemove_actionPerformed(ActionEvent e)
  {
    Object []o = list1.getSelectedValues();
    for(int i=0; i<o.length; i++)
    {
      list2.add((ObjectResource)o[i]);
      list1.remove((ObjectResource)o[i]);
    }
  }

}



/*
package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;






public class TwoListsPanel extends GeneralPanel
{
  public String childTyp = "";

  public JLabel name1 = new JLabel();
  public JLabel name2 = new JLabel();

  OrListBox list1 = new OrListBox();
  OrListBox list2 = new OrListBox();

  ObjectResource or;

  JScrollPane scrollPane1 = new JScrollPane();
  JScrollPane scrollPane2 = new JScrollPane();

  JButton buttonAdd = new JButton();
  JButton buttonRemove = new JButton();

  JPanel mainPanel1 = new JPanel();
  JPanel mainPanel2 = new JPanel();

  JPanel panelAdd = new JPanel();
  JPanel panelRemove = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();

  public TwoListsPanel()
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

  public TwoListsPanel(String firstListName, String secondListName, String childTyp)
  {
    this();
    name1.setText(firstListName);
    name2.setText(secondListName);
    this.childTyp = childTyp;
  }


  public TwoListsPanel(ObjectResource or, String firstListName, String secondListName, String childTyp)
  {
    this();
    name1.setText(firstListName);
    name2.setText(secondListName);

    this.childTyp = childTyp;

    this.setObjectResource(or);
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout3);

    name1.setText("name1");
    name2.setText("name2");

//		Color disabledColor = jLabel1.getBackground();

    scrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
//    scrollPane1.setPreferredSize(new Dimension(194, 360));
    scrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
//    scrollPane2.setPreferredSize(new Dimension(198, 360));
    buttonAdd.setText("Добавить");
    buttonAdd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonAdd_actionPerformed(e);
      }
    });
//    mainPanel1.setPreferredSize(new Dimension(250, 200));

    mainPanel1.setLayout(borderLayout1);

    buttonRemove.setText("Удалить");
    buttonRemove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonRemove_actionPerformed(e);
      }
    });
    mainPanel2.setLayout(borderLayout2);
//    mainPanel2.setPreferredSize(new Dimension(250, 200));
    this.add(mainPanel1, BorderLayout.WEST);
    mainPanel1.add(name1, BorderLayout.NORTH);
    mainPanel1.add(scrollPane1, BorderLayout.CENTER);
    mainPanel1.add(panelRemove, BorderLayout.SOUTH);
    panelRemove.add(buttonRemove, null);

    JPanel p = new JPanel();
    p.setPreferredSize(new Dimension(50, 300));
    p.setMinimumSize(new Dimension(50, 300));
    this.add(p, BorderLayout.CENTER);


    this.add(mainPanel2, BorderLayout.EAST);
    mainPanel2.add(name2, BorderLayout.NORTH);
    mainPanel2.add(scrollPane2, BorderLayout.CENTER);
    mainPanel2.add(panelAdd, BorderLayout.SOUTH);
    panelAdd.add(buttonAdd, null);
    scrollPane2.getViewport().add(list2, null);
    scrollPane1.getViewport().add(list1, null);


    panelAdd.setPreferredSize(new Dimension(198, 37));
    panelRemove.setPreferredSize(new Dimension(198, 37));

    name1.setPreferredSize(new Dimension(198, 17));
    name2.setPreferredSize(new Dimension(198, 17));

  }

  public ObjectResource getObjectResource()
  {
    return or;
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.or = or;

//    System.out.println("set prop pane to " + cpa.name);

    list1.removeAll();
    list1.setContents(or.getChildren(childTyp));

    Hashtable h = new Hashtable();

    Hashtable tmp_h = Pool.getHash(childTyp);
    if(tmp_h == null)
      tmp_h = new Hashtable();
    for(Enumeration e = tmp_h.elements(); e.hasMoreElements();)
    {
      ObjectResource o = (ObjectResource)e.nextElement();
      h.put(o.getId(), o);
    }

    for(Enumeration e = or.getChildren(childTyp);
        e.hasMoreElements();)
    {
      h.remove(((ObjectResource)e.nextElement()).getId());
    }
    list2.removeAll();
    list2.setContents(h);
    return true;
  }
/*
  public boolean modify()
  {
//    cpa.group_ids = this.cpaGroups.getVectorIDfromList();
    return true;
  }

  void buttonAdd_actionPerformed(ActionEvent e)
  {
    ObjectResource o = (ObjectResource)
                      (this.list2.getSelectedObjectResource());
    if(o == null)
      return;
    this.list1.add(o);
    this.list2.remove(o);
  }

  void buttonRemove_actionPerformed(ActionEvent e)
  {
    ObjectResource o = (ObjectResource)
                      (this.list1.getSelectedObjectResource());
    if(o == null)
      return;
    this.list2.add(o);
    this.list1.remove(o);
  }
  */


/*
  public boolean modify(Vector modifiedVector)
  {
    modifiedVector.removeAllElements();
    Vector v  = this.list1.getVectorIDfromList();

    for(int i=0; i<v.size(); i++)
    {
      modifiedVector.add(v.get(i));
    }
    return true;
  }

  void buttonAdd_actionPerformed(ActionEvent e)
  {
    list1.getSelectedObjectResource();
    Object []o = list2.getSelectedValues();
    for(int i=0; i<o.length; i++)
    {
      list1.add((ObjectResource)o[i]);
      list2.remove((ObjectResource)o[i]);
    }
  }

  void buttonRemove_actionPerformed(ActionEvent e)
  {
    Object []o = list1.getSelectedValues();
    for(int i=0; i<o.length; i++)
    {
      list2.add((ObjectResource)o[i]);
      list1.remove((ObjectResource)o[i]);
    }
  }

}
*/
