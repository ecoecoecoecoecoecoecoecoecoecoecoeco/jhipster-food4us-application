import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ingredient from './ingredient';
import IngredientDeleteDialog from './ingredient-delete-dialog';
import IngredientDetail from './ingredient-detail';
import IngredientUpdate from './ingredient-update';

const IngredientRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ingredient />} />
    <Route path="new" element={<IngredientUpdate />} />
    <Route path=":id">
      <Route index element={<IngredientDetail />} />
      <Route path="edit" element={<IngredientUpdate />} />
      <Route path="delete" element={<IngredientDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IngredientRoutes;
