import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AllergyIntolerance from './allergy-intolerance';
import DietaryEntry from './dietary-entry';
import Ingredient from './ingredient';
import Product from './product';
import Recipe from './recipe';
import Supplier from './supplier';
import UserProfile from './user-profile';
import Warehouse from './warehouse';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/user-profile/*" element={<UserProfile />} />
        <Route path="/warehouse/*" element={<Warehouse />} />
        <Route path="/product/*" element={<Product />} />
        <Route path="/recipe/*" element={<Recipe />} />
        <Route path="/ingredient/*" element={<Ingredient />} />
        <Route path="/supplier/*" element={<Supplier />} />
        <Route path="/dietary-entry/*" element={<DietaryEntry />} />
        <Route path="/allergy-intolerance/*" element={<AllergyIntolerance />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
