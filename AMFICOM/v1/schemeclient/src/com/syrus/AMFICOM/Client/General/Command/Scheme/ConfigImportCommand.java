/*-
 * $Id: ConfigImportCommand.java,v 1.6 2006/02/15 12:19:50 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlConfigurationLibrary;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlPortType;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipmentSeq;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.util.Log;

public class ConfigImportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public ConfigImportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();

		final String fileName = openFileForReading(
				LangModelScheme.getString("Title.open.config_xml"));
		if(fileName == null)
			return;

		try {
			loadConfigXML(fileName);
		} catch (CreateObjectException e) {
			Log.errorMessage(e.getMessage());
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (XmlException e) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					LangModelScheme.getString("Message.error.parse_xml"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			Log.errorMessage(e);
			return;
		} catch (IOException e) {
			Log.errorMessage(e);
			return;
		}		 

		ApplicationModel aModel = this.pane.getContext().getApplicationModel();
		aModel.setEnabled("Menu.import.commit", true);
		aModel.fireModelChanged();
	}
	
	protected void loadConfigXML(String fileName) throws CreateObjectException, XmlException, IOException {
		File xmlfile = new File(fileName);
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.parse(xmlfile);
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
		}
		
//		make sure default types loaded
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		String importType = doc.getImportType();
		
		XmlPortTypeSeq xmlPortTypes = doc.getPortTypes();
		if (xmlPortTypes != null) {
			for(XmlPortType xmlPortType : xmlPortTypes.getPortTypeArray()) {
				PortType.createInstance(this.userId, importType, xmlPortType);
			}
		}
		XmlLinkTypeSeq xmlLinkTypes = doc.getLinkTypes();
		if (xmlLinkTypes != null) {
			for(XmlLinkType xmlLinkType : xmlLinkTypes.getLinkTypeArray()) {
				LinkType.createInstance(this.userId, importType, xmlLinkType);
			}
		}
		List<String> errorMessages = new LinkedList<String>();
		XmlCableLinkTypeSeq xmlCableLinkTypes = doc.getCableLinkTypes();
		if (xmlCableLinkTypes != null) {
			for (final XmlCableLinkType cableLinkType : xmlCableLinkTypes.getCableLinkTypeArray()) {
				CableLinkType cableLinkType2 = CableLinkType.createInstance(this.userId, cableLinkType, importType);
				if (cableLinkType2.getCableThreadTypes(false).size() == 0) {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_type_no_threads") + cableLinkType2.getName()); //$NON-NLS-1$
				}
			}
		}
		if (errorMessages.size() > 0) {
			if (!ClientUtils.showConfirmDialog(new JScrollPane(new JList(errorMessages.toArray())),
					LangModelScheme.getString("Message.confirmation.continue_parse"))) { //$NON-NLS-1$
				throw new CreateObjectException("incorrect input data");
			}
		}
		XmlProtoEquipmentSeq xmlProtoEquipments = doc.getProtoEquipments();
		if (xmlProtoEquipments != null) {
			for(XmlProtoEquipment xmlEquipmentType : xmlProtoEquipments.getProtoEquipmentArray()) {
				ProtoEquipment.createInstance(this.userId, xmlEquipmentType, importType);
			}
		}
		XmlEquipmentSeq xmlEquipments = doc.getEquipments();
		if (xmlEquipments != null) {
			for(XmlEquipment xmlEquipment : xmlEquipments.getEquipmentArray()) {
				Equipment.createInstance(this.userId, xmlEquipment, importType);
			}
		}
		
		System.setProperty(USER_DIR,  user_dir);
	}
}
