// user-node.ts
import { TreeNode } from 'primeng/api';

export interface UserNode extends TreeNode {
  id: number;
  username: string;
  role: string;
  accepted: boolean;
  subordinates?: UserNode[]; // Used in API response, will be mapped to children
  data?: {
    name: string;
    role: string;
    image?: string; // Added for image support
  };
  children?: UserNode[]; // Used by PrimeNG for rendering
}