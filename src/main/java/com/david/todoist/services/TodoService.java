package com.david.todoist.services;

import java.util.Collection;
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

    public Todo findById(Long id) {
      return todoRepo.findById(id).get();
    }

    public void delete(long id) {
        todoRepo.deleteById(id);
    }

    public List<Todo> findAll() {
      return todoRepo.findAll();
    }
}
