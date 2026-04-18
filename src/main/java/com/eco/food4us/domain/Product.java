package com.eco.food4us.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Size(max = 50)
    @Column(name = "qr_code", length = 50)
    private String qrCode;

    @Size(max = 50)
    @Column(name = "bar_code", length = 50)
    private String barCode;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @DecimalMin(value = "0")
    @Column(name = "purchase_price", precision = 21, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public Product qrCode(String qrCode) {
        this.setQrCode(qrCode);
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public Product barCode(String barCode) {
        this.setBarCode(barCode);
        return this;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public Product expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Product quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return this.purchasePrice;
    }

    public Product purchasePrice(BigDecimal purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Product purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product warehouse(Warehouse warehouse) {
        this.setWarehouse(warehouse);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            ", barCode='" + getBarCode() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", quantity=" + getQuantity() +
            ", purchasePrice=" + getPurchasePrice() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            "}";
    }
}
