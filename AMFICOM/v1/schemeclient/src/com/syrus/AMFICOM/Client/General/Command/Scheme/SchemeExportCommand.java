/*-
 * $Id: SchemeExportCommand.java,v 1.2 2005/09/20 10:42:03 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlConfigurationLibrary;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentSeq;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlPortType;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeSeq;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.SchemeProtoGroupsDocument;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroupSeq;
import com.syrus.util.Log;

public class SchemeExportCommand extends AbstractCommand {
	SchemeTabbedPane pane;
	static final String importType = "AMFICOM";
	static final String protosFileName = "/export/protos.xml";
	static final String configFileName = "/export/config.xml";
	
	public SchemeExportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		// get top level protoGroup
		try {
//			StorableObjectCondition condition1 = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER, ObjectEntities.SCHEMEPROTOGROUP_CODE); 
//			Collection<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
//			Set<XmlSchemeProtoGroup> xmlProtoGroups = new HashSet<XmlSchemeProtoGroup>();
//			for (SchemeProtoGroup protoGroup : groups) {
//				XmlSchemeProtoGroup xmlProto = XmlSchemeProtoGroup.Factory.newInstance();
//				xmlProtoGroups.add(protoGroup.getXmlTransferable(xmlProto, importType));
//			}
//			saveProtoGroupsXML(protosFileName, xmlProtoGroups);

			StorableObjectCondition condition2 = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE); 
			Collection<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlLinkType> xmlLinkTypes = new HashSet<XmlLinkType>();
			for (LinkType linkType : linkTypes) {
				XmlLinkType xmlLinkType = XmlLinkType.Factory.newInstance();
				linkType.getXmlTransferable(xmlLinkType, importType);
				xmlLinkTypes.add(xmlLinkType);
			}
			
//		 TODO remove comment
			Set<XmlCableLinkType> xmlCableLinkTypes = Collections.emptySet();
//			condition2 = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE); 
//			Collection<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
//			Set<XmlCableLinkType> xmlCableLinkTypes = new HashSet<XmlCableLinkType>();
//			for (CableLinkType linkType : cableLinkTypes) {
//				XmlCableLinkType xmlCableLinkType = XmlCableLinkType.Factory.newInstance();
//				linkType.getXmlTransferable(xmlCableLinkType, importType)
//				xmlCableLinkTypes.add(xmlCableLinkType);
//			}
			condition2 = new EquivalentCondition(ObjectEntities.PORT_TYPE_CODE); 
			Collection<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlPortType> xmlPortTypes = new HashSet<XmlPortType>();
			for (PortType portType : portTypes) {
				XmlPortType xmlPortType = XmlPortType.Factory.newInstance();
				portType.getXmlTransferable(xmlPortType, importType);
				xmlPortTypes.add(xmlPortType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE); 
			Collection<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlEquipmentType> xmlEquipmentTypes = new HashSet<XmlEquipmentType>();
			for (EquipmentType eqType : equipmentTypes) {
				XmlEquipmentType xmlEquipmentType = XmlEquipmentType.Factory.newInstance();
				eqType.getXmlTransferable(xmlEquipmentType, importType);
				xmlEquipmentTypes.add(xmlEquipmentType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE); 
			Collection<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlEquipment> xmlEquipments = new HashSet<XmlEquipment>();
			for (Equipment eq : equipments) {
				XmlEquipment xmlEquipment = XmlEquipment.Factory.newInstance();
				eq.getXmlTransferable(xmlEquipment, importType);
				xmlEquipments.add(xmlEquipment);
			}
			
			saveConfigXML(protosFileName, xmlLinkTypes, xmlCableLinkTypes, xmlPortTypes, xmlEquipmentTypes, xmlEquipments);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}		
	}
	
	private void saveProtoGroupsXML(String fileName, Set<XmlSchemeProtoGroup> xmlProtoGroups) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "schemeProtoGroups");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemeProtoGroupsDocument doc = SchemeProtoGroupsDocument.Factory.newInstance(xmlOptions);
		
		XmlSchemeProtoGroupSeq xmlProtoGroupSeq = doc.addNewSchemeProtoGroups();
		xmlProtoGroupSeq.setSchemeProtoGroupArray(xmlProtoGroups.toArray(new XmlSchemeProtoGroup[xmlProtoGroups.size()]));
		
		File f = new File(fileName);

		try {
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = ClientUtils.validateXml(doc);
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}
	}

	private void saveConfigXML(String fileName, 
			Set<XmlLinkType> xmlLinkTypes, 
			Set<XmlCableLinkType> xmlCableLinkTypes,
			Set<XmlPortType> xmlPortTypes,
			Set<XmlEquipmentType> xmlEquipmentTypes, 
			Set<XmlEquipment> xmlEquipments) {
		System.out.println("Start saving config XML");
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/config/xml", "config");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.newInstance(xmlOptions);
		XmlIdentifier uid = doc.addNewId();
		uid.setStringValue(importType + "_config_library");
		doc.setName(importType + " types");
		doc.setCodename(importType + " types");
		doc.setImportType(importType);

		XmlLinkTypeSeq xmlLinkTypeSeq = doc.addNewLinkTypes();
		xmlLinkTypeSeq.setLinkTypeArray(xmlLinkTypes.toArray(new XmlLinkType[xmlLinkTypes.size()]));
		
		// TODO remove comment
//		XmlCableLinkTypeSeq xmlCableLinkTypeSeq = doc.addNewCableLinkTypes();
//		xmlCableLinkTypeSeq.setCableLinkTypeArray(xmlCableLinkTypes.toArray(new XmlCableLinkType[xmlCableLinkTypes.size()]));
		
		XmlPortTypeSeq xmlPortTypeSeq = doc.addNewPortTypes();
		xmlPortTypeSeq.setPortTypeArray(xmlPortTypes.toArray(new XmlPortType[xmlPortTypes.size()]));
		
		XmlEquipmentTypeSeq xmlEquipmentTypeSeq = doc.addNewEquipmentTypes();
		xmlEquipmentTypeSeq.setEquipmentTypeArray(xmlEquipmentTypes.toArray(new XmlEquipmentType[xmlEquipmentTypes.size()]));
		
		XmlEquipmentSeq xmlEquipmentSeq = doc.addNewEquipments();
		xmlEquipmentSeq.setEquipmentArray(xmlEquipments.toArray(new XmlEquipment[xmlEquipments.size()]));

		try {
			File f = new File(fileName);
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = ClientUtils.validateXml(doc);
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}
	}
}
