package com.chenxing.managesystem.domain.base;

/**
 * 统一定义id的entity基类.
 * <p/>
 *
 * @author liuxing
 */
public abstract class IdEntity {

	protected long id;

	public long getId() {
        return id;
    }

	public void setId(long id) {
        this.id = id;
    }
}
