import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Recipe from './recipe';
import RecipeDeleteDialog from './recipe-delete-dialog';
import RecipeDetail from './recipe-detail';
import RecipeUpdate from './recipe-update';

const RecipeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Recipe />} />
    <Route path="new" element={<RecipeUpdate />} />
    <Route path=":id">
      <Route index element={<RecipeDetail />} />
      <Route path="edit" element={<RecipeUpdate />} />
      <Route path="delete" element={<RecipeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecipeRoutes;
