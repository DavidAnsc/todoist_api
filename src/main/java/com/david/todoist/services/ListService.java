package com.david.todoist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.david.todoist.models.TodoList;
import com.david.todoist.repos.ListRepo;
import com.david.todoist.repos.TodoRepo;

@Service
@Transactional
public class ListService {
    @Autowired
    private ListRepo listRepo;
  @Autowired
  private TodoRepo todoRepo;

    public TodoList findByTitle(String title) {
        return listRepo.findByTitle(title);
    }

    public List<TodoList> findAll() {
      return listRepo.findAll();
    }

    public TodoList save(TodoList list) {
        return listRepo.save(list);
    }

    public void deleteByTitle(String title) {
        listRepo.deleteByTitle(title);
    }

    public void deleteById(Long id) {
      todoRepo.deleteByTodoList_Id(id);
      listRepo.deleteById(id);
    }
}
