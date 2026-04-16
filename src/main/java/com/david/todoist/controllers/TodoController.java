package com.david.todoist.controllers;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.david.todoist.auth.expt_handling.UnprocessableBodyException;
import com.david.todoist.models.Todo;
import com.david.todoist.models.TodoList;
import com.david.todoist.repos.ListRepo;
import com.david.todoist.repos.TodoRepo;
import com.david.todoist.services.ListService;
import com.david.todoist.services.TodoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/app")
public class TodoController {
  @Autowired
  private TodoService todoService;
  @Autowired
  private ListRepo listRepo;
  @Autowired
  private ListService listService;

  @PostMapping("/addTodo")
  public Todo addTodo(@RequestBody Todo todo) {
    try {
      if (todo.getTitle() == null || todo.getTitle().isBlank()) {
        throw new UnprocessableBodyException("Todo title is required");
      }
      if (todo.getTodoList() == null || todo.getTodoList().getId() == 0) {
        throw new UnprocessableBodyException("Todo must belong to a TodoList");
      }

      long listId = todo.getTodoList().getId();
      TodoList existingList = listRepo.findById(listId)
          .orElseThrow(() -> new NoSuchElementException("TodoList not found: " + listId));
      todo.setTodoList(existingList);
      return todoService.save(todo);
    } catch (NoSuchElementException e) {
      throw new UnprocessableBodyException(e.getMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "An error occurred while processing the request");
    }
  }

  @PostMapping("/editTodo")
  public Todo editTodo(@RequestBody Todo entity) {
    todoService.save(entity);
    return entity;
  }

  @PostMapping("/editList")
  public TodoList editList(@RequestBody TodoList entity) {
    listService.save(entity);
    return entity;
  }
  

  @DeleteMapping("/delTodo")
  public void deleteTodo(@RequestParam("id") long id) {
    System.out.println(id);
    todoService.delete(id);
  }

  @PostMapping("/addList")
  public TodoList addList(@RequestBody TodoList list) {
    return listRepo.save(list);
  }

  @DeleteMapping("/delList")
  public void deleteList(@RequestParam("id") long id) {
    listService.deleteById(id);
  }

  @GetMapping("/allTodos")
  public Collection<Todo> getAllTodos() {
    return todoService.findAll();
  }
  @GetMapping("/allLists")
  public List<TodoList> getMethodName() {
      return listService.findAll();
  }
  
}
