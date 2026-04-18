import { AllergyIntoleranceType } from 'app/shared/model/enumerations/allergy-intolerance-type.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IAllergyIntolerance {
  id?: number;
  type?: keyof typeof AllergyIntoleranceType;
  description?: string | null;
  userProfiles?: IUserProfile[] | null;
}

export const defaultValue: Readonly<IAllergyIntolerance> = {};
