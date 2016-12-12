(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookDetailController', BookDetailController)
        .directive('toNumber', function () {
             return {
                 require: 'ngModel',
                 link: function (scope, elem, attrs, ctrl) {
                     ctrl.$parsers.push(function (value) {
                         return parseFloat(value || '');
                     });
                 }
             };
         });
    BookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Book'];

    function BookDetailController($scope, $rootScope, $stateParams, previousState, entity, Book) {
        var vm = this;

        vm.book = entity;
        vm.previousState = previousState.name;

        vm.rating = 5;
        vm.daysCountOld=1;
        vm.daysCount = 1;
        vm.cost = vm.daysCount * vm.book.price;
        vm.returnDate = countDate(vm.daysCount);

        var unsubscribe = $rootScope.$on('aiProjektApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.$watch('vm.daysCount', function(newVal, oldVal){
            vm.cost = vm.daysCount * vm.book.price;
            vm.returnDate = countDate(vm.daysCount)
        });

        function countDate(days)
        {
            var result = new Date();
            result.setDate(result.getDate() + parseInt(days));
            var dd = result.getDate();
            var mm = result.getMonth() + 1;
            var y = result.getFullYear();
            var someFormattedDate = dd + ' - '+ mm + ' - '+ y;
            return someFormattedDate;
        };
    }
})();
