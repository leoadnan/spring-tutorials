package entity;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "event_audit")
public class EventAudit {

	@PrimaryKey
	private EventAuditKey eventAuditKey;

	@Column(value = "userid")
	private String userId;

	@Column(value = "action")
	private String actionType;

	@Column(value = "content")
	private String content;

	public EventAuditKey getEventAuditKey() {
		return eventAuditKey;
	}

	public void setEventAuditKey(EventAuditKey eventAuditKey) {
		this.eventAuditKey = eventAuditKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}