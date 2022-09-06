package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    // get
    public List<TodoEntity> retrieve(String userId) {
        return todoRepository.findByUserId(userId);
    }

    // create
    public List<TodoEntity> create(TodoEntity entity) {
        validate(entity);
        todoRepository.save(entity);
        log.info("Entity ID : {} is saved", entity.getId());
        return todoRepository.findByUserId(entity.getUserId());
    }



    // update
    public List<TodoEntity> update(TodoEntity entity) {
        validate(entity);
        Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            todoRepository.save(todo);
        });
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(TodoEntity entity) {
        validate(entity);
        try {
            todoRepository.delete(entity);
        } catch (Exception e) {
            log.error("ERROR while deleting entity : ", entity.getId(), e);

            // 컨트롤러로 exception 보냄. 데이터베이스 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception 오브젝트를 리턴함
            throw new RuntimeException("error deleting entity + " + entity.getId());
        }
        // 새 Todo 리스트를 가져와 리턴
        return retrieve(entity.getUserId());
    }

    // validate
    private  void validate(TodoEntity entity) {
        if (entity == null) {
            log.warn("entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown User");
        }
    }


}
