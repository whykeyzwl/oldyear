package com.wishes.yearOld.model;

import java.math.BigDecimal;

public class Vip {
    private Integer id;

    private Integer monthCount;

    private BigDecimal price;

   
    public Vip(Integer id, Integer monthCount, BigDecimal price) {
		super();
		this.id = id;
		this.monthCount = monthCount;
		this.price = price;
	}

	public Vip() {
        super();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

 
}