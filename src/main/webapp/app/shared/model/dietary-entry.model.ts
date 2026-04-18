import dayjs from 'dayjs';

import { IProduct } from 'app/shared/model/product.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IDietaryEntry {
  id?: number;
  consumptionDate?: dayjs.Dayjs;
  consumptionTime?: string;
  quantity?: number;
  userProfile?: IUserProfile;
  product?: IProduct;
}

export const defaultValue: Readonly<IDietaryEntry> = {};
