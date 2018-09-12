package model;

import javax.persistence.*;
import ID;
import FIRST_NAME;
import LAST_NAME;
import CITY;
import ADDRESS;
import GENDER;

@Entity
@Table(name = "PERSONS")
public class Persons {
	
			@Id
		@Column(name = "ID", nullable = false,  unique = true, length = 10)
		public INT ID; 
		@Column(name = "FIRST_NAME",  length = 50)
		public VARCHAR FIRST_NAME; 
		@Column(name = "LAST_NAME",  length = 50)
		public VARCHAR LAST_NAME; 
		@Column(name = "CITY",  length = 100)
		public VARCHAR CITY; 
		@Column(name = "ADDRESS",  length = 100)
		public VARCHAR ADDRESS; 
		@Column(name = "GENDER",  length = 6)
		public ENUM GENDER; 

		
}