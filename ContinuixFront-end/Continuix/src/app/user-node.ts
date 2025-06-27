import { TreeNode } from 'primeng/api';

export interface UserNode extends TreeNode {
  id: number;
  username: string;
  role: string;
  accepted: boolean;
  subordinates?: UserNode[];
  data?: {
    name: string;
    role: string;
  };
  children?: UserNode[];
}