/*
 * $Id: OperatorGroupGeneralPanel.java,v 1.3 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
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
    setName("?????");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

    this.setLayout(gridBagLayout1);
//		this.setLayout(new BorderLayout());

    jLabelName.setText("????????");
    jLabelId.setText("?????????????");
    jLabelOwner.setText("????????");
    jLabelCreated.setText("???? ????????");
    jLabelCreatedBy.setText("??? ???????");
    jLabelModified.setText("???? ?????????");
    jLabelModifiedBy.setText("??? ????????");
    jLabelUsers.setText("?????????");
    jLabelRemarks.setText("??????????");

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

  public StorableObject getObjectResource()
  {
    return group;
  }

  public void setObjectResource(StorableObject or)
  {
    this.group = (OperatorGroup )or;
//    System.out.println("set prop pane to " + group.name);

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    groupName.setText(group.name);
    groupId.setText(group.getId());
    groupCreated.setText(sdf.format(new Date(group.created)));
    groupCreatedBy.setTextNameByID(User.class.getName(),group.created_by);
    groupModified.setText(sdf.format(new Date(group.modified)));
    groupModifiedBy.setTextNameByID(User.class.getName(), group.modified_by);
    groupNotes.setText(group.description);

    groupOperators.removeAll();
    groupOperators.setContents(group.getChildren(User.class.getName()));
    // list of operators

    groupOwner.setTyp(User.class.getName());
    groupOwner.setSelectedTyp(User.class.getName(), group.owner_id);
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
