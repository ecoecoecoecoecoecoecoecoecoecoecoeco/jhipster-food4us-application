import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './warehouse.reducer';

export const WarehouseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const warehouseEntity = useAppSelector(state => state.warehouse.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="warehouseDetailsHeading">
          <Translate contentKey="food4UsApp.warehouse.detail.title">Warehouse</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{warehouseEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="food4UsApp.warehouse.name">Name</Translate>
            </span>
          </dt>
          <dd>{warehouseEntity.name}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="food4UsApp.warehouse.location">Location</Translate>
            </span>
          </dt>
          <dd>{warehouseEntity.location}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="food4UsApp.warehouse.type">Type</Translate>
            </span>
          </dt>
          <dd>{warehouseEntity.type}</dd>
          <dt>
            <Translate contentKey="food4UsApp.warehouse.userProfile">User Profile</Translate>
          </dt>
          <dd>{warehouseEntity.userProfile ? warehouseEntity.userProfile.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/warehouse" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/warehouse/${warehouseEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WarehouseDetail;
