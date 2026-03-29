package com.david.todoist.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.todoist.models.Todo;
import com.david.todoist.models.TodoList;
import com.david.todoist.repos.ListRepo;
import com.david.todoist.repos.TodoRepo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/app")
public class TodoController {
    @Autowired
    private TodoRepo todoRepo;
    @Autowired
    private ListRepo listRepo;


    @PostMapping("/addTodo")
    public Todo addTodo(@RequestBody Todo todo) {
        return todoRepo.save(todo);
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
        listRepo.deleteByTitle(title);
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
