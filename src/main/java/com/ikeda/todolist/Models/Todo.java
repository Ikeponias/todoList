package com.ikeda.todolist.Models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "todos")
public class Todo {
	@Id
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Setter
	@Column(nullable = false)
	private boolean done;

	@Setter
	@Column(nullable = false)
	private String task;

	@Setter
	@JsonIgnore
	@ManyToOne
	private Todo parent;

	@Setter
	@JsonIgnore
	@OneToMany(mappedBy = "parent")
	private List<Todo> children;

	@Column
	@Setter
	@JsonIgnore
	@Version
	private int version;

	public void switchDone() {
		done = !done;
	}
}
