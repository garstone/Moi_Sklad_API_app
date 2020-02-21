package com.kamenev.model;

import javax.persistence.*;

@Entity
@Table(name="products")
public class Product {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="model")
    private String model;

    public Product(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getModel().equals(product.getModel());
    }

    @Override
    public int hashCode() {
        return getModel().hashCode();
    }

    @Override
    public String toString() {
        return this.getModel();
    }

    public boolean isUnique() {
        return true;
    }
}
