/*-
 * $Id: ConfigExportCommand.java,v 1.5 2005/10/30 14:49:18 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.util.Log;

public class ConfigExportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public ConfigExportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
		
		final String fileName = openFileForWriting(
				LangModelScheme.getString("Title.save.config_xml")); //$NON-NLS-1$
		if(fileName == null)
			return;

		try {
			StorableObjectCondition condition2 = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE); 
			Collection<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlLinkType> xmlLinkTypes = new HashSet<XmlLinkType>();
			for (LinkType linkType : linkTypes) {
				XmlLinkType xmlLinkType = XmlLinkType.Factory.newInstance();
				linkType.getXmlTransferable(xmlLinkType, AMFICOM_IMPORT, false);
				xmlLinkTypes.add(xmlLinkType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE); 
			Collection<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlCableLinkType> xmlCableLinkTypes = new HashSet<XmlCableLinkType>();
			for (CableLinkType linkType : cableLinkTypes) {
				XmlCableLinkType xmlCableLinkType = XmlCableLinkType.Factory.newInstance();
				linkType.getXmlTransferable(xmlCableLinkType, AMFICOM_IMPORT, false);
				xmlCableLinkTypes.add(xmlCableLinkType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.PORT_TYPE_CODE); 
			Collection<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlPortType> xmlPortTypes = new HashSet<XmlPortType>();
			for (PortType portType : portTypes) {
				XmlPortType xmlPortType = XmlPortType.Factory.newInstance();
				portType.getXmlTransferable(xmlPortType, AMFICOM_IMPORT, false);
				xmlPortTypes.add(xmlPortType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.PROTOEQUIPMENT_CODE); 
			Collection<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlProtoEquipment> xmlEquipmentTypes = new HashSet<XmlProtoEquipment>();
			for (ProtoEquipment protoEq : protoEquipments) {
				if (!protoEq.getType().equals(EquipmentType.BUG_136)) {
					XmlProtoEquipment xmlEquipmentType = XmlProtoEquipment.Factory.newInstance();
					protoEq.getXmlTransferable(xmlEquipmentType, AMFICOM_IMPORT, false);
					xmlEquipmentTypes.add(xmlEquipmentType);
				}
			}
			condition2 = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE); 
			Collection<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlEquipment> xmlEquipments = new HashSet<XmlEquipment>();
			for (Equipment eq : equipments) {
				XmlEquipment xmlEquipment = XmlEquipment.Factory.newInstance();
				eq.getXmlTransferable(xmlEquipment, AMFICOM_IMPORT, false);
				xmlEquipments.add(xmlEquipment);
			}
			File configFile = new File(fileName);
			saveConfigXML(configFile, xmlLinkTypes, xmlCableLinkTypes, xmlPortTypes, xmlEquipmentTypes, xmlEquipments);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}		
	}
	
	private void saveConfigXML(File f, 
			Set<XmlLinkType> xmlLinkTypes, 
			Set<XmlCableLinkType> xmlCableLinkTypes,
			Set<XmlPortType> xmlPortTypes,
			Set<XmlProtoEquipment> xmlProtoEquipments, 
			Set<XmlEquipment> xmlEquipments) {
		System.out.println("Start saving config XML");
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/config/xml", "config");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.newInstance(xmlOptions);
		XmlIdentifier uid = doc.addNewId();
		uid.setStringValue(AMFICOM_IMPORT + "_config_library");
		doc.setName(AMFICOM_IMPORT + " types");
		doc.setCodename(AMFICOM_IMPORT + " types");
		doc.setImportType(AMFICOM_IMPORT);

		XmlLinkTypeSeq xmlLinkTypeSeq = doc.addNewLinkTypes();
		xmlLinkTypeSeq.setLinkTypeArray(xmlLinkTypes.toArray(new XmlLinkType[xmlLinkTypes.size()]));
		
		XmlCableLinkTypeSeq xmlCableLinkTypeSeq = doc.addNewCableLinkTypes();
		xmlCableLinkTypeSeq.setCableLinkTypeArray(xmlCableLinkTypes.toArray(new XmlCableLinkType[xmlCableLinkTypes.size()]));
		
		XmlPortTypeSeq xmlPortTypeSeq = doc.addNewPortTypes();
		xmlPortTypeSeq.setPortTypeArray(xmlPortTypes.toArray(new XmlPortType[xmlPortTypes.size()]));
		
		XmlProtoEquipmentSeq xmlEquipmentTypeSeq = doc.addNewProtoEquipments();
		xmlEquipmentTypeSeq.setProtoEquipmentArray(xmlProtoEquipments.toArray(new XmlProtoEquipment[xmlProtoEquipments.size()]));
		
		XmlEquipmentSeq xmlEquipmentSeq = doc.addNewEquipments();
		xmlEquipmentSeq.setEquipmentArray(xmlEquipments.toArray(new XmlEquipment[xmlEquipments.size()]));

		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
				Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
			} catch(IOException e) {
				Log.errorMessage(e);
			}
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}		
	}
}

