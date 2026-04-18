import { IRecipe } from 'app/shared/model/recipe.model';

export interface IIngredient {
  id?: number;
  name?: string;
  quantity?: string;
  recipe?: IRecipe;
}

export const defaultValue: Readonly<IIngredient> = {};
