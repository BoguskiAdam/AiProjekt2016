(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookRatingsDetailController', BookRatingsDetailController);

    BookRatingsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BookRatings'];

    function BookRatingsDetailController($scope, $rootScope, $stateParams, previousState, entity, BookRatings) {
        var vm = this;

        vm.bookRatings = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aiProjektApp:bookRatingsUpdate', function(event, result) {
            vm.bookRatings = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
