import {Workspace} from '@models/workspace';
import {User} from '@models/user';

export interface Invitation {
  id: number;
  email: string;
  workspace?: Workspace;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  createdAt: Date;
  invitee?: User;
  invitedBy?: User;
}
