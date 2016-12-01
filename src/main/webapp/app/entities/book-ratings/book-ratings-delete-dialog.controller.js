(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookRatingsDeleteController',BookRatingsDeleteController);

    BookRatingsDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookRatings'];

    function BookRatingsDeleteController($uibModalInstance, entity, BookRatings) {
        var vm = this;

        vm.bookRatings = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookRatings.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
