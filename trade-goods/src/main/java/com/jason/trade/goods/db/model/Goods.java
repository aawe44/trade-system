package com.jason.trade.goods.db.model;

import java.util.Date;

public class Goods {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.id
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.title
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.number
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.brand
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String brand;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.image
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String image;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.description
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.price
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Integer price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.status
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.keywords
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String keywords;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.category
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private String category;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.available_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Integer availableStock;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.lock_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Integer lockStock;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.sale_num
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Integer saleNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods.create_time
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.id
     *
     * @return the value of goods.id
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.id
     *
     * @param id the value for goods.id
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.title
     *
     * @return the value of goods.title
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.title
     *
     * @param title the value for goods.title
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.number
     *
     * @return the value of goods.number
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.number
     *
     * @param number the value for goods.number
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.brand
     *
     * @return the value of goods.brand
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getBrand() {
        return brand;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.brand
     *
     * @param brand the value for goods.brand
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.image
     *
     * @return the value of goods.image
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.image
     *
     * @param image the value for goods.image
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.description
     *
     * @return the value of goods.description
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.description
     *
     * @param description the value for goods.description
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.price
     *
     * @return the value of goods.price
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.price
     *
     * @param price the value for goods.price
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.status
     *
     * @return the value of goods.status
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.status
     *
     * @param status the value for goods.status
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.keywords
     *
     * @return the value of goods.keywords
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.keywords
     *
     * @param keywords the value for goods.keywords
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.category
     *
     * @return the value of goods.category
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public String getCategory() {
        return category;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.category
     *
     * @param category the value for goods.category
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.available_stock
     *
     * @return the value of goods.available_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Integer getAvailableStock() {
        return availableStock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.available_stock
     *
     * @param availableStock the value for goods.available_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.lock_stock
     *
     * @return the value of goods.lock_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Integer getLockStock() {
        return lockStock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.lock_stock
     *
     * @param lockStock the value for goods.lock_stock
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setLockStock(Integer lockStock) {
        this.lockStock = lockStock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.sale_num
     *
     * @return the value of goods.sale_num
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Integer getSaleNum() {
        return saleNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.sale_num
     *
     * @param saleNum the value for goods.sale_num
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods.create_time
     *
     * @return the value of goods.create_time
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods.create_time
     *
     * @param createTime the value for goods.create_time
     *
     * @mbg.generated Fri Oct 13 23:36:56 EDT 2023
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}