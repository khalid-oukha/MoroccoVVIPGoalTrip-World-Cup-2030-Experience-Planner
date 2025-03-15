import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { RolesService } from '../../../../core/services/roles.service';
import { Role } from '../../../../core/models/Role';
import { FormField, GenericFormComponent } from '../../components/generic-form/generic-form.component';
import {NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [TableComponent, GenericFormComponent, NgIf, NgClass, NgForOf],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.scss',
})
export class RolesComponent implements OnInit {
  roles: Role[] = [];
  showForm = false;
  selectedRole: Role | null = null;
  isLoading = false;

  formFields: FormField[] = [
    {
      key: 'name',
      label: 'Role Name',
      type: 'text',
      required: true,
      // Make sure role names start with ROLE_ prefix
      defaultValue: 'ROLE_'
    }
  ];

  columns = [
    { key: 'name', header: 'Role Name' },
    { key: 'actions', header: 'Actions' }
  ];

  constructor(private rolesService: RolesService) {}

  ngOnInit() {
    this.loadRoles();
  }

  loadRoles() {
    this.isLoading = true;
    this.rolesService.findAll().subscribe({
      next: (roles) => {
        this.roles = roles;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading roles:', err);
        this.isLoading = false;
      }
    });
  }

  handleEdit(role: Role) {
    this.selectedRole = { ...role };
    this.showForm = true;
  }

  handleDelete(role: Role) {
    // Don't allow deletion of built-in roles
    if (role.name === 'ROLE_ADMIN' || role.name === 'ROLE_USER') {
      alert('Cannot delete built-in roles');
      return;
    }

    if (role.id && confirm(`Delete role "${role.name}"?`)) {
      this.isLoading = true;
      this.rolesService.delete(role.id).subscribe({
        next: () => {
          this.loadRoles();
        },
        error: (err) => {
          console.error('Delete failed:', err);
          this.isLoading = false;
          alert('Failed to delete role');
        }
      });
    }
  }

  handleFormSubmit(formData: any) {
    this.isLoading = true;

    // Ensure role name starts with ROLE_
    let roleName = formData.name.trim();
    if (!roleName.startsWith('ROLE_')) {
      roleName = 'ROLE_' + roleName;
    }

    // Create a role object
    const roleData: Role = {
      name: roleName
    };

    // If editing, include the ID
    if (this.selectedRole && this.selectedRole.id) {
      roleData.id = this.selectedRole.id;
    }

    const operation = this.selectedRole && this.selectedRole.id
      ? this.rolesService.update(this.selectedRole.id, roleData)
      : this.rolesService.create(roleData);

    operation.subscribe({
      next: () => {
        this.showForm = false;
        this.selectedRole = null;
        this.loadRoles();
      },
      error: (err) => {
        console.error('Save failed:', err);
        this.isLoading = false;
        alert('Failed to save role');
      }
    });
  }

  handleFormCancel() {
    this.showForm = false;
    this.selectedRole = null;
  }

  // Helper to determine if a role is a built-in role
  isBuiltInRole(role: Role): boolean {
    return role.name === 'ROLE_ADMIN' || role.name === 'ROLE_USER';
  }
}
