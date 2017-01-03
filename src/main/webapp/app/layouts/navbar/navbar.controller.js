(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','User'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, User) {
        var vm = this;

        vm.userId = 'user-2';

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;


        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        Principal.identity(true)
                .then(function(response)
                {
                    vm.userId = response.login;
                });

        function login() {
            collapseNavbar();
            var a = LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
