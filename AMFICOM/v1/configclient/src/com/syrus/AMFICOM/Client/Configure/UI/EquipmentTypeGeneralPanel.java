package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class EquipmentTypeGeneralPanel extends GeneralPanel
{
  EquipmentType equipmentType = null;

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  JLabel idLabel = new JLabel();
  JTextField idField = new JTextField();

  JLabel nameLabel = new JLabel();
  JTextField nameField = new JTextField();

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  private JLabel manufacturerLabel = new JLabel();
  private JTextField manufacturerField = new JTextField();

  private JLabel manufacturerCodeLabel = new JLabel();
  private JTextField manufacturerCodeField = new JTextField();

  private JLabel modifyLabel2 = new JLabel();
  private JLabel modifyLabel1 = new JLabel();
  private JTextField ModifyField = new JTextField();

  private JLabel descLabel = new JLabel();
  private JPanel descriptionPanel = new JPanel();
  JScrollPane descriptionScrollPane = new JScrollPane();
  public JTextPane descTextArea = new JTextPane();

  private JLabel portsNumberLabel = new JLabel();
  private JTextField portsNumberField = new JTextField();

  private JLabel cabelPortsNumberLabel1 = new JLabel();
  private JLabel cabelPortsNumberLabel2 = new JLabel();
  private JTextField cabelPortsNumberField = new JTextField();


  private JButton saveButton = new JButton();

  private BorderLayout borderLayout1 = new BorderLayout();

  public EquipmentTypeGeneralPanel()
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

  public EquipmentTypeGeneralPanel(EquipmentType eqType)
  {
    this();
    setObjectResource(eqType);
  }

  private void jbInit() throws Exception
  {
    setName("Общие");

    idLabel.setText("Идентификатор");
    idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    idField.setEnabled(false);

    nameLabel.setText("Название");
    nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

    descLabel.setText("Описание");
    descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

    manufacturerCodeLabel.setText("Код производителя");
    manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    manufacturerField.setEnabled(false);

    manufacturerLabel.setText("Производитель");
    manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    manufacturerCodeField.setEnabled(false);

    modifyLabel1.setText("Время");
    modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
    modifyLabel2.setText("последнего изменения");
    modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
    ModifyField.setEnabled(false);

    cabelPortsNumberLabel1.setText("Число");
    cabelPortsNumberLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
    cabelPortsNumberLabel2.setText("кабельных портов");
    cabelPortsNumberLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

    portsNumberLabel.setText("Число портов");
    portsNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, 10));

    saveButton.setText("Сохранить");
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveButton_actionPerformed(e);
      }
    });

    this.setLayout(gridBagLayout1);

    descriptionPanel.setLayout(borderLayout1);
    descriptionScrollPane.getViewport().add(descTextArea, null);
    descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

    this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(manufacturerLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(manufacturerCodeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(cabelPortsNumberLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    this.add(cabelPortsNumberLabel2,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    this.add(portsNumberLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(modifyLabel1,      new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    this.add(modifyLabel2,           new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    this.add(descLabel,  new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(idLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(manufacturerField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(manufacturerCodeField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(cabelPortsNumberField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(portsNumberField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(ModifyField,       new GridBagConstraints(1, 7, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(descriptionPanel,  new GridBagConstraints(1, 9, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(idField, new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  }

  public ObjectResource getObjectResource()
  {
    return equipmentType;
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.equipmentType = (EquipmentType)or;
    if(equipmentType != null)
    {
//			System.out.println("set prop pane to " + equipment.name);

      idField.setText(equipmentType.getId());
      nameField.setText(equipmentType.getName());
      this.descTextArea.setText(equipmentType.description);

      this.ModifyField.setText(sdf.format(new Date(equipmentType.modified)));

      int portsNumber = 0;
      int cablePortsNumber = 0;

      Hashtable protoHash = Pool.getHash(ProtoElement.typ);
      if  (protoHash == null)
        return false;

      Enumeration protoEnum = protoHash.elements();
      for (; protoEnum.hasMoreElements();)
      {
        ProtoElement curProto = (ProtoElement) protoEnum.nextElement();
        EquipmentType itsEqType = (EquipmentType) Pool.get(EquipmentType.typ,curProto.equipment_type_id);
        if (itsEqType == this.equipmentType)
        {
          for (int i = 0; i < curProto.devices.size(); i++)
          {
            SchemeDevice curSD = (SchemeDevice)curProto.devices.get(i);
            portsNumber += curSD.ports.size();
            cablePortsNumber += curSD.cableports.size();
          }
          break;
        }
      }

      this.portsNumberField.setText(Long.toString(portsNumber));
      this.cabelPortsNumberField.setText(Long.toString(cablePortsNumber));
    }
    else
    {
      idField.setText("");
      nameField.setText("");
      this.descTextArea.setText("");
      this.ModifyField.setText("");
      this.portsNumberField.setText("");
      this.cabelPortsNumberField.setText("");
    }
    return true;

  }

  public boolean modify()
  {
    try
    {
      if(MyUtil.validName(nameField.getText()))
         equipmentType.name = nameField.getText();
      else
        return false;

      equipmentType.id = idField.getText();
      equipmentType.description = this.descTextArea.getText();
    }
    catch(Exception ex)
    {
      return false;
    }
    return true;
  }

  void saveButton_actionPerformed(ActionEvent e)
  {
    if(!Checker.checkCommandByUserId(
        aContext.getSessionInterface().getUserId(),
        Checker.catalogTCediting))
    {
      return;
    }

    if(modify())
    {
      DataSourceInterface dataSource = aContext.getDataSourceInterface();
      String[] s = new String[1];
      s[0] = equipmentType.getId();
      dataSource.SaveEquipmentTypes(s);
    }
  }

  public boolean delete()
  {
/*		if(!Checker.checkCommandByUserId(
        aContext.getSessionInterface().getUserId(),
        Checker.catalogTCediting))
      return false;

    String []s = new String[1];

    s[0] = linkType.id;
    aContext.getDataSourceInterface().RemoveLinks(s);*/

    return true;
  }
}

