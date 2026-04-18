import { IAllergyIntolerance } from 'app/shared/model/allergy-intolerance.model';
import { UserType } from 'app/shared/model/enumerations/user-type.model';
import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: number;
  userType?: keyof typeof UserType;
  internalUser?: IUser | null;
  allergyIntolerances?: IAllergyIntolerance[] | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
