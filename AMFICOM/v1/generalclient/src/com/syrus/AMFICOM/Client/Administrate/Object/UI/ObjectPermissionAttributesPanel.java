package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

public class ObjectPermissionAttributesPanel extends GeneralPanel//PropertiesPanel
{
  AdminObjectResource permissionObjectResource;

  User admin;
  public ObjectPermissionAttributes opa;
  SimpleDateFormat sdf = new SimpleDateFormat();
  JLabel jLabelID = new JLabel();
  JLabel jLabelName = new JLabel();
  JLabel jLabelOwner = new JLabel();
  JLabel jLabelCreated = new JLabel();
  JLabel jLabelCreatedBy = new JLabel();
  JLabel jLabelModified = new JLabel();
  JLabel jLabelModifiedBy = new JLabel();
  ObjectResourceTextField jTextID = new ObjectResourceTextField();
  ObjectResourceTextField jTextName = new ObjectResourceTextField();
  OrComboBox comboBoxOwner = new OrComboBox();//(OperatorProfile.typ);
  ObjectResourceTextField jTextCreated = new ObjectResourceTextField();
  ObjectResourceTextField jTextCreatedBy = new ObjectResourceTextField();
  ObjectResourceTextField jTextModified = new ObjectResourceTextField();
  ObjectResourceTextField jTextModifiedBy = new ObjectResourceTextField();
  JLabel jLabelForbidden = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jTextAreaForbidden = new JEditorPane();

  public RWXPanel rwxPanel = new RWXPanel();

  FlowLayout flowLayout1 = new FlowLayout();
  JLabel jLabelAttributes = new JLabel();
  private TwoListsPanel groupsPanel = new TwoListsPanel("Подключенные группы", "Неподключенные группы", OperatorGroup.typ);
  private JLabel jLabelGroups = new JLabel();
  private TitledBorder titledBorder1;
  private ApplicationContext aContext;
  private TitledBorder titledBorder2;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();


  public ObjectPermissionAttributesPanel()
  {
    setName("Атрибуты");
    try
    {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public ObjectPermissionAttributesPanel(ObjectPermissionAttributes opa)
  {
    this();
    setObjectResource(opa);
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    jLabelID.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setText("Идентификатор");
    this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(570, 611));
    jLabelName.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelName.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelName.setText("Название");
    jLabelOwner.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelOwner.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelOwner.setText("Владелец");
    jLabelCreated.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setText("Дата создания");
    jLabelCreatedBy.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreatedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreatedBy.setText("Кем создан");
    jLabelModified.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setText("Дата изменения");
    jLabelModifiedBy.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setText("Кем изменен");

    jTextID.setText("");
    jTextName.setText("");
    jTextCreated.setText("");
    jTextCreatedBy.setText("");
    jTextModified.setText("");
/*
    jTextID.setForeground(Color.gray);
    jTextID.setEditable(false);
    jTextCreated.setForeground(Color.gray);
    jTextCreated.setEditable(false);
    jTextCreatedBy.setForeground(Color.gray);
    jTextCreatedBy.setEditable(false);
    jTextModified.setForeground(Color.gray);
    jTextModified.setEditable(false);
    jTextModifiedBy.setForeground(Color.gray);
    jTextModifiedBy.setEditable(false);
*/
    jTextID.setEnabled(false);
    jTextCreated.setEnabled(false);
    jTextCreatedBy.setEnabled(false);
    jTextModified.setEnabled(false);
    jTextModifiedBy.setEnabled(false);

//    ComboOwner.setEditable(true);
    jLabelForbidden.setMinimumSize(new Dimension(DEF_WIDTH, 60));
    jLabelForbidden.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelForbidden.setText("Текст запрета");
    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(460, 150));
//    jPanelRWX.setPreferredSize(new Dimension(327, 100));
//    jPanelRWX.setLayout(flowLayout1);
    jLabelAttributes.setMinimumSize(new Dimension(DEF_WIDTH, 150));
    jLabelAttributes.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelAttributes.setText("Атрибуты");
    groupsPanel.setBorder(titledBorder1);
    groupsPanel.setPreferredSize(new Dimension(429, 150));
    jLabelGroups.setMinimumSize(new Dimension(DEF_WIDTH, 150));
    jLabelGroups.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelGroups.setText("Группы");
    comboBoxOwner.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        comboBoxOwner_actionPerformed(e);
      }
    });
    rwxPanel.setBorder(titledBorder2);
    jScrollPane1.getViewport().add(jTextAreaForbidden, null);

    this.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(jLabelOwner, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreated, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreatedBy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModified, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModifiedBy, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelGroups, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelAttributes, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelForbidden, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelID, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(jTextName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(comboBoxOwner, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextCreated, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextCreatedBy, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextModified, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextModifiedBy, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(groupsPanel, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rwxPanel, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane1, new GridBagConstraints(1, 8, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jTextID, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    // Setting of the editable fields:
/*
    jTextID.setBackground(Color.white);
    jTextCreated.setBackground(Color.white);
    jTextCreatedBy.setBackground(Color.white);
    jTextModified.setBackground(Color.white);
    jTextModifiedBy.setBackground(Color.white);
*/
  }

  public boolean modify()
  {
    Date date = new Date();

    rwxPanel.getModifiedRWXproperties(opa);

    groupsPanel.modify(opa.group_ids);

    opa.name = this.jTextName.getText();
    opa.owner_id = comboBoxOwner.getSelectedId();
    opa.whyRejected = this.jTextAreaForbidden.getText();
    opa.modified_by = admin.id;
    opa.modified = date.getTime();

    if(permissionObjectResource != null)
    {
      permissionObjectResource.setOwnerId(comboBoxOwner.getSelectedId());
      permissionObjectResource.setModificationTime(opa.modified);
      if(permissionObjectResource instanceof Domain)
        ((Domain)(permissionObjectResource)).modified_by = opa.modified_by;
    }
    opa.setTransferableFromLocal();
    return true;
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.admin = (User)(Pool.get(User.typ,
                                aContext.getSessionInterface().getUserId()));
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.opa = (ObjectPermissionAttributes)or;
    if(opa == null)
      return false;

    this.jTextCreatedBy.setTextNameByID(User.typ,opa.created_by);
    this.jTextID.setText(opa.id);
    this.jTextModifiedBy.setTextNameByID(User.typ,opa.modified_by);
    this.jTextName.setText(opa.name);
    this.jTextAreaForbidden.setText(opa.whyRejected);

    this.comboBoxOwner.setTyp(User.typ);
    this.comboBoxOwner.setSelectedTyp(User.typ, opa.owner_id);


    this.jTextCreated.setText(sdf.format(new Date(opa.created)));
    this.jTextModified.setText(sdf.format(new Date(opa.modified)));


    rwxPanel.setRWXproperties(opa);

    groupsPanel.setObjectResource(opa);

    return true;
  }

  public ObjectResource getObjectResource()
  {
    return opa;
  }

  public void setPermissionObjectResource(AdminObjectResource aor)
  {
    this.permissionObjectResource = aor;
  }

  void comboBoxOwner_actionPerformed(ActionEvent e)
  {
    if(permissionObjectResource != null)
      permissionObjectResource.setOwnerId(comboBoxOwner.getSelectedId());
  }

  public void enableExecutingEditing(boolean key)
  {
    rwxPanel.setEnabledExecutingEditing(key);
  }

  public void enableReadingEditing(boolean key)
  {
    rwxPanel.setEnabledReadingEditing(key);
  }

  public void enableWritingEditing(boolean key)
  {
    rwxPanel.setEnabledWritingEditing(key);
  }


}
