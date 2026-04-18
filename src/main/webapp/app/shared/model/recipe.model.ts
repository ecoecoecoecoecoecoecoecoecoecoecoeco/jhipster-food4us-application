export interface IRecipe {
  id?: number;
  name?: string;
  description?: string | null;
  preparationTime?: number | null;
  calories?: number | null;
}

export const defaultValue: Readonly<IRecipe> = {};
