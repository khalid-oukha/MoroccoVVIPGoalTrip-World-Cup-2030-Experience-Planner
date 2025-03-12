import { MenuItem } from '../models/menu.model';

export class Menu {
  public static pages: MenuItem[] = [
    {
      group: 'Management',
      separator: true,
      items: [
        {
          icon: 'assets/icons/heroicons/outline/users.svg',
          label: 'Users',
          route: '/users',
        },
        {
          icon: 'assets/icons/heroicons/outline/eye.svg',
          label: 'Activities',
          route: '/dashboard/activities'
        },
        {
          icon: 'assets/icons/heroicons/outline/folder.svg',
          label: 'Categories',
          route: '/dashboard/categories'
        },
        {
          icon: 'assets/icons/heroicons/outline/bookmark.svg',
          label: 'Articles',
          route: '/dashboard/articles'
        },
        {
          icon: 'assets/icons/heroicons/outline/sun.svg',
          label: 'Cities',
          route: '/dashboard/cities'
        }
      ],
    },
    {
      group: 'Base',
      separator: false,
      items: [
        {
          icon: 'assets/icons/heroicons/outline/chart-pie.svg',
          label: 'Dashboard',
          route: '/dashboard'
        },
        {
          icon: 'assets/icons/heroicons/outline/lock-closed.svg',
          label: 'Auth',
          route: '/auth',
          children: [
            { label: 'Sign up', route: '/auth/sign-up' },
            { label: 'Sign in', route: '/auth/sign-in' },
          ],
        },
        {
          icon: 'assets/icons/heroicons/outline/exclamation-triangle.svg',
          label: 'Errors',
          route: '/errors',
          children: [
            { label: '404', route: '/errors/404' },
            { label: '500', route: '/errors/500' },
          ],
        },
      ],
    }
  ];
}
