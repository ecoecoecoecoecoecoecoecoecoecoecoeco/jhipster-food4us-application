import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { AllergyIntoleranceType } from 'app/shared/model/enumerations/allergy-intolerance-type.model';
import { mapIdList } from 'app/shared/util/entity-utils';

import { createEntity, getEntity, reset, updateEntity } from './allergy-intolerance.reducer';

export const AllergyIntoleranceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const allergyIntoleranceEntity = useAppSelector(state => state.allergyIntolerance.entity);
  const loading = useAppSelector(state => state.allergyIntolerance.loading);
  const updating = useAppSelector(state => state.allergyIntolerance.updating);
  const updateSuccess = useAppSelector(state => state.allergyIntolerance.updateSuccess);
  const allergyIntoleranceTypeValues = Object.keys(AllergyIntoleranceType);

  const handleClose = () => {
    navigate(`/allergy-intolerance${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...allergyIntoleranceEntity,
      ...values,
      userProfiles: mapIdList(values.userProfiles),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'LACTOSE',
          ...allergyIntoleranceEntity,
          userProfiles: allergyIntoleranceEntity?.userProfiles?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="food4UsApp.allergyIntolerance.home.createOrEditLabel" data-cy="AllergyIntoleranceCreateUpdateHeading">
            <Translate contentKey="food4UsApp.allergyIntolerance.home.createOrEditLabel">Create or edit a AllergyIntolerance</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="allergy-intolerance-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('food4UsApp.allergyIntolerance.type')}
                id="allergy-intolerance-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {allergyIntoleranceTypeValues.map(allergyIntoleranceType => (
                  <option value={allergyIntoleranceType} key={allergyIntoleranceType}>
                    {translate(`food4UsApp.AllergyIntoleranceType.${allergyIntoleranceType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('food4UsApp.allergyIntolerance.description')}
                id="allergy-intolerance-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 300, message: translate('entity.validation.maxlength', { max: 300 }) },
                }}
              />
              <ValidatedField
                label={translate('food4UsApp.allergyIntolerance.userProfile')}
                id="allergy-intolerance-userProfile"
                data-cy="userProfile"
                type="select"
                multiple
                name="userProfiles"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/allergy-intolerance" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AllergyIntoleranceUpdate;
