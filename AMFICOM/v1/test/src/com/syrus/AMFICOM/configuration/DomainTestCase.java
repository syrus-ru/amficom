/*
 * Created on 10.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.com.syrus.AMFICOM.configuration;

import java.security.DomainCombiner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.Object;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @author max
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DomainTestCase extends ConfigureTestCase {
    
    public DomainTestCase(String name) {
    	super(name);
	}
    
    public static void main(java.lang.String[] args) {
        Class clazz = DomainTestCase.class;
        junit.awtui.TestRunner.run(clazz);
//      junit.swingui.TestRunner.run(clazz);
//      junit.textui.TestRunner.run(clazz);
    }

    public static Test suite() {
        return suiteWrapper(DomainTestCase.class);
    }
    
    public void _testCreation() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, IllegalDataException {
    	
        Domain domain = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.creatorId,"DomainTestCase", "domain created by DomainTestCase" );
        Domain domain2 = Domain.getInstance((Domain_Transferable)
                domain.getTransferable());
        Domain domain3 = new Domain(domain2.getId());
        assertEquals(domain.getId(), domain2.getId());
        assertEquals(domain.getId(), domain3.getId());
        
    }
    
    public void _testPreparedStatement() throws UpdateObjectException, CreateObjectException, IllegalDataException {
    	
        List storableObjects = new ArrayList();
        Domain parantDomain = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Test1", "upDomain" );
        
        Domain domainToSet1 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Test1", "domain1" );
        Domain domainToSet2 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Teste2", "domain2" );
        Domain domainToSet3 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId,
                "Test3", "domain3" );
        storableObjects.add(domainToSet1);
        storableObjects.add(domainToSet2);
        storableObjects.add(domainToSet3);
        
        DomainDatabase database = (DomainDatabase) ConfigurationDatabaseContext.getDomainDatabase();
        database.insert(storableObjects);
        
        String sql = new String("UPDATE Domain SET id = ? , modified = ? , creator_id = ? , modifier_id = ? , domain_id = ? , name = ? , description = ? WHERE id = ?");
            
        PreparedStatement preparedStatement = null;
        
        Connection connection = DatabaseConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            Log.debugMessage("Domain Database.updateEntities | Trying: " + sql,
                                Log.DEBUGLEVEL09);
            for (Iterator it = storableObjects.iterator(); it.hasNext();) {
                Domain domain = (Domain) it.next();
                int i=1;
                preparedStatement.setTimestamp(i++, new Timestamp(domain.getCreated().getTime()));
                preparedStatement.setTimestamp(i++, new Timestamp(domain.getModified().getTime()));
                DatabaseIdentifier.setIdentifier(preparedStatement, i++ , domain.getCreatorId());
                DatabaseIdentifier.setIdentifier(preparedStatement, i++ , domain.getModifierId());
                DatabaseIdentifier.setIdentifier(preparedStatement, i++, domainId);
                preparedStatement.setString( i++, domain.getName());
                preparedStatement.setString( i++, domain.getDescription());
                DatabaseIdentifier.setIdentifier(preparedStatement, i++, domain.getId());
                
                Log.debugMessage("Domain " + "Database.updateEntities | Inserting  "
                        + domain.getName(), Log.DEBUGLEVEL09);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException sqle) {
            String mesg = "Domain " + "Database.updateEntities | Cannot update "
                    + this.getName() + sqle.getMessage();
            throw new UpdateObjectException(mesg, sqle);
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException sqle1) {
                Log.errorException(sqle1);
            } finally{
                DatabaseConnection.releaseConnection(connection);
            }
        }
    }
    
    public void testInsertListGetID() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, UpdateObjectException, IllegalDataException, VersionCollisionException, AMFICOMRemoteException {
        
        List list = new ArrayList();
        List idList = new ArrayList();
        Domain parantDomain = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Test1", "upDomain" );
        
        Domain domainToSet1 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Test1", "domain1" );
        Domain domainToSet2 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId, 
                "Teste2", "domain2" );
        Domain domainToSet3 = Domain.createInstance(ConfigureTestCase.creatorId, ConfigureTestCase.domainId,
                "Test3", "domain3" );
        idList.add(domainToSet1.getId());
        idList.add(domainToSet2.getId());
        idList.add(domainToSet3.getId());
        CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
        List charTypeList = database.retrieveAll();
        if (charTypeList == null || charTypeList.isEmpty()) {
        	assertFalse("no types found", true);
        }
        
        CharacteristicType characteristicType1 = (CharacteristicType) charTypeList.get(0);
        CharacteristicType characteristicType2 = (CharacteristicType) charTypeList.get(1);
        
        Characteristic characteristic1OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test1", "Char1", 0, "test1", domainToSet1.getId() , true, true);
        
        Characteristic characteristic2OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test2", "Char2", 0, "test2", domainToSet1.getId() , true, true);
        
        Characteristic characteristic3OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType2, "Test3", "Char3", 0, "test3", domainToSet1.getId() , true, true);
        
        Characteristic characteristic4OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType2, "Test4", "Char4", 0, "test4", domainToSet1.getId() , true, true);
        
        Characteristic characteristic1OfDomain2 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test1", "Char1", 0, "test1", domainToSet2.getId() , true, true);
        
        Characteristic characteristic2OfDomain2 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test2", "Char2", 0, "test2", domainToSet2.getId() , true, true);
        
        Characteristic characteristic3OfDomain2 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test3", "Char3", 0, "test3", domainToSet2.getId() , true, true);
        
        Characteristic characteristic1OfDomain3 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test1", "Char1", 0, "test1", domainToSet3.getId() , true, true);
        // Test1. Insert list of objects
        List charList1 = new LinkedList();
        charList1.add(characteristic1OfDomain1);
        charList1.add(characteristic2OfDomain1);
        charList1.add(characteristic3OfDomain1);
        charList1.add(characteristic4OfDomain1);
        List charList2 = new LinkedList();
        charList2.add(characteristic3OfDomain2);
        List charList3 = new LinkedList();
        
        domainToSet1.setCharacteristics(charList1);
        domainToSet2.setCharacteristics(charList2);
        domainToSet3.setCharacteristics(null);
        
        list.add(domainToSet1);
        list.add(domainToSet2);
        list.add(domainToSet3);
        DomainDatabase domainDatabase = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();  
        domainDatabase.insert(list);        
        
        List testList = domainDatabase.retrieveByIds(idList, null);
        System.out.println("testList.size()" + testList.size());
        for (Iterator it = testList.iterator(); it.hasNext();) {
			Domain domain = (Domain) it.next();
            System.out.println("domain.getCharacteristics().size() = " + domain.getCharacteristics().size());			
		}
        //  Test2. Update list
        charList1 = new LinkedList();
        charList1.add(characteristic3OfDomain1);
        charList2 = new LinkedList();
        charList2.add(characteristic1OfDomain2);
        charList2.add(characteristic2OfDomain2);
        charList3 = new LinkedList();
        charList3.add(characteristic1OfDomain3);
        
        domainToSet1.setCharacteristics(charList1);
        domainToSet2.setCharacteristics(charList2);
        domainToSet3.setCharacteristics(charList3);
        
        //domainToSet1.set
        
        domainDatabase.update(list, StorableObjectDatabase.UPDATE_FORCE, null);
        
        testList = domainDatabase.retrieveByIds(idList, null);
        System.out.println("testList.size()" + testList.size());
        for (Iterator it = testList.iterator(); it.hasNext();) {
            Domain domain = (Domain) it.next();
            System.out.println("domain.getCharacteristics().size() = " + domain.getCharacteristics().size());           
        }
//        //Test3. Delete
//        domainDatabase.delete(idList);
//        
//        testList = domainDatabase.retrieveByIds(idList, null);
//        System.out.println("testList.size()" + testList.size());
//        for (Iterator it = testList.iterator(); it.hasNext();) {
//            Domain domain = (Domain) it.next();
//            System.out.println("domain.getCharacteristics().size() = " + domain.getCharacteristics().size());           
//        }
        
    }

    public void _testInsertListGetList() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, IllegalDataException {
        List listIds = new ArrayList();
        List list = new ArrayList();
        List listToCompare = new ArrayList();
        Identifier id1 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet1 = Domain.createInstance(ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain1 created by DomainTestCase" );
        Identifier id2 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet2 = Domain.createInstance(ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain2 created by DomainTestCase" );
        Identifier id3 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet3 = Domain.createInstance(ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain3 created by DomainTestCase" );
        listIds.add(id1);
        listIds.add(id2);
        listIds.add(id3);
        list.add(domainToSet1);
        list.add(domainToSet2);
        list.add(domainToSet3);
        
        DomainDatabase domainDatabase = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        domainDatabase.insert(list);
        listToCompare = domainDatabase.retrieveByIds(listIds, null);
        for (Iterator it = listToCompare.iterator(); it.hasNext();) {
			Domain tempDomain = (Domain) it.next();
            System.out.println("!!!"+tempDomain.getDescription()+"!!!");		
		}
        
        //assertTrue(listIds.contains(id2));
        //assertEquals(list, listToCompare);
        
    }
    
    public void _testSingleUpdate() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, UpdateObjectException, IllegalDataException, VersionCollisionException {
        List list = new ArrayList();
        Identifier id1 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet1 = Domain.createInstance(id1, ConfigureTestCase
                .creatorId, 
                "Test1", "domain1" );        
        CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
        List charTypeList = database.retrieveAll();
        if (charTypeList == null || charTypeList.isEmpty()) {
            assertFalse("no types found", true);
        }
        
        CharacteristicType characteristicType1 = (CharacteristicType) charTypeList.get(0);
        
        Identifier charId1 = IdentifierGenerator.generateIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
        Characteristic characteristic1OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test1", "Char1", 0, "test1", id1 , true, true);
        Identifier charId2 = IdentifierGenerator.generateIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
        Characteristic characteristic2OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test2", "Char2", 0, "test2", id1 , true, true);
        Identifier charId3 = IdentifierGenerator.generateIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
        Characteristic characteristic3OfDomain1 = Characteristic.createInstance(ConfigureTestCase.creatorId, characteristicType1, "Test3", "Char3", 0, "test3", id1 , true, true);                
        List charList1 = new LinkedList();
        charList1.add(characteristic2OfDomain1);
        domainToSet1.setCharacteristics(charList1);
        
        DomainDatabase domainDatabase = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        domainDatabase.update(domainToSet1, -2 ,null);
        Domain testDomain = new Domain(id1);
        System.out.println("testDomain.getCharacteristics().size() = " + testDomain.getCharacteristics().size());
        for (Iterator it = testDomain.getCharacteristics().iterator(); it.hasNext();) {
            Characteristic characteristic = (Characteristic) it.next();
            System.out.println("characteristic.getDescription()" + characteristic.getDescription());            
        }
        testDomain.setCharacteristics(null);
        domainDatabase.update(testDomain, -2 ,null);
        testDomain = new Domain(id1);
        System.out.println("testDomain.getCharacteristics().size() = " + testDomain.getCharacteristics().size());
        for (Iterator it = testDomain.getCharacteristics().iterator(); it.hasNext();) {
            Characteristic characteristic = (Characteristic) it.next();
            System.out.println("characteristic.getDescription()" + characteristic.getDescription());            
        }
        charList1.add(characteristic1OfDomain1);
        charList1.add(characteristic3OfDomain1);
        testDomain.setCharacteristics(charList1);
        domainDatabase.update(testDomain, -2 ,null);
        testDomain = new Domain(id1);
        System.out.println("testDomain.getCharacteristics().size() = " + testDomain.getCharacteristics().size());
        for (Iterator it = testDomain.getCharacteristics().iterator(); it.hasNext();) {
            Characteristic characteristic = (Characteristic) it.next();
            System.out.println("characteristic.getDescription()" + characteristic.getDescription());            
        }        
    }


}
//SELECT id , TO_CHAR(created, 'YYYYMMDD HH24MISS') created , TO_CHAR(modified, 'YYYYMMDD HH24MISS') modified , creator_id , modifier_id , domain_id , name , description FROM Domain WHERE 1=1 AND  ( id IN  ( 'Domain_1338' , 'Domain_1340' , 'Domain_1342' )  )
//SELECT id , TO_CHAR(created, 'YYYYMMDD HH24MISS') created , TO_CHAR(modified, 'YYYYMMDD HH24MISS') modified , creator_id , modifier_id , domain_id , name , description FROM Domain WHERE 1=1 AND  ( id IN  ( 'Domain_1338' , 'Domain_1340' , 'Domain_1342' )  )
