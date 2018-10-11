package com.chenxing.managesystem.domain.base;

/**
 * 统一定义id的entity基类.
 * <p/>
 *
 * @author liuxing
 */
public abstract class IdEntity {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
