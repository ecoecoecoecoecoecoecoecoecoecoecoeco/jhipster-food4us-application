import allergyIntolerance from 'app/entities/allergy-intolerance/allergy-intolerance.reducer';
import dietaryEntry from 'app/entities/dietary-entry/dietary-entry.reducer';
import ingredient from 'app/entities/ingredient/ingredient.reducer';
import product from 'app/entities/product/product.reducer';
import recipe from 'app/entities/recipe/recipe.reducer';
import supplier from 'app/entities/supplier/supplier.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import warehouse from 'app/entities/warehouse/warehouse.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  warehouse,
  product,
  recipe,
  ingredient,
  supplier,
  dietaryEntry,
  allergyIntolerance,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
