package me.viniciuspiedade.springboot1.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import me.viniciuspiedade.springboot1.util.View;

@Entity
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id",
		  scope=Product.class)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Product {

	@Id
	@GeneratedValue
	@JsonView(View.Summary.class)
	private Long id;
	
	@Column
	@JsonView(View.Summary.class)
	private String name;
	
	@Column
	@JsonView(View.Summary.class)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn(name="parent_id")
	//@JsonBackReference
	@JsonIdentityInfo(
			  generator = ObjectIdGenerators.PropertyGenerator.class, 
			  property = "id")
	private Product parent;
	
	@OneToMany(mappedBy="parent", orphanRemoval = true, cascade=CascadeType.ALL)
	private Set<Product> children = new HashSet<Product>();
	
	@OneToMany(mappedBy="product", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Image> images = new HashSet<Image>();
	
	public Product() {
		
	}
	
	public Product(Long id, String name, String description, Product parent) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parent = parent;
	}
	
	public Product(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Product(String name, String description, Product parent) {
		super();
		this.name = name;
		this.description = description;
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Product getParent() {
		return parent;
	}
	
	public void setParent(Product parent) {
		this.parent = parent;
	}

	public Set<Image> getImages() {
		return images;
	}
	
	public Set<Product> getChildren() {
		return children;
	}
	
	
}
