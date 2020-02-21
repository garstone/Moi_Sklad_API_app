package com.kamenev.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@Table(name="store")
public class Store {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="product_id")
    private long productId;

    private String model;

    @Column(name="quantity")
    private int quantity;

    @Column(name="price")
    private int price;

    @Column(name="date")
    private String date;

    @Column(name="updated_at")
    private String updatedAt;

    public Store(long productId, int quantity, int price, String date) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.updatedAt = date;
    }

    public Store(long productId, int quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.date = new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime());
        this.updatedAt = this.date;
    }

    public String getModel() {
        return model;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long id) {
        this.productId = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
