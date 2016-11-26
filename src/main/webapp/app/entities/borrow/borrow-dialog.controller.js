(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BorrowDialogController', BorrowDialogController);

    BorrowDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Borrow'];

    function BorrowDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Borrow) {
        var vm = this;

        vm.borrow = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.borrow.id !== null) {
                Borrow.update(vm.borrow, onSaveSuccess, onSaveError);
            } else {
                Borrow.save(vm.borrow, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aiProjektApp:borrowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.borrowDate = false;
        vm.datePickerOpenStatus.returnDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
