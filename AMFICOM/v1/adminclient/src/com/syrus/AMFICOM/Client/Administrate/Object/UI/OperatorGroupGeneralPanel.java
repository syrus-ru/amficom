package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class OperatorGroupGeneralPanel extends GeneralPanel
{
  ApplicationContext aContext = new ApplicationContext();
  JLabel jLabelName = new JLabel();
  JLabel jLabelId = new JLabel();
  JLabel jLabelOwner = new JLabel();
  JLabel jLabelCreated = new JLabel();
  JLabel jLabelCreatedBy = new JLabel();
  JLabel jLabelModified = new JLabel();
  JLabel jLabelModifiedBy = new JLabel();
  JLabel jLabelUsers = new JLabel();
  JLabel jLabelRemarks = new JLabel();


  ObjectResourceTextField groupName = new ObjectResourceTextField();
  ObjectResourceTextField groupId = new ObjectResourceTextField();
  OrComboBox groupOwner = new OrComboBox();
  ObjectResourceTextField groupCreated = new ObjectResourceTextField();
  ObjectResourceTextField groupCreatedBy = new ObjectResourceTextField();
  ObjectResourceTextField groupModified = new ObjectResourceTextField();
  ObjectResourceTextField groupModifiedBy = new ObjectResourceTextField();

  OrListBox groupOperators = new OrListBox();
  JEditorPane groupNotes = new JEditorPane();

  OperatorGroup group;
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();

  JPanel jPanelLabels = new JPanel();
  JPanel jPanelTexts = new JPanel();
  JPanel jPanelTextFields = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public OperatorGroupGeneralPanel()
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

  public OperatorGroupGeneralPanel(OperatorGroup group)
  {
    this();
    setObjectResource(group);
  }

  private void jbInit() throws Exception
  {
    setName("Общие");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

    this.setLayout(gridBagLayout1);
//		this.setLayout(new BorderLayout());

    jLabelName.setText("Название");
    jLabelId.setText("Идентификатор");
    jLabelOwner.setText("Владелец");
    jLabelCreated.setText("Дата создания");
    jLabelCreatedBy.setText("Кем создана");
    jLabelModified.setText("Дата изменения");
    jLabelModifiedBy.setText("Кем изменена");
    jLabelUsers.setText("Операторы");
    jLabelRemarks.setText("Примечания");

    jLabelName.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelId.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelOwner.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreatedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModifiedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelUsers.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelRemarks.setPreferredSize(new Dimension(DEF_WIDTH, 150));

    Color disabledColor = jLabelName.getBackground();
/*
    groupId.setForeground(Color.gray);
    groupId.setEditable(false);
//    groupNotes.setWrapStyleWord(true);
    groupCreated.setForeground(Color.gray);
    groupCreated.setEditable(false);
    groupCreatedBy.setForeground(Color.lightGray);
    groupCreatedBy.setEditable(false);
    groupModified.setForeground(Color.gray);
    groupModified.setEditable(false);
    groupModifiedBy.setForeground(Color.gray);
    groupModifiedBy.setEditable(false);
*/
    groupId.setEnabled(false);
    groupCreated.setEnabled(false);
    groupCreatedBy.setEnabled(false);
    groupModified.setEnabled(false);
    groupModifiedBy.setEnabled(false);

    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(341, 150));
    jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane2.setPreferredSize(new Dimension(341, 150));

    jScrollPane1.getViewport().add(groupOperators, null);
    jScrollPane2.getViewport().add(groupNotes, null);

    this.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(jLabelOwner, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreated, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreatedBy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModified, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModifiedBy, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelUsers, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelRemarks, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelId, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(groupName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    this.add(groupOwner, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(groupCreated, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(groupCreatedBy, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(groupModified, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(groupModifiedBy, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane1, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane2, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(groupId, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 9, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
/*
    groupId.setBackground(Color.white);

    groupCreated.setBackground(Color.white);
    groupCreatedBy.setBackground(Color.white);
    groupModified.setBackground(Color.white);
    groupModifiedBy.setBackground(Color.white);
*/	
  }

  public ObjectResource getObjectResource()
  {
    return group;
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.group = (OperatorGroup )or;
//    System.out.println("set prop pane to " + group.name);

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    groupName.setText(group.name);
    groupId.setText(group.id);
    groupCreated.setText(sdf.format(new Date(group.created)));
    groupCreatedBy.setTextNameByID(User.typ,group.created_by);
    groupModified.setText(sdf.format(new Date(group.modified)));
    groupModifiedBy.setTextNameByID(User.typ, group.modified_by);
    groupNotes.setText(group.description);

    groupOperators.removeAll();
    groupOperators.setContents(group.getChildren(User.typ));
    // list of operators

    groupOwner.setTyp(User.typ);
    groupOwner.setSelectedTyp(User.typ, group.owner_id);
    return true;
  }

  public boolean modify()
  {
    group.name = this.groupName.getText();
    group.description = this.groupNotes.getText();
    group.owner_id = groupOwner.getSelectedId();
    return true;
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
  }

}