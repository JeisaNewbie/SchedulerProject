package com.example.scheduler.comparator;

import com.example.scheduler.dto.ToDoResponseDto;

import java.util.Comparator;

public class TheDayComparator implements Comparator<ToDoResponseDto> {
    @Override
    public int compare(ToDoResponseDto o1, ToDoResponseDto o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
