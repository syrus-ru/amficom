/*
 * $Id: AgentPanel.java,v 1.1 2004/08/06 12:14:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.text.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/08/06 12:14:19 $
 * @module generalclient_v1
 */
public class AgentPanel extends GeneralPanel
{
	private Agent agent;

	private JLabel jLabelId = new JLabel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private JLabel nameLabel = new JLabel();
	private JLabel versionabel = new JLabel();
	private JLabel licenceLabel = new JLabel();
	private JLabel locationLabel = new JLabel();
	private JLabel contactPersonLabel = new JLabel();
	private JLabel hostNameLabel = new JLabel();
	private JLabel createdLabel = new JLabel();
	private JLabel modifiedLabel = new JLabel();

	private JTextField jTextFieldId = new JTextField();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JTextField jTextFieldName = new JTextField();
	private JTextField jTextFieldVersion = new JTextField();
	private JTextField jTextFieldLicense = new JTextField();
	private JTextField jTextFieldLocation = new JTextField();
	private JTextField jTextFieldContact = new JTextField();
	private JTextField jTextFieldHostName = new JTextField();
	private JTextField jTextFieldCreated = new JTextField();
	private JTextField jTextFieldModified = new JTextField();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public AgentPanel()
  {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public AgentPanel(ObjectResource or)
  {
    this();
    setObjectResource(or);
  }

  void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(533, 369));
    jLabelId.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelId.setText("Идентификатор");
    nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    nameLabel.setText("Название");
    versionabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    versionabel.setText("Версия");
    licenceLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    licenceLabel.setText("Лицензия");
    locationLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    locationLabel.setText("Расположение");
    contactPersonLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    contactPersonLabel.setText("Контактное лицо");
    hostNameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    hostNameLabel.setText("Имя хоста");
    createdLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    createdLabel.setText("Дата создания");
    modifiedLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    modifiedLabel.setText("Дата изменения");
/*
    jTextFieldId.setForeground(Color.gray);
    jTextFieldId.setEditable(false);
    jTextFieldCreated.setForeground(Color.gray);
    jTextFieldCreated.setEditable(false);
    jTextFieldModified.setForeground(Color.gray);
    jTextFieldModified.setEditable(false);
*/
    jTextFieldId.setEnabled(false);
    jTextFieldCreated.setEnabled(false);
    jTextFieldModified.setEnabled(false);

    this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(versionabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(licenceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(locationLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(contactPersonLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(hostNameLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(createdLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(modifiedLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelId, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldVersion, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldLicense, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldLocation, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldContact, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldHostName, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldCreated, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldModified, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		this.add(jTextFieldId, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 9, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
/*
    jTextFieldCreated.setBackground(Color.white);
    jTextFieldModified.setBackground(Color.white);
    jTextFieldId.setBackground(Color.white);
*/
  }


  public boolean setObjectResource(ObjectResource or)
  {
    if(or == null)
      return false;

    agent = (Agent)or;
    setData(agent);
    return true;
  }

  private void setData(Agent agent)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    jTextFieldContact.setText(agent.contact);
    jTextFieldCreated.setText(sdf.format(new Date(agent.created)));
    jTextFieldHostName.setText(agent.hostname);
    jTextFieldId.setText(agent.id);
    jTextFieldLicense.setText(agent.licence_id);
    jTextFieldLocation.setText(agent.location);
    jTextFieldModified.setText(sdf.format(new Date(agent.modified)));
    jTextFieldName.setText(agent.name);
    jTextFieldVersion.setText(agent.version);
  }

  public ObjectResource getModifiedObjectResource()
  {
    Date d = new Date();
    agent.contact = jTextFieldContact.getText();

    agent.hostname = jTextFieldHostName.getText();
    agent.id = jTextFieldId.getText();
    agent.licence_id = jTextFieldLicense.getText();
    agent.location = jTextFieldLocation.getText();

    agent.name = jTextFieldName.getText();
    agent.version = jTextFieldVersion.getText();

    agent.modified = d.getTime();

    return agent;
  }




}
