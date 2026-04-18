package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.Product;
import com.eco.food4us.domain.Supplier;
import com.eco.food4us.domain.Warehouse;
import com.eco.food4us.service.dto.ProductDTO;
import com.eco.food4us.service.dto.SupplierDTO;
import com.eco.food4us.service.dto.WarehouseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "warehouseName")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierName")
    ProductDTO toDto(Product s);

    @Named("warehouseName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WarehouseDTO toDtoWarehouseName(Warehouse warehouse);

    @Named("supplierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SupplierDTO toDtoSupplierName(Supplier supplier);
}
