/*
 * $Id: ServerPanel.java,v 1.2 2004/08/17 15:02:50 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.System.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:50 $
 * @module generalclient_v1
 */
public class ServerPanel extends GeneralPanel
{
  private Server server;

  private JPanel jPanelLabels = new JPanel();
  private JLabel jLabelId = new JLabel();
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  private JLabel jLabelName = new JLabel();
  private JLabel jLabelVersion = new JLabel();
  private JLabel jLabelLicense = new JLabel();
  private JLabel jLabelLocation = new JLabel();
  private JLabel jLabelContact = new JLabel();
  private JLabel jLabelHost = new JLabel();
  private JLabel jLabelCreated = new JLabel();
  private JLabel jLabelModified = new JLabel();
  private JPanel jPanelTextFields = new JPanel();
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
  private JLabel jLabelSessions = new JLabel();
  private JTextField jTextFieldSessions = new JTextField();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public ServerPanel()
  {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public ServerPanel(ObjectResource or)
  {
    this();
    setObjectResource(or);
  }

  void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
    jLabelId.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelId.setText("Идентификатор");
    jPanelLabels.setLayout(verticalFlowLayout1);
    jLabelName.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelName.setText("Название");
    jLabelVersion.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelVersion.setText("Версия");
    jLabelLicense.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLicense.setText("Лицензия");
    jLabelLocation.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelLocation.setText("Расположение");
    jLabelContact.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelContact.setText("Контактное лицо");
    jLabelHost.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelHost.setText("Имя хоста");
    jLabelCreated.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelCreated.setText("Дата создания");
    jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelModified.setText("Дата изменения");
    jPanelTextFields.setLayout(verticalFlowLayout2);

/*
    jTextFieldId.setForeground(Color.gray);
    jTextFieldId.setEditable(false);
    jTextFieldCreated.setEditable(false);
    jTextFieldModified.setForeground(Color.gray);
    jTextFieldModified.setEditable(false);
*/
    jTextFieldId.setEnabled(false);
    jTextFieldCreated.setEnabled(false);
    jTextFieldModified.setEnabled(false);

    jTextFieldSessions.setForeground(Color.gray);
    jLabelSessions.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelSessions.setText("Число сессий");

    this.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelVersion, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelLicense, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelLocation, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelContact, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelHost, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelCreated, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelModified, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelSessions, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jLabelId, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldVersion, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldLicense, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldLocation, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldContact, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldHostName, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldCreated, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldModified, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jTextFieldSessions, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
	    this.add(jTextFieldId, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 10, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
/*
    jTextFieldCreated.setBackground(Color.white);
    jTextFieldModified.setBackground(Color.white);
    jTextFieldId.setBackground(Color.white);
*/
  }


  public void setObjectResource(ObjectResource or)
  {
    if(or == null)
      return;

    server = (Server)or;
    setData(server);
  }

  private void setData(Server server)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    jTextFieldContact.setText(server.contact);
    jTextFieldCreated.setText(sdf.format(new Date(server.created)));
    jTextFieldHostName.setText(server.hostname);
    jTextFieldId.setText(server.id);
    jTextFieldLicense.setText(server.licence_id);
    jTextFieldLocation.setText(server.location);
    jTextFieldModified.setText(sdf.format(new Date(server.modified)));
    jTextFieldName.setText(server.name);
    jTextFieldVersion.setText(server.version);
    jTextFieldSessions.setText(String.valueOf(server.sessions));
  }

  public ObjectResource getModifiedObjectResource()
  {
    Date d = new Date();
    server.contact = jTextFieldContact.getText();

    server.hostname = jTextFieldHostName.getText();
    server.id = jTextFieldId.getText();
    server.licence_id = jTextFieldLicense.getText();
    server.location = jTextFieldLocation.getText();

    server.name = jTextFieldName.getText();
    server.version = jTextFieldVersion.getText();

    server.modified = d.getTime();

    return server;
  }

}
