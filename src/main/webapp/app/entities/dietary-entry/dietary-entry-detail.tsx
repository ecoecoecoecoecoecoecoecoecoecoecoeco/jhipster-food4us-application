import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dietary-entry.reducer';

export const DietaryEntryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dietaryEntryEntity = useAppSelector(state => state.dietaryEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dietaryEntryDetailsHeading">
          <Translate contentKey="food4UsApp.dietaryEntry.detail.title">DietaryEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dietaryEntryEntity.id}</dd>
          <dt>
            <span id="consumptionDate">
              <Translate contentKey="food4UsApp.dietaryEntry.consumptionDate">Consumption Date</Translate>
            </span>
          </dt>
          <dd>
            {dietaryEntryEntity.consumptionDate ? (
              <TextFormat value={dietaryEntryEntity.consumptionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="consumptionTime">
              <Translate contentKey="food4UsApp.dietaryEntry.consumptionTime">Consumption Time</Translate>
            </span>
          </dt>
          <dd>{dietaryEntryEntity.consumptionTime}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="food4UsApp.dietaryEntry.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{dietaryEntryEntity.quantity}</dd>
          <dt>
            <Translate contentKey="food4UsApp.dietaryEntry.userProfile">User Profile</Translate>
          </dt>
          <dd>{dietaryEntryEntity.userProfile ? dietaryEntryEntity.userProfile.id : ''}</dd>
          <dt>
            <Translate contentKey="food4UsApp.dietaryEntry.product">Product</Translate>
          </dt>
          <dd>{dietaryEntryEntity.product ? dietaryEntryEntity.product.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/dietary-entry" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/dietary-entry/${dietaryEntryEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DietaryEntryDetail;
