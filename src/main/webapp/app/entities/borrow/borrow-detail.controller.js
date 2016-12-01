(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BorrowDetailController', BorrowDetailController);

    BorrowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Borrow'];

    function BorrowDetailController($scope, $rootScope, $stateParams, previousState, entity, Borrow) {
        var vm = this;

        vm.borrow = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aiProjektApp:borrowUpdate', function(event, result) {
            vm.borrow = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
