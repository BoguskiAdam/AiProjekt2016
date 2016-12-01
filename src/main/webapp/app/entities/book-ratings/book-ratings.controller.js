(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookRatingsController', BookRatingsController);

    BookRatingsController.$inject = ['$scope', '$state', 'BookRatings'];

    function BookRatingsController ($scope, $state, BookRatings) {
        var vm = this;
        
        vm.bookRatings = [];

        loadAll();

        function loadAll() {
            BookRatings.query(function(result) {
                vm.bookRatings = result;
            });
        }
    }
})();
