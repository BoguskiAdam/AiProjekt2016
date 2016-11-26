(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BookRatingsDialogController', BookRatingsDialogController);

    BookRatingsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookRatings'];

    function BookRatingsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookRatings) {
        var vm = this;

        vm.bookRatings = entity;
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
            if (vm.bookRatings.id !== null) {
                BookRatings.update(vm.bookRatings, onSaveSuccess, onSaveError);
            } else {
                BookRatings.save(vm.bookRatings, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aiProjektApp:bookRatingsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
