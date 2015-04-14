package test;

 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.AuditRepository;

import com.datastax.driver.core.utils.UUIDs;

import conf.AuditCoreContextConfig;
import dao.EventDaoImpl;
import entity.EventAudit;
import entity.EventAuditKey;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AuditCoreContextConfig.class })
@Category(IntegrationTest.class)
public class EventDaoImplTest {
 
    @Autowired
    public EventDaoImpl eventDaoImpl;
 
    @Autowired
    private AuditRepository auditRepository;
 
    @Test
    public void testSaveEventAudit() {
        EventAuditKey eventAuditKey= new EventAuditKey();
        EventAudit expectedEventAudit = new EventAudit();
 
        eventAuditKey.setCategory("commitcode");
        eventAuditKey.setCategoryId("9000");
        eventAuditKey.setMonth(new Date());//Need to convert into Month
        eventAuditKey.setVersion(UUIDs.timeBased()); //Return pseudo randomly generated cryptography UUID.
 
        expectedEventAudit.setEventAuditKey(eventAuditKey);
        expectedEventAudit.setActionType("SAVEDRAFT");
        expectedEventAudit.setContent("{_id:9000}"); //JSON or any other String content
        expectedEventAudit.setUserId("rsrivastava");
 
        //Saving Data in Cassandra
        eventDaoImpl.save(expectedEventAudit);
        EventAudit actualEventAudit = auditRepository.findOne(eventAuditKey);
        assertEquals(expectedEventAudit.getEventAuditKey(), actualEventAudit.getEventAuditKey());
    }
 
    @Test
    public void testSaveCollectionOfEventAudit() {
        fail("Not yet implemented");
    }
 
    @Test
    public void testFind() {
        fail("Not yet implemented");
    }
 
    @Test
    public void testListEventAuditMonthCategoryWise(){
 
    }
 
    @Test
    public void testGetAll() {
        fail("Not yet implemented");
    }
}