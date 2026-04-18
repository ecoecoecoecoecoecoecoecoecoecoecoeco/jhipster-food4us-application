import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DietaryEntry from './dietary-entry';
import DietaryEntryDeleteDialog from './dietary-entry-delete-dialog';
import DietaryEntryDetail from './dietary-entry-detail';
import DietaryEntryUpdate from './dietary-entry-update';

const DietaryEntryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DietaryEntry />} />
    <Route path="new" element={<DietaryEntryUpdate />} />
    <Route path=":id">
      <Route index element={<DietaryEntryDetail />} />
      <Route path="edit" element={<DietaryEntryUpdate />} />
      <Route path="delete" element={<DietaryEntryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DietaryEntryRoutes;
