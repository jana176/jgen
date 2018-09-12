package model;

import javax.persistence.*;
import ID;
import BOOK_NAME;
import BOOK_AUTHOR;
import DATE_OF_PUBLICATION;
import LENT;
import PERSON_ID;

@Entity
@Table(name = "BOOKS")
public class Books {
	
			@Id
		@Column(name = "ID", nullable = false,  unique = true, length = 10)
		public INT ID; 
		@Column(name = "BOOK_NAME",  unique = true, length = 100)
		public VARCHAR BOOK_NAME; 
		@Column(name = "BOOK_AUTHOR",  length = 100)
		public VARCHAR BOOK_AUTHOR; 
		@Column(name = "DATE_OF_PUBLICATION",  length = 10)
		public DATE DATE_OF_PUBLICATION; 
		@Column(name = "LENT",  length = 0)
		public BIT LENT; 
		@Column(name = "PERSON_ID",  length = 10)
		public INT PERSON_ID; 

		
}