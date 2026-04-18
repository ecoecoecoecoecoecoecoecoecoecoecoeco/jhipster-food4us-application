import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recipe.reducer';

export const RecipeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recipeEntity = useAppSelector(state => state.recipe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipeDetailsHeading">
          <Translate contentKey="food4UsApp.recipe.detail.title">Recipe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="food4UsApp.recipe.name">Name</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="food4UsApp.recipe.description">Description</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.description}</dd>
          <dt>
            <span id="preparationTime">
              <Translate contentKey="food4UsApp.recipe.preparationTime">Preparation Time</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.preparationTime}</dd>
          <dt>
            <span id="calories">
              <Translate contentKey="food4UsApp.recipe.calories">Calories</Translate>
            </span>
          </dt>
          <dd>{recipeEntity.calories}</dd>
        </dl>
        <Button as={Link as any} to="/recipe" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/recipe/${recipeEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipeDetail;
