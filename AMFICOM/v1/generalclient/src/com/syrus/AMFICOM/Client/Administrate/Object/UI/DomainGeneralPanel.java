package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.text.*;
import java.util.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Color;

public class DomainGeneralPanel extends GeneralPanel
{
	public ApplicationContext aContext = new ApplicationContext();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

	JLabel jLabelId = new JLabel();
//  JLabel jLabelOwner = new JLabel();

	ObjectResourceTextField domainId = new ObjectResourceTextField();
//  OrComboBox domainOwner = new OrComboBox();
	ObjectResourceTextField domainCreatedBy = new ObjectResourceTextField();
	ObjectResourceTextField domainModified = new ObjectResourceTextField();
	ObjectResourceTextField domainModifiedBy = new ObjectResourceTextField();


	Dispatcher dispatcher;


	Domain domain;

	JScrollPane jScrollPane1 = new JScrollPane();
//  JTextArea domainNotes = new JTextArea();
	JEditorPane  domainNotes = new JEditorPane();
	private JPanel jPanel2 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabelName = new JLabel();
	JLabel jLabelCreated = new JLabel();
	JLabel jLabelCreatedBy = new JLabel();
	JLabel jLabelModified = new JLabel();
	JLabel jLabelModifiedBy = new JLabel();
	JLabel jLabel1Remarks = new JLabel();
	ObjectResourceTextField domainName = new ObjectResourceTextField();
	ObjectResourceTextField domainCreated = new ObjectResourceTextField();

	public DomainGeneralPanel()
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

	public DomainGeneralPanel(Domain domain)
	{
		this();
		setObjectResource(domain);
	}

	private void jbInit() throws Exception
	{
		this.setPreferredSize(new Dimension(500, 500));

		this.setLayout(gridBagLayout1);


		jLabelId.setPreferredSize(new Dimension(90, DEF_HEIGHT));
		jLabelId.setText("Идентификатор");
//    jLabelOwner.setPreferredSize(new Dimension(135, 23));
//    jLabelOwner.setText("Владелец");


    Color disabledColor = jLabelName.getBackground();
/*
    domainId.setForeground(Color.gray);
    domainId.setEditable(false);

    domainCreatedBy.setForeground(Color.gray);
    domainCreatedBy.setEditable(false);

    domainModified.setForeground(Color.gray);
    domainModified.setEditable(false);

    domainModifiedBy.setEditable(false);
    domainModifiedBy.setForeground(Color.gray);

		domainCreated.setEditable(false);
		domainCreated.setForeground(Color.gray);
		domainCreated.setBackground(Color.white);
*/
    domainId.setEnabled(false);
    domainCreatedBy.setEnabled(false);
    domainModified.setEnabled(false);
    domainModifiedBy.setEnabled(false);
	domainCreated.setEnabled(false);

    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(322, 200));

    jPanel2.setLayout(verticalFlowLayout2);
		jLabelName.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelName.setText("Название");
		jLabelCreated.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelCreated.setText("Дата создания");
		jLabelCreatedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelCreatedBy.setText("Кем создан");
		jLabelModified.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelModified.setText("Дата изменения");
		jLabelModifiedBy.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelModifiedBy.setText("Кем изменен");
		jLabel1Remarks.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel1Remarks.setText("Примечания");
//    jPanel1.add(jLabelId, null);//tmp
//    jPanel1.add(jLabelOwner, null);
//    jPanel2.add(domainId, null);//tmp
//    jPanel2.add(domainOwner, null);
    jScrollPane1.getViewport().add(domainNotes, null);
		this.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabelCreated, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabelCreatedBy, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabelModified, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabelModifiedBy, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel1Remarks, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainCreated, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainCreatedBy, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainModified, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainModifiedBy, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jScrollPane1, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
/*
    domainCreatedBy.setBackground(Color.white);
    domainModified.setBackground(Color.white);
    domainModifiedBy.setBackground(Color.white);
    domainId.setBackground(Color.white);
*/
    this.setVisible(true);
  }


  public void setObjectResource(ObjectResource or)
  {
    this.domain = (Domain)or;
    if(this.domain == null)
      return;

    SimpleDateFormat sdf = new SimpleDateFormat();
    this.domainCreated.setText(sdf.format( new Date(domain.created)));
    this.domainCreatedBy.setTextNameByID(User.typ, domain.created_by);
    this.domainId.setText(domain.id);
    this.domainModified.setText(sdf.format(new Date(domain.modified)));
    this.domainModifiedBy.setTextNameByID(User.typ, domain.modified_by);
    this.domainName.setText(domain.name);
    this.domainNotes.setText(domain.description);

//    this.domainOwner.setTyp(User.typ);
//    this.domainOwner.setSelectedTyp(User.typ, domain.owner_id);
  }

  public boolean modify()
  {
//    Date date = new Date();
//    domain.owner_id = this.domainOwner.getSelectedId();
    domain.name = this.domainName.getText();
    domain.description = this.domainNotes.getText();

//    domain.opa.owner_id = domain.owner_id;
//    domain.opa.modified = domain.modified;
//    domain.opa.modified_by = domain.modified_by;
//    domain.opa.created = domain.created;
//    domain.opa.created_by = domain.created_by;
    return true;
  }

}
