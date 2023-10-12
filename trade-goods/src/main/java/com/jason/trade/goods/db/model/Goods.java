package com.jason.trade.goods.db.model;

import java.util.Date;

public class Goods {
    private Long id;

    private String title;

    private String number;

    private String brand;

    private String image;

    private String description;

    private Integer price;

    private Integer status;

    private String keywords;

    private String category;

    private Integer availableStock;

    private Integer lockStock;

    private Integer saleNum;

    private Date createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public void setLockStock(Integer lockStock) {
        this.lockStock = lockStock;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}