package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;
import javax.swing.border.*;

public class RWXPanel extends JPanel
{
  private JPanel jPanelOwner = new JPanel();
  private JCheckBox OwnerR = new JCheckBox();
  private JCheckBox OwnerX = new JCheckBox();
  private JCheckBox OwnerW = new JCheckBox();
  private XYLayout xYLayout2 = new XYLayout();
  private JCheckBox GroupX = new JCheckBox();
  private JCheckBox GroupW = new JCheckBox();
  private JCheckBox GroupR = new JCheckBox();
  private JPanel jPanelGroup = new JPanel();
  private XYLayout xYLayout3 = new XYLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JCheckBox OtherR = new JCheckBox();
  private JPanel jPanelOther = new JPanel();
  private XYLayout xYLayout4 = new XYLayout();
  private JCheckBox OtherX = new JCheckBox();
  private JCheckBox OtherW = new JCheckBox();
  private Border border1;
  private TitledBorder titledBorder1;
  private Border border2;
  private TitledBorder titledBorder2;
  private Border border3;
  private TitledBorder titledBorder3;

  public RWXPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public RWXPanel(boolean editR, boolean editW, boolean editX)
  {
    this();
    setEnabledReadingEditing(editR);
    setEnabledWritingEditing(editW);
    setEnabledExecutingEditing(editX);
  }

  void jbInit() throws Exception
  {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Владелец");
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Группа");
    border3 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(border3,"Прочие");
    jPanelOwner.setLayout(xYLayout2);
    jPanelOwner.setBorder(titledBorder1);
    jPanelOwner.setPreferredSize(new Dimension(120, 110));
    this.setLayout(flowLayout1);
    OwnerR.setText("Чтение");
    OwnerR.setFocusPainted(false);
    OwnerX.setText("Исполнение");
    OwnerX.setFocusPainted(false);
    OwnerW.setText("Запись");
    OwnerW.setFocusPainted(false);
    GroupX.setText("Исполнение");
    GroupX.setFocusPainted(false);
    GroupW.setText("Запись");
    GroupW.setFocusPainted(false);
    GroupR.setText("Чтение");
    GroupR.setFocusPainted(false);
    jPanelGroup.setLayout(xYLayout3);
    jPanelGroup.setBorder(titledBorder2);
    jPanelGroup.setPreferredSize(new Dimension(120, 110));
    OtherR.setText("Чтение");
    OtherR.setFocusPainted(false);
    jPanelOther.setLayout(xYLayout4);
    jPanelOther.setBorder(titledBorder3);
    jPanelOther.setPreferredSize(new Dimension(120, 110));
    OtherX.setText("Исполнение");
    OtherX.setFocusPainted(false);
    OtherW.setText("Запись");
    OtherW.setFocusPainted(false);
    this.setPreferredSize(new Dimension(400, 130));
    jPanelOwner.add(OwnerR,   new XYConstraints(1, 5, -1, -1));
    jPanelOwner.add(OwnerW,    new XYConstraints(1, 30, -1, -1));
    jPanelOwner.add(OwnerX,  new XYConstraints(1, 55, -1, -1));
    this.add(jPanelOwner, null);
    jPanelGroup.add(GroupR,  new XYConstraints(1, 5, -1, -1));
    jPanelGroup.add(GroupW,  new XYConstraints(1, 30, -1, -1));
    jPanelGroup.add(GroupX,  new XYConstraints(1, 55, -1, -1));
    this.add(jPanelGroup, null);
    jPanelOther.add(OtherR,  new XYConstraints(1, 5, -1, -1));
    jPanelOther.add(OtherW,  new XYConstraints(1, 30, -1, -1));
    jPanelOther.add(OtherX,  new XYConstraints(1, 55, -1, -1));
    this.add(jPanelOther, null);
  }


  public boolean getOwnerR()
  {
    if(OwnerR.isSelected())
      return true;
    return false;
  }

  public boolean getOwnerW()
  {
    if(OwnerW.isSelected())
      return true;
    return false;
  }

  public boolean getOwnerX()
  {
    if(OwnerX.isSelected())
      return true;
    return false;
  }

  public boolean getGroupR()
  {
    if(GroupR.isSelected())
      return true;
    return false;
  }

  public boolean getGroupW()
  {
    if(GroupW.isSelected())
      return true;
    return false;
  }

  public boolean getGroupX()
  {
    if(GroupX.isSelected())
      return true;
    return false;
  }

  public boolean getOtherR()
  {
    if(OtherR.isSelected())
      return true;
    return false;
  }

  public boolean getOtherW()
  {
    if(OtherW.isSelected())
      return true;
    return false;
  }

  public boolean getOtherX()
  {
    if(OtherX.isSelected())
      return true;
    return false;
  }




  public void setOwnerR(boolean key)
  {
    OwnerR.setSelected(key);
  }

  public void setOwnerW(boolean key)
  {
    OwnerW.setSelected(key);
  }

  public void setOwnerX(boolean key)
  {
    OwnerX.setSelected(key);
  }

  public void setGroupR(boolean key)
  {
    GroupR.setSelected(key);
  }

  public void setGroupW(boolean key)
  {
    GroupW.setSelected(key);
  }

  public void setGroupX(boolean key)
  {
    GroupX.setSelected(key);
  }

  public void setOtherR(boolean key)
  {
    OtherR.setSelected(key);
  }

  public void setOtherW(boolean key)
  {
    OtherW.setSelected(key);
  }

  public void setOtherX(boolean key)
  {
    OtherX.setSelected(key);
  }


  public void setRWXproperties(AdminObjectRWXresource resource)
  {
    setOwnerR(resource.getUserR());
    setOwnerW(resource.getUserW());
    setOwnerX(resource.getUserX());

    setGroupR(resource.getGroupR());
    setGroupW(resource.getGroupW());
    setGroupX(resource.getGroupX());

    setOtherR(resource.getOtherR());
    setOtherW(resource.getOtherW());
    setOtherX(resource.getOtherX());
  }


  public void getModifiedRWXproperties(AdminObjectRWXresource resource)
  {
    resource.SetUserR(getOwnerR());
    resource.SetUserW(getOwnerW());
    resource.SetUserX(getOwnerX());

    resource.SetGroupR(getGroupR());
    resource.SetGroupW(getGroupW());
    resource.SetGroupX(getGroupX());

    resource.SetOtherR(getOtherR());
    resource.SetOtherW(getOtherW());
    resource.SetOtherX(getOtherX());
  }

  public void setEnabledExecutingEditing(boolean key)
  {
    OwnerX.setEnabled(key);
    GroupX.setEnabled(key);
    OtherX.setEnabled(key);
  }

  public void setEnabledReadingEditing(boolean key)
  {
    OwnerR.setEnabled(key);
    GroupR.setEnabled(key);
    OtherR.setEnabled(key);
  }

  public void setEnabledWritingEditing(boolean key)
  {
    OwnerW.setEnabled(key);
    GroupW.setEnabled(key);
    OtherW.setEnabled(key);
  }



}
