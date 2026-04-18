import { SupplierType } from 'app/shared/model/enumerations/supplier-type.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISupplier {
  id?: number;
  name?: string;
  address?: string;
  contact?: string | null;
  website?: string | null;
  supplierType?: keyof typeof SupplierType | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<ISupplier> = {};
