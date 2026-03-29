package com.david.todoist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.todoist.models.TodoList;
import com.david.todoist.repos.ListRepo;

@Service
public class ListService {
    @Autowired
    private ListRepo listRepo;

    public TodoList findByTitle(String title) {
        return listRepo.findByTitle(title);
    }

    public TodoList save(TodoList list) {
        return listRepo.save(list);
    }

    public TodoList deleteByTitle(String title) {
        return listRepo.deleteByTitle(title);
    }
}
