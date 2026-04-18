import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './supplier.reducer';

export const SupplierDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const supplierEntity = useAppSelector(state => state.supplier.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="supplierDetailsHeading">
          <Translate contentKey="food4UsApp.supplier.detail.title">Supplier</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="food4UsApp.supplier.name">Name</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.name}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="food4UsApp.supplier.address">Address</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.address}</dd>
          <dt>
            <span id="contact">
              <Translate contentKey="food4UsApp.supplier.contact">Contact</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.contact}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="food4UsApp.supplier.website">Website</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.website}</dd>
          <dt>
            <span id="supplierType">
              <Translate contentKey="food4UsApp.supplier.supplierType">Supplier Type</Translate>
            </span>
          </dt>
          <dd>{supplierEntity.supplierType}</dd>
          <dt>
            <Translate contentKey="food4UsApp.supplier.userProfile">User Profile</Translate>
          </dt>
          <dd>{supplierEntity.userProfile ? supplierEntity.userProfile.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/supplier" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/supplier/${supplierEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SupplierDetail;
