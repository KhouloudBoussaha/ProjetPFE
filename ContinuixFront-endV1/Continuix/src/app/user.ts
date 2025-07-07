import { Role } from "./role";

export class User {
 id: number = 0;
  password: string = '';
  username: string = '';
  email: string = '';
  supervisor: number = 0;
  role:Role = Role.COLLABORATOR; // valeur par défaut possible
}