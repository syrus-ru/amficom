/*-
 * $Id: SchemeExportCommand.java,v 1.3 2005/09/20 19:47:52 stas Exp $
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

import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
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

public class SchemeExportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public SchemeExportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
		
		// get top level protoGroup
		try {
			StorableObjectCondition condition1 = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER, ObjectEntities.SCHEMEPROTOGROUP_CODE); 
			Collection<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			Set<XmlSchemeProtoGroup> xmlProtoGroups = new HashSet<XmlSchemeProtoGroup>();
			for (SchemeProtoGroup protoGroup : groups) {
				XmlSchemeProtoGroup xmlProto = XmlSchemeProtoGroup.Factory.newInstance();
				protoGroup.getXmlTransferable(xmlProto, AMFICOM_IMPORT);
				xmlProtoGroups.add(xmlProto);
			}
			final File protoFile = new File(exportDirectory + separator + protoElementsFileName);
			saveProtoGroupsXML(protoFile, xmlProtoGroups);

			StorableObjectCondition condition2 = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE); 
			Collection<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlLinkType> xmlLinkTypes = new HashSet<XmlLinkType>();
			for (LinkType linkType : linkTypes) {
				XmlLinkType xmlLinkType = XmlLinkType.Factory.newInstance();
				linkType.getXmlTransferable(xmlLinkType, AMFICOM_IMPORT);
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
				portType.getXmlTransferable(xmlPortType, AMFICOM_IMPORT);
				xmlPortTypes.add(xmlPortType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE); 
			Collection<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlEquipmentType> xmlEquipmentTypes = new HashSet<XmlEquipmentType>();
			for (EquipmentType eqType : equipmentTypes) {
				XmlEquipmentType xmlEquipmentType = XmlEquipmentType.Factory.newInstance();
				eqType.getXmlTransferable(xmlEquipmentType, AMFICOM_IMPORT);
				xmlEquipmentTypes.add(xmlEquipmentType);
			}
			condition2 = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE); 
			Collection<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			Set<XmlEquipment> xmlEquipments = new HashSet<XmlEquipment>();
			for (Equipment eq : equipments) {
				XmlEquipment xmlEquipment = XmlEquipment.Factory.newInstance();
				eq.getXmlTransferable(xmlEquipment, AMFICOM_IMPORT);
				xmlEquipments.add(xmlEquipment);
			}
			File configFile = new File(exportDirectory + separator + configurationFileName);
			saveConfigXML(configFile, xmlLinkTypes, xmlCableLinkTypes, xmlPortTypes, xmlEquipmentTypes, xmlEquipments);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}		
	}
	
	private void saveProtoGroupsXML(File f, Set<XmlSchemeProtoGroup> xmlProtoGroups) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "schemeProtoGroups");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemeProtoGroupsDocument doc = SchemeProtoGroupsDocument.Factory.newInstance(xmlOptions);
		
		XmlSchemeProtoGroupSeq xmlProtoGroupSeq = doc.addNewSchemeProtoGroups();
		xmlProtoGroupSeq.setSchemeProtoGroupArray(xmlProtoGroups.toArray(new XmlSchemeProtoGroup[xmlProtoGroups.size()]));
		
		try {
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}
	}

	private void saveConfigXML(File f, 
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
		uid.setStringValue(AMFICOM_IMPORT + "_config_library");
		doc.setName(AMFICOM_IMPORT + " types");
		doc.setCodename(AMFICOM_IMPORT + " types");
		doc.setImportType(AMFICOM_IMPORT);

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

		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
				Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
			} catch(IOException e) {
				Log.errorException(e);
			}
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}		
	}
}
