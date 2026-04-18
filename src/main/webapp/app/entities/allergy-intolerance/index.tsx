import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllergyIntolerance from './allergy-intolerance';
import AllergyIntoleranceDeleteDialog from './allergy-intolerance-delete-dialog';
import AllergyIntoleranceDetail from './allergy-intolerance-detail';
import AllergyIntoleranceUpdate from './allergy-intolerance-update';

const AllergyIntoleranceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AllergyIntolerance />} />
    <Route path="new" element={<AllergyIntoleranceUpdate />} />
    <Route path=":id">
      <Route index element={<AllergyIntoleranceDetail />} />
      <Route path="edit" element={<AllergyIntoleranceUpdate />} />
      <Route path="delete" element={<AllergyIntoleranceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AllergyIntoleranceRoutes;
