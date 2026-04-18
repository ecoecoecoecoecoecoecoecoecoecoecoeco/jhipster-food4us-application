package com.eco.food4us.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eco.food4us.domain.Product} entity. This class is used
 * in {@link com.eco.food4us.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter qrCode;

    private StringFilter barCode;

    private LocalDateFilter expirationDate;

    private IntegerFilter quantity;

    private BigDecimalFilter purchasePrice;

    private LocalDateFilter purchaseDate;

    private LongFilter warehouseId;

    private LongFilter supplierId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.qrCode = other.optionalQrCode().map(StringFilter::copy).orElse(null);
        this.barCode = other.optionalBarCode().map(StringFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(LocalDateFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.purchasePrice = other.optionalPurchasePrice().map(BigDecimalFilter::copy).orElse(null);
        this.purchaseDate = other.optionalPurchaseDate().map(LocalDateFilter::copy).orElse(null);
        this.warehouseId = other.optionalWarehouseId().map(LongFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getQrCode() {
        return qrCode;
    }

    public Optional<StringFilter> optionalQrCode() {
        return Optional.ofNullable(qrCode);
    }

    public StringFilter qrCode() {
        if (qrCode == null) {
            setQrCode(new StringFilter());
        }
        return qrCode;
    }

    public void setQrCode(StringFilter qrCode) {
        this.qrCode = qrCode;
    }

    public StringFilter getBarCode() {
        return barCode;
    }

    public Optional<StringFilter> optionalBarCode() {
        return Optional.ofNullable(barCode);
    }

    public StringFilter barCode() {
        if (barCode == null) {
            setBarCode(new StringFilter());
        }
        return barCode;
    }

    public void setBarCode(StringFilter barCode) {
        this.barCode = barCode;
    }

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<LocalDateFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new LocalDateFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPurchasePrice() {
        return purchasePrice;
    }

    public Optional<BigDecimalFilter> optionalPurchasePrice() {
        return Optional.ofNullable(purchasePrice);
    }

    public BigDecimalFilter purchasePrice() {
        if (purchasePrice == null) {
            setPurchasePrice(new BigDecimalFilter());
        }
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimalFilter purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public Optional<LocalDateFilter> optionalPurchaseDate() {
        return Optional.ofNullable(purchaseDate);
    }

    public LocalDateFilter purchaseDate() {
        if (purchaseDate == null) {
            setPurchaseDate(new LocalDateFilter());
        }
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LongFilter getWarehouseId() {
        return warehouseId;
    }

    public Optional<LongFilter> optionalWarehouseId() {
        return Optional.ofNullable(warehouseId);
    }

    public LongFilter warehouseId() {
        if (warehouseId == null) {
            setWarehouseId(new LongFilter());
        }
        return warehouseId;
    }

    public void setWarehouseId(LongFilter warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public Optional<LongFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new LongFilter());
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(qrCode, that.qrCode) &&
            Objects.equals(barCode, that.barCode) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(purchasePrice, that.purchasePrice) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(warehouseId, that.warehouseId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            qrCode,
            barCode,
            expirationDate,
            quantity,
            purchasePrice,
            purchaseDate,
            warehouseId,
            supplierId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalQrCode().map(f -> "qrCode=" + f + ", ").orElse("") +
            optionalBarCode().map(f -> "barCode=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalPurchasePrice().map(f -> "purchasePrice=" + f + ", ").orElse("") +
            optionalPurchaseDate().map(f -> "purchaseDate=" + f + ", ").orElse("") +
            optionalWarehouseId().map(f -> "warehouseId=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
