/*
 * Created on 10.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.com.syrus.AMFICOM.configuration;

import java.security.DomainCombiner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.omg.CORBA.Object;

import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

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
    
    public void testCreation() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, IllegalDataException {
    	Identifier id = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domain = Domain.createInstance(id, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase",
                "domain created by DomainTestCase" );
        Domain domain2 = Domain.getInstance((Domain_Transferable)
                domain.getTransferable());
        Domain domain3 = new Domain(domain2.getId());
        assertEquals(domain.getId(), domain2.getId());
        assertEquals(domain.getId(), domain3.getId());
        
    }
    
    public void testInsertListGetID() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, IllegalDataException {
        List list = new ArrayList();
        Identifier id1 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet1 = Domain.createInstance(id1, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain1 created by DomainTestCase" );
        Identifier id2 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet2 = Domain.createInstance(id2, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain2 created by DomainTestCase" );
        Identifier id3 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet3 = Domain.createInstance(id3, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain3 created by DomainTestCase" );
        
        list.add(domainToSet1);
        list.add(domainToSet2);
        list.add(domainToSet3);
        DomainDatabase domainDatabase = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        domainDatabase.insert(list);
        Domain domainToGet1 = new Domain(id1);
        domainDatabase.retrieve(domainToGet1);
        assertEquals(domainToGet1.getDescription(), domainToSet1.getDescription());
        
    }

    public void testInsertListGetList() throws IllegalObjectEntityException, IdentifierGenerationException, ObjectNotFoundException, RetrieveObjectException, CreateObjectException, IllegalDataException {
        List listIds = new ArrayList();
        List list = new ArrayList();
        List listToCompare = new ArrayList();
        Identifier id1 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet1 = Domain.createInstance(id1, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain1 created by DomainTestCase" );
        Identifier id2 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet2 = Domain.createInstance(id2, ConfigureTestCase
                .creatorId, ConfigureTestCase.domainId ,
                "DomainTestCase", "domain2 created by DomainTestCase" );
        Identifier id3 = IdentifierGenerator.
                generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
        Domain domainToSet3 = Domain.createInstance(id3, ConfigureTestCase
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


}
