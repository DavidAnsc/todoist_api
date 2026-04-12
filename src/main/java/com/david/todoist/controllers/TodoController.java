package com.david.todoist.controllers;

import java.util.Collection;
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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/app")
public class TodoController {
    @Autowired
    private TodoRepo todoRepo;
    @Autowired
    private ListRepo listRepo;
    @Autowired
    private ListService listService;


    @PostMapping("/addTodo")
    public Todo addTodo(@RequestBody Todo todo) {
      try {
        if (todo.getTodoList() != null) {
            long listId = todo.getTodoList().getId();
            TodoList existingList = listRepo.findById(listId)
                    .orElseThrow(() -> new NoSuchElementException("TodoList not found: " + listId));
            todo.setTodoList(existingList);
        }
        return todoRepo.save(todo);
      } catch (NoSuchElementException e) {
        throw new UnprocessableBodyException(e.getMessage());
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
      }
    }

    @DeleteMapping("/delTodo")
    public void deleteTodo(@RequestParam long id) {
        todoRepo.deleteById(id);
    }

    @PostMapping("/addList")
    public TodoList addList(@RequestBody TodoList list) {
        return listRepo.save(list);
    }

    @DeleteMapping("/delList")
    public void deleteList(@RequestParam String title) {
      listService.deleteByTitle(title);
    }

    @GetMapping("/allTodos")
    public Collection<Todo> getAllTodos() {
        return todoRepo.findAll();
    }

    @GetMapping("/todos")
    public Collection<Todo> getTodosByList(@RequestParam(name = "title") String listTitle) {
        return listRepo.findByTitle(listTitle).getTodos();
    }
    
    
}
