package com.ikeda.todolist.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "places")
public class Place {
	@Id
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Setter
	@Column(nullable = false)
	private String name;
}
