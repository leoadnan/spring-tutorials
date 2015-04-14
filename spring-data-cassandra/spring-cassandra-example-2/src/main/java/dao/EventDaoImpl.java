package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.AuditRepository;

import com.google.common.collect.Lists;

import entity.EventAudit;
import entity.EventAuditKey;

@Component
public class EventDaoImpl implements EventAuditDao {

	@Autowired
	public AuditRepository auditRepository;

	@Override
	public void save(EventAudit entity) {
		auditRepository.save(entity);

	}

	@Override
	public void save(Collection<EventAudit> entities) {
		auditRepository.save(entities);
	}

	@Override
	public EventAudit find(EventAuditKey eventAuditKey) {
		if (null == eventAuditKey) {
			throw new IllegalArgumentException("It doesn't has all required instance variable set !");
		}
		return auditRepository.findOne(eventAuditKey);
	}

	@Override
	public List<EventAudit> getAll() {
		Iterable<EventAudit> iterable = auditRepository.findAll();
		if (null != iterable.iterator()) {
			return Lists.newArrayList(iterable.iterator());
		}
		return new ArrayList<>();
	}

	@Override
	public List<EventAudit> getListEventAuditMonthCategoryWise(Date date, String Category) {
		// TODO Auto-generated method stub
		return null;
	}
}