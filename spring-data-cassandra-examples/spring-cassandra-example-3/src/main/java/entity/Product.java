package entity;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class Product {

	@PrimaryKeyColumn(name = "upc_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private Long upcId;
	
	@Column(value="cat_id")
	private Integer catId;
	
	@Column(value="j4u_cat")
	private String j4uCat;
	
	@Column(value="upc_dsc")
	private String upcDsc;

	public Product() {
	}

	public Product(Long upcId, Integer catId, String j4uCat, String upcDsc) {
		this.upcId = upcId;
		this.catId = catId;
		this.j4uCat = j4uCat;
		this.upcDsc = upcDsc;
	}

	
	public Product(Long upcId, String upcDsc) {
		this.upcId = upcId;
		this.upcDsc = upcDsc;
	}

	public Product(Long upcId) {
		super();
		this.upcId = upcId;
	}

	public Long getUpcId() {
		return upcId;
	}

	public void setUpcId(Long upcId) {
		this.upcId = upcId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getJ4uCat() {
		return j4uCat;
	}

	public void setJ4uCat(String j4uCat) {
		this.j4uCat = j4uCat;
	}

	public String getUpcDsc() {
		return upcDsc;
	}

	public void setUpcDsc(String upcDsc) {
		this.upcDsc = upcDsc;
	}

	@Override
	public String toString() {
		return "Product [upcId=" + upcId + ", catId=" + catId + ", j4uCat="	+ j4uCat + ", upcDsc=" + upcDsc + "]";
	}

}
