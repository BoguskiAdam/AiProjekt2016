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
