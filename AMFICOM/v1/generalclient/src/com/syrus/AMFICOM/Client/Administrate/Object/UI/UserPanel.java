package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class UserPanel  extends GeneralPanel
{
  User user;
  JLabel jLabelLogin = new JLabel();
  JLabel jLabelID = new JLabel();
  JLabel jLabelTyp = new JLabel();
  JLabel jLabelProfileName = new JLabel();
  JLabel jLabelCategory = new JLabel();
  JLabel jLabelGroup = new JLabel();
  ObjectResourceTextField jTextFieldLogin = new ObjectResourceTextField();
  ObjectResourceTextField jTextFieldID = new ObjectResourceTextField();
  ObjectResourceTextField jTextFieldTyp = new ObjectResourceTextField();
  ObjectResourceTextField jTextFieldProfileName = new ObjectResourceTextField();
  JScrollPane jScrollPaneCategory = new JScrollPane();
  JScrollPane jScrollPaneGroup = new JScrollPane();

  OrListBox groupList = new OrListBox();
  OrListBox categoryList = new OrListBox();
  JPanel jPanel1 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel2 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public UserPanel()
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

  public UserPanel(User user)
  {
    this();
    setObjectResource(user);
  }



  void jbInit() throws Exception
  {
    jLabelLogin.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLogin.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLogin.setText("Логин");
    this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(554, 560));
    jLabelID.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelID.setText("Идентификатор");
    jLabelTyp.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelTyp.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelTyp.setText("Тип профиля");
    jLabelProfileName.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelProfileName.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelProfileName.setText("Имя профиля");
    jLabelCategory.setMinimumSize(new Dimension(DEF_WIDTH, 80));
    jLabelCategory.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelCategory.setText("Список категорий");
    jLabelGroup.setMinimumSize(new Dimension(DEF_WIDTH, 80));
    jLabelGroup.setPreferredSize(new Dimension(DEF_WIDTH, 150));
    jLabelGroup.setText("Список групп");
    jScrollPaneCategory.getViewport().setBackground(Color.white);
    jScrollPaneCategory.setForeground(Color.gray);
    jScrollPaneCategory.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPaneCategory.setMinimumSize(new Dimension(24, 80));
    jScrollPaneCategory.setPreferredSize(new Dimension(336, 150));
    jScrollPaneGroup.getViewport().setBackground(Color.white);
    jScrollPaneGroup.setForeground(Color.gray);
    jScrollPaneGroup.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPaneGroup.setMinimumSize(new Dimension(24, 80));
    jScrollPaneGroup.setPreferredSize(new Dimension(336, 150));
/*
    jTextFieldProfileName.setForeground(Color.gray);
    jTextFieldProfileName.setEditable(false);
    jTextFieldTyp.setForeground(Color.gray);
    jTextFieldTyp.setEditable(false);
    jTextFieldID.setForeground(Color.gray);
    jTextFieldID.setEditable(false);
    jTextFieldLogin.setForeground(Color.gray);
    jTextFieldLogin.setEditable(false);
*/
    jTextFieldProfileName.setEnabled(false);
    jTextFieldTyp.setEnabled(false);
    jTextFieldID.setEnabled(false);
    jTextFieldLogin.setEnabled(false);

    groupList.setForeground(Color.gray);
    categoryList.setForeground(Color.gray);

    this.jScrollPaneCategory.getViewport().add(this.categoryList);
    this.jScrollPaneGroup.getViewport().add(this.groupList);

    this.add(jLabelLogin, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelTyp, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelProfileName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCategory, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelGroup, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelID, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(jTextFieldLogin, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldTyp, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldProfileName, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPaneCategory, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPaneGroup, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jTextFieldID, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
/*
    jTextFieldID.setBackground(Color.white);
    jTextFieldLogin.setBackground(Color.white);
    jTextFieldProfileName.setBackground(Color.white);
    jTextFieldTyp.setBackground(Color.white);
*/
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.user = (User)or;
    if(user == null)
      return false;

    this.jTextFieldID.setText(user.id);
    this.jTextFieldLogin.setText(user.login);
    this.jTextFieldTyp.setText(user.type);
    this.jTextFieldProfileName.setTextNameByID(user.type, user.object_id);

    Vector g = new Vector();
    for(int i=0; i<user.group_ids.size(); i++)
    {
      Object o = Pool.get(OperatorGroup.typ, (String)user.group_ids.get(i));
      if(o!=null)
        g.add(o);
    }

    Vector c = new Vector();
    for(int i=0; i<user.category_ids.size(); i++)
    {
      Object o = Pool.get(OperatorCategory.typ, (String)user.category_ids.get(i));
      if(o!=null)
        c.add(o);
    }

    this.groupList.setContents(g.elements());
    this.categoryList.setContents(c.elements());
    return true;
  }
}
