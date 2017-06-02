package me.viniciuspiedade.springboot1.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id",
		  scope=Image.class)
public class Image {

	@Id
	@GeneratedValue
	private long id;
	private String type;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JsonBackReference
	@JoinColumn(name="product_id")
	private Product product;
	
	public Image() {
		
	}
	
	public Image(long id, String type, Product product) {
		super();
		this.id = id;
		this.type = type;
		this.product = product;
	}
	
	public Image(String type, Product product) {
		super();
		this.type = type;
		this.product = product;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
