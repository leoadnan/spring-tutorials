package repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entity.EventAudit;
import entity.EventAuditKey;
 
@Repository
public interface AuditRepository extends CrudRepository<EventAudit, EventAuditKey> {
 
    @Query("select * from event_owner.event_audit")
    public List<EventAudit> eventAudit();
}