package com.example.scheduler.comparator;

import com.example.scheduler.dto.ToDoResponseDto;

import java.util.Comparator;

public class NameComparator implements Comparator<ToDoResponseDto> {
    @Override
    public int compare(ToDoResponseDto o1, ToDoResponseDto o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
