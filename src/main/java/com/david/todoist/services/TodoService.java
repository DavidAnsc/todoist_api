package com.david.todoist.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.todoist.models.Todo;
import com.david.todoist.repos.TodoRepo;

@Service
public class TodoService {
    @Autowired
    private TodoRepo todoRepo;

    public Todo save(Todo todo) {
        return todoRepo.save(todo);
    }

    public Todo findById(long id) {
      Optional<Todo> todo = todoRepo.findById(id);
      if (todo.isEmpty()) {
        return null;
      }
      return todo.get();
    }

    public List<Todo> findAllByUserId(long id) {
      return todoRepo.findAllByTodoListUserId(id);
    }

    public void deleteById(long id) {
        todoRepo.deleteById(id);
    }
}
