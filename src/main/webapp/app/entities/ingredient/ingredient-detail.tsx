import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingredient.reducer';

export const IngredientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingredientEntity = useAppSelector(state => state.ingredient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientDetailsHeading">
          <Translate contentKey="food4UsApp.ingredient.detail.title">Ingredient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="food4UsApp.ingredient.name">Name</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.name}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="food4UsApp.ingredient.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.quantity}</dd>
          <dt>
            <Translate contentKey="food4UsApp.ingredient.recipe">Recipe</Translate>
          </dt>
          <dd>{ingredientEntity.recipe ? ingredientEntity.recipe.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/ingredient" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/ingredient/${ingredientEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngredientDetail;
