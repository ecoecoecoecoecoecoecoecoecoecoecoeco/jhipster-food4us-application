import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './allergy-intolerance.reducer';

export const AllergyIntoleranceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const allergyIntoleranceEntity = useAppSelector(state => state.allergyIntolerance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="allergyIntoleranceDetailsHeading">
          <Translate contentKey="food4UsApp.allergyIntolerance.detail.title">AllergyIntolerance</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{allergyIntoleranceEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="food4UsApp.allergyIntolerance.type">Type</Translate>
            </span>
          </dt>
          <dd>{allergyIntoleranceEntity.type}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="food4UsApp.allergyIntolerance.description">Description</Translate>
            </span>
          </dt>
          <dd>{allergyIntoleranceEntity.description}</dd>
          <dt>
            <Translate contentKey="food4UsApp.allergyIntolerance.userProfile">User Profile</Translate>
          </dt>
          <dd>
            {allergyIntoleranceEntity.userProfiles
              ? allergyIntoleranceEntity.userProfiles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {allergyIntoleranceEntity.userProfiles && i === allergyIntoleranceEntity.userProfiles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/allergy-intolerance" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/allergy-intolerance/${allergyIntoleranceEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AllergyIntoleranceDetail;
