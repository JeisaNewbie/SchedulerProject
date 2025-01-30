package com.example.scheduler.comparator;

import com.example.scheduler.dto.ToDoResponseDto;

import java.util.Comparator;

public class ModifiedDateComparator implements Comparator<ToDoResponseDto> {

    @Override
    public int compare(ToDoResponseDto o1, ToDoResponseDto o2) {
        return o1.getModifiedDate().compareTo(o2.getModifiedDate());
    }

}
