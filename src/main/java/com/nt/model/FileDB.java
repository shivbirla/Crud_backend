package com.nt.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "files")
public class FileDB{

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "id")
	  private Long id;
	  
		@Column(name = "name")
		private String name;

	@Column(name ="type")
	  private String type;

//	  @Transient
//	  private byte[] data;
	  
//	  @Transient
//	  private String path;



	@Override
	public String toString() {
		return "FileDB [id=" + id + ", name=" + name + ", type=" + type + "]";
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public FileDB(String path, String name, String type) {
//		super();
//		this.path = path;
//		this.name = name;
//		this.type = type;
//	}
	public FileDB() {
	}

	public FileDB( String name, String type) {
		this.name = name;
		this.type = type;
		
	}


  }

