(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BorrowController', BorrowController);

    BorrowController.$inject = ['$scope', '$state', 'Borrow'];

    function BorrowController ($scope, $state, Borrow) {
        var vm = this;
        
        vm.borrows = [];

        loadAll();

        function loadAll() {
            Borrow.query(function(result) {
                vm.borrows = result;
            });
        }
    }
})();
