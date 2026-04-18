package com.eco.food4us.service;

import com.eco.food4us.domain.Warehouse;
import com.eco.food4us.repository.WarehouseRepository;
import com.eco.food4us.service.dto.WarehouseDTO;
import com.eco.food4us.service.mapper.WarehouseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eco.food4us.domain.Warehouse}.
 */
@Service
@Transactional
public class WarehouseService {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseService.class);

    private final WarehouseRepository warehouseRepository;

    private final WarehouseMapper warehouseMapper;

    public WarehouseService(WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public WarehouseDTO save(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to save Warehouse : {}", warehouseDTO);
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(warehouse);
    }

    /**
     * Update a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public WarehouseDTO update(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to update Warehouse : {}", warehouseDTO);
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(warehouse);
    }

    /**
     * Partially update a warehouse.
     *
     * @param warehouseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WarehouseDTO> partialUpdate(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to partially update Warehouse : {}", warehouseDTO);

        return warehouseRepository
            .findById(warehouseDTO.getId())
            .map(existingWarehouse -> {
                warehouseMapper.partialUpdate(existingWarehouse, warehouseDTO);

                return existingWarehouse;
            })
            .map(warehouseRepository::save)
            .map(warehouseMapper::toDto);
    }

    /**
     * Get one warehouse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WarehouseDTO> findOne(Long id) {
        LOG.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id).map(warehouseMapper::toDto);
    }

    /**
     * Delete the warehouse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteById(id);
    }
}
