package com.david.todoist.models;

import com.david.todoist.enums.Priorities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_id_seq")
    @SequenceGenerator(name = "todo_id_seq", allocationSize = 1, sequenceName = "todo_id_seq")
    private long id;

    private String title;
    private String description;
    private Priorities priority;
    private boolean status; // true for finished

    @ManyToOne
    @JoinColumn(name = "todolist_id", referencedColumnName = "id")
    private TodoList todoList;


    public Todo() {
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Priorities getPriority() {
        return priority;
    }
    public void setPriority(Priorities priority) {
        this.priority = priority;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public TodoList getTodoList() {
        return todoList;
    }
    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }
}
