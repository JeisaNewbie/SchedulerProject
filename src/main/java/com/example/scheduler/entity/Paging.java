package com.example.scheduler.entity;

import lombok.Getter;

@Getter
public class Paging {
    private final long page;
    private final long pageSize;
    private final long offset;

    public Paging(long page, long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.offset = (page - 1) * pageSize;
    }
}
