(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookStoreDetailController', BookStoreDetailController)
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
    BookStoreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Book', 'Borrow'];

    function BookStoreDetailController($scope, $rootScope, $stateParams, previousState, entity, Book, Borrow) {
        var vm = this;

        vm.book = entity;
        vm.previousState = previousState.name;

        vm.rating = 5;
        vm.daysCountOld=1;
        vm.daysCount = 1;
        vm.cost = vm.daysCount * vm.book.price;
        vm.returnDate = countDate(vm.daysCount);
        vm.isBorrowed = false;

        var unsubscribe = $rootScope.$on('aiProjektApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.$watch('vm.daysCount', function(newVal, oldVal){
            vm.cost = vm.daysCount * vm.book.price;
            vm.returnDate = countDate(vm.daysCount)
        });

        $scope.$watch('vm.isBorrowed', function(newVal, oldVal){

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

        $scope.borrowBookFunction = function()
        {
            if(vm.isBorrowed)
            {
                   console.log('juz jest wypozyczona');
                   return;

            }
            var currentDate = new Date();
            currentDate.setDate(currentDate.getDate() + 0);
            var returnDate = new Date();
            returnDate.setDate(returnDate.getDate() + parseInt(vm.daysCount));
            var borrow =
            {
                borrowDate : currentDate,
                fee : vm.cost,
                isbn: vm.book.isbn,
                paid:false,
                returnDate: returnDate,
                userId: ''
            };
            console.log(JSON.stringify(borrow));
            Borrow.save(borrow);
            console.log('zrobione');
            vm.isBorrowed = true;
        };
    }
})();
