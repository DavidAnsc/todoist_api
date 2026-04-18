package com.david.todoist.models;

import com.david.todoist.enums.Priorities;

/*{
    "title": "read american prometheus",
    "description": "my second todo",
    "priority": "LOW",
    "status": false,
    "todoList": {
        "id": 1
    }
} */

public class TodoDTO {
  long id;

  String title;
  String description;
  Priorities priority;
  boolean status;
  TodoListDTOForTodo todoList;

  public TodoDTO() {
  }
  
  
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
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
  public boolean getStatus() {
    return status;
  }
  public void setStatus(boolean status) {
    this.status = status;
  }
  public TodoListDTOForTodo getTodoList() {
    return todoList;
  }
  public void setTodoList(TodoListDTOForTodo todoList) {
    this.todoList = todoList;
  }
}
