/*
 * $Id: OperatorProfileOtherPanel.java,v 1.2 2004/08/17 15:02:50 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:50 $
 * @module generalclient_v1
 */
public class OperatorProfileOtherPanel extends GeneralPanel
{
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();

  JTextField profileFirstName = new JTextField();
  JTextField profileSecondName = new JTextField();
  JTextField profileLastName = new JTextField();
  JTextField profilePhoneWork = new JTextField();
  JTextField profilePhoneHome = new JTextField();
  JTextField profilePhoneMobile = new JTextField();
  JTextField profilePhoneEmergency = new JTextField();

  JTextField profilePagerPhone = new JTextField();
  JTextField profilePagerNumber = new JTextField();
  JTextField profilePagerSMS = new JTextField();

  JTextArea profileAddress = new JTextArea();
  JTextField profileLanguage = new JTextField();
  JTextField profileOrganization = new JTextField();
  JTextField profileEMail = new JTextField();

  OperatorProfile profile;

  JScrollPane jScrollPane1 = new JScrollPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public OperatorProfileOtherPanel()
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

  public OperatorProfileOtherPanel(OperatorProfile profile)
  {
    this();
    setObjectResource(profile);
  }

  private void jbInit() throws Exception
  {

    this.setLayout(gridBagLayout1);

    jLabel1.setText("Имя");
    jLabel2.setText("Отчество");
    jLabel3.setText("Фамилия");
    jLabel4.setText("Телефон рабочий");
    jLabel5.setText("Телефон домашний");
    jLabel6.setText("Телефон мобильный");
    jLabel7.setText("Телефон экстренный");
    jLabel8.setText("Пейджер - телефон");
    jLabel9.setText("Пейджер - номер");
    jLabel10.setText("SMS");
    jLabel11.setText("Адрес");
    jLabel12.setText(" Язык");
    jLabel13.setText("Организация");
    jLabel14.setText("e-mail");

    jLabel1.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel2.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel2.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel3.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel3.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel4.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel4.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel5.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel5.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel6.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel6.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel7.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel7.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel8.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel8.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel9.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel9.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel10.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel10.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel11.setMinimumSize(new Dimension(DEF_WIDTH, 50));
    jLabel11.setPreferredSize(new Dimension(DEF_WIDTH, 100));
    jLabel12.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel12.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel13.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel13.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel14.setMinimumSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabel14.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

    Color disabledColor = jLabel1.getBackground();
		this.setSize(new Dimension(495, 443));

//		profileId.setEditable(false);
//		profileId.setBackground(disabledColor);
    profileAddress.setWrapStyleWord(true);

    jScrollPane1.getViewport().add(profileAddress, null);

    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setPreferredSize(new Dimension(367, 100));

    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel6, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel7, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel8, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel9, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel10, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel11, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel12, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel13, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel14, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(profileFirstName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileSecondName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileLastName, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePhoneWork, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePhoneHome, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePhoneMobile, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePhoneEmergency, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePagerPhone, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePagerNumber, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profilePagerSMS, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jScrollPane1, new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileLanguage, new GridBagConstraints(1, 11, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileOrganization, new GridBagConstraints(1, 12, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(profileEMail, new GridBagConstraints(1, 13, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 14, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }

  public ObjectResource getObjectResource()
  {
    return profile;
  }

  public void setObjectResource(ObjectResource or)
  {
    this.profile = (OperatorProfile )or;

//    System.out.println("set prop pane to " + profile.name);
/*
  profileN.setText(profile.name);
  profileCodeName.setText(profile.codename);
  profileId.setText(profile.id);
  if(profile.image_id != null && !profile.image_id.equals(""))
   profileImage.setIcon(new ImageIcon(ImageCatalogue.get(profile.image_id).getImage(profileImage)));
  profileOperators.removeAll();
  profileOperators.setListData(profile.operator_ids);
  profileNotes.setText(profile.description);
*/
    this.profileAddress.setText(profile.address);
    this.profileEMail.setText(profile.e_mail);
    this.profileFirstName.setText(profile.first_name);
    this.profileLanguage.setText(profile.language);
    this.profileLastName.setText(profile.last_name);
    this.profileOrganization.setText(profile.organization);
    this.profilePagerNumber.setText(profile.pager_number);
    this.profilePagerPhone.setText(profile.pager_phone);
    this.profilePagerSMS.setText(profile.sms_number);
    this.profilePhoneEmergency.setText(profile.phone_emergency);
    this.profilePhoneHome.setText(profile.phone_home);
    this.profilePhoneMobile.setText(profile.phone_mobile);
    this.profilePhoneWork.setText(profile.phone_work);
    this.profileSecondName.setText(profile.second_name);
  }

  public boolean modify()
  {
    profile.address = this.profileAddress.getText();
    profile.e_mail = this.profileEMail.getText();
    profile.first_name = this.profileFirstName.getText();
    profile.language = this.profileLanguage.getText();
    profile.last_name = this.profileLastName.getText();
    profile.organization = this.profileOrganization.getText();
    profile.pager_number = this.profilePagerNumber.getText();
    profile.pager_phone = this.profilePagerPhone.getText();
    profile.phone_emergency = this.profilePhoneEmergency.getText();
    profile.phone_home = this.profilePhoneHome.getText();
    profile.phone_mobile = this.profilePhoneMobile.getText();
    profile.phone_work = this.profilePhoneWork.getText();
    profile.second_name = this.profileSecondName.getText();
    profile.sms_number = this.profilePagerSMS.getText();
    return true;
  }

}

