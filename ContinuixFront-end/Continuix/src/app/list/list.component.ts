import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from '../Services/users.service';
import { Role } from '../role';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { User } from '../user';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  userForm!: FormGroup;
  user: User[] = [];
  showForm: boolean = false;
  openedUserId: number | null = null;
  editMode: boolean = false;
  editedUser: User | null = null;

  roles = [
    { id: 0, name: 'Admin' },
    { id: 1, name: 'MANAGER' },
    { id: 2, name: 'SECURITYAGENT' },
    { id: 3, name: 'TEAMLEADER' },
    { id: 4, name: 'COLLABORATOR' },
  ];
   

  constructor(private fb: FormBuilder, private usersService: UsersService, private router: Router) { }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      supervisor: ['', Validators.required],
      role: [0, Validators.required],
    });

    this.loadUsers();
  }

  loadUsers(): void {
    this.usersService.getUsersList().subscribe({
      next: (users) => {
        console.log('Fetched users:', users);
        this.user = users;
      },
      error: (error) => {
        console.error('Error fetching users:', error);
      },
    });
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
  }

  getRoleName(role: Role): string {
    return role;
  }

  getRoleId(roleName: string): number {
    const role = this.roles.find(r => r.name.toLowerCase() === roleName.toLowerCase());
    return role ? role.id : 0;
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const userData: User = this.userForm.value;

      if (this.editMode && this.editedUser) {
        // Mise à jour de l'utilisateur
        this.usersService.updateUser(this.editedUser.id!, userData).subscribe({
          next: (response) => {
            console.log('User updated successfully:', response);
            this.loadUsers();
            Swal.fire({
              icon: 'success',
              title: 'User updated successfully!',
              confirmButtonText: 'OK',
              confirmButtonColor: '#1BC5BD'
            }).then(() => {
              this.showForm = false;
              this.editMode = false;
              this.editedUser = null;
              this.userForm.reset({ role: 0 });
            });
          },
          error: (error) => {
            console.error('Error updating user:', error);
            Swal.fire({
              icon: 'error',
              title: 'Oops!',
              text: 'Something went wrong while updating the user.',
            });
          }
        });
      } else {
        // Création de nouvel utilisateur
        this.usersService.createUser(userData).subscribe({
          next: (response) => {
            console.log('User created successfully:', response);
            this.loadUsers();
            Swal.fire({
              icon: 'success',
              title: 'User created successfully!',
              confirmButtonText: 'OK',
              confirmButtonColor: '#1BC5BD'
            }).then(() => {
              this.showForm = false;
              this.userForm.reset({ role: 0 });
            });
          },
          error: (error) => {
            console.error('Error creating user:', error);
            Swal.fire({
              icon: 'error',
              title: 'Oops!',
              text: 'Something went wrong while creating the user.',
            });
          }
        });
      }
    } else {
      console.log('Form invalid');
    }
  }

  onReset(): void {
    this.userForm.reset({ role: 0 });
  }

  deleteUser(id: number): void {
    Swal.fire({
      icon: 'warning',
      title: 'Are you sure you want to delete this user?',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete!',
      cancelButtonText: 'No, cancel',
      confirmButtonColor: '#F64E60',
      cancelButtonColor: '#E4E6EF',
    }).then((result) => {
      if (result.isConfirmed) {
        this.usersService.deleteUser(id).subscribe({
          next: () => {
            console.log('User deleted successfully');
            this.loadUsers();
            Swal.fire({
              icon: 'success',
              title: 'User has been deleted successfully!',
              confirmButtonText: 'Ok, got it!',
              confirmButtonColor: '#1BC5BD'
            });
          },
          error: (error) => {
            console.error('Error deleting user:', error);
            Swal.fire({
              icon: 'error',
              title: 'Oops!',
              text: 'Something went wrong while deleting the user.',
            });
          },
        });
      }
    });
  }

  toggleActions(userId: number): void {
    this.openedUserId = this.openedUserId === userId ? null : userId;
  }

  editUser(user: User): void {
    this.editedUser = { ...user };
    this.editMode = true;
    this.showForm = true;

    this.userForm.patchValue({
      username: user.username,
      email: user.email,
      supervisor: user.supervisor,
      role: this.getRoleId(user.role),
    });
  }

  cancelForm(): void {
    Swal.fire({
      icon: 'warning',
      title: 'Are you sure you would like to cancel?',
      showCancelButton: true,
      confirmButtonText: 'Yes, cancel it!',
      cancelButtonText: 'No, return',
      confirmButtonColor: '#1BC5BD',
      cancelButtonColor: '#E4E6EF',
    }).then((result) => {
      if (result.isConfirmed) {
        this.onReset();
        this.editMode = false;
        this.editedUser = null;
        this.showForm = false;
      }
    });
  }
}
