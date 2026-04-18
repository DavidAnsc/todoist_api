package com.david.todoist.controllers;

import com.david.todoist.auth.JWT.JwtService;
import com.david.todoist.auth.services.UserService;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.todoist.auth.AppUser;
import com.david.todoist.auth.expt_handling.UnprocessableBodyException;
import com.david.todoist.models.Todo;
import com.david.todoist.models.TodoDTO;
import com.david.todoist.models.TodoList;
import com.david.todoist.repos.ListRepo;
import com.david.todoist.services.ListService;
import com.david.todoist.services.TodoService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/app")
public class TodoController {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;
  @Autowired
  private TodoService todoService;
  @Autowired
  private ListRepo listRepo;
  @Autowired
  private ListService listService;


  
  
  @PostMapping("/editList")
  public TodoList editList(@RequestBody TodoList entity, HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    TodoList updatedList = listService.findById(entity.getId());

    entity.setUser(updatedList.getUser());

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);
    
    if (entity.getUser() == null) {
      throw new UnprocessableBodyException("TodoList must have a valid user");
    }
    if (!entity.getUser().getUsername().equals(jwtService.extractUsername(jwtToken))) {
      throw new UnprocessableBodyException("The user from the entity doesn't match the user on the jwt token.");
    }
    
    listService.save(entity);
    return entity;
  }
  
  @PostMapping("/editTodo")
  public Todo editTodo(@RequestBody TodoDTO todo, HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);

    long todoListId = todo.getTodoList().getId();
    TodoList todoList = listService.findById(todoListId);
    
    Todo newTodo = new Todo();
    newTodo.setId(todo.getId());
    newTodo.setDescription(todo.getDescription());
    newTodo.setPriority(todo.getPriority());
    newTodo.setStatus(todo.getStatus());
    newTodo.setTitle(todo.getTitle());
    newTodo.setTodoList(todoList);
    
    if (!jwtService.extractUsername(jwtToken).equals(newTodo.getTodoList().getUser().getUsername())) {
      throw new UnprocessableBodyException("JWT token's user doesn't match the user in the list");
    }

    return todoService.save(newTodo);
  }
  
  
  @PostMapping("/addTodo")
  public Todo addTodo(@RequestBody TodoDTO todo, HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);

    long todoListId = todo.getTodoList().getId();
    Todo newTodo = new Todo();
    TodoList todoList = listService.findById(todoListId);
    newTodo.setDescription(todo.getDescription());
    newTodo.setPriority(todo.getPriority());
    newTodo.setStatus(todo.getStatus());
    newTodo.setTitle(todo.getTitle());
    newTodo.setTodoList(todoList);

    if (!jwtService.extractUsername(jwtToken).equals(newTodo.getTodoList().getUser().getUsername())) {
      throw new UnprocessableBodyException("JWT token's user doesn't match the user in the list");
    }


    return todoService.save(newTodo);
  }
  @PostMapping("/addList")
  public TodoList addList(@RequestBody TodoList list, HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);
    System.out.println("usn: "+jwtService.extractUsername(jwtToken));
    AppUser user = userService.loadUserByUsername(jwtService.extractUsername(jwtToken));
    list.setUser(user);
    return listRepo.save(list);
  }


  
  @DeleteMapping("/delTodo")
  public void deleteTodo(@RequestParam long id, HttpServletRequest httpRequest) {
    System.out.println("HERE");
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);
    
    Todo todo = todoService.findById(id);
    if (todo == null) {
      throw new UnprocessableBodyException("Can't find todo.");
    }
    
    if (todo.getTodoList() == null || todo.getTodoList().getUser() == null) {
      throw new UnprocessableBodyException("Todo does not have a valid associated list.");
    }
    if (!todo.getTodoList().getUser().getUsername().equals(jwtService.extractUsername(jwtToken))) {
      throw new UnprocessableBodyException("You cannot delete a todo from other user.");
    }
    todoService.deleteById(id);
  }
  @DeleteMapping("/delList")
  public void deleteList(@RequestParam long id, HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);

    TodoList list = listService.findById(id);
    if (list == null) {
      throw new UnprocessableBodyException("Can't find todo list.");
    }

    if (list.getUser() == null) {
      throw new UnprocessableBodyException("Todo list does not have a valid user.");
    }
    if (!list.getUser().getUsername().equals(jwtService.extractUsername(jwtToken))) {
      throw new UnprocessableBodyException("You cannot delete a todo list from other user.");
    }
    listService.deleteById(id);
  }



  @GetMapping("/allTodos")
  public Collection<Todo> getAllTodos(HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);
    return todoService.findAllByUserId(getUserFromJWT(jwtToken).getId());
  }
  @GetMapping("/allLists")
  public List<TodoList> getAllLists(HttpServletRequest httpRequest) {
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnprocessableBodyException("Missing or invalid Authorization header");
    }
    String jwtToken = authHeader.substring(7);
    return listService.findAllByUserId(getUserFromJWT(jwtToken).getId());
  }



  private AppUser getUserFromJWT(String jwtToken) {
    String username = jwtService.extractUsername(jwtToken);
    AppUser user = userService.loadUserByUsername(username);
    return user;
  }
  
}
