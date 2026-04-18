import { WarehouseType } from 'app/shared/model/enumerations/warehouse-type.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IWarehouse {
  id?: number;
  name?: string;
  location?: string | null;
  type?: keyof typeof WarehouseType;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IWarehouse> = {};
