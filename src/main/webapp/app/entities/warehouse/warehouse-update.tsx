import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { WarehouseType } from 'app/shared/model/enumerations/warehouse-type.model';

import { createEntity, getEntity, reset, updateEntity } from './warehouse.reducer';

export const WarehouseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const warehouseEntity = useAppSelector(state => state.warehouse.entity);
  const loading = useAppSelector(state => state.warehouse.loading);
  const updating = useAppSelector(state => state.warehouse.updating);
  const updateSuccess = useAppSelector(state => state.warehouse.updateSuccess);
  const warehouseTypeValues = Object.keys(WarehouseType);

  const handleClose = () => {
    navigate(`/warehouse${location.search}`);
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
      ...warehouseEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
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
          type: 'COLD',
          ...warehouseEntity,
          userProfile: warehouseEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="food4UsApp.warehouse.home.createOrEditLabel" data-cy="WarehouseCreateUpdateHeading">
            <Translate contentKey="food4UsApp.warehouse.home.createOrEditLabel">Create or edit a Warehouse</Translate>
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
                  id="warehouse-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('food4UsApp.warehouse.name')}
                id="warehouse-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('food4UsApp.warehouse.location')}
                id="warehouse-location"
                name="location"
                data-cy="location"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField label={translate('food4UsApp.warehouse.type')} id="warehouse-type" name="type" data-cy="type" type="select">
                {warehouseTypeValues.map(warehouseType => (
                  <option value={warehouseType} key={warehouseType}>
                    {translate(`food4UsApp.WarehouseType.${warehouseType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="warehouse-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('food4UsApp.warehouse.userProfile')}
                type="select"
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/warehouse" replace variant="info">
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

export default WarehouseUpdate;
