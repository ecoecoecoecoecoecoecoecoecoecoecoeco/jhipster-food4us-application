import dayjs from 'dayjs';

import { ISupplier } from 'app/shared/model/supplier.model';
import { IWarehouse } from 'app/shared/model/warehouse.model';

export interface IProduct {
  id?: number;
  name?: string;
  qrCode?: string | null;
  barCode?: string | null;
  expirationDate?: dayjs.Dayjs | null;
  quantity?: number;
  purchasePrice?: number | null;
  purchaseDate?: dayjs.Dayjs | null;
  warehouse?: IWarehouse;
  supplier?: ISupplier | null;
}

export const defaultValue: Readonly<IProduct> = {};
