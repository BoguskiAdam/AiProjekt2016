(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('UserBorrowsController', UserBorrowsController);

    UserBorrowsController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'UserBorrows', 'entities'];

    function UserBorrowsController ($scope, $state, ParseLinks, AlertService, UserBorrows, entities) {
        var vm = this;
        vm.borrows = entities;
        }
    })();


    /*.$inject = ['Auth', 'Principal', '$http', '$state'];

    function UserBorrowsController(Auth, Principal, $http, $state) {
        var vm = this;
        vm.title = 'Wypozyczone ksiazki:';

        vm.loginUser = '';

        vm.settingsAccount = null;

        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

    }
})();
*/
