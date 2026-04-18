package com.david.todoist.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.david.todoist.models.TodoList;

@Repository
public interface ListRepo extends JpaRepository<TodoList, Long> {
    public TodoList findByTitle(String title);
    public TodoList deleteByTitle(String title);
    public List<TodoList> findAllByUserId(Long id);
}
