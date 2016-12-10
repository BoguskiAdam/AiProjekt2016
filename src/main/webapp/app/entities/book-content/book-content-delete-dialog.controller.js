(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookContentDeleteController',BookContentDeleteController);

    BookContentDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookContent'];

    function BookContentDeleteController($uibModalInstance, entity, BookContent) {
        var vm = this;

        vm.bookContent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookContent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
