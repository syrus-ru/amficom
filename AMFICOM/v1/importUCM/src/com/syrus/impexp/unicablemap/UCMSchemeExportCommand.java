/*-
 * $Id: UCMSchemeExportCommand.java,v 1.1 2005/08/29 13:14:35 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.General.Command.ExportCommand;
import com.syrus.amficom.config.xml.CableLinkType;
import com.syrus.amficom.config.xml.CableLinkTypes;
import com.syrus.amficom.config.xml.ConfigurationLibrary;
import com.syrus.amficom.config.xml.EquipmentType;
import com.syrus.amficom.config.xml.EquipmentTypes;
import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.DirectionType;
import com.syrus.amficom.scheme.xml.Kind;
import com.syrus.amficom.scheme.xml.Scheme;
import com.syrus.amficom.scheme.xml.SchemeCableLink;
import com.syrus.amficom.scheme.xml.SchemeCableLinks;
import com.syrus.amficom.scheme.xml.SchemeElement;
import com.syrus.amficom.scheme.xml.SchemeElements;
import com.syrus.amficom.scheme.xml.Schemes;
import com.syrus.amficom.scheme.xml.SchemesDocument;
import com.syrus.impexp.unicablemap.objects.Cable;
import com.syrus.impexp.unicablemap.objects.CableThread;
import com.syrus.impexp.unicablemap.objects.CableType;
import com.syrus.impexp.unicablemap.objects.ChannelingItem;
import com.syrus.impexp.unicablemap.objects.Element;
import com.syrus.impexp.unicablemap.objects.MuffType;
import com.syrus.impexp.unicablemap.objects.Port;
import com.syrus.impexp.unicablemap.objects.ThreadType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/08/29 13:14:35 $
 * @module importUCM
 */

public class UCMSchemeExportCommand extends ExportCommand {
	UniCableMapDatabase ucmDatabase;
	
	HashMap<Integer, CableType> cabletypes = new HashMap<Integer, CableType>();
	HashMap<Integer, MuffType> eqtypes = new HashMap<Integer, MuffType>();
	HashMap<Integer, Object> muffs = new HashMap<Integer, Object>();
	HashMap<Integer, Object> buildings = new HashMap<Integer, Object>();
	HashMap<Integer, Object> cables = new HashMap<Integer, Object>();
	
	private long start;
	
	public UCMSchemeExportCommand(UniCableMapDatabase ucmDatabase) {
		this.ucmDatabase = ucmDatabase;
	}
	
	void parseObject(PrintWriter out, UniCableMapObject ucmObject) {
		out.println("Объект: " + ucmObject.text);
		out.println("\t x0 = " + ucmObject.x0 + ";\n\t y0 = " + ucmObject.y0 + ";\n\t x1 = " + ucmObject.x1 + ";\n\t y1 = " + ucmObject.y1 + ";\n\t dx = " + ucmObject.dx + ";\n\t dy = " + ucmObject.dy);
		out.println("\t msk = " + ucmObject.msk + ";\n\t ord = " + ucmObject.ord + ";\n\t state = " + ucmObject.state + ";\n\t un = " + ucmObject.un);
		out.println("\t Тип: " + ucmObject.typ.text);
		out.println("\t Параметры:");
		for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();) {
			UniCableMapParameter param = (UniCableMapParameter )it2.next();
			out.println("\t    " + param.realParameter.text + " = " + param.value);
		}
	}
	
	
	public void processTypeObjects() {
		Collection<UniCableMapObject> muffTypes = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_MUFF_TYPE));
		System.out.println(muffTypes.size() + " видов муфт прочитано");
		this.start = System.currentTimeMillis();
		createMuffTypes(muffTypes);
		System.out.println(muffTypes.size() + " EquimpentType созданно за " + (System.currentTimeMillis() - this.start) + " ms");

		Collection<UniCableMapObject> cableTypes = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_TYPE));
		System.out.println(cableTypes.size() + " типов кабеля прочитано");
		this.start = System.currentTimeMillis();
		createCableTypes(cableTypes);
		System.out.println(cableTypes.size() + " EquimpentType созданно за " + (System.currentTimeMillis() - this.start) + " ms");
	}
	
	public void processSchemeObjects() throws SQLException {
		Collection<UniCableMapObject> buildings1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_BUILDING_PLAN));
		System.out.println(buildings1.size() + " зданий прочитано");
		this.start = System.currentTimeMillis();
		createBuildings(buildings1);
		System.out.println(buildings1.size() + " SchemeElement созданно за " + (System.currentTimeMillis() - this.start) + " ms");
		
		Collection<UniCableMapObject> muffs1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_MUFF));
		System.out.println(muffs1.size() + " муфт прочитано");
		this.start = System.currentTimeMillis();
		createMuffs(muffs1);
		System.out.println(muffs1.size() + " SchemeElement созданно за " + (System.currentTimeMillis() - this.start) + " ms");
		
		Collection<UniCableMapObject> cables1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_LINEAR));
		System.out.println(cables1.size() + " кабелей прочитано");
		this.start = System.currentTimeMillis();
		createCables(cables1);
		System.out.println(cables1.size() + " Cable созданно за " + (System.currentTimeMillis() - this.start) + " ms");
	}
	
	void createCables(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject ucmObject : objects) {
			Cable cable = new Cable(Integer.toString(ucmObject.un));
			this.cables.put(ucmObject.un, cable);
			
			cable.setName(ucmObject.text);
			int fibers = 0;
			
			for(UniCableMapParameter param : ucmObject.buf.params) {
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_FIBRES)) {
					fibers = Integer.parseInt(param.value);
				}
			}
			
			CableType type = null;
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
					 cable.setTypeId(ucmLink.parent.un);
					 if (fibers != 0) {
						 if (type == null) {
							 type = this.cabletypes.get(Integer.valueOf(ucmLink.parent.un));
						 }
						 if (type != null) {
							 type.setThreadNum(fibers);
						 } else {
							 System.err.println("type not found with id = " + ucmLink.parent.un);
						 }
					 }
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
					 cable.setCodenameId(ucmLink.parent.un);
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
					UniCableMapObject startObj = ucmLink.parent;
					cable.setStartPortId(getFreePort(cable, DirectionType.IN, startObj));
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
					UniCableMapObject endObj = ucmLink.parent;
					cable.setEndPortId(getFreePort(cable, DirectionType.OUT, endObj));
				} 
			}
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CABLE_LAYOUT)) {
					UniCableMapObject razrez = ucmLink.child;  
					cable.setLayoutId(razrez.un); 
					if (cable.getThreads().isEmpty()) {
						createThreads(type, cable, razrez);
					}
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
					ChannelingItem item = createChannelingItem(ucmLink.child);
					cable.addChannelingItem(item);
				}
			}
		}
	}
	
	void createThreads(CableType type, Cable cable, UniCableMapObject razrez) throws SQLException {
		int fiberCount = 0;
		
		for(UniCableMapLink ucmLink2 : this.ucmDatabase.getChildren(razrez)) {
			if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject fiber = ucmLink2.child;

				CableThread thread = new CableThread(cable.getId() + "thread" + (++fiberCount));
				thread.setName(fiber.text);
				cable.addCableThread(thread);
				
				for(UniCableMapLink ucmLink4 : this.ucmDatabase.getChildren(fiber)) {
					if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
						UniCableMapObject nextFiber = ucmLink4.child;
						thread.setSourcePortId(nextFiber.un);
					} else if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
						UniCableMapObject previosFiber = ucmLink4.child;
						thread.setTargetPortId(previosFiber.un);
					}
				}
				
				if (type != null) {
					ThreadType tt = new ThreadType(type.getId() + "threadType" + fiberCount);
					tt.setCableTypeId(type.getId());
					type.addThreadType(tt);
					thread.setThreadTypeId(tt.getId());
					
					for(UniCableMapParameter param : fiber.buf.params) {
						if(param.realParameter.text.equals(UniCableMapParameter.UCM_MUNBER_RO_MANDATORY)) {
							String codename = param.value;
							tt.setCodename(codename);
							break;
						}
					}
					for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(fiber)) {
						if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_TYPE_REALIZATION)) {
							tt.setLinkTypeId(ucmLink4.parent.un);
							break;
						}
					}
				} else {
					System.err.println("cable type not found. can't create CTT's");
				}
			}
		}
	}
	
	ChannelingItem createChannelingItem(UniCableMapObject ucmObject) throws SQLException {  // место кабеля
		ChannelingItem item = new ChannelingItem(ucmObject.un);
		
		for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
						
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject truba = ucmLink.parent;
				for(UniCableMapLink ucmLink1 : this.ucmDatabase.getParents(truba)) {
					if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
						UniCableMapObject block = ucmLink1.parent;
						int pox = 0;
						int poy = 0;
						for(UniCableMapParameter param : block.buf.params) {
							if (param.realParameter.text.equals(UniCableMapParameter.UCM_X)) {
								pox = Integer.parseInt(param.value);
							} else if (param.realParameter.text.equals(UniCableMapParameter.UCM_Y)) {
								poy = Integer.parseInt(param.value);
							}
						}
						int seq = truba.un - block.un;
						item.setRowX(seq / poy == 0 ? 1 : poy);
						item.setPlaceY(seq % pox == 0 ? seq : pox);
						for(UniCableMapLink ucmLink2 : this.ucmDatabase.getParents(block)) {
							if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
								UniCableMapObject razrez = ucmLink2.parent;
								for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(razrez)) {
									if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
										UniCableMapObject tunnel = ucmLink3.parent;
										item.setTunnelId(tunnel.un);
										
										for(UniCableMapParameter param : tunnel.buf.params) {
											if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
												String dval = param.value.replace(',', '.');
												item.setLength(Double.parseDouble(dval));
											}
										}
										
										for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(tunnel)) {
											if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
												item.setStartSiteId(ucmLink4.parent.un);
											} else if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
												item.setEndSiteId(ucmLink4.parent.un);
											}
										}
										break;
									}
								}
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		return item;
	}
	
	void createMuffTypes(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject ucmObject : objects) {
			MuffType type = new MuffType(ucmObject.un);
			type.setName(ucmObject.text);
			this.eqtypes.put(ucmObject.un, type);
		}
	}

	void createCableTypes(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject ucmObject : objects) {
			CableType type = new CableType(ucmObject.un);
			type.setName(ucmObject.text);
			this.cabletypes.put(ucmObject.un, type);
		}
	}
	
	void createMuffs(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject ucmObject : objects) {
			Element muf = new Element(ucmObject.un);
			muf.setName(ucmObject.text);
			this.muffs.put(ucmObject.un, muf);
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_TYPE_REALIZATION)) {
					 muf.setEqtId(ucmLink.parent.un);
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
					muf.setCodename("MUFF_STRAIGHT");
//					 muf.setCodename(Integer.toString(ucmLink.parent.un));
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					 muf.setWellId(ucmLink.parent.un);
				}
			}
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
					 muf.addInputPort(ucmLink.child.un);
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
					 muf.addOutputPort(ucmLink.child.un);
				}
			}
		}
	}
		
	void createBuildings(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject plan : objects) {
			Element building = new Element(plan.un);
			building.setWellId(plan.un);
			building.setName(plan.text);
			this.buildings.put(plan.un, building);
			building.setCodename("SCHEMED");
		}
	}
	
	private String getFreePort(Cable cable, DirectionType.Enum directionType, UniCableMapObject obj) throws SQLException {
		if (obj.typ.text.equals(UniCableMapType.UCM_MUFF)) {
			Element muff = (Element)this.muffs.get(obj.un);
			Collection<Port> outPorts = muff.getPorts(directionType);
			for (Port port : outPorts) {
				if (!port.isConnected()) {
					port.setConnected(true);
					return port.getId();
				}
				System.out.println("Port already connected");
			}
		} else if (obj.typ.text.equals(UniCableMapType.UCM_CABLE_INLET)) {
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(obj)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					 Element building = (Element)this.buildings.get(ucmLink.parent.un);
					 Port port;
					 if (directionType.equals(DirectionType.IN)) {
						 port = building.addInputPort(obj.un);
						 port.setName(obj.text);
					 } else {
						 port = building.addOutputPort(obj.un);
						 port.setName(obj.text);
					 }
					 return port.getId();
				}
			}
		} else {
			System.out.println("Cable " + cable.getName() + " connected to " + obj.typ.text + obj.text + " [" + obj.un + "]");
			return null;
		}
		System.err.println("No free port found for " + obj.text);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void saveConfigXML(String fileName) throws SQLException {
		Log.debugMessage("Start saving config XML", Level.FINE);
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/config/xml", "config");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		ConfigurationLibrary doc = ConfigurationLibrary.Factory.newInstance(xmlOptions);
		UID uid = doc.addNewUid();
		uid.setStringValue("1");
		doc.setName("UCM types");
		doc.setCodename("UCM types");
		doc.setImporttype("ucm");
		
		CableLinkTypes xmlCableLinkTypes = doc.addNewCablelinktypes();
		EquipmentTypes xmlEquipmentTypes = doc.addNewEquipmenttypes();

		Collection<CableLinkType> lts = new ArrayList<CableLinkType>(this.cabletypes.size());
		for (CableType cableType : this.cabletypes.values()) {
			lts.add(cableType.toXMLObject());
		}
		xmlCableLinkTypes.setCablelinktypeArray(lts.toArray(new CableLinkType[lts.size()]));
		
		Collection<EquipmentType> eqts = new ArrayList<EquipmentType>(this.eqtypes.size());
		for (MuffType eqType : this.eqtypes.values()) {
			eqts.add(eqType.toXMLObject());
		}
		xmlEquipmentTypes.setEquipmenttypeArray(eqts.toArray(new EquipmentType[eqts.size()]));

		Log.debugMessage("Проверка на валидность XML", Level.FINE);
		this.start = System.currentTimeMillis();
		boolean isXmlValid = validateXml(doc);
		System.out.println("проверка завершена за " + (System.currentTimeMillis() - this.start) + " ms");
		if(isXmlValid) {
			File f = new File(fileName);

			Log.debugMessage("Save XML to file", Level.FINE);
			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nXML Instance Document saved at : "
					+ f.getPath());
			Log.debugMessage("Done successfully", Level.FINE);
		} else {
			Log.debugMessage("Done with errors", Level.FINE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveSchemeXML(String fileName) {
		Log.debugMessage("Start saving scheme XML", Level.FINE);
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "scheme");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemesDocument doc = SchemesDocument.Factory.newInstance(xmlOptions);
		
		Schemes xmlSchemes = doc.addNewSchemes();
		Scheme xmlScheme = xmlSchemes.addNewScheme();
		UID uid = xmlScheme.addNewUid();
		uid.setStringValue("1");
		
		xmlScheme.setName("UCM top scheme");
		xmlScheme.setDescription("");
		xmlScheme.setKind(Kind.NETWORK);
		xmlScheme.setWidth(BigInteger.valueOf(420));
		xmlScheme.setHeight(BigInteger.valueOf(297));
		
		
		SchemeElements xmlSchemeElements = xmlScheme.addNewSchemeelements();
		SchemeCableLinks xmlSchemeCableLinks = xmlScheme.addNewSchemecablelinks();
//		SchemeLinks xmlSchemeLinks = xmlScheme.addNewSchemelinks();

		this.start = System.currentTimeMillis();
		Collection<SchemeElement> ses = new ArrayList<SchemeElement>(this.muffs.size() + this.buildings.size());
		for (Object muff : this.muffs.values()) {
			ses.add(((Element)muff).toXMLObject());
		}
		for (Object building : this.buildings.values()) {
			ses.add(((Element)building).toXMLObject());
		}
		System.out.println(ses.size() + " XMLSchemeElement созданно за " + (System.currentTimeMillis() - this.start) + " ms");
		xmlSchemeElements.setSchemeelementArray(ses.toArray(new SchemeElement[ses.size()]));

		this.start = System.currentTimeMillis();
		Collection<SchemeCableLink> cls = new ArrayList<SchemeCableLink>(this.cables.size());
		for (Object cable : this.cables.values()) {
			cls.add(((Cable)cable).toXMLObject());
		}
		System.out.println(cls.size() + " XMLSchemeCableLink созданно за " + (System.currentTimeMillis() - this.start) + " ms");
		xmlSchemeCableLinks.setSchemecablelinkArray(cls.toArray(new SchemeCableLink[cls.size()]));

		
		File f = new File(fileName);

		Log.debugMessage("Save XML to file", Level.FINE);
		try {
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			System.out.println("\nXML Instance Document saved at : " + f.getPath());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Log.debugMessage("Проверка на валидность XML", Level.FINE);
		this.start = System.currentTimeMillis();
		boolean isXmlValid = validateXml(doc);
		System.out.println("проверка завершена за " + (System.currentTimeMillis() - this.start) + " ms");
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.FINE);			
		} else {
			Log.debugMessage("Done with errors", Level.FINE);
		}
		
	}
	
	private boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			System.out.println("Invalid XML: ");
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				System.out.println("");
				System.out.println(xml);
				System.out.println("");
				System.out.println(error.getMessage());
				System.out.println(error.getObjectLocation());
				System.out.println("Column " + error.getColumn());
				System.out.println("Line " + error.getLine());
				System.out.println("Offset " + error.getOffset());
				System.out.println("Object at cursor " + error.getCursorLocation().getObject());
				System.out.println("Source name " + error.getSourceName());
			}
		}
		return isXmlValid;
	}
	
	
	public void execute() {
		boolean survey = false;
		
		if (!survey) {
			try {
				processTypeObjects();
				processSchemeObjects();
//				saveConfigXML("\\export\\config.xml");
				saveSchemeXML("\\export\\scheme.xml");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		if (survey) {
			PrintWriter pw;
			try {
				File f = new File("/out.txt");
				FileOutputStream fos = new FileOutputStream(f);
				pw = new PrintWriter(fos);
			}
			catch (FileNotFoundException e) {
				pw = new PrintWriter(System.out);
			}		
			surveyObjects(pw, UniCableMapLinkType.UCM_FIBRE);
		}
	}
		
	void surveyObjects(PrintWriter pw, String type) {
		Collection objs = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(type));
		
		int i = 0;
		Iterator it2 = objs.iterator();
		while (it2.hasNext()) {
			if(i++ > 100)
				break;
		UniCableMapObject ucmObject = (UniCableMapObject)it2.next();
		{
			parseObject(pw, ucmObject);
			try {
				Collection children = this.ucmDatabase.getChildren(ucmObject);
				pw.println("Children:");
				for (Iterator it = children.iterator(); it.hasNext();) {
					// get link
					UniCableMapLink ucmChild = (UniCableMapLink)it.next();
					pw.println("Тип связи: " + ucmChild.mod);
					// parse child
					parseObject(pw, ucmChild.child);
				}
				
				Collection parents = this.ucmDatabase.getParents(ucmObject);
				pw.println("Parents:");
				for (Iterator it = parents.iterator(); it.hasNext();) {
					// get link
					UniCableMapLink ucmParent = (UniCableMapLink)it.next();
					pw.println("Тип связи: " + ucmParent.mod);
					// parse parent
					parseObject(pw, ucmParent.parent);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		}
		System.out.println(objs.size() + " done");
		pw.flush();
	}
}
