package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import oracle.jdeveloper.layout.*;
import javax.swing.border.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

public class CommandPermissionAttributesPanel extends GeneralPanel//PropertiesPanel
{
  public CommandPermissionAttributes cpa;
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
  OrComboBox ComboOwner = new OrComboBox();//(OperatorProfile.typ);
  ObjectResourceTextField jTextCreated = new ObjectResourceTextField();
  ObjectResourceTextField jTextCreatedBy = new ObjectResourceTextField();
  ObjectResourceTextField jTextModified = new ObjectResourceTextField();
  ObjectResourceTextField jTextModifiedBy = new ObjectResourceTextField();
  JLabel jLabelForbidden = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jTextAreaForbidden = new JEditorPane();
  JPanel jPanel4 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel5 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  JLabel jLabelCategories = new JLabel();
  private TwoListsPanel categoriesPanel = new TwoListsPanel("Подключенные категории", "Неподключенные категории", OperatorCategory.typ);
  private TitledBorder titledBorder1;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public CommandPermissionAttributesPanel()
  {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public CommandPermissionAttributesPanel(CommandPermissionAttributes cpa)
  {
    this();
    setObjectResource(cpa);
  }



  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    jLabelID.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setText("Идентификатор");
    this.setLayout(gridBagLayout1);
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
    jLabelCreatedBy.setText("Кем создана");
    jLabelModified.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setText("Дата изменения");
    jLabelModifiedBy.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setText("Кем изменена");
	
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
    jLabelForbidden.setMinimumSize(new Dimension(80, 60));
    jLabelForbidden.setPreferredSize(new Dimension(125, 150));
    jLabelForbidden.setText("Текст запрета");
    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(345, 150));
    jLabelCategories.setMinimumSize(new Dimension(41, 60));
    jLabelCategories.setPreferredSize(new Dimension(125, 220));
    jLabelCategories.setText("Категории");
    this.setName("Основные свойства");
		this.setSize(new Dimension(576, 598));
    categoriesPanel.setBorder(titledBorder1);
    categoriesPanel.setPreferredSize(new Dimension(345, 220));
    jScrollPane1.getViewport().add(jTextAreaForbidden, null);

    this.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelOwner, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreated, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreatedBy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModified, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModifiedBy, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCategories, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelForbidden, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelID, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(jTextName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(ComboOwner, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextCreated, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextCreatedBy, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextModified, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextModifiedBy, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(categoriesPanel, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane1, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jTextID, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 9, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


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
    categoriesPanel.modify(cpa.category_ids);

    cpa.name = this.jTextName.getText();
    cpa.whyRejected = this.jTextAreaForbidden.getText();
    cpa.owner_id = this.ComboOwner.getSelectedId();
    return true;
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.cpa = (CommandPermissionAttributes)or;
    if(cpa == null)
      return false;

    this.jTextCreatedBy.setTextNameByID(User.typ,cpa.created_by);
    this.jTextID.setText(cpa.id);
    this.jTextModifiedBy.setTextNameByID(User.typ,cpa.modified_by);
    this.jTextName.setText(cpa.name);
    this.jTextAreaForbidden.setText(cpa.whyRejected);

    this.ComboOwner.setTyp(User.typ);
    this.ComboOwner.setSelectedTyp(User.typ, cpa.owner_id);


    this.jTextCreated.setText(sdf.format(new Date(cpa.created)));
    this.jTextModified.setText(sdf.format(new Date(cpa.modified)));

    categoriesPanel.setObjectResource(cpa);

    return true;
  }
  public ObjectResource getObjectResource()
  {
    return cpa;
  }




}