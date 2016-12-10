(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookContentDetailController', BookContentDetailController);

    BookContentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BookContent'];

    function BookContentDetailController($scope, $rootScope, $stateParams, previousState, entity, BookContent) {
        var vm = this;

        vm.bookContent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aiProjektApp:bookContentUpdate', function(event, result) {
            vm.bookContent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
