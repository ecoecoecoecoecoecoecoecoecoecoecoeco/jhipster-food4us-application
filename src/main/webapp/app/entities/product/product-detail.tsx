import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="food4UsApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="food4UsApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="qrCode">
              <Translate contentKey="food4UsApp.product.qrCode">Qr Code</Translate>
            </span>
          </dt>
          <dd>{productEntity.qrCode}</dd>
          <dt>
            <span id="barCode">
              <Translate contentKey="food4UsApp.product.barCode">Bar Code</Translate>
            </span>
          </dt>
          <dd>{productEntity.barCode}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="food4UsApp.product.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.expirationDate ? (
              <TextFormat value={productEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="food4UsApp.product.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{productEntity.quantity}</dd>
          <dt>
            <span id="purchasePrice">
              <Translate contentKey="food4UsApp.product.purchasePrice">Purchase Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.purchasePrice}</dd>
          <dt>
            <span id="purchaseDate">
              <Translate contentKey="food4UsApp.product.purchaseDate">Purchase Date</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.purchaseDate ? (
              <TextFormat value={productEntity.purchaseDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="food4UsApp.product.warehouse">Warehouse</Translate>
          </dt>
          <dd>{productEntity.warehouse ? productEntity.warehouse.name : ''}</dd>
          <dt>
            <Translate contentKey="food4UsApp.product.supplier">Supplier</Translate>
          </dt>
          <dd>{productEntity.supplier ? productEntity.supplier.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/product" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/product/${productEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
