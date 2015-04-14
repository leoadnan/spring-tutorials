package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class EventAuditKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "category", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String category;

	@PrimaryKeyColumn(name = "month", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private Date month;

	@PrimaryKeyColumn(name = "cat_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	private String categoryId;

	@PrimaryKeyColumn(name = "version", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID version;

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public UUID getVersion() {
		return version;
	}

	public void setVersion(UUID version) {
		this.version = version;
	}

}