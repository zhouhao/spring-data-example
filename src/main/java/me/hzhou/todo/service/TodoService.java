package me.hzhou.todo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import me.hzhou.todo.domain.Todo;
import me.hzhou.todo.domain.User;
import me.hzhou.todo.domain.dto.TodoDto;
import me.hzhou.todo.domain.dto.UserDto;
import me.hzhou.todo.repository.TodoRepository;
import me.hzhou.todo.repository.UserRepository;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Todo save(@NonNull TodoDto todoDto) {
        Optional<User> user = userRepository.findById(todoDto.getUserId());
        Todo out = new Todo();
        if (user.isPresent()) {
            out.setRemindTime(todoDto.getRemindTime());
            out.setContent(todoDto.getContent());
            out.setUser(user.get());
            out.setCreatedTime(LocalDateTime.now());
            out = todoRepository.save(out);
        }
        return out;
    }

    public User save(@NonNull UserDto user) {
        User out = userRepository.findFirstByNameOrPhone(user.getName(), user.getPhone());
        if (out != null) {
            return null;
        }
        out = new User();
        out.setName(user.getName());
        out.setPhone(user.getPhone());
        return userRepository.save(out);
    }

    public User update(@NonNull UserDto user) {
        if (user.getId() == null || user.getId() < 1) {
            return null;
        }
        Optional<User> opt = userRepository.findById(user.getId());
        if (!opt.isPresent()) {
            return null;
        }
        User out = opt.get();
        out.setPhone(user.getPhone());
        out.setName(user.getName());
        return userRepository.save(out);
    }
}
