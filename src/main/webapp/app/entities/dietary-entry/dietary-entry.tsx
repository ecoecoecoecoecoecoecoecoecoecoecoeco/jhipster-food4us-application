import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import { getEntities } from './dietary-entry.reducer';

export const DietaryEntry = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const dietaryEntryList = useAppSelector(state => state.dietaryEntry.entities);
  const loading = useAppSelector(state => state.dietaryEntry.loading);
  const totalItems = useAppSelector(state => state.dietaryEntry.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="dietary-entry-heading" data-cy="DietaryEntryHeading">
        <Translate contentKey="food4UsApp.dietaryEntry.home.title">Dietary Entries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="food4UsApp.dietaryEntry.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dietary-entry/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="food4UsApp.dietaryEntry.home.createLabel">Create new Dietary Entry</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dietaryEntryList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="food4UsApp.dietaryEntry.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('consumptionDate')}>
                  <Translate contentKey="food4UsApp.dietaryEntry.consumptionDate">Consumption Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('consumptionDate')} />
                </th>
                <th className="hand" onClick={sort('consumptionTime')}>
                  <Translate contentKey="food4UsApp.dietaryEntry.consumptionTime">Consumption Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('consumptionTime')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="food4UsApp.dietaryEntry.quantity">Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th>
                  <Translate contentKey="food4UsApp.dietaryEntry.userProfile">User Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="food4UsApp.dietaryEntry.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dietaryEntryList.map(dietaryEntry => (
                <tr key={`entity-${dietaryEntry.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/dietary-entry/${dietaryEntry.id}`} variant="link" size="sm">
                      {dietaryEntry.id}
                    </Button>
                  </td>
                  <td>
                    {dietaryEntry.consumptionDate ? (
                      <TextFormat type="date" value={dietaryEntry.consumptionDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{dietaryEntry.consumptionTime}</td>
                  <td>{dietaryEntry.quantity}</td>
                  <td>
                    {dietaryEntry.userProfile ? (
                      <Link to={`/user-profile/${dietaryEntry.userProfile.id}`}>{dietaryEntry.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{dietaryEntry.product ? <Link to={`/product/${dietaryEntry.product.id}`}>{dietaryEntry.product.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/dietary-entry/${dietaryEntry.id}`}
                        variant="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/dietary-entry/${dietaryEntry.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        variant="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/dietary-entry/${dietaryEntry.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="food4UsApp.dietaryEntry.home.notFound">No Dietary Entries found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={dietaryEntryList && dietaryEntryList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default DietaryEntry;
