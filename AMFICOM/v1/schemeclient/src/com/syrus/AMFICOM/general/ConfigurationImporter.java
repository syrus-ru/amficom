/*-
 * $Id: ConfigurationImporter.java,v 1.1.2.1 2006/05/18 17:50:01 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.io.Importer;
import com.syrus.io.IntelStreamReader;
import com.syrus.util.Log;

public class ConfigurationImporter extends Importer {
	static final String OPTICAL = "optical";
	static final String OPERATIONAL = "operational";
	static final String ELECTRICAL = "electrical";
	static final String INTERFACE = "interface";
	static final String VISUAL = "visual";
	static final String SPLICE = "splice";
	
	static final String CROSS = "cross";
	static final String MUFF = "mufta";
	static final String FILTER = "filter";
	static final String RECEIVER = "receiver";
	static final String TRANSMITTER = "transmitter";
	static final String TESTER = "tester";
	static final String MULTIPLEXOR = "multiplexor";
	static final String SWITCH = "switch";
	
	static final String DOUBLE = "double";
	static final String INTEGER = "integer";
	static final String TIME = "time";
	static final String STRING = "String";
	
	static final String CHARACTERISTIC = "characteristic";
	static final String CHARACTERISTIC_TYPE = "characteristictype";
	static final String EQUIPMENT = "equipment";
	static final String EQUIPMENT_TYPE = "equipmenttype";
	static final String LINK = "link";
	static final String LINK_TYPE = "linktype";
	static final String CABLELINK = "cablelink";
	static final String CABLELINK_TYPE = "cablelinktype";
	static final String PORT = "port";
	static final String PORT_TYPE = "porttype";
	
	private static Map<String, Identifier> idsMapping = new HashMap<String, Identifier>();
	private static Map<Identifier, StorableObject> objects = new HashMap<Identifier, StorableObject>();
	
	public static Set<StorableObject> loadTypes (String typeDir) throws CreateObjectException {
		idsMapping.clear();
		objects.clear();
		
		File[] files = new File(typeDir).listFiles();
		if (files == null) {
			Collections.emptySet();
		}
		
		// first of all read characteristicTypes
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.startsWith(CHARACTERISTIC) && !files[i].isDirectory()) {
				loadCharacteristicTypes(files[i]);
			}
		}
		// next read other types
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.startsWith(EQUIPMENT) && !files[i].isDirectory()) {
				loadEquipmentTypes(files[i]);
			} else if (filename.startsWith(LINK) && !files[i].isDirectory()) {
				loadLinkTypes(files[i]);
			} else if (filename.startsWith(CABLELINK) && !files[i].isDirectory()) {
				loadCableLinkTypes(files[i]);
			} else if (filename.startsWith(PORT) && !files[i].isDirectory()) {
				loadPortTypes(files[i]);
			}
		}
		return new HashSet<StorableObject>(objects.values());
	}
	
	private static void loadCharacteristicTypes(File file) throws CreateObjectException {
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CHARACTERISTIC_TYPE)) {
				throw new CreateObjectException("Unknown file type " + file.getName());
			}
			// skip one empty string
			isr.readASCIIString();
			
			String id = null;
			String name = null;
			String description = null;
			CharacteristicTypeSort sort = null;
			DataType dataType = null;
			
			CharacteristicType ch;
			while (isr.ready()) {
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals("")) {
					ch = CharacteristicType.createInstance(
							LoginManager.getUserId(),
							name,
							description,
							name,
							dataType,
							sort);
					idsMapping.put(id, ch.getId());
					objects.put(ch.getId(), ch);
					ch = null;
				} else if (s[0].equals("@name")) {
					name = s[1];
				} else if (s[0].equals("@id")) {
					id = s[1];
				} else if (s[0].equals("@description")) {
					description = s[1];
				} else if (s[0].equals("@ch_class")) {
					String chClass = s[1];
					if (chClass.equals(OPTICAL)) {
						sort = CharacteristicTypeSort.OPTICAL;
					} else if (chClass.equals(OPERATIONAL)) {
						sort = CharacteristicTypeSort.OPERATIONAL;
					} else if (chClass.equals(ELECTRICAL)) {
						sort = CharacteristicTypeSort.ELECTRICAL;
					} else if (chClass.equals(INTERFACE)) {
						sort = CharacteristicTypeSort.INTERFACE;
					} else if (chClass.equals(VISUAL)) {
						sort = CharacteristicTypeSort.VISUAL;
					} else {
						assert false : "Unsupported CharacteristicTypeSort " + chClass;
					}
				} else if (s[0].equals("@value_type_id")) {
					String valueTypeId = s[1];
					if (valueTypeId.equals(DOUBLE)) {
						dataType = DataType.DOUBLE;
					} else if (valueTypeId.equals(INTEGER)) {
						dataType = DataType.INTEGER;
					} else if (valueTypeId.equals(TIME)) {
						dataType = DataType.DATE;
					} else if (valueTypeId.equals(STRING)) {
						dataType = DataType.STRING;
					} else {
						dataType = DataType.RAW;
					}
				}
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			throw new CreateObjectException(e);
		}
	}
	
	private static void loadEquipmentTypes(File file) throws CreateObjectException {
		try {
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(EQUIPMENT_TYPE)) {
				throw new CreateObjectException("Unknown file type " + file.getName());
			}
			// skip one empty string
			isr.readASCIIString();

			String id = null;
			String name = null;
			String description = null;
			String manufacturer = null;
			String manufacturerCode = null;
			EquipmentType eqt = null;
			
			ProtoEquipment protoEquipment = null;
			while (isr.ready()) {
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals("")) {
					if (eqt == null) {
						Log.debugMessage("Skip creation of ProtoEquipment as EQT is null for " + name, Level.FINER);
					} else {
						if (protoEquipment == null) {
							protoEquipment = ProtoEquipment.createInstance(LoginManager.getUserId(),
									eqt.getId(),
									name,
									description,
									manufacturer,
									manufacturerCode);
						}
						idsMapping.put(id, protoEquipment.getId());
						objects.put(protoEquipment.getId(), protoEquipment);
						protoEquipment = null;
					}
				} else if (s[0].equals("@name")) {
					name = s[1];
				} else if (s[0].equals("@id")) {
					id = s[1];
				} else if (s[0].equals("@description")) {
					description = s[1];
				} else if (s[0].equals("@manufacturer")) {
					manufacturer = s[1];
				} else if (s[0].equals("@manufacturer_code")) {
					manufacturerCode = s[1];
				} else if (s[0].equals("@eq_class")) {
					String eqClass = s[1];
					try {
						if (eqClass.equals(CROSS)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.CROSS.stringValue());
						} else if (eqClass.equals(MUFF)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.MUFF.stringValue());
						} else if (eqClass.equals(FILTER)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.FILTER.stringValue());
						} else if (eqClass.equals(MULTIPLEXOR)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.MULTIPLEXOR.stringValue());
						} else if (eqClass.equals(RECEIVER)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.RECEIVER.stringValue());
						} else if (eqClass.equals(SWITCH)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.OPTICAL_SWITCH.stringValue());
						} else if (eqClass.equals(TESTER)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.REFLECTOMETER.stringValue());
						} else if (eqClass.equals(TRANSMITTER)) {
							eqt = EquipmentType.valueOf(EquipmentTypeCodename.TRANSMITTER.stringValue());
						} else {
							eqt = null;
						}
					} catch (ApplicationException ae) {
						throw new CreateObjectException("Cannot load EquipmentType '" + eqClass + "'", ae);
					}
				} else if (s[0].equals("@characteristics")) {
					Set<Characteristic> characteristics = new HashSet<Characteristic>();
					if (eqt == null) {
						Log.debugMessage("Skip creation of ProtoEquipment as EQT is null for " + name, Level.FINER);
					} else {
						protoEquipment = ProtoEquipment.createInstance(LoginManager.getUserId(),
								eqt.getId(),
								name,
								description,
								manufacturer,
								manufacturerCode);
					}
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end")) {
						Identifier chTypeId = idsMapping.get(ch[0]);
						if (chTypeId == null) {
							throw new CreateObjectException("CharacteristicType not found for " + ch[0]);
						}
						
						if (protoEquipment != null) {
							CharacteristicType chType = (CharacteristicType)objects.get(chTypeId);
							Characteristic chr = Characteristic.createInstance(
									LoginManager.getUserId(),
									chType,
									chType.getName(),
									chType.getDescription(),
									ch[1],
									protoEquipment,
									true,
									true);
							characteristics.add(chr);
						}
						ch = analyseString(isr.readASCIIString());
					}
					
					if (protoEquipment != null) {
						try {
							protoEquipment.setCharacteristics(characteristics);
						} catch (ApplicationException e) {
							throw new CreateObjectException(e);
						}
					}
				}
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			throw new CreateObjectException(e);
		}
	}
	
	private static void loadLinkTypes(File file) throws CreateObjectException {
		try {
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(LINK_TYPE)) {
				throw new CreateObjectException("Unknown file type " + file.getName());
			}
			// skip one empty string
			isr.readASCIIString();

			String id = null;
			String name = null;
			String codename = null;
			String description = null;
			String manufacturer = null;
			String manufacturerCode = null;
			Identifier imageId = Identifier.VOID_IDENTIFIER;
			
			LinkType linkType = null;
			while (isr.ready()) {
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals("")) {
					if (linkType == null) {
						final Set<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(
								new TypicalCondition(codename, OPERATION_EQUALS, LINK_TYPE_CODE, COLUMN_CODENAME),
								true);
						
						if (linkTypes.isEmpty()) {
							linkType = LinkType.createInstance(
									LoginManager.getUserId(), 
									codename, 
									description, 
									name, 
									LinkTypeSort.LINKTYPESORT_OPTICAL,
									manufacturer, 
									manufacturerCode,
									imageId);
						} else {
							linkType = linkTypes.iterator().next();
							linkType.setDescription(description);
							linkType.setName(name);
							linkType.setManufacturer(manufacturer);
							linkType.setManufacturerCode(manufacturerCode);
						}
					}
					idsMapping.put(id, linkType.getId());
					objects.put(linkType.getId(), linkType);
					linkType = null;
				} else if (s[0].equals("@name")) {
					name = s[1];
				} else if (s[0].equals("@codename")) {
					codename = s[1];
				} else if (s[0].equals("@id")) {
					id = s[1];
				} else if (s[0].equals("@description")) {
					description = s[1];
				} else if (s[0].equals("@manufacturer")) {
					manufacturer = s[1];
				} else if (s[0].equals("@manufacturer_code")) {
					manufacturerCode = s[1];
				} else if (s[0].equals("@characteristics")) {
					Set<Characteristic> characteristics = new HashSet<Characteristic>();

					final Set<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(codename, OPERATION_EQUALS, LINK_TYPE_CODE, COLUMN_CODENAME),
							true);
					
					if (linkTypes.isEmpty()) {
						linkType = LinkType.createInstance(
								LoginManager.getUserId(), 
								codename, 
								description, 
								name, 
								LinkTypeSort.LINKTYPESORT_OPTICAL,
								manufacturer, 
								manufacturerCode,
								imageId);
					} else {
						linkType = linkTypes.iterator().next();
						linkType.setDescription(description);
						linkType.setName(name);
						linkType.setManufacturer(manufacturer);
						linkType.setManufacturerCode(manufacturerCode);
					}

					
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end")) {
						Identifier chTypeId = idsMapping.get(ch[0]);
						if (chTypeId == null) {
							throw new CreateObjectException("CharacteristicType not found for " + ch[0]);
						}
						CharacteristicType chType = (CharacteristicType)objects.get(chTypeId);
						
						Characteristic chr = Characteristic.createInstance(
								LoginManager.getUserId(),
								chType,
								chType.getName(),
								chType.getDescription(),
								ch[1],
								linkType,
								true,
								true);
						characteristics.add(chr);
						ch = analyseString(isr.readASCIIString());
					}
					
					try {
						linkType.setCharacteristics(characteristics);
					} catch (ApplicationException e) {
						throw new CreateObjectException(e);
					}
				}
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			throw new CreateObjectException(e);
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	private static void loadCableLinkTypes(File file) throws CreateObjectException {
		try {
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CABLELINK_TYPE)) {
				throw new CreateObjectException("Unknown file type " + file.getName());
			}
			// skip one empty string
			isr.readASCIIString();

			String id = null;
			String name = null;
			String codename = null;
			String description = null;
			String manufacturer = null;
			String manufacturerCode = null;
			Identifier imageId = Identifier.VOID_IDENTIFIER;
			
			CableLinkType linkType = null;
			while (isr.ready()) {
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals("")) {
					if (linkType == null) {
						final Set<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(
								new TypicalCondition(codename, OPERATION_EQUALS, CABLELINK_TYPE_CODE, COLUMN_CODENAME),
								true);
						
						if (cableLinkTypes.isEmpty()) {
							linkType = CableLinkType.createInstance(
									LoginManager.getUserId(), 
									codename, 
									description, 
									name, 
									LinkTypeSort.LINKTYPESORT_OPTICAL,
									manufacturer, 
									manufacturerCode,
									imageId);
						} else {
							linkType = cableLinkTypes.iterator().next();
							linkType.setDescription(description);
							linkType.setName(name);
							linkType.setManufacturer(manufacturer);
							linkType.setManufacturerCode(manufacturerCode);
						}
					}
					idsMapping.put(id, linkType.getId());
					objects.put(linkType.getId(), linkType);
					linkType = null;
				} else if (s[0].equals("@name")) {
					name = s[1];
				} else if (s[0].equals("@codename")) {
					codename = s[1];
				} else if (s[0].equals("@id")) {
					id = s[1];
				} else if (s[0].equals("@description")) {
					description = s[1];
				} else if (s[0].equals("@manufacturer")) {
					manufacturer = s[1];
				} else if (s[0].equals("@manufacturer_code")) {
					manufacturerCode = s[1];
				} else if (s[0].equals("@characteristics")) {
					Set<Characteristic> characteristics = new HashSet<Characteristic>();

					final Set<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(codename, OPERATION_EQUALS, CABLELINK_TYPE_CODE, COLUMN_CODENAME),
							true);
					
					if (cableLinkTypes.isEmpty()) {
						linkType = CableLinkType.createInstance(
								LoginManager.getUserId(), 
								codename, 
								description, 
								name, 
								LinkTypeSort.LINKTYPESORT_OPTICAL,
								manufacturer, 
								manufacturerCode,
								imageId);
					} else {
						linkType = cableLinkTypes.iterator().next();
						linkType.setDescription(description);
						linkType.setName(name);
						linkType.setManufacturer(manufacturer);
						linkType.setManufacturerCode(manufacturerCode);
					}
					
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end")) {
						Identifier chTypeId = idsMapping.get(ch[0]);
						if (chTypeId == null) {
							throw new CreateObjectException("CharacteristicType not found for " + ch[0]);
						}
						CharacteristicType chType = (CharacteristicType)objects.get(chTypeId);
						
						Characteristic chr = Characteristic.createInstance(
								LoginManager.getUserId(),
								chType,
								chType.getName(),
								chType.getDescription(),
								ch[1],
								linkType,
								true,
								true);
						characteristics.add(chr);
						ch = analyseString(isr.readASCIIString());
					}
					
					try {
						linkType.setCharacteristics(characteristics);
					} catch (ApplicationException e) {
						throw new CreateObjectException(e);
					}
				}
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			throw new CreateObjectException(e);
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
	
	private static void loadPortTypes(File file) throws CreateObjectException {
		try {
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(PORT_TYPE)) {
				throw new CreateObjectException("Unknown file type " + file.getName());
			}
			// skip one empty string
			isr.readASCIIString();

			String id = null;
			String name = null;
			String codename = null;
			String description = null;
			PortTypeSort sort = null;
			
			PortType portType = null;
			while (isr.ready()) {
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals("")) {
					if (portType == null) {
						final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(
								new TypicalCondition(codename, OPERATION_EQUALS, PORT_TYPE_CODE, COLUMN_CODENAME),
								true);
						
						if (portTypes.isEmpty()) {
							portType = PortType.createInstance(
									LoginManager.getUserId(), 
									codename, 
									description, 
									name, 
									sort,
									PortTypeKind.PORT_KIND_SIMPLE);	
						} else {
							portType = portTypes.iterator().next();
						}
					}
					idsMapping.put(id, portType.getId());
					objects.put(portType.getId(), portType);
					portType = null;
				} else if (s[0].equals("@name")) {
					name = s[1];
				} else if (s[0].equals("@codename")) {
					codename = s[1];
				} else if (s[0].equals("@id")) {
					id = s[1];
				} else if (s[0].equals("@description")) {
					description = s[1];
				} else if (s[0].equals("@p_class")) {
					String pClass = s[1];
					if (pClass.equals(OPTICAL)) {
						sort = PortTypeSort.PORTTYPESORT_OPTICAL;
					} else if (pClass.equals(ELECTRICAL)) {
						sort = PortTypeSort.PORTTYPESORT_ELECTRICAL;
					} else if (pClass.equals(SPLICE)) {
						sort = PortTypeSort.PORTTYPESORT_THERMAL;
					} else {
						throw new CreateObjectException("Unknown PortTypeSort " + pClass);
					}
				} else if (s[0].equals("@characteristics")) {
					Set<Characteristic> characteristics = new HashSet<Characteristic>();

					final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(codename, OPERATION_EQUALS, PORT_TYPE_CODE, COLUMN_CODENAME),
							true);
					
					if (portTypes.isEmpty()) {
						portType = PortType.createInstance(
								LoginManager.getUserId(), 
								codename, 
								description, 
								name, 
								sort,
								PortTypeKind.PORT_KIND_SIMPLE);	
					} else {
						portType = portTypes.iterator().next();
					}
					
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end")) {
						Identifier chTypeId = idsMapping.get(ch[0]);
						if (chTypeId == null) {
							throw new CreateObjectException("CharacteristicType not found for " + ch[0]);
						}
						CharacteristicType chType = (CharacteristicType)objects.get(chTypeId);
						
						Characteristic chr = Characteristic.createInstance(
								LoginManager.getUserId(),
								chType,
								chType.getName(),
								chType.getDescription(),
								ch[1],
								portType,
								true,
								true);
						characteristics.add(chr);
						ch = analyseString(isr.readASCIIString());
					}
					
					try {
						portType.setCharacteristics(characteristics);
					} catch (ApplicationException e) {
						throw new CreateObjectException(e);
					}
				}
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			throw new CreateObjectException(e);
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}
}
