package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("todo")
public class TodoController {


    private final TodoService todoService;

    // get
    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }


    // create
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // DTO -> Entity
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setId(null);

            // temp UserID
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.create(entity);

            // Entity -> List<DTO>
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    // update
    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(temporaryUserId);

        List<TodoEntity> entities = todoService.update(entity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // delete
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // DTO -> Entity
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(temporaryUserId);


            List<TodoEntity> entities = todoService.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // If there is an exception, it returns a message in error instead of DTO
            String err = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(err).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
