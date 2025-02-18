import {IUser} from '../core/models/user.model';

export interface AuthState {
  user: IUser | null;
  accessToken: string | null;
  refreshToken: string | null;
  loading: boolean;
  error: string | null;
}

export const initialState: AuthState = {
  user: JSON.parse(localStorage.getItem('userDetails') || 'null'),
  accessToken: localStorage.getItem('access_token'),
  refreshToken: localStorage.getItem('refresh_token'),
  loading: false,
  error: null
};
