/*
 * $Id: OperatorCategoryPanel.java,v 1.3 2004/09/27 13:04:43 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.*;
import javax.swing.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 13:04:43 $
 * @module generalclient_v1
 */
public final class OperatorCategoryPanel extends JPanel implements ObjectResourcePropertiesPane {
	private static OperatorCategoryPanel instance = null;

  ApplicationContext aContext = new ApplicationContext();
  NewUpDater updater;
  JLabel idLabel = new JLabel();
  JLabel nameLabel = new JLabel();
  JLabel operatorsLabel = new JLabel();
  JLabel remarksLabel = new JLabel();

  User user;

  JTextField categoryName = new JTextField();
  JTextField categoryId = new JTextField();
  OrListBox categoryOperators = new OrListBox();
  JEditorPane categoryNotes = new JEditorPane();

  OperatorCategory category;
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public OperatorCategoryPanel() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public OperatorCategoryPanel(OperatorCategory category) {
		this();
		setObjectResource(category);
	}

	private void jbInit() {
		idLabel.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		operatorsLabel.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		operatorsLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		remarksLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		remarksLabel.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jScrollPane1.setPreferredSize(new Dimension(381, 200));
		jScrollPane2.setPreferredSize(new Dimension(381, 200));
		this.setLayout(gridBagLayout1);
		nameLabel.setText("Название");
		nameLabel.setText("Название");
		operatorsLabel.setText("Операторы");
		remarksLabel.setText("Примечания");
		Color disabledColor = nameLabel.getBackground();
		categoryNotes.setBorder(null);
		categoryId.setEnabled(false);
		categoryName.setEnabled(false);
		jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
		jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(operatorsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(remarksLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		if (Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(categoryName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jScrollPane1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jScrollPane2, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		if (Environment.isDebugMode())
			this.add(categoryId, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jScrollPane2.getViewport().add(categoryNotes, null);
		jScrollPane1.getViewport().add(categoryOperators, null);
	}

  public ObjectResource getObjectResource()
  {
    return category;
  }

  public void setObjectResource(ObjectResource or)
  {
    ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
                                   Pool.get("ObjectFrame", "AdministrateObjectFrame");
    if(f!=null)
    {
      f.setTitle("Категории");
    }

    if(!Checker.checkCommand(user, Checker.readCategoryInfo))
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
    this.category = (OperatorCategory )or;
    category.updateLocalFromTransferable();
    categoryName.setText(category.name);
    categoryId.setText(category.id);
    categoryOperators.removeAll();

    categoryOperators.setContents(category.getChildren(User.typ));
    categoryNotes.setText(category.description);
  }

  public void setContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.user = (User)Pool.get(User.typ,
                               this.aContext.getSessionInterface().getUserId());
    this.updater = new NewUpDater(this.aContext);
  }

  public boolean modify()
  {
    if(!Checker.checkCommand(user, Checker.modifyCategory))
    {
      this.setData(category);
      return false;
    }



    this.showTheWindow(true);
    category.description = this.categoryNotes.getText();

    if(!NewUpDater.checkName(category))
      return false;

    updater.updateObjectResources(category, false);
    Pool.put(OperatorCategory.typ, category.id, category);

    this.aContext.getDataSource().SaveCategory(category.id);
    return true;
  }

  public boolean delete()
  {
    String error = "Ошибка: Категория не может быть удалена.";
    JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
    return false;
  }

  public boolean open()
  {
    return true;
  }

  public boolean cancel()
  {
    this.setData(category);
    return true;
  }

  public boolean save()
  {
    return modify();
  }

  public boolean create()
  {
    String error = "Ошибка: Категория не может быть создана.";
    JOptionPane.showMessageDialog(null, error, "Ошибка", JOptionPane.OK_OPTION);
    return false; // Categories are not to be created !!!
  }


  void showTheWindow(boolean key)
  {
//    this.jp.setVisible(key);
    repaint();
  }

	public static OperatorCategoryPanel getInstance() {
		if (instance == null)
			synchronized (OperatorCategoryPanel.class) {
				if (instance == null)
					instance = new OperatorCategoryPanel();
			}
		return instance;
	}
}
