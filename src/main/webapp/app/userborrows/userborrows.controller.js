(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('UserBorrowsController', UserBorrowsController);

    UserBorrowsController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'UserBorrows'];

    function UserBorrowsController ($scope, $state, ParseLinks, AlertService, UserBorrows) {
        var vm = this;
        vm.test = 'Nie zaladowane';
        loadAll();
        console.log('loadAll - Przed');

        function loadAll () {
                    UserBorrows.query({
                    }, onSuccess, onError);
                    function onSuccess(data, headers) {
                        console.log('loadAll - onSuccess');
                        console.log(JSON.stringify(data));
//                        vm.links = ParseLinks.parse(headers('link'));
                        vm.totalItems = headers('X-Total-Count');
                        vm.queryCount = vm.totalItems;
                        vm.borrows = data;
                        console.log('loadAll - onSuccess Complete');
                    }
                    function onError(error) {
                        console.log('loadAll - onError');
                        AlertService.error(error.data.message);
                    }
            }
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
