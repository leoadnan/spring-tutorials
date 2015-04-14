package dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import entity.EventAudit;
import entity.EventAuditKey;

public interface EventAuditDao{

	public void save(EventAudit entity);

	public void save(Collection<EventAudit> entities) ;
	
	public EventAudit find(EventAuditKey eventAuditKey);
	
	public List<EventAudit> getAll() ;
	
	public List<EventAudit> getListEventAuditMonthCategoryWise(Date date,String Category) ;
}