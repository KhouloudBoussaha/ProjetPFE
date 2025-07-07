import { Component, OnInit } from '@angular/core';
import { GroupService } from '../group.service';
import { UsersService } from '../Services/users.service';
import { Group } from '../models/group';
import { User } from '../user';
import { GroupRequestDTO } from '../models/group-request-dto';

@Component({
  selector: 'app-group-management',
  templateUrl: './group-management.component.html',
  styleUrls: ['./group-management.component.css']
})
export class GroupManagementComponent implements OnInit {
  groups: Group[] = [];
  users: User[] = [];
  selectedGroup: Group | null = null;
  selectedUserId: number | null = null;
  newGroupName: string = '';

  constructor(
    private groupService: GroupService,
    private userService: UsersService
  ) {}

  ngOnInit(): void {
    this.loadGroups();
    this.loadUsers();
  }

  // Charge tous les groupes depuis le backend
  loadGroups(): void {
    this.groupService.getAllGroups().subscribe({
      next: (data) => {
        this.groups = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des groupes', err);
      }
    });
  }

  // Charge tous les utilisateurs depuis le backend
  loadUsers(): void {
    this.userService.getUsersList().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
      }
    });
  }

  // Sélectionne un groupe (copie pour éviter effets de bord)
  selectGroup(group: Group): void {
    this.selectedGroup = JSON.parse(JSON.stringify(group));
    this.selectedUserId = null;
  }

  // Désélectionne le groupe
  deselectGroup(): void {
    this.selectedGroup = null;
    this.selectedUserId = null;
  }

  // Ajoute un utilisateur au groupe sélectionné (localement)
  addUserToGroup(): void {
    if (this.selectedGroup && this.selectedUserId) {
      const userToAdd = this.users.find(u => u.id === this.selectedUserId);
      if (userToAdd && !this.selectedGroup.members.some(u => u.id === this.selectedUserId)) {
        this.selectedGroup.members.push(userToAdd);
      }
      this.selectedUserId = null;
    }
  }

  // Retire un utilisateur du groupe sélectionné (localement)
  removeUserFromGroup(userId: number): void {
    if (this.selectedGroup) {
      this.selectedGroup.members = this.selectedGroup.members.filter(u => u.id !== userId);
    }
  }

  // Sauvegarde les modifications du groupe (création ou mise à jour)
saveGroupChanges(): void {
  if (!this.selectedGroup) return;

  const groupDto: GroupRequestDTO = {
    id: this.selectedGroup.id,
    name: this.selectedGroup.name,
    memberIds: this.selectedGroup.members.map(u => u.id!)
  };

  if (groupDto.id) {
    // Mise à jour
    this.groupService.updateGroup(groupDto).subscribe({
      next: () => {
        this.loadGroups();
        this.deselectGroup();
      },
      error: (err) => console.error('Erreur lors de la mise à jour du groupe', err)
    });
  } else {
    // Création
    this.groupService.createGroup(groupDto).subscribe({
      next: () => {
        this.loadGroups();
        this.deselectGroup();
      },
      error: (err) => console.error('Erreur lors de la création du groupe', err)
    });
  }
}

  // Crée un groupe depuis le champ de saisie
  createGroup(): void {
    if (!this.newGroupName.trim()) return;

    const groupDto: GroupRequestDTO = {
      name: this.newGroupName.trim(),
      memberIds: []
    };

    this.groupService.createGroup(groupDto).subscribe({
      next: () => {
        this.newGroupName = '';
        this.loadGroups();
      },
      error: (err) => console.error('Erreur lors de la création du groupe', err)
    });
  }

  // Supprime un groupe par son id
  deleteGroup(groupId?: number): void {
    if (!groupId) return;

    if (confirm('Are you sure you want to delete this group?')) {
      this.groupService.deleteGroup(groupId).subscribe({
        next: () => this.loadGroups(),
        error: (err) => console.error('Erreur lors de la suppression du groupe', err)
      });
    }
  }

  // Edite le nom d’un groupe directement via prompt (optionnel)
  editGroup(group: Group): void {
    const newName = prompt('Modify group name:', group.name);
    if (newName && newName.trim() !== '') {
      group.name = newName.trim();
      this.saveGroupChanges();
    }
  }
}
