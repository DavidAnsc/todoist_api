package com.david.todoist.repos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.david.todoist.models.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {
    public Collection<Todo> findAllByTitle(String title);
    public void deleteByTodoList_Id(Long todoListId);
    public List<Todo> findAllByTodoListUserId(Long id);
}
