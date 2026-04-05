package com.david.todoist.models;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "list_id_seq")
    @SequenceGenerator(name = "list_id_seq", sequenceName = "list_id_seq")
    private long id;

    @OneToMany(mappedBy = "todoList")
    private Collection<Todo> todos;
    
    @Column(unique = true)
    private String title;

    private String icon;
    
    public Collection<Todo> getTodos() {
        return todos;
    }
    public void setTodos(Collection<Todo> todos) {
        this.todos = todos;
    }
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setId(long id) {
      this.id = id;
    }
}
