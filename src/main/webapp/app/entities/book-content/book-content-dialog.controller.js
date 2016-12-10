(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookContentDialogController', BookContentDialogController);

    BookContentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookContent'];

    function BookContentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookContent) {
        var vm = this;

        vm.bookContent = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bookContent.id !== null) {
                BookContent.update(vm.bookContent, onSaveSuccess, onSaveError);
            } else {
                BookContent.save(vm.bookContent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aiProjektApp:bookContentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
