package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.SchedulerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    // 유저 email 로 해당유저가 등록한 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByUserInfo(User user) {

        User savedUser = schedulerRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다 = " + user.getEmail()));

        List<ToDo> toDoList = schedulerRepository.findToDoListByUserId(savedUser.getId());
        return toDoList.stream().map(toDo -> new ToDoResponseDto(toDo, savedUser)).toList();
    }

    @Override
    public List<ToDoResponseDto> findScheduleByUserNameAndUserId(String userName, Long userId) {
        User savedUser = schedulerRepository.findUserByUserNameAndUserId(userName, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다 = " + userName + "-" + userId));

        List<ToDo> toDoList = schedulerRepository.findToDoListByUserId(savedUser.getId());
        return toDoList.stream().map(toDo -> new ToDoResponseDto(toDo, savedUser)).toList();
    }

    // 수정일 로 해당 날짜의 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByModifiedDate(String date) {
        return List.of();
    }

    // D-DAY 로 해당 날짜의 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByDay(String date) {
        return List.of();
    }

    // 일정 생성 to_do(registerd_date modified_date work) user (name password email)
    @Override
    @Transactional
    public ScheduleResponseDto saveSchedule(User user, ToDo toDo) {
        // 사용자가 존재하는지 확인
        // 사용자가 존재하지 않으면 사용자 저장

        /*

        데이터 베이스에 유저가 존재해서 유저를 반환하더라도 orElse 는 항상 실행이 보장된다.
        따라서 saveUser(user)가 실행이 되기 때문에 Unique 속성인 email Column 으로 인하여 예외가 발생한다.

        User savedUser = schedulerRepository.findUserByEmailAndPassword(user)
                .orElse(schedulerRepository.saveUser(user));

         */

        // 그렇기 때문에 saveUser(user) 함수의 실행을 Null 체크 이후로 보장해야 한다.
        User savedUser = schedulerRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseGet(() -> schedulerRepository.saveUser(user));

        // 새 일정을 추가
        toDo.setUser(savedUser);
        ToDo savedTodo = schedulerRepository.saveToDo(toDo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ScheduleResponseDto(savedUser, savedTodo);
    }

    @Override
    public ToDoResponseDto updateToDo() {
        return null;
    }

    @Override
    public UserResponseDto updateUser() {
        return null;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void deleteToDoById(Long id) {

    }


}
