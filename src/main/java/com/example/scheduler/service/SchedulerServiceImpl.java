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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    // 사용자 명 으로 해당 날짜의 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByUserNameAndUserId(String userName, Long userId) {
        User savedUser = schedulerRepository.findUserByUserNameAndUserId(userName, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다 = " + userName + "-" + userId));

        List<ToDo> toDoList = schedulerRepository.findToDoListByUserId(savedUser.getId());

        return toDoList.stream().map(toDo -> new ToDoResponseDto(toDo, savedUser)).toList();
    }

    // 수정일 로 해당 날짜의 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByModifiedDate(LocalDate date) {

        return schedulerRepository.findToDoListByModifiedDate(date)
                .stream()
                .map(toDo -> {
                    User savedUser = schedulerRepository.findUserByUserId(toDo.getUserId())
                            .orElseGet(() -> User.builder().name("임시 사용자")
                                    .email("example@example.com")
                                    .id(null)
                                    .build());
                    return new ToDoResponseDto(toDo, savedUser);
                })
                .collect(Collectors.toList());
    }

    // D-DAY 로 해당 날짜의 모든 일정 조회
    @Override
    public List<ToDoResponseDto> findScheduleByTheDay(LocalDate date) {

        /*
        * toList() 대신 collect(Collectors.toList()) 를 사용한 이유
        * toList() 는 불변 객체를 반환한다 (The returned List is unmodifiable).
        * client 코드에서 RequestParams 값 (order = asc or desc)에 따라서 list 를 재정렬 해야한다.
        * 따라서 collect(Collectors.toList()) 로 stream 값을 반환하거나
        * client 코드에서 new ArrayList<>(schedulerService.findScheduleByTheDay(date)) 로 코드를 수정해야한다.
        * stream 에서 mutable list 를 반환하는게 가독성이 더 좋아보여서 선택했다.
        */

        return schedulerRepository.findToDoListByTheDay(date)
                .stream()
                .map(toDo -> {
                    User savedUser = schedulerRepository.findUserByUserId(toDo.getUserId())
                            .orElseGet(() -> User.builder()
                                    .id(null)
                                    .name("임시 사용자")
                                    .email("example@example.com")
                                    .build());
                    return new ToDoResponseDto(toDo, savedUser);
                })
                .collect(Collectors.toList());
    }

    // 일정 생성 to_do(registered_date modified_date work) user (name password email)
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
        User savedUser = schedulerRepository.findUserByUserIdAndPassword(user.getId(), user.getPassword())
                .orElseGet(() -> schedulerRepository.saveUser(user));

        // 새 일정을 추가
        toDo.setUser(savedUser);
        ToDo savedTodo = schedulerRepository.saveToDo(toDo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ScheduleResponseDto(savedUser, savedTodo);
    }

    @Override
    public UserResponseDto updateUser(User user) {

        schedulerRepository.findUserByUserIdAndPassword(user.getId(), user.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자의 ID 혹은 비밀번호가 일치하지 않습니다."));

        int updatedRow = schedulerRepository.updateUser(user);

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User updatedUser = schedulerRepository.findUserByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다 = " + user.getName() + "-" + user.getId()));

        return new UserResponseDto(updatedUser);
    }

    @Override
    public ToDoResponseDto updateToDo(User user, ToDo toDo) {

        User savedUser = schedulerRepository.findUserByUserIdAndPassword(user.getId(), user.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다 = " + user.getName() + "-" + user.getId()));

        schedulerRepository.findToDoById(toDo.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다 = " + toDo.getId()));

        int updatedRow = schedulerRepository.updateToDo(toDo);

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        ToDo updatedToDo = schedulerRepository.findToDoById(toDo.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다 = " + toDo.getId()));

        return new ToDoResponseDto(updatedToDo, savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {

        schedulerRepository.findUserByUserIdAndPassword(user.getId(), user.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자의 ID 혹은 비밀번호가 일치하지 않습니다."));

        schedulerRepository.deleteToDoListByUserId(user.getId());

        schedulerRepository.deleteUser(user.getId());
    }

    @Override
    public void deleteToDo(User user, Long toDoId) {

        schedulerRepository.findUserByUserIdAndPassword(user.getId(), user.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자의 ID 혹은 비밀번호가 일치하지 않습니다."));

        int deletedRow = schedulerRepository.deleteToDo(toDoId);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다 = " + toDoId);
        }
    }


}
